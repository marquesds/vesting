package unit.com.carta.shared.extension

import com.carta.shared.extensions.BigDecimalExtension.toString
import com.carta.shared.extensions.BigDecimalExtension.withPrecision
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.hasLength
import strikt.assertions.isEqualTo
import strikt.assertions.isNotEqualTo
import java.math.BigDecimal

@Tag("unit")
class BigDecimalExtensionTest {

    @Test
    fun `should convert to string keeping the precision`() {
        val expectedString = "10.0005"

        expectThat(BigDecimal(expectedString).toString(6))
            .hasLength(7)
            .isEqualTo(expectedString)
    }

    @Test
    fun `should keep the given precision without changing it when converting to a string`() {
        val expectedString = "10.0005"

        expectThat(BigDecimal(expectedString).toString(5))
            .hasLength(6)
            .isNotEqualTo(expectedString)
    }

    @Test
    fun `should round down with given precision`() {
        val expectedBigDecimal = BigDecimal("10.059")

        expectThat(expectedBigDecimal.withPrecision(4))
            .isEqualTo(BigDecimal("10.06"))
    }

}