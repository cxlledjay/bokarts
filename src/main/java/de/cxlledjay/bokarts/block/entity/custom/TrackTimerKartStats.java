package de.cxlledjay.bokarts.block.entity.custom;

import net.minecraft.nbt.NbtCompound;

public record TrackTimerKartStats(long timestamp, long best, int laps, boolean finished) {

    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
        nbt.putLong("Timestamp", this.timestamp);
        nbt.putLong("Best", this.best);
        nbt.putInt("Laps", this.laps);
        nbt.putBoolean("Finished", this.finished);
        return nbt;
    }

    public static TrackTimerKartStats fromNbt(NbtCompound nbt) {
        return new TrackTimerKartStats(
                nbt.getLong("Timestamp"),
                nbt.getLong("Best"),
                nbt.getInt("Laps"),
                nbt.getBoolean("Finished")
        );
    }

}
