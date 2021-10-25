package com.carta

import com.carta.gateway.file.VestFileFileCommand
import com.carta.gateway.request.parser.FailFastVestRequestEventParser
import com.carta.infra.handler.file.VestRequestEventCSVFileHandler
import com.carta.usecase.ProcessVestUseCase

fun main(args: Array<String>) {

    val processVestUseCase = ProcessVestUseCase()
    val csvFileHandler = VestRequestEventCSVFileHandler()
    val vestRequestEventParser = FailFastVestRequestEventParser()

    val vestFileCommand = VestFileFileCommand(processVestUseCase, csvFileHandler, vestRequestEventParser)

    when (args.size) {
        3 -> vestFileCommand.execute(args[0], args[1], args[2]).map { println(it.toString()) }
        2 -> vestFileCommand.execute(args[0], "2020-04-01", "0").map { println(it.toString()) }
        else -> vestFileCommand.execute("/home/lucas/Projects/vesting/carta.csv", "2020-04-01", "0")
            .map { println(it.toString()) }
    }
}