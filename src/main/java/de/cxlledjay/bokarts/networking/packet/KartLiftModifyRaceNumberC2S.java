package de.cxlledjay.bokarts.networking.packet;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.block.entity.custom.KartLiftBlockEntity;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;

public record KartLiftModifyRaceNumberC2S(BlockPos blockPos, int raceNumber) implements CustomPayload {

    public static final CustomPayload.Id<KartLiftModifyRaceNumberC2S> ID = new CustomPayload.Id<>(BoKarts.id("kart_lift_modify_race_number"));

    public static final PacketCodec<RegistryByteBuf, KartLiftModifyRaceNumberC2S> STREAM_CODEC =  PacketCodec.tuple(
            BlockPos.PACKET_CODEC, KartLiftModifyRaceNumberC2S::blockPos,
            PacketCodecs.VAR_INT, KartLiftModifyRaceNumberC2S::raceNumber,
            KartLiftModifyRaceNumberC2S::new
    );


    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void receive(KartLiftModifyRaceNumberC2S payload, ServerPlayNetworking.Context ctx) {
        BlockEntity be = ctx.player().getWorld().getBlockEntity(payload.blockPos());

        if (be instanceof KartLiftBlockEntity lift) {
            lift.setRaceNumber(payload.raceNumber());
        }
    }
}
