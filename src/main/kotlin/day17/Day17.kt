package org.baec23.day17

import org.baec23.util.readLines

/*
Try once without looping back
Try again allowing loops but stop if total ever higher than without a loop
 */

fun main() {
    val input = readLines(day = 17, isSample = true)
    println(partOne(input))
}

private fun partOne(input: List<String>): Long {
    return walk(input,0,0,Direction.East, 1, 0, HashSet(), HashSet())
}

private fun walk(
    input: List<String>,
    row: Int,
    col: Int,
    travelDirection: Direction,
    numStraightMoves: Int,
    currTotal: Long,
    currSeen: Set<Pair<Int, Int>>,
    loopSeen: MutableSet<Pair<Int,Int>>
): Long {
    if (row !in input.indices || col !in input[0].indices || currSeen.contains(Pair(row,col)) || loopSeen.contains(Pair(row,col)) ) {
        return Long.MAX_VALUE
    }

    val myValue = input[row][col].digitToInt()

    if (row == input.lastIndex && col == input[0].lastIndex) {
        return myValue + currTotal
    }

    //try going straight
    var toReturn = Long.MAX_VALUE
    if (numStraightMoves < 3) {
        val (newRow, newCol) = getNextRowCol(row, col, travelDirection)
        val straightSeen = currSeen.toMutableSet()
        straightSeen.add(Pair(row,col))
        toReturn = minOf(
            toReturn, walk(
                input,
                newRow,
                newCol,
                travelDirection,
                numStraightMoves + 1,
                currTotal + myValue,
                straightSeen.toSet(),
                loopSeen
            )
        )
    }

    //try going left
    val leftDirection = travelDirection.left()
    val (leftRow, leftCol) = getNextRowCol(row, col, leftDirection)
    val leftSeen = currSeen.toMutableSet()
    leftSeen.add(Pair(row,col))
    toReturn = minOf(
        toReturn, walk(
            input,
            leftRow,
            leftCol,
            leftDirection,
            1,
            currTotal + myValue,
            leftSeen.toSet(),
            loopSeen
        )
    )

    //try going right
    val rightDirection = travelDirection.right()
    val (rightRow, rightCol) = getNextRowCol(row, col, rightDirection)
    val rightSeen = currSeen.toMutableSet()
    rightSeen.add(Pair(row,col))
    toReturn = minOf(
        toReturn, walk(
            input,
            rightRow,
            rightCol,
            rightDirection,
            1,
            currTotal + myValue,
            rightSeen.toSet(),
            loopSeen
        )
    )
    if(toReturn == Long.MAX_VALUE){
        loopSeen.add(Pair(row,col))
    }

    return toReturn
}

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

