package com.molean.velocityskinloader.model.mojang;

import com.velocitypowered.api.util.GameProfile;
import lombok.Data;

import java.util.List;

@Data
public class MojangSkin {
    private String id;
    private String name;
    private List<GameProfile.Property> properties;
}
