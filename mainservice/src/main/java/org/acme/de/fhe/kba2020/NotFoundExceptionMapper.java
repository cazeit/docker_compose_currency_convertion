package org.acme.de.fhe.kba2020;

import org.acme.de.fhe.kba2020.helper.ResponseBuilder;
import org.acme.de.fhe.kba2020.model.ApiResult;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

// overriding page not found error :)
@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {
    @Override
    public Response toResponse(NotFoundException e) {
        ApiResult apiResult = ResponseBuilder.buildPageNotFoundError();
        return Response.status(404).entity(apiResult).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}
