package community.flock.examples.axon.webshop.app.basket.shared

import community.flock.examples.axon.webshop.app.common.Value
import java.util.UUID
import java.util.concurrent.atomic.AtomicInteger

data class BasketId(
    override val value: UUID = UUID.randomUUID(),
) : Value<UUID> {
    override fun toString() = value.toString()

    companion object {
        operator fun invoke(value: String) =
            runCatching { UUID.fromString(value) }
                .map(::BasketId)
                .getOrNull()
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
            runCatching { value.toInt() }
                .map(::ItemId)
                .getOrNull()
    }
}
