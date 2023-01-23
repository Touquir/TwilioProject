package com.twilio.javaproject.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ValidateRequest {
    private String to;
    private String code;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
