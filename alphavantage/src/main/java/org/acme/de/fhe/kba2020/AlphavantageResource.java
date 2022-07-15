/*
 * AlphavantageResource
 *
 * Version 1.0
 *
 * Copyright (c) 2020 Fh Erfurt
 * by C. Zeitler and S. Golchert
 */
package org.acme.de.fhe.kba2020;

import org.apache.http.HttpStatus;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * This is the resource class which defines a REST Endpoint on <server_ip_address>/av/convertion.
 * This class uses the IAlphavantageService interface to provide access to the rest client which is connected with the Alphavantage REST-Server.
 *
 * @author Sebastian Golchert
 * @version 1.0
 */
@Path("/av")
public class AlphavantageResource {

    /**
     * The Restservice which ist connected with Alphavantage REST-API send us the currency informations
     */
    @Inject
    @RestClient
    dockerIAlphavantageService iAlphavantageService;

    /**
     * The API-KEY as String we need to get access to https://www.alphavantage.co/query the REST-ENDPOINT
     */
    private final String m_API_KEY = "MFULP22J9LXY83N8";
    /**
     * We also need a function name as String to tell Alphavantage which market data we want to get,
     * like API DOCU EXAMPLE: https://www.alphavantage.co/query?function=CURRENCY_EXCHANGE_RATE&...
     */
    private final String m_FUNCTION_NAME = "CURRENCY_EXCHANGE_RATE";
    /**
     * Our POJO which hold the JSON Response for us.
     */
    private ExchangeRate m_ExchangeRate = null;
    /**
     * The TRunner instance to idle the different API-CALLS for simulation stuff
     */
    private TRunner tRunner;


    @GET
    @Path("/health")
    public Response healthCheck() {
        Response response;
        if(TRunner.getSharedInstance().getSleeping()) {
            response = Response.status(HttpStatus.SC_SERVICE_UNAVAILABLE).build();
        } else {
            response = Response.noContent().build();
        }
        return response;
    }
    /**
     * With that function Quarkus generates a GET_REST_ENDPOINT URL for us like:
     * <server_ip_address>/av/convertion//{from_name}/{to_name}/{quantity} as example:
     * 0.0.0.0/av/convertion/EUR/USD/123.123 will get the Exchange rate from Alphavantage REST API and calculating a quantity on that value and
     * give us a MediaType.APPLICATION_JSON or MediaType.TEXT_PLAIN which we can handle further.
     * <p>
     * As Currency is based on the format <base currency>/<curse currency> like USD/EUR and ad float value which represents the curse from the market.
     * The base currency represents the factor 1.0 of the currency and is changed by the curse from the market to the curse currency.
     *
     * @param from_name as String which holds UPPER-CASE currency short 3-CHAR expression EUR - EURO or USD for US.Dollar, as base currency
     * @param to_name   as String which holds UPPER-CASE currency short 3-CHAR expression EUR - EURO or USD for US.Dollar, as curse currency
     * @param quant     the quantity as float, is needed to calculate the amount by: amount = quantity * value(curse value)
     * @return Response<m_ExchangeRate> Object which contains the accessed informations
     * <p>
     * A Typical response for Request: <server_ip_address>/av/convertion/USD/EUR/123.123
     * Looks like:
     * {
     * "result":
     * {
     * "updated": "2020-08-21T08:39:42.459942",
     * "fromCurrency":"USD",
     * "toCurrency":"EUR",
     * "value":0.8463,
     * "quantity":123.123,
     * "amount":104.199
     * },
     * "error":null,
     * "status":"OK",
     * "source":"Alphavantage"
     * }
     */
    @GET
    @Path("/convertion/{from_name}/{to_name}/{quantity}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getRateWithQuantity(@PathParam("from_name") String from_name, @PathParam("to_name") String to_name, @PathParam("quantity") float quant) {
        if(TRunner.getSharedInstance().getSleeping()) {
            return Response.status(503).build();
        }

        this.m_ExchangeRate = iAlphavantageService.getExchangeRateOf(m_FUNCTION_NAME, from_name, to_name, m_API_KEY);

        if (this.m_ExchangeRate.getResult() == null) {
            AlphavantageErrorHandler aveHandler = new AlphavantageErrorHandler();
            this.m_ExchangeRate.setResult(null);
            aveHandler.checkForErrors(this.m_ExchangeRate);
            this.m_ExchangeRate.setError(aveHandler.checkForErrors(this.m_ExchangeRate));

            return Response.serverError().entity(m_ExchangeRate).build();
        } else {
            this.m_ExchangeRate.getResult().setQuantity(quant);
            this.m_ExchangeRate.getResult().setAmount(quant * this.m_ExchangeRate.getResult().getValue());

            AlphavantageErrorHandler aveHandler = new AlphavantageErrorHandler();

            this.m_ExchangeRate.setError(aveHandler.checkForErrors(this.m_ExchangeRate));

            return Response.ok(m_ExchangeRate).build();
        }
    }

