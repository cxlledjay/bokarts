package de.cxlledjay.bokarts.entity.custom.kart.property;

import java.util.function.IntFunction;

import com.mojang.serialization.Codec;
import de.cxlledjay.bokarts.BoKarts;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.ValueLists;
import org.jetbrains.annotations.Nullable;

public enum SpoilerType implements StringIdentifiable {
    NONE(0, "none"),
    STREET(1, "street"),
    STREET_CARBON(2, "street_carbon"),
    RACE(3, "race"),
    RACE_CARBON(4, "race_carbon");

    // id to value function
    private static final IntFunction<SpoilerType> INDEX_TO_VALUE = ValueLists.createIdToValueFunction(
            SpoilerType::getId, values(), ValueLists.OutOfBoundsHandling.ZERO
    );

    // serialisation
    public static final Codec<SpoilerType> CODEC = StringIdentifiable.createCodec(SpoilerType::values);
    public static final PacketCodec<ByteBuf, SpoilerType> PACKET_CODEC = PacketCodecs.indexed(
            INDEX_TO_VALUE, SpoilerType::getId
    );

    // --- Properties ---
    private final int id;
    private final String name;
    @Nullable
    private final Identifier texture;

    SpoilerType(final int id, final String name) {
        this.id = id;
        this.name = name;

        // Example: dynamically creating a texture/model path using the enum's name
        this.texture = (name.equals("none")) ? null : BoKarts.id("textures/entity/kartv2/spoiler/" + name + ".png");
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

    public static SpoilerType fromId(int id) {
        return INDEX_TO_VALUE.apply(id);
    }

    // --- textures ---
    public Identifier getTexture() {
        return this.texture;
    }

}
