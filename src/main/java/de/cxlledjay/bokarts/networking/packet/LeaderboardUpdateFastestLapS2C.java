package de.cxlledjay.bokarts.networking.packet;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.hud.custom.gpleaderboard.LeaderboardHudOverlay;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record LeaderboardUpdateFastestLapS2C(
        String name,
        long best
) implements CustomPayload {

    public static final Id<LeaderboardUpdateFastestLapS2C> ID = new Id<>(BoKarts.id("leaderboard_update_fastest"));

    public static final PacketCodec<RegistryByteBuf, LeaderboardUpdateFastestLapS2C> STREAM_CODEC = PacketCodec.of(
            LeaderboardUpdateFastestLapS2C::write,
            LeaderboardUpdateFastestLapS2C::read
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    private void write(RegistryByteBuf buf) {
        buf.writeString(this.name);
        buf.writeLong(this.best);
    }

    private static LeaderboardUpdateFastestLapS2C read(RegistryByteBuf buf) {
        return new LeaderboardUpdateFastestLapS2C(
                buf.readString(),
                buf.readLong()
        );
    }


    public static void receive(LeaderboardUpdateFastestLapS2C payload, ClientPlayNetworking.Context ctx) {
        ctx.client().execute(() -> LeaderboardHudOverlay.LEADERBOARD_DATA.update(payload));
    }
}