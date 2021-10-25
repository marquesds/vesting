package unit.com.carta.gateway.file

import com.carta.gateway.file.VestFileCommand
import com.carta.gateway.request.parser.FailFastVestRequestEventParser
import com.carta.gateway.response.VestResponse
import com.carta.infra.handler.file.InMemoryVestRequestEventFileHandler
import com.carta.usecase.ProcessVestUseCase
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.containsSequence
import strikt.assertions.hasSize
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

@Tag("unit")
class VestFileCommandTest {

    private val processVestUseCase = ProcessVestUseCase()
    private val vestRequestEventParser = FailFastVestRequestEventParser()
    private val fakeFilePath = "vests.csv"

    @Test
    fun `should process a vest event request list`() {
        val vestEvents = listOf(
            listOf("VEST", "E001", "Alice Smith", "ISO-001", "2020-01-01", "1000"),
            listOf("VEST", "E001", "Alice Smith", "ISO-001", "2021-01-01", "1000"),
            listOf("VEST", "E001", "Alice Smith", "ISO-002", "2020-03-01", "300"),
            listOf("VEST", "E001", "Alice Smith", "ISO-002", "2020-04-01", "500"),
            listOf("VEST", "E002", "Bobby Jones", "NSO-001", "2020-01-02", "100"),
            listOf("VEST", "E002", "Bobby Jones", "NSO-001", "2020-02-02", "200"),
            listOf("VEST", "E002", "Bobby Jones", "NSO-001", "2020-03-02", "300"),
            listOf("VEST", "E003", "Cat Helms", "NSO-002", "2024-01-01", "100")
        )

        val csvFileHandler = InMemoryVestRequestEventFileHandler(vestEvents)
        val command = VestFileCommand(processVestUseCase, csvFileHandler, vestRequestEventParser)

        val expectedResults = listOf(
            VestResponse("E001", "Alice Smith", "ISO-001", BigDecimal(1000)),
            VestResponse("E001", "Alice Smith", "ISO-002", BigDecimal(800)),
            VestResponse("E002", "Bobby Jones", "NSO-001", BigDecimal(600)),
            VestResponse("E003", "Cat Helms", "NSO-002", BigDecimal.ZERO)
        )

        val results = command.execute(fakeFilePath, "2020-04-01", "0")

        expectThat(results)
            .hasSize(expectedResults.size)
            .containsSequence(expectedResults)
    }

    @Test
    fun `should process a vest event request list with a cancel event`() {
        val vestEvents = listOf(
            listOf("VEST", "E001", "Alice Smith", "ISO-001", "2020-01-01", "1000"),
            listOf("CANCEL", "E001", "Alice Smith", "ISO-001", "2021-01-01", "700")
        )

        val csvFileHandler = InMemoryVestRequestEventFileHandler(vestEvents)
        val command = VestFileCommand(processVestUseCase, csvFileHandler, vestRequestEventParser)

        val expectedResults = listOf(
            VestResponse("E001", "Alice Smith", "ISO-001", BigDecimal(300))
        )

        val results = command.execute(fakeFilePath, "2021-01-01", "0")

        expectThat(results)
            .hasSize(expectedResults.size)
            .containsSequence(expectedResults)
    }

    @Test
    fun `should process a vest event request list specifying the precision`() {
        val vestEvents = listOf(
            listOf("VEST", "E001", "Alice Smith", "ISO-001", "2020-01-01", "1000.5"),
            listOf("CANCEL", "E001", "Alice Smith", "ISO-001", "2021-01-01", "700.75"),
            listOf("VEST", "E002", "Bobby Jones", "ISO-002", "2020-01-01", "234")
        )

        val csvFileHandler = InMemoryVestRequestEventFileHandler(vestEvents)
        val command = VestFileCommand(processVestUseCase, csvFileHandler, vestRequestEventParser)

        val roundPolicy = MathContext(1, RoundingMode.HALF_DOWN)
        val expectedResults = listOf(
            VestResponse("E001", "Alice Smith", "ISO-001", BigDecimal("299.8").round(roundPolicy)),
            VestResponse("E002", "Bobby Jones", "ISO-002", BigDecimal("234.0").round(roundPolicy))
        )

        val results = command.execute(fakeFilePath, "2021-01-01", "1")

        expectThat(results)
            .hasSize(expectedResults.size)
            .containsSequence(expectedResults)
    }

    @Test
    fun `should process a vest event request list ignoring the errors`() {
        val vestEvents = listOf(
            listOf("VEST", "E001", "Alice Smith", "ISO-001", "2020-01-01", "1000"),
            listOf("VEST", "E001", "Alice Smith", "ISO-001", "2021-01-01", "1000"),
            listOf("VEST", "E001", "Alice Smith", "ISO-001", "2021-01-01", "invalid_number"),
            listOf("VEST", "E001", "Alice Smith", "ISO-002", "2020-03-01", "300"),
            listOf("VEST", "E001", "Alice Smith", "ISO-002", "2020-04-01", "500"),
            listOf("VEST", "E001", "Alice Smith", "ISO-002", "invalid_date", "500"),
            listOf("VEST", "E002", "Bobby Jones", "NSO-001", "2020-01-02", "100"),
            listOf("VEST", "E002", "Bobby Jones", "NSO-001", "2020-02-02", "200"),
            listOf("VEST", "E002", "Bobby Jones", "NSO-001", "2020-03-02", "300"),
            listOf("invalid_event_type", "E002", "Bobby Jones", "NSO-001", "2020-03-02", "300"),
            listOf("VEST", "E003", "Cat Helms", "NSO-002", "2024-01-01", "100")
        )

        val csvFileHandler = InMemoryVestRequestEventFileHandler(vestEvents)
        val command = VestFileCommand(processVestUseCase, csvFileHandler, vestRequestEventParser)

        val expectedResults = listOf(
            VestResponse("E001", "Alice Smith", "ISO-001", BigDecimal(1000)),
            VestResponse("E001", "Alice Smith", "ISO-002", BigDecimal(800)),
            VestResponse("E002", "Bobby Jones", "NSO-001", BigDecimal(600)),
            VestResponse("E003", "Cat Helms", "NSO-002", BigDecimal.ZERO)
        )

        val results = command.execute(fakeFilePath, "2020-04-01", "0")

        expectThat(results)
            .hasSize(expectedResults.size)
            .containsSequence(expectedResults)
    }

}