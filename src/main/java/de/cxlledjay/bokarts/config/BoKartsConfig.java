package de.cxlledjay.bokarts.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class BoKartsConfig extends MidnightConfig {

    // ========================================
    // CLIENT SETTINGS
    // ========================================


    // HUD SETTINGS

    @Comment(category = "hud", centered = true)
    public static Comment headingHud;

    @Entry(category = "hud")
    public static boolean enableKartHud = false;

    @Entry(category = "hud", min = 0.1f, max = 2.0f, isSlider = true)
    public static float kartHudScale = 1.0f;

    @Entry(category = "hud")
    public static int kartHudPadding = 10;

    @Entry(category = "hud", min = 0.1f, max = 1.0f, isSlider = true)
    public static float kartHudTransparency = 0.8f;

    @Entry(category = "hud", isColor = true)
    public static String kartHudTextColor = "#54B3F2";

    @Entry(category = "hud")
    public static KartSpeedUnit kartHudSpeedUnit = KartSpeedUnit.BLOCKS_PER_SECOND;

    public enum KartSpeedUnit {
        BLOCKS_PER_SECOND,
        KILOMETRES_PER_HOUR,
        BURGERS_PER_FREEDOM
    }




    @Comment(category = "race", centered = true)
    public static Comment headingLeaderboard;

    @Entry(category = "race")
    public static boolean enableLeaderboard = false;

    @Entry(category = "race")
    public static boolean displayOnLeft = true;

    @Entry(category = "race", min = 0.1f, max = 2.0f, isSlider = true)
    public static float leaderboardScale = 1.0f;


    // sounds

    @Comment(category = "sounds", centered = true)
    public static Comment headingSounds;

    @Entry(category = "sounds", min = 0.0f, max = 1.0f, isSlider = true)
    public static float hornPreviewVolume = 0.75f;






    // ========================================
    // SERVER SETTINGS
    // ========================================

    @Comment(category = "server", centered = true)
    public static Comment headingFuel;

    @Entry(category = "server")
    public static float maxFuelCapacity = 12800.0f;

    // @Entry(category = "server")
    // public static float fuelConsumptionPerTick = 0.7625f;

    @Entry(category = "server")
    public static float fuelTimeToRangeConversion = 1.0f;

    @Comment(category = "server", centered = true)
    public static Comment headingOther;

    @Entry(category = "server")
    public static int maxTicksInWater = 100;

    /*
    @Comment(category = "server", centered = true)
    public static Comment physicsWarning;

    @Entry(category = "server", min = 0.0, max = 1.0)
    public static float slipperinessDriveable = 0.98f;

    @Entry(category = "server", min = 0.0, max = 1.0)
    public static float slipperinessNonDriveable = 0.9f;

    @Entry(category = "server", min = 0.0, max = 1.0)
    public static float slipperinessSlowed = 0.75f;

    @Entry(category = "server", min = 0.0, max = 1.0)
    public static float slipperinessNoPassenger = 0.6f;
    */
}