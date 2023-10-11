package com.carta.gateway.request.handler

import com.carta.gateway.request.VestRequestEvent
import com.carta.shared.type.Either

interface IHandler<A> {
    fun handle(event: VestRequestEvent): Either<String, A>
}