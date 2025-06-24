package com.saybatan.assetservice.controller;

import com.saybatan.assetservice.dto.AssetDto;
import com.saybatan.assetservice.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;

    @GetMapping("/{id}")
    public ResponseEntity<String> getAsset(@PathVariable String id) {
        String asset = assetService.getAssetById(id);
        System.out.println("asset => " + asset);
        return ResponseEntity.ok(asset);
    }

    @PostMapping
    public ResponseEntity<String> createAsset(@RequestBody AssetDto assetDto) {
        return assetService.createAsset(assetDto);
    }
}
