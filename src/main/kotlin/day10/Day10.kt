package org.baec23.day10

import org.baec23.util.readLines

fun main() {
    val input = readLines(day = 10, isSample = true)
    println(partTwo(input))
}

private fun partTwo(input: List<String>): Long {
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


    val seen = HashSet<Pair<Int, Int>>()
    var area = 0L
    for (row in input.indices) {
        for (col in input[row].indices) {
            val arr = findArea(input, path, col, row, seen)
            if (arr != -1L) {
                val sortedRow = path.filter { it.y == row }.sortedBy { it.x }
                var currStartPipeIndex = findNextOpenIndex(sortedRow, 0)
                var currEndPipeIndex = findNextCloseIndex(sortedRow, currStartPipeIndex + 1)
                val potentialRanges = mutableListOf<Pair<Int, Int>>()
                while (currStartPipeIndex != -1 && currEndPipeIndex != -1) {
                    val startPipe = path[currStartPipeIndex]
                    val endPipe = path[currEndPipeIndex]
                    potentialRanges.add(Pair(startPipe.x, endPipe.x))
                    currStartPipeIndex = findNextOpenIndex(sortedRow, currEndPipeIndex)
                    currEndPipeIndex = findNextCloseIndex(sortedRow, currStartPipeIndex + 1)
                }
                var shouldAdd = false
                potentialRanges.forEach {
                    if (col in it.first..it.second) {
                        shouldAdd = true
                    }
                }
                if (shouldAdd) {
                    area += arr
                }
            }
        }
    }
    return area


    //startX startY

//    val pathGroupedByY = path.sortedBy { it.y }.groupBy { it.y }.values.toList()
//    var toReturn = 0L

//    pathGroupedByY.forEach { line ->
////        val sortedLine = line.sortedBy{it.x}
////        val potentialLengths = mutableListOf<Long>()
////        val filtered = sortedLine.filter { it.char != '-' }
////        for(i in 1..<filtered.size){
////            potentialLengths.add(filtered[i].x - filtered[i-1].x - 1L)
////        }
////        val a = 1+2
//
//        val potentialAreaRanges = mutableListOf<Long>()
//        val sortedLine = line.sortedBy { it.x }
//        var currSectionStartIndex = findNextOpenIndex(sortedLine, 0)
//        var currSectionEndIndex = findNextCloseIndex(sortedLine, currSectionStartIndex + 1)
//        println("line = ${sortedLine.map { it.char }}")
//        while (currSectionStartIndex != -1 && currSectionEndIndex != -1) {
//            val startPipe = sortedLine[currSectionStartIndex]
//            val endPipe = sortedLine[currSectionEndIndex]
//            potentialAreaRanges.add(endPipe.x - startPipe.x - 1L)
//            println("\tfound range from - ${startPipe.char} to ${endPipe.char}")
//            currSectionStartIndex = findNextOpenIndex(sortedLine, currSectionEndIndex)
//            currSectionEndIndex = findNextCloseIndex(sortedLine, currSectionStartIndex + 1)
//        }
//        potentialAreaRanges.forEachIndexed { index, potentialArea ->
//            if(potentialAreaRanges.size % 2 == 0){
//                toReturn += potentialArea
//            }else{
//
//            }
//            if (index % 2 == 0) {
//                toReturn += potentialArea
//            }
//        }
//    }

//    return toReturn
}

private fun findNextOpenIndex(line: List<Pipe>, startIndex: Int): Int {
    for (i in startIndex..<line.size) {
        if (line[i].char == '|' || line[i].char == 'J' || line[i].char == '7') {
            return i
        }
    }
    return -1
}

private fun findNextCloseIndex(line: List<Pipe>, startIndex: Int): Int {
    for (i in startIndex..<line.size) {
        if (line[i].char == '|' || line[i].char == 'L' || line[i].char == 'F') {
            return i
        }
    }
    return -1
}

private fun findArea(
    grid: List<String>,
    pipes: List<Pipe>,
    x: Int,
    y: Int,
    seen: MutableSet<Pair<Int, Int>>
): Long {
    val currPair = Pair(x, y)
    if (x !in grid[0].indices || y !in grid.indices) {
        return -1L
    }
    if (seen.contains(currPair)) {
        return 0L
    }
    val pipeLocs = pipes.map { Pair(it.x, it.y) }
    if (pipeLocs.contains(currPair)) {
        return 0
    }
    seen.add(currPair)
    val north = findArea(grid, pipes, x, y - 1, seen)
    val east = findArea(grid, pipes, x + 1, y, seen)
    val south = findArea(grid, pipes, x, y + 1, seen)
    val west = findArea(grid, pipes, x - 1, y, seen)
    if (minOf(north, east, south, west) == -1L) {
        return -1L
    }
    println("I'm part of it: ${y} , ${x}")
    return 1 + north + east + south + west
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
