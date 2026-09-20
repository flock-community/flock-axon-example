package community.flock.examples.axon.webshop.app.basket.shared

import community.flock.examples.axon.webshop.app.basket.command.model.Item

data class BasketCreatedEvent(
    val basketId: BasketId,
)

data class ItemAddedEvent(
    val basketId: BasketId,
    val item: Item,
)

data class ItemRemovedEvent(
    val basketId: BasketId,
    val itemId: ItemId,
)
