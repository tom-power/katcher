# Katcher

A Kotlin class for doing something like regex pattern matching with case classes in [Scala](https://docs.scala-lang.org/tour/regular-expression-patterns.html).

Inspired by questions about the availability of similar in Kotlin, particularly [this one](https://discuss.kotlinlang.org/t/using-regex-in-a-when/1794) and related suggestions.

Use by calling `matches` on a `CharSequence` with a lambda that will have available:
 
- an overloaded `in` keyword for regex matching 
- `match` and `matchX` methods to help with destructuring capture groups

Mainly for fun :)

```kotlin
"hello everybody" matches {
    when (it) {
        in """([a-z ]+)""" -> match().let { (something) -> "say $something" }
        else               -> "say nothing"
    }
}.let { println(it) } // "say hello everybody"

"sum 1 2" matches {
    when (it) {
        in """sum (\d+) (\d+)"""    -> match2<Int, Int>().let { (first, second) -> first + second }
        in """concat (\d+) (\d+)""" -> match().let { (first, second) -> (first + second).toInt() }
        else                        -> Int.MAX_VALUE
    }
}.let { println(it) } // "3"
```