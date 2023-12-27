package org.baec23.day11

import org.baec23.util.readLines

fun main() {
    val input = readLines(day = 11, isSample = false)
    println(partOne(input))
}

private fun partOne(input: List<String>): Long {
    var toReturn = 0L
    val galaxies = mutableListOf<Pair<Int, Int>>()
    val expandedRowIndexes = mutableListOf<Int>()
    val expandedColIndexes = mutableListOf<Int>()
    //Find galaxies and expandedRowIndexes
    input.forEachIndexed { row, line ->
        var hasSeenGalaxy = false
        line.forEachIndexed { col, char ->
            if (char == '#') {
                galaxies.add(Pair(row, col))
                hasSeenGalaxy = true
            }
        }
        if (!hasSeenGalaxy) {
            expandedRowIndexes.add(row)
        }
    }
    //Find expandedColIndexes
    for (i in 0..<input[0].length) {
        val galaxyCount = galaxies.count { it.second == i }
        if (galaxyCount == 0) {
            expandedColIndexes.add(i)
        }
    }
    //Pair galaxies
    val unpairedGalaxies = HashSet<Pair<Int, Int>>()
    unpairedGalaxies.addAll(galaxies)
    galaxies.forEach { galaxy ->
        val filtered = unpairedGalaxies.filter { it != galaxy }
        filtered.forEach { otherGalaxy ->
            toReturn += getDistance(galaxy, otherGalaxy, expandedRowIndexes, expandedColIndexes)
        }
        unpairedGalaxies.remove(galaxy)
    }
    return toReturn
}

private fun getDistance(
    galaxy1: Pair<Int, Int>,
    galaxy2: Pair<Int, Int>,
    expandedRows: List<Int>,
    expandedCols: List<Int>
): Long {
    val rowRange = minOf(galaxy1.first, galaxy2.first)..maxOf(galaxy1.first, galaxy2.first)
    val colRange = minOf(galaxy1.second, galaxy2.second)..maxOf(galaxy1.second, galaxy2.second)
    var rowDiff = rowRange.last - rowRange.first.toLong()
    var colDiff = colRange.last - colRange.first.toLong()
    expandedRows.forEach {
        if (it in rowRange) {
            rowDiff += 1_000_000 - 1
        }
    }
    expandedCols.forEach {
        if (it in colRange) {
            colDiff += 1_000_000 - 1
        }
    }
    return rowDiff + colDiff
}