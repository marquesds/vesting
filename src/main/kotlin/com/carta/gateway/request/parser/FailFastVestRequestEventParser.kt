package com.carta.gateway.request.parser

import com.carta.entity.EventType
import com.carta.entity.Vest
import com.carta.gateway.request.VestRequestEvent
import com.carta.gateway.request.parser.field.EventTypeParser
import com.carta.gateway.request.parser.field.PrecisionParser
import com.carta.gateway.request.parser.field.QuantityParser
import com.carta.shared.extensions.BigDecimalExtension.withPrecision
import com.carta.shared.extensions.LocalDateExtension
import java.math.BigDecimal
import java.time.LocalDate

class FailFastVestRequestEventParser : IVestRequestEventParser {
    override fun parse(event: VestRequestEvent): Vest? {
        val eventType: EventType = EventTypeParser.parse(event.eventType) ?: return null
        val date: LocalDate = LocalDateExtension.fromIsoDateString(event.date) ?: return null
        val precision: Int = event.precision?.let { PrecisionParser.parse(event.precision) } ?: return null
        val quantity: BigDecimal = QuantityParser.parse(event.quantity)?.withPrecision(precision) ?: return null

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
}
