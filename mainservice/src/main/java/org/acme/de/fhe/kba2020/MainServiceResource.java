/*
 * MainServiceResource
 *
 * Version 1.0
 *
 * Copyright (c) 2020 Fh Erfurt
 * by C. Zeitler and S. Golchert
 */
package org.acme.de.fhe.kba2020;

import org.acme.de.fhe.kba2020.helper.ErrorHandler;
import org.acme.de.fhe.kba2020.helper.ResponseBuilder;
import org.acme.de.fhe.kba2020.model.AffectedService;
import org.acme.de.fhe.kba2020.model.ApiResult;
import org.acme.de.fhe.kba2020.model.DebugParameter;
import org.acme.de.fhe.kba2020.model.Error;
import org.acme.de.fhe.kba2020.services.AlphavantageEndpointService;
import org.acme.de.fhe.kba2020.services.WahrungsrechnEndpointService;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * This is the resource class which defines a REST Endpoint on <server_ip_address>/.
 * This class uses the CurrencyEndpointService interface to provide access to the rest client which is connected with the
 * traefik reverse-proxy which do all the load balancing between the Alphavantage and Wahrungsrechner REST-Service.
 *
 * @author Sebastian Golchert, Carl Zeitler
 * @version 1.0
 */
@Path("/")
@ApplicationScoped
public class MainServiceResource {

    /**
     * The rest client service which holds the needed functions we will use to connect to the two microservices
     */

    @Inject
    @RestClient
    AlphavantageEndpointService currencyAlphavantageEndpointService;

    @Inject
    @RestClient
    WahrungsrechnEndpointService currencyWahrungsrechnerEndpointService;

    private final static String quantityDefault = "1.0";
    private final static String affectedServiceDefault = "both";

    /**
     * This Method is used if someone uses the URL only with two parameters like:
     * <server_ip_address>/{from_name}/{to_name} as example:
     * localhost/EUR/USD
     *
     * @param fromCurrency as String which holds UPPER-CASE currency short 3-CHAR expression EUR - EURO or USD for US.Dollar
     * @param toCurrency   as String holds UPPER-CASE currency short 3-CHAR expression GBP - Pfund or USD for US.Dollar
     * @return ApiResult object which holds the returned informations and the error messages to.
     * <p>
     * <p>
     * <p>
     * A Typical response from Request URL: http://localhost/USD/EUR
     * looks like:
     * {
     * "result":
     * {
     * "updated": "2020-08-31T08:39:42.459942",
     * "fromCurrency":"USD",
     * "toCurrency":"EUR",
     * "value":0.8463,
     * "quantity":1.0,
     * "amount":0.8463
     * },
     * "error":null,
     * "status":"OK",
     * "source":"Alphavantage"
     * }
     */
    @Path("/{fromCurrency}/{toCurrency}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResult getRateWithoutQuantity(@PathParam("fromCurrency") String fromCurrency,
                                            @PathParam("toCurrency") String toCurrency) {
        return getRate(fromCurrency, toCurrency, quantityDefault);
    }

    /**
     * This Method is used if someone uses the URL only with two currency parameters and a quantity like:
     * <server_ip_address>/{from_name}/{to_name}/{quantity} as example:
     * localhost/EUR/USD/12.51
     *
     * @param fromCurrency as String which holds UPPER-CASE currency short 3-CHAR expression EUR - EURO or USD for US.Dollar.
     * @param toCurrency   as String holds UPPER-CASE currency short 3-CHAR expression GBP - Pfund or USD for US.Dollar.
     * @param quantity     as Double to calculate the amount by multiplying the value times the quantity.
     * @return ApiResult object which holds the returned informations and the error messages to.
     * <p>
     * <p>
     * <p>
     * A Typical response from Request URL: http://localhost/USD/EUR/10.0
     * looks like:
     * {
     * "result":
     * {
     * "updated": "2020-08-31T08:39:42.459942",
     * "fromCurrency":"USD",
     * "toCurrency":"EUR",
     * "value":0.8463,
     * "quantity":10.0,
     * "amount":8.4631
     * },
     * "error":null,
     * "status":"OK",
     * "source":"Alphavantage"
     * }
     */
    @GET
    @Path("/{fromCurrency}/{toCurrency}/{quantity}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public ApiResult getRate(@PathParam("fromCurrency") String fromCurrency,
                             @PathParam("toCurrency") String toCurrency,
                             @PathParam("quantity") String quantity) {
        ApiResult apiResult;

        try {
            Double quantityValue = Double.parseDouble(quantity);

            apiResult = getRateAlphavantage(fromCurrency, toCurrency, quantityValue);
            if (ErrorHandler.shouldForwardToNextService(apiResult)) {
                apiResult = getRateWahrungsrechner(fromCurrency, toCurrency, quantityValue);
            }
        } catch (Exception e) {
            apiResult = ResponseBuilder.buildParameterError();
        }
        return apiResult;
    }

