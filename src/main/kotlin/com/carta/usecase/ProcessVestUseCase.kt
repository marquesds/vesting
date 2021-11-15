package com.carta.usecase

import com.carta.entity.EventType
import com.carta.entity.Vest
import java.time.LocalDate

data class VestKey(val employeeId: String, val awardId: String)

class ProcessVestUseCase() : IProcessVestUseCase {

    override fun process(vests: List<Vest>, targetDate: LocalDate): List<Vest> =
        vests
            .map { it.setAwardGreaterThanTargetDateToZero(targetDate) }
            .groupBy { VestKey(it.employeeId, it.awardId) }
            .toSortedMap(orderByEmployeeIdAndAwardIdAsc())
            .map { it.value }
            .map { it.reduce { acc, vestEvent -> changeQuantity(acc, vestEvent) } }

    private fun orderByEmployeeIdAndAwardIdAsc() =
        compareBy<VestKey> { it.employeeId }.thenBy { it.awardId }

    private fun changeQuantity(acc: Vest, vestEvent: Vest): Vest =
        when (vestEvent.eventType) {
            EventType.CANCEL -> acc.copy(quantity = acc.decreaseQuantity(vestEvent.quantity))
            else -> acc.copy(quantity = acc.quantity + vestEvent.quantity)
        }
}