package community.flock.examples.axon.webshop.app.basket.command

import community.flock.examples.axon.webshop.api.model.ItemDto
import community.flock.examples.axon.webshop.app.basket.command.model.Item
import community.flock.examples.axon.webshop.app.basket.command.model.Price
import community.flock.examples.axon.webshop.app.basket.command.model.Title
import community.flock.examples.axon.webshop.app.common.Consumer

object ItemConsumer : Consumer<ItemDto, Item> {
    override fun ItemDto.consume() =
        Item(
            title = Title(title),
            price = Price(price),
        )
}