    /**
     * This Method is used if someone uses the URL only with two currency parameters and a debug parameter to like:
     * <server_ip_address>/{from_name}/{to_name}/debug/…{debugParameter} as example:
     * localhost/EUR/USD/debug/Random
     * <p>
     * Possible debug strings are: random, fixed, noresponse.
     *
     * @param fromCurrency as String which holds UPPER-CASE currency short 3-CHAR expression EUR - EURO or USD for US.Dollar.
     * @param toCurrency   as String holds UPPER-CASE currency short 3-CHAR expression GBP - Pfund or USD for US.Dollar.
     * @param debugParam   as String to change the behaviour of the corresponding microservice.
     * @return ApiResult object which holds the returned informations and the error messages to.
     * <p>
     * <p>
     * <p>
     * A Typical response from Request URL: http://localhost/USD/EUR/debug/fixed
     * looks like:
     * {
     * "result":
     * {
     * "updated": "2020-08-31T08:39:42.459942",
     * "fromCurrency":"USD",
     * "toCurrency":"EUR",
     * "value":0.8463,
     * "quantity":1.0,
     * "amount":0.8463
     * },
     * "error":null,
     * "status":"OK",
     * "source":"Alphavantage"
     * }
     */
    @Path("/{fromCurrency}/{toCurrency}/debug/{debugParam}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResult getRateWithoutQuantityWithDebug(@PathParam("fromCurrency") String fromCurrency,
                                                     @PathParam("toCurrency") String toCurrency,
                                                     @PathParam("debugParam") String debugParam) {
        return getRateWithDebugAndService(fromCurrency, toCurrency, quantityDefault, debugParam, affectedServiceDefault);
    }

    /**
     * This Method is used if someone uses the URL only with two currency parameters and a debug parameter as well as affected service - like:
     * <server_ip_address>/{from_name}/{to_name}/debug/…{debugParameter}/{service} as example:
     * localhost/EUR/USD/debug/random/av
     * <p>
     * Possible debug strings are: random,fixed,noresponse.
     *
     * @param fromCurrency    as String which holds UPPER-CASE currency short 3-CHAR expression EUR - EURO or USD for US.Dollar.
     * @param toCurrency      as String holds UPPER-CASE currency short 3-CHAR expression GBP - Pfund or USD for US.Dollar.
     * @param debugParam      as String to change the behaviour of the corresponding microservice.
     * @param affectedService as String to change the affected services
     * @return ApiResult object which holds the returned informations and the error messages to.
     * <p>
     * <p>
     * <p>
     * A Typical response from Request URL: http://localhost/USD/EUR/debug/fixed/av
     * looks like:
     * {
     * "result":
     * {
     * "updated": "2020-08-31T08:39:42.459942",
     * "fromCurrency":"USD",
     * "toCurrency":"EUR",
     * "value":0.8463,
     * "quantity":1.0,
     * "amount":0.8463
     * },
     * "error":null,
     * "status":"OK",
     * "source":"Alphavantage"
     * }
     */
    @Path("/{fromCurrency}/{toCurrency}/debug/{debugParam}/{service}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResult getRateWithoutQuantityWithDebugAndService(@PathParam("fromCurrency") String fromCurrency,
                                                               @PathParam("toCurrency") String toCurrency,
                                                               @PathParam("debugParam") String debugParam,
                                                               @PathParam("service") String affectedService) {
        return getRateWithDebugAndService(fromCurrency, toCurrency, quantityDefault, debugParam, affectedService);
    }

