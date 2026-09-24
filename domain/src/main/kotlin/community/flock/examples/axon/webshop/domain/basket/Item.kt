package community.flock.examples.axon.webshop.domain.basket

import community.flock.examples.axon.webshop.domain.shared.ItemId
import community.flock.examples.axon.webshop.domain.shared.Value
import java.math.BigDecimal

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
