package com.postcrud.component.formatter

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DateFormatter : Formatter {
    private val dateFormat: DateFormat =
        SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    override fun getFormatDate(time: Long): String {
        val date = Date()
        date.time = time
        return dateFormat.format(date.time).toString()
    }
}