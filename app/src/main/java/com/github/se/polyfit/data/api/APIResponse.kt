package com.github.se.polyfit.data.api

enum class APIResponse {
  SUCCESS,
  FAILURE;

  companion object {
    fun fromString(status: String): APIResponse? {
      return when (status.lowercase()) {
        "success" -> SUCCESS
        "failure" -> FAILURE
        else -> null
      }
    }
  }
}
