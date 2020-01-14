package day04


object Parser {
    fun parseEvent(line: String): NewShiftEvent? {
        val regex = Regex("^\\[(.*) (.*)] Guard #(.*) begins shift")
        val matchResult = regex.find(line)
        return if (matchResult != null) {
            val (date, time, guardId) = matchResult.destructured
            NewShiftEvent(guardId, date, time)
        } else {
            null
        }
    }

    fun parseSleepEvent(line: String): SleepEvent? {
        val regex = Regex("^\\[(.*) (.*)] falls asleep")
        val matchResult = regex.find(line)
        return if (matchResult != null) {
            val (date, time) = matchResult.destructured
            SleepEvent(date, time)
        } else {
            null
        }
    }

    fun parseWakeUpEvent(line: String): WakeUpEvent? {
        val regex = Regex("^\\[(.*) (.*)] wakes up")
        val matchResult = regex.find(line)
        return if (matchResult != null) {
            val (date, time) = matchResult.destructured
            WakeUpEvent(date, time)
        } else {
            null
        }
    }

}

data class WakeUpEvent(val date: String, val time: String)

data class SleepEvent(val date: String, val time: String)

data class NewShiftEvent(val guardId: String, val date: String, val time:String)
