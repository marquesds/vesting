package com.carta.gateway.file

import com.carta.gateway.request.parser.IVestRequestEventParser
import com.carta.gateway.response.VestResponse
import com.carta.infra.handler.file.IFileHandler
import com.carta.usecase.IProcessVestUseCase
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class VestFileCommand(
    private val processVestUseCase: IProcessVestUseCase,
    private val fileHandler: IFileHandler,
    private val parser: IVestRequestEventParser
) : IVestFileCommand {

    override fun execute(filePath: String, targetDate: String, precision: String): List<VestResponse> {
        val vests = VestRequestEventConverter.fromListOfValues(fileHandler.read(filePath), precision)
            .mapNotNull { it.toVest(parser) }
        return processVestUseCase.process(vests, LocalDate.parse(targetDate, DateTimeFormatter.ISO_DATE))
            .map { VestResponse.fromVest(it) }
    }
}