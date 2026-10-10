package de.cxlledjay.bokarts.entity.custom.kart.property;

import com.mojang.serialization.Codec;
import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.entity.custom.kart.KartEntity;
import de.cxlledjay.bokarts.sound.ModSounds;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.ValueLists;

import java.util.function.IntFunction;

public enum HornType implements StringIdentifiable {
    HORN1               (0,"horn_1",            ModSounds.HORN_CIVIC),
    HORN2               (1,"horn_2",            ModSounds.HORN_MINI),
    HORN3               (2,"horn_3",            ModSounds.HORN_BIKE),
    VILLAGER            (3,"villager",          SoundEvents.ENTITY_VILLAGER_HURT),
    METAL_PIPE          (4,"metal_pipe",        ModSounds.HORN_METAL_PIPE),
    DISCORD_JOIN        (5,"discord_join",      ModSounds.HORN_DISCORD_JOIN),
    DISCORD_LEAVE       (6,"discord_leave",     ModSounds.HORN_DISCORD_LEAVE),
    AUGHH               (7,"aughh",             ModSounds.HORN_AUGHH),
    RIZZ                (8,"rizz",              ModSounds.HORN_RIZZ),
    YODA                (9,"yoda",              ModSounds.HORN_YODA);

    // id to value function
    private static final IntFunction<HornType> INDEX_TO_VALUE = ValueLists.createIdToValueFunction(
            HornType::getId, values(), ValueLists.OutOfBoundsHandling.WRAP
    );

    // serialisation
    public static final Codec<HornType> CODEC = StringIdentifiable.createCodec(HornType::values);
    public static final PacketCodec<ByteBuf, HornType> PACKET_CODEC = PacketCodecs.indexed(
            INDEX_TO_VALUE, HornType::getId
    );

    // --- Properties ---
    private final int id;
    private final String name;
    private final SoundEvent soundEvent;

    HornType(final int id, final String name, final SoundEvent soundEvent) {
        this.id = id;
        this.name = name;
        this.soundEvent = soundEvent;
    }

    // --- interface ---
    @Override
    public String asString() {
        return this.name;
    }

    // --- id <-> enum ---
    public int getId() {
        return this.id;
    }

    public static HornType fromId(int id) {
        return INDEX_TO_VALUE.apply(id);
    }

    // --- sounds ---
    public SoundEvent getSoundEvent() {
        return this.soundEvent;
    }

    public HornType next() {
        return INDEX_TO_VALUE.apply(getId() + 1);
    }

    public HornType previous() {
        return INDEX_TO_VALUE.apply(getId() - 1);
    }
}
