package com.github.tompower

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class KatcherExtensionTest {

    @Test
    fun `can match a list of String typed capture groups`() {
        val matcher: Katcher.() -> String = {
            when (input) {
                """([a-z]+) (\d+)""" -> matches.let { (letters, numbers) -> "$letters, $numbers" }
                else                 -> "not found"
            }
        }

        assertThat(matcher.match("abc 123"), equalTo("abc, 123"))
        assertThat(matcher.match("lalala"), equalTo("not found"))
    }

    @Test
    fun `can match Int typed capture groups`() {
        val matcher: Katcher.() -> Int = {
            when (input) {
                """(\d+) (\d+)""" -> match2As<Int, Int>().run { first + second }
                else              -> Int.MAX_VALUE
            }
        }

        assertThat(matcher.match("1 2"), equalTo(3))
        assertThat(matcher.match("lalala"), equalTo(Int.MAX_VALUE))
    }

    @Test
    fun `can match Char typed capture groups`() {
        val matcher: Katcher.() -> Char = {
            when (input) {
                """([a-zA-Z])""" -> match1As<Char>().first
                else             -> 'N'
            }
        }

        assertThat(matcher.match("C"), equalTo('C'))
        assertThat(matcher.match("lalala"), equalTo('N'))
    }

    @Test
    fun `can match mixed capture groups`() {
        val robotCommandMatcher: Katcher.() -> RobotCommand = {
            when (input) {
                """S"""                         -> Start
                """S ([a-zA-Z]+)"""             -> matches.let { (something) -> Say(something) }
                """W (\d+) (\d+) ([a-zA-Z])"""  ->
                    match3As<Int, Int, Char>().let { (times, vigour, hand) ->
                        Wave(times, vigour, hand)
                    }
                """F (\d+) (\d+) (\d+) (\d+)""" ->
                    match4As<Int, Int, Int, Int>().let { (speed, x, y, z) ->
                        Fly(speed, PointInSpace(x, y, z))
                    }
                else                            -> Unknown
            }
        }

        expectThat(robotCommandMatcher.match("S")).isEqualTo(Start)
        expectThat(robotCommandMatcher.match("S bye")).isEqualTo(Say("bye"))
        expectThat(robotCommandMatcher.match("W 5 100 L")).isEqualTo(Wave(times = 5, vigour = 100, hand = 'L'))
        expectThat(robotCommandMatcher.match("F 100000000 1 0 0")).isEqualTo(
            Fly(
                speed = 100000000,
                place = PointInSpace(1, 0, 0)
            )
        )
        expectThat(robotCommandMatcher.match("lalala")).isEqualTo(Unknown)
    }

    data class PointInSpace(val x: Int, val y: Int, val z: Int)
    open class RobotCommand
    object Start : RobotCommand()
    data class Say(val something: String) : RobotCommand()
    data class Wave(val times: Int, val vigour: Int, val hand: Char) : RobotCommand()
    data class Fly(val speed: Int, val place: PointInSpace) : RobotCommand()
    object Unknown : RobotCommand()
}






