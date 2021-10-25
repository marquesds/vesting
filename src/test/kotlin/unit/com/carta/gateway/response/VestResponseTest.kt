package unit.com.carta.gateway.response

import com.appmattus.kotlinfixture.kotlinFixture
import com.carta.entity.Vest
import com.carta.gateway.response.VestResponse
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@Tag("unit")
class VestResponseTest {

    private val fixture = kotlinFixture()

    @Test
    fun `should convert a vest object to a vest response`() {
        val vest = fixture<Vest>()

        val expectedVestResponse = VestResponse.fromVest(vest)
        expectThat(expectedVestResponse) {
            get { employeeId }.isEqualTo(vest.employeeId)
            get { employeeName }.isEqualTo(vest.employeeName)
            get { awardId }.isEqualTo(vest.awardId)
            get { quantity }.isEqualTo(vest.quantity)
        }
    }

}