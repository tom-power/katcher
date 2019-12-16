package com.github.tompower

@Suppress("UNCHECKED_CAST")
class Katcher {
    private var matches: List<String> = emptyList()

    operator fun String.contains(text: CharSequence): Boolean =
        this.toRegex().matches(text)
            .also { matches ->
                if (matches) {
                    this@Katcher.matches = this.toRegex().find(text)?.destructured?.toList() ?: emptyList()
                }
            }

    fun matches() = matches

    inline fun <reified A> matches1(): Matches1<A> = Matches1(matches().component1().toType())

    inline fun <reified A, reified B> matches2(): Matches2<A, B> =
        matches().let {
            Matches2(
                it.component1().toType(),
                it.component2().toType()
            )
        }

    inline fun <reified A, reified B, reified C> matches3(): Matches3<A, B, C> =
        matches().let {
            Matches3(
                it.component1().toType(),
                it.component2().toType(),
                it.component3().toType()
            )
        }

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

infix fun <T> CharSequence.match(block: Katcher.(text: CharSequence) -> T): T = Katcher().block(this)




