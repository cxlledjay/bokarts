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

public enum EngineType implements StringIdentifiable {
    ENGINE_COPPER(0, "engine_copper"),
    ENGINE_IRON(1, "engine_iron"),
    ENGINE_GOLD(2, "engine_gold"),
    ENGINE_DIAMOND(3, "engine_diamond");

    // id to value function
    private static final IntFunction<EngineType> INDEX_TO_VALUE = ValueLists.createIdToValueFunction(
            EngineType::getId, values(), ValueLists.OutOfBoundsHandling.ZERO
    );

    // serialisation
    public static final Codec<EngineType> CODEC = StringIdentifiable.createCodec(EngineType::values);
    public static final PacketCodec<ByteBuf, EngineType> PACKET_CODEC = PacketCodecs.indexed(
            INDEX_TO_VALUE, EngineType::getId
    );

    // --- Properties ---
    private final int id;
    private final String name;
    private final Identifier texture;

    EngineType(final int id, final String name) {
        this.id = id;
        this.name = name;

        // Example: dynamically creating a texture/model path using the enum's name
        this.texture = BoKarts.id("textures/entity/kartv2/engine/" + name + ".png");
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

    public static EngineType fromId(int id) {
        return INDEX_TO_VALUE.apply(id);
    }

    // --- textures ---
    public Identifier getTexture() {
        return this.texture;
    }

}
