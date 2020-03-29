package com.github.tompower

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.Test

class KatcherTest {

    @Test
    fun `can match a list of String typed capture groups`() {
        val matcher: Katcher.() -> String = {
            when (input) {
                """([a-z]+) (\d+)""" -> matches.let { (letters, numbers) -> "$letters, $numbers" }
                else                 -> "not found"
            }
        }

        assertThat("abc 123" match matcher, equalTo("abc, 123"))
        assertThat("lalala" match matcher, equalTo("not found"))
    }

    @Test
    fun `can match Int typed capture groups`() {
        val matcher: Katcher.() -> Int = {
            when (input) {
                """(\d+) (\d+)""" -> match2As<Int, Int>().run { first + second }
                else              -> Int.MAX_VALUE
            }
        }

        assertThat("1 2" match matcher, equalTo(3))
        assertThat("lalala" match matcher, equalTo(Int.MAX_VALUE))
    }

    @Test
    fun `can match Char typed capture groups`() {
        val matcher: Katcher.() -> Char = {
            when (input) {
                """([a-zA-Z])""" -> match1As<Char>().first
                else             -> 'N'
            }
        }

        assertThat("C" match matcher, equalTo('C'))
        assertThat("lalala" match matcher, equalTo('N'))
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

        assertThat("S" match robotCommandMatcher, equalTo(Start as RobotCommand))
        assertThat("S bye" match robotCommandMatcher, equalTo(Say("bye") as RobotCommand))
        assertThat(
            "W 5 100 L" match robotCommandMatcher,
            equalTo(Wave(times = 5, vigour = 100, hand = 'L') as RobotCommand)
        )
        assertThat(
            "F 100000000 1 0 0" match robotCommandMatcher,
            equalTo(Fly(speed = 100000000, place = PointInSpace(1, 0, 0)) as RobotCommand)
        )
        assertThat("lalala" match robotCommandMatcher, equalTo(Unknown as RobotCommand))
    }

    data class PointInSpace(val x: Int, val y: Int, val z: Int)
    open class RobotCommand
    object Start : RobotCommand()
    data class Say(val something: String) : RobotCommand()
    data class Wave(val times: Int, val vigour: Int, val hand: Char) : RobotCommand()
    data class Fly(val speed: Int, val place: PointInSpace) : RobotCommand()
    object Unknown : RobotCommand()
}






