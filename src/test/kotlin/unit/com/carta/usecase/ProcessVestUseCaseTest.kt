package unit.com.carta.usecase

import com.appmattus.kotlinfixture.kotlinFixture
import com.carta.entity.EventType
import com.carta.entity.Vest
import com.carta.usecase.ProcessVestUseCase
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import strikt.api.expectThat
import strikt.assertions.hasSize
import strikt.assertions.isEqualTo
import java.math.BigDecimal
import java.time.LocalDate

@Tag("unit")
class ProcessVestUseCaseTest {

    private val fixture = kotlinFixture()
    private val useCase = ProcessVestUseCase()

    private val defaultVestDate = LocalDate.of(2021, 10, 1)
    private val defaultTargetDate = LocalDate.of(2021, 10, 2)

    @ParameterizedTest
    @ValueSource(strings = ["10", "20", "30", "15.5"])
    fun `should sum all vest entries for an employee`(quantity: String) {
        val sequenceSize = 20
        val vests = generateSequence {
            fixture<Vest>() {
                property(Vest::eventType) { EventType.VEST }
                property(Vest::employeeId) { "E001" }
                property(Vest::awardId) { "A001" }
                property(Vest::date) { defaultVestDate }
                property(Vest::quantity) { BigDecimal(quantity) }
            }
        }.take(sequenceSize).toList()

        val expectedVests = useCase.process(vests, defaultTargetDate)
        val expectedQuantity = BigDecimal(sequenceSize) * BigDecimal(quantity)

        expectThat(expectedVests).hasSize(1)
        expectThat(expectedVests[0].quantity).isEqualTo(expectedQuantity)
    }

    @ParameterizedTest
    @ValueSource(strings = ["10", "20", "30", "15.5"])
    fun `should sum all vest entries for an employee only for a specific target date`(quantity: String) {
        val sequenceSize = 20
        val vests1 = generateSequence {
            fixture<Vest>() {
                property(Vest::eventType) { EventType.VEST }
                property(Vest::employeeId) { "E001" }
                property(Vest::awardId) { "A001" }
                property(Vest::date) { defaultVestDate }
                property(Vest::quantity) { BigDecimal(quantity) }
            }
        }.take(sequenceSize).toList()

        val futureVestDate = LocalDate.of(2025, 10, 1)
        val vests2 = generateSequence {
            fixture<Vest>() {
                property(Vest::eventType) { EventType.VEST }
                property(Vest::employeeId) { "E001" }
                property(Vest::awardId) { "A001" }
                property(Vest::date) { futureVestDate }
                property(Vest::quantity) { BigDecimal(quantity) }
            }
        }.take(sequenceSize).toList()

        val vests = vests1 + vests2
        val expectedVests = useCase.process(vests, defaultTargetDate)
        val expectedQuantity = BigDecimal(sequenceSize) * BigDecimal(quantity)

        expectThat(expectedVests).hasSize(1)
        expectThat(expectedVests[0].quantity).isEqualTo(expectedQuantity)
    }

    @Test
    fun `should sum all vest entries for each employee`() {
        val employeeVests1 = generateSequence {
            fixture<Vest>() {
                property(Vest::eventType) { EventType.VEST }
                property(Vest::employeeId) { "E001" }
                property(Vest::awardId) { "A001" }
                property(Vest::date) { defaultVestDate }
                property(Vest::quantity) { BigDecimal("10") }
            }
        }.take(10).toList()

        val employeeVests2 = generateSequence {
            fixture<Vest>() {
                property(Vest::eventType) { EventType.VEST }
                property(Vest::employeeId) { "E002" }
                property(Vest::awardId) { "A001" }
                property(Vest::date) { defaultVestDate }
                property(Vest::quantity) { BigDecimal("20") }
            }
        }.take(20).toList()

        val employeeVests3 = generateSequence {
            fixture<Vest>() {
                property(Vest::eventType) { EventType.VEST }
                property(Vest::employeeId) { "E003" }
                property(Vest::awardId) { "A001" }
                property(Vest::date) { defaultVestDate }
                property(Vest::quantity) { BigDecimal("30") }
            }
        }.take(30).toList()

        val vests = employeeVests1 + employeeVests2 + employeeVests3
        val expectedVests = useCase.process(vests, defaultTargetDate)

        expectThat(expectedVests).hasSize(3)
        expectThat(expectedVests[0]) {
            get { employeeId }.isEqualTo("E001")
            get { quantity }.isEqualTo(BigDecimal("100"))
        }
        expectThat(expectedVests[1]) {
            get { employeeId }.isEqualTo("E002")
            get { quantity }.isEqualTo(BigDecimal("400"))
        }
        expectThat(expectedVests[2]) {
            get { employeeId }.isEqualTo("E003")
            get { quantity }.isEqualTo(BigDecimal("900"))
        }
    }

