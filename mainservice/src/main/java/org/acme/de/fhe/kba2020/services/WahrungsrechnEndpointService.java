package org.acme.de.fhe.kba2020.services;


import org.acme.de.fhe.kba2020.model.ApiResult;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/convertion")
@RegisterRestClient(configKey = "currency-wahrungsrechner-service")
@Produces(MediaType.APPLICATION_JSON)
public interface WahrungsrechnEndpointService {
    @Path("/{fromCurrency}/{toCurrency}/{quantity}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    ApiResult getExchangeRate(@PathParam("fromCurrency") String fromCurrency,
                              @PathParam("toCurrency") String toCurrency,
                              @PathParam("quantity") Double quantity
    );

    @Path("/{fromCurrency}/{toCurrency}/{quantity}/debug/{debugParam}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    ApiResult getExchangeRateDebug(@PathParam("fromCurrency") String fromCurrency,
                                   @PathParam("toCurrency") String toCurrency,
                                   @PathParam("quantity") Double quantity,
                                   @PathParam("debugParam") String debugParam
    );
}
