package community.flock.examples.axon.webshop.app.basket.query.item

import community.flock.examples.axon.webshop.api.model.ItemQueryDto
import community.flock.examples.axon.webshop.app.common.Producer
import community.flock.examples.axon.webshop.domain.basket.Item

object ItemProducer : Producer<Item, ItemQueryDto> {
    override fun Item.produce() =
        ItemQueryDto(
            id = id.toString(),
            title = title.toString(),
            price = price.toString(),
        )
}
