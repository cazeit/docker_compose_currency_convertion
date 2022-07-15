/*
 * CurrencyConvertionExternalResult
 *
 * Version 1.0
 *
 * Copyright (c) 2020 Fh Erfurt
 * by C. Zeitler and S. Golchert
 */

package de.kba.api.wahrungsrechner.model.wahrungsrechner

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = false)
class CurrencyConvertionExternalResult {
    @JsonProperty("updated")
    val updated: String? = null

    @JsonProperty("source")
    val fromCurrency: String? = null

    @JsonProperty("target")
    val toCurrency: String? = null

    // == value from request
    @JsonProperty("quantity")
    val quantity: Double? = null

    // value of 1 fromCurrency in toCurrency
    @JsonProperty("value")
    val value: Double? = null

    // calculated conversion
    @JsonProperty("amount")
    val amount: Double? = null
}