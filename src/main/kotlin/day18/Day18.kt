package org.baec23.day18

import org.baec23.util.readLines
import org.baec23.util.splitByWhitespace

fun main() {
    val input = readLines(day = 18, isSample = false)
    println("******")
    println("Area = ${partOne(input)}")
}

private fun partOne(input: List<String>): Long {
    var minX = 0
    var maxX = 0
    var minY = 0
    var maxY = 0
    var currX = 0
    var currY = 0
    val commands = mutableListOf<Command>()
    input.forEach { line ->
        val splitLine = line.splitByWhitespace()
        val command = splitLine.first().first()
        val length = splitLine[1].toInt()
        when (command) {
            'U' -> {
                commands.add(Command(CommandType.Up, length))
                currY -= length
            }

            'D' -> {
                commands.add(Command(CommandType.Down, length))
                currY += length
            }

            'L' -> {
                commands.add(Command(CommandType.Left, length))
                currX -= length
            }

            'R' -> {
                commands.add(Command(CommandType.Right, length))
                currX += length
            }

            else -> throw Exception("Something horribly wrong")
        }
        if (currX < minX) {
            minX = currX
        }
        if (currX > maxX) {
            maxX = currX
        }
        if (currY < minY) {
            minY = currY
        }
        if (currY > maxY) {
            maxY = currY
        }
    }
    val numRows = maxY - minY + 1
    val numCols = maxX - minX + 1
    var currRow = 0 - minY
    var currCol = 0 - minX
    val grid = Array(numRows) { CharArray(numCols) { '.' } }
    grid[currRow][currCol] = '#'
    commands.forEach { command ->
        when (command.type) {
            CommandType.Up -> {
                for (i in 0..<command.length) {
                    currRow--
                    grid[currRow][currCol] = '#'
                }
            }

            CommandType.Down -> {
                for (i in 0..<command.length) {
                    currRow++
                    grid[currRow][currCol] = '#'
                }
            }

            CommandType.Left -> {
                for (i in 0..<command.length) {
                    currCol--
                    grid[currRow][currCol] = '#'
                }
            }

            CommandType.Right -> {
                for (i in 0..<command.length) {
                    currCol++
                    grid[currRow][currCol] = '#'
                }
            }
        }
    }
//    grid.forEach { line ->
//        println(line)
//    }
    var perimeter = 0
    var area = 0
    for (row in 1..<grid.lastIndex) {
        //Get potential edges
        val currRow = grid[row]
        val potentialEdges = mutableListOf<IntRange>()
        var currRangeStart: Int? = null
        for (i in currRow.indices) {
            if (currRow[i] == '#' && currRangeStart == null) {
                currRangeStart = i
            } else if (currRow[i] == '.' && currRangeStart != null) {
                potentialEdges.add(currRangeStart..<i)
                currRangeStart = null
            }
        }
        if (currRangeStart != null) {
            potentialEdges.add(currRangeStart..currRow.lastIndex)
        }

        //Remove edges that are corners
        val edges = mutableListOf<IntRange>()
        potentialEdges.forEach { edge ->
            var hasPointAbove = false
            var hasPointBelow = false
            val rowAbove = grid[row - 1]
            val rowBelow = grid[row + 1]
            for (i in edge) {
                if (rowAbove[i] == '#') {
                    hasPointAbove = true
                }
                if (rowBelow[i] == '#') {
                    hasPointBelow = true
                }
            }
            if (hasPointAbove && hasPointBelow) {
                edges.add(edge)
            }
        }

        for (col in grid[row].indices) {
            if (grid[row][col] == '#') {
                perimeter++
            } else {
                if (isInside(edges, col, grid[row].lastIndex)) {
                    grid[row][col] = '?'
                    area++
                }
            }
        }
    }
    grid.forEach { line ->
        println(line)
    }
    var wtf = 0
    grid.forEach { line -> line.forEach { char -> if (char != '.') wtf++ } }
    println("WTF ? $wtf")
    return perimeter + area.toLong()
}

private fun isInside(edges: List<IntRange>, col: Int, rowLastIndex: Int): Boolean {
    var numIntersections = 0
    edges.forEach { edge ->
        if (edge.first > col && edge.last <= rowLastIndex) {
            numIntersections++
        }
    }
    val toReturn = numIntersections % 2 != 0
    return toReturn
}

private data class Command(
    val type: CommandType,
    val length: Int
)

private enum class CommandType {
    Up,
    Down,
    Left,
    Right
}