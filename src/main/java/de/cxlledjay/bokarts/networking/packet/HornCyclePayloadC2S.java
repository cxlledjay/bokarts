package de.cxlledjay.bokarts.networking.packet;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.entity.custom.kart.KartEntity;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;

public record HornCyclePayloadC2S(int direction) implements CustomPayload {

    public static final CustomPayload.Id<HornCyclePayloadC2S> ID = new CustomPayload.Id<>(BoKarts.id("set_horn"));

    public static final PacketCodec<RegistryByteBuf, HornCyclePayloadC2S> STREAM_CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT, HornCyclePayloadC2S::direction,
            HornCyclePayloadC2S::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void receive(HornCyclePayloadC2S payload, ServerPlayNetworking.Context ctx) {
        ServerPlayerEntity player = ctx.player();

        if(player.getVehicle() instanceof KartEntity kart) {
            kart.cycleHornSound(player, payload.direction);
        }
    }
}
