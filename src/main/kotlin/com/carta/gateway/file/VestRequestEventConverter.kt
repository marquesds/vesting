package com.carta.gateway.file

import com.carta.gateway.request.VestRequestEvent

class VestRequestEventConverter {

    companion object {
        fun fromListOfValues(events: List<List<String>>, precision: String): List<VestRequestEvent> {
            return events.mapNotNull {
                try {
                    VestRequestEvent(it[0], it[1], it[2], it[3], it[4], it[5], precision)
                } catch (e: Exception) {
                    null
                }
            }
        }
    }
}