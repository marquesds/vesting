package unit.com.carta.gateway.request.parser.field

import com.carta.entity.EventType
import com.carta.gateway.request.parser.field.EventTypeParser
import org.junit.jupiter.api.Tag
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNull

@Tag("unit")
class EventTypeParserTest {

    @ParameterizedTest
    @ValueSource(strings = ["VEST", "CANCEL"])
    fun `should parse a valid event type`(validEventType: String) {
        expectThat(EventTypeParser.parse(validEventType))
            .isEqualTo(EventType.valueOf(validEventType))
    }

    @ParameterizedTest
    @ValueSource(strings = ["-1", "invalid_event_type"])
    fun `should not parse a invalid event type`(invalidEventType: String) {
        expectThat(EventTypeParser.parse(invalidEventType)).isNull()
    }

}