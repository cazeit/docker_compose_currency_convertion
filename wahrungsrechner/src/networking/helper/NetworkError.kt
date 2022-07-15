/*
 * NetworkError
 *
 * Version 1.0
 *
 * Copyright (c) 2020 Fh Erfurt
 * by C. Zeitler and S. Golchert
 */

package de.kba.api.wahrungsrechner.networking.helper

class NetworkError {

    companion object {
        fun offlineError(): NetworkError {
            val error = NetworkError()
            error.status =
                    StatusCode.connectionFailure
            error.message = "Connection failed or aborted."
            return error
        }

        fun decodeError(): NetworkError {
            val error = NetworkError()
            error.status =
                    StatusCode.unexpectedPayload
            error.message = "Response could not be processed."
            return error
        }

        fun parameterError(): NetworkError {
            val error = NetworkError()
            error.status = StatusCode.invalidParameters
            error.message = "Invalid paramters passed!"
            return error
        }

        fun debugError(message: String = "Seems like a developer made a mistake!"): NetworkError {
            val error = NetworkError()
            error.status = StatusCode.debugError
            error.message = message
            return error
        }
    }

    class StatusCode {
        companion object {
            const val unexpectedPayload = 1333
            const val connectionFailure = 999
            const val debugError = 66
            const val invalidParameters = 1999
        }
    }

    var status: Int? = null
    var message: String? = null
}