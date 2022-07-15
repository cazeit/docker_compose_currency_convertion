/*
 * CurrencyValidationUtil
 *
 * Version 1.0
 *
 * Copyright (c) 2020 Fh Erfurt
 * by C. Zeitler and S. Golchert
 */

package de.kba.api.wahrungsrechner.networking.helper

object CurrencyValidationUtil {

    /**
     * Since the API does not check if the currency has a valid name while processing a request,
     * I implemented a check using the codes that are marked as usable in the Documentation
     */
    private val supportedCurrencyNames = arrayOf("AED", "AFN", "ALL", "AMD", "AOA", "ARS", "AUD", "AWG",
            "AZN", "BAM", "BBD", "BCH", "BDT", "BGN", "BHD", "BIF", "BMD", "BND", "BOB", "BRL", "BSD", "BTC", "BTN",
            "BWP", "BYN", "BYR", "BZD", "CAD", "CDF", "CHF", "CLP", "CNY", "COP", "CRC", "CUP", "CVE", "CZK", "DJF",
            "DKK", "DOP", "DZD", "ECS", "EGP", "ERN", "ETB", "ETH", "EUR", "FJD", "FKP", "GBP", "GEL", "GHS", "GIP",
            "GMD", "GNF", "GTQ", "GYD", "HKD", "HNL", "HRK", "HTG", "HUF", "IDR", "ILS", "INR", "IQD", "IRR", "ISK",
            "JMD", "JOD", "JPY", "KES", "KGS", "KHR", "KMF", "KPW", "KRW", "KWD", "KYD", "KZT", "LAK", "LBP", "LKR",
            "LRD", "LSL", "LTC", "LYD", "MAD", "MDL", "MGA", "MKD", "MMK", "MNT", "MOP", "MRO", "MUR", "MVR", "MWK",
            "MXN", "MYR", "NAD", "NGN", "NIO", "NOK", "NPR", "NZD", "OMR", "PAB", "PEN", "PGK", "PHP", "PKR", "PLN",
            "PYG", "QAR", "RON", "RSD", "RUB", "RWF", "SAR", "SBD", "SCR", "SEK", "SGD", "SHP", "SLL", "SOS", "SRD",
            "STD", "SVC", "SYP", "SZL", "THB", "TND", "TOP", "TRY", "TTD", "TWD", "TZS", "UAH", "UGX", "USD", "UYU",
            "UZS", "VND", "VUV", "WST", "YER", "ZAR")

    fun isCurrencyNameValid(currencyName: String?): Boolean {
        if (currencyName.isNullOrBlank()) {
            return false
        }
        return supportedCurrencyNames.contains(currencyName)
    }
}