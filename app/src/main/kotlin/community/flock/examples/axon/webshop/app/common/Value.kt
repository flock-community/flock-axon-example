package community.flock.examples.axon.webshop.app.common

interface Value<T : Any> {
    val value: T
}

operator fun <T : Any> Value<T>.component1() = value
