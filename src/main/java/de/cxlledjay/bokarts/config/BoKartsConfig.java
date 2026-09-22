package de.cxlledjay.bokarts.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class BoKartsConfig extends MidnightConfig {

    // ========================================
    // CLIENT SETTINGS
    // ========================================

    // TBD







    // ========================================
    // SERVER SETTINGS
    // ========================================

    @Entry(category = "server")
    public static float maxFuelCapacity = 12800.0f;

    @Entry(category = "server")
    public static float fuelConsumptionPerTick = 0.7625f;

    @Entry(category = "server")
    public static float fuelTimeToRangeConversion = 1.0f;

    @Entry(category = "server")
    public static int maxTicksInWater = 100;

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
}