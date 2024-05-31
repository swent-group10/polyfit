package com.github.se.polyfit.data.api

/** Enum class that represents the status of an API call. */
enum class APIResponse {
  SUCCESS,
  FAILURE;

  companion object {
    /**
     * Convert a string to an APIResponse object.
     *
     * @param status The string to convert
     * @return The APIResponse object
     * @throws IllegalArgumentException If the string is not "success" or "failure"
     */
    fun fromString(status: String): APIResponse {
      return when (status.lowercase()) {
        "success" -> SUCCESS
        "failure" -> FAILURE
        else -> throw IllegalArgumentException("Invalid status: $status")
      }
    }
  }
}
