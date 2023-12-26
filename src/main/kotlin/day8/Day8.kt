package org.baec23.day8

import org.baec23.util.readLines
import org.baec23.util.splitByWhitespace

fun main() {
    val input = readLines(day = 8, isSample = false)
    println(partTwo(input))
}

private fun partTwo(input: List<String>): Long {
    val directions = input.first()
    val nodeStrings = input.takeLast(input.size - 2)
    val nodes = HashMap<String, Node>()
    nodeStrings.forEach { nodeString ->
        val split = nodeString.split(" = ")
        val nodeValue = split.first()
        val leftRight = split.last().replace("(", " ").replace(")", " ").replace(",", " ").trim().splitByWhitespace()
        val newNode = Node(value = nodeValue, left = leftRight.first(), right = leftRight.last())
        nodes[nodeValue] = newNode
    }
    val startNodes = mutableListOf<String>()
    nodes.keys.forEach { key ->
        if (key.endsWith("A")) {
            startNodes.add(key)
        }
    }

    val stepsList = startNodes.map { nodeValue ->
        var currNode = nodeValue
        var numSteps = 0L
        var stepIndex = 0
        while (!currNode.endsWith('Z')) {
            if (directions[stepIndex] == 'L') {
                numSteps++
                currNode = nodes[currNode]?.left ?: throw Exception("No left node")
            } else {
                numSteps++
                currNode = nodes[currNode]?.right ?: throw Exception("No right node")
            }
            stepIndex++
            if (stepIndex >= directions.length) {
                stepIndex = 0
            }
        }
        numSteps
    }

    var toReturn = stepsList[0]
    for (i in 1..<stepsList.size) {
        toReturn = lcm(toReturn, stepsList[i])
    }
    return toReturn
}

private fun gcd(a: Long, b: Long): Long {
    if (b == 0L) {
        return a
    }
    return gcd(b, a % b)
}

private fun lcm(a: Long, b: Long): Long {
    return (a / gcd(a, b)) * b
}

private fun List<String>.isAllZ(): Boolean {
    var isAllZ = true
    this.forEach { nodeValue ->
        if (nodeValue.last() != 'Z') {
            isAllZ = false
        }
    }
    return isAllZ
}

private fun partOne(input: List<String>): Long {
    val directions = input.first()
    val nodeStrings = input.takeLast(input.size - 2)
    val nodes = HashMap<String, Node>()
    nodeStrings.forEach { nodeString ->
        val split = nodeString.split(" = ")
        val nodeValue = split.first()
        val leftRight = split.last().replace("(", " ").replace(")", " ").replace(",", " ").trim().splitByWhitespace()
        val newNode = Node(value = nodeValue, left = leftRight.first(), right = leftRight.last())
        nodes[nodeValue] = newNode
    }
    var stepIndex = 0
    var numSteps = 0L
    var currNode = "AAA"
    while (currNode != "ZZZ") {
        if (directions[stepIndex] == 'L') {
            numSteps++
            currNode = nodes[currNode]?.left ?: throw Exception("No left node")
        } else {
            numSteps++
            currNode = nodes[currNode]?.right ?: throw Exception("No right node")
        }
        stepIndex++
        if (stepIndex >= directions.length) {
            stepIndex = 0
        }
    }
    return numSteps
}

private class Node(
    val value: String,
    var left: String,
    var right: String
) {

}