package community.flock.examples.axon.webshop.basket.application

import community.flock.examples.axon.webshop.basket.domain.model.BasketId
import community.flock.examples.axon.webshop.basket.domain.model.Item
import community.flock.examples.axon.webshop.basket.domain.model.ItemId
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
