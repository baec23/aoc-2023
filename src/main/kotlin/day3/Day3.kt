package org.baec23.day3

import org.baec23.util.readLines

fun main() {
    val input = readLines(day = 3, isSample = false)
    print(partOne(input))
}

private fun partTwo(input: List<String>): Int {
    val potentialNumbers = getPotentialNumbers(input)
    val potentialGears = mutableListOf<Gear>()
    input.forEachIndexed { lineIndex, line ->
        line.forEachIndexed { charIndex, char ->
            if (char == '*') {
                potentialGears.add(Gear(lineNum = lineIndex, index = charIndex))
            }
        }
    }
    var gearRatioSum = 0
    potentialGears.forEach { gear ->
        val lineRange = gear.lineNum - 1..gear.lineNum + 1
        val filteredNumbers = potentialNumbers.filter {
            it.lineNum in lineRange && gear.index in it.startIndex - 1..it.endIndex + 1
        }
        if (filteredNumbers.size == 2) {
            gearRatioSum += filteredNumbers.first().numberValue * filteredNumbers.last().numberValue
        }
    }
    return gearRatioSum
}

private fun partOne(input: List<String>): Int {
    val potentialNumbers = getPotentialNumbers(input)
    println("num potentialNumbers = ${potentialNumbers.size}")
    var partNumberSum = 0
    potentialNumbers.forEach { potentialNumber ->
        if (isPartNumber(input, potentialNumber.lineNum, potentialNumber.startIndex, potentialNumber.endIndex)) {
            partNumberSum += potentialNumber.numberValue
        }
    }
    return partNumberSum
}

private fun getPotentialNumbers(input: List<String>): List<SchematicNumber> {
    val currNumber = StringBuilder()
    val potentialNumbers = mutableListOf<SchematicNumber>()
    input.forEachIndexed { lineIndex, line ->
        line.forEachIndexed { charIndex, char ->
            when {
                char.isDigit() -> currNumber.append(char)
                else -> {
                    if (currNumber.isNotEmpty()) {
                        potentialNumbers.add(
                            SchematicNumber(
                                numberString = currNumber.toString(),
                                lineNum = lineIndex,
                                startIndex = charIndex - currNumber.length,
                                endIndex = charIndex - 1
                            )
                        )
                    }
                    currNumber.clear()
                }
            }
        }
        if (currNumber.isNotEmpty()) {
            potentialNumbers.add(
                SchematicNumber(
                    numberString = currNumber.toString(),
                    lineNum = lineIndex,
                    startIndex = line.length - currNumber.length,
                    endIndex = line.length - 1
                )
            )
        }
        currNumber.clear()
    }
    return potentialNumbers.toList()
}

private data class SchematicNumber(
    val numberString: String,
    val lineNum: Int,
    val startIndex: Int,
    val endIndex: Int,
) {
    val numberValue = numberString.toInt()
}

private data class Gear(
    val lineNum: Int,
    val index: Int
)

private fun isPartNumber(input: List<String>, lineNum: Int, startIndex: Int, endIndex: Int): Boolean {
    val rowRange = maxOf(0, lineNum - 1)..minOf(input.size - 1, lineNum + 1)
    val colRange = maxOf(0, startIndex - 1)..minOf(input[lineNum].length - 1, endIndex + 1)

    var isValid = false
    for (i in rowRange) {
        for (j in colRange) {
            if (!(input[i][j].isLetterOrDigit()) && input[i][j] != '.') {
                isValid = true
                break
            }
        }
    }
    return isValid
}