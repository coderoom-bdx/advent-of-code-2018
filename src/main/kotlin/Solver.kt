import day04.calculateMaxSleepingTimeGuard
import day04.findMostFrequentAsleepMinute
import day04.findTheGuardMostFrequentlyAsleepOnSameMinute
import java.io.File

fun main() {

    val logLines = File("src/main/resources/guards.log")
        .readText()

    val sleepiestGuardId = logLines.calculateMaxSleepingTimeGuard()!!
    val mostFrequentAsleepMinute = logLines.findMostFrequentAsleepMinute(sleepiestGuardId)

    println("Sleepiest Guard $sleepiestGuardId")
    println("mostFrequentAsleepMinute $mostFrequentAsleepMinute")

    println("Answer : ${sleepiestGuardId.toInt() * mostFrequentAsleepMinute!!}")

    println("Part 2")

    val (guardId, minute) = logLines.findTheGuardMostFrequentlyAsleepOnSameMinute()
    println("GuardId : $guardId")
    println("Minute : $minute")
    println("Answer : ${guardId.toInt() * minute}")
}