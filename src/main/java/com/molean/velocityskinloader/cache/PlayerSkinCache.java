package com.molean.velocityskinloader.cache;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.velocitypowered.api.util.GameProfile;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class PlayerSkinCache implements Cache<String, PlayerSkinCache.TimeLimitedSkin> {

    private static final PlayerSkinCache instance = new PlayerSkinCache();

    @Data
    public static class TimeLimitedSkin {
        private Timestamp expireTime;
        private GameProfile.Property property;
    }

    public static PlayerSkinCache playerSkinCache() {
        return instance;
    }

    private Map<String, PlayerSkinCache.TimeLimitedSkin> payload = new HashMap<>();

    @Override
    public @NotNull Map<String, PlayerSkinCache.TimeLimitedSkin> payload() {
        return payload;
    }

    @Override
    public void payload(Map<String, PlayerSkinCache.TimeLimitedSkin> payload) {
        this.payload = payload;
    }

    @Override
    public void load(Gson gson, File file) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] bytes = fileInputStream.readAllBytes();
            Type type = new TypeToken<Map<String, TimeLimitedSkin>>() {}.getType();
            Map<String, TimeLimitedSkin> map = gson.fromJson(new String(bytes, StandardCharsets.UTF_8), type);
            payload = map;
            if (payload == null) {
                payload = new HashMap<>();
            }
        }
    }
}
