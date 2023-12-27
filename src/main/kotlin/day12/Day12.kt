package org.baec23.day12

import org.baec23.util.readLines
import org.baec23.util.splitByChar
import org.baec23.util.splitByWhitespace

fun main() {
    val input = readLines(day = 12, isSample = true)
    println(partTwo(input))
}

private fun partTwo(input: List<String>): Long {
    var toReturn = 0L
    input.forEach { line ->
        val splitLine = line.splitByWhitespace()
        val conditionString = splitLine.first()
        val damagedLengths = splitLine.last().trim().splitByChar(',').map { it.toInt() }
        val expandedDamagedLengths = damagedLengths + damagedLengths + damagedLengths + damagedLengths + damagedLengths
        val expandedConditionString = "$conditionString?$conditionString?$conditionString?$conditionString?$conditionString"
        val expandedNumArrangements = findNumArrangements(expandedConditionString, expandedDamagedLengths, HashMap())
        println("Line: ${expandedConditionString} | numArrangements: ${expandedNumArrangements}")
        toReturn += expandedNumArrangements
    }
    return toReturn
}

private fun partOne(input: List<String>): Long {
    var toReturn = 0L
    input.forEach { line ->
        val splitLine = line.splitByWhitespace()
        val conditionString = splitLine.first()
        val damagedLengths = splitLine.last().trim().splitByChar(',').map { it.toInt() }
        val numArrangements = findNumArrangements(conditionString, damagedLengths, HashMap())
//        println("Line: ${conditionString} | numArrangements: ${numArrangements}")
        toReturn += numArrangements
    }
    return toReturn
}

private fun findNumArrangements(
    conditionString: String,
    damagedLengths: List<Int>,
    memo: MutableMap<String, Long>
): Long {
    memo[conditionString]?.let {
        return it
    }
    val unknownIndex = conditionString.indexOfFirst { it == '?' }
    var toReturn = -1L
    if (unknownIndex == -1) {
        toReturn = if (conditionString.isValid(damagedLengths)) {
            1L
        } else {
            0L
        }
        memo[conditionString] = toReturn
        return toReturn
    }
    val replacedWithOperational = conditionString.replaceRange(unknownIndex, unknownIndex + 1, ".")
    val replacedWithDamaged = conditionString.replaceRange(unknownIndex, unknownIndex + 1, "#")
    toReturn = findNumArrangements(replacedWithOperational, damagedLengths, memo) + findNumArrangements(
        replacedWithDamaged,
        damagedLengths,
        memo
    )
    memo[conditionString] = toReturn
    return toReturn
}

private fun String.isValid(damagedLengths: List<Int>): Boolean {
    val split = this.splitByChar('.')
    if (split.size != damagedLengths.size) {
        return false
    }
    damagedLengths.forEachIndexed { index, length ->
        if (split[index].length != length) {
            return false
        }
    }
    return true
}