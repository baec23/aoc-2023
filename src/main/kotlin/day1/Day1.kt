package org.baec23.day1

import org.baec23.util.readLines


fun main() {
    print(findDigitSum())
}

private fun findDigitSum(): Int {
    val input = readLines(1)
    var total = 0
    input.forEach { line ->
        val char1 = findFirstDigit(line)
        val char2 = findLastDigit(line)
        val asNumber = (char1 + char2).toInt()
        total += asNumber
    }
    return total
}

private fun findFirstDigit(line: String): String {
    var minIndex = Int.MAX_VALUE
    var minDigit = ""
    DigitString.entries.forEach { digitString ->
        val firstIndex = line.indexOf(digitString.name)
        if (firstIndex in 0..<minIndex) {
            minIndex = firstIndex
            minDigit = digitString.asChar
        }
    }
    val firstIndex = line.indexOfFirst { it.isDigit() }
    if (firstIndex in 0..<minIndex) {
        minIndex = firstIndex
        minDigit = line[firstIndex].toString()
    }
    return minDigit
}

private fun findLastDigit(line: String): String {
    var maxIndex = -1
    var maxDigit = ""
    DigitString.entries.forEach { digitString ->
        val lastIndex = line.lastIndexOf(digitString.name)
        if (lastIndex > maxIndex) {
            maxIndex = lastIndex
            maxDigit = digitString.asChar
        }
    }
    val lastIndex = line.indexOfLast { it.isDigit() }
    if (lastIndex > maxIndex) {
        maxIndex = lastIndex
        maxDigit = line[lastIndex].toString()
    }
    return maxDigit
}

enum class DigitString(val asChar: String) {
    one("1"),
    two("2"),
    three("3"),
    four("4"),
    five("5"),
    six("6"),
    seven("7"),
    eight("8"),
    nine("9"),
}