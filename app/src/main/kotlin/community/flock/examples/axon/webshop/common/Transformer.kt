package community.flock.examples.axon.webshop.common

import arrow.core.Either

/**
* Placeholder interface to define upstream transformers.
* (i.e., used by listener or controller implementations)
*/
interface Transformer<POTENTIAL : Any, DOMAIN : Any, PRODUCED : Any> :
    Consumer<POTENTIAL, DOMAIN>,
    Producer<DOMAIN, PRODUCED>

interface Consumer<in DTO : Any, out DOMAIN : Any> {
    fun DTO.consume(): DOMAIN

    fun Iterable<DTO>.consume(): List<DOMAIN> = map { it.consume() }
}

interface Producer<in DOMAIN : Any, out DTO : Any> {
    fun DOMAIN.produce(): DTO

    fun Iterable<DOMAIN>.produce(): List<DTO> = map { it.produce() }
}

interface SymmetricTransformer<DTO : Any, DOMAIN : Any> : Transformer<DTO, DOMAIN, DTO>

interface Validator<DTO : Any, DOMAIN : Any> : Consumer<DTO, Either<ValidationProblem, DOMAIN>> {
    fun DTO.validate(): Either<MultipleValidationProblems, DOMAIN> = consume().mapLeft { MultipleValidationProblems(it.errors) }
}

interface AsymmetricValidator<POTENTIAL : Any, DOMAIN : Any, PRODUCED : Any> :
    Validator<POTENTIAL, DOMAIN>,
    Producer<DOMAIN, PRODUCED>

interface SymmetricValidator<DTO : Any, DOMAIN : Any> : AsymmetricValidator<DTO, DOMAIN, DTO>
