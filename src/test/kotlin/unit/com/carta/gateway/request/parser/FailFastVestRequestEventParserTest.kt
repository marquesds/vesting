package unit.com.carta.gateway.request.parser

import com.appmattus.kotlinfixture.kotlinFixture
import com.carta.entity.EventType
import com.carta.gateway.request.VestRequestEvent
import com.carta.gateway.request.parser.FailFastVestRequestEventParser
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNull
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.time.LocalDate
import kotlin.math.PI

@Tag("unit")
class FailFastVestRequestEventParserTest {

    private val fixture = kotlinFixture()
    private val parser = FailFastVestRequestEventParser()

    @Test
    fun `should parse a vest request event`() {
        val vestRequest = fixture<VestRequestEvent>() {
            property(VestRequestEvent::eventType) { EventType.VEST.toString() }
            property(VestRequestEvent::date) { "2021-10-01" }
            property(VestRequestEvent::quantity) { "100" }
            property(VestRequestEvent::precision) { "5" }
        }

        val vest = parser.parse(vestRequest)

        expectThat(vest) {
            get { this?.eventType }.isEqualTo(EventType.VEST)
            get { this?.date }.isEqualTo(LocalDate.of(2021, 10, 1))
            get { this?.quantity }.isEqualTo(BigDecimal("100"))
            get { this?.precision }.isEqualTo(5)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["0", "1", "2", "3", "4", "5", "6"])
    fun `should parse a vest request event applying precision`(precision: String) {
        val vestRequest = fixture<VestRequestEvent>() {
            property(VestRequestEvent::eventType) { EventType.VEST.toString() }
            property(VestRequestEvent::date) { "2021-10-01" }
            property(VestRequestEvent::quantity) { PI.toString() }
            property(VestRequestEvent::precision) { precision }
        }

        val roundPolicy = MathContext(precision.toInt(), RoundingMode.HALF_DOWN)
        val vest = parser.parse(vestRequest)

        expectThat(vest) {
            get { this?.eventType }.isEqualTo(EventType.VEST)
            get { this?.date }.isEqualTo(LocalDate.of(2021, 10, 1))
            get { this?.quantity }.isEqualTo(BigDecimal(PI.toString()).round(roundPolicy))
            get { this?.precision }.isEqualTo(precision.toInt())
        }
    }

    @Test
    fun `should not parse a vest request event when receiving an invalid event type`() {
        val vestRequest = fixture<VestRequestEvent>() {
            property(VestRequestEvent::eventType) { "invalid_event_type" }
            property(VestRequestEvent::date) { "2021-10-01" }
            property(VestRequestEvent::quantity) { "100" }
            property(VestRequestEvent::precision) { "5" }
        }

        val vest = parser.parse(vestRequest)
        expectThat(vest).isNull()
    }

    @Test
    fun `should not parse a vest request event when receiving an invalid date`() {
        val vestRequest = fixture<VestRequestEvent>() {
            property(VestRequestEvent::eventType) { EventType.VEST.toString() }
            property(VestRequestEvent::date) { "invalid_date" }
            property(VestRequestEvent::quantity) { "100" }
            property(VestRequestEvent::precision) { "5" }
        }

        val vest = parser.parse(vestRequest)
        expectThat(vest).isNull()
    }

    @ParameterizedTest
    @ValueSource(strings = ["-100", "invalid_quantity"])
    fun `should not parse a vest request event when receiving an invalid quantity`(quantity: String) {
        val vestRequest = fixture<VestRequestEvent>() {
            property(VestRequestEvent::eventType) { EventType.VEST.toString() }
            property(VestRequestEvent::date) { "2021-10-01" }
            property(VestRequestEvent::quantity) { quantity }
            property(VestRequestEvent::precision) { "5" }
        }

        val vest = parser.parse(vestRequest)
        expectThat(vest).isNull()
    }

    @ParameterizedTest
    @ValueSource(strings = ["7", "invalid_precision", "-1"])
    fun `should not parse a vest request event when receiving an invalid precision`(precision: String) {
        val vestRequest = fixture<VestRequestEvent>() {
            property(VestRequestEvent::eventType) { EventType.VEST.toString() }
            property(VestRequestEvent::date) { "2021-10-01" }
            property(VestRequestEvent::quantity) { "100" }
            property(VestRequestEvent::precision) { precision }
        }

        val vest = parser.parse(vestRequest)
        expectThat(vest).isNull()
    }

}