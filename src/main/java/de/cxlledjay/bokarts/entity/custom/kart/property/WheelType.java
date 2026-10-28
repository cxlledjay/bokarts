package de.cxlledjay.bokarts.entity.custom.kart.property;

import com.mojang.serialization.Codec;
import de.cxlledjay.bokarts.BoKarts;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.ValueLists;

import java.util.function.IntFunction;

public enum WheelType implements StringIdentifiable {
    NONE        (0, "none",         "None",             1.0f,   0.0f,   0.0f,       0.0f) {
        @Override
        public float getStepHeight() { return 0.0f; }
    },
    STREET      (1, "street",       "Street",           0.04f,  0.08f,  0.045f,     0.03f),
    SEMI_SLICK  (2, "semi_slick",   "Semi Slicks",      0.045f, 0.09f,  0.0675f,    0.045f),
    SOFT        (3, "soft",         "Slicks (Soft)",    0.06f,  0.1f,   0.09f,      0.03f),
    HARD        (4, "hard",         "Slicks (Hard)",    0.03f,  0.1f,   0.045f,     0.06f),
    DRIFT       (5, "drift",        "Drift",            0.02f,  0.08f,  0.0f,       0.0f),
    DRIFT_PLUS  (6, "drift_plus",   "Drift Plus",       0.015f, 0.08f,  -0.16f,     0.055f),
    OFFROAD     (7, "offroad",      "Offroad",          0.048f, 0.0f,   -0.1f,      -0.40825f) {
        @Override
        public float getStepHeight() { return 1.05f; }
        @Override
        public Identifier getOverlayTexture() { return BoKarts.id("textures/entity/kartv2/wheels/overlay_offroad.png"); }
    },
    SPIKES      (8, "spikes",       "Spikes",           0.055f, 0.0f,   0.05f,      -0.35f) {
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
    private final Identifier texture;

    WheelType(final int id, final String name, final String display_name, float baseF, float penalityF, float multiA, float multiV) {
        this.id = id;
        this.name = name;
        this.display_name = display_name;
        this.baseF = baseF;
        this.penalityF = penalityF;
        this.multiA = multiA;
        this.multiV = multiV;

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
}