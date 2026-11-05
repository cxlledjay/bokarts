package de.cxlledjay.bokarts.networking.packet;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.hud.custom.gpleaderboard.LeaderboardHudOverlay;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public record LeaderboardResetS2C() implements CustomPayload {

    public static final CustomPayload.Id<LeaderboardResetS2C> ID = new CustomPayload.Id<>(BoKarts.id("leaderboard_reset"));

    public static final PacketCodec<RegistryByteBuf, LeaderboardResetS2C> STREAM_CODEC = PacketCodec.of(
            LeaderboardResetS2C::write,
            LeaderboardResetS2C::read
    );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }

    private void write(RegistryByteBuf buf) {

    }

    private static LeaderboardResetS2C read(RegistryByteBuf buf) {
        return new LeaderboardResetS2C();
    }


    public static void receive(LeaderboardResetS2C payload, ClientPlayNetworking.Context ctx) {
        ctx.client().execute(LeaderboardHudOverlay.LEADERBOARD_DATA::reset);
    }
}