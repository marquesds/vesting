package com.carta.usecase

import com.carta.entity.Vest
import java.time.LocalDate

interface IProcessVestUseCase {

    fun process(vests: List<Vest>, targetDate: LocalDate): List<Vest>

}