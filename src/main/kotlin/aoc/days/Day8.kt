package aoc.days

import aoc.model.Day

fun main() {
    Day8().solve()
}

class Day8 : Day() {
    private val inputList = input().map { parseInstr(it) }.toList()

    override fun part1() = exec(inputList).acc.toLong()

    override fun part2() = findCorruptInstr(inputList).acc.toLong()
}

private fun exec(instructions: List<Instr>): ExecResult {
    val visited = mutableSetOf<Int>()
    var acc = 0
    var instrIndex = 0
    while (instrIndex != instructions.size && visited.add(instrIndex)) {
        val instr = instructions[instrIndex]
        acc += instr.acc()
        instrIndex += instr.next()
    }
    return if (instrIndex == instructions.size) Success(acc) else Failure(acc, instrIndex)
}

private fun parseInstr(input: String): Instr {
    val instr = input.substring(0, 3)
    val value = input.substring(4).toInt()

    return when (instr) {
        "acc" -> Acc(value)
        "jmp" -> Jmp(value)
        "nop" -> Nop(value)
        else -> throw IllegalArgumentException("$instr is invalid")
    }
}

private fun findCorruptInstr(instr: List<Instr>): ExecResult =
    modifiedInstructions(instr).map { exec(it) }.first { it is Success }

private fun modifiedInstructions(instrs: List<Instr>): Sequence<List<Instr>> {
    return sequence {
        instrs.forEachIndexed { i, instr ->
            val newInstrs = instrs.toMutableList()
            val swapped = swap(instr)
            if (instr.javaClass != swapped.javaClass) {
                newInstrs[i] = swapped
                yield(newInstrs)
            }
        }
    }
}

private fun swap(instr: Instr) = when (instr) {
    is Acc -> instr
    is Jmp -> Nop(instr.value)
    is Nop -> Jmp(instr.value)
}

private sealed class ExecResult(val acc: Int)

private class Success(acc: Int) : ExecResult(acc) {
    override fun toString() = "SUCCESS acc: $acc"
}

private class Failure(acc: Int, private val loopInstr: Int) : ExecResult(acc) {
    override fun toString() = "FAIL acc: $acc loopAt: $loopInstr"
}

private sealed class Instr {
    abstract fun acc(): Int
    abstract fun next(): Int
}

private class Acc(private val value: Int) : Instr() {
    override fun acc() = value
    override fun next() = 1
    override fun toString() = "acc $value"
}

private class Jmp(val value: Int) : Instr() {
    override fun acc() = 0
    override fun next() = value
    override fun toString() = "jpm $value"
}

private class Nop(val value: Int) : Instr() {
    override fun acc() = 0
    override fun next() = 1
    override fun toString() = "nop $value"
}