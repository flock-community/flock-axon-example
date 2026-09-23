package community.flock.examples.axon.webshop.basket.adapters.inbound.controllers

import community.flock.examples.axon.webshop.api.model.ItemDto
import community.flock.examples.axon.webshop.basket.domain.model.Item
import community.flock.examples.axon.webshop.basket.domain.model.Price
import community.flock.examples.axon.webshop.basket.domain.model.Title
import community.flock.examples.axon.webshop.common.Consumer

object ItemConsumer : Consumer<ItemDto, Item> {
    override fun ItemDto.consume() =
        Item(
            title = Title(title),
            price = Price(price),
        )
}
