package community.flock.examples.axon.webshop.app.basket.command

import community.flock.examples.axon.webshop.app.basket.command.model.Item
import community.flock.examples.axon.webshop.app.basket.shared.BasketId
import community.flock.examples.axon.webshop.app.basket.shared.ItemId
import org.axonframework.modelling.annotation.TargetEntityId

data class CreateBasketCommand(
    @TargetEntityId val basketId: BasketId,
)

data class AddItemCommand(
    @TargetEntityId
    val basketId: BasketId,
    val item: Item,
)

data class RemoveItemCommand(
    @TargetEntityId
    val basketId: BasketId,
    val itemId: ItemId,
)
