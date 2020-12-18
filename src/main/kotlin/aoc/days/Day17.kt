package aoc.days

import aoc.model.Day

fun main() {
    Day17().solve()
}

class Day17 : Day() {
    private val inputList = input().toList()

    override fun part1(): Long {
        val grid3D = Grid(3)
        inputList.forEachIndexed { y, row ->
            row.withIndex().filter { it.value == '#' }.forEach { grid3D.activate(listOf(it.index, -y, 0)) }
        }
        return runCycles(grid3D, 6).getActive().size.toLong()
    }

    override fun part2(): Long {
        val grid4D = Grid(4)
        inputList.forEachIndexed { y, row ->
            row.withIndex().filter { it.value == '#' }.forEach { grid4D.activate(listOf(it.index, -y, 0, 0)) }
        }
        return runCycles(grid4D, 6).getActive().size.toLong()
    }
}

private fun runCycles(grid: Grid, i: Int): Grid {
    var currentGrid = grid
    repeat(i) {
        val nextGrid = Grid(grid.dimensions)
        currentGrid.forEach { point ->
            val activeNeighors = currentGrid.getActiveNeighbors(point).size
            val active = currentGrid.isActive(point)
            if ((active && (activeNeighors == 2 || activeNeighors == 3)) || (!active && activeNeighors == 3)) {
                nextGrid.activate(point)
            }
        }
        currentGrid = nextGrid
    }
    return currentGrid
}

private class Grid(val dimensions: Int) : Iterable<Point> {
    private val active = hashSetOf<Point>()

    fun isActive(point: Point) = point in active

    fun activate(point: Point) {
        require(point.size == dimensions)
        active += point
    }

    fun getActiveNeighbors(point: Point) = getNeighbors(point).intersect(active)

    fun getActive(): Set<Point> = active

    fun getNeighbors(point: Point): Set<Point> =
        cartesianProduct(-1..1, dimensions).map { diff ->
            val neighbor = mutableListOf<Int>()
            point.zip(diff).forEach { (p, d) ->
                neighbor += p + d
            }
            neighbor
        }.toMutableSet().apply { remove(point) }

    override fun iterator(): Iterator<Point> {
        val min = Array(dimensions) { Int.MAX_VALUE }
        val max = Array(dimensions) { Int.MIN_VALUE }
        active.forEach { point ->
            point.zip(min).withIndex().forEach {
                min[it.index] = if (it.value.first < it.value.second) it.value.first else it.value.second
            }
            point.zip(max).withIndex().forEach {
                max[it.index] = if (it.value.first > it.value.second) it.value.first else it.value.second
            }
        }
        min.forEachIndexed { index, i -> min[index] = i - 1 }
        max.forEachIndexed { index, i -> max[index] = i + 1 }
        return iterator {
            min.zip(max).map { (it.first..it.second).toList() }
                .let { cartesianProduct(it).forEach { point -> yield(point) } }
        }
    }
}

typealias Point = List<Int>

private fun <T> cartesianProduct(lists: List<List<T>>): Set<List<T>> =
    lists.fold(listOf(listOf<T>())) { acc, values ->
        acc.flatMap { list -> values.map { value -> list + value } }
    }.toSet()

private fun <T> cartesianProduct(list: List<T>, repeat: Int): Set<List<T>> =
    mutableListOf(list).let { allLists ->
        repeat(repeat - 1) {
            allLists += list
        }
        cartesianProduct(allLists)
    }

private fun cartesianProduct(range: IntRange, repeat: Int) = cartesianProduct(range.toList(), repeat)