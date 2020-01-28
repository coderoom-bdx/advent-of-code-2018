package day04

import day04.EventType.FALLING_ASLEEP
import day04.EventType.WAKING_UP

fun String.calculateSleepingMinutes(): Int {

    val events = this.split("\n").map { Event.from(it) }
    val fallsAsleepEvents = events.filter { it.type == FALLING_ASLEEP }
    val wakesUpEvents = events.filter { it.type == WAKING_UP }

    return fallsAsleepEvents
        .zip(wakesUpEvents)
        .map { (fallsAsleepEvent, wakesUpEvent) ->
            wakesUpEvent - fallsAsleepEvent }
        .sum()
}
fun String.calculateSleepingMinutesForSeveralGuards(): Map<String, Int> {
    return mapOf()
}

data class Event(val type: EventType, val date: String, val time: String, val guardId: String? = null) {

    operator fun minus(otherEvent: Event): Int {
        return this.extractMinutes() - otherEvent.extractMinutes()
    }

    private fun extractMinutes() = time.split(":")[1].toInt()

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
