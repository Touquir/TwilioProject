package com.twilio.javaproject.Controller;

import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Map;

@RestController
public class MyController {

    public static final String ACCOUNT_SID = "";
    public static final String AUTH_TOKEN = "";
    public static final String SERVICE_SID = "";

    @PostMapping(path = "/authorize", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String sendOTP(@RequestParam MultiValueMap<String,String> paramMap) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost("https://verify.twilio.com/v2/Services/" + SERVICE_SID + "/Verifications");

        ArrayList<NameValuePair> list = new ArrayList<>();
        Map<String, String> mappp = paramMap.toSingleValueMap();
        mappp.forEach((a, b)-> {
            list.add(new BasicNameValuePair(a,b));
        });

        httppost.addHeader("Authorization", this.getBasicAuthenticationHeader(ACCOUNT_SID, AUTH_TOKEN));
        httppost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        httppost.setEntity(new UrlEncodedFormEntity(list));

        HttpResponse resp = httpclient.execute(httppost);
        return EntityUtils.toString(resp.getEntity());
    }

    @PostMapping(path = "/authorize2", consumes = "application/json")
    public String sendOTP2(@RequestBody Object body) throws IOException {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Verification verification = Verification.creator(SERVICE_SID,"+9188746355", "sms").create();
        System.out.println(verification.getSid());
        return "sent to client";
    }

    @PostMapping("/validate")
    public String validateOTP() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        VerificationCheck verificationCheck = VerificationCheck.creator(
                        SERVICE_SID)
                .setTo("+9188746355")
                .setCode("543136")
                .create();
        System.out.println(verificationCheck.getStatus());
        return "verified";
    }

    private String getBasicAuthenticationHeader(String username, String password) {
        String valueToEncode = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
    }
}