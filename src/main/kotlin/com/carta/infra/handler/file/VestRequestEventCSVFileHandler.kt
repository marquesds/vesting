package com.carta.infra.handler.file

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import java.io.BufferedInputStream
import java.io.FileInputStream

class VestRequestEventCSVFileHandler : IFileHandler {

    private val bufferSize = 2048

    override fun read(filePath: String): List<List<String>> {
        val buffer = BufferedInputStream(FileInputStream(filePath), bufferSize)
        return csvReader().readAll(buffer)
    }
}
