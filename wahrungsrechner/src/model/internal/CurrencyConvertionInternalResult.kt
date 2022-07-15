/*
 * CurrencyConvertionInternalResult
 *
 * Version 1.0
 *
 * Copyright (c) 2020 Fh Erfurt
 * by C. Zeitler and S. Golchert
 */

package de.kba.api.wahrungsrechner.model.internal

import com.fasterxml.jackson.annotation.JsonProperty

class CurrencyConvertionInternalResult {

    /**
     * last updated stamp
     */
    @JsonProperty("updated")
    var updated: String? = null

    /**
     * 3digit code for currency that is converted from
     */
    @JsonProperty("fromCurrency")
    var fromCurrency: String? = null

    /**
     * 3digit code for currency that is converted to
     */
    @JsonProperty("toCurrency")
    var toCurrency: String? = null

    /**
     * Value of 1 fromCurrency in toCurrency ( 1 fromCurrency == value toCurrency )
     */
    @JsonProperty("value")
    var value: Double? = null

    /**
     * Quantity of fromCurrency that was converted
     */
    @JsonProperty("quantity")
    var quantity: Double? = null

    /**
     * Amount of toCurrency that was calculated
     */
    @JsonProperty("amount")
    var amount: Double? = null
}