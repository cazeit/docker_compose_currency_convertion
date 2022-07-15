/*
 * ApiManager
 *
 * Version 1.0
 *
 * Copyright (c) 2020 Fh Erfurt
 * by C. Zeitler and S. Golchert
 */

package de.kba.api.wahrungsrechner.networking

import de.kba.api.wahrungsrechner.model.internal.SimpleResponse
import de.kba.api.wahrungsrechner.networking.helper.SimpleResponseCreator
import de.kba.api.wahrungsrechner.networking.helper.PrecisionConvertionUtil
import de.kba.api.wahrungsrechner.networking.requests.CurrencyConvertionRequest
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Singleton that handles all outside API calls
 */
object ApiManager {
    suspend fun convertCurrency(fromCurrency: String, toCurrency: String, quantity: Double? = 1.0): SimpleResponse =
            suspendCoroutine { cont ->
                val safeQuantity = quantity ?: 1.0
                val request = CurrencyConvertionRequest()
                request.currencyFrom = fromCurrency
                request.currencyTo = toCurrency
                request.quantity = PrecisionConvertionUtil.convertForRequestWithPrecision(safeQuantity)
                request.onFinished = {
                    val respondText = SimpleResponseCreator.simpleResponseForCurrencyConvertionResult(it)
                    cont.resume(respondText)
                }
                request.performRequest()
            }
}
