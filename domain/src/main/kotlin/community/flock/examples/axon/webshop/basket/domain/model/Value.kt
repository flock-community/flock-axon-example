package community.flock.examples.axon.webshop.basket.domain.model

interface Value<T : Any> {
    val value: T
}

operator fun <T : Any> Value<T>.component1() = value
