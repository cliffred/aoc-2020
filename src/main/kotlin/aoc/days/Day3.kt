package aoc.days

import aoc.model.Day

fun main() {
    Day3().solve()
}

class Day3 : Day() {
    private val inputList = input().toList()

    override fun part1() = countTrees(inputList, 0, 0, 3, 1).toLong()

    override fun part2() = sequenceOf(Pair(1, 1), Pair(3, 1), Pair(5, 1), Pair(7, 1), Pair(1, 2))
        .map { countTrees(inputList, 0, 0, it.first, it.second) }
        .map { it.toLong() }
        .reduce { a, b -> a * b }
}

fun countTrees(map: List<String>, startX: Int, startY: Int, right: Int, down: Int): Int {
    val width = map.first().length
    var trees = 0
    var x = startX
    var y = startY

    do {
        x = if (x >= width) x - width else x
        if (map[y][x] == '#') {
            trees++
        }
        x += right
        y += down
    } while (y < map.size)

    return trees
}