package com.molean.velocityskinloader.client;

import com.molean.velocityskinloader.model.mojang.MojangSkin;
import com.molean.velocityskinloader.model.mojang.UUIDProfile;

import java.net.http.HttpResponse;

public class MojangClient extends ApiClient {

    private static final MojangClient instance = new MojangClient();

    public static MojangClient instance() {
        return instance;
    }

    private MojangClient() {
        super("https://");
    }

    public UUIDProfile getUUIDByName(String name) throws Exception {
        HttpResponse<String> response = get("api.mojang.com/users/profiles/minecraft/" + name);
        String body = response.body();
        return gson.fromJson(body, UUIDProfile.class);
    }

    public MojangSkin getSkinByUUIDProfile(UUIDProfile uuidProfile) throws Exception {
        HttpResponse<String> response = get("sessionserver.mojang.com/session/minecraft/profile/" + uuidProfile.getId());
        String body = response.body();
        return gson.fromJson(body, MojangSkin.class);
    }
}
