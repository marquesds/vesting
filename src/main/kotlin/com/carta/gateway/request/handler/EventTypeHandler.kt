package com.carta.gateway.request.handler

import com.carta.entity.EventType
import com.carta.gateway.request.VestRequestEvent
import com.carta.gateway.request.parser.field.EventTypeParser
import com.carta.shared.type.Either
import com.carta.shared.type.Left
import com.carta.shared.type.Right

object EventTypeHandler : IHandler<EventType> {
    override fun handle(event: VestRequestEvent): Either<String, EventType> {
        val result = EventTypeParser.parse(event.eventType)
        return if (result != null) Right(result) else Left("${event.eventType} is an invalid EventType.")
    }
}