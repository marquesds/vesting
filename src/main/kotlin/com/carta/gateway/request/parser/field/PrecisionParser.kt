package com.carta.gateway.request.parser.field

object PrecisionParser : IEventFieldParser<Int> {
    override fun parse(value: String): Int? {
        val parsedPrecision = value.toIntOrNull()
        return if (parsedPrecision != null) {
            if (parsedPrecision < 0 || parsedPrecision > 6) null
            else parsedPrecision
        } else null
    }
}