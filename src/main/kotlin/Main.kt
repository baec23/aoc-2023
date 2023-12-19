package org.baec23

import java.io.File

fun main() {
    println("Hello World!")
    val lines = File("src/main/resources/inputs/day1.txt").readLines()
    print(lines.size)
}