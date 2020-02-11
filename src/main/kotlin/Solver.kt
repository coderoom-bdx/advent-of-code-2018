import day04.calculateMaxSleepingTimeGuard
import day04.findMostFrequentAsleepMinute
import java.io.File

fun main() {

    val logLines = File("src/main/resources/guards.log")
        .readText()

    val sleepiestGuardId = logLines.calculateMaxSleepingTimeGuard()!!
    val mostFrequentAsleepMinute = logLines.findMostFrequentAsleepMinute(sleepiestGuardId)

    println("Sleepiest Guard $sleepiestGuardId")
    println("mostFrequentAsleepMinute $mostFrequentAsleepMinute")

    println("Answer : ${sleepiestGuardId.toInt() * mostFrequentAsleepMinute!!}")

}