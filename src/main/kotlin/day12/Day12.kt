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
    input.forEachIndexed { index, line ->
        val splitLine = line.splitByWhitespace()
        val rawConditionString = splitLine.first()
        val rawDamagedLengths = splitLine.last().trim().splitByChar(',').map { it.toInt() }
        val damagedLengths = rawDamagedLengths + rawDamagedLengths + rawDamagedLengths + rawDamagedLengths + rawDamagedLengths
        val conditionString = "${rawConditionString}?${rawConditionString}?${rawConditionString}?${rawConditionString}?${rawConditionString}"
        val numUnknowns = conditionString.count { it == '?' }
        val unknowns = List<Char>(numUnknowns){'?'}.toString()
        val numArrangements = findNumArrangements2(conditionString, unknowns, damagedLengths, HashMap())
        toReturn += numArrangements
        println("Finished ${index+1}/${input.size}")

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

private fun findNumArrangements2(
    conditionString: String,
    unknowns: String,
    damagedLengths: List<Int>,
    memo: MutableMap<String, Long>
): Long {
    var toReturn = -1L
    memo[unknowns]?.let {
        return it
    }
    val unknownIndex = unknowns.indexOfFirst { it == '?' }
    if (unknownIndex == -1) {
        val constructedString = constructString(conditionString, unknowns)
        toReturn = if (constructedString.isValid(damagedLengths)) {
            1L
        } else {
            0L
        }
        memo[unknowns] = toReturn
        return toReturn
    }
    val operationalUnknown = unknowns.substring(0..<unknownIndex) + "." + unknowns.substring(unknownIndex+1..<unknowns.length)
    val damagedUnknown = unknowns.substring(0..<unknownIndex) + "#" + unknowns.substring(unknownIndex+1..<unknowns.length)

    toReturn = findNumArrangements2(conditionString, operationalUnknown, damagedLengths, memo) + findNumArrangements2(
        conditionString,
        damagedUnknown,
        damagedLengths,
        memo
    )
    memo[unknowns] = toReturn
    return toReturn
}

private fun constructString(conditionString: String, unknowns:String):String{
    var unknownIndex = 0
    val sb = StringBuilder()
    conditionString.forEach{
        if(it == '?'){
            sb.append(unknowns[unknownIndex])
            unknownIndex++
        }else{
            sb.append(it)
        }
    }
    return sb.toString()
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