    /**
     * This Method is used if someone uses the URL with tree parameters and a debug parameter to like:
     * <server_ip_address>/{from_name}/{to_name}/{quantity}/debug/{debugParam} as example:
     * localhost/EUR/USD/12.51/debug/noresponse
     *
     * @param fromCurrency as String which holds UPPER-CASE currency short 3-CHAR expression EUR - EURO or USD for US.Dollar.
     * @param toCurrency   as String holds UPPER-CASE currency short 3-CHAR expression GBP - Pounds or USD for US.Dollar.
     * @param quantity     as Double to calculate the amount by multiplying the value times the quantity.
     * @param debugParam   as String defined in DebugParameters Enum
     * @return ApiResult object which holds the returned informations and the error messages to.
     * <p>
     * <p>
     * <p>
     * A Typical response from Request URL: http://localhost/USD/EUR/10.0/debug/random
     * looks like:
     * {
     * "result":
     * {
     * "updated": "2020-08-31T08:39:42.459942",
     * "fromCurrency":"USD",
     * "toCurrency":"EUR",
     * "value":0.8463,
     * "quantity":10.0,
     * "amount":8.4631
     * },
     * "error":null,
     * "status":"OK",
     * "source":"Alphavantage"
     * }
     */

    @Path("/{fromCurrency}/{toCurrency}/{quantity}/debug/{debugParam}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResult getRateWithDebug(@PathParam("fromCurrency") String fromCurrency,
                                      @PathParam("toCurrency") String toCurrency,
                                      @PathParam("quantity") String quantity,
                                      @PathParam("debugParam") String debugParam
    ) {
        return getRateWithDebugAndService(fromCurrency, toCurrency, quantity, debugParam, affectedServiceDefault);
    }

    /**
     * This Method is used if someone uses the URL with tree parameters and a debug parameter to like:
     * <server_ip_address>/{from_name}/{to_name}/{quantity}/debug/{debugParam}/{service} as example:
     * localhost/EUR/USD/12.51/3.0/debug/noresponse/av
     *
     * @param fromCurrency as String which holds UPPER-CASE currency short 3-CHAR expression EUR - EURO or USD for US.Dollar.
     * @param toCurrency   as String holds UPPER-CASE currency short 3-CHAR expression GBP - Pounds or USD for US.Dollar.
     * @param quantity     as Double to calculate the amount by multiplying the value times the quantity.
     * @param debugParam   as String defined in DebugParameters Enum
     * @param service      as String defined in AffectedService Enum - represents to what microservice the debugParam is respected
     * @return ApiResult object which holds the returned informations and the error messages to.
     * <p>
     * <p>
     * <p>
     * A Typical response from Request URL: http://localhost/USD/EUR/10.0/debug/random/av
     * looks like:
     * {
     * "result":
     * {
     * "updated": "2020-08-31T08:39:42.459942",
     * "fromCurrency":"USD",
     * "toCurrency":"EUR",
     * "value":0.8463,
     * "quantity":10.0,
     * "amount":8.4631
     * },
     * "error":null,
     * "status":"OK",
     * "source":"Alphavantage"
     * }
     */

    @Path("/{fromCurrency}/{toCurrency}/{quantity}/debug/{debugParam}/{service}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResult getRateWithDebugAndService(@PathParam("fromCurrency") String fromCurrency,
                                                @PathParam("toCurrency") String toCurrency,
                                                @PathParam("quantity") String quantity,
                                                @PathParam("debugParam") String debugParam,
                                                @PathParam("service") String service
    ) {
        ApiResult apiResult;

        try {
            Double quantityValue = Double.parseDouble(quantity);
            DebugParameter debugParameter = DebugParameter.valueOf(debugParam);
            AffectedService affectedService = AffectedService.valueOf(service);

            apiResult = getRateWithDebugAlphavantage(fromCurrency, toCurrency, quantityValue, debugParameter, affectedService);
            if (ErrorHandler.shouldForwardToNextService(apiResult)) {
                apiResult = getRateWithDebugWahrungsrechner(fromCurrency, toCurrency, quantityValue, debugParameter, affectedService);
            }
        } catch (Exception e) {
            apiResult = ResponseBuilder.buildParameterError();
        }
        return apiResult;
    }

