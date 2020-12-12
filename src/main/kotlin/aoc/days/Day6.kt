package aoc.days

import aoc.model.Day

fun main() {
    Day6().solve()
}

class Day6 : Day() {
    override fun part1() = answers1(input()).map { it.size }.sum().toLong()

    override fun part2() = answers2(input()).map { it.size }.sum().toLong()
}

private fun answers1(lines: Sequence<String>): List<Set<Char>> {
    val answerPerGroup = mutableListOf<Set<Char>>()
    var answers = mutableSetOf<Char>()
    lines.forEach { line ->
        if (line.isBlank()) {
            answerPerGroup += answers
            answers = mutableSetOf()
        } else {
            answers.addAll(line.toCharArray().asIterable())
        }
    }
    if (answers.isNotEmpty()) {
        answerPerGroup += answers
    }

    return answerPerGroup
}

private fun answers2(lines: Sequence<String>): List<Set<Char>> {
    val answerPerGroup = mutableListOf<Set<Char>>()
    var answers = mutableSetOf<Char>()
    var newGroup = true
    lines.forEach { line ->
        if (line.isBlank()) {
            answerPerGroup += answers
            answers = mutableSetOf()
            newGroup = true
        } else {
            if (newGroup) {
                answers.addAll(line.toCharArray().asIterable())
                newGroup = false
            } else {
                answers.removeIf { it !in line.toCharArray() }
            }
        }
    }
    if (answers.isNotEmpty()) {
        answerPerGroup += answers
    }

    return answerPerGroup
}