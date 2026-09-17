package de.cxlledjay.bokarts.networking;

import de.cxlledjay.bokarts.networking.packet.HornPayloadC2S;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class ModPackets {

    public static void registerC2SPackets() {
        PayloadTypeRegistry.playC2S().register(HornPayloadC2S.ID, HornPayloadC2S.STREAM_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(HornPayloadC2S.ID, HornPayloadC2S::receive);
    }

}
