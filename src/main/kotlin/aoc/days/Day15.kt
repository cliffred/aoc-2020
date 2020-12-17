package aoc.days

import aoc.model.Day

fun main() {
    Day15().solve()
}

class Day15 : Day() {
    private val startingNumbers = input().first().split(',').map { it.toInt() }

    override fun part1() = numberSpoken(startingNumbers, 2020).toLong()

    override fun part2() = numberSpoken(startingNumbers, 30_000_000).toLong()
}

fun numberSpoken(startingNumbers: List<Int>, endTurn: Int): Int {
    if (endTurn <= startingNumbers.size) {
        return startingNumbers[endTurn - 1]
    }
    val numbers = startingNumbers.dropLast(1).withIndex().associate { Pair(it.value, it.index + 1) }.toMutableMap()
    val startTurn = startingNumbers.size + 1
    var previousNumber = startingNumbers.last()
    for (turn in startTurn..endTurn) {
        val currentNumber = numbers[previousNumber]?.let { turn - 1 - it } ?: 0
        numbers[previousNumber] = turn - 1
        previousNumber = currentNumber
    }
    return previousNumber
}