package com.saybatan.assetservice.service;

import com.saybatan.assetservice.dto.AssetDto;
import com.saybatan.assetservice.dto.AssetUpdateDto;
import com.saybatan.assetservice.utils.Base62UuidGenerator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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

    @Setter
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

        Map<String, Object> fullPayload = getStringObjectMap(assetDto);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(fullPayload, headers);

        return restTemplate.postForEntity(assetUrl, request, String.class);
    }

    private static Map<String, Object> getStringObjectMap(AssetDto assetDto) {
        Map<String, Object> fullPayload = new HashMap<>();
        String generatedId = Base62UuidGenerator.generateBase62Uuid();
        long createdOn = System.currentTimeMillis();
        fullPayload.put("id", generatedId);
        fullPayload.put("version", 0);
        fullPayload.put("createdOn", createdOn);
        fullPayload.put("name", assetDto.getName());
        fullPayload.put("accessPublicRead", assetDto.isAccessPublicRead());
        fullPayload.put("realm", assetDto.getRealm());
        fullPayload.put("type", assetDto.getType());
        fullPayload.put("path", List.of(generatedId));
        fullPayload.put("attributes", assetDto.getAttributes() != null ? assetDto.getAttributes() : new HashMap<>());
        return fullPayload;
    }

    public void updateAsset(String assetId, AssetUpdateDto assetUpdateDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authClient.getToken());

        HttpEntity<Void> getRequest = new HttpEntity<>(headers);
        String getUrl = assetUrl + "/" + assetId;

        ResponseEntity<Map> response = restTemplate.exchange(getUrl, HttpMethod.GET, getRequest, Map.class);
        Map<String, Object> existingAsset = response.getBody();

        if (existingAsset == null) {
            throw new RuntimeException("Asset not found with id: " + assetId);
        }

        updateFields(assetUpdateDto, existingAsset);

        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> putRequest = new HttpEntity<>(existingAsset, headers);
        String putUrl = assetUrl + "/" + assetId;

        restTemplate.exchange(putUrl, HttpMethod.PUT, putRequest, Void.class);
    }

    private static void updateFields(AssetUpdateDto assetUpdateDto, Map<String, Object> existingAsset) {
        if (assetUpdateDto.getName() != null && !assetUpdateDto.getName().isEmpty()) {
            existingAsset.put("name", assetUpdateDto.getName());
        }

        if (assetUpdateDto.getAccessPublicRead() != null) {
            existingAsset.put("accessPublicRead", assetUpdateDto.getAccessPublicRead());
        }

        if (assetUpdateDto.getAttributes() != null) {
            existingAsset.put("attributes", assetUpdateDto.getAttributes());
        }
    }

    public void deleteAsset(String assetId) {
        String accessToken = authClient.getToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        String deleteUrl = assetUrl + "?assetId=" + assetId;
        restTemplate.exchange(deleteUrl, HttpMethod.DELETE, request, Void.class);
    }

}
