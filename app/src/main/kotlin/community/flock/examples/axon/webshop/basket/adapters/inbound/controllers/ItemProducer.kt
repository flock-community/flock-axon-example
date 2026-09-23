package community.flock.examples.axon.webshop.basket.adapters.inbound.controllers

import community.flock.examples.axon.webshop.api.model.ItemQueryDto
import community.flock.examples.axon.webshop.basket.domain.model.Item
import community.flock.examples.axon.webshop.common.Producer

object ItemProducer : Producer<Item, ItemQueryDto> {
    override fun Item.produce() =
        ItemQueryDto(
            id = id.toString(),
            title = title.toString(),
            price = price.toString(),
        )
}
