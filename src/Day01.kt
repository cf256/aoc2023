enum class StringDigits(val digit: Int) {
    One(1),
    Two(2),
    Three(3),
    Four(4),
    Five(5),
    Six(6),
    Seven(7),
    Eight(8),
    Nine(9)
}

enum class CompoundStringDigits(val digit: Int) {
    Oneight(18),
    Twone(21),
    Threeight(38),
    Fiveight(58),
    Sevenine(79),
    Eightwo(82),
    Eighthree(83),
    Nineight(98)
}

fun String.convertStringDigitsToDigits(): String {
    /**
     * Convert Compound digits first, then convert regular digits...
     */
    val compoundIndexes = mutableListOf<Pair<Int, CompoundStringDigits>>()

    CompoundStringDigits.entries.forEach {
        val index = this.indexOf(it.name, ignoreCase = true)
        if (index >= 0) {
            compoundIndexes.add(Pair(index, it))
        }
    }

    var compoundsReplaced = this
    compoundIndexes.sortedBy { it.first }.forEach {
        compoundsReplaced = compoundsReplaced.replace(it.second.name, it.second.digit.toString(), ignoreCase = true)
    }

    val firstReplacementIndexes = mutableListOf<Pair<Int, StringDigits>>()
    StringDigits.entries.forEach {
        val index = compoundsReplaced.indexOf(it.name, ignoreCase = true)
        if (index >= 0) {
            firstReplacementIndexes.add(Pair(index, it))
        }
    }

    var result = compoundsReplaced
    firstReplacementIndexes.sortedBy { it.first }.forEach {
        result = result.replace(it.second.name, it.second.digit.toString(), ignoreCase = true)
    }

    return result
}

fun main() {
    fun part1(input: List<String>): Int {
        val result = mutableListOf<Int>()

        input.forEach {
            val first = it.first { c -> c.isDigit() }
            val last = it.last { c -> c.isDigit() }
            val number = "$first$last".toInt()
            result.add(number)
        }

        return result.sumOf { it }
    }

    fun part2(input: List<String>): Int {
        val mapped = input.map {
            it.convertStringDigitsToDigits()
        }

        return part1(mapped)
    }

    val input = readInput("day01_input")

    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
