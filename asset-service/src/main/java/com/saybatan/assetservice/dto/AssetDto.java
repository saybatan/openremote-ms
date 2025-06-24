package com.saybatan.assetservice.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AssetDto {

    private String name;
    private boolean accessPublicRead;
    private String realm;
    private String type;
    private Map<String, Object> attributes;
}
