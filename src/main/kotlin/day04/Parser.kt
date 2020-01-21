package day04

import java.security.InvalidParameterException


data class Event(val type: EventType, val date: String, val time: String, val guardId: String? = null) {

    companion object {
        fun from(s: String): Event {
            val regex = Regex("^\\[(.*) (.*)](.*)$")
            val matchResult = regex.find(s)
            if (matchResult != null) {
                val (date, time, eventLine) = matchResult.destructured
                return EventType.parse(date, time, eventLine)
            }
            throw InvalidParameterException()
        }
    }

}

enum class EventType {
    WAKING_UP {
        override fun parseEventLine(date: String, time: String, eventLine: String): Event? {
            val regex = Regex(" wakes up")
            val matchResult = regex.find(eventLine)
            return if (matchResult != null) {
                Event(type = WAKING_UP, date = date, time = time)
            } else {
                null
            }
        }
    },
    FALLING_ASLEEP {
        override fun parseEventLine(date: String, time: String, eventLine: String): Event? {
            val regex = Regex(" falls asleep")
            val matchResult = regex.find(eventLine)
            return if (matchResult != null) {
                Event(type = FALLING_ASLEEP, date = date, time = time)
            } else {
                null
            }
        }
    },
    NEW_SHIFT {
        override fun parseEventLine(date: String, time: String, eventLine: String): Event? {
            val regex = Regex(" Guard #(.*) begins shift")
            val matchResult = regex.find(eventLine)
            return if (matchResult != null) {
                val (guardId) = matchResult.destructured
                Event(type = NEW_SHIFT, guardId = guardId, date = date, time = time)
            } else {
                null
            }
        }
    };

    abstract fun parseEventLine(date: String, time: String, eventLine: String): Event?

    companion object {

        fun parse(date: String, time: String, eventLine: String): Event =
            values().mapNotNull { it.parseEventLine(date, time, eventLine) }.first()
    }
}
