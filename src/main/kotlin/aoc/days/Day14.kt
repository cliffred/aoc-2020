package aoc.days

import aoc.model.Day

fun main() {
    Day14().solve()
}

class Day14 : Day() {
    private val instructionsV1 = input().map { Instruction.v1(it) }.toList()
    private val instructionsV2 = input().map { Instruction.v2(it) }.toList()

    override fun part1() = runInstructions(instructionsV1).values.sum()

    override fun part2() = runInstructions(instructionsV2).values.sum()
}

private fun runInstructions(instructions: Iterable<Instruction>): HashMap<Long, Long> {
    val memory = hashMapOf<Long, Long>()
    var mask: Instruction.Mask? = null

    for (i in instructions) {
        when (i) {
            is Instruction.Mask -> mask = i
            is Instruction.Assignment -> mask!!.applyMask(i).forEach { memory[it.location] = it.value }
        }
    }
    return memory
}

private sealed class Instruction {

    abstract class Mask : Instruction() {
        abstract fun applyMask(assignment: Assignment): List<Assignment>
    }

    class MaskV1(mask: String) : Mask() {
        private val andMask = mask.replace('X', '1').toLong(2)
        private val orMask = mask.replace('X', '0').toLong(2)
        override fun applyMask(assignment: Assignment) =
            listOf(Assignment(assignment.location, assignment.value and andMask or orMask))
    }

    class MaskV2(mask: String) : Mask() {
        private val baseMask = mask.replace('X', '0').toLong(2)
        private val floaters = mask.mapIndexedNotNull { i, c -> if (c == 'X') i else null }

        override fun applyMask(assignment: Assignment): List<Assignment> {
            val base = (assignment.location or baseMask).toString(2).padStart(36, '0')
            return bitCombos(floaters.size).map { bits ->
                val baseArray = base.toCharArray()
                floaters.forEachIndexed { i, f -> baseArray[f] = bits[i] }
                Assignment(String(baseArray).toLong(2), assignment.value)
            }
        }
    }

    data class Assignment(val location: Long, val value: Long) : Instruction()

    companion object {
        private val memPattern =
            """mem\[(\d+)] = (\d+)""".toRegex()

        fun v1(instr: String) = if (instr.startsWith("mask")) MaskV1(instr.drop(7)) else parseAssignment(instr)
        fun v2(instr: String) = if (instr.startsWith("mask")) MaskV2(instr.drop(7)) else parseAssignment(instr)

        private fun parseAssignment(instr: String) =
            memPattern.matchEntire(instr)!!.groupValues.let { (_, l, v) -> Assignment(l.toLong(), v.toLong()) }
    }
}

private fun bitCombos(n: Int) = (0 until (1 shl n)).map { it.toString(2).padStart(n, '0').toCharArray() }