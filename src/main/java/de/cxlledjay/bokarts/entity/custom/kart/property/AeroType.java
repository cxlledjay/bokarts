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
    NONE            (0,  "none",            "None",             0.0f,       0.0f,        0.0f),
    RACE            (1,  "race",            "Race (Normal)",    0.05f,      -0.035f,     0.025f),
    RACE_CARBON     (2,  "race_carbon",     "Race (Carbon)",    0.07f,      -0.025f,     0.035f);

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
    @Nullable
    private final Identifier texture;

    AeroType(final int id, final String name, final String display_name, float multiA, float multiV, float multiF) {
        this.id = id;
        this.name = name;
        this.display_name = display_name;
        this.multiA = multiA;
        this.multiV = multiV;
        this.multiF = multiF;

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

}
