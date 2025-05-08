package com.example.weatherapp.domain.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

fun getFormattedNow(
    pattern: String = "EEE, MMMM dd hh:mm a",
    zoneId: String = "Africa/Cairo",
    locale: Locale = Locale.ENGLISH
): String {
    val now     = Instant.now().atZone(ZoneId.of(zoneId))
    val fmt     = DateTimeFormatter.ofPattern(pattern, locale)
    return fmt.format(now)
}

fun isDayTime(): Boolean {
    val calendar = Calendar.getInstance()
    val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)

    return hourOfDay in 6..18
}