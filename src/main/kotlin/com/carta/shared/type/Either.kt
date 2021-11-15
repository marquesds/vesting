package com.carta.shared.type

sealed class Either<out E, out B>

class Left<out E>(val value: E) : Either<E, Nothing>()

class Right<out A>(val value: A) : Either<Nothing, A>()

fun <E, A> Either<E, A>.unsafeLeft(): E? =
    when (this) {
        is Left -> this.value
        else -> null
    }

fun <E, A> Either<E, A>.unsafeRight(): A? =
    when (this) {
        is Right -> this.value
        else -> null
    }
