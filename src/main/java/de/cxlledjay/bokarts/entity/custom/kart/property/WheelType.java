package de.cxlledjay.bokarts.entity.custom.kart.property;

import com.mojang.serialization.Codec;
import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.item.ModItems;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.ValueLists;

import java.util.function.IntFunction;
import java.util.function.Supplier;

public enum WheelType implements StringIdentifiable {
    NONE        (0, "none",         "None",                 0.0f,       0.0f,   0.0f,       0.0f,       () -> null) {
        @Override
        public float getStepHeight() { return 0.0f; }
    },
    STREET      (1, "street",       "Street",               0.03f,      0.08f,  0.04f,      0.03f,      () -> ModItems.WHEELS_STREET),
    SUPER_SOFT  (2, "super_soft",   "Slicks (Super Soft)",  0.06f,      0.1f,   0.1f,       0.02f,      () -> ModItems.WHEELS_SUPER_SOFT),
    SOFT        (3, "soft",         "Slicks (Soft)",        0.0525f,    0.095f, 0.9f,       0.045f,     () -> ModItems.WHEELS_SOFT),
    MEDIUM      (4, "medium",       "Slicks (Medium)",      0.045f,     0.09f,  0.8f,       0.07f,      () -> ModItems.WHEELS_MEDIUM),
    HARD        (5, "hard",         "Slicks (Hard)",        0.04125f,   0.085f, 0.7f,       0.085f,     () -> ModItems.WHEELS_HARD),
    SUPER_HARD  (6, "super_hard",   "Slicks (Super Hard)",  0.0375f,    0.08f,  0.06f,      0.11f,      () -> ModItems.WHEELS_SUPER_HARD),
    DRIFT       (7, "drift",        "Drift",                0.02f,      0.08f,  0.0f,       0.0f,       () -> ModItems.WHEELS_DRIFT),
    DRIFT_PLUS  (8, "drift_plus",   "Drift Plus",           0.011f,     0.08f,  -0.1f,      0.09f,      () -> ModItems.WHEELS_DRIFT_PLUS),
    OFFROAD     (9, "offroad",      "Offroad",              0.048f,     0.0f,   -0.15f,     -0.40825f,  () -> ModItems.WHEELS_OFFROAD) {
        @Override
        public float getStepHeight() { return 1.05f; }
        @Override
        public Identifier getOverlayTexture() { return BoKarts.id("textures/entity/kartv2/wheels/overlay_offroad.png"); }
    },
    SPIKES      (10, "spikes",       "Spikes",              0.055f,     0.0f,   0.10f,      -0.3f,      () -> ModItems.WHEELS_SPIKES) {
        @Override
        public float getStepHeight() { return 1.05f; }
        @Override
        public Identifier getOverlayTexture() { return BoKarts.id("textures/entity/kartv2/wheels/overlay_offroad.png"); }
    };


    // id to value function
    private static final IntFunction<WheelType> INDEX_TO_VALUE = ValueLists.createIdToValueFunction(
            WheelType::getId, values(), ValueLists.OutOfBoundsHandling.ZERO
    );

    // serialisation
    public static final Codec<WheelType> CODEC = StringIdentifiable.createCodec(WheelType::values);
    public static final PacketCodec<ByteBuf, WheelType> PACKET_CODEC = PacketCodecs.indexed(
            INDEX_TO_VALUE, WheelType::getId
    );



    // properties
    private final int id;
    private final String name;
    private final String display_name;
    private final float baseF;
    private final float penalityF;
    private final float multiA;
    private final float multiV;
    private final Supplier<Item> item;
    private final Identifier texture;

    WheelType(final int id, final String name, final String display_name, float baseF, float penalityF, float multiA, float multiV, Supplier<Item> item) {
        this.id = id;
        this.name = name;
        this.display_name = display_name;
        this.baseF = baseF;
        this.penalityF = penalityF;
        this.multiA = multiA;
        this.multiV = multiV;
        this.item = item;

        // create paths for textures
        this.texture = BoKarts.id("textures/entity/kartv2/wheels/" + name + ".png");
    }



    // --- interface ---
    @Override
    public String asString() { return this.name; }

    // --- id <-> enum ---
    public int getId() { return this.id; }
    public static WheelType fromId(int id) { return INDEX_TO_VALUE.apply(id); }


    // --- physics ---
    public float getStepHeight() { return 0.75F; }
    public float getBaseF() { return baseF; }
    public float getPenalityF() { return penalityF; }
    public float getMultiA() { return multiA; }
    public float getMultiV() { return multiV; }

    // --- textures ---
    public Identifier getBaseTexture() { return this.texture; }
    public Identifier getOverlayTexture() { return BoKarts.id("textures/entity/kartv2/wheels/overlay_normal.png"); }

    // --- items ---
    public String getDisplayName() { return this.display_name; }
    public Item getItem() { return this.item == null ? null : this.item.get(); }
}