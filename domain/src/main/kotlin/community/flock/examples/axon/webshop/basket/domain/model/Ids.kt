package community.flock.examples.axon.webshop.basket.domain.model

import java.util.UUID
import java.util.concurrent.atomic.AtomicInteger

data class BasketId(
    override val value: UUID = UUID.randomUUID(),
) : Value<UUID> {
    override fun toString() = value.toString()

    companion object {
        /** The id [value] spells, or null when it is not a UUID. */
        fun parse(value: String): BasketId? =
            try {
                BasketId(UUID.fromString(value))
            } catch (e: IllegalArgumentException) {
                null
            }
    }
}

@JvmInline
value class ItemId(
    override val value: Int = integer.getAndIncrement(),
) : Value<Int> {
    override fun toString() = value.toString()

    companion object {
        private val integer = AtomicInteger()

        /** The id [value] spells, or null when it is not a number. */
        fun parse(value: String): ItemId? = value.toIntOrNull()?.let(::ItemId)
    }
}
