package community.flock.examples.axon.webshop.basket.application

import community.flock.examples.axon.webshop.basket.domain.model.BasketId
import community.flock.examples.axon.webshop.basket.domain.model.Item
import community.flock.examples.axon.webshop.basket.domain.model.minus
import community.flock.examples.axon.webshop.basket.domain.ports.BasketItemsPort
import org.axonframework.messaging.eventhandling.annotation.EventHandler
import org.axonframework.messaging.queryhandling.annotation.QueryHandler
import org.springframework.stereotype.Component

@Component
class ItemsProjection(
    private val basketItems: BasketItemsPort,
) {
    @QueryHandler
    fun handleGetItemsQuery(query: GetItemsQuery): List<Item> =
        query.let { (basketId) ->
            basketItems
                .getAllItemsFromBasket(basketId)
                .also { println("Projection with basketId: $basketId, queried") }
        }

    @EventHandler
    fun on(event: BasketCreatedEvent): BasketId =
        event.let { (basketId) ->
            basketItems
                .createBasket(basketId)
                .also { println("Basket with id $basketId created on projection") }
        }

    @EventHandler
    fun on(event: ItemAddedEvent): Item? =
        event.let { (basketId, item) ->
            basketItems
                .saveItemInBasket(basketId, item.copy(price = item.price - 1.0))
                .also { println("Item: $item, added to projection with basketId: $basketId") }
        }

    @EventHandler
    fun on(event: ItemRemovedEvent): Item? =
        event.let { (basketId, itemId) ->
            basketItems
                .deleteItemFromBasketById(basketId, itemId)
                .also { println("Item: $it removed from projection with basketId: $basketId") }
        }
}
