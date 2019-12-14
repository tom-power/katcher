package com.github.tompower

import junit.framework.TestCase.assertEquals
import org.junit.Test

class KatcherTest {

    @Test
    fun `can match stringly typed capture groups`() {
        val matcher: Katcher.(CharSequence) -> String = {
            when (it) {
                in """([a-z]+) (\d+)""" -> match().let { (letters, numbers) -> "$letters, $numbers" }
                else                    -> "not found"
            }
        }

        assertEquals("abc, 123", "abc 123" matches matcher)
        assertEquals("not found", "lalala" matches matcher)
    }

    @Test
    fun `can match stringly typed capture groups using anonymous function syntax`() {
        val matcher = fun Katcher.(input: CharSequence): String =
            when (input) {
                in """([a-z]+) (\d+)""" -> match().let { (letters, numbers) -> "$letters, $numbers" }
                else                    -> "not found"
            }

        assertEquals("abc, 123", "abc 123" matches matcher)
        assertEquals("not found", "lalala" matches matcher)
    }

    @Test
    fun `can match stringly typed capture groups in a function`() {
        fun match(input: String): String =
            input matches {
                when (it) {
                    in """([a-z]+) (\d+)""" -> match().let { (letters, numbers) -> "$letters, $numbers" }
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
                in """([a-z]+) (\d+)""" -> match2<String, String>().let { (letters, numbers) -> "$letters, $numbers" }
                else                    -> "not found"
            }
        }

        assertEquals("abc, 123", "abc 123" matches matcher)
        assertEquals("not found", "lalala" matches matcher)
    }

    @Test
    fun `can match int typed capture groups`() {
        val matcher: Katcher.(CharSequence) -> Int = {
            when (it) {
                in """(\d+) (\d+)""" -> match2<Int, Int>().let { (first, second) -> first + second }
                else                 -> Int.MAX_VALUE
            }
        }

        assertEquals(3, "1 2" matches matcher)
        assertEquals(Int.MAX_VALUE, "lalala" matches matcher)
    }

    @Test
    fun `can match char typed groups`() {
        val matcher: Katcher.(CharSequence) -> Char = {
            when (it) {
                in """([a-zA-Z])""" -> match1<Char>().let { (char) -> char }
                else                -> 'N'
            }
        }

        assertEquals('C', "C" matches matcher)
        assertEquals('N', "lalala" matches matcher)
    }

    @Test
    fun `can match multiple and mixed typed capture groups`() {
        val robotCommandMatcher: Katcher.(CharSequence) -> RobotCommand = {
            when (it) {
                in """S"""                         -> Start
                in """S ([a-zA-Z]+)"""             -> match().let { (something) -> Say(something) }
                in """W (\d+) (\d+) ([a-zA-Z])"""  ->
                    match3<Int, Int, Char>().let { (times, vigour, hand) ->
                        Wave(times, vigour, hand)
                    }
                in """F (\d+) (\d+) (\d+) (\d+)""" ->
                    match4<Int, Int, Int, Int>().let { (speed, x, y, z) ->
                        Fly(speed, PointInSpace(x, y, z))
                    }
                else                               -> Unknown
            }
        }

        assertEquals(Start, "S" matches robotCommandMatcher)
        assertEquals(Say("bye"), "S bye" matches robotCommandMatcher)
        assertEquals(Wave(times = 5, vigour = 100, hand = 'L'), "W 5 100 L" matches robotCommandMatcher)
        assertEquals(
            Fly(speed = 100000000, place = PointInSpace(1, 0, 0)),
            "F 100000000 1 0 0" matches robotCommandMatcher
        )
        assertEquals(Unknown, "lalala" matches robotCommandMatcher)
    }

    data class PointInSpace(val x: Int, val y: Int, val z: Int)
    open class RobotCommand
    object Start : RobotCommand()
    data class Say(val something: String) : RobotCommand()
    data class Wave(val times: Int, val vigour: Int, val hand: Char) : RobotCommand()
    data class Fly(val speed: Int, val place: PointInSpace) : RobotCommand()
    object Unknown : RobotCommand()
}






