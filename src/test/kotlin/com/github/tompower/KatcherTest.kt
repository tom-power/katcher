package com.github.tompower

import junit.framework.TestCase.assertEquals
import org.junit.Test

class KatcherTest {

    @Test
    fun `can match stringly typed capture groups`() {
        val matcher: Katcher.(CharSequence) -> String = {
            when (it) {
                in """([a-z]+) (\d+)""" -> matches().let { (letters, numbers) -> "$letters, $numbers" }
                else                    -> "not found"
            }
        }

        assertEquals("abc, 123", "abc 123" match matcher)
        assertEquals("not found", "lalala" match matcher)
    }

    @Test
    fun `can match stringly typed capture groups using anonymous function syntax`() {
        val matcher = fun Katcher.(input: CharSequence): String =
            when (input) {
                in """([a-z]+) (\d+)""" -> matches().let { (letters, numbers) -> "$letters, $numbers" }
                else                    -> "not found"
            }

        assertEquals("abc, 123", "abc 123" match matcher)
        assertEquals("not found", "lalala" match matcher)
    }

    @Test
    fun `can match stringly typed capture groups in a function`() {
        fun match(input: String): String =
            input match {
                when (it) {
                    in """([a-z]+) (\d+)""" -> matches().let { (letters, numbers) -> "$letters, $numbers" }
                    else                    -> "not found"
                }
            }

        assertEquals("abc, 123", match("abc 123"))
        assertEquals("not found", match("lalala"))
    }

    @Test
    fun `can match stringly typed capture groups specifying string type`() {
        val matcher: Katcher.(CharSequence) -> String = {
            when (it) {
                in """([a-z]+) (\d+)""" -> matches2<String, String>().let { (letters, numbers) -> "$letters, $numbers" }
                else                    -> "not found"
            }
        }

        assertEquals("abc, 123", "abc 123" match matcher)
        assertEquals("not found", "lalala" match matcher)
    }

    @Test
    fun `can match int typed capture groups`() {
        val matcher: Katcher.(CharSequence) -> Int = {
            when (it) {
                in """(\d+) (\d+)""" -> matches2<Int, Int>().let { (first, second) -> first + second }
                else                 -> Int.MAX_VALUE
            }
        }

        assertEquals(3, "1 2" match matcher)
        assertEquals(Int.MAX_VALUE, "lalala" match matcher)
    }

    @Test
    fun `can match char typed groups`() {
        val matcher: Katcher.(CharSequence) -> Char = {
            when (it) {
                in """([a-zA-Z])""" -> matches1<Char>().let { (char) -> char }
                else                -> 'N'
            }
        }

        assertEquals('C', "C" match matcher)
        assertEquals('N', "lalala" match matcher)
    }

    @Test
    fun `can match multiple and mixed typed capture groups`() {
        val robotCommandMatcher: Katcher.(CharSequence) -> RobotCommand = {
            when (it) {
                in """S"""                         -> Start
                in """S ([a-zA-Z]+)"""             -> matches().let { (something) -> Say(something) }
                in """W (\d+) (\d+) ([a-zA-Z])"""  ->
                    matches3<Int, Int, Char>().let { (times, vigour, hand) ->
                        Wave(times, vigour, hand)
                    }
                in """F (\d+) (\d+) (\d+) (\d+)""" ->
                    matches4<Int, Int, Int, Int>().let { (speed, x, y, z) ->
                        Fly(speed, PointInSpace(x, y, z))
                    }
                else                               -> Unknown
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






