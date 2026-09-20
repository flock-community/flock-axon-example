package community.flock.examples.axon.webshop.app.basket.shared

sealed class AppException(
    override val message: String,
) : RuntimeException(message)

class InvalidBasketIdException(
    id: String,
) : AppException("Invalid basket ID: $id")

class InvalidItemIdException(
    id: String,
) : AppException("Invalid item ID: $id")
