package org.baec23.day13
import org.baec23.util.readLines

fun main(){
    val input = readLines(day = 13, isSample = true)
    partOne(input)
}

private fun partOne(input: List<String>){
    val cols = mutableListOf<MutableList<Char>>()
    val rows = input.map{it.toCharArray().toList()}
    println(findHorizontalReflection(rows))
}

private fun findHorizontalReflection(rows: List<List<Char>>): Int{
    var toReturn = -1
    val potentialRange = 1..<rows.size-1
    for(i in potentialRange){
        val numToCheck = minOf((0..<i).count(), (i..<rows.size-1).count())
        var isReflection = true
        for(j in 1..numToCheck){
            if(rows[i-j] != rows[i+j]){
                isReflection = false
            }
        }
        if(isReflection){
            toReturn = i
        }
    }
    return toReturn
}

private fun findVerticalReflection(columns: List<List<Char>>): Int{
    var toReturn = -1
    val potentialRange = 1..<columns.size-1
    for(i in potentialRange){
        val numToCheck = minOf((0..<i).count(), (i..<columns.size).count())
        var isReflection = true
        for(j in 1..numToCheck){
            if(columns[i-j] != columns[i+j]){
                isReflection = false
            }
        }
        if(isReflection){
            toReturn = i
        }
    }
    return toReturn
}