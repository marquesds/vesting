package com.carta.gateway.request.parser

import com.carta.entity.EventType
import com.carta.entity.Vest
import com.carta.gateway.request.VestRequestEvent
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FailFastVestRequestEventParser : IVestRequestEventParser {
    override fun parse(event: VestRequestEvent): Vest? {
        val eventType: EventType = parseEventType(event.eventType) ?: return null
        val date: LocalDate = parseDate(event.date) ?: return null
        val precision: Int = event.precision?.let { parsePrecision(it) } ?: return null
        val quantity: BigDecimal = parseQuantity(event.quantity)
            ?.round(MathContext(precision, RoundingMode.HALF_DOWN)) ?: return null

        return Vest(
            eventType,
            event.employeeId,
            event.employeeName,
            event.awardId,
            date,
            quantity,
            precision
        )
    }

    private fun parseEventType(eventType: String): EventType? {
        return if (eventType in listOf(
                EventType.VEST.toString(),
                EventType.CANCEL.toString()
            )
        ) EventType.valueOf(eventType)
        else null
    }

    private fun parseDate(date: String): LocalDate? {
        return try {
            LocalDate.parse(date, DateTimeFormatter.ISO_DATE)
        } catch (e: Exception) {
            null
        }
    }

    private fun parsePrecision(precision: String): Int? {
        val parsedPrecision = precision.toIntOrNull()
        return if (parsedPrecision != null) {
            if (parsedPrecision < 0 || parsedPrecision > 6) null
            else parsedPrecision
        } else null
    }

    private fun parseQuantity(quantity: String): BigDecimal? {
        return try {
            BigDecimal(quantity)
        } catch (e: Exception) {
            null
        }
    }
}