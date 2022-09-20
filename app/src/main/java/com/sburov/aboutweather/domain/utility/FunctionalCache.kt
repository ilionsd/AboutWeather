package com.sburov.aboutweather.domain.utility

import arrow.core.*

sealed class CacheError

data class EvaluatorException(val e: Throwable) : CacheError()

object CacheMiss : CacheError()

object CacheExpired : CacheError()

class FunctionalCache<in K, out V>(
    private val evaluator: (K) -> V,
    private val invalidateIf: (V) -> Boolean
) {
    private val cache = mutableMapOf<K, V>()

    fun get(value: K): Either<CacheError, V> = search(value).handleErrorWith {
        evaluate(value).tap { cache[value] = it }
    }

    fun invalidate(value: K): FunctionalCache<K, V> = apply {
        cache.remove(value)
    }

    fun search(value: K): Either<CacheError, V> =
        Option
            .fromNullable(cache[value]).toEither { CacheMiss }
            .filterOrElse({ invalidateIf(it).not() }, { CacheExpired })

    private fun evaluate(value: K): Either<CacheError, V> = Either.catch {
        evaluator(value)
    }.mapLeft {
        EvaluatorException(it)
    }
}
