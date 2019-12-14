package com.github.tompower

@Suppress("UNCHECKED_CAST")
class Katcher {
    private var match: List<String> = emptyList()

    operator fun String.contains(text: CharSequence): Boolean =
        this.toRegex().matches(text)
            .also { matches ->
                if (matches) {
                    match = this.toRegex().find(text)?.destructured?.toList() ?: emptyList()
                }
            }

    fun match() = match

    inline fun <reified A> match1(): Match1<A> = Match1(match().component1().toType())

    inline fun <reified A, reified B> match2(): Match2<A, B> =
        match().let {
            Match2(
                it.component1().toType(),
                it.component2().toType()
            )
        }

    inline fun <reified A, reified B, reified C> match3(): Match3<A, B, C> =
        match().let {
            Match3(
                it.component1().toType(),
                it.component2().toType(),
                it.component3().toType()
            )
        }

    inline fun <reified A, reified B, reified C, reified D> match4(): Match4<A, B, C, D> =
        match().let {
            Match4(
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

data class Match1<A>(val first: A)
data class Match2<A, B>(val first: A, val second: B)
data class Match3<A, B, C>(val first: A, val second: B, val third: C)
data class Match4<A, B, C, D>(val first: A, val second: B, val third: C, val forth: D)

infix fun <T> CharSequence.matches(block: Katcher.(text: CharSequence) -> T): T = Katcher().block(this)




