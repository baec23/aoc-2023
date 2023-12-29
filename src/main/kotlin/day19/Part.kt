package org.baec23.day19

import org.baec23.util.splitByChar


data class Part(
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

    fun satisfiesInstruction(instruction: Instruction): Boolean {
        var toReturn = false
        val myOperand1 = when (instruction.operand1) {
            'x' -> this.x
            'm' -> this.m
            'a' -> this.a
            's' -> this.s
            else -> {
                throw Exception("Shouldn't happen")
            }
        }
        if (instruction.operator == Operator.IsLessThan) {
            if (myOperand1 < instruction.operand2) {
                toReturn = true
            }
        } else {
            if (myOperand1 > instruction.operand2) {
                toReturn = true
            }
        }
        return toReturn
    }
}