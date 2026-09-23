package community.flock.examples.axon.webshop.basket.application

import community.flock.examples.axon.webshop.basket.domain.model.BasketId
import community.flock.examples.axon.webshop.basket.domain.model.Item
import community.flock.examples.axon.webshop.basket.domain.model.ItemId
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
