package s.yarlykov.fixdataproto.domain.time

import java.text.SimpleDateFormat
import java.util.*

class TimeLineHandler(startTime: Long = System.currentTimeMillis()) {

    private val timeFormat = "dd-HH-mm-ss"

    private val day = 0
    private val hour = 1
    private val minute = 2

    private val parsedStartTime = parseTime(startTime)

    private var lastDay: Int = parsedStartTime[day]
    private var lastHour: Int = parsedStartTime[hour]
    private var lastMinute: Int = parsedStartTime[minute]

    /**
     * Вернуть маркер, который клеится к маркет дате
     */
    fun getMarker(time: Long): TimeLineMarker =
        TimeLineMarker(time, getEvent(time))

    /**
     * Определить произошел ли переход дня/часа/минуты
     * Переход дня подразумевает также переход часа и минуты,
     * а переход часа - переход минуты.
     */
    private fun getEvent(time: Long): TimeEvent {
        val currentTime = parseTime(time)

        return if (currentTime[day] != lastDay) {
            lastDay = currentTime[day]
            TimeEvent.DAY
        } else if (currentTime[hour] != lastHour) {
            lastHour = currentTime[hour]
            TimeEvent.HOUR
        } else if (currentTime[minute] != lastMinute) {
            lastMinute = currentTime[minute]
            TimeEvent.MINUTE
        } else {
            TimeEvent.SECOND
        }
    }

    private fun parseTime(time: Long): List<Int> {
        val sdf = SimpleDateFormat(timeFormat, Locale.getDefault())
        return sdf
            .format(time)
            .split("-".toRegex())
            .map {
                it.toInt()
            }
    }
}