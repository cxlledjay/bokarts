package de.cxlledjay.bokarts.networking.packet;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.entity.custom.KartEntity;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record HornPayloadC2S() implements CustomPayload {

    public static final CustomPayload.Id<HornPayloadC2S> ID = new CustomPayload.Id<>(BoKarts.id("play_horn"));

    public static final PacketCodec<RegistryByteBuf, HornPayloadC2S> STREAM_CODEC = PacketCodec.unit(new HornPayloadC2S());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void receive(HornPayloadC2S payload, ServerPlayNetworking.Context ctx) {
        PlayerEntity player = ctx.player();

        if(player.getVehicle() instanceof KartEntity kart) {
            kart.playHornSound();
        }
    }

}
