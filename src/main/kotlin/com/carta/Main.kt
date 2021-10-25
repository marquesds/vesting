package com.carta

import com.carta.gateway.file.VestFileCommand
import com.carta.gateway.request.parser.FailFastVestRequestEventParser
import com.carta.infra.handler.file.VestRequestEventCSVFileHandler
import com.carta.usecase.ProcessVestUseCase

fun main(args: Array<String>) {

    val processVestUseCase = ProcessVestUseCase()
    val csvFileHandler = VestRequestEventCSVFileHandler()
    val vestRequestEventParser = FailFastVestRequestEventParser()

    val vestFileCommand = VestFileCommand(processVestUseCase, csvFileHandler, vestRequestEventParser)

    when (args.size) {
        3 -> vestFileCommand.execute(args[0], args[1], args[2]).map { println(it.toString()) }
        2 -> vestFileCommand.execute(args[0], args[1], "0").map { println(it.toString()) }
        else -> println("The min number of arguments is 2 and the max is 3. Provided: ${args.size}")
    }
}