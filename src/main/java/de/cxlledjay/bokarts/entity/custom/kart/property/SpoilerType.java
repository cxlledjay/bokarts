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
    NONE(0, "none", "None", 0.0f, 0.0f),
    STREET(1, "street", "Street (Normal)", 0.2f, -0.05f),
    STREET_CARBON(2, "street_carbon", "Street (Carbon)", 0.2f, -0.05f),
    RACE(3, "race", "Race (Normal)", 0.25f, -0.1f),
    RACE_CARBON(4, "race_carbon", "Race (Carbon)", 0.25f, -0.1f);

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
    private final String display_name;
    private final float accelerationMultiplier;
    private final float vMaxMultiplier;
    @Nullable
    private final Identifier texture;

    SpoilerType(final int id, final String name, final String display_name, float accelerationMultiplier, float vMaxMultiplier) {
        this.id = id;
        this.name = name;
        this.display_name = display_name;
        this.accelerationMultiplier = accelerationMultiplier;
        this.vMaxMultiplier = vMaxMultiplier;

        // dynamically creating a texture/model path using the enum's name
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

    // --- physics ---
    public float getAccelerationMultiplier() {
        return this.accelerationMultiplier;
    }

    public float getVMaxMultiplier() {
        return this.vMaxMultiplier;
    }

    // --- textures ---
    public @Nullable Identifier getTexture() {
        return this.texture;
    }

    // --- items ---
    public String getDisplayName() {
        return this.display_name;
    }

}
