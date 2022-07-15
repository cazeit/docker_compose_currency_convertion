package org.acme.de.fhe.kba2020.helper;

import org.acme.de.fhe.kba2020.model.ApiResult;
import org.acme.de.fhe.kba2020.model.Error;
import org.acme.de.fhe.kba2020.model.Status;

public class ResponseBuilder {
    public static ApiResult buildResponse(ApiResult apiResult, Error error) {
        if (error == null) {
            return apiResult;
        } else {
            Error responseError = ErrorHandler.responseProcessFailed();
            return new ApiResult(null, responseError, Status.FAIL);
        }
    }

    public static ApiResult buildExceptionResponse(Exception exception) {
        Error debugError = ErrorHandler.debugError(exception.getMessage());
        return new ApiResult(null, debugError, Status.FAIL);
    }

    public static ApiResult buildNotReachableResponse() {
        Error notReachableError = ErrorHandler.connectionFailed();
        return new ApiResult(null, notReachableError, Status.FAIL);
    }

    public static ApiResult buildParameterError() {
        Error parameterError = ErrorHandler.invalidParametersPassed();
        return new ApiResult(null, parameterError, Status.FAIL);
    }

    public static ApiResult buildPageNotFoundError() {
        Error pageNotFoundError = ErrorHandler.pageNotFound();
        return new ApiResult(null, pageNotFoundError, Status.FAIL);
    }
}
