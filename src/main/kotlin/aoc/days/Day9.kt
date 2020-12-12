package aoc.days

import aoc.model.Day

fun main() {
    Day9().solve()
}

class Day9 : Day() {
    private val inputList = input().map { it.toLong() }.toList()

    override fun part1() = validate(inputList, 25)

    override fun part2() = consecSum(inputList, part1()).let { it.minOrNull()!! + it.maxOrNull()!! }
}

fun consecSum(nums: List<Long>, target: Long): List<Long> {
    for (i in 0..nums.lastIndex) {
        var j = 1
        while (true) {
            val slice = nums.slice(i..i + j)
            val sum = slice.sum()
            j++
            if (sum == target) {
                return slice
            }
            if (sum > target) {
                break
            }
        }
    }
    throw IllegalStateException("No sum")
}

fun validate(nums: List<Long>, preamble: Int): Long {
    for (i in preamble..nums.lastIndex) {
        val target = nums[i]
        if (!sumsUpTo(nums.slice(i - preamble until i), target)) {
            return target
        }
    }
    throw IllegalStateException("All is valid")
}

fun sumsUpTo(nums: Iterable<Long>, target: Long): Boolean {
    val numsSeen = mutableSetOf<Long>()
    nums.forEach {
        val complement = target - it
        if (complement in numsSeen) {
            return true
        }
        numsSeen += it
    }
    return false
}