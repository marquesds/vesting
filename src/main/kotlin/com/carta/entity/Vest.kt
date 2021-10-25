package com.carta.entity

import java.math.BigDecimal
import java.time.LocalDate

enum class EventType {
    VEST, CANCEL
}

data class Vest(
    val eventType: EventType,
    val employeeId: String,
    val employeeName: String,
    val awardId: String,
    val date: LocalDate,
    val quantity: BigDecimal,
    val precision: Int? = 0
) {
    fun decreaseQuantity(cancelQuantity: BigDecimal): BigDecimal =
        when {
            quantity - cancelQuantity <= BigDecimal.ZERO -> BigDecimal.ZERO
            else -> quantity - cancelQuantity
        }
}