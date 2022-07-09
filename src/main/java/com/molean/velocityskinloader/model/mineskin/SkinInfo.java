package com.molean.velocityskinloader.model.mineskin;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class SkinInfo {
    private long id;
    private String idStr;
    private String uuid;
    private String name;
    private String variant;
    private SkinData data;
    private Long timestamp;
    private Long duration;
    private Long account;
    private String server;
    @SerializedName("private")
    private boolean privateSkin;
    private long views;


}
