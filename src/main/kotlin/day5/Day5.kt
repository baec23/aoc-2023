package org.baec23.day5

import org.baec23.util.readLines

fun main() {
    val input = readLines(day = 5, isSample = true)
    println(partTwo(input))
}

private fun partTwo(input: List<String>): Long {
//    val seedRanges = input.first().split(':').last().trim().split(' ').map { it.toLong() }
    val mappers = parseInputToMappers(input)

    val mapper1 = mappers[MapperType.LocationToHumidity]!!
    val mapper2 = mappers[MapperType.HumidityToTemperature]!!
    val mapper3 = mappers[MapperType.TemperatureToLight]!!
    val mapper4 = mappers[MapperType.LightToWater]!!
    val mapper5 = mappers[MapperType.WaterToFertilizer]!!
    val mapper6 = mappers[MapperType.FertilizerToSoil]!!
    val mapper7 = mappers[MapperType.SoilToSeed]!!
    mapper1.ranges.sortBy{
        it.range.first
    }
    val humidityRanges = mapper1.ranges.map{
        mapper1.getMappedValue(it.range.first)..mapper1.getMappedValue(it.range.last)
    }
    val temperatureRanges = humidityRanges.map{
        mapper2.getMappedValue(it.first)..mapper2.getMappedValue(it.last)
    }
    val lightRanges = temperatureRanges.map{
        mapper3.getMappedValue(it.first)..mapper3.getMappedValue(it.last)
    }
    val waterRanges = lightRanges.map{
        mapper4.getMappedValue(it.first)..mapper4.getMappedValue(it.last)
    }
    val fertilizerRanges = waterRanges.map{
        mapper5.getMappedValue(it.first)..mapper5.getMappedValue(it.last)
    }
    val soilRanges = fertilizerRanges.map{
        mapper6.getMappedValue(it.first)..mapper6.getMappedValue(it.last)
    }
    var seedRanges = soilRanges.map{
        mapper7.getMappedValue(it.first)..mapper7.getMappedValue(it.last)
    }
    seedRanges = seedRanges.map{
        minOf(it.first, it.last)..maxOf(it.first,it.last)
    }


    return 1
}

private fun partOne(input: List<String>): Long {
    val seeds = input.first().split(':').last().trim().split(' ').map { it.toLong() }

    val mappers = parseInputToMappers(input)
    val soils =
        seeds.map { seed -> mappers[MapperType.SeedToSoil]?.getMappedValue(seed) ?: throw Exception("Something wrong") }
    val fertilizers =
        soils.map { soil ->
            mappers[MapperType.SoilToFertilizer]?.getMappedValue(soil) ?: throw Exception("Something wrong")
        }
    val waters =
        fertilizers.map { fertilizer ->
            mappers[MapperType.FertilizerToWater]?.getMappedValue(fertilizer) ?: throw Exception("Something wrong")
        }
    val lights =
        waters.map { water ->
            mappers[MapperType.WaterToLight]?.getMappedValue(water) ?: throw Exception("Something wrong")
        }
    val temperatures =
        lights.map { light ->
            mappers[MapperType.LightToTemperature]?.getMappedValue(light) ?: throw Exception("Something wrong")
        }
    val humidities =
        temperatures.map { temp ->
            mappers[MapperType.TemperatureToHumidity]?.getMappedValue(temp) ?: throw Exception("Something wrong")
        }
    val locations =
        humidities.map { humidity ->
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
        val reverseMapperType = when {
            mapperString.startsWith("seed") -> MapperType.SoilToSeed
            mapperString.startsWith("soil") -> MapperType.FertilizerToSoil
            mapperString.startsWith("fertilizer") -> MapperType.WaterToFertilizer
            mapperString.startsWith("water") -> MapperType.LightToWater
            mapperString.startsWith("light") -> MapperType.TemperatureToLight
            mapperString.startsWith("temperature") -> MapperType.HumidityToTemperature
            mapperString.startsWith("humidity") -> MapperType.LocationToHumidity
            else -> throw Exception("Something wrong")
        }
        currLineIndex++
        val ranges = mutableListOf<MappedRange>()
        val reverseRanges = mutableListOf<MappedRange>()
        while (currLineIndex < input.size && input[currLineIndex].isNotEmpty()) {
            val values = input[currLineIndex].split(' ').map { it.toLong() }
            val offset = values[0] - values[1]
            val reverseOffset = values[1] - values[0]
            ranges.add(
                MappedRange(
                    range = values[1]..<values[1] + values[2],
                    offset = offset
                )
            )
            reverseRanges.add(
                MappedRange(
                    range = values[0]..<values[0] + values[2],
                    offset = reverseOffset
                )
            )
            currLineIndex++
        }
        toReturn[mapperType] = Mapper(ranges)
        toReturn[reverseMapperType] = Mapper(reverseRanges)
    }
    return toReturn
}

