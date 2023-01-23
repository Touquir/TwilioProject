package com.twilio.javaproject.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.javaproject.Models.AuthRequest;
import com.twilio.javaproject.Models.ApiResponse;
import com.twilio.javaproject.Models.ValidateRequest;
import com.twilio.javaproject.Models.ValidateResponse;

import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@RestController
public class MyController {

    public static final String ACCOUNT_SID = "";
    public static final String AUTH_TOKEN = "";
    public static final String SERVICE_SID = "";

    @PostMapping("/authorize")
    public ResponseEntity<ApiResponse> sendOTP(@RequestBody AuthRequest requestBody) {
        ApiResponse response = new ApiResponse();
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://verify.twilio.com/v2/Services/" + SERVICE_SID + "/Verifications";
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("To", requestBody.getTo());
        map.add("Channel", requestBody.getChannel());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", this.getBasicAuthenticationHeader());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        try {
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map,headers);
            restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            response.setMessage("OTP sent successfully");
            response.setStatusCode(200);
        } catch (HttpClientErrorException ex) {
            response.setMessage(ex.getMessage());
            response.setStatusCode(ex.getStatusCode().value());
        } catch (Exception ex) {
            response.setMessage("OTP send failure");
            response.setStatusCode(500);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/validate")
    public ResponseEntity<ApiResponse> validateOTP(@RequestBody ValidateRequest requestBody) {
        ApiResponse response = new ApiResponse();
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();

        String url = "https://verify.twilio.com/v2/Services/" + SERVICE_SID + "/VerificationCheck";
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("To", requestBody.getTo());
        map.add("Code", requestBody.getCode());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", this.getBasicAuthenticationHeader());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        try {
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map,headers);
            ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            ValidateResponse validateResponse = mapper.readValue(resp.getBody(), ValidateResponse.class);

            if(validateResponse.isValid()) {
                response.setMessage("OTP validated successfully");
                response.setStatusCode(200);
            } else {
                response.setMessage("OTP did not match");
                response.setStatusCode(400);
            }
        } catch (HttpClientErrorException ex) {
            response.setMessage(ex.getMessage());
            response.setStatusCode(ex.getStatusCode().value());
        } catch (Exception ex) {
            response.setMessage("OTP validate failure");
            response.setStatusCode(500);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private String getBasicAuthenticationHeader() {
        String valueToEncode = MyController.ACCOUNT_SID + ":" + MyController.AUTH_TOKEN;
        return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
    }

//    @PostMapping(path = "/authorize2", consumes = "application/json")
//    public String sendOTP2(@RequestBody Object body) throws IOException {
//        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//        Verification verification = Verification.creator(SERVICE_SID, "+9188746355", "sms").create();
//        System.out.println(verification.getSid());
//        return "sent to client";
//    }
//
//    @PostMapping("/validate2")
//    public String validateOTP() {
//        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//        VerificationCheck verificationCheck = VerificationCheck.creator(
//                        SERVICE_SID)
//                .setTo("+9188746355")
//                .setCode("543136")
//                .create();
//        System.out.println(verificationCheck.getStatus());
//        return "verified";
//    }
}