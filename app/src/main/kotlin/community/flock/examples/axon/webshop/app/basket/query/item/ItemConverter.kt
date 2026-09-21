package community.flock.examples.axon.webshop.app.basket.query.item

import community.flock.examples.axon.webshop.app.basket.command.model.Item
import community.flock.examples.axon.webshop.app.basket.command.model.Price
import community.flock.examples.axon.webshop.app.basket.command.model.Title
import community.flock.examples.axon.webshop.app.basket.shared.ItemId
import community.flock.examples.axon.webshop.app.common.Converter
import community.flock.examples.axon.webshop.app.common.SymmetricConverter
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
