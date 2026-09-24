package community.flock.examples.axon.webshop.domain.event

import community.flock.examples.axon.webshop.domain.basket.Item
import community.flock.examples.axon.webshop.domain.event.BasketEvent.Companion.BASKET_TAG
import community.flock.examples.axon.webshop.domain.shared.BasketId
import community.flock.examples.axon.webshop.domain.shared.ItemId
import org.axonframework.eventsourcing.annotation.EventTag

sealed interface Event

sealed interface BasketEvent : Event {
    companion object {
        const val BASKET_TAG = "basketId"
    }
}

data class BasketCreatedEvent(
    @EventTag(BASKET_TAG)
    val basketId: BasketId,
) : BasketEvent

data class ItemAddedEvent(
    @EventTag(BASKET_TAG)
    val basketId: BasketId,
    val item: Item,
) : BasketEvent

data class ItemRemovedEvent(
    @EventTag(BASKET_TAG)
    val basketId: BasketId,
    val itemId: ItemId,
) : BasketEvent
