package com.molean.velocityskinloader.provider;

import com.molean.velocityskinloader.cache.KnownSkinCache;
import com.molean.velocityskinloader.config.SkinProviderConfig;
import com.molean.velocityskinloader.client.BlessingSkinClient;
import com.molean.velocityskinloader.client.MineSkinClient;
import com.molean.velocityskinloader.model.blessingskin.BlessingSkinProfile;
import com.molean.velocityskinloader.model.mineskin.SkinInfo;
import com.molean.velocityskinloader.model.mineskin.TextureInfo;
import com.velocitypowered.api.util.GameProfile;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BlessingSkinProvider implements SkinProvider{
    private static Map<String, BlessingSkinProvider> blessingSkinProviderMap = new ConcurrentHashMap<>();

    private final BlessingSkinClient blessingSkinClient;

    public static BlessingSkinProvider of(SkinProviderConfig skinProviderConfig) {
        String url = skinProviderConfig.getUrl();
        BlessingSkinClient blessingSkinClient = BlessingSkinClient.of(url);
        return new BlessingSkinProvider(blessingSkinClient);
    }

    private BlessingSkinProvider(BlessingSkinClient blessingSkinClient) {
        this.blessingSkinClient = blessingSkinClient;
    }


    @Override
    public GameProfile.Property getProperty(String name) {
        try {
            BlessingSkinProfile profileByName = blessingSkinClient.getProfileByName(name);
            if (!profileByName.getSkins().isAvailable()) {
                return null;
            }
            byte[] skinTexturePngAsBytes = blessingSkinClient.getSkinTexturePngAsBytes(profileByName.getSkins().getAvailable());
            String hash = KnownSkinCache.knownSkinCache().getHash(skinTexturePngAsBytes);
            if (KnownSkinCache.knownSkinCache().contains(hash)) {
                return KnownSkinCache.knownSkinCache().get(hash);
            }
            SkinInfo skinInfo = MineSkinClient.instance().generateByUpload(null, null, null, skinTexturePngAsBytes);
            TextureInfo texture = skinInfo.getData().getTexture();
            String signature = texture.getSignature();
            String value = texture.getValue();
            GameProfile.Property textures = new GameProfile.Property("textures", value, signature);
            KnownSkinCache.knownSkinCache().set(hash, textures);
            return textures;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public String getDisplay() {
        return blessingSkinClient.getBase();
    }
}