    // region Private API

    private ApiResult getRateAlphavantage(String fromCurrency, String toCurrency, Double quantity) {
        ApiResult apiResult;
        System.out.println("Getting rate from Alphavantage.");

        try {
            ApiResult serviceApiResult = currencyAlphavantageEndpointService.getExchangeRate(fromCurrency, toCurrency, quantity);
            Error error = ErrorHandler.getErrorForApiResult(serviceApiResult);
            apiResult = ResponseBuilder.buildResponse(serviceApiResult, error);
        } catch (ProcessingException e) {
            apiResult = ResponseBuilder.buildNotReachableResponse();
        } catch (Exception e) {
            System.out.println("Following Exception was thrown: " + e.getClass());
            apiResult = ResponseBuilder.buildExceptionResponse(e);
        }

        return apiResult;
    }

    private ApiResult getRateWahrungsrechner(String fromCurrency, String toCurrency, Double quantity) {
        ApiResult apiResult;
        System.out.println("Getting rate from Wahrungsrechner.");

        try {
            ApiResult serviceApiResult = currencyWahrungsrechnerEndpointService.getExchangeRate(fromCurrency, toCurrency, quantity);
            Error error = ErrorHandler.getErrorForApiResult(serviceApiResult);
            apiResult = ResponseBuilder.buildResponse(serviceApiResult, error);
        } catch (ProcessingException e) {
            apiResult = ResponseBuilder.buildNotReachableResponse();
        } catch (Exception e) {
            System.out.println("Following Exception was thrown: " + e.getClass());
            apiResult = ResponseBuilder.buildExceptionResponse(e);
        }

        return apiResult;
    }

    private ApiResult getRateWithDebugAlphavantage(String fromCurrency, String toCurrency, Double quantity, DebugParameter debugParam, AffectedService affectedService) {
        ApiResult apiResult;
        System.out.println("Getting rate with debug from Alphavantage.");

        try {
            ApiResult serviceApiResult;
            if (affectedService != null && affectedService != AffectedService.wr) {
                serviceApiResult = currencyAlphavantageEndpointService.getExchangeRateDebug(fromCurrency, toCurrency, quantity, debugParam.name());
            } else {
                serviceApiResult = currencyAlphavantageEndpointService.getExchangeRate(fromCurrency, toCurrency, quantity);
            }

            Error error = ErrorHandler.getErrorForApiResult(serviceApiResult);
            apiResult = ResponseBuilder.buildResponse(serviceApiResult, error);
        } catch (ProcessingException e) {
            apiResult = ResponseBuilder.buildNotReachableResponse();
        } catch (Exception e) {
            System.out.println("Following Exception was thrown: " + e.getClass());
            apiResult = ResponseBuilder.buildExceptionResponse(e);
        }

        return apiResult;
    }

    private ApiResult getRateWithDebugWahrungsrechner(String fromCurrency, String toCurrency, Double quantity, DebugParameter debugParam, AffectedService affectedService) {
        ApiResult apiResult;
        System.out.println("Getting rate with debug from Wahrungsrechner.");

        try {
            ApiResult serviceApiResult;
            if (affectedService != null && affectedService != AffectedService.av) {
                serviceApiResult = currencyWahrungsrechnerEndpointService.getExchangeRateDebug(fromCurrency, toCurrency, quantity, debugParam.name());
            } else {
                serviceApiResult = currencyWahrungsrechnerEndpointService.getExchangeRate(fromCurrency, toCurrency, quantity);
            }

            Error error = ErrorHandler.getErrorForApiResult(serviceApiResult);
            apiResult = ResponseBuilder.buildResponse(serviceApiResult, error);
        } catch (ProcessingException e) {
            apiResult = ResponseBuilder.buildNotReachableResponse();
        } catch (Exception e) {
            System.out.println("Following Exception was thrown: " + e.getClass());
            apiResult = ResponseBuilder.buildExceptionResponse(e);
        }

        return apiResult;
    }

    // endregion
}