    @Test
    fun `should sum all vest entries for each employee, even for those with date greater then the target date`() {
        val employeeVests1 = generateSequence {
            fixture<Vest>() {
                property(Vest::eventType) { EventType.VEST }
                property(Vest::employeeId) { "E001" }
                property(Vest::awardId) { "A001" }
                property(Vest::date) { defaultVestDate }
                property(Vest::quantity) { BigDecimal("10") }
            }
        }.take(10).toList()

        val employeeVests2 = generateSequence {
            fixture<Vest>() {
                property(Vest::eventType) { EventType.VEST }
                property(Vest::employeeId) { "E002" }
                property(Vest::awardId) { "A001" }
                property(Vest::date) { defaultVestDate }
                property(Vest::quantity) { BigDecimal("20") }
            }
        }.take(20).toList()

        val employeeVests3 = generateSequence {
            fixture<Vest>() {
                property(Vest::eventType) { EventType.VEST }
                property(Vest::employeeId) { "E003" }
                property(Vest::awardId) { "A001" }
                property(Vest::date) { defaultVestDate }
                property(Vest::quantity) { BigDecimal("30") }
            }
        }.take(30).toList()

        val employeeVests4 = generateSequence {
            fixture<Vest>() {
                property(Vest::eventType) { EventType.VEST }
                property(Vest::employeeId) { "E004" }
                property(Vest::awardId) { "A001" }
                property(Vest::date) { LocalDate.now() }
                property(Vest::quantity) { BigDecimal("100") }
            }
        }.take(15).toList()

        val vests = employeeVests1 + employeeVests2 + employeeVests3 + employeeVests4
        val expectedVests = useCase.process(vests, defaultTargetDate)

        expectThat(expectedVests).hasSize(4)
        expectThat(expectedVests[0]) {
            get { employeeId }.isEqualTo("E001")
            get { quantity }.isEqualTo(BigDecimal("100"))
        }
        expectThat(expectedVests[1]) {
            get { employeeId }.isEqualTo("E002")
            get { quantity }.isEqualTo(BigDecimal("400"))
        }
        expectThat(expectedVests[2]) {
            get { employeeId }.isEqualTo("E003")
            get { quantity }.isEqualTo(BigDecimal("900"))
        }
        expectThat(expectedVests[3]) {
            get { employeeId }.isEqualTo("E004")
            get { quantity }.isEqualTo(BigDecimal("0"))
        }
    }

    @Test
    fun `should sum all vest entries for each employee, even for the only one with date greater then the target date`() {
        val employeeVests1 = generateSequence {
            fixture<Vest>() {
                property(Vest::eventType) { EventType.VEST }
                property(Vest::employeeId) { "E001" }
                property(Vest::awardId) { "A001" }
                property(Vest::date) { defaultVestDate }
                property(Vest::quantity) { BigDecimal("10") }
            }
        }.take(10).toList()

        val employeeVests2 = generateSequence {
            fixture<Vest>() {
                property(Vest::eventType) { EventType.VEST }
                property(Vest::employeeId) { "E002" }
                property(Vest::awardId) { "A001" }
                property(Vest::date) { defaultVestDate }
                property(Vest::quantity) { BigDecimal("20") }
            }
        }.take(20).toList()

        val employeeVests3 = generateSequence {
            fixture<Vest>() {
                property(Vest::eventType) { EventType.VEST }
                property(Vest::employeeId) { "E003" }
                property(Vest::awardId) { "A001" }
                property(Vest::date) { defaultVestDate }
                property(Vest::quantity) { BigDecimal("30") }
            }
        }.take(30).toList()

        val employeeVests4 = generateSequence {
            fixture<Vest>() {
                property(Vest::eventType) { EventType.VEST }
                property(Vest::employeeId) { "E004" }
                property(Vest::awardId) { "A001" }
                property(Vest::date) { LocalDate.now() }
                property(Vest::quantity) { BigDecimal("100") }
            }
        }.take(1).toList()

        val vests = employeeVests1 + employeeVests2 + employeeVests3 + employeeVests4
        val expectedVests = useCase.process(vests, defaultTargetDate)

        expectThat(expectedVests).hasSize(4)
        expectThat(expectedVests[0]) {
            get { employeeId }.isEqualTo("E001")
            get { quantity }.isEqualTo(BigDecimal("100"))
        }
        expectThat(expectedVests[1]) {
            get { employeeId }.isEqualTo("E002")
            get { quantity }.isEqualTo(BigDecimal("400"))
        }
        expectThat(expectedVests[2]) {
            get { employeeId }.isEqualTo("E003")
            get { quantity }.isEqualTo(BigDecimal("900"))
        }
        expectThat(expectedVests[3]) {
            get { employeeId }.isEqualTo("E004")
            get { quantity }.isEqualTo(BigDecimal("0"))
        }
    }

