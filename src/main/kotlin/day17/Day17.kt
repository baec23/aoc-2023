package org.baec23.day17

import org.baec23.util.readLines
import java.nio.file.Files.walk

fun main() {
    val input = readLines(day = 17, isSample = true)
    println(partOne(input))
}

private fun partOne(input: List<String>): Long {
    val runner = Runner(input)
    runner.run()
    return 0
}

class Runner(val input: List<String>) {
    private var currMin = Long.MAX_VALUE
    fun run() : Long{
        walk(input, 0, 0, Direction.East, 1, 0, emptyList())
        return currMin
    }

    private fun walk(
        input: List<String>,
        row: Int,
        col: Int,
        travelDirection: Direction,
        numStraightMoves: Int,
        currTotal: Long,
        seenStates: List<WalkState>
    ) {
        //if out of bounds, stop
        if (row !in input.indices || col !in input[0].indices) {
            return
        }
        if (currTotal > currMin) {
            return
        }
        //if been here, stop
        if (seenStates.contains(WalkState(row, col))) {
            return
        }

        val myValue = input[row][col].digitToInt()
        //if at end
        if (row == input.lastIndex && col == input[0].lastIndex) {
            val mTotal = currTotal + myValue
            if (mTotal < currMin) {
                currMin = mTotal
            }
            println(currTotal + myValue)
            return
        }

        //try going straight
        if (numStraightMoves < 3) {
            val (newRow, newCol) = getNextRowCol(row, col, travelDirection)
            walk(
                input,
                newRow,
                newCol,
                travelDirection,
                numStraightMoves + 1,
                currTotal + myValue,
                seenStates + WalkState(row, col)
            )
        }

        //try going left
        val leftDirection = travelDirection.left()
        val (leftRow, leftCol) = getNextRowCol(row, col, leftDirection)
        walk(
            input,
            leftRow,
            leftCol,
            leftDirection,
            1,
            currTotal + myValue,
            seenStates + WalkState(row, col)
        )

        //try going right
        val rightDirection = travelDirection.right()
        val (rightRow, rightCol) = getNextRowCol(row, col, rightDirection)
        walk(
            input,
            rightRow,
            rightCol,
            rightDirection,
            1,
            currTotal + myValue,
            seenStates + WalkState(row, col)
        )
    }

    private data class WalkState(
        val row: Int,
        val col: Int,
//        val travelDirection: Direction,
//        val numStraightMoves: Int
    )

    private fun Direction.left(): Direction {
        return when (this) {
            Direction.North -> Direction.West
            Direction.East -> Direction.North
            Direction.South -> Direction.East
            Direction.West -> Direction.South
        }
    }

    private fun Direction.right(): Direction {
        return when (this) {
            Direction.North -> Direction.East
            Direction.East -> Direction.South
            Direction.South -> Direction.West
            Direction.West -> Direction.North
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
}
