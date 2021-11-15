package unit.com.carta.gateway.request.parser.field

import com.carta.gateway.request.parser.field.QuantityParser
import org.junit.jupiter.api.Tag
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNull
import java.math.BigDecimal

@Tag("unit")
class QuantityParserTest {

    @ParameterizedTest
    @ValueSource(strings = ["200", "1000", "10.5"])
    fun `should parse a valid quantity`(validQuantity: String) {
        expectThat(QuantityParser.parse(validQuantity))
            .isEqualTo(BigDecimal(validQuantity))
    }

    @ParameterizedTest
    @ValueSource(strings = ["-100", "invalid_quantity"])
    fun `should not parse an invalid quantity`(invalidQuantity: String) {
        expectThat(QuantityParser.parse(invalidQuantity)).isNull()
    }
}
