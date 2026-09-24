package community.flock.examples.axon.webshop.domain.shared

/** A rule of the domain that was not met. Each part of the domain declares its own problems as subtypes. */
abstract class DomainProblem(
    val reason: String,
)

/** Carries a [DomainProblem] out of a decision: a decider that cannot honour a command throws this. */
class DomainException(
    val problem: DomainProblem,
) : RuntimeException(problem.reason)

class InvalidBasketId(
    id: String,
) : DomainProblem("Basket id is not valid: $id")

class InvalidItemId(
    id: String,
) : DomainProblem("Item id is not valid: $id")
