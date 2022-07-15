/*
 * AlphavantageErrorHandler
 *
 * Version 1.0
 *
 * Copyright (c) 2020 Fh Erfurt
 * by C. Zeitler and S. Golchert
 */
package org.acme.de.fhe.kba2020;

/**
 * We use this class to Handle Errors which can occur by requesting a REST-API.
 * We have four main errors and there corresponding error codes which we maintain.
 * Acual we check for Response errors and connection errors, later we will support the other errors to.
 *
 * @author Sebastian Golchert
 * @version 1.0
 */
public class AlphavantageErrorHandler {
    /**
     * The known error codes
     */
    private final static int[] allErrorCodes = {66, 999, 1333, 1999};

    /**
     * The error messages
     */
    private final static String[] errorMessages = {
            "Response could not be processed!",
            "Connection failed or aborted!",
            "An Debug Error occurs!",
            "Invalid parameters passed!"
    };

    /**
     * actual error
     */
    private AlphavantageError actualError = null;
    /**
     * All valid currencys, sorry for the column, we change later to row format
     */
    private final static String[] validCurrencies = {"AED", "AFN", "ALL", "AMD", "ANG", "AOA", "ARS", "AUD", "AWG",
            "AZN", "BAM", "BBD", "BDT", "BGN", "BHD", "BIF", "BMD", "BND", "BOB", "BRL", "BSD", "BTN", "BWP", "BZD",
            "CAD", "CDF", "CHF", "CLF", "CLP", "CNH", "CNY", "COP", "CUP", "CVE", "CZK", "DJF", "DKK", "DOP", "DZD",
            "EGP", "ERN", "ETB", "EUR", "FJD", "FKP", "GBP", "GEL", "GHS", "GIP", "GMD", "GNF", "GTQ", "GYD", "HKD",
            "HNL", "HRK", "HTG", "HUF", "IDR", "ILS", "INR", "IQD", "IRR", "ISK", "JEP", "JMD", "JOD", "JPY", "KES",
            "KGS", "KHR", "KMF", "KPW", "KRW", "KWD", "KYD", "KZT", "LAK", "LBP", "LKR", "LRD", "LSL", "LYD", "MAD",
            "MDL", "MGA", "MKD", "MMK", "MNT", "MOP", "MRO", "MRU", "MUR", "MVR", "MWK", "MXN", "MYR", "MZN", "NAD",
            "NGN", "NOK", "NPR", "NZD", "OMR", "PAB", "PEN", "PGK", "PHP", "PKR", "PLN", "PYG", "QAR", "RON", "RSD",
            "RUB", "RUR", "RWF", "SAR", "SBDf", "SCR", "SDG", "SDR", "SEK", "SGD", "SHP", "SLL", "SOS", "SRD", "SYP",
            "SZL", "THB", "TJS", "TMT", "TND", "TOP", "TRY", "TTD", "TWD", "TZS", "UAH", "UGX", "USD", "UYU", "UZS",
            "VND", "VUV", "WST", "XAF", "XAG", "XAU", "XCD", "XDR", "XOF", "XPF", "YER", "ZAR", "ZMW", "ZWL"};


    public AlphavantageError getActualError() {
        return actualError;
    }

    public void setActualError(AlphavantageError actualError) {
        this.actualError = actualError;
        System.out.println("Error Message was: " + actualError.getMessage() + "\n");
    }

    private void connectionFailed() {
        this.actualError = new AlphavantageError(allErrorCodes[1], errorMessages[1]);
    }

    private void ResponseProcessFailed() {
        setActualError(new AlphavantageError(allErrorCodes[0], errorMessages[0]));
    }

    public boolean checkForValidCurrency(String basic_currency, String curse_currency) {
        boolean hit = false;
        for (String s : validCurrencies) {
            if (s.equals(basic_currency) && s.equals(curse_currency)) {
                hit = true;
            }
        }
        return hit;
    }

    public AlphavantageError checkForErrors(ExchangeRate exRate) {

        if (exRate.getResult() == null) {
            this.ResponseProcessFailed();
            exRate.setStatus(EAlphavantageStatus.FAIL);
        } else if (this.checkForValidCurrency(exRate.getResult().getFromCurrency(), exRate.getResult().getToCurrency())) {
            this.WrongParameterFound();
        }

        return this.actualError;
    }

    private void WrongParameterFound() {
        setActualError(new AlphavantageError(allErrorCodes[3], errorMessages[3]));
    }
}
