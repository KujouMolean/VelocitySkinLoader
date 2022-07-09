package com.molean.velocityskinloader.cache;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.velocitypowered.api.util.GameProfile;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface Cache<KEY, VALUE> {
    default boolean contains(KEY key) {
        return payload().containsKey(key);
    }

    @NotNull Map<KEY, VALUE> payload();

    void payload(Map<KEY, VALUE> payload);


    default VALUE get(KEY key) {
        return payload().get(key);
    }

    default void set(KEY key, VALUE value) {
        payload().put(key, value);
    }

    default void save(Gson gson, File file) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            String s = gson.toJson(payload());
            fileOutputStream.write(s.getBytes(StandardCharsets.UTF_8));
        }
    }

    default void load(Gson gson, File file) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] bytes = fileInputStream.readAllBytes();
            Type type = new TypeToken<Map<KEY, VALUE>>() {}.getType();
            Map<KEY,VALUE> map = gson.fromJson(new String(bytes, StandardCharsets.UTF_8), type);
            if (map == null) {
                map = payload();
            }
            this.payload(map);
        }
    }
}
