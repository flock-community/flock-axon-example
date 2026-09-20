package community.flock.examples.axon.webshop.app.common

sealed class AppException(
    override val message: String?,
    cause: Throwable? = null,
) : RuntimeException(message, cause)

class TechnicalException(
    message: String?,
    override val cause: Throwable,
) : AppException(message, cause)
