package community.flock.examples.axon.webshop.domain.shared

interface Value<T : Any> {
    val value: T
}

operator fun <T : Any> Value<T>.component1() = value
