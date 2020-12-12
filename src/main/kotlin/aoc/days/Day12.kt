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
    private val instructions = input().map { Instruction.parse(it) }

    override fun part1() = instructions
        .fold(Position(EAST, 0, 0)) { p, i -> i.move(p) }
        .let { abs(it.ewPos) + abs(it.nsPos) }
        .toLong()

    override fun part2() = TODO()
}

sealed class Instruction(val value: Int) {
    abstract fun move(p: Position): Position

    companion object {
        fun parse(instr: String): Instruction {
            val c = instr.first()
            val value = instr.drop(1).toInt()
            return when (c) {
                'N' -> N(value)
                'S' -> S(value)
                'E' -> E(value)
                'W' -> W(value)
                'L' -> L(value)
                'R' -> R(value)
                'F' -> F(value)
                else -> throw IllegalStateException()
            }
        }
    }
}

class N(value: Int) : Instruction(value) {
    override fun move(p: Position) = p.copy(nsPos = p.nsPos + value)
}

class S(value: Int) : Instruction(value) {
    override fun move(p: Position) = p.copy(nsPos = p.nsPos - value)
}

class E(value: Int) : Instruction(value) {
    override fun move(p: Position) = p.copy(ewPos = p.ewPos + value)
}

class W(value: Int) : Instruction(value) {
    override fun move(p: Position) = p.copy(ewPos = p.ewPos - value)
}

class L(value: Int) : Instruction(value) {
    override fun move(p: Position): Position {
        val turns = value / 90
        var newDirection = p.direction
        repeat(turns) { newDirection = newDirection.left() }
        return p.copy(direction = newDirection)
    }
}

class R(value: Int) : Instruction(value) {
    override fun move(p: Position): Position {
        val turns = value / 90
        var newDirection = p.direction
        repeat(turns) { newDirection = newDirection.right() }
        return p.copy(direction = newDirection)
    }
}

class F(value: Int) : Instruction(value) {
    override fun move(p: Position) = when (p.direction) {
        NORTH -> p.copy(nsPos = p.nsPos + value)
        SOUTH -> p.copy(nsPos = p.nsPos - value)
        EAST -> p.copy(ewPos = p.ewPos + value)
        WEST -> p.copy(ewPos = p.ewPos - value)
    }
}

enum class Direction {
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

data class Position(val direction: Direction, val ewPos: Int, val nsPos: Int)