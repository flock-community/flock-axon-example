package community.flock.examples.axon.webshop.app.basket.command

import arrow.core.merge
import arrow.core.raise.either
import arrow.core.raise.zipOrAccumulate
import community.flock.examples.axon.webshop.api.endpoint.DeleteItem
import community.flock.examples.axon.webshop.api.endpoint.GetNewBasket
import community.flock.examples.axon.webshop.api.endpoint.PostItem
import community.flock.examples.axon.webshop.api.model.CommandProblem
import community.flock.examples.axon.webshop.app.basket.command.BasketIdProducer.produce
import community.flock.examples.axon.webshop.app.basket.command.ItemConsumer.consume
import community.flock.examples.axon.webshop.app.basket.shared.BasketId
import community.flock.examples.axon.webshop.app.basket.shared.ItemId
import community.flock.examples.axon.webshop.app.common.SingleValidationProblem
import community.flock.examples.axon.webshop.app.common.ValidationProblem
import community.flock.examples.axon.webshop.app.common.plus
import org.axonframework.config.Configuration
import org.springframework.web.bind.annotation.RestController

private interface CommandApi :
    GetNewBasket.Handler,
    PostItem.Handler,
    DeleteItem.Handler

@RestController
class CommandController(
    configuration: Configuration,
) : CommandApi {
    private val commandGateway = configuration.commandGateway()

    override suspend fun getNewBasket(request: GetNewBasket.Request): GetNewBasket.Response<*> =
        run {
            val command = CreateBasketCommand(basketId = BasketId())
            commandGateway.sendAndWait<BasketId>(command).produce()
        }.let(GetNewBasket::Response200)

    override suspend fun postItem(request: PostItem.Request): PostItem.Response<*> =
        either {
            zipOrAccumulate(
                { BasketId(request.path.basketId).mapLeft { SingleValidationProblem(it.reason) }.bind() },
                { request.body.consume() },
                ::AddItemCommand,
            ).let<AddItemCommand, Unit>(commandGateway::sendAndWait)
        }.map { PostItem.Response200 }
            .mapLeft { (it as List<ValidationProblem>).reduce { acc, problem -> acc.plus(problem) } }
            .mapLeft { PostItem.Response400(CommandProblem(it.reason)) }
            .merge()

    override suspend fun deleteItem(request: DeleteItem.Request): DeleteItem.Response<*> =
        either {
            val basketId = BasketId(request.path.basketId).mapLeft { DeleteItem.Response400(CommandProblem(it.reason)) }.bind()
            val itemId = ItemId(request.path.itemId).mapLeft { DeleteItem.Response400(CommandProblem(it.reason)) }.bind()
            val command = RemoveItemCommand(basketId = basketId, itemId = itemId)
            commandGateway.sendAndWait<Unit>(command)
        }.map { DeleteItem.Response200 }.merge()
}
