package org.baec23.day15

import org.baec23.util.readLines
import org.baec23.util.splitByChar

fun main() {
    val input = readLines(day = 15, isSample = false)
    println(partTwo(input))

}

private fun partTwo(input: List<String>): Long {
    val strings = input.first().splitByChar(',')
    val map = HashMap<Int, MutableList<Lens>>()
    strings.forEach { string ->

        if (string.endsWith('-')) {
            val label = string.takeWhile { it != '-' }
            val hash = label.toHash()
            map[hash]?.let { currList ->
                val index = currList.indexOfFirst { it.label == label }
                if (index >= 0) {
                    currList.removeAt(index)
                }
            }
        } else {
            val splitString = string.split('=')
            val label = splitString.first()
            val focalLength = splitString.last().toInt()
            val hash = label.toHash()
            val currList = map[hash] ?: mutableListOf()
            val index = currList.indexOfFirst { it.label == label }
            if (index < 0) {
                currList.add(Lens(label, focalLength))

            } else {
                currList[index] = Lens(label, focalLength)
            }
            map[hash] = currList
        }
    }
    var toReturn = 0L
    map.forEach { (hash, lenses) ->
        val a = hash + 1
        var b = 0L
        lenses.forEachIndexed { index, lens ->
            b += (index + 1) * lens.focalLength
        }
        toReturn += a * b

    }
    return toReturn
}

private data class Lens(
    val label: String,
    val focalLength: Int,
)

private fun String.toHash(): Int {
    var toReturn = 0
    this.forEach { char ->
        toReturn += char.code
        toReturn *= 17
        toReturn %= 256
    }
    return toReturn
}

private fun partOne(input: String): Long {
    var toReturn = 0L
    input.forEach { char ->
        toReturn += char.code
        toReturn *= 17
        toReturn %= 256
    }
    return toReturn
}
