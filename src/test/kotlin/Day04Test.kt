import day04.NewShiftEvent
import day04.Parser
import day04.SleepEvent
import day04.WakeUpEvent
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test

class Day04Test {

    @Test
    fun `should parse new shift event`() {
        Parser.parseEvent("[1518-11-01 00:00] Guard #10 begins shift") shouldBe
                NewShiftEvent(guardId = "10", date = "1518-11-01", time = "00:00")
    }

    @Test
    internal fun `should parse new sleep event`() {
        Parser.parseSleepEvent("[1518-11-01 00:05] falls asleep") shouldBe
                SleepEvent(date = "1518-11-01", time = "00:05")
    }

    @Test
    internal fun `should parse new wake up event`() {
        Parser.parseWakeUpEvent("[1518-11-01 00:25] wakes up") shouldBe WakeUpEvent(date = "1518-11-01", time = "00:25")
    }
}