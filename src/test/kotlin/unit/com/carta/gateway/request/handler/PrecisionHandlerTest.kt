package unit.com.carta.gateway.request.handler

import com.appmattus.kotlinfixture.kotlinFixture
import com.carta.gateway.request.VestRequestEvent
import com.carta.gateway.request.handler.PrecisionHandler
import com.carta.shared.type.unsafeLeft
import com.carta.shared.type.unsafeRight
import org.junit.jupiter.api.Tag
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@Tag("unit")
class PrecisionHandlerTest {

    private val fixture = kotlinFixture()

    @ParameterizedTest
    @ValueSource(strings = ["0", "1", "2", "3", "4", "5", "6"])
    fun `should parse a valid precision`(validPrecision: String) {
        val vestRequest = fixture<VestRequestEvent>() {
            property(VestRequestEvent::eventType) { "VEST" }
            property(VestRequestEvent::date) { "2021-10-01" }
            property(VestRequestEvent::quantity) { "100" }
            property(VestRequestEvent::precision) { validPrecision }
        }

        expectThat(PrecisionHandler.handle(vestRequest).unsafeRight())
            .isEqualTo(validPrecision.toInt())
    }

    @ParameterizedTest
    @ValueSource(strings = ["-1", "7", "invalid_precision"])
    fun `should return an error message when parsing an invalid precision`(invalidPrecision: String) {
        val vestRequest = fixture<VestRequestEvent>() {
            property(VestRequestEvent::eventType) { "VEST" }
            property(VestRequestEvent::date) { "2021-10-01" }
            property(VestRequestEvent::quantity) { "100" }
            property(VestRequestEvent::precision) { invalidPrecision }
        }

        expectThat(PrecisionHandler.handle(vestRequest).unsafeLeft())
            .isEqualTo("$invalidPrecision is an invalid precision.")
    }

}