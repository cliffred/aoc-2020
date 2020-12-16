package aoc.days

import aoc.model.Day

fun main() {
    Day16().solve()
}

class Day16 : Day() {
    private val fields: List<Field>

    private val myTicket: Ticket

    private val nearbyTickets: List<Ticket>

    init {
        val fields = mutableListOf<Field>()
        var myTicket: Ticket? = null
        val nearbyTickets = mutableListOf<Ticket>()

        val parsers = ArrayDeque(
            listOf<(String) -> Unit>(
                { fields += Field.from(it) },
                { if (!it.endsWith(":")) myTicket = it.split(',').map { n -> n.toLong() } },
                { if (!it.endsWith(":")) nearbyTickets += it.split(',').map { n -> n.toLong() } }
            )
        )
        var parser = parsers.removeFirst()
        input().forEach { if (it.isBlank()) parser = parsers.removeFirst() else parser(it) }

        this.fields = fields
        this.myTicket = myTicket!!
        this.nearbyTickets = nearbyTickets
    }

    override fun part1() =
        nearbyTickets.flatMap { it.filterNot { num -> fields.any { field -> field.isValid(num) } } }.sum()

    override fun part2(): Long {
        val validTickets = nearbyTickets.filter { it.all { num -> fields.any { field -> field.isValid(num) } } }
        return findFieldsInOrder(validTickets, fields).zip(myTicket)
            .filter { it.first.name.startsWith("departure") }
            .map { it.second }
            .reduce { a, b -> a * b }
    }
}

private data class Field(val name: String, val rangeA: IntRange, val rangeB: IntRange) {
    fun isValid(num: Long) = num in rangeA || num in rangeB

    companion object {
        val fieldPattern = Regex("""(.*): (\d+)-(\d+) or (\d+)-(\d+)""")

        fun from(str: String) = fieldPattern.matchEntire(str)!!.groupValues.let {
            Field(
                it[1],
                it[2].toInt()..it[3].toInt(),
                it[4].toInt()..it[5].toInt()
            )
        }
    }
}

private fun findFieldsInOrder(validTickets: List<Ticket>, fields: List<Field>): Array<Field> {
    val possibleFields = (fields.indices).map { fields.toMutableSet() }

    validTickets.forEach { ticket ->
        ticket.withIndex().forEach { (i, num) ->
            fields.filterNot { it.isValid(num) }.forEach { field -> possibleFields[i].remove(field) }
        }
    }

    while (possibleFields.any { it.size > 1 }) {
        val toRemove = possibleFields.filter { it.size == 1 }.map { it.first() }
        toRemove.forEach { remove ->
            possibleFields.filter { it.size > 1 }.forEach { it.remove(remove) }
        }
    }

    return possibleFields.map { it.first() }.toTypedArray()
}

typealias Ticket = List<Long>