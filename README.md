# Katcher

[![](https://jitpack.io/v/tom-power/katcher.svg)](https://jitpack.io/#tom-power/katcher)

A Kotlin class for using regex to do something like pattern matching with case classes in [Scala](https://docs.scala-lang.org/tour/pattern-matching.html).

Inspired by questions about the availability of similar in Kotlin, particularly [this one](https://discuss.kotlinlang.org/t/using-regex-in-a-when/1794) and related suggestions.

Use by calling `match` on a `CharSequence` with a lambda that will have available:
 
- an overloaded `in` keyword for regex matching 
- `matches` and `matchesX` methods to help with destructuring capture groups

Mainly for fun :)

```kotlin
"hello everybody" match {
    when (it) {
        in """([a-z ]+)""" -> matches().let { (something) -> "say $something" }
        else               -> "say nothing"
    }
} // "say hello everybody"

"sum 1 2" match {
    when (it) {
        in """sum (\d+) (\d+)"""    -> matches2<Int, Int>().let { (first, second) -> first + second }
        in """concat (\d+) (\d+)""" -> matches().let { (first, second) -> (first + second).toInt() }
        else                        -> Int.MAX_VALUE
    }
} // 3
```

### Installation

```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
        implementation 'com.github.tom-power:katcher:master-SNAPSHOT'
}
```

