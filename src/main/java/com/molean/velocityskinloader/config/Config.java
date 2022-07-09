package com.molean.velocityskinloader.config;

import com.google.gson.Gson;
import lombok.Data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.SortedSet;
import java.util.TreeSet;

@Data
public class Config {
    private GeneralConfig generalConfig = new GeneralConfig();
    private SortedSet<SkinProviderConfig> skinProviderList = initialList();


    public static SortedSet<SkinProviderConfig> initialList() {
        TreeSet<SkinProviderConfig> skinProviderConfigs = new TreeSet<>();
        SkinProviderConfig blessing = new SkinProviderConfig();
        blessing.setType("BlessingSkin");
        blessing.setUrl("https://skin.prinzeugen.net/");
        blessing.setPriority(100);

        SkinProviderConfig little = new SkinProviderConfig();
        little.setType("BlessingSkin");
        little.setUrl("https://littlesk.in/");
        little.setPriority(99);

        SkinProviderConfig official = new SkinProviderConfig();
        official.setType("Official");
        official.setPriority(0);
        official.setUrl(null);

        skinProviderConfigs.add(blessing);
        skinProviderConfigs.add(little);
        skinProviderConfigs.add(official);
        return skinProviderConfigs;
    }

    public void save(Gson gson, File file) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            String s = gson.toJson(this);
            fileOutputStream.write(s.getBytes(StandardCharsets.UTF_8));
        }
    }

    public static Config load(Gson gson, File file) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] bytes = fileInputStream.readAllBytes();
            Config config = gson.fromJson(new String(bytes, StandardCharsets.UTF_8), Config.class);
            if (config == null) {
                config = new Config();
            }
            return config;
        }
    }
}
