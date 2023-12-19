package org.baec23.day2

import org.baec23.util.readLines

fun main() {
    val input = readLines(day = 2, isSample = false)
    print(partTwo(input))
}

private fun partTwo(input: List<String>): Int {
    var powerSum = 0
    input.forEach { line ->
        val currGame = line.toGame()
        powerSum += currGame.power
    }
    return powerSum
}

private fun partOne(input: List<String>): Int {
    var idSum = 0
    input.forEach { line ->
        val currGame = line.toGame()
        if (currGame.isValid) {
            idSum += currGame.id
        }
    }
    return idSum
}

private fun String.toGame(): Game {
    val gameId = this.takeWhile { it != ':' }.split(" ").last().toInt()
    val roundStrings = this.takeLastWhile { it != ':' }.trim().split(';')
    val rounds = roundStrings.map { roundString ->
        var numRed = 0
        var numGreen = 0
        var numBlue = 0
        val results = roundString.split(',').map { it.trim() }
        results.forEach { result ->
            val splitResult = result.split(' ')
            when (splitResult.last()) {
                "red" -> {
                    numRed = splitResult.first().toInt()
                }

                "green" -> {
                    numGreen = splitResult.first().toInt()
                }

                "blue" -> {
                    numBlue = splitResult.first().toInt()
                }

                else -> {
                    throw Exception("Something wrong")
                }
            }
        }
        Round(numRed = numRed, numGreen = numGreen, numBlue = numBlue)
    }
    return Game(id = gameId, rounds = rounds)
}

private data class Game(
    val id: Int,
    val rounds: List<Round>
) {
    private val minRedCount: Int = rounds.maxOf { it.numRed }
    private val minGreenCount: Int = rounds.maxOf { it.numGreen }
    private val minBlueCount: Int = rounds.maxOf { it.numBlue }
    val power: Int = minRedCount * minGreenCount * minBlueCount
    val isValid: Boolean
        get() {
            var isValid = true
            rounds.forEach { if (!it.isValid) isValid = false }
            return isValid
        }
}

private data class Round(
    val numRed: Int = 0,
    val numGreen: Int = 0,
    val numBlue: Int = 0
) {
    val isValid =
        numRed <= MaxCubeCounts.red.max && numGreen <= MaxCubeCounts.green.max && numBlue <= MaxCubeCounts.blue.max

}

private enum class MaxCubeCounts(val max: Int) {
    red(12),
    green(13),
    blue(14)
}