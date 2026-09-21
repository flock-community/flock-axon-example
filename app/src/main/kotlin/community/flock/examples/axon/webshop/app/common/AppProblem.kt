package community.flock.examples.axon.webshop.app.common

sealed class AppProblem(
    val reason: String,
)

sealed class ValidationProblem(
    reason: String,
) : AppProblem(reason)

open class SingleValidationProblem(
    reason: String,
) : ValidationProblem(reason)

class MultipleValidationProblems(
    val errors: Set<SingleValidationProblem>,
) : ValidationProblem("Validation failed: ${errors.joinToString { it.reason }}")

operator fun ValidationProblem.plus(other: ValidationProblem): MultipleValidationProblems =
    MultipleValidationProblems(errors + other.errors)

val ValidationProblem.errors: Set<SingleValidationProblem>
    get() =
        when (this) {
            is SingleValidationProblem -> setOf(this)
            is MultipleValidationProblems -> errors
        }
