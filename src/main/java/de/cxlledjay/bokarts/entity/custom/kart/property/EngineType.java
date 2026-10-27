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
    ENGINE_NONE(0, "engine_none", "None"),
    ENGINE_COPPER(1, "engine_copper", "Copper"),
    ENGINE_IRON(2, "engine_iron", "Iron"),
    ENGINE_GOLD(3, "engine_gold", "Gold"),
    ENGINE_DIAMOND(4, "engine_diamond", "Diamond"),
    ENGINE_NETHERITE(5, "engine_netherite", "Netherite");

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
    private final Identifier texture;

    EngineType(final int id, final String name, final String display_name) {
        this.id = id;
        this.name = name;
        this.display_name = display_name;

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

    // --- textures ---
    public Identifier getTexture() {
        return this.texture;
    }

    // --- items ---
    public String getDisplayName() {
        return this.display_name;
    }

}
