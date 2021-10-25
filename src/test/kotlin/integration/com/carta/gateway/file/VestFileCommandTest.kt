package integration.com.carta.gateway.file

import com.carta.gateway.file.VestFileCommand
import com.carta.gateway.request.parser.FailFastVestRequestEventParser
import com.carta.gateway.response.VestResponse
import com.carta.infra.handler.file.VestRequestEventCSVFileHandler
import com.carta.usecase.ProcessVestUseCase
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.containsSequence
import strikt.assertions.hasSize
import java.math.BigDecimal

@Tag("integration")
class VestFileCommandTest {

    private val processVestUseCase = ProcessVestUseCase()
    private val vestRequestEventParser = FailFastVestRequestEventParser()
    private val fileHandler = VestRequestEventCSVFileHandler()

    @Test
    fun `should process a vest event request list`() {
        val filePath = "src/test/kotlin/resources/vest_entries.csv"
        val command = VestFileCommand(processVestUseCase, fileHandler, vestRequestEventParser)

        val expectedResults = listOf(
            VestResponse("E001", "Alice Smith", "ISO-001", BigDecimal(1000)),
            VestResponse("E001", "Alice Smith", "ISO-002", BigDecimal(800)),
            VestResponse("E002", "Bobby Jones", "NSO-001", BigDecimal(600)),
            VestResponse("E003", "Cat Helms", "NSO-002", BigDecimal.ZERO)
        )

        val results = command.execute(filePath, "2020-04-01", "0")

        expectThat(results)
            .hasSize(expectedResults.size)
            .containsSequence(expectedResults)
    }

}