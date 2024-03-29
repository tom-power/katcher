package com.github.tompower

/**
 * Class for using regex to do something like pattern matching with case classes in [Scala](https://docs.scala-lang.org/tour/pattern-matching.html).
 */
@Suppress("UNCHECKED_CAST")
class Katcher(
    private val _input: String
) {
    val input: Any get() = this

    private var matchResult: MatchResult? = null

    override fun equals(other: Any?): Boolean =
        if (other is String) {
            val regex = other.toRegex()
            regex.matches(this._input).also { match ->
                if (match) {
                    matchResult = regex.find(this._input)
                }
            }
        } else super.equals(other)

    /**
     * Matched capture groups
     * @return List<String>
     */
    val matches: List<String> get() = matchResult?.destructured?.toList() ?: emptyList()


    /**
     * One typed matched capture group
     * @return Match1<A>
     */
    inline fun <reified A> List<String>.oneAs(): A = matches.component1().toType()

    /**
     * Two typed matched capture groups
     * @return Match2<A, B>
     */
    inline fun <reified A, reified B> List<String>.twoAs(): Match2<A, B> =
        Match2(
            component1().toType(),
            component2().toType()
        )

    /**
     * Three typed matched capture groups
     * @return Match3<A, B, C>
     */
    inline fun <reified A, reified B, reified C> List<String>.threeAs(): Match3<A, B, C> =
        Match3(
            component1().toType(),
            component2().toType(),
            component3().toType()
        )

    /**
     * Four typed matched capture groups
     * @return Match4<A, B, C, D>
     */
    inline fun <reified A, reified B, reified C, reified D> List<String>.fourAs(): Match4<A, B, C, D> =
        Match4(
            component1().toType(),
            component2().toType(),
            component3().toType(),
            component4().toType()
        )

    inline fun <reified T> String.toType(): T =
        when (T::class) {
            Integer::class -> this.toInt() as T
            Char::class    -> this.toCharArray()[0] as T
            else           -> this as T
        }

    data class Match2<A, B>(val first: A, val second: B)
    data class Match3<A, B, C>(val first: A, val second: B, val third: C)
    data class Match4<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
}

infix fun <T> CharSequence.match(block: Katcher.() -> T): T = Katcher(this.toString()).block()

fun <T> (Katcher.() -> T).match(input: CharSequence): T = Katcher(input.toString()).this()




