package org.baec23.day7

import org.baec23.util.readLines
import org.baec23.util.splitByWhitespace

fun main() {
    val input = readLines(day = 7, isSample = false)
    println(partOne(input))
}

private fun partOne(input: List<String>): Long {
    val hands = input.map { PokerHand.fromString(it) }.sorted()
    var sum = 0L
    hands.forEachIndexed { index, hand ->
        sum += hand.bid * (index + 1)
    }
    return sum
}

private sealed class PokerHand(
    val value: Int,
    open val subValues: List<Int>,
    open val bid: Int,
    open val handString: String
) :
    Comparable<PokerHand> {
    companion object {
        fun fromString(inputString: String): PokerHand {
            val splitString = inputString.trim().splitByWhitespace()
            val handString = splitString.first()
            val bid = splitString.last().toInt()
            val cards = handString.map { Card(it) }
            val cardValues = cards.map { it.value }
            val cardGroups = cards.groupBy { it }.values.toList().sortedByDescending { it.size }
//            val cardGroups =
//                cards.sortedDescending().groupBy { it }.values.toList()
//                    .sortedByDescending { it.size }

            return when (cardGroups.size) {
                1 -> {
//                    FiveOfAKind(listOf(cardGroups.first().first().value), bid, handString)
                    FiveOfAKind(cardValues, bid, handString)
                }

                2 -> {
                    if (cardGroups.first().size == 4) {
//                        FourOfAKind(
//                            listOf(cardGroups.first().first().value, cardGroups.last().first().value),
//                            bid,
//                            handString
//                        )
                        FourOfAKind(
                            cardValues,
                            bid,
                            handString
                        )
                    } else {
//                        FullHouse(
//                            listOf(cardGroups.first().first().value, cardGroups.last().first().value),
//                            bid,
//                            handString
//                        )
                        FullHouse(
                            cardValues,
                            bid,
                            handString
                        )
                    }
                }

                3 -> {
                    val subValues = mutableListOf(cardGroups.first().first().value)
                    subValues.add(cardGroups[1].first().value)
                    subValues.add(cardGroups.last().first().value)
                    if (cardGroups.first().size == 3) {
//                        ThreeOfAKind(subValues.toList(), bid, handString)
                        ThreeOfAKind(cardValues, bid, handString)
                    } else {
//                        TwoPair(subValues.toList(), bid, handString)
                        TwoPair(cardValues, bid, handString)
                    }
                }

                4 -> {
                    val subValues = mutableListOf(cardGroups.first().first().value)
                    subValues.addAll(cardGroups.takeLast(3).flatten().map { it.value })
//                    OnePair(subValues.toList(), bid, handString)
                    OnePair(cardValues, bid, handString)
                }

                5 -> {
//                    HighCard(cardGroups.flatten().map { it.value }, bid, handString)
                    HighCard(cardValues, bid, handString)
                }

                else -> {
                    throw Exception("Invalid cardGroup size")
                }
            }
        }
    }

    override fun compareTo(other: PokerHand): Int {
        val primaryCompare = this.value.compareTo(other.value)
        if (primaryCompare == 0) {
            var index = 0
            while (index < subValues.size) {
                val myValue = subValues[index]
                val otherValue = other.subValues[index]
                val compare = myValue.compareTo(otherValue)
                if (compare != 0) {
                    return compare
                }
                index++
            }
        }
        return primaryCompare
    }

    data class FiveOfAKind(
        override val subValues: List<Int>,
        override val bid: Int,
        override val handString: String
    ) : PokerHand(6, subValues, bid, handString)

    data class FourOfAKind(
        override val subValues: List<Int>,
        override val bid: Int,
        override val handString: String
    ) : PokerHand(5, subValues, bid, handString)

    data class FullHouse(
        override val subValues: List<Int>,
        override val bid: Int,
        override val handString: String
    ) : PokerHand(4, subValues, bid, handString)

    data class ThreeOfAKind(
        override val subValues: List<Int>,
        override val bid: Int,
        override val handString: String
    ) : PokerHand(3, subValues, bid, handString)

    data class TwoPair(
        override val subValues: List<Int>,
        override val bid: Int,
        override val handString: String
    ) : PokerHand(2, subValues, bid, handString)

    data class OnePair(
        override val subValues: List<Int>,
        override val bid: Int,
        override val handString: String
    ) : PokerHand(1, subValues, bid, handString)

    data class HighCard(
        override val subValues: List<Int>,
        override val bid: Int,
        override val handString: String
    ) : PokerHand(0, subValues, bid, handString)
}

private data class Card(val cardChar: Char) : Comparable<Card> {
    val value = when (cardChar) {
        'A' -> 12
        'K' -> 11
        'Q' -> 10
        'J' -> 9
        'T' -> 8
        '9' -> 7
        '8' -> 6
        '7' -> 5
        '6' -> 4
        '5' -> 3
        '4' -> 2
        '3' -> 1
        '2' -> 0
        else -> throw Exception("Invalid Card")
    }

    override fun compareTo(other: Card): Int {
        return this.value.compareTo(other.value)
    }
}