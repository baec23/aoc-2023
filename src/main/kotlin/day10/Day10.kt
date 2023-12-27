package org.baec23.day10

import org.baec23.util.readLines

fun main() {
    val input = readLines(day = 10, isSample = false)
    println(partTwo(input))
}

private fun partTwo(input: List<String>): Long {
    //Find 'S'
    var startY = 0
    var startX = 0
    for (i in input.indices) {
        for (j in input[i].indices) {
            if (input[i][j] == 'S') {
                startY = i
                startX = j
                break
            }
        }
    }

    //Find Pipes in path
    var pathPipes = emptyList<Pipe>()
    var startDirections = mutableListOf<Direction>()
    //North
    val northResult = walkIt(
        grid = input,
        loopStartX = startX,
        loopStartY = startY,
        startX = startX,
        startY = startY - 1,
        from = Direction.South,
    )
    if (northResult.isNotEmpty()) {
        pathPipes = northResult
        startDirections.add(Direction.North)
    }
    //East
    val eastResult = walkIt(
        grid = input,
        loopStartX = startX,
        loopStartY = startY,
        startX = startX + 1,
        startY = startY,
        from = Direction.West,
    )
    if (eastResult.isNotEmpty()) {
        pathPipes = eastResult
        startDirections.add(Direction.East)
    }

    //South
    val southResult = walkIt(
        grid = input,
        loopStartX = startX,
        loopStartY = startY,
        startX = startX,
        startY = startY + 1,
        from = Direction.North,
    )
    if (southResult.isNotEmpty()) {
        pathPipes = southResult
        startDirections.add(Direction.South)
    }

    //West
    val westResult = walkIt(
        grid = input,
        loopStartX = startX,
        loopStartY = startY,
        startX = startX - 1,
        startY = startY,
        from = Direction.East,
    )
    if (westResult.isNotEmpty()) {
        pathPipes = westResult
        startDirections.add(Direction.West)
    }

    //Replace 'S' with correct Pipe
    val startPipeChar = when {
        startDirections.first() == Direction.North && startDirections.last() == Direction.South -> '|'
        startDirections.first() == Direction.South && startDirections.last() == Direction.North -> '|'

        startDirections.first() == Direction.East && startDirections.last() == Direction.West -> '-'
        startDirections.first() == Direction.West && startDirections.last() == Direction.East -> '-'

        startDirections.first() == Direction.North && startDirections.last() == Direction.East -> 'L'
        startDirections.first() == Direction.East && startDirections.last() == Direction.North -> 'L'

        startDirections.first() == Direction.West && startDirections.last() == Direction.North -> 'J'
        startDirections.first() == Direction.North && startDirections.last() == Direction.West -> 'J'

        startDirections.first() == Direction.South && startDirections.last() == Direction.West -> '7'
        startDirections.first() == Direction.West && startDirections.last() == Direction.South -> '7'

        startDirections.first() == Direction.East && startDirections.last() == Direction.South -> 'F'
        startDirections.first() == Direction.South && startDirections.last() == Direction.East -> 'F'
        else -> throw Exception("Invalid startDirections")
    }
    val path = pathPipes.toMutableList()
    path.add(Pipe(char = startPipeChar, y = startY, x = startX))
    //Do Area Calculation

    val pathIntersections = path.groupBy { it.y }.mapValues { it ->
        it.value.getIntersectionLocations()
    }

    val pathSet = path.map { Pair(it.x, it.y) }.toHashSet()
    var toReturn = 0L
    for (row in input.indices) {
        for (col in input[row].indices) {
            if (!pathSet.contains(Pair(col, row))) {
                val pathRow = pathIntersections[row]
                pathRow?.let {
                    if (isInside(col, it)) {
                        toReturn++
                    }
                }
            }
        }
    }
    return toReturn
}

private fun List<Pipe>.getIntersectionLocations(): List<Int> {
    val toReturn = mutableListOf<Int>()
    //find FJ and L7
    var currOpenCorner: Char? = null
    var currOpenCornerPos: Int? = null
    this.sortedBy { it.x }.forEach { pipe ->
        when (pipe.char) {
            '|' -> toReturn.add(pipe.x)
            'F', 'L' -> {
                if (currOpenCorner != null) {
                    throw Exception("What")
                }
                currOpenCorner = pipe.char
                currOpenCornerPos = pipe.x
            }

            '7' -> {
                if (currOpenCorner == null) {
                    throw Exception("Super What")
                }
                if (currOpenCorner == 'L') {
                    toReturn.add(currOpenCornerPos!!)
                }
                currOpenCorner = null
                currOpenCornerPos = null
            }

            'J' -> {
                if (currOpenCorner == null) {
                    throw Exception("Super What")
                }
                if (currOpenCorner == 'F') {
                    toReturn.add(currOpenCornerPos!!)
                }
                currOpenCorner = null
                currOpenCornerPos = null
            }

            else -> {}
        }
    }
    return toReturn.sorted().toList()
}

