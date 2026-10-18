package de.cxlledjay.bokarts.networking.packet;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.entity.custom.kart.KartEntity;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record KartEntityInputPayloadC2S(int entityId, boolean left, boolean right, boolean forward, boolean back, boolean space) implements CustomPayload {

    public static final CustomPayload.Id<KartEntityInputPayloadC2S> ID = new CustomPayload.Id<>(BoKarts.id("kart_input"));

    public static final PacketCodec<RegistryByteBuf, KartEntityInputPayloadC2S> STREAM_CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT, KartEntityInputPayloadC2S::entityId,
            PacketCodecs.BOOL, KartEntityInputPayloadC2S::left,
            PacketCodecs.BOOL, KartEntityInputPayloadC2S::right,
            PacketCodecs.BOOL, KartEntityInputPayloadC2S::forward,
            PacketCodecs.BOOL, KartEntityInputPayloadC2S::back,
            PacketCodecs.BOOL, KartEntityInputPayloadC2S::space,
            KartEntityInputPayloadC2S::new
    );


    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void receive(KartEntityInputPayloadC2S payload, ServerPlayNetworking.Context ctx) {
        Entity entity = ctx.player().getWorld().getEntityById(payload.entityId());

        if (entity instanceof KartEntity kart) {
            kart.setInputsByServer(payload.left(), payload.right(), payload.forward(), payload.back(), payload.space());
        }
    }


}
