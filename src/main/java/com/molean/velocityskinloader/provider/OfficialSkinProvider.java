package com.molean.velocityskinloader.provider;

import com.molean.velocityskinloader.client.MojangClient;
import com.molean.velocityskinloader.model.mojang.MojangSkin;
import com.molean.velocityskinloader.model.mojang.UUIDProfile;
import com.molean.velocityskinloader.provider.SkinProvider;
import com.velocitypowered.api.util.GameProfile;

import java.util.List;
import java.util.UUID;

public class OfficialSkinProvider implements SkinProvider {
    private static OfficialSkinProvider instance = new OfficialSkinProvider();

    public static OfficialSkinProvider instance() {
        return instance;
    }

    private OfficialSkinProvider() {

    }

    @Override
    public GameProfile.Property getProperty(String name) {
        try {
            UUIDProfile uuidByName = MojangClient.instance().getUUIDByName(name);
            MojangSkin skinByUUIDProfile = MojangClient.instance().getSkinByUUIDProfile(uuidByName);
            List<GameProfile.Property> propertyList = skinByUUIDProfile.getProperties();
            return propertyList.size() > 0 ? propertyList.get(0) : null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getDisplay() {
        return "Official";
    }
}
