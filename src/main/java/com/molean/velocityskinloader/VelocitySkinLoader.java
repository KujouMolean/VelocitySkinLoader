package com.molean.velocityskinloader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.molean.velocityskinloader.cache.KnownSkinCache;
import com.molean.velocityskinloader.cache.PlayerSkinCache;
import com.molean.velocityskinloader.config.Config;
import com.molean.velocityskinloader.listener.SkinApply;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Plugin(
        id = "velocity_skin_loader",
        name = "VelocitySkinLoader",
        version = "1.0-SNAPSHOT",
        description = "Velocity server side custom skin loader.",
        authors = {"Molean"}
)
public class VelocitySkinLoader {

    private final Logger logger;

    private final ProxyServer proxyServer;

    private final Path dataDirectory;

    private final File configFile;
    private final File knownSkins;
    private final File playerSkins;


    private final Gson gson;

    @Inject
    public VelocitySkinLoader(Logger logger, ProxyServer proxyServer, @DataDirectory Path dataDirectory) throws IOException {
        this.logger = logger;
        this.proxyServer = proxyServer;
        this.dataDirectory = dataDirectory;
        this.configFile = new File(dataDirectory + "/config.json");
        this.knownSkins = new File(dataDirectory + "/KnownSkins.json");
        this.playerSkins = new File(dataDirectory + "/PlayerSkins.json");
        gson = new GsonBuilder().setPrettyPrinting().create();
    }


    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) throws IOException {
        if (!dataDirectory.toFile().exists() && !dataDirectory.toFile().mkdirs()) throw new AssertionError();
        if (!configFile.exists() && !configFile.createNewFile()) throw new AssertionError();
        Config config = Config.load(gson, configFile);
        config.save(gson, configFile);
        if (!knownSkins.exists() && !knownSkins.createNewFile()) throw new AssertionError();
        KnownSkinCache.knownSkinCache().load(gson, knownSkins);
        KnownSkinCache.knownSkinCache().save(gson, knownSkins);
        if (!playerSkins.exists() && !playerSkins.createNewFile()) throw new AssertionError();
        PlayerSkinCache.playerSkinCache().load(gson, playerSkins);
        PlayerSkinCache.playerSkinCache().save(gson, playerSkins);
        proxyServer.getEventManager().register(this, new SkinApply(config, proxyServer, this));
    }

    @Subscribe
    public void savePlayerSkinCache(ProxyShutdownEvent event) throws IOException {
        PlayerSkinCache.playerSkinCache().save(gson, playerSkins);
    }

    @Subscribe
    public void saveKnownSkins(ProxyShutdownEvent event) throws IOException {
        KnownSkinCache.knownSkinCache().save(gson, playerSkins);
    }


}

