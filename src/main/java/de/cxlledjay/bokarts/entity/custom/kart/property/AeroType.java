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

public enum AeroType implements StringIdentifiable {
    NONE(0, "none"),
    RACE(1, "race"),
    RACE_CARBON(2, "race_carbon");

    // id to value function
    private static final IntFunction<AeroType> INDEX_TO_VALUE = ValueLists.createIdToValueFunction(
            AeroType::getId, values(), ValueLists.OutOfBoundsHandling.ZERO
    );

    // serialisation
    public static final Codec<AeroType> CODEC = StringIdentifiable.createCodec(AeroType::values);
    public static final PacketCodec<ByteBuf, AeroType> PACKET_CODEC = PacketCodecs.indexed(
            INDEX_TO_VALUE, AeroType::getId
    );

    // --- Properties ---
    private final int id;
    private final String name;
    @Nullable
    private final Identifier texture;

    AeroType(final int id, final String name) {
        this.id = id;
        this.name = name;

        // Example: dynamically creating a texture/model path using the enum's name
        this.texture = (name.equals("none")) ? null : BoKarts.id("textures/entity/kartv2/aero/" + name + ".png");
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

    public static AeroType fromId(int id) {
        return INDEX_TO_VALUE.apply(id);
    }

    // --- textures ---
    public Identifier getTexture() {
        return this.texture;
    }

}
