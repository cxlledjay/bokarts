package de.cxlledjay.bokarts.block.entity.custom;

import net.minecraft.nbt.NbtCompound;

public record TrackTimerPlayerStats(long timestamp, long best, int laps, boolean finished) {

    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
        nbt.putLong("Timestamp", this.timestamp);
        nbt.putLong("Best", this.best);
        nbt.putInt("Laps", this.laps);
        nbt.putBoolean("Finished", this.finished);
        return nbt;
    }

    public static TrackTimerPlayerStats fromNbt(NbtCompound nbt) {
        return new TrackTimerPlayerStats(
                nbt.getLong("Timestamp"),
                nbt.getLong("Best"),
                nbt.getInt("Laps"),
                nbt.getBoolean("Finished")
        );
    }

}
