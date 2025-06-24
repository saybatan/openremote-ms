package com.saybatan.authservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${openremote.auth-url}")
    private String authUrl;

    @Value("${openremote.client-id}")
    private String clientId;

    @Value("${openremote.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;

    private String accessToken;
    private long expiryTime;

    public synchronized String getAccessToken() {
        if (accessToken == null || Instant.now().isAfter(Instant.ofEpochMilli(expiryTime))) {
            fetchAccessToken();
        }
        return accessToken;
    }

    private void fetchAccessToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(authUrl, request, Map.class);
        Map<String, Object> responseBody = response.getBody();

        this.accessToken = (String) responseBody.get("access_token");
        this.expiryTime = Instant.now().plusSeconds((Integer) responseBody.get("expires_in") - 60).toEpochMilli();
    }
}
