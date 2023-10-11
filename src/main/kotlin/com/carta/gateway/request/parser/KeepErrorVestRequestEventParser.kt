package com.carta.gateway.request.parser

import com.carta.entity.EventType
import com.carta.entity.Vest
import com.carta.gateway.request.VestRequestEvent
import com.carta.gateway.request.handler.EventTypeHandler
import com.carta.gateway.request.handler.IsoDateHandler
import com.carta.gateway.request.handler.PrecisionHandler
import com.carta.gateway.request.handler.QuantityHandler
import com.carta.shared.extension.BigDecimalExtension.withPrecision
import com.carta.shared.type.Left
import com.carta.shared.type.Right
import java.math.BigDecimal
import java.time.LocalDate

class KeepErrorVestRequestEventParser() : IVestRequestEventParser {
    private val handlers = setOf(EventTypeHandler, IsoDateHandler, QuantityHandler, PrecisionHandler)
    var errors: List<String> = emptyList()

    override fun parse(event: VestRequestEvent): Vest? {
        val results = handlers.map { it.handle(event) }
        val parsedResults = results.filterIsInstance<Right<Any>>().map { it.value }
        errors = results.filterIsInstance<Left<String>>().map { it.value }

        return if (errors.isNotEmpty()) null else
            Vest(
                parsedResults[0] as EventType,
                event.employeeId,
                event.employeeName,
                event.awardId,
                parsedResults[1] as LocalDate,
                (parsedResults[2] as BigDecimal).withPrecision(parsedResults[3] as Int),
                parsedResults[3] as Int
            )
    }
}