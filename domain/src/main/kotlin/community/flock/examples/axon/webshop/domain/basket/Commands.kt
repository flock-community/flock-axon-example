package community.flock.examples.axon.webshop.domain.basket

import community.flock.examples.axon.webshop.domain.shared.BasketId
import community.flock.examples.axon.webshop.domain.shared.ItemId

sealed interface BasketCommand {
    val basketId: BasketId
}

data class CreateBasketCommand(
    override val basketId: BasketId,
) : BasketCommand

data class AddItemCommand(
    override val basketId: BasketId,
    val item: Item,
) : BasketCommand

data class RemoveItemCommand(
    override val basketId: BasketId,
    val itemId: ItemId,
) : BasketCommand
