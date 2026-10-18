package de.cxlledjay.bokarts.networking;

import de.cxlledjay.bokarts.networking.packet.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class ModPackets {


    public static void registerPayloads() {
        // S2C
        PayloadTypeRegistry.playS2C().register(ConfigSyncS2C.ID, ConfigSyncS2C.STREAM_CODEC);

        // C2S
        PayloadTypeRegistry.playC2S().register(HornPlayPayloadC2S.ID, HornPlayPayloadC2S.STREAM_CODEC);
        PayloadTypeRegistry.playC2S().register(KartEntityAddFuelPayloadC2S.ID, KartEntityAddFuelPayloadC2S.STREAM_CODEC);
        PayloadTypeRegistry.playC2S().register(HornCyclePayloadC2S.ID, HornCyclePayloadC2S.STREAM_CODEC);
        PayloadTypeRegistry.playC2S().register(KartEntityInputPayloadC2S.ID, KartEntityInputPayloadC2S.STREAM_CODEC);
        PayloadTypeRegistry.playC2S().register(KartLiftChangeStateC2S.ID, KartLiftChangeStateC2S.STREAM_CODEC);
        PayloadTypeRegistry.playC2S().register(KartLiftModifyRaceNumberC2S.ID, KartLiftModifyRaceNumberC2S.STREAM_CODEC);
    }


    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(HornPlayPayloadC2S.ID, HornPlayPayloadC2S::receive);
        ServerPlayNetworking.registerGlobalReceiver(KartEntityAddFuelPayloadC2S.ID, KartEntityAddFuelPayloadC2S::receive);
        ServerPlayNetworking.registerGlobalReceiver(HornCyclePayloadC2S.ID, HornCyclePayloadC2S::receive);
        ServerPlayNetworking.registerGlobalReceiver(KartEntityInputPayloadC2S.ID, KartEntityInputPayloadC2S::receive);
        ServerPlayNetworking.registerGlobalReceiver(KartLiftChangeStateC2S.ID, KartLiftChangeStateC2S::receive);
        ServerPlayNetworking.registerGlobalReceiver(KartLiftModifyRaceNumberC2S.ID, KartLiftModifyRaceNumberC2S::receive);
    }

    public static void registerS2CPackets(){
        ClientPlayNetworking.registerGlobalReceiver(ConfigSyncS2C.ID, ConfigSyncS2C::receive);
    }

}
