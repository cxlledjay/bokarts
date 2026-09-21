package de.cxlledjay.bokarts.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class BoKartsConfig extends MidnightConfig {

    // ========================================
    // FUEL SETTINGS
    // ========================================

    @Entry(category = "fuel")
    public static float maxFuelCapacity = 12800.0f;

    @Entry(category = "fuel")
    public static float fuelConsumptionPerTick = 0.7625f;

    @Entry(category = "fuel")
    public static float fuelTimeToRangeConversion = 1.0f;


    // ========================================
    // PHYSICS SETTINGS
    // ========================================

    @Entry(category = "physics")
    public static int maxTicksInWater = 100;

    @Comment(category = "physics", centered = true)
    public static Comment physicsWarning;

    @Entry(category = "physics", min = 0.0, max = 1.0)
    public static float slipperinessDriveable = 0.98f;

    @Entry(category = "physics", min = 0.0, max = 1.0)
    public static float slipperinessNonDriveable = 0.9f;

    @Entry(category = "physics", min = 0.0, max = 1.0)
    public static float slipperinessSlowed = 0.75f;

    @Entry(category = "physics", min = 0.0, max = 1.0)
    public static float slipperinessNoPassenger = 0.6f;
}