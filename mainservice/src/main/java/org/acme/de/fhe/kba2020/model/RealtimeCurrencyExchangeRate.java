/*
 * RealtimeCurrencyExchangeRate
 *
 * Version 1.0
 *
 * Copyright (c) 2020 Fh Erfurt
 * by C. Zeitler and S. Golchert
 */
package org.acme.de.fhe.kba2020.model;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbPropertyOrder;
import java.io.Serializable;

/**
 * This is the wrapper class to represent our result information's we want to send via JSON, especially the Currency infirmation's.
 *
 * @author Sebastian Golchert, Carl Zeitler
 * @version 1.0
 */
@JsonbPropertyOrder({"updated", "fromCurrency", "toCurrency", "value", "quantity", "amount"})
public class RealtimeCurrencyExchangeRate implements Serializable {
    /**
     * The Timestamp as string like: "2020-08-31T08:39:42.459942"
     */
    private String updated;
    /**
     * The basic currency
     */
    private String FromCurrency;
    /**
     * The curse currency
     */
    private String ToCurrency;
    /**
     * The value we get from the different Api's
     */
    private float value;
    /**
     * The quantity we use to calculate the amount like: amount = value * quantity
     */
    private float quantity = 1.0f;
    /**
     * the result amount
     */
    private float amount;

    public RealtimeCurrencyExchangeRate() {
    }

    public RealtimeCurrencyExchangeRate(String fromCurrencyCode,
                                        String toCurrencyCode,
                                        float exchangeRate,
                                        float quantity) {
        this.FromCurrency = fromCurrencyCode;
        this.ToCurrency = toCurrencyCode;
        this.value = exchangeRate;
        this.quantity = quantity;
        this.amount = quantity * value;
    }

    public String getUpdated() {
        return this.updated;
    }

    @JsonbProperty(value = "updated")
    public void setUpdated(String locDateTime) {
        this.updated = locDateTime;
    }

    public String getFromCurrency() {
        return FromCurrency;
    }

    @JsonbProperty(value = "fromCurrency")
    public void setFromCurrency(String fromCurrencyCode) {
        FromCurrency = fromCurrencyCode;
    }

    public String getToCurrency() {
        return ToCurrency;
    }

    @JsonbProperty(value = "toCurrency")
    public void setToCurrency(String toCurrencyCode) {
        ToCurrency = toCurrencyCode;
    }


    public float getValue() {
        return value;
    }

    @JsonbProperty(value = "value")
    public void setValue(float exchangeRate) {
        value = exchangeRate;
    }

    public float getQuantity() {
        return this.quantity;
    }

    @JsonbProperty(value = "quantity")
    public void setQuantity(float newQuantity) {
        this.quantity = newQuantity;
    }

    public float getAmount() {
        return this.amount;
    }

    @JsonbProperty(value = "amount")
    public void setAmount(float newAmount) {
        this.amount = newAmount;
    }

    @Override
    public String toString() {
        return "RealtimeCurrencyExchangeRate{" +
                "FromCurrencyCode='" + FromCurrency + '\'' +
                ", ToCurrencyCode='" + ToCurrency + '\'' +
                ", ExchangeRate='" + value + '\'' +
                ", UpdatedAt=" + updated.toString() + '\'' +
                '}';
    }

}
