package de.cxlledjay.bokarts.entity.custom.kart.property;

import java.util.function.IntFunction;

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

public enum SpoilerType implements StringIdentifiable {
    NONE                (0, "none",             "None",                     0.0f,       0.0f,           0.0f,       null),
    STREET              (1, "street",           "Street",                   0.04f,      -0.01f,         0.03f,      ModItems.SPOILER_STREET),
    STREET_CARBON       (2, "street_carbon",    "Street (Carbon)",          0.05f,      -0.0125f,       0.0325f,    ModItems.SPOILER_STREET_CARBON),
    RACE                (3, "race",             "Race",                     0.07f,      -0.0175f,       0.0525f,    ModItems.SPOILER_RACE),
    RACE_CARBON         (4, "race_carbon",      "Race (Carbon)",            0.08f,      -0.02f,         0.06f,      ModItems.SPOILER_RACE_CARBON),
    RACE_PLUS           (5, "race_plus",        "Race Pro Max",             0.09f,      -0.12f,         0.35f,      ModItems.SPOILER_RACE_PRO_MAX),
    RACE_PLUS_CARBON    (6, "race_plus_carbon", "Race Pro Max (Carbon)",    0.1f,       -0.11f,         0.42f,      ModItems.SPOILER_RACE_PRO_MAX_CARBON);

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
    private final float multiA;
    private final float multiV;
    private final float multiF;
    private final Item item;
    @Nullable
    private final Identifier texture;

    SpoilerType(final int id, final String name, final String display_name, float multiA, float multiV, float multiF, Item item) {
        this.id = id;
        this.name = name;
        this.display_name = display_name;
        this.multiA = multiA;
        this.multiV = multiV;
        this.multiF = multiF;
        this.item = item;

        // dynamically creating a texture/model path using the enum's name
        this.texture = (name.equals("none")) ? null : BoKarts.id("textures/entity/kartv2/spoiler/" + name + ".png");
    }

    // --- interface ---
    @Override
    public String asString() { return this.name; }

    // --- id <-> enum ---
    public int getId() { return this.id; }

    public static SpoilerType fromId(int id) { return INDEX_TO_VALUE.apply(id); }

    // --- physics ---
    public float getMultiA() { return multiA; }
    public float getMultiV() { return multiV; }
    public float getMultiF() { return multiF; }

    // --- textures ---
    public @Nullable Identifier getTexture() { return this.texture; }

    // --- items ---
    public String getDisplayName() { return this.display_name; }
    public Item getItem() { return this.item; }

}
