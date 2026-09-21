package community.flock.examples.axon.webshop.app.basket.event

import community.flock.examples.axon.webshop.app.basket.command.model.Item
import community.flock.examples.axon.webshop.app.basket.shared.BasketId
import community.flock.examples.axon.webshop.app.basket.shared.ItemId

sealed interface Event

sealed interface BasketEvent : Event

data class BasketCreatedEvent(
    val basketId: BasketId,
) : BasketEvent

data class ItemAddedEvent(
    val basketId: BasketId,
    val item: Item,
) : BasketEvent

data class ItemRemovedEvent(
    val basketId: BasketId,
    val itemId: ItemId,
) : BasketEvent
