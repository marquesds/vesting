package com.carta.gateway.request.parser.field

import com.carta.entity.EventType

object EventTypeParser : IEventFieldParser<EventType> {
    override fun parse(value: String): EventType? {
        return if (value in listOf(
                EventType.VEST.toString(),
                EventType.CANCEL.toString()
            )
        ) EventType.valueOf(value)
        else null
    }
}