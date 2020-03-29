# Katcher

[![](https://jitpack.io/v/tom-power/katcher.svg)](https://jitpack.io/#tom-power/katcher)

Class for doing pattern matching with regex in Kotlin.

Inspired by questions about the availability of [pattern matching](https://docs.scala-lang.org/tour/pattern-matching.html) features in Kotlin, particularly [this one](https://discuss.kotlinlang.org/t/using-regex-in-a-when/1794) and related suggestions.

Use by calling `match` on a `CharSequence` with a lambda that will have available:
 
- an `input` property with an overloaded `equals` to do regex matching with 
- `match` property and `matchXAs` to help with destructuring capture groups

Mainly for fun :)

```kotlin
"hello everybody" match {
    when (input) {
        """([a-z ]+)""" -> matches.let { (something) -> "say $something" }
        else            -> "say nothing"
    }
} // "say hello everybody"

"sum 1 2" match {
    when (input) {
        """sum (\d+) (\d+)"""    -> match2As<Int, Int>().let { first + second }
        """concat (\d+) (\d+)""" -> match2As<String, String>().let { (first + second).toInt() }
        else                     -> Int.MAX_VALUE
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

