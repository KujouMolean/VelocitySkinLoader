package com.molean.velocityskinloader.client;

import com.molean.velocityskinloader.client.ApiClient;
import com.molean.velocityskinloader.model.blessingskin.BlessingSkinProfile;

import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BlessingSkinClient extends ApiClient {

    private static final Map<String, BlessingSkinClient> map = new ConcurrentHashMap<>();

    public static BlessingSkinClient of(String url) {
        return map.computeIfAbsent(url, BlessingSkinClient::new);
    }

    private BlessingSkinClient(String base) {
        super(base);
    }

    public BlessingSkinProfile getProfileByName(String name) throws Exception {
        HttpResponse<String> response = get("/" + name + ".json");
        String body = response.body();
        return gson.fromJson(body, BlessingSkinProfile.class);
    }

    public byte[] getSkinTexturePngAsBytes(String texture) throws Exception {
        HttpResponse<byte[]> bytes = getBytes("/textures/" + texture);
        return bytes.body();
    }
}
