/*
 * RealtimeCurrencyExchangeRate
 *
 * Version 1.0
 *
 * Copyright (c) 2020 Fh Erfurt
 * by C. Zeitler and S. Golchert
 */
package org.acme.de.fhe.kba2020;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbPropertyOrder;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * This is another Model class, also known as POJO class which is holding all the needed informations about Currencys.
 * It represents the JSON field result on our result json object ExchangeRate. This class must be Serializable so that
 * quarkus can map the class into a JSON object.
 *
 * @author Sebastian Golchert
 * @version 1.0
 */
@JsonbPropertyOrder({"updated", "fromCurrency", "toCurrency", "value", "quantity", "amount"})
public class RealtimeCurrencyExchangeRate implements Serializable {

    /**
     * LocalDateTime object which holds the timestamp we get the data from Alphavantage
     */
    private LocalDateTime updated = LocalDateTime.now();
    /**
     * String of the base currency
     */
    private String FromCurrency;
    /**
     * String of the curse currency
     */
    private String ToCurrency;
    /**
     * The curse value as float
     */
    private float value;
    /**
     * the used quantity as float by default 1.0f
     */
    private float quantity = 1.0f;
    /**
     * The amount as float calculated : amount = value * quantity
     */
    private float amount;

    /**
     * Ctor we need for the Mapping
     */
    public RealtimeCurrencyExchangeRate() {
    }

    /**
     * Ctor to init all fields.
     *
     * @param fromCurrencyCode base currency as String
     * @param toCurrencyCode   curse currency as String
     * @param exchangeRate     the exchange rate value as float
     * @param quantity         the quantity value as float
     */
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
        return this.updated.toString();
    }

    public void setUpdated(String locDateTime) {
        this.updated = LocalDateTime.parse(locDateTime);
    }

    public String getFromCurrency() {
        return FromCurrency;
    }

    @JsonbProperty(value = "1. From_Currency Code")
    public void setFromCurrency(String fromCurrencyCode) {
        FromCurrency = fromCurrencyCode;
    }

    public String getToCurrency() {
        return ToCurrency;
    }

    @JsonbProperty(value = "3. To_Currency Code")
    public void setToCurrency(String toCurrencyCode) {
        ToCurrency = toCurrencyCode;
    }

    public float getValue() {
        return value;
    }

    @JsonbProperty(value = "5. Exchange Rate")
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
};