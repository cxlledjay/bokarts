package de.cxlledjay.bokarts.config;

import de.cxlledjay.bokarts.networking.packet.KartConfigSyncS2C;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class ClientSyncedConfig {

    public static void initSyncedConfigServerEvent() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            // Grab the server's authoritative config value
            KartConfigSyncS2C payload = new KartConfigSyncS2C(
                    BoKartsConfig.maxFuelCapacity,
                    BoKartsConfig.fuelConsumptionPerTick,
                    BoKartsConfig.fuelTimeToRangeConversion,
                    BoKartsConfig.maxTicksInWater,
                    BoKartsConfig.slipperinessDriveable,
                    BoKartsConfig.slipperinessNonDriveable,
                    BoKartsConfig.slipperinessSlowed,
                    BoKartsConfig.slipperinessNoPassenger
            );

            // Send it to the player who just joined
            ServerPlayNetworking.send(handler.player, payload);
        });
    }


    public static void initSyncedConfigClientEvent() {
        // reset isUsingServerConfig
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            isUsingServerConfig = false;
        });
    }





    // set via client/server sync
    public static boolean isUsingServerConfig = false;

    // interface for accessing server sided config

    public static float activeMaxFuelCapacity = BoKartsConfig.maxFuelCapacity;
    public static float getMaxFuelCapacity() {
        return (isUsingServerConfig) ? (activeMaxFuelCapacity) : (BoKartsConfig.maxFuelCapacity);
    }

    public static float activeFuelConsumptionPerTick = BoKartsConfig.fuelConsumptionPerTick;
    public static float getFuelConsumptionPerTick() {
        return (isUsingServerConfig) ? (activeFuelConsumptionPerTick) : (BoKartsConfig.fuelConsumptionPerTick);
    }

    public static float activeFuelTimeToRangeConversion = BoKartsConfig.fuelTimeToRangeConversion;
    public static float getFuelTimeToRangeConversion() {
        return (isUsingServerConfig) ? (activeFuelTimeToRangeConversion) : (BoKartsConfig.fuelTimeToRangeConversion);
    }

    public static int activeMaxTicksInWater = BoKartsConfig.maxTicksInWater;
    public static int getMaxTicksInWater() {
        return (isUsingServerConfig) ? (activeMaxTicksInWater) : (BoKartsConfig.maxTicksInWater);
    }

    public static float activeSlipperinessDriveable = BoKartsConfig.slipperinessDriveable;
    public static float getSlipperinessDriveable() {
        return (isUsingServerConfig) ? (activeSlipperinessDriveable) : (BoKartsConfig.slipperinessDriveable);
    }

    public static float activeSlipperinessNonDriveable = BoKartsConfig.slipperinessNonDriveable;
    public static float getSlipperinessNonDriveable() {
        return (isUsingServerConfig) ? (activeSlipperinessNonDriveable) : (BoKartsConfig.slipperinessNonDriveable);
    }

    public static float activeSlipperinessSlowed = BoKartsConfig.slipperinessSlowed;
    public static float getSlipperinessSlowed() {
        return (isUsingServerConfig) ? (activeSlipperinessSlowed) : (BoKartsConfig.slipperinessSlowed);
    }

    public static float activeSlipperinessNoPassenger = BoKartsConfig.slipperinessNoPassenger;
    public static float getSlipperinessNoPassenger() {
        return (isUsingServerConfig) ? (activeSlipperinessNoPassenger) : (BoKartsConfig.slipperinessNoPassenger);
    }

}
