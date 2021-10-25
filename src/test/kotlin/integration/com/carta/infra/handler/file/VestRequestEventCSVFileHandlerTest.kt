package integration.com.carta.infra.handler.file

import com.carta.infra.handler.file.VestRequestEventCSVFileHandler
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import strikt.api.expectCatching
import strikt.api.expectThat
import strikt.assertions.containsSequence
import strikt.assertions.hasSize
import strikt.assertions.isA
import strikt.assertions.isFailure
import java.io.FileNotFoundException

@Tag("integration")
class VestRequestEventCSVFileHandlerTest {

    @Test
    fun `should read a csv file and extract vest request events`() {
        val filePath = "src/test/kotlin/resources/vest_entries.csv"
        val fileHandler = VestRequestEventCSVFileHandler()

        val events = fileHandler.read(filePath)

        val expectedEvents = listOf(
            listOf("VEST", "E001", "Alice Smith", "ISO-001", "2020-01-01", "1000"),
            listOf("VEST", "E001", "Alice Smith", "ISO-001", "2021-01-01", "1000"),
            listOf("VEST", "E001", "Alice Smith", "ISO-002", "2020-03-01", "300"),
            listOf("VEST", "E001", "Alice Smith", "ISO-002", "2020-04-01", "500"),
            listOf("VEST", "E002", "Bobby Jones", "NSO-001", "2020-01-02", "100"),
            listOf("VEST", "E002", "Bobby Jones", "NSO-001", "2020-02-02", "200"),
            listOf("VEST", "E002", "Bobby Jones", "NSO-001", "2020-03-02", "300"),
            listOf("VEST", "E003", "Cat Helms", "NSO-002", "2024-01-01", "100")
        )

        expectThat(events)
            .hasSize(expectedEvents.size)
            .containsSequence(expectedEvents)
    }

    @Test
    fun `should raise an error when the file don't exists`() {
        val filePath = "src/test/kotlin/resources/invalid_file.csv"
        val fileHandler = VestRequestEventCSVFileHandler()

        expectCatching { fileHandler.read(filePath) }
            .isFailure()
            .isA<FileNotFoundException>()
    }

}