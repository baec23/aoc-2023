package org.baec23.day19

data class Instruction(
    val operand1: Char,
    val operator: Operator,
    val operand2: Int,
    val redirect: String,
) {
    init {
        val myRange = when (operator) {
            Operator.IsLessThan -> 1..<operand2
            Operator.IsGreaterThan -> operand2 + 1..4000
        }

    }

    fun getRange(): Pair<Char, IntRange> {
        val range = when (operator) {
            Operator.IsLessThan -> 1..<operand2
            Operator.IsGreaterThan -> operand2 + 1..4000
        }
        return Pair(operand1, range)
    }
}

data class InstructionRange(
    private val xRanges: List<IntRange>,
    private val mRanges: List<IntRange>,
    private val aRanges: List<IntRange>,
    private val sRanges: List<IntRange>,
) {
    val count: Long
        get() {
            return xRanges.sumOf { it.last.toLong() - it.first + 1 } *
                    mRanges.sumOf { it.last.toLong() - it.first + 1 } *
                    aRanges.sumOf { it.last.toLong() - it.first + 1 } *
                    sRanges.sumOf { it.last.toLong() - it.first + 1 }
        }

    companion object {
        fun initial(): InstructionRange {
            return InstructionRange(
                listOf(1..4000),
                listOf(1..4000),
                listOf(1..4000),
                listOf(1..4000)
            )
        }

        fun empty(): InstructionRange {
            return InstructionRange(
                listOf(IntRange.EMPTY),
                listOf(IntRange.EMPTY),
                listOf(IntRange.EMPTY),
                listOf(IntRange.EMPTY)
            )
        }
    }

    fun subtract(type: Char, range: IntRange): InstructionRange {
        when (type) {
            'x' -> {
                val newXRanges = mutableListOf<IntRange>()
                xRanges.forEach { xRange ->
                    val startMax = maxOf(xRange.first, range.first)
                    val endMin = minOf(xRange.last, range.last)
                    val left = xRange.first..<startMax
                    if (!left.isEmpty()) {
                        newXRanges.add(left)
                    }
                    val right = endMin + 1..xRange.last
                    if (!right.isEmpty()) {
                        newXRanges.add(right)
                    }
                }
                return this.copy(xRanges = newXRanges)
            }

            'm' -> {
                val newMRanges = mutableListOf<IntRange>()
                mRanges.forEach { mRange ->
                    val startMax = maxOf(mRange.first, range.first)
                    val endMin = minOf(mRange.last, range.last)
                    val left = mRange.first..<startMax
                    if (!left.isEmpty()) {
                        newMRanges.add(left)
                    }
                    val right = endMin + 1..mRange.last
                    if (!right.isEmpty()) {
                        newMRanges.add(right)
                    }
                }
                return this.copy(mRanges = newMRanges)
            }

            'a' -> {
                val newARanges = mutableListOf<IntRange>()
                aRanges.forEach { aRange ->
                    val startMax = maxOf(aRange.first, range.first)
                    val endMin = minOf(aRange.last, range.last)
                    val left = aRange.first..<startMax
                    if (!left.isEmpty()) {
                        newARanges.add(left)
                    }
                    val right = endMin + 1..aRange.last
                    if (!right.isEmpty()) {
                        newARanges.add(right)
                    }
                }
                return this.copy(aRanges = newARanges)
            }

            's' -> {
                val newSRanges = mutableListOf<IntRange>()
                sRanges.forEach { sRange ->
                    val startMax = maxOf(sRange.first, range.first)
                    val endMin = minOf(sRange.last, range.last)
                    val left = sRange.first..<startMax
                    if (!left.isEmpty()) {
                        newSRanges.add(left)
                    }
                    val right = endMin + 1..sRange.last
                    if (!right.isEmpty()) {
                        newSRanges.add(right)
                    }
                }
                return this.copy(sRanges = newSRanges)
            }

            else -> {
                throw Exception("Shouldn't be here")
            }
        }
    }

    fun intersect(type: Char, range: IntRange): InstructionRange {
        when (type) {
            'x' -> {
                val newXRanges = xRanges.map { xRange ->
                    val intersect = xRange.intersect(range)
                    val rangeStart = intersect.minOrNull()
                    val rangeEnd = intersect.maxOrNull()
                    if (rangeStart == null || rangeEnd == null) {
                        IntRange.EMPTY
                    } else {
                        rangeStart..rangeEnd
                    }
                }
                return this.copy(xRanges = newXRanges)
            }

            'm' -> {
                val newMRanges = mRanges.map { mRange ->
                    val intersect = mRange.intersect(range)
                    val rangeStart = intersect.minOrNull()
                    val rangeEnd = intersect.maxOrNull()
                    if (rangeStart == null || rangeEnd == null) {
                        IntRange.EMPTY
                    } else {
                        rangeStart..rangeEnd
                    }
                }
                return this.copy(mRanges = newMRanges)
            }

            'a' -> {
                val newARanges = aRanges.map { aRange ->
                    val intersect = aRange.intersect(range)
                    val rangeStart = intersect.minOrNull()
                    val rangeEnd = intersect.maxOrNull()
                    if (rangeStart == null || rangeEnd == null) {
                        IntRange.EMPTY
                    } else {
                        rangeStart..rangeEnd
                    }
                }
                return this.copy(aRanges = newARanges)
            }

            's' -> {
                val newSRanges = sRanges.map { sRange ->
                    val intersect = sRange.intersect(range)
                    val rangeStart = intersect.minOrNull()
                    val rangeEnd = intersect.maxOrNull()
                    if (rangeStart == null || rangeEnd == null) {
                        IntRange.EMPTY
                    } else {
                        rangeStart..rangeEnd
                    }
                }
                return this.copy(sRanges = newSRanges)
            }

            else -> {
                throw Exception("Shouldn't be here")
            }
        }
    }

    operator fun get(char: Char): List<IntRange> {
        return when (char) {
            'x' -> {
                xRanges.toList()
            }

            'm' -> {
                mRanges.toList()
            }

            'a' -> {
                aRanges.toList()
            }

            's' -> {
                sRanges.toList()
            }

            else -> {
                throw Exception("Should not happen")
            }
        }
    }
}


enum class Operator {
    IsLessThan,
    IsGreaterThan
}
