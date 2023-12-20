package org.baec23.day6

import org.baec23.util.readLines
import org.baec23.util.splitByWhitespace
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

fun main() {
    val input = readLines(day = 6, isSample = false)
    val times = input[0].takeLastWhile { it != ':' }.trim().splitByWhitespace().map { it.toLong() }
    val distances = input[1].takeLastWhile { it != ':' }.trim().splitByWhitespace().map { it.toLong() }
    println(partTwo(times, distances))
}

private fun partTwo(times: List<Long>, distances: List<Long>): Long {
    val time = times.joinToString(separator = "") { it.toString() }.toLong()
    val distance = distances.joinToString(separator = "") { it.toString() }.toLong()
    return partOne(listOf(time), listOf(distance))
}

private fun partOne(times: List<Long>, distances: List<Long>): Long {
    var product = 1L
    times.forEachIndexed { index, time ->
        val targetDistance = distances[index] + 1
        val disc = sqrt((time * time - 4 * targetDistance).toDouble())
        val rangeMin = ceil((time - disc) / 2).toInt()
        val rangeMax = floor((time + disc) / 2).toInt()
        product *= (rangeMax - rangeMin) + 1
    }
    return product
}