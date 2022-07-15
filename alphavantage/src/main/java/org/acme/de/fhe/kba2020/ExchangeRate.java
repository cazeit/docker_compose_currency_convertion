/*
 * ExchangeRate
 *
 * Version 1.0
 *
 * Copyright (c) 2020 Fh Erfurt
 * by C. Zeitler and S. Golchert
 */
package org.acme.de.fhe.kba2020;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbPropertyOrder;

/**
 * This is the POJO class which holds all the necessary Exchange Rate Informations we want to use.
 * This class will mapped to a JSON Object which holds all the informations we got.
 * We told Jsonb that the property order should be 1."result", 2."error", 3."status",
 * to earn a consistent JSON Response format at our REST-API Endpoint.
 *
 * @author Sebastian Golchert
 * @version 1.0
 */
@JsonbPropertyOrder({"result", "error", "status", "source"})
public class ExchangeRate {

    /**
     * RealtimeCurrencyExchangeRate name result, holding all Exchange informations
     */
    private RealtimeCurrencyExchangeRate result = null;
    /**
     * AlphavantageError Holding error code and a message
     */
    private AlphavantageError error = null;
    /**
     * EAlphavantageStatus Object named status which is set to OK by init
     */
    private EAlphavantageStatus status = EAlphavantageStatus.OK;

    /**
     * String that returns where the response was processed
     */
    private String source = "Alphavantage";

    public EAlphavantageStatus getStatus() {
        return status;
    }

    @JsonbProperty(value = "status")
    public void setStatus(EAlphavantageStatus status) {
        this.status = status;
    }

    public RealtimeCurrencyExchangeRate getResult() {
        return result;
    }

    @JsonbProperty(value = "Realtime Currency Exchange Rate", nillable = true)
    public void setResult(RealtimeCurrencyExchangeRate result) {
        AlphavantageErrorHandler aveh = new AlphavantageErrorHandler();
        this.result = result;
        this.error = aveh.checkForErrors(this);
    }

    public AlphavantageError getError() {
        return error;
    }

    @JsonbProperty(value = "error", nillable = true)
    public void setError(AlphavantageError ave) {
        this.error = ave;
    }

    public String getSource() {
        return source;
    }

    @JsonbProperty(value = "source")
    public void setSource(String src) {
        this.source = src;
    }
}
