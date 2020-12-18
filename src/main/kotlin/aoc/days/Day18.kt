package aoc.days

import aoc.model.Day

fun main() {
    Day18().solve()
}

class Day18 : Day() {
    private val inputList = input().toList()

    override fun part1() = inputList.map { calc(it) }.sum()

    override fun part2() = TODO()
}

private fun calc(sum: String): Long {
    if ('(' !in sum) {
        return simpleSolve(sum)
    }
    val a = sum.lastIndexOf('(')
    val b = sum.indexOf(')', a)
    val sub = calc(sum.substring(a + 1, b))
    return calc(sum.substring(0, a) + sub + sum.substring(b + 1))
}

private fun simpleSolve(sum: String): Long {
    var acc = 0L
    var lastOperator = Operator.PLUS
    sum.split(' ').forEach { expr ->
        if (expr.toLongOrNull() != null) {
            acc = lastOperator(acc, expr.toLong())
        } else {
            when (expr) {
                "+" -> lastOperator = Operator.PLUS
                "-" -> lastOperator = Operator.MINUS
                "*" -> lastOperator = Operator.TIMES
                "/" -> lastOperator = Operator.DIV
            }
        }
    }
    return acc
}

enum class Operator {
    PLUS {
        override fun invoke(a: Long, b: Long) = a + b
    },
    MINUS {
        override fun invoke(a: Long, b: Long) = a - b
    },
    TIMES {
        override fun invoke(a: Long, b: Long) = a * b
    },
    DIV {
        override fun invoke(a: Long, b: Long) = a / b
    };

    abstract operator fun invoke(a: Long, b: Long): Long
}