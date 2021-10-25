package com.carta.gateway.request

import com.carta.entity.Vest
import com.carta.gateway.request.parser.IVestRequestEventParser

data class VestRequestEvent(
    val eventType: String,
    val employeeId: String,
    val employeeName: String,
    val awardId: String,
    val date: String,
    val quantity: String,
    val precision: String? = "0"
) {

    fun toVest(parser: IVestRequestEventParser): Vest? {
        return parser.parse(this)
    }
}
