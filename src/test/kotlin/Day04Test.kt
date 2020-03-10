import day04.*
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test

class Day04Test {

    @Test
    fun `should parse new shift event`() {
        Event.from("[1518-11-01 00:00] Guard #10 begins shift") shouldBe Event(
            type = EventType.NEW_SHIFT,
            guardId = "10",
            date = "1518-11-01",
            time = "00:00"
        )
    }

    @Test
    fun `should parse new sleep event`() {
        Event.from("[1518-11-01 00:05] falls asleep") shouldBe Event(
            type = EventType.FALLING_ASLEEP,
            date = "1518-11-01",
            time = "00:05"
        )
    }

    @Test
    fun `should parse new wake up event`() {
        Event.from("[1518-11-01 00:25] wakes up") shouldBe Event(
            type = EventType.WAKING_UP,
            date = "1518-11-01",
            time = "00:25"
        )
    }

    @Test
    fun `should return number of sleeping minutes`() {
        val input = """
           [1518-11-01 00:00] Guard #10 begins shift
           [1518-11-01 00:05] falls asleep
           [1518-11-01 00:25] wakes up
           [1518-11-01 00:30] falls asleep
           [1518-11-01 00:55] wakes up
       """.trimIndent()

        input.calculateNumberOfSleepingMinutes() shouldBe 45
    }

    @Test
    fun `should return number of sleeping minutes for each guard`() {
        val input = """
            [1518-11-01 00:00] Guard #10 begins shift
            [1518-11-01 00:05] falls asleep
            [1518-11-01 00:25] wakes up
            [1518-11-01 00:30] falls asleep
            [1518-11-01 00:55] wakes up
            [1518-11-01 23:58] Guard #99 begins shift
            [1518-11-02 00:40] falls asleep
            [1518-11-02 00:50] wakes up
        """.trimIndent()
        input.calculateSleepingMinutesForSeveralGuards() shouldBe mapOf("10" to 45, "99" to 10)
    }

    @Test
    fun `should return number of sleeping minutes for each guard with unsorted lines`() {
        val input = """
            [1518-11-01 00:55] wakes up
            [1518-11-01 23:58] Guard #99 begins shift
            [1518-11-02 00:40] falls asleep
            [1518-11-01 00:25] wakes up
            [1518-11-01 00:30] falls asleep
            [1518-11-01 00:00] Guard #10 begins shift
            [1518-11-01 00:05] falls asleep
            [1518-11-02 00:50] wakes up
        """.trimIndent()
        input.calculateSleepingMinutesForSeveralGuards() shouldBe mapOf("10" to 45, "99" to 10)

        val input2 = """
            [1518-11-01 00:55] wakes up
            [1518-11-01 23:58] Guard #99 begins shift
            [1518-11-03 23:48] Guard #99 begins shift
            [1518-11-02 00:40] falls asleep
            [1518-11-01 00:25] wakes up
            [1518-11-01 00:30] falls asleep
            [1518-11-01 00:00] Guard #10 begins shift
            [1518-11-01 00:05] falls asleep
            [1518-11-02 00:50] wakes up
            [1518-11-04 00:25] falls asleep
            [1518-11-04 00:40] wakes up
        """.trimIndent()
        input2.calculateSleepingMinutesForSeveralGuards() shouldBe mapOf("10" to 45, "99" to 25)

        """
            [1518-11-01 00:00] Guard #10 begins shift
            [1518-11-01 00:05] falls asleep
            [1518-11-01 00:25] wakes up
            [1518-11-01 00:30] falls asleep
            [1518-11-01 00:55] wakes up
            [1518-11-01 23:58] Guard #99 begins shift
            [1518-11-02 00:40] falls asleep
            [1518-11-02 00:50] wakes up
            [1518-11-03 00:05] Guard #10 begins shift
            [1518-11-03 00:24] falls asleep
            [1518-11-03 00:29] wakes up
            [1518-11-04 00:02] Guard #99 begins shift
            [1518-11-04 00:36] falls asleep
            [1518-11-04 00:46] wakes up
            [1518-11-05 00:03] Guard #99 begins shift
            [1518-11-05 00:45] falls asleep
            [1518-11-05 00:55] wakes up
        """.trimIndent().calculateSleepingMinutesForSeveralGuards() shouldBe mapOf("10" to 50, "99" to 30)
    }

    @Test
    fun `should return the max sleeping time guard`() {
        val input = """
            [1518-11-01 00:55] wakes up
            [1518-11-01 23:58] Guard #99 begins shift
            [1518-11-02 00:40] falls asleep
            [1518-11-01 00:25] wakes up
            [1518-11-01 00:30] falls asleep
            [1518-11-01 00:00] Guard #10 begins shift
            [1518-11-01 00:05] falls asleep
            [1518-11-02 00:50] wakes up
        """.trimIndent()
        input.calculateMaxSleepingTimeGuard() shouldBe "10"
    }

    @Test
    fun `should find the minutes that a guard spent asleep for one night`() {
        val input = """
            [1518-11-01 00:00] Guard #10 begins shift
            [1518-11-01 00:05] falls asleep
            [1518-11-01 00:10] wakes up
            [1518-11-01 00:40] falls asleep
            [1518-11-01 00:42] wakes up
       """.trimIndent()

        input.findAsleepMinutes() shouldBe listOf(
            5, 6, 7, 8, 9, 40, 41
        )
    }

