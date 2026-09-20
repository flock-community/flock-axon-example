package community.flock.examples.axon.webshop.app.basket.query.item

import community.flock.examples.axon.webshop.app.basket.command.model.Item
import community.flock.examples.axon.webshop.app.basket.command.model.minus
import community.flock.examples.axon.webshop.app.basket.query.GetItemsQuery
import community.flock.examples.axon.webshop.app.basket.shared.BasketCreatedEvent
import community.flock.examples.axon.webshop.app.basket.shared.BasketId
import community.flock.examples.axon.webshop.app.basket.shared.ItemAddedEvent
import community.flock.examples.axon.webshop.app.basket.shared.ItemRemovedEvent
import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class ItemsProjection(
    private val repository: ItemRepository,
) {
    @QueryHandler
    fun handleGetItemsQuery(query: GetItemsQuery): List<Item> =
        query.let { (basketId) ->
            repository
                .getAllItemsFromBasket(basketId)
                .also { println("Projection with basketId: $basketId, queried") }
        }

    @EventHandler
    fun on(event: BasketCreatedEvent): BasketId =
        event.let { (basketId) ->
            repository
                .createBasket(basketId)
                .also { println("Basket with id $basketId created on projection") }
        }

    @EventHandler
    fun on(event: ItemAddedEvent): Item? =
        event.let { (basketId, item) ->
            repository
                .saveItemInBasket(basketId, item.copy(price = item.price - 1.0))
                .also { println("Item: $item, added to projection with basketId: $basketId") }
        }

    @EventHandler
    fun on(event: ItemRemovedEvent): Item? =
        event.let { (basketId, itemId) ->
            repository
                .deleteItemFromBasketById(basketId, itemId)
                .also { println("Item: $it removed from projection with basketId: $basketId") }
        }
}
