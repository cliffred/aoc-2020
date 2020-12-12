package aoc.days

import aoc.days.Direction.EAST
import aoc.days.Direction.NORTH
import aoc.days.Direction.SOUTH
import aoc.days.Direction.WEST
import aoc.model.Day
import kotlin.math.abs

fun main() {
    Day12().solve()
}

class Day12 : Day() {
    private val instructions1 = input().map { parseShipInstruction(it) }
    private val instructions2 = input().map { parseWaypointInstruction(it) }

    override fun part1() = instructions1
        .fold(Vector(EAST, 0, 0)) { p, i -> i.move(p) }
        .let { abs(it.x) + abs(it.y) }
        .toLong()

    override fun part2() = instructions2
        .fold(ShipAndWaypoint(0, 0, 10, 1)) { snw, i -> i.move(snw) }
        .let { abs(it.shipX) + abs(it.shipY) }
        .toLong()
}

private fun interface ShipInstruction {
    fun move(p: Vector): Vector
}

private fun parseShipInstruction(instr: String): ShipInstruction {
    val c = instr.first()
    val value = instr.drop(1).toInt()
    return when (c) {
        'N' -> ShipInstruction { it.copy(y = it.y + value) }
        'S' -> ShipInstruction { it.copy(y = it.y - value) }
        'E' -> ShipInstruction { it.copy(x = it.x + value) }
        'W' -> ShipInstruction { it.copy(x = it.x - value) }
        'L' -> ShipInstruction { p ->
            val turns = value / 90
            var newDirection = p.direction
            repeat(turns) { newDirection = newDirection.left() }
            p.copy(direction = newDirection)
        }
        'R' -> ShipInstruction { p ->
            val turns = value / 90
            var newDirection = p.direction
            repeat(turns) { newDirection = newDirection.right() }
            p.copy(direction = newDirection)
        }
        'F' -> ShipInstruction { p ->
            when (p.direction) {
                NORTH -> p.copy(y = p.y + value)
                SOUTH -> p.copy(y = p.y - value)
                EAST -> p.copy(x = p.x + value)
                WEST -> p.copy(x = p.x - value)
            }
        }
        else -> throw IllegalStateException()
    }
}

private fun interface WaypointInstruction {
    fun move(shipAndWaypoint: ShipAndWaypoint): ShipAndWaypoint
}

private fun parseWaypointInstruction(instr: String): WaypointInstruction {
    val c = instr.first()
    val value = instr.drop(1).toInt()
    return when (c) {
        'N' -> WaypointInstruction { it.copy(wpY = it.wpY + value) }
        'S' -> WaypointInstruction { it.copy(wpY = it.wpY - value) }
        'E' -> WaypointInstruction { it.copy(wpX = it.wpX + value) }
        'W' -> WaypointInstruction { it.copy(wpX = it.wpX - value) }
        'R' -> WaypointInstruction {
            val turns = value / 90
            var newWpX = it.wpX
            var newWpY = it.wpY
            repeat(turns) {
                val tmpX = newWpX
                newWpX = newWpY
                newWpY = -tmpX
            }
            it.copy(wpX = newWpX, wpY = newWpY)
        }
        'L' -> WaypointInstruction {
            val turns = value / 90
            var newWpX = it.wpX
            var newWpY = it.wpY
            repeat(turns) {
                val tmpX = newWpX
                newWpX = -newWpY
                newWpY = tmpX
            }
            it.copy(wpX = newWpX, wpY = newWpY)
        }
        'F' -> WaypointInstruction { it.copy(shipX = it.shipX + it.wpX * value, shipY = it.shipY + it.wpY * value) }
        else -> throw IllegalStateException()
    }
}

private enum class Direction {
    NORTH, SOUTH, EAST, WEST;

    fun left() = when (this) {
        NORTH -> WEST
        SOUTH -> EAST
        EAST -> NORTH
        WEST -> SOUTH
    }

    fun right() = when (this) {
        NORTH -> EAST
        SOUTH -> WEST
        EAST -> SOUTH
        WEST -> NORTH
    }
}

private data class Vector(val direction: Direction, val x: Int, val y: Int)
private data class ShipAndWaypoint(val shipX: Int, val shipY: Int, val wpX: Int, val wpY: Int)