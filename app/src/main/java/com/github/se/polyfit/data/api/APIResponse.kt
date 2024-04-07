package com.github.se.polyfit.data.api


enum class APIReponse {
    SUCCESS,
    FAILURE;

    companion object {
        fun fromString(status: String): APIReponse {
            return when (status.lowercase()) {
                "success" -> SUCCESS
                "failure" -> FAILURE
                else -> throw IllegalArgumentException("Invalid status: $status")
            }
        }
    }
}