    /**
     * With that function Quarkus generates a GET_REST_ENDPOINT URL for us like:
     * <server_ip_address>/av/convertion//{from_name}/{to_name}/{quantity}/debug/{debug_parameter} as example:
     * 0.0.0.0/av/convertion/EUR/USD/25.13/debug/Random will get the Exchange rate from Alphavantage REST API and calculating a quantity on that value and call the TRunner
     * with the behaviour (Random). We also will get a MediaType.APPLICATION_JSON or MediaType.TEXT_PLAIN mapped into a Response object.
     *
     * @param from_name   as String which holds UPPER-CASE currency short 3-CHAR expression as base currency
     * @param to_name     as String which holds UPPER-CASE currency short 3-CHAR expression as curse currency
     * @param quant       the quantity as float, is needed to calculate the amount by: amount = quantity * value(curse value)
     * @param debug_param the possible debug parameter: Random, FixedTimeOut, NoResponse as EDebugParameters Enum, Quarkus will map the given URL String to the Enum automatically
     * @return Response<m_ExchangeRate> Object which contains the accessed informations
     * <p>
     * To get a response the request Url has to look like: <server_ip_address>/av/convertion/USD/EUR/123.123/debug/Random
     * we get the same output like above, but additionally the Console will inform us about a idle time which the response was paused by TRunner
     * like:
     * Sleeping for: 2224 ms.
     * Wake Up.
     */
    @GET
    @Path("/convertion/{from_name}/{to_name}/{quantity}/debug/{debug_parameter}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getRateWithQuantityDebug(@PathParam("from_name") String from_name, @PathParam("to_name") String to_name, @PathParam("quantity") float quant, @PathParam("debug_parameter") EDebugParameters debug_param) {
        if(TRunner.getSharedInstance().getSleeping()) {
            return Response.status(503).build();
        }

        EDebugParameters eDebugParam = debug_param;
        TRunner.getSharedInstance().run(debug_param);
        return getRateWithQuantity(from_name, to_name, quant);
    }

    /**
     * With that function Quarkus generates a GET_REST_ENDPOINT URL for us like:
     * <server_ip_address>/av/convertion/{from_name}/{to_name}as example:
     * 0.0.0.0/av/convertion/EUR/USD/ will get the Exchange rate from Alphavantage REST API and using a quantity with 1.0 on background,
     * giving us a MediaType.APPLICATION_JSON or MediaType.TEXT_PLAIN which we can handle further.
     * <p>
     * Intern the function calls the getRateWithQuantity(...) function like:
     * return getRateWithQuantity(from_name,to_name, 1.0f);
     *
     * @param from_name as String which holds UPPER-CASE currency short 3-CHAR expression EUR - EURO or USD for US.Dollar, as base currency
     * @param to_name   as String which holds UPPER-CASE currency short 3-CHAR expression EUR - EURO or USD for US.Dollar, as curse currency
     * @return Response<m_ExchangeRate> Object which contains the accessed informations
     * <p>
     * A Typical response for Request: <server_ip_address>/av/convertion/USD/EUR/123.123
     * Looks like:
     * {
     * "result":
     * {
     * "updated": "2020-08-21T08:39:42.459942",
     * "fromCurrency":"USD",
     * "toCurrency":"EUR",
     * "value":0.8463,
     * "quantity":1.0,
     * "amount":0.8463
     * },
     * "error":null,
     * "status":"OK"
     * }
     */
    @GET
    @Path("/convertion/{from_name}/{to_name}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getRateWithoutQuantity(@PathParam("from_name") String from_name, @PathParam("to_name") String to_name) {
        if(TRunner.getSharedInstance().getSleeping()) {
            return Response.status(503).build();
        }

        return getRateWithQuantity(from_name, to_name, 1.0f);
    }

    /**
     * With that function Quarkus generates a GET_REST_ENDPOINT URL for us like:
     * <server_ip_address>/av/convertion/{from_name}/{to_name}/debug/{debug_parameter} as example:
     * 0.0.0.0/av/convertion/EUR/USD/debug/FixedTimeOut will get the Exchange rate from Alphavantage REST API and calculating a quantity on that value and call the TRunner
     * with the behaviour (FixedTimeOut). We also will get a MediaType.APPLICATION_JSON or MediaType.TEXT_PLAIN mapped into a Response object.
     * The qunatity will set to 1.0 in th background.
     * Internally we also routes to the getRateWithQuantity(...) function.
     *
     * @param from_name   as String which holds UPPER-CASE currency short 3-CHAR expression as base currency
     * @param to_name     as String which holds UPPER-CASE currency short 3-CHAR expression as curse currency
     * @param debug_param the possible debug parameter: Random, FixedTimeOut, NoResponse as EDebugParameters Enum, Quarkus will map the given URL String to the Enum automatically
     * @return Response<m_ExchangeRate> Object which contains the accessed informations
     * <p>
     * To get a response the request Url has to look like: <server_ip_address>/av/convertion/USD/EUR/debug/FixedTimeOut
     * we get the same output like above, but additionally the Console will inform us about a idle time which the response was paused by TRunner
     * like:
     * Sleeping for: 1000 ms.
     * Wake Up.
     */
    @GET
    @Path("/convertion/{from_name}/{to_name}/debug/{debug_parameter}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response getRateWithoutQuantityDebug(@PathParam("from_name") String from_name, @PathParam("to_name") String to_name, @PathParam("debug_parameter") EDebugParameters debug_param) {
        if(TRunner.getSharedInstance().getSleeping()) {
            return Response.status(503).build();
        }
        EDebugParameters eDebugParam = debug_param;
        TRunner.getSharedInstance().run(debug_param);
        return getRateWithQuantity(from_name, to_name, 1.0f);

    }
}
