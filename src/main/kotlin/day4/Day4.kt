package org.baec23.day4

import org.baec23.util.readLines
import kotlin.math.pow
import kotlin.math.roundToInt

fun main() {
    val input = readLines(day = 4, isSample = false)
    print(partTwo(input))
}

private fun partTwo(input: List<String>): Int {
    val originalCards = input.map { Card.fromString(it) }
    var numCards = 0
    originalCards.forEach { card ->
        numCards += findNumCards(originalCards, card)
    }
    return numCards
}

private fun findNumCards(originalCards: List<Card>, card: Card): Int {
    var toReturn = 1
    for (i in 1..card.numMatches) {
        toReturn += findNumCards(originalCards, originalCards[card.cardNumber - 1 + i])
    }
    return toReturn
}

private fun partOne(input: List<String>): Int {
    val cards = input.map { Card.fromString(it) }
    return cards.sumOf { it.pointValue }
}

private data class Card(
    val cardNumber: Int,
    val winningNumbers: List<Int>,
    val myNumbers: List<Int>
) {
    companion object {
        fun fromString(string: String): Card {

            val cardNumber = string.takeWhile { it != ':' }.split("\\s+".toRegex()).last().toInt()
            val numbersStrings = string.takeLastWhile { it != ':' }.trim().split('|')
            val winningNumbers = numbersStrings.first().trim().split("\\s+".toRegex()).map { it.toInt() }
            val myNumbers = numbersStrings.last().trim().split("\\s+".toRegex()).map { it.toInt() }
            return Card(cardNumber, winningNumbers, myNumbers)
        }
    }

    val numMatches: Int
        get() {
            var numMatches = 0
            for (number in myNumbers) {
                if (number in winningNumbers) {
                    numMatches++
                }
            }
            return numMatches
        }

    val pointValue: Int
        get() {
            if (numMatches == 0) {
                return 0
            }
            return 2f.pow(numMatches - 1).roundToInt()
        }
}