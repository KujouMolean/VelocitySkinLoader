package com.molean.velocityskinloader.model.blessingskin;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Skins {
    @SerializedName("default")
    private String classic;
    private String slim;

    public boolean isAvailable() {
        return classic != null || slim != null;

    }
    public String getAvailable() {
        return isAvailable() ? (classic == null ? slim : classic) : null;
    }
}
