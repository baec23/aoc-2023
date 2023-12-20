package org.baec23.day5

import org.baec23.util.readLines

fun main() {
    val input = readLines(day = 5, isSample = false)
    val inValues = input.first().split(':').last().trim().split(' ').map { it.toLong() }
    val inRange1 = inValues[0]..<inValues[0] + inValues[1]
    val inRange2 = inValues[2]..<inValues[2] + inValues[3]
    val mappers = parseInputToMappers(input)
    val lowestSeed = partTwo(inRange1, inRange2, mappers)

    val seedSoil = mappers[MapperType.SeedToSoil]!!
    val soilFertilizer = mappers[MapperType.SoilToFertilizer]!!
    val fertilizerWater = mappers[MapperType.FertilizerToWater]!!
    val waterLight = mappers[MapperType.WaterToLight]!!
    val lightTemperature = mappers[MapperType.LightToTemperature]!!
    val temperatureHumidity = mappers[MapperType.TemperatureToHumidity]!!
    val humidityLocation = mappers[MapperType.HumidityToLocation]!!
    println(humidityLocation.getMappedValue(
        temperatureHumidity.getMappedValue(
            lightTemperature.getMappedValue(
                waterLight.getMappedValue(
                    fertilizerWater.getMappedValue(soilFertilizer.getMappedValue(seedSoil.getMappedValue(lowestSeed)))
                )
            )
        )
    ))

}

private fun partTwo(inRange1: LongRange, inRange2: LongRange, mappers: Map<MapperType, Mapper>): Long {
    val seedSoil = mappers[MapperType.SeedToSoil]!!
    val soilFertilizer = mappers[MapperType.SoilToFertilizer]!!
    val fertilizerWater = mappers[MapperType.FertilizerToWater]!!
    val waterLight = mappers[MapperType.WaterToLight]!!
    val lightTemperature = mappers[MapperType.LightToTemperature]!!
    val temperatureHumidity = mappers[MapperType.TemperatureToHumidity]!!
    val humidityLocation = mappers[MapperType.HumidityToLocation]!!

    val sortedLocationRanges = humidityLocation.ranges.sortedBy { mappedRange ->
        mappedRange.outRange.first
    }.map { it.inRange }
    val seedRanges = seedSoil.backConvertRanges(
        soilFertilizer.backConvertRanges(
            fertilizerWater.backConvertRanges(
                waterLight.backConvertRanges(
                    lightTemperature.backConvertRanges(temperatureHumidity.backConvertRanges(sortedLocationRanges))
                )
            )
        )
    )
    seedRanges.forEach { seedRange ->
        var minValue = Long.MAX_VALUE
        var start = maxOf(inRange1.first, seedRange.first)
        var end = minOf(inRange1.last, seedRange.last)
        if (start <= end) {
            minValue = start
        }
        start = maxOf(inRange2.first, seedRange.first)
        end = minOf(inRange2.last, seedRange.last)
        if (start <= end) {
            minValue = start
        }
        if (minValue < Long.MAX_VALUE) {
            return minValue
        }
    }
    return 1

//    testValue(mappers, false)
//    seedSoil.getMappedValue(79)
}

private fun testValue(mappers: Map<MapperType, Mapper>, reverse: Boolean = false) {
    val seedSoil = mappers[MapperType.SeedToSoil]!!
    val soilFertilizer = mappers[MapperType.SoilToFertilizer]!!
    val fertilizerWater = mappers[MapperType.FertilizerToWater]!!
    val waterLight = mappers[MapperType.WaterToLight]!!
    val lightTemperature = mappers[MapperType.LightToTemperature]!!
    val temperatureHumidity = mappers[MapperType.TemperatureToHumidity]!!
    val humidityLocation = mappers[MapperType.HumidityToLocation]!!
    if (!reverse) {
        print("Seed? ")
        var input = readln()
        while (input.isNotEmpty()) {
            val seed = input.toLong()
            val soil = seedSoil.getMappedValue(seed)
            val fertilizer = soilFertilizer.getMappedValue(soil)
            val water = fertilizerWater.getMappedValue(fertilizer)
            val light = waterLight.getMappedValue(water)
            val temperature = lightTemperature.getMappedValue(light)
            val humidity = temperatureHumidity.getMappedValue(temperature)
            val location = humidityLocation.getMappedValue(humidity)
            println(
                """
                Seed ${seed}
                Soil ${soil}
                Fertilizer ${fertilizer}
                Water ${water}
                Light ${light}
                Temperature ${temperature}
                Humidity ${humidity}
                Location ${location}
            """.trimIndent() + "\n"
            )
            print("Seed? ")
            input = readln()
        }
    } else {
        print("Location? ")
        var input = readln()
        while (input.isNotEmpty()) {
            val location = input.toLong()
            val humidity = humidityLocation.getReverseMappedValue(location)
            val temperature = temperatureHumidity.getReverseMappedValue(humidity)
            val light = lightTemperature.getReverseMappedValue(temperature)
            val water = waterLight.getReverseMappedValue(light)
            val fertilizer = fertilizerWater.getReverseMappedValue(water)
            val soil = soilFertilizer.getReverseMappedValue(fertilizer)
            val seed = seedSoil.getReverseMappedValue(soil)
            println(
                """
                    Location ${location}
                    Humidity ${humidity}
                    Temperature ${temperature}
                    Light ${light}
                    Water ${water}
                    Fertilizer ${fertilizer}
                    Soil ${soil}
                    Seed ${seed}
            """.trimIndent() + "\n"
            )
            print("Location? ")
            input = readln()
        }
    }
}

