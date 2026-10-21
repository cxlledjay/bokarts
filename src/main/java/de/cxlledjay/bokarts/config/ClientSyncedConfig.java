package de.cxlledjay.bokarts.config;

import de.cxlledjay.bokarts.networking.packet.ConfigSyncS2C;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class ClientSyncedConfig {

    public static void initSyncedConfigServerEvent() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            // Grab the server's authoritative config value
            ConfigSyncS2C payload = new ConfigSyncS2C(
                    BoKartsConfig.maxFuelCapacity,
                    BoKartsConfig.fuelTimeToRangeConversion,
                    BoKartsConfig.maxTicksInWater
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

    public static float activeFuelTimeToRangeConversion = BoKartsConfig.fuelTimeToRangeConversion;
    public static float getFuelTimeToRangeConversion() {
        return (isUsingServerConfig) ? (activeFuelTimeToRangeConversion) : (BoKartsConfig.fuelTimeToRangeConversion);
    }

    public static int activeMaxTicksInWater = BoKartsConfig.maxTicksInWater;
    public static int getMaxTicksInWater() {
        return (isUsingServerConfig) ? (activeMaxTicksInWater) : (BoKartsConfig.maxTicksInWater);
    }

}
