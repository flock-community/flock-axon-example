package community.flock.examples.axon.webshop.app.basket.command

import community.flock.examples.axon.webshop.api.basket.command.CommandApi
import community.flock.examples.axon.webshop.api.basket.command.model.ItemDto
import community.flock.examples.axon.webshop.app.basket.command.ItemConsumer.consume
import community.flock.examples.axon.webshop.app.basket.shared.BASKET_BASE_URL
import community.flock.examples.axon.webshop.app.basket.shared.BasketId
import community.flock.examples.axon.webshop.app.basket.shared.ITEMS_PATH
import community.flock.examples.axon.webshop.app.basket.shared.InvalidBasketIdException
import community.flock.examples.axon.webshop.app.basket.shared.InvalidItemIdException
import community.flock.examples.axon.webshop.app.basket.shared.ItemId
import community.flock.examples.axon.webshop.app.common.TechnicalException
import kotlinx.coroutines.future.await
import org.axonframework.config.Configuration
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping(BASKET_BASE_URL)
class CommandController(
    configuration: Configuration,
) : CommandApi {
    private val commandGateway = configuration.commandGateway()

    @PostMapping
    override suspend fun getNewBasket(): UUID =
        CreateBasketCommand(basketId = BasketId())
            .let { commandGateway.send<BasketId>(it) }
            .await()
            .value

    @PostMapping("/{potentialBasketId}/$ITEMS_PATH")
    override suspend fun postItem(
        @PathVariable potentialBasketId: String,
        @RequestBody potentialItem: ItemDto,
    ): ItemDto? =
        run {
            val basketId = BasketId(potentialBasketId) ?: throw InvalidBasketIdException(potentialBasketId)
            val item = potentialItem.consume()
            AddItemCommand(basketId = basketId, item = item)
                .let<AddItemCommand, CompletableFuture<ItemDto>>(commandGateway::send)
                .runCatching { await() }
                .getOrElse { throw TechnicalException("Adding an Item Failed", it) }
        }

    @DeleteMapping("/{potentialBasketId}/$ITEMS_PATH/{potentialItemId}")
    override suspend fun deleteItem(
        @PathVariable potentialBasketId: String,
        @PathVariable potentialItemId: String,
    ): ItemDto? {
        val itemId = ItemId(potentialItemId) ?: throw InvalidItemIdException(potentialItemId)
        val basketId = BasketId(potentialBasketId) ?: throw InvalidBasketIdException(potentialBasketId)
        return RemoveItemCommand(
            basketId = basketId,
            itemId = itemId,
        ).let<RemoveItemCommand, CompletableFuture<ItemDto>?>(commandGateway::send)
            .runCatching { this?.await() }
            .getOrElse { throw TechnicalException("Deleting an Item Failed", it) }
    }
}
