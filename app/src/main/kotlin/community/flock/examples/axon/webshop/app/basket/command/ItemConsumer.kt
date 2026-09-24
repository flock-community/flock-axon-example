package community.flock.examples.axon.webshop.app.basket.command

import community.flock.examples.axon.webshop.api.model.ItemDto
import community.flock.examples.axon.webshop.app.common.Consumer
import community.flock.examples.axon.webshop.domain.basket.Item
import community.flock.examples.axon.webshop.domain.basket.Price
import community.flock.examples.axon.webshop.domain.basket.Title

object ItemConsumer : Consumer<ItemDto, Item> {
    override fun ItemDto.consume() =
        Item(
            title = Title(title),
            price = Price(price),
        )
}
