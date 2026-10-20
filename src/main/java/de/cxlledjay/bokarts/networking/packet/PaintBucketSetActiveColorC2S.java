package de.cxlledjay.bokarts.networking.packet;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.screen.custom.PaintBucketScreenHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;

public record PaintBucketSetActiveColorC2S(int color) implements CustomPayload {

    public static final CustomPayload.Id<PaintBucketSetActiveColorC2S> ID = new CustomPayload.Id<>(BoKarts.id("paint_bucket_set_active_color"));

    public static final PacketCodec<RegistryByteBuf, PaintBucketSetActiveColorC2S> STREAM_CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT, PaintBucketSetActiveColorC2S::color,
            PaintBucketSetActiveColorC2S::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void receive(PaintBucketSetActiveColorC2S payload, ServerPlayNetworking.Context ctx) {
        ctx.server().execute(() -> {
            PlayerEntity player = ctx.player();
            if (player.currentScreenHandler instanceof PaintBucketScreenHandler handler) {
                handler.setActiveColorServerBound(payload.color());
            }
        });
    }

}
