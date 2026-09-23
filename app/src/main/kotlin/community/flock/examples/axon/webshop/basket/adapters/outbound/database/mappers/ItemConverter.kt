package community.flock.examples.axon.webshop.basket.adapters.outbound.database.mappers

import community.flock.examples.axon.webshop.basket.adapters.outbound.database.model.ItemEntity
import community.flock.examples.axon.webshop.basket.domain.model.Item
import community.flock.examples.axon.webshop.basket.domain.model.ItemId
import community.flock.examples.axon.webshop.basket.domain.model.Price
import community.flock.examples.axon.webshop.basket.domain.model.Title
import community.flock.examples.axon.webshop.common.SymmetricConverter
import java.math.BigDecimal

object ItemConverter : SymmetricConverter<ItemEntity, Item> {
    override fun ItemEntity.internalize() =
        Item(
            id = ItemId(id),
            title = Title(title),
            price = Price(BigDecimal(price)),
        )

    override fun Item.externalize() =
        ItemEntity(
            id = id.value,
            title = title.toString(),
            price = price.toString(),
        )
}
