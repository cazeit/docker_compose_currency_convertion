package org.acme.de.fhe.kba2020;
//TODO: finish docu

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbPropertyOrder;
import java.io.Serializable;

@JsonbPropertyOrder({"code", "message"})
public class AlphavantageError implements Serializable {
    private int code = 0;
    private String message = "No Error!";

    public AlphavantageError() {
    }

    public AlphavantageError(int code, String message) {
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
}
