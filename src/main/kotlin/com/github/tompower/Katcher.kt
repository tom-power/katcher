package com.github.tompower

/**
 * Class for using regex to do something like pattern matching with case classes in [Scala](https://docs.scala-lang.org/tour/pattern-matching.html).
 */
@Suppress("UNCHECKED_CAST")
class Katcher {
    private var matches: List<String> = emptyList()

    /**
     * Overloaded in keyword for regex matching
     * @return true if matched
     */
    operator fun String.contains(text: CharSequence): Boolean =
        this.toRegex().matches(text)
            .also { matches ->
                if (matches) {
                    this@Katcher.matches = this.toRegex().find(text)?.destructured?.toList() ?: emptyList()
                }
            }

    /**
     * Matched capture groups
     * @return List<String>
     */
    fun matches() = matches

    /**
     * One typed matched capture group
     * @return Matches1<A>
     */
    inline fun <reified A> matches1(): Matches1<A> = Matches1(matches().component1().toType())

    /**
     * Two typed matched capture groups
     * @return Matches2<A, B>
     */
    inline fun <reified A, reified B> matches2(): Matches2<A, B> =
        matches().let {
            Matches2(
                it.component1().toType(),
                it.component2().toType()
            )
        }

    /**
     * Three typed matched capture groups
     * @return Matches3<A, B, C>
     */
    inline fun <reified A, reified B, reified C> matches3(): Matches3<A, B, C> =
        matches().let {
            Matches3(
                it.component1().toType(),
                it.component2().toType(),
                it.component3().toType()
            )
        }

    /**
     * Four typed matched capture groups
     * @return Matches4<A, B, C, D>
     */
    inline fun <reified A, reified B, reified C, reified D> matches4(): Matches4<A, B, C, D> =
        matches().let {
            Matches4(
                it.component1().toType(),
                it.component2().toType(),
                it.component3().toType(),
                it.component4().toType()
            )
        }

    inline fun <reified T> String.toType(): T =
        try {
            this.toInt() as T
        } catch (e: Exception) {
            when {
                this.count() == 1 -> this.toCharArray()[0] as T
                else -> this as T
            }
        }
}

data class Matches1<A>(val first: A)
data class Matches2<A, B>(val first: A, val second: B)
data class Matches3<A, B, C>(val first: A, val second: B, val third: C)
data class Matches4<A, B, C, D>(val first: A, val second: B, val third: C, val forth: D)

/**
 * Function literal with Katcher as receiver for matching and processing text input
 * Passed lambda has available
 *  - an overloaded in keyword for regex matching
 *  - matches and matchesX methods to help with destructuring capture groups
 */
infix fun <T> CharSequence.match(block: Katcher.(text: CharSequence) -> T): T = Katcher().block(this)




