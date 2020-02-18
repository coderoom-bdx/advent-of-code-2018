package day04

import day04.EventType.FALLING_ASLEEP
import day04.EventType.WAKING_UP

fun String.calculateSleepingMinutes(): Int = toEvents().calculateSleepingMinutes()

fun String.findAsleepMinutes() = toEvents().findAsleepMinutes()

/*
TODO part 2

Guard 10 -> 20, 21, 22, 45, 46, 45, 45 -> (45, 3), (20, 1), (21, 1), etc.
Guard 99 -> 4, 5, 6, 5                 -> (5, 2), (4, 1), (6, 1),
Guard 12 -> 30, 31, 32                 -> (30, 1), etc.


Map<String, List<Event>>

typealias Minute = Int
typealias Count = Int

Map<String, List<Pair<Minute, Count>>
-> mapValues()
Map<String, Pair<Minute, Count>>
-> maxBy(values.second)

MapEntry<String, Pair<Minute, Count>> -> String x Minute

 */

fun List<Event>.findAsleepMinutes(): List<Int> {

    val fallsAsleepEvents = filter { it.type == FALLING_ASLEEP }
    val wakesUpEvents = filter { it.type == WAKING_UP }

    return fallsAsleepEvents
        .zip(wakesUpEvents)
        .flatMap { (fallsAsleepEvent, wakesUpEvent) ->
            val fallingAsleepMinute = fallsAsleepEvent.extractMinutes()
            val wakingUpMinute = wakesUpEvent.extractMinutes()

            fallingAsleepMinute until wakingUpMinute
        }
}

fun String.findAllSleepingMinutesForEachGuard(): Map<String, List<Int>> {
    return toEvents()
        .groupBy { it.guardId!!}
        .mapValues { it.value.findAsleepMinutes() }
}
fun String.findTheNumberOfTimesEachGuardSleep(): Map<String, Map<Int, Int>> {
    return findAllSleepingMinutesForEachGuard()
        .mapValues { it.value
            .groupingBy { minute->minute }
            .eachCount() }
}

fun String.findTheGuardMostFrequentlyAsleepOnSameMinute(): Pair<String, Int> {

    toEvents()
        .findAsleepMinutes()
        .groupingBy { it }



    return Pair("99", 45)
}

fun String.findMostFrequentAsleepMinute(selectedGuardId: String): Int? {
    return toEvents()
        .filter { it.guardId == selectedGuardId }
        .findAsleepMinutes()
        .groupingBy { it }
        .eachCount()
        .maxBy { it.value }
        ?.key
}

private fun List<Event>.calculateSleepingMinutes(): Int {
    val fallsAsleepEvents = filter { it.type == FALLING_ASLEEP }
    val wakesUpEvents = filter { it.type == WAKING_UP }

    return fallsAsleepEvents
        .zip(wakesUpEvents)
        .map { (fallsAsleepEvent, wakesUpEvent) ->
            wakesUpEvent - fallsAsleepEvent
        }
        .sum()
}

fun String.calculateSleepingMinutesForSeveralGuards(): Map<String, Int> {
    return toEvents()
        .groupBy { it.guardId }
        .mapNotNull { (guardId, events) ->
            guardId!! to events.calculateSleepingMinutes()
        }
        .toMap()
}

fun String.calculateMaxSleepingTimeGuard(): String? {
    return calculateSleepingMinutesForSeveralGuards()
        .maxBy { (_, sleepingTime) -> sleepingTime }?.key
}

data class ParsingContext(val parsedEvents: List<Event>, val currentGuardId: String?)

private fun String.toEvents(): List<Event> {
    val parsingContext = ParsingContext(listOf(), null)

    return this
        .trim()
        .split("\n")
        .sorted()
        .fold(parsingContext) { context, line ->
            val event = Event.from(line)
            val currentGuardId = event.guardId ?: context.currentGuardId

            ParsingContext(
                parsedEvents = context.parsedEvents + event.copy(guardId = currentGuardId),
                currentGuardId = currentGuardId
            )
        }
        .parsedEvents
}

data class Event(val type: EventType, val date: String, val time: String, val guardId: String? = null) {

    operator fun minus(otherEvent: Event): Int {
        return this.extractMinutes() - otherEvent.extractMinutes()
    }

    fun extractMinutes() = time.split(":")[1].toInt()

    companion object {
        fun from(line: String): Event {
            val regex = Regex("^\\[(.*) (.*)](.*)$")
            val matchResult = regex.find(line)
            if (matchResult != null) {
                val (date, time, eventLine) = matchResult.destructured
                return EventType.parse(date, time, eventLine)
            } else throw IllegalStateException()
        }
    }

}

enum class EventType {
    WAKING_UP {
        override fun parseEventLine(date: String, time: String, eventLine: String) =
            if (eventLine.contains(" wakes up")) {
                Event(type = this, date = date, time = time)
            } else null
    },
    FALLING_ASLEEP {
        override fun parseEventLine(date: String, time: String, eventLine: String) =
            if (eventLine.contains(" falls asleep")) {
                Event(type = this, date = date, time = time)
            } else null
    },
    NEW_SHIFT {
        override fun parseEventLine(date: String, time: String, eventLine: String): Event? {
            val regex = Regex(" Guard #(.*) begins shift")
            val matchResult = regex.find(eventLine)
            return if (matchResult != null) {
                val (guardId) = matchResult.destructured
                Event(type = this, guardId = guardId, date = date, time = time)
            } else null
        }
    };

    abstract fun parseEventLine(date: String, time: String, eventLine: String): Event?

    companion object {
        fun parse(date: String, time: String, eventLine: String): Event =
            values()
                .mapNotNull { it.parseEventLine(date, time, eventLine) }
                .first()
    }
}
