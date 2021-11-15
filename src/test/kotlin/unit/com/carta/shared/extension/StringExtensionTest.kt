package unit.com.carta.shared.extension

import com.carta.shared.extension.StringExtension.toLocalDate
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNull
import java.time.LocalDate

@Tag("unit")
class StringExtensionTest {

    @Test
    fun `should parse an iso date string`() {
        val validIsoDate = "2021-10-01"

        expectThat(validIsoDate.toLocalDate())
            .isEqualTo(LocalDate.of(2021, 10, 1))
    }

    @Test
    fun `should return null when the iso date is invalid`() {
        val invalidIsoDate = "2021-13-01"

        expectThat(invalidIsoDate.toLocalDate()).isNull()
    }
}