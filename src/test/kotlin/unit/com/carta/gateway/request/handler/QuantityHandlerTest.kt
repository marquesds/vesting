package unit.com.carta.gateway.request.handler

import com.appmattus.kotlinfixture.kotlinFixture
import com.carta.entity.EventType
import com.carta.gateway.request.VestRequestEvent
import com.carta.gateway.request.handler.QuantityHandler
import com.carta.shared.type.unsafeLeft
import com.carta.shared.type.unsafeRight
import org.junit.jupiter.api.Tag
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import java.math.BigDecimal

@Tag("unit")
class QuantityHandlerTest {

    private val fixture = kotlinFixture()

    @ParameterizedTest
    @ValueSource(strings = ["200", "1000", "10.5"])
    fun `should return a valid quantity`(validQuantity: String) {
        val vestRequest = fixture<VestRequestEvent>() {
            property(VestRequestEvent::eventType) { EventType.VEST.toString() }
            property(VestRequestEvent::date) { "2021-10-01" }
            property(VestRequestEvent::quantity) { validQuantity }
            property(VestRequestEvent::precision) { "5" }
        }

        expectThat(QuantityHandler.handle(vestRequest).unsafeRight())
            .isEqualTo(BigDecimal(validQuantity))
    }

    @ParameterizedTest
    @ValueSource(strings = ["-100", "invalid_quantity"])
    fun `should return an error message when parsing an invalid quantity`(invalidQuantity: String) {
        val vestRequest = fixture<VestRequestEvent>() {
            property(VestRequestEvent::eventType) { EventType.VEST.toString() }
            property(VestRequestEvent::date) { "2021-10-01" }
            property(VestRequestEvent::quantity) { invalidQuantity }
            property(VestRequestEvent::precision) { "5" }
        }

        expectThat(QuantityHandler.handle(vestRequest).unsafeLeft())
            .isEqualTo("$invalidQuantity is an invalid quantity.")
    }

}
