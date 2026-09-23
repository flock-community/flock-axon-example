package community.flock.examples.axon.webshop.common

import arrow.core.Either

/**
 * Placeholder interface to define downstream converters.
 * (i.e., used by publisher, adapter, or repository implementations)
 */
interface Converter<EXTERNAL : Any, DOMAIN : Any, EXTERNALIZED : Any> :
    Internalizer<EXTERNAL, DOMAIN>,
    Externalizer<DOMAIN, EXTERNALIZED>

interface Internalizer<in DTO : Any, out DOMAIN : Any> {
    fun DTO.internalize(): DOMAIN

    fun Iterable<DTO>.internalize(): List<DOMAIN> = map { it.internalize() }
}

interface Externalizer<in DOMAIN : Any, out DTO : Any> {
    fun DOMAIN.externalize(): DTO

    fun Iterable<DOMAIN>.externalize(): List<DTO> = map { it.externalize() }
}

interface SymmetricConverter<DTO : Any, DOMAIN : Any> : Converter<DTO, DOMAIN, DTO>

interface Verifier<DTO : Any, DOMAIN : Any> : Internalizer<DTO, Either<ValidationProblem, DOMAIN>> {
    fun DTO.verify(): Either<MultipleValidationProblems, DOMAIN> = internalize().mapLeft { MultipleValidationProblems(it.errors) }
}

interface AsymmetricVerifier<EXTERNAL : Any, DOMAIN : Any, EXTERNALIZED : Any> :
    Verifier<EXTERNAL, DOMAIN>,
    Externalizer<DOMAIN, EXTERNALIZED>

interface SymmetricVerifier<DTO : Any, DOMAIN : Any> : AsymmetricVerifier<DTO, DOMAIN, DTO>
