package unit.com.carta.gateway.request

import com.appmattus.kotlinfixture.kotlinFixture
import com.carta.entity.EventType
import com.carta.gateway.request.VestRequestEvent
import com.carta.gateway.request.parser.FailFastVestRequestEventParser
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNull
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Tag("unit")
class VestRequestEventTest {

    private val fixture = kotlinFixture()
    private val parser = FailFastVestRequestEventParser()

    @Test
    fun `should convert a vest request to a vest object`() {
        val vestRequest = fixture<VestRequestEvent>() {
            property(VestRequestEvent::eventType) { EventType.VEST.toString() }
            property(VestRequestEvent::date) { "2021-10-01" }
            property(VestRequestEvent::quantity) { "100" }
            property(VestRequestEvent::precision) { "5" }
        }
        val expectedVest = vestRequest.toVest(parser)

        expectThat(expectedVest) {
            get { this?.eventType }.isEqualTo(EventType.valueOf(vestRequest.eventType))
            get { this?.employeeId }.isEqualTo(vestRequest.employeeId)
            get { this?.employeeName }.isEqualTo(vestRequest.employeeName)
            get { this?.awardId }.isEqualTo(vestRequest.awardId)
            get { this?.date }.isEqualTo(LocalDate.parse(vestRequest.date, DateTimeFormatter.ISO_DATE))
            get { this?.quantity }
                .isEqualTo(
                    BigDecimal(vestRequest.quantity).round(
                        vestRequest.precision?.let { MathContext(it.toInt(), RoundingMode.HALF_DOWN) }
                    )
                )
            get { this?.precision }.isEqualTo(vestRequest.precision?.toInt())
        }
    }

    @Test
    fun `should not convert a vest request to a vest object when the values are wrong`() {
        val vestRequest = fixture<VestRequestEvent>() {
            property(VestRequestEvent::date) { "invalid_date" }
        }
        val expectedVest = vestRequest.toVest(parser)

        expectThat(expectedVest).isNull()
    }
}