    @Test
    fun `should sum all vest entries for an employee's each award`() {
        val awardVests1 = generateSequence {
            fixture<Vest>() {
                property(Vest::eventType) { EventType.VEST }
                property(Vest::employeeId) { "E001" }
                property(Vest::awardId) { "A001" }
                property(Vest::date) { defaultVestDate }
                property(Vest::quantity) { BigDecimal("10") }
            }
        }.take(10).toList()

        val awardVests2 = generateSequence {
            fixture<Vest>() {
                property(Vest::eventType) { EventType.VEST }
                property(Vest::employeeId) { "E001" }
                property(Vest::awardId) { "A002" }
                property(Vest::date) { defaultVestDate }
                property(Vest::quantity) { BigDecimal("20") }
            }
        }.take(20).toList()

        val awardVests3 = generateSequence {
            fixture<Vest>() {
                property(Vest::eventType) { EventType.VEST }
                property(Vest::employeeId) { "E001" }
                property(Vest::awardId) { "A003" }
                property(Vest::date) { defaultVestDate }
                property(Vest::quantity) { BigDecimal("30") }
            }
        }.take(30).toList()

        val vests = awardVests1 + awardVests2 + awardVests3
        val expectedVests = useCase.process(vests, defaultTargetDate)

        expectThat(expectedVests).hasSize(3)
        expectThat(expectedVests[0]) {
            get { awardId }.isEqualTo("A001")
            get { quantity }.isEqualTo(BigDecimal("100"))
        }
        expectThat(expectedVests[1]) {
            get { awardId }.isEqualTo("A002")
            get { quantity }.isEqualTo(BigDecimal("400"))
        }
        expectThat(expectedVests[2]) {
            get { awardId }.isEqualTo("A003")
            get { quantity }.isEqualTo(BigDecimal("900"))
        }
    }

    @Test
    fun `should sum vest entries and keep employeeId and awardId order`() {
        val awardVests1 = generateSequence {
            fixture<Vest>() {
                property(Vest::eventType) { EventType.VEST }
                property(Vest::employeeId) { "E003" }
                property(Vest::awardId) { "A003" }
                property(Vest::date) { defaultVestDate }
                property(Vest::quantity) { BigDecimal("30") }
            }
        }.take(30).toList()

        val awardVests2 = generateSequence {
            fixture<Vest>() {
                property(Vest::eventType) { EventType.VEST }
                property(Vest::employeeId) { "E002" }
                property(Vest::awardId) { "A002" }
                property(Vest::date) { defaultVestDate }
                property(Vest::quantity) { BigDecimal("20") }
            }
        }.take(20).toList()

        val awardVests3 = generateSequence {
            fixture<Vest>() {
                property(Vest::eventType) { EventType.VEST }
                property(Vest::employeeId) { "E001" }
                property(Vest::awardId) { "A001" }
                property(Vest::date) { defaultVestDate }
                property(Vest::quantity) { BigDecimal("10") }
            }
        }.take(10).toList()

        val awardVests4 = generateSequence {
            fixture<Vest>() {
                property(Vest::eventType) { EventType.VEST }
                property(Vest::employeeId) { "E001" }
                property(Vest::awardId) { "A002" }
                property(Vest::date) { defaultVestDate }
                property(Vest::quantity) { BigDecimal("10") }
            }
        }.take(10).toList()

        val vests = awardVests1 + awardVests2 + awardVests3 + awardVests4
        val expectedVests = useCase.process(vests, defaultTargetDate)

        expectThat(expectedVests).hasSize(4)
        expectThat(expectedVests[0]) {
            get { employeeId }.isEqualTo("E001")
            get { awardId }.isEqualTo("A001")
            get { quantity }.isEqualTo(BigDecimal("100"))
        }
        expectThat(expectedVests[1]) {
            get { employeeId }.isEqualTo("E001")
            get { awardId }.isEqualTo("A002")
            get { quantity }.isEqualTo(BigDecimal("100"))
        }
        expectThat(expectedVests[2]) {
            get { employeeId }.isEqualTo("E002")
            get { awardId }.isEqualTo("A002")
            get { quantity }.isEqualTo(BigDecimal("400"))
        }
        expectThat(expectedVests[3]) {
            get { employeeId }.isEqualTo("E003")
            get { awardId }.isEqualTo("A003")
            get { quantity }.isEqualTo(BigDecimal("900"))
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["50", "200", "30", "18.75"])
    fun `should cancel a quantity of awards for an employee`(quantity: String) {
        val sequenceSize = 20
        val vests1 = generateSequence {
            fixture<Vest>() {
                property(Vest::eventType) { EventType.VEST }
                property(Vest::employeeId) { "E001" }
                property(Vest::awardId) { "A001" }
                property(Vest::date) { defaultVestDate }
                property(Vest::quantity) { BigDecimal("10") }
            }
        }.take(sequenceSize).toList()

        val vests2 = generateSequence {
            fixture<Vest>() {
                property(Vest::eventType) { EventType.CANCEL }
                property(Vest::employeeId) { "E001" }
                property(Vest::awardId) { "A001" }
                property(Vest::date) { defaultVestDate }
                property(Vest::quantity) { BigDecimal(quantity) }
            }
        }.take(1).toList()

        val vests = vests1 + vests2
        val expectedVests = useCase.process(vests, defaultTargetDate)
        val expectedQuantity = BigDecimal("200") - BigDecimal(quantity)

        expectThat(expectedVests).hasSize(1)
        expectThat(expectedVests[0].quantity).isEqualTo(expectedQuantity)
    }
}