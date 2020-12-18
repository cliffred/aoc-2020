package aoc.days

import aoc.model.Day

fun main() {
    Day17().solve()
}

class Day17 : Day() {
    private val inputList = input().toList()

    override fun part1(): Long {
        val grid3D = Grid3D()
        inputList.forEachIndexed { y, row ->
            row.withIndex().filter { it.value == '#' }.forEach { grid3D.activate(Point(it.index, -y, 0)) }
        }
        return runCycles(grid3D, 6).getActive().size.toLong()
    }

    override fun part2() = TODO()
}

private fun runCycles(grid3D: Grid3D, i: Int): Grid3D {
    var currentGrid3D = grid3D
    repeat(i) {
        val nextGrid3D = Grid3D()
        currentGrid3D.forEach { point ->
            val activeNeighors = currentGrid3D.getActiveNeighbors(point).size
            val active = currentGrid3D.isActive(point)
            if ((active && (activeNeighors == 2 || activeNeighors == 3)) || (!active && activeNeighors == 3)) {
                nextGrid3D.activate(point)
            }
        }
        currentGrid3D = nextGrid3D
    }
    return currentGrid3D
}

private class Grid3D : Iterable<Point> {
    private val active = hashSetOf<Point>()

    fun isActive(point: Point) = point in active

    fun activate(point: Point) {
        active += point
    }

    fun getActiveNeighbors(point: Point) = getNeighbors(point).intersect(active)

    fun getActive(): Set<Point> = active

    fun getNeighbors(point: Point): Set<Point> =
        (-1..1).flatMap { a ->
            (-1..1).flatMap { b ->
                (-1..1).map { c ->
                    val (x, y, z) = point
                    Point(x + a, y + b, z + c)
                }
            }
        }.toMutableSet().apply { remove(point) }

    override fun iterator(): Iterator<Point> {
        var minX = Int.MAX_VALUE
        var minY = Int.MAX_VALUE
        var minZ = Int.MAX_VALUE
        var maxX = Int.MIN_VALUE
        var maxY = Int.MIN_VALUE
        var maxZ = Int.MIN_VALUE
        active.forEach {
            minX = if (it.x < minX) it.x else minX
            minY = if (it.y < minY) it.y else minY
            minZ = if (it.z < minZ) it.z else minZ
            maxX = if (it.x > maxX) it.x else maxX
            maxY = if (it.y > maxY) it.y else maxY
            maxZ = if (it.z > maxZ) it.z else maxZ
        }
        minX--
        minY--
        minZ--
        maxX++
        maxY++
        maxZ++
        return iterator {
            (minX..maxX).forEach { x ->
                (minY..maxY).forEach { y ->
                    (minZ..maxZ).forEach { z ->
                        yield(Point(x, y, z))
                    }
                }
            }
        }
    }
}

private data class Point(val x: Int, val y: Int, val z: Int)