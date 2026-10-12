package com.example.expo.Client;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class MoyasarClient {

    private final RestClient restClient;

    public MoyasarClient(
            @Value("${moyasar.secret-key}") String secretKey) {

        this.restClient = RestClient.builder()
                .baseUrl("https://api.moyasar.com/v1")
                .defaultHeaders(headers ->
                        headers.setBasicAuth(secretKey, ""))
                .build();
    }

    public String verifyPayment(String paymentId) {
        return restClient.get()
                .uri("/payments/{paymentId}", paymentId)
                .retrieve()
                .body(String.class);
    }
}