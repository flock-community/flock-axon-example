package community.flock.examples.axon.webshop.app.basket.query.item

import community.flock.examples.axon.webshop.app.common.SymmetricConverter
import community.flock.examples.axon.webshop.domain.basket.Item
import community.flock.examples.axon.webshop.domain.basket.Price
import community.flock.examples.axon.webshop.domain.basket.Title
import community.flock.examples.axon.webshop.domain.shared.ItemId
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
