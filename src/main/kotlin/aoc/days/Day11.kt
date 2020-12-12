package aoc.days

import aoc.model.Day

fun main() {
    Day11().solve()
}

class Day11 : Day() {
    private val inputArray = input().map { it.toCharArray() }.toList().toTypedArray()

    override fun part1() = seatArrangement(inputArray, 1, 4)
        .fold(0) { acc, chars -> acc + chars.count { it == '#' } }
        .toLong()

    override fun part2() = seatArrangement(inputArray, Int.MAX_VALUE, 5)
        .fold(0) { acc, chars -> acc + chars.count { it == '#' } }
        .toLong()
}

tailrec fun seatArrangement(map: Array<CharArray>, radius: Int, minOccupied: Int): Array<CharArray> {
    fun adjacent(row: Int, col: Int): Int {
        var occupied = 0
        val directions = (-1..1).flatMap { a -> (-1..1).map { b -> Pair(a, b) } }.filterNot { it == Pair(0, 0) }

        directions.forEach dir@{ dir ->
            var r = row
            var c = col
            repeat(radius) {
                r += dir.first
                c += dir.second
                if (r > map.lastIndex || r < 0 || c > map[r].lastIndex || c < 0 || map[r][c] == 'L') {
                    return@dir
                }
                if (map[r][c] == '#') {
                    occupied++
                    return@dir
                }
            }
        }
        return occupied
    }

    val newMap = Array(map.size) { CharArray(map[0].size) }
    var changed = false
    for (r in 0..map.lastIndex) {
        for (c in 0..map[r].lastIndex) {
            when (map[r][c]) {
                '.' -> newMap[r][c] = '.'
                'L' -> newMap[r][c] = if (adjacent(r, c) == 0) '#' else 'L'
                '#' -> newMap[r][c] = if (adjacent(r, c) >= minOccupied) 'L' else '#'
            }
            if (map[r][c] != newMap[r][c]) {
                changed = true
            }
        }
    }
    return if (!changed) newMap else seatArrangement(newMap, radius, minOccupied)
}