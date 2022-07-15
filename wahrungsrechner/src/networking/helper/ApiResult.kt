/*
 * ApiResult
 *
 * Version 1.0
 *
 * Copyright (c) 2020 Fh Erfurt
 * by C. Zeitler and S. Golchert
 */

package de.kba.api.wahrungsrechner.networking.helper

/**
 * Helper class to wrap the API-Result
 */
sealed class ApiResult<out T> {
    data class Success<out T : Any>(val data: T) : ApiResult<T>()
    data class Error(val error: NetworkError) : ApiResult<Nothing>()
}