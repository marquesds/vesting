package com.carta.gateway.request.handler

import com.carta.gateway.request.VestRequestEvent
import com.carta.gateway.request.parser.field.PrecisionParser
import com.carta.shared.type.Either
import com.carta.shared.type.Left
import com.carta.shared.type.Right

object PrecisionHandler : IHandler<Int> {
    override fun handle(event: VestRequestEvent): Either<String, Int> {
        val result = event.precision?.let { PrecisionParser.parse(it) }
        return if (result != null) Right(result) else Left("${event.precision} is an invalid precision.")
    }
}