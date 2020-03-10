package day04

import day04.EventType.FALLING_ASLEEP
import day04.EventType.WAKING_UP

typealias GuardId = String
typealias Minute = Int

fun String.calculateNumberOfSleepingMinutes(): Int = toEvents().calculateNumberOfSleepingMinutes()

fun String.findAsleepMinutes(): List<Minute> = toEvents().findAsleepMinutes()

fun List<Event>.findAsleepMinutes(): List<Minute> {

    val fallsAsleepEvents = filter { it.type == FALLING_ASLEEP }
    val wakesUpEvents = filter { it.type == WAKING_UP }

    return fallsAsleepEvents
        .zip(wakesUpEvents)
        .flatMap { (fallsAsleepEvent, wakesUpEvent) ->
            val fallingAsleepMinute = fallsAsleepEvent.extractMinute()
            val wakingUpMinute = wakesUpEvent.extractMinute()

            fallingAsleepMinute until wakingUpMinute
        }
}

fun String.findAllSleepingMinutesForEachGuard(): Map<GuardId, List<Minute>> {
    return toEvents()
        .groupBy { it.guardId!! }
        .mapValues { (_, events) -> events.findAsleepMinutes() }
}

fun String.findTheNumberOfTimesEachGuardSleep(): Set<Triple<GuardId, Minute, Int>> {
    return findAllSleepingMinutesForEachGuard()
        .mapValues { (_, minutes) ->
            minutes.groupingBy { it }.eachCount()
        }
        .flatMap { (guardId, mapOfMinutesAndCount) ->
            mapOfMinutesAndCount.map { (minute, count) ->
                Triple(guardId, minute, count)
            }
        }.toSet()
}

fun String.findTheGuardMostFrequentlyAsleepOnSameMinute(): Pair<GuardId, Minute> {

    val mostFrequentAsleepMinute = findTheNumberOfTimesEachGuardSleep()
        .maxBy { (_, _, count) -> count }

    return Pair(mostFrequentAsleepMinute!!.first, mostFrequentAsleepMinute.second)
}

fun String.findMostFrequentAsleepMinute(selectedGuardId: GuardId): Minute? {
    return toEvents()
        .filter { it.guardId == selectedGuardId }
        .findAsleepMinutes()
        .groupingBy { it }
        .eachCount()
        .maxBy { it.value }
        ?.key
}

private fun List<Event>.calculateNumberOfSleepingMinutes(): Int {
    val fallsAsleepEvents = filter { it.type == FALLING_ASLEEP }
    val wakesUpEvents = filter { it.type == WAKING_UP }

    return fallsAsleepEvents
        .zip(wakesUpEvents)
        .map { (fallsAsleepEvent, wakesUpEvent) ->
            wakesUpEvent - fallsAsleepEvent
        }
        .sum()
}

fun String.calculateSleepingMinutesForSeveralGuards(): Map<GuardId, Int> {
    return toEvents()
        .groupBy { it.guardId }
        .mapNotNull { (guardId, events) ->
            guardId!! to events.calculateNumberOfSleepingMinutes()
        }
        .toMap()
}

fun String.calculateMaxSleepingTimeGuard(): GuardId? {
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
        return this.extractMinute() - otherEvent.extractMinute()
    }

    fun extractMinute(): Minute = time.split(":")[1].toInt()

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
