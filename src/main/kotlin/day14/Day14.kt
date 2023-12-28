package org.baec23.day14

import org.baec23.util.readLines

fun main() {
    val input = readLines(day = 14, isSample = false)
    partTwo(input)
}

private fun partTwo(input: List<String>) {
    val platform = Platform(input)
    val numCycles = 1000000000
    println("Load for $numCycles = ${platform.getLoadForCycle(numCycles)}")
}

private class Platform(input: List<String>) {
    val rocks = mutableListOf<Rock>()
    val rows = mutableListOf<PlatformRow>()
    val cols = mutableListOf<PlatformCol>()
    private val states = mutableListOf<RockState>()
    private val loads = mutableListOf<Long>()
    private val memo = HashMap<RockState, RockState>()
    private var initialOffset = -1
    private var cycleLength = -1

    init {
        for (row in input.indices) {
            val blockIndexes = mutableListOf<Int>()
            for (col in input[row].indices) {
                when (input[row][col]) {
                    'O' -> rocks.add(Rock(row, col))
                    '#' -> blockIndexes.add(col)
                }
            }
            rows.add(PlatformRow(row, input.size, blockIndexes.toList()))
        }
        for (col in input[0].indices) {
            val blockIndexes = mutableListOf<Int>()
            for (row in input.indices) {
                if (input[row][col] == '#') {
                    blockIndexes.add(row)
                }
            }
            cols.add(PlatformCol(col, input[0].length, blockIndexes.toList()))
        }
        states.add(RockState.fromRocks(rocks))
        loads.add(calcLoad())
        var isCycleFound = false
        while (!isCycleFound) {
            isCycleFound = cycle()
        }
        loadFromState(states[0])
    }

    fun getLoadForCycle(cycleCount: Int): Long {
        if (cycleCount < loads.size) {
            return loads[cycleCount]
        }
        val loadIndex = initialOffset + ((cycleCount - initialOffset) % cycleLength)
        return loads[loadIndex]
    }

    fun print() {
        val sb = StringBuilder()
        rows.forEach { row ->
            for (col in 0..<row.length) {
                val rock = rocks.find { it.col == col && it.row == row.index }
                if (rock != null) {
                    sb.append('O')
                } else if (col in row.blockIndexes) {
                    sb.append('#')
                } else {
                    sb.append('.')
                }
            }
            sb.append('\n')
        }
        println(sb.toString())
    }

    //Returns whether this cycle is a repeat
    private fun cycle(): Boolean {
        val currState = RockState.fromRocks(rocks)
        memo[currState]?.let {
            val cycleStartIndex = states.indexOfFirst { thisState -> thisState == it }
            cycleLength = states.size - cycleStartIndex
            initialOffset = cycleStartIndex - 1
            loadFromState(it)
            return true
        }
        tiltNorth()
        tiltWest()
        tiltSouth()
        tiltEast()
        val newRockState = RockState.fromRocks(rocks)
        states.add(newRockState)
        loads.add(calcLoad())
        memo[currState] = newRockState
        return false
    }

    private fun calcLoad(): Long {
        var toReturn = 0L
        rocks.forEach { rock ->
            toReturn += rows.size - rock.row
        }
        return toReturn
    }

    private fun tiltNorth() {
        cols.forEach { col ->
            col.tiltNorth(rocks)
        }
    }

    private fun tiltWest() {
        rows.forEach { row -> row.tiltWest(rocks) }

    }

    private fun tiltSouth() {
        cols.forEach { col ->
            col.tiltSouth(rocks)
        }
    }

    private fun tiltEast() {
        rows.forEach { row -> row.tiltEast(rocks) }
    }


    private fun loadFromState(rockState: RockState) {
        if (rockState.state.size != rocks.size) {
            throw Exception("That shouldn't happen")
        }
        rockState.state.forEachIndexed { index, pair ->
            rocks[index].row = pair.first
            rocks[index].col = pair.second
        }
    }


}

private class Rock(
    var row: Int,
    var col: Int
) {
    fun toPair(): Pair<Int, Int> {
        return Pair(row, col)
    }
}

private data class RockState(
    val state: Set<Pair<Int, Int>>
) {
    companion object {
        fun fromRocks(rocks: List<Rock>): RockState {
            val pairs = rocks.map { it.toPair() }
            val set = HashSet<Pair<Int, Int>>()
            set.addAll(pairs)
            return RockState(set)
        }
    }
}

private data class PlatformRow(
    val index: Int,
    val length: Int,
    val blockIndexes: List<Int>
) {
    val ranges: List<IntRange>

    init {
        val temp = mutableListOf<IntRange>()
        var rangeStart = 0
        blockIndexes.forEach { block ->
            val rangeToAdd = rangeStart..<block
            if (!rangeToAdd.isEmpty()) {
                temp.add(rangeToAdd)
            }
            rangeStart = block + 1
        }
        val rangeToAdd = rangeStart..<length
        if (!rangeToAdd.isEmpty()) {
            temp.add(rangeToAdd)
        }
        ranges = temp.toList()
    }

    fun tiltWest(rocks: List<Rock>) {
        val rowRocks = rocks.filter { it.row == index }.sortedBy { it.col }
        ranges.forEach { range ->
            var currMinCol = range.first
            val rangeRocks = rowRocks.filter { it.col in range }
            rangeRocks.forEach {
                it.col = currMinCol
                currMinCol++
            }
        }
    }

    fun tiltEast(rocks: List<Rock>) {
        val rowRocks = rocks.filter { it.row == index }.sortedByDescending { it.col }
        ranges.forEach { range ->
            var currMaxCol = range.last()
            val rangeRocks = rowRocks.filter { it.col in range }
            rangeRocks.forEach {
                it.col = currMaxCol
                currMaxCol--
            }
        }
    }
}

private data class PlatformCol(
    val index: Int,
    val length: Int,
    val blockIndexes: List<Int>
) {
    val ranges: List<IntRange>

    init {
        val temp = mutableListOf<IntRange>()
        var rangeStart = 0
        blockIndexes.forEach { block ->
            val rangeToAdd = rangeStart..<block
            if (!rangeToAdd.isEmpty()) {
                temp.add(rangeToAdd)
            }
            rangeStart = block + 1
        }
        val rangeToAdd = rangeStart..<length
        if (!rangeToAdd.isEmpty()) {
            temp.add(rangeToAdd)
        }
        ranges = temp.toList()
    }

    fun tiltNorth(rocks: List<Rock>) {
        val colRocks = rocks.filter { it.col == index }.sortedBy { it.row }
        ranges.forEach { range ->
            var currMinRow = range.first
            val rangeRocks = colRocks.filter { it.row in range }
            rangeRocks.forEach {
                it.row = currMinRow
                currMinRow++
            }
        }
    }

    fun tiltSouth(rocks: List<Rock>) {
        val colRocks = rocks.filter { it.col == index }.sortedByDescending { it.row }
        ranges.forEach { range ->
            var currMaxRow = range.last
            val rangeRocks = colRocks.filter { it.row in range }
            rangeRocks.forEach {
                it.row = currMaxRow
                currMaxRow--
            }
        }
    }
}

private fun partOne(input: List<String>): Long {
    var toReturn = 0L
    val maxLoad = input.size
    for (col in input[0].indices) {
        var currBase = 0
        var currRockNumber = 0
        for (row in input.indices) {
            if (input[row][col] == 'O') {
                toReturn += maxLoad - currBase - currRockNumber
                currRockNumber++
            } else if (input[row][col] == '#') {
                currBase = row + 1
                currRockNumber = 0
            }
        }
    }
    return toReturn
}
