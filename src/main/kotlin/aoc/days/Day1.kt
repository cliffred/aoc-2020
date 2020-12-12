package aoc.days

import aoc.model.Day

fun main() {
    Day1().solve()
}

class Day1 : Day() {
    private val inputList = input().map { it.toInt() }.toList()

    override fun part1() = twoSumMultiply(inputList, 2020).toLong()

    override fun part2() = threeSumMultiply(inputList, 2020).toLong()
}

private fun twoSumMultiply(nums: List<Int>, target: Int): Int {
    val numsSeen = mutableSetOf<Int>()
    nums.forEach {
        val complement = target - it
        if (complement in numsSeen) {
            return it * complement
        }
        numsSeen += it
    }
    throw IllegalArgumentException()
}

private fun threeSumMultiply(nums: List<Int>, target: Int): Int {
    val dubbleSum = mutableMapOf<Int, Set<Int>>()

    for (i in nums.indices) {
        for (j in i + 1 until nums.size) {
            val complement = target - nums[j]
            dubbleSum[complement]?.let {
                return nums[j] * it.reduce { a, b -> a * b }
            }
            dubbleSum[nums[i] + nums[j]] = setOf(nums[i], nums[j])
        }
    }
    throw IllegalArgumentException()
}