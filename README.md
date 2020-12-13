# Katcher

[![](https://jitpack.io/v/tom-power/katcher.svg)](https://jitpack.io/#tom-power/katcher)

Fun (!) class for doing pattern matching with regex in Kotlin.

Inspired by questions about the availability
of [pattern matching](https://docs.scala-lang.org/tour/pattern-matching.html) features in Kotlin,
particularly [this one](https://discuss.kotlinlang.org/t/using-regex-in-a-when/1794) and related suggestions.

Use by writing a lambda that has `Katcher` as receiver, it will have available:

- an `input` property with an overloaded `equals` to do regex matching with
- a `matches` property and `List<String>.XAs` methods to access capture groups

For instance using `infix fun <T> CharSequence.match(block: Katcher.() -> T): T`:

```kotlin
"hello everybody" match {
    when (input) {
        """([a-z ]+)""" -> matches.let { (something) -> "say $something" }
        else            -> "say nothing"
    }
} // "say hello everybody"

"sum 1 2" match {
    when (input) {
        """sum (\d+) (\d+)"""    -> matches.twoAs<Int, Int>().run { first + second }
        """concat (\d+) (\d+)""" -> matches.twoAs<String, String>().run { (first + second).toInt() }
        else                     -> Int.MAX_VALUE
    }
} // 3
```

Other examples can be found in `KatcherTest`. 

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

