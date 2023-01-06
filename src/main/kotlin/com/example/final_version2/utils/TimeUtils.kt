package com.example.final_version2.utils

import java.util.*

fun getTodayTimeStamp(): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    return calendar.time.time
}

fun getOneDayTime(): Long {
    return 1000 * 60 * 60 * 24
}

