package aoc.days

import aoc.model.Day

fun main() {
    Day4().solve()
}

class Day4 : Day() {
    private val passports = parsePassports(input())

    override fun part1() = passports.count { isValidPassport1(it) }.toLong()

    override fun part2() = passports.count { isValidPassport2(it) }.toLong()
}

fun parsePassports(lines: Sequence<String>): List<Map<String, String>> {
    val passports = mutableListOf<Map<String, String>>()
    var passport = mutableMapOf<String, String>()
    lines.forEach { line ->
        if (line.isEmpty()) {
            passports += passport
            passport = mutableMapOf()
        } else {
            val info = line.split(" ").associate { p -> p.split(":").let { Pair(it[0], it[1]) } }
            passport.putAll(info)
        }
    }
    if (passport.isNotEmpty()) {
        passports += passport
    }
    return passports
}

val requiredFields = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")

fun isValidPassport1(passport: Map<String, String>) =
    passport.keys.size >= 7 && passport.keys.containsAll(requiredFields)

fun isValidPassport2(passport: Map<String, String>): Boolean {
    return isValidPassport1(passport) && passport.all { validationRules[it.key]?.invoke(it.value) ?: false }
}

val heightPattern = Regex("(\\d+)(cm|in)")
val hairPattern = Regex("#[0-9a-f]{6}")
val eyeColours = setOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")

val validationRules = mapOf<String, (String) -> Boolean>(
    "byr" to { it.toInt() in 1920..2002 },
    "iyr" to { it.toInt() in 2010..2020 },
    "eyr" to { it.toInt() in 2020..2030 },
    "hgt" to {
        heightPattern.matchEntire(it)
            ?.run {
                if (groupValues[2] == "cm") {
                    groupValues[1].toInt() in 150..193
                } else {
                    groupValues[1].toInt() in 59..76
                }
            } ?: false
    },
    "hcl" to { it.matches(hairPattern) },
    "ecl" to { it in eyeColours },
    "pid" to { it.length == 9 && it.all { c -> c.isDigit() } },
    "cid" to { true }
)