package com.carta.gateway.request.parser.field

interface IEventFieldParser<A> {
    fun parse(value: String): A?
}