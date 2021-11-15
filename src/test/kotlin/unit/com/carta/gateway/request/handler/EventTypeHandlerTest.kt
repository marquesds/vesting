package unit.com.carta.gateway.request.handler

import com.appmattus.kotlinfixture.kotlinFixture
import com.carta.entity.EventType
import com.carta.gateway.request.VestRequestEvent
import com.carta.gateway.request.handler.EventTypeHandler
import com.carta.shared.type.unsafeLeft
import com.carta.shared.type.unsafeRight
import org.junit.jupiter.api.Tag
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@Tag("unit")
class EventTypeHandlerTest {

    private val fixture = kotlinFixture()

    @ParameterizedTest
    @ValueSource(strings = ["VEST", "CANCEL"])
    fun `should parse a valid event type`(validEventType: String) {
        val vestRequest = fixture<VestRequestEvent>() {
            property(VestRequestEvent::eventType) { validEventType }
            property(VestRequestEvent::date) { "2021-10-01" }
            property(VestRequestEvent::quantity) { "100" }
            property(VestRequestEvent::precision) { "5" }
        }

        expectThat(EventTypeHandler.handle(vestRequest).unsafeRight())
            .isEqualTo(EventType.valueOf(validEventType))
    }

    @ParameterizedTest
    @ValueSource(strings = ["-1", "invalid_event_type"])
    fun `should return an error message when parsing an invalid event type`(invalidEventType: String) {
        val vestRequest = fixture<VestRequestEvent>() {
            property(VestRequestEvent::eventType) { invalidEventType }
            property(VestRequestEvent::date) { "2021-10-01" }
            property(VestRequestEvent::quantity) { "100" }
            property(VestRequestEvent::precision) { "5" }
        }

        expectThat(EventTypeHandler.handle(vestRequest).unsafeLeft())
            .isEqualTo("$invalidEventType is an invalid EventType.")
    }
}