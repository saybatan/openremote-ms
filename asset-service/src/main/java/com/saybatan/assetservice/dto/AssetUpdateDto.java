package com.saybatan.assetservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class AssetUpdateDto {

    private String name;
    private Boolean accessPublicRead;
    private Map<String, Object> attributes;

}
