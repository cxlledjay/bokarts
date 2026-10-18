package de.cxlledjay.bokarts.networking.packet;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.block.entity.custom.KartLiftBlockEntity;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;

public record KartLiftChangeStateC2S(BlockPos blockPos) implements CustomPayload {

    public static final CustomPayload.Id<KartLiftChangeStateC2S> ID = new CustomPayload.Id<>(BoKarts.id("change_kart_lift_state"));

    public static final PacketCodec<RegistryByteBuf, KartLiftChangeStateC2S> STREAM_CODEC =  PacketCodec.tuple(
            BlockPos.PACKET_CODEC, KartLiftChangeStateC2S::blockPos,
            KartLiftChangeStateC2S::new
    );


    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void receive(KartLiftChangeStateC2S payload, ServerPlayNetworking.Context ctx) {
        BlockEntity be = ctx.player().getWorld().getBlockEntity(payload.blockPos());

        if (be instanceof KartLiftBlockEntity lift) {
            lift.toggleLiftState();
        }
    }
}
