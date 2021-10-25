package com.carta.gateway.request.parser

import com.carta.entity.Vest
import com.carta.gateway.request.VestRequestEvent

interface IVestRequestEventParser {

    fun parse(event: VestRequestEvent): Vest?

}