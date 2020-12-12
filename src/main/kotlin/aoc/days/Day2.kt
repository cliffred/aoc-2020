package aoc.days

import aoc.model.Day

fun main() {
    Day2().solve()
}

class Day2 : Day() {
    override fun part1() = input().count { isValidOld(it) }.toLong()

    override fun part2() = input().count { isValidNew(it) }.toLong()
}

val passwordPattern = Regex("(\\d+)-(\\d+) ([a-z]): (.+)")

private fun isValidOld(entry: String): Boolean {
    val (from, to, char, password) = parsePasswordEntry(entry)
    val count = password.count { it == char }
    return count in from..to
}

private fun isValidNew(entry: String): Boolean {
    val (idx1, idx2, char, password) = parsePasswordEntry(entry)
    return (password[idx1 - 1] == char) xor (password[idx2 - 1] == char)
}

private fun parsePasswordEntry(entry: String): PasswordEntry {
    val (_, firstStr, secondStr, charStr, password) = passwordPattern.matchEntire(entry)!!.groupValues
    val char = charStr[0]
    val first = firstStr.toInt()
    val second = secondStr.toInt()
    return PasswordEntry(first, second, char, password)
}

private data class PasswordEntry(val first: Int, val second: Int, val char: Char, val password: String)