package org.baec23.util

import java.io.File

private const val inputSourcePath = "src/main/resources/inputs/"
fun readLines(day: Int, isSample: Boolean = false): List<String> {
    var pathString = inputSourcePath
    pathString += "day${day}"
    if (isSample) {
        pathString += "_sample"
    }
    pathString += ".txt"
    return File(pathString).readLines()
}

fun String.splitByWhitespace(): List<String> {
    return this.split("\\s+".toRegex())
}

fun String.splitByChar(char: Char): List<String> {
    val regexSafeChar = Regex.escape(char.toString())
    return this.split("${regexSafeChar}+".toRegex()).filter{it.isNotEmpty()}
}