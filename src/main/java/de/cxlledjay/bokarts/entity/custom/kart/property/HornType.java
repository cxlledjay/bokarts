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
    HORN1               (0, "horn_1",            "Car Horn (1)",                ModSounds.HORN_CIVIC),
    HORN2               (1, "horn_2",            "Car Horn (2)",                ModSounds.HORN_MINI),
    HORN3               (2, "horn_3",            "Car Horn (3)",                ModSounds.HORN_BIKE),
    VILLAGER            (3, "villager",          "Villager Hurt",               SoundEvents.ENTITY_VILLAGER_HURT),
    DISCORD_JOIN        (4, "discord_join",      "Discord Call Join",           ModSounds.HORN_DISCORD_JOIN),
    DISCORD_PING        (5, "discord_ping",      "Discord Ping",                ModSounds.HORN_DISCORD_PING),
    DISCORD_LEAVE       (6, "discord_leave",     "Discord Call Leave",          ModSounds.HORN_DISCORD_LEAVE),
    METAL_PIPE          (7, "metal_pipe",        "Metal Pipe Falling",          ModSounds.HORN_METAL_PIPE),
    AUGHH               (8, "aughh",             "Snoring",                     ModSounds.HORN_AUGHH),
    HEHEHEHA            (9, "heheheha",          "Heheheha",                    ModSounds.HORN_HEHEHEHA),
    YODA                (10,"yoda",              "LEGO Yoda Death",             ModSounds.HORN_YODA),
    CASH                (11,"cash",              "$ Cash $",                    ModSounds.HORN_CASH),
    BONK                (12,"bonk",              "B o n k",                     ModSounds.HORN_BONK),
    VINE_BOOM           (13,"vine_boom",         "Vine Boom",                   ModSounds.HORN_VINE_BOOM),
    RIZZ                (14,"rizz",              "Rizz",                        ModSounds.HORN_RIZZ);

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
    private final String display_name;
    private final SoundEvent soundEvent;

    HornType(final int id, final String name, final String display_name, final SoundEvent soundEvent) {
        this.id = id;
        this.name = name;
        this.display_name = display_name;
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

    // --- items ---
    public String getDisplayName() {
        return this.display_name;
    }
}
