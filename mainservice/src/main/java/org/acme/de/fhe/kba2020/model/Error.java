/*
 * Error
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
 * This class manage the different error messages and the corresponding error code.
 * The Error class is used in the ErrorHandler class.
 *
 * @author Sebastian Golchert, Carl Zeitler
 * @version 1.0
 */
@JsonbPropertyOrder({"code", "message"})
public class Error {
    /**
     * The error code as integer
     */
    private int code = 0;
    /**
     * The error message
     */
    private String message = "No Error!";

    public Error() {
    }

    public Error(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    @JsonbProperty(value = "code")
    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    @JsonbProperty(value = "message")
    public void setMessage(String m) {
        message = m;
    }
};