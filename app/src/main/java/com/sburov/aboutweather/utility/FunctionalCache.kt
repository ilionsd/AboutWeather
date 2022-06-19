package com.sburov.aboutweather.utility

import arrow.core.*

sealed class CacheError

object CacheMiss : CacheError()

object CacheExpired : CacheError()

object EvaluatorException : CacheError()

class FunctionalCache<in K, out V>(
    private val evaluator: (K) -> V,
    private val invalidateIf: (V) -> Boolean
) {
    private val responseCacheMap = mutableMapOf<K, V>()

    private fun getCachedResult(value: K): Either<CacheError, V> =
        Option
            .fromNullable(responseCacheMap[value])
            .toEither { CacheMiss }
            .filterOrElse({ invalidateIf(it).not() }, { CacheExpired })

    private fun evaluateResult(value: K): Either<CacheError, V> =
        Either.catch { evaluator(value) }.mapLeft { EvaluatorException }.tap { responseCacheMap[value] = it }

    fun invalidate(value: K): FunctionalCache<K, V> = apply {
        responseCacheMap.remove(value)
    }

    fun evaluate(value: K): Either<CacheError, V> = getCachedResult(value).handleErrorWith { evaluateResult(value) }
}
