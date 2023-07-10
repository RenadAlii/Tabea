package com.renad.tabea.core.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtil {

    fun dateFormatter(): SimpleDateFormat = SimpleDateFormat("d MMM yyyy", Locale.getDefault())

    fun timeFormatter(): SimpleDateFormat = SimpleDateFormat("d MMM yyyy", Locale.getDefault())

    fun Long.getDate(): Date? = dateFormatter().parse(dateFormatter().format(this))

    fun Long.getTime(): Date? = timeFormatter().parse(timeFormatter().format(this))

    fun String.getTime(): Date? = timeFormatter().parse(this)

    /**
     *  @return the current date with time = 0 00:00:00 so we will not face problem with the time when compare 2 date.
     */
    fun dateOfToday(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }

    /**
     *  @return the current time = 00:00.
     */
    fun currentTime(): Date? {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time.time.getTime()
    }
}
