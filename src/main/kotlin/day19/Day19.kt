package org.baec23.day19

import org.baec23.util.readLines
import org.baec23.util.splitByChar


fun main() {
    val input = readLines(day = 19, isSample = true)
}

private fun partOne(input: List<String>) {
}

private data class Workflow(
    val name: String,
    val instructions: List<Instruction>,
    val defaultRedirect: String
) {
    companion object {
        fun fromString(input: String): Workflow {
            val name = input.takeWhile { it != '{' }
            val defaultRedirect = input.takeLastWhile { it != '{' }.trim { it == '}' }.takeLastWhile { it != ',' }
            var instructionsString = input.takeLastWhile { it != '{' }.trim { it == '}' }
            val lastIndex = instructionsString.lastIndexOf(',')
            instructionsString = instructionsString.substring(0..<lastIndex)
            val instructions = instructionsString.splitByChar(',').map { instructionString ->
                val operand1 = instructionString.takeWhile { it.isLetterOrDigit() }.first()
                val operatorIndex = instructionString.indexOfFirst { it == '<' || it == '>' }
                val operator = if (instructionString[operatorIndex] == '<') {
                    Operator.IsLessThan
                } else {
                    Operator.IsGreaterThan
                }
                val colonIndex = instructionString.indexOfFirst { it == ':' }
                val operand2 = instructionString.substring(operatorIndex + 1..<colonIndex).toInt()
                val redirect = instructionString.substring(colonIndex + 1)
                Instruction(operand1, operator, operand2, redirect)
            }
            return Workflow(name, instructions, defaultRedirect)
        }
    }
}

private data class Instruction(
    val operand1: Char,
    val operator: Operator,
    val operand2: Int,
    val redirect: String
)

private enum class Operator {
    IsLessThan,
    IsGreaterThan
}

private data class Part(
    val x: Int,
    val m: Int,
    val a: Int,
    val s: Int
) {
    companion object {
        fun fromString(input: String): Part {
            val splitInputs = input.replace(" ", "").trim { it == '{' || it == '}' }.splitByChar(',')
            val x = splitInputs[0].takeLastWhile { it != '=' }.toInt()
            val m = splitInputs[1].takeLastWhile { it != '=' }.toInt()
            val a = splitInputs[2].takeLastWhile { it != '=' }.toInt()
            val s = splitInputs[3].takeLastWhile { it != '=' }.toInt()
            return Part(x, m, a, s)
        }
    }
}