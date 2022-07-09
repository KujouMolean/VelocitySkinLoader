package com.molean.velocityskinloader.config;

import lombok.Data;

@Data
public class GeneralConfig {
    private boolean initialBlockingLoading = true;
    private boolean printStackTracesIfSkinLoadFailed = false;
    private long playerSkinCacheTimeInSeconds = 600;
}
