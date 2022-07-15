/*
 * ErrorHandler
 *
 * Version 1.0
 *
 * Copyright (c) 2020 Fh Erfurt
 * by C. Zeitler and S. Golchert
 */
package org.acme.de.fhe.kba2020.helper;

import org.acme.de.fhe.kba2020.model.ApiResult;
import org.acme.de.fhe.kba2020.model.Error;
import org.acme.de.fhe.kba2020.model.Status;

import java.util.ArrayList;

/**
 * The Error Handler class implements the logic to recognize errors which can happen during the REST-API-Calls.
 * Typically we build up the most common errors, which will happen during the prototype phase, later we will use more error cases.
 * Actual the Alphavantage Api Service handles the following errors:
 * <p>
 * 1. Wrong result is incoming,
 * 2. Wrong debug message is incoming
 *
 * @author Sebastian Golchert, Carl Zeitler
 * @version 1.0
 */
public class ErrorHandler {
    /**
     * This List contains Error Objects
     */
    private final static ArrayList<Error> errorList = createErrorList();

    /**
     * This method will initialize the errorList field var with the needed error cases.
     *
     * @return
     */
    private static ArrayList<Error> createErrorList() {

        ArrayList<Error> retVal = new ArrayList<Error>();
        retVal.add(new Error(66, "Response could not be processed!"));
        retVal.add(new Error(999, "Connection failed or aborted!"));
        retVal.add(new Error(1333, "An Exception was caught: "));
        retVal.add(new Error(1999, "Invalid parameters passed!"));
        retVal.add(new Error(13, "Page not found. Check your parameters!"));

        return retVal;
    }

    public static Error invalidParametersPassed() {
        return errorList.get(3);
    }

    public static Error connectionFailed() {
        return errorList.get(1);
    }

    public static Error responseProcessFailed() {
        return errorList.get(0);
    }

    public static Error pageNotFound() {
        return errorList.get(4);
    }

    public static Error debugError(String errorMessage) {
        Error debugError = errorList.get(2);
        String debugMessage = debugError.getMessage();
        debugMessage += errorMessage;
        debugError.setMessage(debugMessage);
        return debugError;
    }

    public static Error getErrorForApiResult(ApiResult apiResult) {
        Error occurringError = null;
        if (apiResult.getResult() == null || apiResult.getResult().toString().isEmpty()) {
            apiResult.setStatus(Status.FAIL);
            occurringError = responseProcessFailed();
        }

        return occurringError;
    }

    /**
     * This method determines if the apiResult is valid for being sent back or if the other API should be called.
     * Capsuled to method for excluding error-codes (initially parameter-error was returning false)
     *
     * @param apiResult ApiResult that needs to be checked
     * @return Boolean value if the apiResult is valid for being sent back to client or if the other API should be called
     */
    public static Boolean shouldForwardToNextService(ApiResult apiResult) {
        return apiResult.getError() != null;
    }
}