package com.saybatan.assetservice.service;


import com.saybatan.assetservice.dto.AssetDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final RestTemplate restTemplate;
    private final AuthClient authClient;

    @Value("${openremote.asset-url}")
    private String assetUrl;

    public ResponseEntity<String> createAsset(AssetDto assetDto) {
        String token = authClient.getToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<AssetDto> request = new HttpEntity<>(assetDto, headers);

        return restTemplate.postForEntity(assetUrl, request, String.class);
    }

    public String getAssetById(String id) {
        String token = authClient.getToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = assetUrl + "/" + id;

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        return response.getBody();
    }

}
