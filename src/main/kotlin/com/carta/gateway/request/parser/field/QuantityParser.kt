package com.carta.gateway.request.parser.field

import java.math.BigDecimal

object QuantityParser : IEventFieldParser<BigDecimal> {
    override fun parse(value: String): BigDecimal? {
        return try {
            val parsedQuantity = BigDecimal(value)
            if (parsedQuantity >= BigDecimal.ZERO) parsedQuantity
            else null
        } catch (e: Exception) {
            null
        }
    }
}