package community.flock.examples.axon.webshop.domain.basket

import community.flock.examples.axon.webshop.domain.shared.BasketId
import community.flock.examples.axon.webshop.domain.shared.ItemId

/** The state of a basket as the decider sees it: nothing until it is created, then the items it holds. */
sealed interface Basket {
    data object NotCreated : Basket

    data class Active(
        val id: BasketId,
        val items: Map<ItemId, Item> = emptyMap(),
    ) : Basket {
        val totalPrice: Price get() = items.values.fold(Price(0.00)) { acc, cur -> acc + cur.price }
    }
}
