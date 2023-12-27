package org.baec23.day14

import org.baec23.util.readLines

fun main() {
    val input = readLines(day = 14, isSample = false)
    println(partOne(input))
}

fun partOne(input: List<String>): Long {
    var toReturn = 0L
    val maxLoad = input.size
    for (col in input[0].indices) {
        var currBase = 0
        var currRockNumber = 0
        for (row in input.indices) {
            if (input[row][col] == 'O') {
                toReturn += maxLoad - currBase - currRockNumber
                currRockNumber++
            } else if (input[row][col] == '#') {
                currBase = row + 1
                currRockNumber = 0
            }
        }
    }
    return toReturn
}