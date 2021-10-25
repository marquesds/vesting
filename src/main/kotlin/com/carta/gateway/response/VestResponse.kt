package com.carta.gateway.response

import com.carta.entity.Vest
import java.math.BigDecimal

data class VestResponse(
    val employeeId: String,
    val employeeName: String,
    val awardId: String,
    val quantity: BigDecimal,
) {

    override fun toString(): String {
        return "$employeeId,$employeeName,$awardId,$quantity"
    }

    companion object {
        fun fromVest(vest: Vest): VestResponse {
            return VestResponse(vest.employeeId, vest.employeeName, vest.awardId, vest.quantity)
        }
    }
}