package com.molean.velocityskinloader.listener;

import com.molean.velocityskinloader.VelocitySkinLoader;
import com.molean.velocityskinloader.cache.PlayerSkinCache;
import com.molean.velocityskinloader.config.Config;
import com.molean.velocityskinloader.config.SkinProviderConfig;
import com.molean.velocityskinloader.exception.NoSuchSkinProviderException;
import com.molean.velocityskinloader.provider.SkinProvider;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.util.GameProfile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

public class SkinApply {
    private final Config config;

    private final Logger logger = Logger.getLogger(VelocitySkinLoader.class.getSimpleName());
    private final ProxyServer proxyServer;
    private final Object plugin;

    public SkinApply(Config config, ProxyServer proxyServer, Object plugin) {
        this.config = config;
        this.proxyServer = proxyServer;
        this.plugin = plugin;
    }

    @Subscribe
    public void onPlayerLogin(LoginEvent event) {
        String username = event.getPlayer().getUsername();
        Player player = event.getPlayer();
        if (!loadSkinFromCache(player, true)) {
            if (config.getGeneralConfig().isInitialBlockingLoading()) {
                loadSkinOnline(player);
            } else {
                if (!loadSkinFromCache(player, false)) {
                    logger.info("Initial blocking loading is disabled.");
                    logger.info(username + " would has skin next login.");
                }
                proxyServer.getScheduler().buildTask(plugin, () -> loadSkinOnline(player)).schedule();
            }
        }


    }

    public boolean loadSkinFromCache(Player player, boolean checkExpire) {
        String username = player.getUsername();
        if (PlayerSkinCache.playerSkinCache().contains(username)) {
            PlayerSkinCache.TimeLimitedSkin timeLimitedSkin = PlayerSkinCache.playerSkinCache().get(username);
            if (!checkExpire || timeLimitedSkin.getExpireTime().toLocalDateTime().isAfter(LocalDateTime.now())) {
                player.setGameProfileProperties(List.of(timeLimitedSkin.getProperty()));
                logger.info("Load cached skin for " + username);
                return true;
            }
        }
        return false;
    }

    public void loadSkinOnline(Player player) {
        String username = player.getUsername();
        for (SkinProviderConfig skinProviderConfig : config.getSkinProviderList()) {
            try {
                SkinProvider skinProvider = skinProviderConfig.toSkinProvider();
                GameProfile.Property property = skinProvider.getProperty(username);
                if (property != null) {
                    player.setGameProfileProperties(List.of(property));
                    logger.info(username + " loaded skin from " + skinProvider.getDisplay());
                    PlayerSkinCache.TimeLimitedSkin timeLimitedSkin = new PlayerSkinCache.TimeLimitedSkin();
                    LocalDateTime localDateTime = LocalDateTime.now().plusSeconds(config.getGeneralConfig().getPlayerSkinCacheTimeInSeconds());
                    timeLimitedSkin.setExpireTime(Timestamp.valueOf(localDateTime));
                    timeLimitedSkin.setProperty(property);
                    PlayerSkinCache.playerSkinCache().set(username, timeLimitedSkin);
                    return;
                }
            } catch (NoSuchSkinProviderException e) {
                e.printStackTrace();
            } catch (Exception exception) {
                if (config.getGeneralConfig().isPrintStackTracesIfSkinLoadFailed()) {
                    exception.printStackTrace();
                }
            }
        }
        logger.info("No available skin for " + username);
    }
}
