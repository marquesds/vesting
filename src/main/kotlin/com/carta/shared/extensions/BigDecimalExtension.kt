package com.carta.shared.extensions

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

object BigDecimalExtension {
    fun BigDecimal.toString(precision: Int): String {
        return "${this.withPrecision(precision)}"
    }

    fun BigDecimal.withPrecision(precision: Int, roundingMode: RoundingMode = RoundingMode.HALF_DOWN): BigDecimal {
        return this.round(MathContext(precision, roundingMode))
    }
}
