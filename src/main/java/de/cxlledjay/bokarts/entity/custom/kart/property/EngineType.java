package de.cxlledjay.bokarts.entity.custom.kart.property;

import java.util.function.IntFunction;
import java.util.function.Supplier;

import com.mojang.serialization.Codec;
import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.item.ModItems;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.Item;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.ValueLists;

public enum EngineType implements StringIdentifiable {
    ENGINE_NONE         (0,  "engine_none",         "None",         0.0f,   0.0f,       0.0f,       () -> null),
    ENGINE_COPPER       (1,  "engine_copper",       "Copper",       0.03f,  1.3f,       0.6f,       () -> ModItems.ENGINE_COPPER),
    ENGINE_IRON         (2,  "engine_iron",         "Iron",         0.035f, 1.7f,       0.8f,       () -> ModItems.ENGINE_IRON),
    ENGINE_GOLD         (3,  "engine_gold",         "Gold",         0.04f,  2.0f,       1.0f,       () -> ModItems.ENGINE_GOLD),
    ENGINE_DIAMOND      (4,  "engine_diamond",      "Diamond",      0.05f,  2.2f,       1.25f,      () -> ModItems.ENGINE_DIAMOND),
    ENGINE_NETHERITE    (5,  "engine_netherite",    "Netherite",    0.06f,  2.3473f,    1.5f,       () -> ModItems.ENGINE_NETHERITE);

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
    private final float baseA;
    private final float baseV;
    private final float multiFuel;
    private final Supplier<Item> item;
    private final Identifier texture;

    EngineType(final int id, final String name, final String display_name, float baseA, float baseV, float multiFuel, Supplier<Item> item) {
        this.id = id;
        this.name = name;
        this.display_name = display_name;
        this.baseA = baseA;
        this.baseV = baseV;
        this.multiFuel = multiFuel;
        this.item = item;

        // dynamically creating a texture/model path using the enum's name
        this.texture = BoKarts.id("textures/entity/kartv2/engine/" + name + ".png");
    }

    // --- interface ---
    @Override
    public String asString() { return this.name; }

    // --- id <-> enum ---
    public int getId() { return this.id; }

    public static EngineType fromId(int id) { return INDEX_TO_VALUE.apply(id); }

    // --- physics ---
    public float getbaseA() { return baseA; }
    public float getbaseV() { return baseV; }
    public float getFuelMulti() {return multiFuel; }


    // --- textures ---
    public Identifier getTexture() { return this.texture; }

    // --- items ---
    public String getDisplayName() { return this.display_name; }
    public Item getItem() { return this.item == null ? null : this.item.get(); }

}
