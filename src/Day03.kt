fun Char.isSymbol(): Boolean {
    return this == '/' ||
            this == '*' ||
            this == '=' ||
            this == '%' ||
            this == '@' ||
            this == '&' ||
            this == '-' ||
            this == '+' ||
            this == '$' ||
            this == '#'
}

data class EngineSchematicRow(val row: String) {

    private fun getNumbers(): Map<IntRange, Int> {
        val numbers = mutableMapOf<IntRange, Int>()

        var digit = ""
        var startIndex = -1
        var endIndex: Int

        for (i in row.indices) {
            val c = row[i]
            if (c.isDigit()) {
                digit += c
                if (startIndex == -1) {
                    startIndex = i
                }
            } else {
                if (digit.isNotEmpty()) {
                    endIndex = i - 1
                    numbers[IntRange(startIndex, endIndex)] = digit.toInt()
                    startIndex = -1
                }
                digit = ""
            }
        }

        // Add last digit
        if (digit.isNotEmpty()) {
            numbers[IntRange(startIndex, row.length)] = digit.toInt()
        }
        return numbers
    }

    fun getPartNumbers(rowAbove: EngineSchematicRow, rowBelow: EngineSchematicRow): Int {
        val numbers = getNumbers()

        var sum = 0
        val minIndex = 0
        val maxIndex = this.row.length - 1

        for ((range, number) in numbers) {
            val start = maxOf(minIndex, range.first - 1)
            val end = minOf(maxIndex, range.last + 1) + 1

            if (rowAbove.row.isNotEmpty()) {
                val sub = rowAbove.row.subSequence(start, end)
                if (sub.any { it.isSymbol() }) {
                    sum += number
                }
            }

            val thisRow = this.row.subSequence(start, end)
            if (thisRow.any { it.isSymbol() }) {
                sum += number
            }

            if (rowBelow.row.isNotEmpty()) {
                val sub = rowBelow.row.subSequence(start, end)
                if (sub.any { it.isSymbol() }) {
                    sum += number
                }
            }
        }

        return sum
    }

    fun getGearIndexes(): List<Int> {
        val result = mutableListOf<Int>()
        this.row.forEachIndexed { index, c ->
            if (c == '*') {
                result.add(index)
            }
        }
        return result
    }

    fun getGearRatios(rowAbove: EngineSchematicRow, rowBelow: EngineSchematicRow): Int {
        val gearIndexes = getGearIndexes()
        if (gearIndexes.isEmpty()) return 0

        val numbersAbove = rowAbove.getNumbers()
        val numbers = getNumbers()
        val numbersBelow = rowBelow.getNumbers()

        val maxIndex = this.row.length - 1

        var sum = 0
        gearIndexes.forEach { index ->
            println("Examining gear at index: $index")

            val adjacentNumbers = mutableListOf<Int>()

            for ((range, number) in numbersAbove) {
                val start = maxOf(0, range.first - 1)
                val end = minOf(maxIndex, range.last + 1)

                if (index in IntRange(start, end)) {
                    println("$index is in [$start, $end] -> Adjacent number: $number")
                    adjacentNumbers.add(number)
                }
            }

            for ((range, number) in numbers) {
                val start = maxOf(0, range.first - 1)
                val end = minOf(maxIndex, range.last + 1)

                if (index in IntRange(start, end)) {
                    println("$index is in [$start, $end] -> Adjacent number: $number")
                    adjacentNumbers.add(number)
                }
            }

            for ((range, number) in numbersBelow) {
                val start = maxOf(0, range.first - 1)
                val end = minOf(maxIndex, range.last + 1)

                if (index in IntRange(start, end)) {
                    println("$index is in [$start, $end] -> Adjacent number: $number")
                    adjacentNumbers.add(number)
                }
            }

            if (adjacentNumbers.size == 2) {
                sum += adjacentNumbers[0] * adjacentNumbers[1]
            }
        }

        return sum
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val rows = mutableListOf<EngineSchematicRow>()

        input.forEach {
            val row = EngineSchematicRow(it)
            rows.add(row)
        }
        var sum = 0
        rows.forEachIndexed { index, engineSchematicRow ->
            val above = rows.getOrElse(index - 1) {
                EngineSchematicRow("")
            }
            val below = rows.getOrElse(index + 1) {
                EngineSchematicRow("")
            }

            sum += engineSchematicRow.getPartNumbers(above, below)
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        val rows = mutableListOf<EngineSchematicRow>()

        input.forEach {
            val row = EngineSchematicRow(it)
            rows.add(row)
        }
        var sum = 0
        rows.forEachIndexed { index, engineSchematicRow ->
            val above = rows.getOrElse(index - 1) {
                EngineSchematicRow("")
            }
            val below = rows.getOrElse(index + 1) {
                EngineSchematicRow("")
            }

            sum += engineSchematicRow.getGearRatios(above, below)
        }

        return sum
    }

    val input = readInput("day03_input")

    println("part 1: ${part1(input)}")
    println("part 2: ${part2(input)}")
}
