/*
 * TRunner
 *
 * Version 1.0
 *
 * Copyright (c) 2020 Fh Erfurt
 * by C. Zeitler and S. Golchert
 */
package org.acme.de.fhe.kba2020;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * This is the Interface using the MicroProfile REST Client to provide the proper JAX-RS and MicroProfile annotations.
 * "@RegisterRestClient" allows Quarkus to know that this interface is meant to be available for CDI injection as a REST Client.
 * In order to determine the base URL to which REST calls will be made, the REST Client uses configuration from application.properties.
 * Please look to https://quarkus.io/guides/rest-client for more informations.
 *
 * @author Sebastian Golchert
 * @version 1.0
 */
@RegisterRestClient(configKey = "alphavantage-service")
public interface dockerIAlphavantageService {

    /**
     * In Order we want to get the response from Alphavantage API on URI: https://www.alphavantage.co/query we have to specify a function,
     * a String for the base currency and another string for the curse currency and a API-Key to.
     * A Example Request hast to look like: https://www.alphavantage.co/query?function=CURRENCY_EXCHANGE_RATE&from_currency=BTC&to_currency=CNY&apikey=demo
     * We tell quarkus with "@Path("/query")" were the API-URI Endpoint starts.
     * Also we inform about HTTP METHOD we want to use with "@GET"  and the Response tape with "@Produces("application/json")".
     *
     * @param function      a String which represents the function Alphavantage should use
     * @param from_currency a String with the base currency
     * @param to_currency   a String with the curse currency
     * @param apikey        a String with the API-Key Alphavantage providing us after registration, demo is only for testing purpose
     * @return
     */
    @Path("/query")
    @GET
    @Produces("application/json")
    ExchangeRate getExchangeRateOf(@QueryParam("function") String function,
                                   @QueryParam("from_currency") String from_currency,
                                   @QueryParam("to_currency") String to_currency,
                                   @QueryParam("apikey") String apikey);
}
