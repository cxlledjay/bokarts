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
    ENGINE_NONE(0, "engine_none", "None", 0.0f, 0.0f),
    ENGINE_COPPER(1, "engine_copper", "Copper", 0.03f, 1.5f),
    ENGINE_IRON(2, "engine_iron", "Iron", 0.035f, 1.75f),
    ENGINE_GOLD(3, "engine_gold", "Gold", 0.04f, 2.0f),
    ENGINE_DIAMOND(4, "engine_diamond", "Diamond", 0.055f, 2.15f),
    ENGINE_NETHERITE(5, "engine_netherite", "Netherite", 0.07f, 2.3f);

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
    private final String display_name;
    private final float acceleration;
    private final float vMax;
    private final Identifier texture;

    EngineType(final int id, final String name, final String display_name, float acceleration, float vMax) {
        this.id = id;
        this.name = name;
        this.display_name = display_name;
        this.acceleration = acceleration;
        this.vMax = vMax;

        // dynamically creating a texture/model path using the enum's name
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

    // --- physics ---
    public float getAcceleration() {
        return this.acceleration;
    }

    public float getVMax() {
        return this.vMax;
    }


    // --- textures ---
    public Identifier getTexture() {
        return this.texture;
    }

    // --- items ---
    public String getDisplayName() {
        return this.display_name;
    }

}
