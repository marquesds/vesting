package com.carta.gateway.file

import com.carta.gateway.response.VestResponse

interface IVestFileCommand {

    fun execute(filePath: String, targetDate: String, precision: String): List<VestResponse>

}