package aoc.days

import aoc.model.Day

fun main() {
    Day14().solve()
}

class Day14 : Day() {
    private val inputList = input().map { Instruction.from(it) }.toList()

    override fun part1() = runInstructions(inputList).values.sum()

    override fun part2() = TODO()
}

private fun runInstructions(instructions: Iterable<Instruction>): HashMap<Long, Long> {
    val memory = hashMapOf<Long, Long>()
    var mask = Instruction.Mask.EMPTY

    for (i in instructions) {
        when (i) {
            is Instruction.Mask -> mask = i
            is Instruction.Assignment -> memory[i.location] = mask.applyMask(i.value)
        }
    }
    return memory
}

private sealed class Instruction {
    class Mask(mask: String) : Instruction() {
        private val andMask = mask.replace('X', '1').toLong(2)
        private val orMask = mask.replace('X', '0').toLong(2)

        fun applyMask(i: Long) = i and andMask or orMask

        companion object {
            val EMPTY = Mask("X".repeat(36))
        }
    }

    data class Assignment(val location: Long, val value: Long) : Instruction()

    companion object {
        private val memPattern =
            """mem\[(\d+)] = (\d+)""".toRegex()

        fun from(instr: String) = if (instr.startsWith("mask")) Mask(instr.drop(7))
        else memPattern.matchEntire(instr)!!.groupValues.let { (_, l, v) -> Assignment(l.toLong(), v.toLong()) }
    }
}