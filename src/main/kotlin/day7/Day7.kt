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
            val cardGroups = cards.groupBy { it }.values.toList().sortedByDescending { it.size }.toMutableList()
            val jokers = cardGroups.find { list -> list.contains(Card('J')) }
            if (jokers != null && cardGroups.size > 1) {
                cardGroups.remove(jokers)
                val newFirstGroup = cardGroups.first().toMutableList()
                newFirstGroup.addAll(jokers)
                cardGroups.removeAt(0)
                cardGroups.add(0, newFirstGroup.toList())
            }

            return when (cardGroups.size) {
                1 -> {
                    FiveOfAKind(cardValues, bid, handString)
                }

                2 -> {
                    if (cardGroups.first().size == 4) {
                        FourOfAKind(
                            cardValues,
                            bid,
                            handString
                        )
                    } else {
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
                        ThreeOfAKind(cardValues, bid, handString)
                    } else {
                        TwoPair(cardValues, bid, handString)
                    }
                }

                4 -> {
                    val subValues = mutableListOf(cardGroups.first().first().value)
                    subValues.addAll(cardGroups.takeLast(3).flatten().map { it.value })
                    OnePair(cardValues, bid, handString)
                }

                5 -> {
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
        'J' -> 0
        'T' -> 9
        '9' -> 8
        '8' -> 7
        '7' -> 6
        '6' -> 5
        '5' -> 4
        '4' -> 3
        '3' -> 2
        '2' -> 1
        else -> throw Exception("Invalid Card")
    }

    override fun compareTo(other: Card): Int {
        return this.value.compareTo(other.value)
    }
}