    @Test
    fun `should find the minute that a guard spends asleep the most`() {

        """
            [1518-11-01 00:00] Guard #10 begins shift
            [1518-11-01 00:05] falls asleep
            [1518-11-01 00:25] wakes up
            [1518-11-01 00:30] falls asleep
            [1518-11-01 00:55] wakes up
            [1518-11-01 23:58] Guard #99 begins shift
            [1518-11-02 00:40] falls asleep
            [1518-11-02 00:50] wakes up
            [1518-11-03 00:05] Guard #10 begins shift
            [1518-11-03 00:24] falls asleep
            [1518-11-03 00:29] wakes up
            [1518-11-04 00:02] Guard #99 begins shift
            [1518-11-04 00:36] falls asleep
            [1518-11-04 00:46] wakes up
            [1518-11-05 00:03] Guard #99 begins shift
            [1518-11-05 00:45] falls asleep
            [1518-11-05 00:55] wakes up
       """.trimIndent().findMostFrequentAsleepMinute("10") shouldBe 24

        """
            [1518-11-01 00:00] Guard #10 begins shift
            [1518-11-01 00:05] falls asleep
            [1518-11-01 00:25] wakes up
            [1518-11-01 00:30] falls asleep
            [1518-11-01 00:55] wakes up
            [1518-11-01 23:58] Guard #99 begins shift
            [1518-11-02 00:40] falls asleep
            [1518-11-02 00:50] wakes up
            [1518-11-03 00:05] Guard #10 begins shift
            [1518-11-03 00:24] falls asleep
            [1518-11-03 00:29] wakes up
            [1518-11-04 00:02] Guard #99 begins shift
            [1518-11-04 00:36] falls asleep
            [1518-11-04 00:46] wakes up
            [1518-11-05 00:03] Guard #99 begins shift
            [1518-11-05 00:45] falls asleep
            [1518-11-05 00:55] wakes up
       """.trimIndent().findMostFrequentAsleepMinute("18") shouldBe null

    }

    @Test
    internal fun `should find the guard most frequently asleep on same minute`() {
        """
            [1518-11-01 00:00] Guard #10 begins shift
            [1518-11-01 00:05] falls asleep
            [1518-11-01 00:25] wakes up
            [1518-11-01 00:30] falls asleep
            [1518-11-01 00:55] wakes up
            [1518-11-01 23:58] Guard #99 begins shift
            [1518-11-02 00:40] falls asleep
            [1518-11-02 00:50] wakes up
            [1518-11-03 00:05] Guard #10 begins shift
            [1518-11-03 00:24] falls asleep
            [1518-11-03 00:29] wakes up
            [1518-11-04 00:02] Guard #99 begins shift
            [1518-11-04 00:36] falls asleep
            [1518-11-04 00:46] wakes up
            [1518-11-05 00:03] Guard #99 begins shift
            [1518-11-05 00:45] falls asleep
            [1518-11-05 00:55] wakes up
       """.trimIndent().findTheGuardMostFrequentlyAsleepOnSameMinute() shouldBe Pair("99", 45)
    }

    @Test
    internal fun `should find all sleeping minutes for each guard`() {
        """
            [1518-11-01 00:00] Guard #10 begins shift
            [1518-11-01 00:05] falls asleep
            [1518-11-01 00:07] wakes up
            [1518-11-01 00:30] falls asleep
            [1518-11-01 00:33] wakes up
            [1518-11-01 23:58] Guard #99 begins shift
            [1518-11-02 00:40] falls asleep
            [1518-11-02 00:45] wakes up
       """.trimIndent().findAllSleepingMinutesForEachGuard() shouldBe mapOf(
            "10" to listOf(5, 6, 30, 31, 32),
            "99" to listOf(40, 41, 42, 43, 44)
        )
    }

    @Test
    internal fun `should find the number of times sleeping minutes for each guard with triple`() {
        """
            [1518-11-01 00:00] Guard #10 begins shift
            [1518-11-01 00:05] falls asleep
            [1518-11-01 00:07] wakes up
            [1518-11-01 00:30] falls asleep
            [1518-11-01 00:33] wakes up
            [1518-11-03 00:00] Guard #10 begins shift
            [1518-11-03 00:04] falls asleep
            [1518-11-03 00:07] wakes up
            [1518-11-01 23:58] Guard #99 begins shift
            [1518-11-02 00:40] falls asleep
            [1518-11-02 00:45] wakes up
       """.trimIndent().findTheNumberOfTimesEachGuardSleep() shouldBe setOf(
            Triple("10", 4, 1),
            Triple("10", 5, 2),
            Triple("10", 6, 2),
            Triple("10", 30, 1),
            Triple("10", 31, 1),
            Triple("10", 32, 1),
            Triple("99", 40, 1),
            Triple("99", 41, 1),
            Triple("99", 42, 1),
            Triple("99", 43, 1),
            Triple("99", 44, 1)
        )
    }
}











