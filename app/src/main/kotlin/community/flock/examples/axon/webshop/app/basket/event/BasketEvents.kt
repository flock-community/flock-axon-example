package community.flock.examples.axon.webshop.app.basket.event

import community.flock.examples.axon.webshop.app.basket.command.model.Item
import community.flock.examples.axon.webshop.app.basket.shared.BasketId
import community.flock.examples.axon.webshop.app.basket.shared.ItemId
import org.axonframework.eventsourcing.annotation.EventTag

sealed interface Event

sealed interface BasketEvent : Event

data class BasketCreatedEvent(
    @EventTag("basketId")
    val basketId: BasketId,
) : BasketEvent

data class ItemAddedEvent(
    @EventTag("basketId")
    val basketId: BasketId,
    val item: Item,
) : BasketEvent

data class ItemRemovedEvent(
    @EventTag("basketId")
    val basketId: BasketId,
    val itemId: ItemId,
) : BasketEvent
