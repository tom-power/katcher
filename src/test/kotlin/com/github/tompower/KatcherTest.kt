package com.github.tompower

import junit.framework.TestCase.assertEquals
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

        assertEquals("abc, 123", "abc 123" match matcher)
        assertEquals("not found", "lalala" match matcher)
    }

    @Test
    fun `can match Int typed capture groups`() {
        val matcher: Katcher.() -> Int = {
            when (input) {
                """(\d+) (\d+)""" -> match2As<Int, Int>().run { first + second }
                else              -> Int.MAX_VALUE
            }
        }

        assertEquals(3, "1 2" match matcher)
        assertEquals(Int.MAX_VALUE, "lalala" match matcher)
    }

    @Test
    fun `can match Char typed capture groups`() {
        val matcher: Katcher.() -> Char = {
            when (input) {
                """([a-zA-Z])""" -> match1As<Char>().first
                else             -> 'N'
            }
        }

        assertEquals('C', "C" match matcher)
        assertEquals('N', "lalala" match matcher)
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

        assertEquals(Start, "S" match robotCommandMatcher)
        assertEquals(Say("bye"), "S bye" match robotCommandMatcher)
        assertEquals(Wave(times = 5, vigour = 100, hand = 'L'), "W 5 100 L" match robotCommandMatcher)
        assertEquals(
            Fly(speed = 100000000, place = PointInSpace(1, 0, 0)),
            "F 100000000 1 0 0" match robotCommandMatcher
        )
        assertEquals(Unknown, "lalala" match robotCommandMatcher)
    }

    data class PointInSpace(val x: Int, val y: Int, val z: Int)
    open class RobotCommand
    object Start : RobotCommand()
    data class Say(val something: String) : RobotCommand()
    data class Wave(val times: Int, val vigour: Int, val hand: Char) : RobotCommand()
    data class Fly(val speed: Int, val place: PointInSpace) : RobotCommand()
    object Unknown : RobotCommand()
}






