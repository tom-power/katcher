package com.github.tompower

class KatcherExtensionTest: KatcherTest() {
    override fun <T> matchWith(matcher: Katcher.() -> T): (String) -> T = { matcher.match(it) }
}





