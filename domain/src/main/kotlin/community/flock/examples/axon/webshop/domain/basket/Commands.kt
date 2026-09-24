package community.flock.examples.axon.webshop.domain.basket

import community.flock.examples.axon.webshop.domain.shared.BasketId
import community.flock.examples.axon.webshop.domain.shared.ItemId
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
