package com.carta.infra.handler.file

interface IFileHandler {

    fun read(filePath: String): List<List<String>>

}