class Mapper(
    inRanges: List<MappedRange>
) {
    var ranges = inRanges.toMutableList()

    init {
        var currStart = 0L
        val rangesSortedByStart = inRanges.sortedBy { mappedRange -> mappedRange.range.first }
        rangesSortedByStart.forEach { mappedRange ->
            if (currStart < mappedRange.range.first - 1) {
                ranges.add(MappedRange(currStart..<mappedRange.range.first, 0))
            }
            currStart = mappedRange.range.last + 1
        }
        if (currStart < Long.MAX_VALUE - 1) {
            ranges.add(MappedRange(currStart..Long.MAX_VALUE - 1, 0))
        }
    }

    fun getMappedValue(input: Long): Long {
        ranges.forEach { mappedRange ->
            if (input in mappedRange.range) {
                return input + mappedRange.offset
            }
        }
        return input
    }

    fun getOffsetAtValue(input: Long): Long {
        ranges.forEach { mappedRange ->
            if (input in mappedRange.range) {
                return mappedRange.offset
            }
        }
        throw Exception("WHAT - ${input}")
    }

    fun getMergedRanges(otherMapper: Mapper): List<MappedRange> {
        val rangeBoundaries = mutableListOf<Long>()
        ranges.forEach { mappedRange ->
            if (!rangeBoundaries.contains(mappedRange.range.first)) {
                rangeBoundaries.add(mappedRange.range.first)
            }
            if (!rangeBoundaries.contains(mappedRange.range.last + 1)) {
                rangeBoundaries.add(mappedRange.range.last + 1)
            }
        }
        otherMapper.ranges.forEach { mappedRange ->
            if (!rangeBoundaries.contains(mappedRange.range.first)) {
                rangeBoundaries.add(mappedRange.range.first)
            }
            if (!rangeBoundaries.contains(mappedRange.range.last + 1)) {
                rangeBoundaries.add(mappedRange.range.last + 1)
            }
        }
        rangeBoundaries.sort()
        val toReturn = mutableListOf<MappedRange>()
        for (i in 0..<rangeBoundaries.size - 1) {
            val myMappedValue = this.getMappedValue(rangeBoundaries[i])
            val otherMappedValue = otherMapper.getMappedValue(myMappedValue)
            val offset = otherMappedValue - myMappedValue + this.getOffsetAtValue(rangeBoundaries[i])
            toReturn.add(MappedRange(rangeBoundaries[i]..<rangeBoundaries[i + 1], offset))
        }
        return toReturn.toList()
    }
}

data class MappedRange(
    val range: LongRange,
    val offset: Long
)

enum class MapperType {
    SeedToSoil,
    SoilToSeed,
    SoilToFertilizer,
    FertilizerToSoil,
    FertilizerToWater,
    WaterToFertilizer,
    WaterToLight,
    LightToWater,
    LightToTemperature,
    TemperatureToLight,
    TemperatureToHumidity,
    HumidityToTemperature,
    HumidityToLocation,
    LocationToHumidity
}