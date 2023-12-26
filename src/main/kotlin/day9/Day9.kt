package org.baec23.day9
import org.baec23.util.readLines
import org.baec23.util.splitByWhitespace

fun main(){
    val input = readLines(day = 9, isSample = false)
    val parsedInput = input.map{it.splitByWhitespace().map{it.toLong()}}
    println(partTwo(parsedInput))
}

private fun partTwo(parsedInput: List<List<Long>>): Long{
    var sum = 0L
    parsedInput.forEach{line ->
        sum += helper2(line)
    }
    return sum
}

private fun helper2(input: List<Long>): Long{
    val diffs = calcDifferences(input)
    val groupedDiffs = diffs.groupBy { it }.values.toList()
    if(groupedDiffs.size == 1 && groupedDiffs.first().first() == 0L){
        return input.first()
    }
    return input.first() - helper2(diffs)
}


private fun partOne(parsedInput: List<List<Long>>): Long{
    var sum = 0L
    parsedInput.forEach{line ->
        sum += helper(line)
    }
    return sum
}

private fun helper(input: List<Long>): Long{
    val diffs = calcDifferences(input)
    val groupedDiffs = diffs.groupBy { it }.values.toList()
    if(groupedDiffs.size == 1 && groupedDiffs.first().first() == 0L){
        return input.last()
    }
    return input.last() + helper(diffs)
}
private fun calcDifferences(input: List<Long>): List<Long>{
    val toReturn = mutableListOf<Long>()
    for (i in 1..< input.size){
        toReturn.add(input[i] - input[i-1])
    }
    return toReturn
}