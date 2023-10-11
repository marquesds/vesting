package com.carta.gateway.request.handler

import com.carta.gateway.request.VestRequestEvent
import com.carta.shared.extension.StringExtension.toLocalDate
import com.carta.shared.type.Either
import com.carta.shared.type.Left
import com.carta.shared.type.Right
import java.time.LocalDate

object IsoDateHandler : IHandler<LocalDate> {
    override fun handle(event: VestRequestEvent): Either<String, LocalDate> {
        val result = event.date.toLocalDate()
        return if (result != null) Right(result) else Left("${event.date} is an invalid date.")
    }
}