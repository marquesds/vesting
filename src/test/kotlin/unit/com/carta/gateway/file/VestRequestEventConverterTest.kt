package unit.com.carta.gateway.file

import com.carta.gateway.file.VestRequestEventConverter
import com.carta.gateway.request.VestRequestEvent
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.containsSequence
import strikt.assertions.hasSize
import strikt.assertions.isEmpty

@Tag("unit")
class VestRequestEventConverterTest {

    @Test
    fun `should convert a list of strings into a list of vest response`() {
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

        val expectedVestEvents = listOf(
            VestRequestEvent("VEST", "E001", "Alice Smith", "ISO-001", "2020-01-01", "1000"),
            VestRequestEvent("VEST", "E001", "Alice Smith", "ISO-001", "2021-01-01", "1000"),
            VestRequestEvent("VEST", "E001", "Alice Smith", "ISO-002", "2020-03-01", "300"),
            VestRequestEvent("VEST", "E001", "Alice Smith", "ISO-002", "2020-04-01", "500"),
            VestRequestEvent("VEST", "E002", "Bobby Jones", "NSO-001", "2020-01-02", "100"),
            VestRequestEvent("VEST", "E002", "Bobby Jones", "NSO-001", "2020-02-02", "200"),
            VestRequestEvent("VEST", "E002", "Bobby Jones", "NSO-001", "2020-03-02", "300"),
            VestRequestEvent("VEST", "E003", "Cat Helms", "NSO-002", "2024-01-01", "100")
        )

        val results = VestRequestEventConverter.fromListOfValues(vestEvents, "0")

        expectThat(results)
            .hasSize(expectedVestEvents.size)
            .containsSequence(expectedVestEvents)
    }

    @Test
    fun `should not convert a list of strings into a list of vest response when there is missing values`() {
        val vestEvents = listOf(
            listOf("VEST", "E001", "Alice Smith", "ISO-001", "2020-01-01"),
            listOf("VEST", "E001", "Alice Smith", "ISO-001"),
            listOf("E001", "Alice Smith", "ISO-002")
        )

        val results = VestRequestEventConverter.fromListOfValues(vestEvents, "0")
        expectThat(results).isEmpty()
    }

}