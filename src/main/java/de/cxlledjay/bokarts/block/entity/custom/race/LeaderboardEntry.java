package de.cxlledjay.bokarts.block.entity.custom.race;

import net.minecraft.network.RegistryByteBuf;

public record LeaderboardEntry(String displayName, long delta, boolean lapped, String tires, boolean finished) {

    public void write(RegistryByteBuf buf) {
        buf.writeString(this.displayName);
        buf.writeLong(this.delta);
        buf.writeBoolean(this.lapped);
        buf.writeString(this.tires);
        buf.writeBoolean(this.finished);
    }

    public static LeaderboardEntry read(RegistryByteBuf buf) {
        return new LeaderboardEntry(
                buf.readString(),
                buf.readLong(),
                buf.readBoolean(),
                buf.readString(),
                buf.readBoolean()
        );
    }
}
