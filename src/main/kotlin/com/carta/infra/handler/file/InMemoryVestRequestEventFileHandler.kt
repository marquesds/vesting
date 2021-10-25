package com.carta.infra.handler.file

class InMemoryVestRequestEventFileHandler(private val values: List<List<String>>) : IFileHandler {
    override fun read(filePath: String): List<List<String>> = values
}