/*
 * ConvertionPathUtil
 *
 * Version 1.0
 *
 * Copyright (c) 2020 Fh Erfurt
 * by C. Zeitler and S. Golchert
 */

package de.kba.api.wahrungsrechner.helper

import de.kba.api.wahrungsrechner.model.DebugParameter
import de.kba.api.wahrungsrechner.model.PathSegment
import de.kba.api.wahrungsrechner.model.internal.SimpleResponse
import de.kba.api.wahrungsrechner.networking.ApiManager
import de.kba.api.wahrungsrechner.networking.helper.*
import io.ktor.http.*

// TODO: finish README
// TODO: test and check stats with more containers!
// TODO: added logging
/**
 * Util that helps processing requests internally by checking their path
 * @author Carl Zeitler
 */
object ConvertionPathUtil {
    /**
     * @param parameters Parameters of a tipical currency request
     * @param additionalPathSegment Enum Value that defines what optional parameters were passed (none/debug)
     * @return
     */
    suspend fun processCurrencyRequest(parameters: Parameters, additionalPathSegment: PathSegment): SimpleResponse {
        TRunner.checkAndAwaitTRunner()
        val fromCurrency = parameters["fromCurrency"]
        val toCurrency = parameters["toCurrency"]
        val quantityString = parameters["quantity"]
        val quantity = quantityString?.toDoubleOrNull()

        when (additionalPathSegment) {
            PathSegment.Debug -> {
                val debug = parameters["debugParameter"]
                        ?: return SimpleResponse(JsonStringCreator.failureResponseJsonStringForCurrencyConvertionResult(NetworkError.parameterError()), HttpStatusCode.BadRequest)
                try {
                    val debugParameter = DebugParameter.valueOf(debug)
                    TRunner.sleep(debugParameter)
                } catch (e: Exception) {
                    return SimpleResponse(JsonStringCreator.failureResponseJsonStringForCurrencyConvertionResult(NetworkError.parameterError()), HttpStatusCode.BadRequest)
                }
            }
            PathSegment.None -> {
                // do nothing :)
            }
        }

        if (!CurrencyValidationUtil.isCurrencyNameValid(fromCurrency) || !CurrencyValidationUtil.isCurrencyNameValid(toCurrency)) {
            return SimpleResponse(JsonStringCreator.failureResponseJsonStringForCurrencyConvertionResult(NetworkError.parameterError()), HttpStatusCode.BadRequest)
        }

        return ApiManager.convertCurrency(fromCurrency as String, toCurrency as String, quantity)
    }
}