package community.flock.examples.axon.webshop.domain.event

import community.flock.examples.axon.webshop.domain.basket.Item
import community.flock.examples.axon.webshop.domain.shared.BasketId
import community.flock.examples.axon.webshop.domain.shared.ItemId

sealed interface Event

/** What happened to one basket. Every event names the basket, which is how a store finds a basket's history. */
sealed interface BasketEvent : Event {
    val basketId: BasketId
}

data class BasketCreatedEvent(
    override val basketId: BasketId,
) : BasketEvent

data class ItemAddedEvent(
    override val basketId: BasketId,
    val item: Item,
) : BasketEvent

data class ItemRemovedEvent(
    override val basketId: BasketId,
    val itemId: ItemId,
) : BasketEvent
