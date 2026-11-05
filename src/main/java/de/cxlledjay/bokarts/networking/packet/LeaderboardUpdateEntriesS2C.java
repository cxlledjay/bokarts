package de.cxlledjay.bokarts.networking.packet;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.block.entity.custom.race.LeaderboardEntry;
import de.cxlledjay.bokarts.hud.custom.gpleaderboard.LeaderboardHudOverlay;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import java.util.ArrayList;
import java.util.List;

public record LeaderboardUpdateEntriesS2C(
        List<LeaderboardEntry> entries
) implements CustomPayload {

    public static final CustomPayload.Id<LeaderboardUpdateEntriesS2C> ID = new CustomPayload.Id<>(BoKarts.id("leaderboard_update_entries"));

    public static final PacketCodec<RegistryByteBuf, LeaderboardUpdateEntriesS2C> STREAM_CODEC = PacketCodec.of(
            LeaderboardUpdateEntriesS2C::write,
            LeaderboardUpdateEntriesS2C::read
    );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }

    private void write(RegistryByteBuf buf) {
        buf.writeVarInt(this.entries.size());
        for (LeaderboardEntry entry : this.entries) {
            entry.write(buf);
        }
    }

    private static LeaderboardUpdateEntriesS2C read(RegistryByteBuf buf) {
        int size = buf.readVarInt();
        List<LeaderboardEntry> parsedEntries = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            parsedEntries.add(LeaderboardEntry.read(buf));
        }
        return new LeaderboardUpdateEntriesS2C(parsedEntries);
    }


    public static void receive(LeaderboardUpdateEntriesS2C payload, ClientPlayNetworking.Context ctx) {
        ctx.client().execute(() -> LeaderboardHudOverlay.LEADERBOARD_DATA.update(payload));
    }
}