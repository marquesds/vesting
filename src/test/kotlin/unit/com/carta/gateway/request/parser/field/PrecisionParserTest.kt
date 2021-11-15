package unit.com.carta.gateway.request.parser.field

import com.carta.gateway.request.parser.field.PrecisionParser
import org.junit.jupiter.api.Tag
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNull

@Tag("unit")
class PrecisionParserTest {

    @ParameterizedTest
    @ValueSource(strings = ["0", "1", "2", "3", "4", "5", "6"])
    fun `should parse a valid precision`(validPrecision: String) {
        expectThat(PrecisionParser.parse(validPrecision))
            .isEqualTo(validPrecision.toInt())
    }

    @ParameterizedTest
    @ValueSource(strings = ["-1", "7", "invalid_precision"])
    fun `should not parse an invalid precision`(invalidPrecision: String) {
        expectThat(PrecisionParser.parse(invalidPrecision)).isNull()
    }

}