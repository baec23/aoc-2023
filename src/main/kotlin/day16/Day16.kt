package org.baec23.day16

import org.baec23.util.readLines
import org.baec23.util.splitByChar

fun main() {
    val input = readLines(day = 16, isSample = true)
    println(partOne(input))
}

fun partOne(input: List<String>): Int {
    val energizedTiles = HashSet<Pair<Int, Int>>()
    walk(input, 0, 0, Direction.East, energizedTiles)

    val visual = Array(input.size) { CharArray(input[0].length) { '.' } }
    energizedTiles.forEach { pair ->
        val row = pair.first
        val col = pair.second
        visual[row][col] = '#'
    }
    visual.forEach { row ->
        println(row)
    }
    return energizedTiles.size
}

private fun walk(
    input: List<String>,
    row: Int,
    col: Int,
    direction: Direction,
    energizedTiles: MutableSet<Pair<Int, Int>>
) {
    if (row !in input.indices || col !in input[0].indices) {
        return
    }
    energizedTiles.add(Pair(row, col))
    when (input[row][col]) {
        '\\' -> {
            val nextDirection = when (direction) {
                Direction.North -> Direction.West
                Direction.East -> Direction.South
                Direction.South -> Direction.East
                Direction.West -> Direction.North
            }
            val (newRow, newCol) = getNextRowCol(row, col, nextDirection)
            walk(input, newRow, newCol, nextDirection, energizedTiles)
        }

        '/' -> {
            val nextDirection = when (direction) {
                Direction.North -> Direction.East
                Direction.East -> Direction.North
                Direction.South -> Direction.West
                Direction.West -> Direction.South
            }
            val (newRow, newCol) = getNextRowCol(row, col, nextDirection)
            walk(input, newRow, newCol, nextDirection, energizedTiles)
        }

        '|' -> {
            when (direction) {
                Direction.North -> {
                    walk(input, row - 1, col, Direction.North, energizedTiles)
                }

                Direction.South -> {
                    walk(input, row + 1, col, Direction.South, energizedTiles)
                }

                Direction.East, Direction.West -> {
                    walk(input, row - 1, col, Direction.North, energizedTiles)
                    walk(input, row + 1, col, Direction.South, energizedTiles)
                }
            }
        }

        '-' -> {
            when (direction) {
                Direction.East -> {
                    walk(input, row, col + 1, Direction.East, energizedTiles)
                }

                Direction.West -> {
                    walk(input, row, col - 1, Direction.West, energizedTiles)
                }

                Direction.North, Direction.South -> {
                    walk(input, row, col + 1, Direction.East, energizedTiles)
                    walk(input, row, col - 1, Direction.West, energizedTiles)
                }
            }
        }

        else -> {
            val (newRow, newCol) = getNextRowCol(row, col, direction)
            walk(input, newRow, newCol, direction, energizedTiles)
        }
    }

}

private fun getNextRowCol(row: Int, col: Int, direction: Direction): Pair<Int, Int> {
    var newRow = row
    var newCol = col
    when (direction) {
        Direction.North -> newRow--
        Direction.East -> newCol++
        Direction.South -> newRow++
        Direction.West -> newCol--
    }
    return Pair(newRow, newCol)
}

private enum class Direction {
    North,
    East,
    South,
    West
}