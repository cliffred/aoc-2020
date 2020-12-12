package aoc.days

import aoc.model.Day

fun main() {
    Day7().solve()
}

class Day7 : Day() {
    override fun part1() = findContainers(simpleGraph(input()), "shiny gold").size.toLong()

    override fun part2() = containingBags(createGraph(input()), "shiny gold").toLong()
}

val rulePattern1 =
    """(\w+ \w+) bags contain (.+)""".toRegex()
val rulePattern2 =
    """(\d+) (\w+ \w+) bags?(?:, |\.)""".toRegex()

fun createGraph(lines: Sequence<String>): Map<String, Set<Pair<Int, String>>> = lines
    .map { rulePattern1.matchEntire(it)!! }
    .map { Pair(it.groupValues[1], it.groupValues[2]) }
    .associate { (outer, inner) ->
        val containing = rulePattern2.findAll(inner)
            .map { it.groupValues }
            .map { Pair(it[1].toInt(), it[2]) }
            .toSet()
        Pair(outer, containing)
    }

fun simpleGraph(lines: Sequence<String>): Map<String, Set<String>> =
    createGraph(lines).asSequence().associate { Pair(it.key, it.value.map { (_, bag) -> bag }.toSet()) }

fun findContainers(graph: Map<String, Set<String>>, bag: String): Set<String> {
    val containers = mutableSetOf<String>()
    val stack = ArrayDeque<String>().apply { addLast(bag) }

    while (stack.isNotEmpty()) {
        val currentBag = stack.removeLast()
        graph
            .filter { currentBag in it.value }
            .forEach {
                if (containers.add(it.key)) {
                    stack.addLast(it.key)
                }
            }
    }
    return containers
}

fun containingBags(graph: Map<String, Set<Pair<Int, String>>>, bag: String): Int {
    var containingBags = 0
    graph[bag]!!.forEach {
        containingBags += (it.first + (it.first * containingBags(graph, it.second)))
    }
    return containingBags
}