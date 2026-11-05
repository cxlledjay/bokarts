package de.cxlledjay.bokarts.block.entity.custom.race;

import net.minecraft.nbt.NbtCompound;

public record ParticipantInfo(long lastSeenTimestamp, long best, int laps, boolean finished) {

    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
        nbt.putLong("LastSeen", this.lastSeenTimestamp);
        nbt.putLong("Best", this.best);
        nbt.putInt("Laps", this.laps);
        nbt.putBoolean("Finished", this.finished);
        return nbt;
    }

    public static ParticipantInfo fromNbt(NbtCompound nbt) {
        return new ParticipantInfo(
                nbt.getLong("LastSeen"),
                nbt.getLong("Best"),
                nbt.getInt("Laps"),
                nbt.getBoolean("Finished")
        );
    }

}
