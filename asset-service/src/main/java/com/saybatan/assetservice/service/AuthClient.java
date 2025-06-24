package com.saybatan.assetservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AuthClient {

    private final RestTemplate restTemplate;

    @Value("${openremote.auth-url}")
    private String authUrl;

    public String getToken() {
        ResponseEntity<String> response = restTemplate.getForEntity(authUrl, String.class);
        return response.getBody();
    }
}
