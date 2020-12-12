package aoc.model

import kotlin.system.measureTimeMillis

abstract class Day {
    protected fun input() = readInputFile(javaClass.simpleName)

    abstract fun part1(): Long

    abstract fun part2(): Long

    fun solve() {
        println("=== ${javaClass.simpleName} ===")
        measureTimeMillis {
            print("part 1: ${part1()}")
        }.let { println(" (${it}ms)") }
        measureTimeMillis {
            print("part 2: ${part2()}")
        }.let { println(" (${it}ms)") }
    }
}

private fun readInputFile(name: String) =
    Thread.currentThread().contextClassLoader
        .getResourceAsStream("${name.toLowerCase()}.txt")!!
        .bufferedReader()
        .lineSequence()