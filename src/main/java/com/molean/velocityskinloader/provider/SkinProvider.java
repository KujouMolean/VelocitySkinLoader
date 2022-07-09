package com.molean.velocityskinloader.provider;

import com.velocitypowered.api.util.GameProfile;

public interface SkinProvider {
    GameProfile.Property getProperty(String name);

    String getDisplay();
}
