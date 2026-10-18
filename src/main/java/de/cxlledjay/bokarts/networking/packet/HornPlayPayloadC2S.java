package de.cxlledjay.bokarts.networking.packet;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.entity.custom.kart.KartEntity;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record HornPlayPayloadC2S() implements CustomPayload {

    public static final CustomPayload.Id<HornPlayPayloadC2S> ID = new CustomPayload.Id<>(BoKarts.id("play_horn"));

    public static final PacketCodec<RegistryByteBuf, HornPlayPayloadC2S> STREAM_CODEC = PacketCodec.unit(new HornPlayPayloadC2S());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void receive(HornPlayPayloadC2S payload, ServerPlayNetworking.Context ctx) {
        PlayerEntity player = ctx.player();

        if(player.getVehicle() instanceof KartEntity kart) {
            kart.playHornSound();
        }
    }

}
