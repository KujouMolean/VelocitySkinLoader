package com.molean.velocityskinloader.cache;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.velocitypowered.api.util.GameProfile;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("UnstableApiUsage")
public class KnownSkinCache implements Cache<String, GameProfile.Property> {

    private static final KnownSkinCache instance = new KnownSkinCache();
    private Map<String, GameProfile.Property> payload = new HashMap<>();


    public static KnownSkinCache knownSkinCache() {
        return instance;
    }

    @Override
    public @NotNull Map<String, GameProfile.Property> payload() {
        return payload;
    }

    @Override
    public void payload(Map<String, GameProfile.Property> payload) {
        this.payload = payload;
    }

    public String getHash(byte[] bytes) {
        return Hashing.sha256().hashBytes(bytes).toString();
    }

}
