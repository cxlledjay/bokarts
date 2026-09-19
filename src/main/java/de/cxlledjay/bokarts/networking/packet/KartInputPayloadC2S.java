package de.cxlledjay.bokarts.networking.packet;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.entity.custom.KartEntity;
import de.cxlledjay.bokarts.screen.custom.KartInventoryScreenHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record KartInputPayloadC2S(int entityId, boolean left, boolean right, boolean forward, boolean back, boolean space) implements CustomPayload {

    public static final CustomPayload.Id<KartInputPayloadC2S> ID = new CustomPayload.Id<>(BoKarts.id("kart_input"));

    public static final PacketCodec<RegistryByteBuf, KartInputPayloadC2S> STREAM_CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT, KartInputPayloadC2S::entityId,
            PacketCodecs.BOOL, KartInputPayloadC2S::left,
            PacketCodecs.BOOL, KartInputPayloadC2S::right,
            PacketCodecs.BOOL, KartInputPayloadC2S::forward,
            PacketCodecs.BOOL, KartInputPayloadC2S::back,
            PacketCodecs.BOOL, KartInputPayloadC2S::space,
            KartInputPayloadC2S::new
    );


    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void receive(KartInputPayloadC2S payload, ServerPlayNetworking.Context ctx) {
        Entity entity = ctx.player().getWorld().getEntityById(payload.entityId());

        if (entity instanceof KartEntity kart) {
            kart.setInputsByServer(payload.left(), payload.right(), payload.forward(), payload.back(), payload.space());
        }
    }


}
