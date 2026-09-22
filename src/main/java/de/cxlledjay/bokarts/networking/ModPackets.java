package de.cxlledjay.bokarts.networking;

import de.cxlledjay.bokarts.networking.packet.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class ModPackets {


    public static void registerPayloads() {
        PayloadTypeRegistry.playC2S().register(HornPayloadC2S.ID, HornPayloadC2S.STREAM_CODEC);
        PayloadTypeRegistry.playC2S().register(AddFuelPayloadC2S.ID, AddFuelPayloadC2S.STREAM_CODEC);
        PayloadTypeRegistry.playC2S().register(SetHornPayloadC2S.ID, SetHornPayloadC2S.STREAM_CODEC);
        PayloadTypeRegistry.playC2S().register(KartInputPayloadC2S.ID, KartInputPayloadC2S.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(KartConfigSyncS2C.ID, KartConfigSyncS2C.STREAM_CODEC);
    }


    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(HornPayloadC2S.ID, HornPayloadC2S::receive);
        ServerPlayNetworking.registerGlobalReceiver(AddFuelPayloadC2S.ID, AddFuelPayloadC2S::receive);
        ServerPlayNetworking.registerGlobalReceiver(SetHornPayloadC2S.ID, SetHornPayloadC2S::receive);
        ServerPlayNetworking.registerGlobalReceiver(KartInputPayloadC2S.ID, KartInputPayloadC2S::receive);
    }

    public static void registerS2CPackets(){
        ClientPlayNetworking.registerGlobalReceiver(KartConfigSyncS2C.ID, KartConfigSyncS2C::receive);
    }

}
