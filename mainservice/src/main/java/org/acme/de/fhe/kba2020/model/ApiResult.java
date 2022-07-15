/*
 * ApiResult
 *
 * Version 1.0
 *
 * Copyright (c) 2020 Fh Erfurt
 * by C. Zeitler and S. Golchert
 */
package org.acme.de.fhe.kba2020.model;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbPropertyOrder;

/**
 * This is the wrapper class to represent our result information's we want to send via JSON. The class is used as return type in the resource classes.
 *
 * @author Carl Zeitler, Sebastian Golchert
 * @version 1.0
 */
@JsonbPropertyOrder({"result", "error", "status", "source"})
public class ApiResult {
    /**
     * The RealtimeCurrencyExchangeRate named result holding the followed informations:
     * String updated;
     * String FromCurrency;
     * String ToCurrency;
     * float value;
     * float quantity;
     * float amount;
     */
    private RealtimeCurrencyExchangeRate result = null;
    /**
     * Error class object to handle the error state.
     */
    private Error error = null;
    /**
     * Enum by default in OK-Status
     */
    private Status status = Status.OK;
    /**
     * The name of the current service to seperate the single services
     */
    private String source = "Mainservice";

    public RealtimeCurrencyExchangeRate getResult() {
        return result;
    }

    public ApiResult() {
    }

    public ApiResult(RealtimeCurrencyExchangeRate exchangeRate, Error error, Status status) {
        this.result = exchangeRate;
        this.error = error;
        this.status = status;
    }

    @JsonbProperty(value = "result", nillable = true)
    public void setResult(RealtimeCurrencyExchangeRate result) {
        this.result = result;
    }

    public Error getError() {
        return error;
    }

    @JsonbProperty(value = "error", nillable = true)
    public void setError(Error error) {
        this.error = error;
    }

    public Status getStatus() {
        return status;
    }

    @JsonbProperty(value = "status")
    public void setStatus(Status status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    @JsonbProperty(value = "source")
    public void setSource(String source) {
        this.source = source;
    }

}
