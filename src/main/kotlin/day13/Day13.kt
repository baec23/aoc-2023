package org.baec23.day13

import org.baec23.util.readLines

fun main() {
    //Parse Input
    val totalInput = readLines(day = 13, isSample = false)
    val splitInputs = mutableListOf<List<String>>()
    val currInput = mutableListOf<String>()
    totalInput.forEach { line ->
        if (line.isBlank()) {
            splitInputs.add(currInput.toList())
            currInput.clear()
        } else {
            currInput.add(line)
        }
    }
    splitInputs.add(currInput.toList())

    //Do
    var answer = 0L
    splitInputs.forEach { input ->
        answer += partOne(input)
    }
    println(answer)
}

private fun partOne(input: List<String>): Int {
    val rows = input.map { it.toCharArray().toList() }
    val cols = mutableListOf<List<Char>>()
    for (i in input[0].indices) {
        cols.add(rows.map { it[i] })
    }
    println("=====COLUMN=====")
    val colReflection = findReflection(cols) + 1
    println("=====ROW=====")
    val rowReflection = findReflection(rows) + 1
    println("colReflection = ${colReflection}\nrowReflection = ${rowReflection}\n")
    return colReflection + 100 * rowReflection
}

private fun findReflection(list: List<List<Char>>): Int {
    var toReturn = -1
    val potentialRange = 0..<list.size - 1
    for (i in potentialRange) {
        val numToCheck = minOf(i + 1, list.size - (i + 1))
        var isReflection = true
//        println("Checking ${i}")
        for (j in 0..<numToCheck) {
//            println("Comparing...\n${list[i-j]}\n${list[i+j+1]}")
            if (list[i - j] != list[i + j + 1]) {
                isReflection = false
            }
//            println("isReflection = ${isReflection}")
        }
//        println()
        if (isReflection) {
            toReturn = i
        }
    }
    return toReturn
}
//
//private fun findColReflection(cols: List<List<Char>>): Int {
//    var toReturn = -1
//    val potentialRange = 1..<cols.size - 1
//    for (i in potentialRange) {
//        val numToCheck = minOf(i + 1, cols.size - (i + 1))
//        var isReflection = true
//        for (j in 0..<numToCheck) {
//            if (cols[i - j] != cols[i + j + 1]) {
//                isReflection = false
//            }
//        }
//        if (isReflection) {
//            toReturn = i
//        }
//    }
//    return toReturn
//}