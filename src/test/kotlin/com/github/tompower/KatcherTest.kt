package com.github.tompower

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

abstract class KatcherTest {

    abstract fun <T> matchWith(matcher: Katcher.() -> T): (String) -> T

    @Test
    fun `can match a list of String typed capture groups`() {
        val matcher: Katcher.() -> String = {
            when (input) {
                """([a-z]+) (\d+)""" -> matches.let { (letters, numbers) -> "$letters, $numbers" }
                else                 -> "not found"
            }
        }

        assertThat(matchWith(matcher)("abc 123"), equalTo("abc, 123"))
        assertThat(matchWith(matcher)("lalala"), equalTo("not found"))
    }

    @Test
    fun `can match Int typed capture groups`() {
        val matcher: Katcher.() -> Int = {
            when (input) {
                """(\d+) (\d+)""" -> matches.twoAs<Int, Int>().run { first + second }
                else              -> Int.MAX_VALUE
            }
        }

        assertThat(matchWith(matcher)("1 2"), equalTo(3))
        assertThat(matchWith(matcher)("lalala"), equalTo(Int.MAX_VALUE))
    }

    @Test
    fun `can match Char typed capture groups`() {
        val matcher: Katcher.() -> Char = {
            when (input) {
                """([a-zA-Z])""" -> matches.oneAs()
                else             -> 'N'
            }
        }

        assertThat(matchWith(matcher)("C"), equalTo('C'))
        assertThat(matchWith(matcher)("lalala"), equalTo('N'))
    }

    @Test
    fun `can match mixed capture groups`() {
        val matcher: Katcher.() -> RobotCommand = {
            when (input) {
                """S"""                         -> Start
                """S ([a-zA-Z]+)"""             -> matches.let { (something) -> Say(something) }
                """W (\d+) (\d+) ([a-zA-Z])"""  ->
                    matches.threeAs<Int, Int, Char>().let { (times, vigour, hand) ->
                        Wave(times, vigour, Hand.valueOf(hand.toString()))
                    }
                """F (\d+) (\d+) (\d+) (\d+)""" ->
                    matches.fourAs<Int, Int, Int, Int>().let { (speed, x, y, z) ->
                        Fly(speed, PointInSpace(x, y, z))
                    }
                else                            -> Unknown
            }
        }

        expectThat(matchWith(matcher)("S")).isEqualTo(Start)
        expectThat(matchWith(matcher)("S bye")).isEqualTo(Say("bye"))
        expectThat(matchWith(matcher)("W 5 100 L")).isEqualTo(Wave(times = 5, vigour = 100, hand = Hand.L))
        expectThat(matchWith(matcher)("F 100000000 1 0 0")).isEqualTo(
            Fly(
                speed = 100000000,
                place = PointInSpace(1, 0, 0)
            )
        )
        expectThat(matchWith(matcher)("lalala")).isEqualTo(Unknown)
    }

    data class PointInSpace(val x: Int, val y: Int, val z: Int)
    open class RobotCommand
    object Start : RobotCommand()
    data class Say(val something: String) : RobotCommand()
    enum class Hand { L, R }
    data class Wave(val times: Int, val vigour: Int, val hand: Hand) : RobotCommand()
    data class Fly(val speed: Int, val place: PointInSpace) : RobotCommand()
    object Unknown : RobotCommand()
}






