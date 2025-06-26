package com.saybatan.assetservice;

import com.saybatan.assetservice.dto.AssetDto;
import com.saybatan.assetservice.dto.AssetUpdateDto;
import com.saybatan.assetservice.service.AssetService;
import com.saybatan.assetservice.service.AuthClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssetServiceApplicationTests {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private AuthClient authClient;

    @InjectMocks
    private AssetService assetService;

    private final String assetUrl = "http://test-asset-url";
    private final String testToken = "test-token";
    private final String testAssetId = "testAsset123";
    private final String testAssetJson = "{\"id\":\"testAsset123\",\"name\":\"Test Asset\"}";

    @BeforeEach
    void setUp() {
        assetService = new AssetService(restTemplate, authClient);
        assetService.setAssetUrl(assetUrl);
    }

    @Test
    void getAssetById_ShouldReturnAsset_WhenAssetExists() {
        when(authClient.getToken()).thenReturn(testToken);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(testToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(testAssetJson, HttpStatus.OK);
        when(restTemplate.exchange(
                eq(assetUrl + "/" + testAssetId),
                eq(HttpMethod.GET),
                eq(entity),
                eq(String.class)
        )).thenReturn(responseEntity);

        String result = assetService.getAssetById(testAssetId);

        assertEquals(testAssetJson, result);
        verify(authClient).getToken();
        verify(restTemplate).exchange(
                eq(assetUrl + "/" + testAssetId),
                eq(HttpMethod.GET),
                eq(entity),
                eq(String.class)
        );
    }

    @Test
    void createAsset_ShouldReturnCreatedAsset_WhenValidInput() {
        AssetDto assetDto = new AssetDto();
        assetDto.setName("New Asset");
        assetDto.setAccessPublicRead(true);
        assetDto.setRealm("testRealm");
        assetDto.setType("testType");

        when(authClient.getToken()).thenReturn(testToken);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(testToken);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(testAssetJson, HttpStatus.CREATED);
        when(restTemplate.postForEntity(
                eq(assetUrl),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(responseEntity);

        ResponseEntity<String> result = assetService.createAsset(assetDto);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(testAssetJson, result.getBody());
        verify(authClient).getToken();
        verify(restTemplate).postForEntity(
                eq(assetUrl),
                any(HttpEntity.class),
                eq(String.class)
        );
    }

    @Test
    void updateAsset_ShouldThrowException_WhenAssetNotFound() {
        AssetUpdateDto updateDto = new AssetUpdateDto();
        updateDto.setName("Updated Name");

        when(authClient.getToken()).thenReturn(testToken);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(testToken);
        HttpEntity<Void> getRequest = new HttpEntity<>(headers);

        ResponseEntity<Map> getResponse = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        when(restTemplate.exchange(
                eq(assetUrl + "/" + testAssetId),
                eq(HttpMethod.GET),
                eq(getRequest),
                eq(Map.class)
        )).thenReturn(getResponse);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> assetService.updateAsset(testAssetId, updateDto));

        assertEquals("Asset not found with id: " + testAssetId, exception.getMessage());
        verify(authClient).getToken();
        verify(restTemplate).exchange(
                eq(assetUrl + "/" + testAssetId),
                eq(HttpMethod.GET),
                eq(getRequest),
                eq(Map.class)
        );
    }

    @Test
    void deleteAsset_ShouldDeleteAsset_WhenAssetExists() {
        when(authClient.getToken()).thenReturn(testToken);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(testToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        when(restTemplate.exchange(
                eq(assetUrl + "?assetId=" + testAssetId),
                eq(HttpMethod.DELETE),
                eq(request),
                eq(Void.class)
        )).thenReturn(response);

        assetService.deleteAsset(testAssetId);

        verify(authClient).getToken();
        verify(restTemplate).exchange(
                eq(assetUrl + "?assetId=" + testAssetId),
                eq(HttpMethod.DELETE),
                eq(request),
                eq(Void.class)
        );
    }
}