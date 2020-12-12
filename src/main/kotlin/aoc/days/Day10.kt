package aoc.days

import aoc.model.Day

fun main() {
    Day10().solve()
}

class Day10 : Day() {
    private val inputList = input().map { it.toInt() }.toList()

    override fun part1() = chain(inputList).zipWithNext { a, b -> b - a }.let { jolts ->
        jolts.count { it == 1 } * jolts.count { it == 3 }
    }.toLong()

    override fun part2() = countArrangements(chain(inputList))
}

private fun chain(adapters: List<Int>) = adapters.toMutableList().apply {
    sort()
    add(0, 0)
    add(last() + 3)
}

private fun countArrangements(nums: List<Int>): Long {
    val cache = hashMapOf<List<Int>, Long>()
    fun mem(nums: List<Int>): Long {
        cache[nums]?.let { return it }
        return if (nums.size == 2) 1 else {
            val allButFirst = nums.drop(1)
            mem(allButFirst).also { cache[allButFirst] = it } + if (nums[2] - nums[0] > 3) 0 else {
                val allButSecond = nums.slice(0 until 1) + nums.drop(2)
                mem(allButSecond).also { cache[allButSecond] = it }
            }
        }
    }
    return mem(nums)
}