package org.baec23.day19

import org.baec23.util.readLines

/*
Start from 'in'
Only deal in ranges
if 'R' return 0
if 'A' return size of current range
sum all
 */

fun main() {
    val input = readLines(day = 19, isSample = false)
    println(partTwo(input))
}

private fun partTwo(input: List<String>): Long {
    val workflows = HashMap<String, Workflow>()
    val parts = mutableListOf<Part>()
    var isWorkflow = true
    input.forEach { line ->
        if (line.isEmpty()) {
            isWorkflow = false
        } else {
            if (isWorkflow) {
                val toAdd = Workflow.fromString(line)
                workflows[toAdd.name] = toAdd
            } else {
                parts.add(Part.fromString(line))
            }
        }
    }

    //START
    return calcTotalAccepted(workflows, "in", InstructionRange.initial())
}

private fun calcTotalAccepted(
    workflows: Map<String, Workflow>,
    workflowName: String,
    cumulativeRange: InstructionRange
): Long {
    val workflow = workflows[workflowName]!!
    val mappedRanges = workflow.mapRanges(cumulativeRange = cumulativeRange)
    var toReturn = 0L
    mappedRanges.forEach {
        val range = it.first
        val redirect = it.second
        when (redirect) {
            "A" -> {
                toReturn += range.count
            }

            "R" -> {}
            else -> {
                toReturn += calcTotalAccepted(workflows, redirect, range)
            }
        }
    }
    return toReturn
}

private fun partOne(input: List<String>): Long {
    val workflows = HashMap<String, Workflow>()
    val parts = mutableListOf<Part>()
    var isWorkflow = true
    input.forEach { line ->
        if (line.isEmpty()) {
            isWorkflow = false
        } else {
            if (isWorkflow) {
                val toAdd = Workflow.fromString(line)
                workflows[toAdd.name] = toAdd
            } else {
                parts.add(Part.fromString(line))
            }
        }
    }
    var toReturn = 0L
    parts.forEach { part ->
        println("Processing ${part} ")
        val result = processPart(part, workflows)
        toReturn += result
    }
    return toReturn
}

private fun processPart(part: Part, workflows: Map<String, Workflow>): Long {
    val initialWorkflow = workflows["in"]!!
    var result = processWorkflow(part, initialWorkflow)
    while (result is WorkflowResult.Redirected) {
        result = processWorkflow(part, workflows[result.redirect]!!)
    }
    return when (result) {
        is WorkflowResult.Accepted -> result.ratingTotal
        WorkflowResult.Rejected -> 0
        else -> throw Exception("I don't think this should happen")
    }
}

private fun processWorkflow(part: Part, workflow: Workflow): WorkflowResult {
    println("Workflow : ${workflow.name}")
    workflow.instructions.forEach { instruction ->
        if (part.satisfiesInstruction(instruction)) {
            return when (instruction.redirect) {
                "A" -> {
                    WorkflowResult.Accepted(part.x + part.m + part.a + part.s.toLong())
                }

                "R" -> {
                    WorkflowResult.Rejected
                }

                else -> {
                    WorkflowResult.Redirected(instruction.redirect)
                }
            }
        }
    }
    return when (workflow.defaultRedirect) {
        "A" -> {
            WorkflowResult.Accepted(part.x + part.m + part.a + part.s.toLong())
        }

        "R" -> {
            WorkflowResult.Rejected
        }

        else -> {
            WorkflowResult.Redirected(workflow.defaultRedirect)
        }
    }
}

