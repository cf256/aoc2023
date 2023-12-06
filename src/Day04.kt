data class ScratchCard(
    val id: Int,
    val winningNumbers: List<Int>,
    val numbers: List<Int>
) {

    var bonusCards: List<ScratchCard> = emptyList()

    fun getPoints(): Int {
        val noOfWinningNumbers = winningNumbers.intersect(numbers.toSet()).size
        return Math.pow(2.toDouble(), (noOfWinningNumbers - 1).toDouble()).toInt()
    }

    fun getNumberOfCopiesWon(): Int {
        return winningNumbers.intersect(numbers.toSet()).size
    }

    companion object {
        private val whitespace = Regex("\\s+")
        fun fromLine(line: String): ScratchCard {
            val id = line
                .split(":")[0]
                .replace(whitespace, " ")
                .split(" ")[1].toInt()

            val winningNumbersString = line.split(":")[1].split("|")[0]
            val winningNumbers = winningNumbersString.replace(whitespace, " ").split(" ").filter {
                it.toIntOrNull() != null
            }.map {
                it.toInt()
            }

            val numbersString = line.split(":")[1].split("|")[1]
            val numbers = numbersString.replace(whitespace, " ").split(" ").filter {
                it.toIntOrNull() != null
            }.map {
                it.toInt()
            }

            return ScratchCard(id = id, winningNumbers = winningNumbers, numbers = numbers)
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val sum = input.map {
            ScratchCard.fromLine(it)
        }.sumOf {
            it.getPoints()
        }

        return sum
    }

    fun part2(input: List<String>): Int {
        val cards = input.map {
            ScratchCard.fromLine(it)
        }


        for (card in cards) {
            val copies = card.getNumberOfCopiesWon()
        }


        //for (card in cards) {
        val newList = mutableListOf<ScratchCard>()

        fun bonusCards(card: ScratchCard, stackOfCards: List<ScratchCard>) {
            if (stackOfCards.isEmpty()) return

            // add this card
            newList.add(card)

            val copiesWon = card.getNumberOfCopiesWon()
            val bonusCards = mutableListOf<ScratchCard>()

            for (i in 1..copiesWon) {
                if (i <= stackOfCards.lastIndex) {
                    bonusCards.add(cards[i])
                }
            }

            // add bonus cards
            newList.addAll(bonusCards)
            newList.sortBy { it.id }

            println(newList.map { it.id })

            for ((index, bonus) in bonusCards.withIndex()) {
                val copy = stackOfCards.slice(IntRange(index + 1, stackOfCards.lastIndex))
                bonusCards(bonus, copy)
            }
        }

        bonusCards(cards[0], cards.slice(IntRange(1, cards.lastIndex)))

        println(newList.size)
        return 0
    }

    val input = readInput("day04_input_test")

//    println("part 1: ${part1(input)}")
    println("part 2: ${part2(input)}")
}