private fun partOne(input: List<String>): Long {
    val seeds = input.first().split(':').last().trim().split(' ').map { it.toLong() }

    val mappers = parseInputToMappers(input)
    val soils =
        seeds.map { seed -> mappers[MapperType.SeedToSoil]?.getMappedValue(seed) ?: throw Exception("Something wrong") }
    val fertilizers = soils.map { soil ->
        mappers[MapperType.SoilToFertilizer]?.getMappedValue(soil) ?: throw Exception("Something wrong")
    }
    val waters = fertilizers.map { fertilizer ->
        mappers[MapperType.FertilizerToWater]?.getMappedValue(fertilizer) ?: throw Exception("Something wrong")
    }
    val lights = waters.map { water ->
        mappers[MapperType.WaterToLight]?.getMappedValue(water) ?: throw Exception("Something wrong")
    }
    val temperatures = lights.map { light ->
        mappers[MapperType.LightToTemperature]?.getMappedValue(light) ?: throw Exception("Something wrong")
    }
    val humidities = temperatures.map { temp ->
        mappers[MapperType.TemperatureToHumidity]?.getMappedValue(temp) ?: throw Exception("Something wrong")
    }
    val locations = humidities.map { humidity ->
        mappers[MapperType.HumidityToLocation]?.getMappedValue(humidity) ?: throw Exception("Something wrong")
    }
    return locations.minOrNull() ?: throw Exception("Something wrong")
}

private fun parseInputToMappers(input: List<String>): Map<MapperType, Mapper> {
    val toReturn = HashMap<MapperType, Mapper>()
    var currLineIndex = 1
    while (currLineIndex < input.size) {
        while (!input[currLineIndex].endsWith(':')) {
            currLineIndex++
        }
        val mapperString = input[currLineIndex].takeWhile { it != ' ' }
        val mapperType = when {
            mapperString.startsWith("seed") -> MapperType.SeedToSoil
            mapperString.startsWith("soil") -> MapperType.SoilToFertilizer
            mapperString.startsWith("fertilizer") -> MapperType.FertilizerToWater
            mapperString.startsWith("water") -> MapperType.WaterToLight
            mapperString.startsWith("light") -> MapperType.LightToTemperature
            mapperString.startsWith("temperature") -> MapperType.TemperatureToHumidity
            mapperString.startsWith("humidity") -> MapperType.HumidityToLocation
            else -> throw Exception("Something wrong")
        }
        currLineIndex++
        val ranges = mutableListOf<MappedRange>()
        while (currLineIndex < input.size && input[currLineIndex].isNotEmpty()) {
            val values = input[currLineIndex].split(' ').map { it.toLong() }
            ranges.add(
                MappedRange(
                    inRange = values[1]..<values[1] + values[2],
                    outRange = values[0]..<values[0] + values[2]
                )
            )
            currLineIndex++
        }
        toReturn[mapperType] = Mapper(ranges)
    }
    return toReturn
}

class Mapper(
    ranges: List<MappedRange>,
) {
    val ranges: MutableList<MappedRange> = ranges.toMutableList()

    init {
        var currStart = 0L
        val rangesSortedByStart = ranges.sortedBy { it.inRange.first }
        rangesSortedByStart.forEach { mappedRange ->
            if (currStart < mappedRange.inRange.first - 1) {
                this.ranges.add(
                    MappedRange(
                        currStart..<mappedRange.inRange.first,
                        currStart..<mappedRange.inRange.first
                    )
                )
            }
            currStart = mappedRange.inRange.last + 1
        }
        if (currStart < Long.MAX_VALUE - 1) {
            this.ranges.add(MappedRange(currStart..<Long.MAX_VALUE, currStart..<Long.MAX_VALUE))
        }
    }

    fun getMappedValue(input: Long): Long {
        ranges.forEach { mappedRange ->
            if (input in mappedRange.inRange) {
                return mappedRange.inToOut(input)
            }
        }
        return input
    }

    fun getReverseMappedValue(input: Long): Long {
        ranges.forEach { mappedRange ->
            if (input in mappedRange.outRange) {
                return mappedRange.outToIn(input)
            }
        }
        return input
    }

    fun backConvertRanges(otherRanges: List<LongRange>): List<LongRange> {
        val toReturn = mutableListOf<LongRange>()
        val toAdd = mutableListOf<LongRange>()
        otherRanges.forEach { otherRange ->
            this.ranges.forEach { myRange ->
                val start = maxOf(myRange.outRange.first, otherRange.first)
                val end = minOf(myRange.outRange.last, otherRange.last)
                if (start <= end) {
                    toAdd.add(getReverseMappedValue(start)..getReverseMappedValue(end))
                }
            }
            if (toAdd.isNotEmpty()) {
                toAdd.sortBy { it.last }
                toReturn.addAll(toAdd)
                toAdd.clear()
            }
        }
        return toReturn
    }
}

data class MappedRange(
    val inRange: LongRange,
    val outRange: LongRange
) {
    fun inToOut(value: Long): Long {
        if (value == Long.MAX_VALUE - 1) {
            return Long.MAX_VALUE - 1
        }
        return outRange.elementAt(inRange.indexOf(value))
    }

    fun outToIn(value: Long): Long {
        if (value == Long.MAX_VALUE - 1) {
            return Long.MAX_VALUE - 1
        }
        return inRange.elementAt(outRange.indexOf(value))
    }
}

enum class MapperType {
    SeedToSoil,
    SoilToFertilizer,
    FertilizerToWater,
    WaterToLight,
    LightToTemperature,
    TemperatureToHumidity,
    HumidityToLocation,
}

