package dev.mythicdrops

internal sealed class Either<out E, out A> {
    data class Left<E>(val left: E) : Either<E, Nothing>()
    data class Right<A>(val right: A) : Either<Nothing, A>()

    fun <R> fold(ifLeft: (E) -> R, ifRight: (A) -> R): R =
        when (this) {
            is Left -> ifLeft(left)
            is Right -> ifRight(right)
        }

    fun <R> foldLeft(init: R, f: (E) -> R): R =
        fold(f) { init }

    fun <R> foldRight(init: R, f: (A) -> R): R =
        fold({ init }, f)

    fun <F, B> bimap(f: (E) -> F, g: (A) -> B): Either<F, B> =
        fold({ left(f(it)) }, { right(g(it)) })

    fun <B> map(f: (A) -> B): Either<E, B> =
        bimap({ it }, f)

    fun <F> mapLeft(f: (E) -> F): Either<F, A> =
        bimap(f) { it }

    fun swap(): Either<A, E> =
        fold(::right, ::left)

    companion object {
        fun <E> left(e: E): Either<E, Nothing> = Left(e)
        fun <A> right(a: A): Either<Nothing, A> = Right(a)

        fun <E, A> pure(a: A): Either<E, A> = right(a)

        fun <E, A, B, C> map2(e1: Either<E, A>, e2: Either<E, B>, f: (A, B) -> C): Either<E, C> =
            e1.fold(::left) { a -> e2.fold(::left) { b -> right(f(a, b)) } }

        fun <E, A, B> product(e1: Either<E, A>, e2: Either<E, B>): Either<E, Pair<A, B>> =
            map2(e1, e2, ::Pair)

        fun <E, A, B> ap(ef: Either<E, (A) -> B>, ea: Either<E, A>): Either<E, B> =
            map2(ef, ea) { f, a -> f(a) }

        fun <E, A, B> flatMap(e: Either<E, A>, f: (A) -> Either<E, B>): Either<E, B> =
            e.fold(::left, f)

        fun <E, F, A> flatMapLeft(e: Either<E, A>, f: (E) -> Either<F, A>): Either<F, A> =
            e.fold(f, ::right)

        fun <E, A> join(e: Either<E, Either<E, A>>): Either<E, A> =
            flatMap(e) { it }
    }
}

// These exist as extension methods because Kotlin doesn't have lower bounds checking

internal fun <E, A, B, C> Either<E, A>.map2(other: Either<E, B>, f: (A, B) -> C): Either<E, C> =
    Either.map2(this, other, f)

internal fun <E, A, B> Either<E, A>.product(other: Either<E, B>): Either<E, Pair<A, B>> =
    Either.product(this, other)

internal fun <E, A, B> Either<E, A>.times(other: Either<E, B>): Either<E, Pair<A, B>> =
    Either.product(this, other)

internal fun <E, A, B> Either<E, (A) -> B>.ap(e: Either<E, A>): Either<E, B> =
    Either.ap(this, e)

internal fun <E, A, B> Either<E, A>.flatMap(f: (A) -> Either<E, B>): Either<E, B> =
    Either.flatMap(this, f)

internal fun <E, F, A> Either<E, A>.flatMapLeft(f: (E) -> Either<F, A>): Either<F, A> =
    Either.flatMapLeft(this, f)

internal fun <E, A> Either<E, Either<E, A>>.join(): Either<E, A> =
    Either.join(this)

internal fun <E, A> Either<E, A>.orElse(f: (E) -> A): A =
    fold(f) { it }

// Traversable for Either<E, A>
// Note: The definition is duplicated because Kotlin can't type check `sequence = traverse(id)`
// Note: The definition of traverse is intentionally not `map(f).sequence()` to avoid an extra traversal

internal fun <E, A> Collection<A>.traverse(f: (A) -> Either<E, A>): Either<E, Collection<A>> =
    fold(Either.pure(listOf())) { eacc, ee -> eacc.map2(f(ee)) { acc, e -> acc + e } }

internal fun <E, A> Collection<Either<E, A>>.sequence(): Either<E, Collection<A>> =
    fold(Either.pure(listOf())) { eacc, ee -> eacc.map2(ee) { acc, e -> acc + e } }
