package unit.com.carta.entity

import com.appmattus.kotlinfixture.kotlinFixture
import com.carta.entity.Vest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import java.math.BigDecimal

@Tag("unit")
class VestTest {

    private val fixture = kotlinFixture()

    @ParameterizedTest
    @ValueSource(strings = ["20", "30", "27.5"])
    fun `should decrease a quantity`(quantity: String) {
        val vest = fixture<Vest>() {
            property(Vest::quantity) { BigDecimal("200") }
        }

        val quantityToDecrease = BigDecimal(quantity)
        val expectedQuantity = vest.decreaseQuantity(quantityToDecrease)

        expectThat(expectedQuantity).isEqualTo(BigDecimal("200") - quantityToDecrease)
    }

    @ParameterizedTest
    @ValueSource(strings = ["10", "30", "27.5"])
    fun `should return zero when the value to be decreased is greater or equal than the vest value`(quantity: String) {
        val vest = fixture<Vest>() {
            property(Vest::quantity) { BigDecimal("10") }
        }

        val expectedQuantity = vest.decreaseQuantity(BigDecimal(quantity))
        expectThat(expectedQuantity).isEqualTo(BigDecimal.ZERO)
    }

}