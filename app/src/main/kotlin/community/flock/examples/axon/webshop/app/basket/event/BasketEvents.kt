package community.flock.examples.axon.webshop.app.basket.event

import community.flock.examples.axon.webshop.app.basket.command.model.Item
import community.flock.examples.axon.webshop.app.basket.event.BasketEvent.Companion.BASKET_TAG
import community.flock.examples.axon.webshop.app.basket.shared.BasketId
import community.flock.examples.axon.webshop.app.basket.shared.ItemId
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
