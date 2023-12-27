package org.baec23.day15

import org.baec23.util.readLines
import org.baec23.util.splitByChar

fun main() {
    val input = readLines(day = 15, isSample = false)
    val strings = input.first().splitByChar(',')
    var sum = 0L
    strings.forEach{
        sum += partOne(it)
    }
    println(sum)
}

fun partOne(input: String): Long {
    var toReturn = 0L
    input.forEach{char ->
        toReturn += char.code
        toReturn *= 17
        toReturn %= 256
    }
    return toReturn
}