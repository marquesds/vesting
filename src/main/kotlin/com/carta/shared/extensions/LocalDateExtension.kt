package com.carta.shared.extensions

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object LocalDateExtension {
    fun fromIsoDateString(isoDate: String): LocalDate? {
        return try {
            LocalDate.parse(isoDate, DateTimeFormatter.ISO_DATE)
        } catch (e: Exception) {
            null
        }
    }
}
