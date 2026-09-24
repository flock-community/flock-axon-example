package community.flock.examples.axon.webshop.app.basket.command

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.merge
import arrow.core.nonEmptyListOf
import arrow.core.raise.either
import arrow.core.raise.zipOrAccumulate
import community.flock.examples.axon.webshop.api.endpoint.DeleteItem
import community.flock.examples.axon.webshop.api.endpoint.GetNewBasket
import community.flock.examples.axon.webshop.api.endpoint.PostItem
import community.flock.examples.axon.webshop.api.model.CommandProblem
import community.flock.examples.axon.webshop.app.basket.command.BasketIdProducer.produce
import community.flock.examples.axon.webshop.app.basket.command.ItemConsumer.consume
import community.flock.examples.axon.webshop.app.common.SingleValidationProblem
import community.flock.examples.axon.webshop.app.common.ValidationProblem
import community.flock.examples.axon.webshop.app.common.plus
import community.flock.examples.axon.webshop.domain.basket.AddItemCommand
import community.flock.examples.axon.webshop.domain.basket.BasketCommand
import community.flock.examples.axon.webshop.domain.basket.CreateBasketCommand
import community.flock.examples.axon.webshop.domain.basket.RemoveItemCommand
import community.flock.examples.axon.webshop.domain.shared.BasketId
import community.flock.examples.axon.webshop.domain.shared.DomainException
import community.flock.examples.axon.webshop.domain.shared.ItemId
import org.axonframework.extension.kotlin.messaging.sendAndWait
import org.axonframework.messaging.commandhandling.CommandExecutionException
import org.axonframework.messaging.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.RestController

private interface CommandApi :
    GetNewBasket.Handler,
    PostItem.Handler,
    DeleteItem.Handler

@RestController
class CommandController(
    val commandGateway: CommandGateway,
) : CommandApi {
    override suspend fun getNewBasket(request: GetNewBasket.Request): GetNewBasket.Response<*> =
        CreateBasketCommand(basketId = BasketId())
            .let { commandGateway.sendAndWait<BasketId>(it) }
            .produce()
            .let { GetNewBasket.Response200(it) }

    override suspend fun postItem(request: PostItem.Request): PostItem.Response<*> =
        either {
            zipOrAccumulate(
                { BasketId(request.path.basketId).mapLeft { SingleValidationProblem(it.reason) }.bind() },
                { request.body.consume() },
                ::AddItemCommand,
            ).let { dispatch(it).bind() }
        }.map { PostItem.Response200 }
            .mapLeft {
                it
                    .reduce(ValidationProblem::plus)
                    .reason
                    .let(::CommandProblem)
                    .let(PostItem::Response400)
            }.merge()

    override suspend fun deleteItem(request: DeleteItem.Request): DeleteItem.Response<*> =
        either {
            zipOrAccumulate(
                { BasketId(request.path.basketId).mapLeft { SingleValidationProblem(it.reason) }.bind() },
                { ItemId(request.path.itemId).mapLeft { SingleValidationProblem(it.reason) }.bind() },
                ::RemoveItemCommand,
            ).let { dispatch(it).bind() }
        }.map { DeleteItem.Response200 }
            .mapLeft {
                it
                    .reduce(ValidationProblem::plus)
                    .reason
                    .let(::CommandProblem)
                    .let(DeleteItem::Response400)
            }.merge()

    /** Dispatches a command; a command the decider rejects is a validation problem for the caller. */
    private fun dispatch(command: BasketCommand): Either<NonEmptyList<SingleValidationProblem>, BasketId> =
        Either
            .catch { commandGateway.sendAndWait<BasketId>(command) }
            .mapLeft { nonEmptyListOf(it.rejection() ?: throw it) }

    private fun Throwable.rejection(): SingleValidationProblem? =
        generateSequence(this) { it.cause }
            .firstNotNullOfOrNull { cause ->
                when (cause) {
                    is DomainException -> cause.problem.reason
                    is CommandExecutionException -> cause.message
                    else -> null
                }
            }?.let(::SingleValidationProblem)
}
