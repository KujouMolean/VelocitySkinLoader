# Velocity Skin Loader

A light-weight plugin load your custom skin for Velocity.

**Velocity only, no BungeeCord/Waterfall support.**

# Current supported skin

 [x] Blessing Skin
 [x] Official Skin


# Default load list

1. [BlessingSkin demo site](https://skin.prinzeugen.net/)
2. [LittleSkin](https://littlesk.in)
3. Official skin


# How to

1. Download the plugin and put it in to plugin directory.
2. Restart velocity proxy
3. Edit plugins/config.json
4. Restart server

```json
{
  "generalConfig": {
    "initialBlockingLoading": true,
    "printStackTracesIfSkinLoadFailed": false,
    "playerSkinCacheTimeInSeconds": 600
  },
  "skinProviderList": [
    {
      "type": "BlessingSkin",
      "url": "https://skin.prinzeugen.net/",
      "priority": 100
    },
    {
      "type": "BlessingSkin",
      "url": "https://littlesk.in/",
      "priority": 99
    },
    {
      "type": "Official",
      "priority": 0
    }
  ]
}
```

Here is config node in general section and what they stand for:

| Node                             | Stand for                                                                                                                                                                      | Default |
|----------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------|
| initialBlockingLoading           | Should the player wait until skin load completely to join the server. Set it to false if you want player join server faster, but player will have when skin next join instead. | true    |
| printStackTracesIfSkinLoadFailed | Print exception stack trace, used for debug.                                                                                                                                   | false   |
| playerSkinCacheTimeInSeconds     | How long will the player's skin refresh in seconds.                                                                                                                            | 600     |
