package unit.com.carta.gateway.request.parser

import com.appmattus.kotlinfixture.kotlinFixture
import com.carta.entity.EventType
import com.carta.gateway.request.VestRequestEvent
import com.carta.gateway.request.parser.KeepErrorVestRequestEventParser
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.*
import java.math.BigDecimal
import java.time.LocalDate

@Tag("unit")
class KeepErrorVestRequestEventParserTest {

    private val fixture = kotlinFixture()

    @Test
    fun `should parse a vest event`() {
        val parser = KeepErrorVestRequestEventParser()

        val vestRequest = fixture<VestRequestEvent>() {
            property(VestRequestEvent::eventType) { EventType.VEST.toString() }
            property(VestRequestEvent::date) { "2021-10-01" }
            property(VestRequestEvent::quantity) { "100" }
            property(VestRequestEvent::precision) { "5" }
        }

        val vest = parser.parse(vestRequest)

        expectThat(parser.errors).isEmpty()
        expectThat(vest) {
            get { this?.eventType }.isEqualTo(EventType.VEST)
            get { this?.date }.isEqualTo(LocalDate.of(2021, 10, 1))
            get { this?.quantity }.isEqualTo(BigDecimal("100"))
            get { this?.precision }.isEqualTo(5)
        }
    }

    @Test
    fun `should show errors of a half valid vest event`() {
        val parser = KeepErrorVestRequestEventParser()

        val vestRequest = fixture<VestRequestEvent>() {
            property(VestRequestEvent::eventType) { "asdf" }
            property(VestRequestEvent::date) { "2021-10-01" }
            property(VestRequestEvent::quantity) { "100" }
            property(VestRequestEvent::precision) { "-15" }
        }

        val vest = parser.parse(vestRequest)
        expectThat(vest).isNull()
        expectThat(parser.errors)
            .hasSize(2)
            .contains("asdf is an invalid EventType.", "-15 is an invalid precision.")
    }

    @Test
    fun `should show errors of a total invalid vest event`() {
        val parser = KeepErrorVestRequestEventParser()

        val vestRequest = fixture<VestRequestEvent>() {
            property(VestRequestEvent::eventType) { "asdf" }
            property(VestRequestEvent::date) { "2021-13-01" }
            property(VestRequestEvent::quantity) { "-200" }
            property(VestRequestEvent::precision) { "-15" }
        }

        val vest = parser.parse(vestRequest)
        expectThat(vest).isNull()
        expectThat(parser.errors)
            .hasSize(4)
            .contains(
                "asdf is an invalid EventType.",
                "2021-13-01 is an invalid date.",
                "-200 is an invalid quantity.",
                "-15 is an invalid precision."
            )
    }
}