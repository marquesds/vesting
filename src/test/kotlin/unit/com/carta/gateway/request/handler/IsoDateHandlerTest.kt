package unit.com.carta.gateway.request.handler

import com.appmattus.kotlinfixture.kotlinFixture
import com.carta.entity.EventType
import com.carta.gateway.request.VestRequestEvent
import com.carta.gateway.request.handler.IsoDateHandler
import com.carta.shared.extension.StringExtension.toLocalDate
import com.carta.shared.type.unsafeLeft
import com.carta.shared.type.unsafeRight
import org.junit.jupiter.api.Tag
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@Tag("unit")
class IsoDateHandlerTest {

    private val fixture = kotlinFixture()

    @ParameterizedTest
    @ValueSource(strings = ["2021-10-01", "1970-01-01", "1991-01-05"])
    fun `should return a valid iso date`(validIsoDate: String) {
        val vestRequest = fixture<VestRequestEvent>() {
            property(VestRequestEvent::eventType) { EventType.VEST.toString() }
            property(VestRequestEvent::date) { validIsoDate }
            property(VestRequestEvent::quantity) { "100" }
            property(VestRequestEvent::precision) { "5" }
        }

        expectThat(IsoDateHandler.handle(vestRequest).unsafeRight())
            .isEqualTo(validIsoDate.toLocalDate())
    }

    @ParameterizedTest
    @ValueSource(strings = ["2021-13-01", "invalid", "1991-01-50"])
    fun `should return error message when parsing an invalid iso date`(invalidIsoDate: String) {
        val vestRequest = fixture<VestRequestEvent>() {
            property(VestRequestEvent::eventType) { EventType.VEST.toString() }
            property(VestRequestEvent::date) { invalidIsoDate }
            property(VestRequestEvent::quantity) { "100" }
            property(VestRequestEvent::precision) { "5" }
        }

        expectThat(IsoDateHandler.handle(vestRequest).unsafeLeft())
            .isEqualTo("$invalidIsoDate is an invalid date.")
    }

}