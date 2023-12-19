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

/*
Notes:
0..55 -> 0..55		offset 0
56..92 -> 60..96	offset 4
93..96 -> 56..59	offset 37
97..inf -> 97..inf	offset 0

0..68 -> 1..69		offset 1
69..69 -> 0..0		offset -69

0..55		offset 1
56..68		offset 5
69..69		offset -65
70..92		offset 4
93..96		offset 37
97..inf		offset 0



18..24		offset 70
25..94		offset 7

53

7 1




====

0..55		offset = 1
56..68		offset = 5
69..69		offset = -69
70..92		offset = 4
93..96		offset = -37
97..inf		offset = 0

light -> temp
45..63		offset 36
77..99
64..76

0	1
45	40
56	40
64	9
69	8
70	8
77	-31
93	-27
97	-27
100	0

===


temp = 78 => location = 82
temp = 42 => location = 43
temp = 82 => location = 86
temp = 34 => location = 35

light = 74 => location = 82
light = 42 => location = 43
light = 46 => location = 86
light = 34 => location = 35

water = 81 => location = 82
water = 49 => location = 43
water = 53 => location = 47
water = 41 => location = 35
79 -> 82
14 -> 36
55 -> 47
13 -> 35
1

temp = 78 => location = 82
temp = 42 => location = 43
temp = 82 => location = 86
temp = 34 => location = 35

light = 74 => location = 82
light = 42 => location = 43
light = 46 => location = 83
light = 34 => location = 35

water = 81 => location = 82
water = 49 => location = 43
water = 53 => location = 47
water = 41 => location = 35

===

myRanges
otherRanges

humidity -> location
0..55 -> 0..55
56..93 -> 60..96
93..96 -> 56..59
97..inf -> 97..inf

inRanges	outRanges
minimize outRanges

temperature -> humidity
0..68 -> 1..69
69..69 -> 0..0
70..inf -> 70..inf

convert location's inRanges
to my inRanges

merge location's inRanges with my outRanges
0..55
	0..0, 1..55
56..93
	56..68, 69..69, 70..93
93..96
	93..96
97..inf
	97..inf

convert to my inRanges
0..0, 1..55
	69..69, 1..55
56..68, 69..69, 70..93

=====

next dude's inRange in terms of my inRange
0..55 -> 0..55
56..93 -> 60..96
93..96 -> 56..59
97..inf -> 97..inf

myRanges
0..68 -> 1..69
69..69 -> 0..0
70..inf -> 70..inf

for each range, compare with my outRanges
0..55
	0..0 and 1..69
56..93
	0..68 and 69..69 and 70..inf
93..96
	70..inf
97..inf
	70..inf





 */