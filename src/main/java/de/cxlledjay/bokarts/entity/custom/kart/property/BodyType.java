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

public enum BodyType implements StringIdentifiable {
    SOLID_COLOR_OVERLAY(0, "solid_color_overlay"),
    LIVERY_FADE(1, "livery_fade"),
    LIVERY_RED_COW(2, "livery_red_cow");


    // id to value function
    private static final IntFunction<BodyType> INDEX_TO_VALUE = ValueLists.createIdToValueFunction(
            BodyType::getId, values(), ValueLists.OutOfBoundsHandling.ZERO
    );

    // serialisation
    public static final Codec<BodyType> CODEC = StringIdentifiable.createCodec(BodyType::values);
    public static final PacketCodec<ByteBuf, BodyType> PACKET_CODEC = PacketCodecs.indexed(
            INDEX_TO_VALUE, BodyType::getId
    );

    // --- Properties ---
    private final int id;
    private final String name;
    private final Identifier texture;

    BodyType(final int id, final String name) {
        this.id = id;
        this.name = name;

        // Example: dynamically creating a texture/model path using the enum's name
        this.texture = BoKarts.id("textures/entity/kartv2/body/" + name + ".png");
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

    public static BodyType fromId(int id) {
        return INDEX_TO_VALUE.apply(id);
    }

    // --- textures ---
    public Identifier getTexture() {
        return this.texture;
    }

}
