package aoc.days

import aoc.model.Day
import kotlin.math.ceil

fun main() {
    listOf("7", "13", "x", "x", "59", "x", "31", "19")
        .mapIndexed { i, b -> Pair(b, i) }
        .filterNot { it.first == "x" }
        .map { (b, i) -> Pair(b.toInt(), i) }
        .let {
            println(findLowestTime(it))
        }
    Day13().solve()
}

class Day13 : Day() {
    private val inputList = input().toList()

    override fun part1(): Long {
        val firstDepart = inputList.first().toLong()
        val busIds = inputList[1].split(',')
            .filterNot { it == "x" }
            .map { it.toInt() }
        return findBus(firstDepart, busIds).let { it.first * (it.second - firstDepart) }
    }

    override fun part2(): Long {
        val busWithOffsets = inputList[1].split(',')
            .mapIndexed { i, b -> Pair(b, i) }
            .filterNot { it.first == "x" }
            .map { (b, i) -> Pair(b.toInt(), i) }
            .toList()
        return findLowestTime(busWithOffsets)
    }
}

typealias BusAndTime = Pair<Int, Long>
typealias BusAndOffset = Pair<Int, Int>

private fun findBus(startTime: Long, busIds: Iterable<Int>): BusAndTime =
    busIds.map { id -> Pair(id, (ceil(startTime.toDouble() / id).toLong() * id)) }.minByOrNull { it.second }!!

private fun findLowestTime(busAndOffsets: List<BusAndOffset>): Long {
    var time = 0L
    var step = 1L
    for ((bus, offset) in busAndOffsets) {
        while ((time + offset) % bus != 0L) {
            time += step
        }
        step *= bus
    }
    return time
}

private inline fun Boolean.ifTrue(block: () -> Unit) = if (this) block() else Unit