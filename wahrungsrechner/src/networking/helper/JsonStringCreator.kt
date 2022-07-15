package de.kba.api.wahrungsrechner.networking.helper

import com.fasterxml.jackson.databind.ObjectMapper
import de.kba.api.wahrungsrechner.model.internal.CurrencyConvertionInternalApiResult
import de.kba.api.wahrungsrechner.model.internal.CurrencyConvertionInternalError
import de.kba.api.wahrungsrechner.model.internal.CurrencyConvertionInternalResult
import de.kba.api.wahrungsrechner.model.wahrungsrechner.CurrencyConvertionExternalApiResult

object JsonStringCreator {

    private val mapper: ObjectMapper = ObjectMapper()

    /**
     * This function creates a successful response and maps the API-Result to the structure to send it to the main-service.
     * It also contains checks to first make sure the result from the API is not broken (we are talking about money here!)
     */
    fun successfulResponseJsonStringForCurrencyConvertionResult(apiResult: CurrencyConvertionExternalApiResult): String {
        val responseObject = CurrencyConvertionInternalApiResult()

        val externalResult = apiResult.result
                ?: return failureResponseJsonStringForCurrencyConvertionResult(NetworkError.decodeError())
        if (externalResult.amount == null || externalResult.quantity == null || externalResult.value == null || externalResult.fromCurrency == null
                || externalResult.toCurrency == null) {
            return failureResponseJsonStringForCurrencyConvertionResult(NetworkError.decodeError())
        }
        val responseResult = CurrencyConvertionInternalResult()
        responseResult.updated = externalResult.updated
        responseResult.fromCurrency = externalResult.fromCurrency
        responseResult.toCurrency = externalResult.toCurrency
        responseResult.value = externalResult.value
        responseResult.quantity = PrecisionConvertionUtil.convertResultWithPrecision(externalResult.quantity)
        responseResult.amount = PrecisionConvertionUtil.convertResultWithPrecision(externalResult.amount)

        responseObject.result = responseResult
        responseObject.status = CurrencyConvertionInternalApiResult.ResultStatus.OK.description

        return mapper.writeValueAsString(responseObject)
    }

    /**
     * This function creates a failureResponse-json-string that can be sent to the main-service with the help of an error
     */
    fun failureResponseJsonStringForCurrencyConvertionResult(error: NetworkError): String {
        val responseObject = CurrencyConvertionInternalApiResult()

        val responseError = CurrencyConvertionInternalError()
        responseError.errorCode = error.status
        responseError.errorMessage = error.message

        responseObject.error = responseError

        return mapper.writeValueAsString(responseObject)
    }
}