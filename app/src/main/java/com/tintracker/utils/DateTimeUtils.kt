package com.tintracker.utils

import com.tintracker.data.model.DiffDate
import java.text.SimpleDateFormat
import java.util.*

fun convertDateDayMonthYear(date: Date): String {
    val df = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())
    return df.format(date).toString()
}

fun calculateDiffTwoDate(date1: Long, date2: Long): DiffDate {
    val diff: Long = date2 - date1
    val diffSeconds = diff / 1000 % 60
    var diffMinutes = diff / (60 * 1000) % 60
    val diffHours = diff / (60 * 60 * 1000) % 24
    val diffDays = diff / (24 * 60 * 60 * 1000)

    if (diffSeconds > 30L) {
        diffMinutes += 1
    }

    return DiffDate(
        diffSeconds = diffSeconds,
        diffMinutes = diffMinutes,
        diffHours = diffHours,
        diffDays = diffDays
    )
}

fun zeroTimeOfDate(date: Date): Date {
    val cal = Calendar.getInstance()
    cal.time = date
    cal[Calendar.HOUR_OF_DAY] = 0
    cal[Calendar.MINUTE] = 0
    cal[Calendar.SECOND] = 0
    cal[Calendar.MILLISECOND] = 0
    return cal.time
}

fun endTimeOfDate(date: Date): Date {
    val cal = Calendar.getInstance()
    cal.time = date
    cal[Calendar.HOUR_OF_DAY] = 23
    cal[Calendar.MINUTE] = 59
    cal[Calendar.SECOND] = 59
    cal[Calendar.MILLISECOND] = 999
    return cal.time
}