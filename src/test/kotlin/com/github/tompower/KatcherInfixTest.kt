package com.github.tompower

class KatcherInfixTest : KatcherTest() {
    override fun <T> matchWith(matcher: Katcher.() -> T): (String) -> T = { it match matcher }
}





