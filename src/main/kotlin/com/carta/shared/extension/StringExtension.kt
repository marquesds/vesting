package com.carta.shared.extension

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object StringExtension {
    fun String.toLocalDate(): LocalDate? {
        return try {
            LocalDate.parse(this, DateTimeFormatter.ISO_DATE)
        } catch (e: Exception) {
            null
        }
    }
}
