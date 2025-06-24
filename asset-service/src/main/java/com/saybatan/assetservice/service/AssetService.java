package com.saybatan.assetservice.service;

import com.saybatan.assetservice.dto.AssetDto;
import com.saybatan.assetservice.utils.Base62UuidGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final RestTemplate restTemplate;
    private final AuthClient authClient;

    @Value("${openremote.asset-url}")
    private String assetUrl;

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

    public ResponseEntity<String> createAsset(AssetDto assetDto) {
        String token = authClient.getToken();

        // Otomatik alanlar
        String generatedId = Base62UuidGenerator.generateBase62Uuid();
        long createdOn = System.currentTimeMillis();

        Map<String, Object> fullPayload = new HashMap<>();
        fullPayload.put("id", generatedId);
        fullPayload.put("version", 0);
        fullPayload.put("createdOn", createdOn);
        fullPayload.put("name", assetDto.getName());
        fullPayload.put("accessPublicRead", assetDto.isAccessPublicRead());
        fullPayload.put("realm", assetDto.getRealm());
        fullPayload.put("type", assetDto.getType());
        fullPayload.put("path", List.of(generatedId));
        fullPayload.put("attributes", assetDto.getAttributes() != null ? assetDto.getAttributes() : new HashMap<>());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(fullPayload, headers);

        return restTemplate.postForEntity(assetUrl, request, String.class);
    }

}
