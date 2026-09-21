package community.flock.examples.axon.webshop.app.basket.shared

import arrow.core.Either
import community.flock.examples.axon.webshop.app.common.Value
import java.util.UUID
import java.util.concurrent.atomic.AtomicInteger

data class BasketId(
    override val value: UUID = UUID.randomUUID(),
) : Value<UUID> {
    override fun toString() = value.toString()

    companion object {
        operator fun invoke(value: String) =
            Either
                .catch { UUID.fromString(value) }
                .mapLeft { InvalidBasketId(value) }
                .map(::BasketId)
    }
}

@JvmInline
value class ItemId(
    override val value: Int = integer.getAndIncrement(),
) : Value<Int> {
    override fun toString() = value.toString()

    companion object {
        private val integer = AtomicInteger()

        operator fun invoke(value: String) =
            Either
                .catch { value.toInt() }
                .mapLeft { InvalidItemId(value) }
                .map(::ItemId)
    }
}
