package aoc.days

import aoc.model.Day
import kotlin.math.pow

fun main() {
    Day5().solve()
}

const val seats = 128 * 8

class Day5 : Day() {
    private val boardingPasses = input().map { seatId(it) }.toSet()

    override fun part1() = boardingPasses.maxOrNull()!!.toLong()

    override fun part2() = (0 until seats).first { it !in boardingPasses && it - 1 in boardingPasses }.toLong()
}

private fun seatId(seats: String): Int {
    val row = number(seats.toCharArray(0, 7), 'F', 'B')
    val column = number(seats.toCharArray(7, 10), 'L', 'R')
    return row * 8 + column
}

private fun number(chars: CharArray, lower: Char, higher: Char): Int {
    var low = 0
    var high = 2f.pow(chars.size).toInt() - 1
    chars.forEach { c ->
        val mid = low + high ushr 1
        if (c == higher) {
            low = mid + 1
        } else {
            high = mid
        }
    }
    return if (chars.last() == lower) low else high
}