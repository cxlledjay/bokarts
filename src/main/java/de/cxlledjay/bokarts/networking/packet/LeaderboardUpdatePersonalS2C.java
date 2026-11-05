package de.cxlledjay.bokarts.networking.packet;

import de.cxlledjay.bokarts.BoKarts;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public record LeaderboardUpdatePersonalS2C(
        String name,
        long last,
        long best,
        int currentLap,
        int totalLaps
) implements CustomPayload {

    public static final CustomPayload.Id<LeaderboardUpdatePersonalS2C> ID = new CustomPayload.Id<>(BoKarts.id("leaderboard_update_personal"));

    public static final PacketCodec<RegistryByteBuf, LeaderboardUpdatePersonalS2C> STREAM_CODEC = PacketCodec.of(
            LeaderboardUpdatePersonalS2C::write,
            LeaderboardUpdatePersonalS2C::read
    );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }

    private void write(RegistryByteBuf buf) {
        buf.writeString(this.name);
        buf.writeLong(this.last);
        buf.writeLong(this.best);
        buf.writeInt(this.currentLap);
        buf.writeInt(this.totalLaps);
    }

    private static LeaderboardUpdatePersonalS2C read(RegistryByteBuf buf) {
        return new LeaderboardUpdatePersonalS2C(
                buf.readString(),
                buf.readLong(),
                buf.readLong(),
                buf.readInt(),
                buf.readInt()
        );
    }


    public static void receive(LeaderboardUpdatePersonalS2C payload, ClientPlayNetworking.Context ctx) {
        ctx.client().execute(() -> {
            // TODO: update HUD
            BoKarts.LOGGER.info("Received LeaderboardUpdatePersonalS2C");
        });
    }
}