package aoc.days

import aoc.model.Day

fun main() {
    Day18().solve()
}

class Day18 : Day() {
    private val inputList = input().toList()

    override fun part1() = inputList.map { calc(it, ::simpleSolve) }.sum()

    override fun part2() = inputList.map { calc(it, ::plusFirstSolve) }.sum()
}

private fun calc(equation: String, secondSolve: (String) -> Long): Long {
    if ('(' !in equation) {
        return secondSolve(equation)
    }
    val a = equation.lastIndexOf('(')
    val b = equation.indexOf(')', a)
    val sub = calc(equation.substring(a + 1, b), secondSolve)
    return calc(equation.substring(0, a) + sub + equation.substring(b + 1), secondSolve)
}

private fun plusFirstSolve(equation: String): Long {
    if (('+' in equation).xor('*' in equation)) {
        return simpleSolve(equation)
    }
    val plusIdx = equation.indexOf('+')
    val firstNum = equation.substring(0, plusIdx - 1).substringAfterLast(' ')
    val secondNum = equation.substring(plusIdx + 2).takeWhile { it.isDigit() }
    val plusSum = "$firstNum + $secondNum"
    val ans = simpleSolve(plusSum)
    return plusFirstSolve(equation.replaceFirst(plusSum, ans.toString()))
}

private fun simpleSolve(equation: String): Long {
    var acc = 0L
    var lastOperator = Operator.PLUS
    equation.split(' ').forEach { expr ->
        if (expr.toLongOrNull() != null) {
            acc = lastOperator(acc, expr.toLong())
        } else {
            when (expr) {
                "+" -> lastOperator = Operator.PLUS
                "*" -> lastOperator = Operator.TIMES
            }
        }
    }
    return acc
}

private enum class Operator {
    PLUS {
        override fun invoke(a: Long, b: Long) = a + b
    },
    TIMES {
        override fun invoke(a: Long, b: Long) = a * b
    };

    abstract operator fun invoke(a: Long, b: Long): Long
}