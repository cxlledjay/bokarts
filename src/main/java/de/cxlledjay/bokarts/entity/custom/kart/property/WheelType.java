package de.cxlledjay.bokarts.entity.custom.kart.property;

import com.mojang.serialization.Codec;
import de.cxlledjay.bokarts.BoKarts;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.ValueLists;

import java.util.function.IntFunction;

public enum WheelType implements StringIdentifiable {
    STREET(0,"street"),
    RACE(1,"race"),
    DRIFT(2,"drift"),
    OFFROAD(3,"offroad") {
        @Override
        public float getStepHeight() {
            return 1.05f;
        }
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
    private final Identifier texture;
    private final Identifier overlay;

    WheelType(final int id, final String name) {
        this.id = id;
        this.name = name;

        // create paths for textures
        this.texture = BoKarts.id("textures/entity/kartv2/wheel/" + name + ".png");
        this.overlay = BoKarts.id("textures/entity/kartv2/wheel/" + name + "_overlay.png");
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

    public static WheelType fromId(int id) {
        return INDEX_TO_VALUE.apply(id);
    }



    // --- physics ---
    public float getStepHeight() {
        return 0.75F;
    }



    // --- textures ---
    public Identifier getBaseTexture() {
        return this.texture;
    }

    public Identifier getOverlayTexture() {
        return this.overlay;
    }
}