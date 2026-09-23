package community.flock.examples.axon.webshop.app.basket.command.model

import community.flock.examples.axon.webshop.app.basket.shared.ItemId
import community.flock.examples.axon.webshop.app.common.Value
import org.axonframework.eventsourcing.annotation.EventSourcedEntity
import java.math.BigDecimal

const val ITEM_TAG = "itemId"

@EventSourcedEntity(tagKey = ITEM_TAG)
data class Item(
    val id: ItemId = ItemId(),
    val title: Title,
    val price: Price,
)

@JvmInline
value class Title(
    override val value: String,
) : Value<String> {
    override fun toString() = value
}

@JvmInline
value class Price(
    override val value: BigDecimal,
) : Value<BigDecimal> {
    override fun toString() = "%.2f".format(value)

    companion object {
        operator fun invoke(value: Double) = Price(value.toBigDecimal())
    }
}

operator fun Price.plus(other: Price) = Price(value + other.value)

operator fun Price.plus(other: Double) = Price(value + BigDecimal(other))

operator fun Price.minus(other: Price) = Price(value - other.value)

operator fun Price.minus(other: Double) = Price(value - BigDecimal(other))
