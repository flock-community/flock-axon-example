package community.flock.examples.axon.webshop.app.basket.command

import community.flock.examples.axon.webshop.app.basket.command.model.Item
import community.flock.examples.axon.webshop.app.basket.shared.BasketId
import community.flock.examples.axon.webshop.app.basket.shared.ItemId
import org.axonframework.modelling.command.TargetAggregateIdentifier

data class CreateBasketCommand(
    @TargetAggregateIdentifier val basketId: BasketId,
)

data class AddItemCommand(
    @TargetAggregateIdentifier
    val basketId: BasketId,
    val item: Item,
)

data class RemoveItemCommand(
    @TargetAggregateIdentifier
    val basketId: BasketId,
    val itemId: ItemId,
)
