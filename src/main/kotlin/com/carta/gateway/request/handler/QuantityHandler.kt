package com.carta.gateway.request.handler

import com.carta.gateway.request.VestRequestEvent
import com.carta.gateway.request.parser.field.QuantityParser
import com.carta.shared.type.Either
import com.carta.shared.type.Left
import com.carta.shared.type.Right
import java.math.BigDecimal

object QuantityHandler : IHandler<BigDecimal> {
    override fun handle(event: VestRequestEvent): Either<String, BigDecimal> {
        val result = QuantityParser.parse(event.quantity)
        return if (result != null) Right(result) else Left("${event.quantity} is an invalid quantity.")
    }
}