package org.baec23.day20


abstract class Module(val name: String) {
    val inputs = mutableListOf<Module>()
    val destinations = mutableListOf<Module>()
    abstract fun handlePulse(pulse: Pulse)
}

class Button(name: String) : Module(name) {
    override fun handlePulse(pulse: Pulse) {
        destinations.forEach { destination -> destination.handlePulse(pulse) }
    }
}

class Broadcaster(name: String) : Module(name) {
    override fun handlePulse(pulse: Pulse) {
        destinations.forEach { destination -> destination.handlePulse(pulse) }
    }
}

class FlipFlop(name: String) : Module(name) {
    private var isOn: Boolean = false
    override fun handlePulse(pulse: Pulse) {
        if (pulse.type == 0) {
            isOn = !isOn
            if (isOn) {
                destinations.forEach { destination -> destination.handlePulse(Pulse(this.name, 1)) }
            } else {
                destinations.forEach { destination -> destination.handlePulse(Pulse(this.name, 0)) }
            }
        }
    }
}

class Conjunction(name: String) : Module(name) {
    private val prevInputStates = HashMap<String, Int>()
    fun init() {
        inputs.forEach { input ->
            prevInputStates[input.name] = 0
        }
    }

    override fun handlePulse(pulse: Pulse) {
        //update
        prevInputStates[pulse.senderName] = pulse.type
        //fire
        val pulseToSend = if (prevInputStates.values.sum() == prevInputStates.size) 0 else 1
        destinations.forEach { destination -> destination.handlePulse(Pulse(this.name, pulseToSend)) }
    }
}

data class Pulse(
    val senderName: String,
    val type: Int,
)