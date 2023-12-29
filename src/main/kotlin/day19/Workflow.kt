package org.baec23.day19

import org.baec23.util.splitByChar

data class Workflow(
    val name: String, val instructions: List<Instruction>, val defaultRedirect: String
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

    fun mapRanges(cumulativeRange: InstructionRange): List<Pair<InstructionRange, String>> {
        val toReturn = mutableListOf<Pair<InstructionRange, String>>()
        var myCumulativeRange = cumulativeRange
        val instructionRanges = mutableListOf<Pair<Char, IntRange>>()
        instructions.forEach { instruction ->
            val pair = instruction.getRange()
            instructionRanges.add(pair)
            val iType = pair.first
            val iRange = pair.second
            val myRange = myCumulativeRange.intersect(iType, iRange)
            myCumulativeRange = myCumulativeRange.subtract(iType, iRange)
            toReturn.add(Pair(myRange, instruction.redirect))
        }

        //Calc default
        toReturn.add(Pair(myCumulativeRange, defaultRedirect))
        return toReturn
    }
}

sealed class WorkflowResult {
    data class Accepted(val ratingTotal: Long) : WorkflowResult()
    data object Rejected : WorkflowResult()
    data class Redirected(val redirect: String) : WorkflowResult()
}