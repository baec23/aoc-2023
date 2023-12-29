package org.baec23.day20

import org.baec23.util.readLines
import org.baec23.util.splitByWhitespace

fun main() {
    val input = readLines(day = 20, isSample = true)
}

private fun partOne(input: List<String>) {
}

class Day20(input: List<String>) {
    var numLowPulses = 0
    var numHighPulses = 0
    val modules = mutableMapOf<String, Module>()

    init {
        input.forEach { line ->
            val moduleString = line.splitByWhitespace().first()
            val startChar = moduleString.first()
            when (startChar) {
                //FlipFlop
                '%' -> {
                    val moduleName = moduleString.substring(1)
                    modules[moduleName] = FlipFlop(moduleName)
                }
                //Conjunction
                '&' -> {
                    val moduleName = moduleString.substring(1)
                    modules[moduleName] = Conjunction(moduleName)
                }
                //Broadcaster
                else -> {
                    modules[moduleString] = Broadcaster(moduleString)
                }
            }
        }
        input.forEach { line ->

        }
    }
}