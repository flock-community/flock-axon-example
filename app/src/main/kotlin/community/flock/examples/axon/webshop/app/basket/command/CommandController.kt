package community.flock.examples.axon.webshop.app.basket.command

import arrow.core.merge
import arrow.core.raise.either
import arrow.core.raise.zipOrAccumulate
import community.flock.examples.axon.webshop.api.endpoint.DeleteItem
import community.flock.examples.axon.webshop.api.endpoint.GetNewBasket
import community.flock.examples.axon.webshop.api.endpoint.PostItem
import community.flock.examples.axon.webshop.api.model.CommandProblem
import community.flock.examples.axon.webshop.api.model.UUID
import community.flock.examples.axon.webshop.app.basket.command.ItemConsumer.consume
import community.flock.examples.axon.webshop.app.basket.shared.BasketId
import community.flock.examples.axon.webshop.app.basket.shared.ItemId
import community.flock.examples.axon.webshop.app.common.SingleValidationProblem
import community.flock.examples.axon.webshop.app.common.ValidationProblem
import community.flock.examples.axon.webshop.app.common.plus
import org.axonframework.extension.kotlin.messaging.sendAndWait
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
            .let<CreateBasketCommand, UUID>(commandGateway::sendAndWait)
            .let(GetNewBasket::Response200)

    override suspend fun postItem(request: PostItem.Request): PostItem.Response<*> =
        either {
            zipOrAccumulate(
                { BasketId(request.path.basketId).mapLeft { SingleValidationProblem(it.reason) }.bind() },
                { request.body.consume() },
                ::AddItemCommand,
            ).let<AddItemCommand, Unit>(commandGateway::sendAndWait)
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
            ).let<RemoveItemCommand, Unit>(commandGateway::sendAndWait)
        }.map { DeleteItem.Response200 }
            .mapLeft {
                it
                    .reduce(ValidationProblem::plus)
                    .reason
                    .let(::CommandProblem)
                    .let(DeleteItem::Response400)
            }.merge()
}
