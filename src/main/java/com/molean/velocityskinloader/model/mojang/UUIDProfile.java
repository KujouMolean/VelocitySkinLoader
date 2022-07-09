package com.molean.velocityskinloader.model.mojang;

import lombok.Data;

import java.util.UUID;

@Data
public class UUIDProfile {
    private String name;
    private String id;/*uuid*/

    public UUID getUUID() {
        return UUID.fromString(id.replaceFirst(
                "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)",
                "$1-$2-$3-$4-$5"));
    }
}
