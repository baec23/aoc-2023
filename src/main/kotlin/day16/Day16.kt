package org.baec23.day16

import org.baec23.util.readLines

fun main() {
    val input = readLines(day = 16, isSample = false)
    println(partTwo(input))
}

private fun partTwo(input: List<String>): Long {

    var maxEnergized = 0L
    val energizedTiles = HashSet<Pair<Int, Int>>()
    input.forEachIndexed { index, line ->
        //start from left
        walk(input, index, 0, Direction.East, energizedTiles, HashSet())
        val left = energizedTiles.count().toLong()
        if (left > maxEnergized) {
            maxEnergized = left
        }
        energizedTiles.clear()
        //start from right
        walk(input, index, line.length - 1, Direction.West, energizedTiles, HashSet())
        val right = energizedTiles.count().toLong()
        if (right > maxEnergized) {
            maxEnergized = right
        }
        energizedTiles.clear()
    }
    input[0].forEachIndexed { index, _ ->
        //start from top
        walk(input, 0, index, Direction.South, energizedTiles, HashSet())
        val top = energizedTiles.count().toLong()
        if (top > maxEnergized) {
            maxEnergized = top
        }
        energizedTiles.clear()

        //start from bottom
        walk(input, input[0].length - 1, index, Direction.North, energizedTiles, HashSet())
        val bottom = energizedTiles.count().toLong()
        if (bottom > maxEnergized) {
            maxEnergized = bottom
        }
        energizedTiles.clear()
    }
    return maxEnergized
}

private fun partOne(input: List<String>): Int {
    val energizedTiles = HashSet<Pair<Int, Int>>()
    walk(input, 0, 0, Direction.East, energizedTiles, HashSet())

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

private data class WalkState(
    val row: Int,
    val col: Int,
    val direction: Direction
)

private fun walk(
    input: List<String>,
    row: Int,
    col: Int,
    direction: Direction,
    energizedTiles: MutableSet<Pair<Int, Int>>,
    seen: MutableSet<WalkState>
) {
    if (row !in input.indices || col !in input[0].indices || seen.contains(WalkState(row, col, direction))) {
        return
    }
    energizedTiles.add(Pair(row, col))
    seen.add(WalkState(row, col, direction))
    when (input[row][col]) {
        '\\' -> {
            val nextDirection = when (direction) {
                Direction.North -> Direction.West
                Direction.East -> Direction.South
                Direction.South -> Direction.East
                Direction.West -> Direction.North
            }
            val (newRow, newCol) = getNextRowCol(row, col, nextDirection)
            walk(input, newRow, newCol, nextDirection, energizedTiles, seen)
        }

        '/' -> {
            val nextDirection = when (direction) {
                Direction.North -> Direction.East
                Direction.East -> Direction.North
                Direction.South -> Direction.West
                Direction.West -> Direction.South
            }
            val (newRow, newCol) = getNextRowCol(row, col, nextDirection)
            walk(input, newRow, newCol, nextDirection, energizedTiles, seen)
        }

        '|' -> {
            when (direction) {
                Direction.North -> {
                    walk(input, row - 1, col, Direction.North, energizedTiles, seen)
                }

                Direction.South -> {
                    walk(input, row + 1, col, Direction.South, energizedTiles, seen)
                }

                Direction.East, Direction.West -> {
                    walk(input, row - 1, col, Direction.North, energizedTiles, seen)
                    walk(input, row + 1, col, Direction.South, energizedTiles, seen)
                }
            }
        }

        '-' -> {
            when (direction) {
                Direction.East -> {
                    walk(input, row, col + 1, Direction.East, energizedTiles, seen)
                }

                Direction.West -> {
                    walk(input, row, col - 1, Direction.West, energizedTiles, seen)
                }

                Direction.North, Direction.South -> {
                    walk(input, row, col + 1, Direction.East, energizedTiles, seen)
                    walk(input, row, col - 1, Direction.West, energizedTiles, seen)
                }
            }
        }

        else -> {
            val (newRow, newCol) = getNextRowCol(row, col, direction)
            walk(input, newRow, newCol, direction, energizedTiles, seen)
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