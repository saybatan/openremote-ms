package com.saybatan.assetservice.utils;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

public class Base62UuidGenerator {

    public static String generateBase62Uuid() {
        UUID uuid = UUID.randomUUID();
        ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());
        return base62Encode(buffer.array());
    }

    private static String base62Encode(byte[] bytes) {
        String encoded = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        return encoded.substring(0, 22);
    }
}
