data class Game(
    val id: Int,
    val rounds: List<Round>
) {
    fun minimumRequiredCubes(): Round {
        val minRed = rounds.maxOf { it.red }
        val minGreen = rounds.maxOf { it.green }
        val minBlue = rounds.maxOf { it.blue }

        return Round(minRed, minGreen, minBlue)
    }
}

data class Round(
    val red: Int,
    val green: Int,
    val blue: Int
) {
    fun power(): Int {
        return red * green * blue
    }
}

fun getRound(input: String): Round {
    var red = 0
    var green = 0
    var blue = 0

    input.split(",").forEach { numberAndColor ->
        var digit = 0

        numberAndColor.split(" ").forEach {
            if (it.toIntOrNull() != null) {
                digit = it.toInt()
            } else {
                when (it) {
                    "red" -> {
                        red = digit
                    }

                    "blue" -> {
                        blue = digit
                    }

                    "green" -> {
                        green = digit
                    }
                }
            }
        }
    }

    return Round(red, green, blue)
}

fun createRounds(input: List<String>): List<Round> {
    return input.map { row ->
        getRound(row)
    }
}

fun createGames(input: List<String>): List<Game> {
    return input.map {
        val idPart = it.split(":")[0]
        val id = idPart.split(" ")[1].toInt()

        val roundPart = it.split(":")[1]
        val rounds = createRounds(roundPart.split(";"))

        Game(id, rounds)
    }
}

fun isRoundPossible(round: Round, bag: Round): Boolean {
    return round.red <= bag.red && round.green <= bag.green && round.blue <= bag.blue
}

fun getPossibleGames(games: List<Game>, bag: Round): List<Game> {
    val possibleGames = mutableListOf<Game>()
    for (game in games) {
        var isPossible = true
        for (round in game.rounds) {
            isPossible = isRoundPossible(round, bag)
            if (!isPossible) {
                break
            }
        }
        if (isPossible) {
            possibleGames.add(game)
        }
    }
    return possibleGames
}

fun main() {
    val input = readInput("day02_input")
    val games = createGames(input)

    fun part1() {
        val bag = Round(12, 13, 14)

        val possibleGames = getPossibleGames(games, bag)

        println(possibleGames.sumOf { it.id })
    }

    fun part2() {
        val minimumRequiredCubes = games.map { it.minimumRequiredCubes() }
        println(minimumRequiredCubes.sumOf { it.power() })
    }

    part1()

    part2()
}
