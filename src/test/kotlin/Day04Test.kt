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
}
