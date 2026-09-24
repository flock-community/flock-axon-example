package community.flock.examples.axon.webshop.domain.shared

sealed class DomainProblem(
    val reason: String,
)

class InvalidBasketId(
    id: String,
) : DomainProblem("Basket id is not valid: $id")

class InvalidItemId(
    id: String,
) : DomainProblem("Item id is not valid: $id")
