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
import org.jetbrains.annotations.Nullable;

public enum AeroType implements StringIdentifiable {
    NONE            (0,  "none",            "None",                 0.0f,       0.0f,        0.0f,          () -> null),
    RACE            (1,  "race",            "Race",                 0.03f,      -0.01f,      0.045f,        () -> ModItems.AERO_RACE),
    RACE_CARBON     (2,  "race_carbon",     "Race (Carbon)",        0.04f,      -0.0125f,    0.06f,         () -> ModItems.AERO_RACE_CARBON),
    TYPE_ONE        (3,  "type_one",        "Type One",             0.075f,     -0.02f,      0.15f,         () -> ModItems.AERO_TYPE_ONE),
    TYPE_ONE_COLORED(4,  "type_one_colored","Type One (Colored)",   0.075f,     -0.02f,      0.15f,         () -> ModItems.AERO_TYPE_ONE_COLORED),
    TYPE_ONE_CARBON (5,  "type_one_carbon", "Type One (Carbon)",    0.075f,     -0.02f,      0.15f,         () -> ModItems.AERO_TYPE_ONE_CARBON),;

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
    private final String display_name;
    private final float multiA;
    private final float multiV;
    private final float multiF;
    private final Supplier<Item> item;
    @Nullable
    private final Identifier texture;

    AeroType(final int id, final String name, final String display_name, float multiA, float multiV, float multiF, Supplier<Item> item) {
        this.id = id;
        this.name = name;
        this.display_name = display_name;
        this.multiA = multiA;
        this.multiV = multiV;
        this.multiF = multiF;
        this.item = item;

        // dynamically creating a texture/model path using the enum's name
        this.texture = (name.equals("none")) ? null : BoKarts.id("textures/entity/kartv2/aero/" + name + ".png");
    }

    // --- interface ---
    @Override
    public String asString() { return this.name; }

    // --- id <-> enum ---
    public int getId() { return this.id; }

    public static AeroType fromId(int id) { return INDEX_TO_VALUE.apply(id); }

    // --- physics ---
    public float getMultiA() { return multiA; }
    public float getMultiV() { return multiV; }
    public float getMultiF() { return multiF; }

    // --- textures ---
    public @Nullable Identifier getTexture() { return this.texture; }

    // --- items ---
    public String getDisplayName() { return this.display_name; }
    public Item getItem() { return this.item == null ? null : this.item.get(); }

}