private fun isInside(col: Int, intersectionLocations: List<Int>): Boolean {
    val numCrossings = intersectionLocations.count { it > col }
    return numCrossings % 2 != 0
}

private fun walkIt(
    grid: List<String>,
    loopStartX: Int,
    loopStartY: Int,
    startX: Int,
    startY: Int,
    from: Direction
): List<Pipe> {
    var currX = startX
    var currY = startY
    var fromDirection = from

    val toReturn = mutableListOf<Pipe>()
    while (!(currX == loopStartX && currY == loopStartY)) {
        if (currY !in grid.indices || currX !in grid.first().indices) {
            toReturn.clear()
            break
        }
        val currPipe = Pipe(grid[currY][currX], y = currY, x = currX)
        val nextDirection =
            try {
                currPipe.getNextDirection(fromDirection)
            } catch (e: Exception) {
                toReturn.clear()
                break
            }
        if (nextDirection == null) {
            toReturn.clear()
            break
        }
        toReturn.add(currPipe)
        when (nextDirection) {
            Direction.North -> {
                currY -= 1
                fromDirection = Direction.South
            }

            Direction.South -> {
                currY += 1
                fromDirection = Direction.North
            }

            Direction.East -> {
                currX += 1
                fromDirection = Direction.West
            }

            Direction.West -> {
                currX -= 1
                fromDirection = Direction.East
            }
        }
    }
    return toReturn.toList()
}

private data class Pipe(val char: Char, val y: Int, val x: Int) {
    val availableDirections: List<Direction>

    init {
        when (char) {
            '|' -> {
                availableDirections = listOf(Direction.North, Direction.South)
            }

            '-' -> {
                availableDirections = listOf(Direction.East, Direction.West)
            }

            'L' -> {
                availableDirections = listOf(Direction.North, Direction.East)
            }

            'J' -> {
                availableDirections = listOf(Direction.North, Direction.West)
            }

            '7' -> {
                availableDirections = listOf(Direction.South, Direction.West)
            }

            'F' -> {
                availableDirections = listOf(Direction.South, Direction.East)
            }

            else -> {
                availableDirections = emptyList()
            }
        }
    }

    fun getNextDirection(from: Direction): Direction? {
        val dirs = availableDirections.filter { availableDirection -> availableDirection != from }
        if (dirs.size > 1) {
            throw Exception("Shouldn't be more than one possible next direction")
        }
        if (dirs.isEmpty()) {
            return null
        }
        return dirs.first()
    }
}

private enum class Direction {
    North,
    South,
    East,
    West
}


private fun partOne(input: List<String>): Long {
    var startY = 0
    var startX = 0
    for (i in input.indices) {
        for (j in input[i].indices) {
            if (input[i][j] == 'S') {
                startY = i
                startX = j
                break
            }
        }
    }
    //North
    val northResult = walkIt(
        grid = input,
        loopStartX = startX,
        loopStartY = startY,
        startX = startX,
        startY = startY - 1,
        from = Direction.South,
    )
    if (northResult.isNotEmpty()) {
        return (northResult.size + 1) / 2L
    }
    //East
    val eastResult = walkIt(
        grid = input,
        loopStartX = startX,
        loopStartY = startY,
        startX = startX + 1,
        startY = startY,
        from = Direction.West,
    )
    if (eastResult.isNotEmpty()) {
        return (eastResult.size + 1) / 2L
    }
    //South
    val southResult = walkIt(
        grid = input,
        loopStartX = startX,
        loopStartY = startY,
        startX = startX,
        startY = startY + 1,
        from = Direction.North,
    )
    if (southResult.isNotEmpty()) {
        return (southResult.size + 1) / 2L
    }
    //West
    val westResult = walkIt(
        grid = input,
        loopStartX = startX,
        loopStartY = startY,
        startX = startX - 1,
        startY = startY,
        from = Direction.East,
    )
    if (westResult.isNotEmpty()) {
        return (westResult.size + 1) / 2L
    }
    throw Exception("No loop found")
}
