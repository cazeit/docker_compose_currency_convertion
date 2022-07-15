/*
 * JsonStringCreator
 *
 * Version 1.0
 *
 * Copyright (c) 2020 Fh Erfurt
 * by C. Zeitler and S. Golchert
 */

package de.kba.api.wahrungsrechner.networking.helper

import de.kba.api.wahrungsrechner.model.internal.SimpleResponse
import de.kba.api.wahrungsrechner.model.wahrungsrechner.CurrencyConvertionExternalApiResult
import io.ktor.http.*

/**
 * This Singleton creates the json-answers that are being sent to the main service
 */
object SimpleResponseCreator {

    /**
     * This function creates a response json string based on a result from an APIResult that was previously fetched
     */
    fun simpleResponseForCurrencyConvertionResult(apiResult: ApiResult<Any>): SimpleResponse {
        return when (apiResult) {
            is ApiResult.Success -> {
                val result = apiResult.data as? CurrencyConvertionExternalApiResult
                if (result == null) {
                    val error = NetworkError.debugError("Seems like there is an error in the code, there is a type conflict!")
                    SimpleResponse(JsonStringCreator.failureResponseJsonStringForCurrencyConvertionResult(error), HttpStatusCode.InternalServerError)
                } else {
                    SimpleResponse(JsonStringCreator.successfulResponseJsonStringForCurrencyConvertionResult(result), HttpStatusCode.OK)
                }
            }
            is ApiResult.Error -> {
                SimpleResponse(JsonStringCreator.failureResponseJsonStringForCurrencyConvertionResult(apiResult.error), HttpStatusCode.InternalServerError)
            }
        }
    }
}