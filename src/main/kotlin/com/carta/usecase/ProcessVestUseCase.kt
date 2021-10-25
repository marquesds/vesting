package com.carta.usecase

import com.carta.entity.EventType
import com.carta.entity.Vest
import java.math.BigDecimal
import java.time.LocalDate

data class VestKey(val employeeId: String, val awardId: String)

class ProcessVestUseCase() : IProcessVestUseCase {

    override fun process(vests: List<Vest>, targetDate: LocalDate): List<Vest> =
        vests
            .map { setAwardGreaterThanTargetDateToZero(it, targetDate) }
            .groupBy { VestKey(it.employeeId, it.awardId) }
            .toSortedMap(compareBy<VestKey> { it.employeeId }.thenBy { it.awardId })
            .map { it.value }
            .map {
                it.reduce { acc, vestEvent ->
                    when (vestEvent.eventType) {
                        EventType.CANCEL -> acc.copy(quantity = acc.decreaseQuantity(vestEvent.quantity))
                        else -> acc.copy(quantity = acc.quantity + vestEvent.quantity)
                    }
                }
            }

    private fun setAwardGreaterThanTargetDateToZero(vest: Vest, targetDate: LocalDate): Vest {
        return if (vest.date <= targetDate) vest
        else vest.copy(quantity = BigDecimal.ZERO)
    }
}