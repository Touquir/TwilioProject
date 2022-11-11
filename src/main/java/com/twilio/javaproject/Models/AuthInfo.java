package com.twilio.javaproject.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthInfo {
    @JsonProperty("To")
    private String to;
    @JsonProperty("Channel")
    private String channel;
    @JsonProperty("CustomFriendlyName")
    private String customFriendlyName;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getCustomFriendlyName() {
        return customFriendlyName;
    }

    public void setCustomFriendlyName(String customFriendlyName) {
        this.customFriendlyName = customFriendlyName;
    }
}
