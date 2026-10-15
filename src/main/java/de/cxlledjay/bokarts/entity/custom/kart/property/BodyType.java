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

public enum BodyType implements StringIdentifiable {
    SOLID_COLOR_OVERLAY(0, "solid_color_overlay", "Single Color"),
    LIVERY_FADE(1, "livery_fade", "Fade"),
    LIVERY_RED_COW(2, "livery_red_cow", "Red Cow"),
    LIVERY_ENGLISH_RACING_ORANGE(3, "livery_english_racing_orange", "English Racing Orange"),
    LIVERY_MUNICH(4, "livery_munich", "Munich"),
    LIVERY_RALLY(5, "livery_rally", "Rally"),
    LIVERY_CAUTION(6, "livery_caution", "Caution!"),
    LIVERY_FULL_CARBON(7, "livery_full_carbon", "Cabron"),
    LIVERY_CREEPER(8, "livery_creeper", "Creeper, aww man!"),
    LIVERY_GROTTI(9, "livery_grotti", "Grotti"),
    LIVERY_BENEFACTOR(10, "livery_benefactor", "Benefactor"),
    LIVERY_SUPER_A_CAR(11, "livery_super_a_car", "Super a car"),
    LIVERY_JAPANESE_RACE_CAR(12, "livery_japanese_race_car", "Japanese race car"),
    LIVERY_POLIZFI(13, "livery_polizfi", "Police");


    // id to value function
    private static final IntFunction<BodyType> INDEX_TO_VALUE = ValueLists.createIdToValueFunction(
            BodyType::getId, values(), ValueLists.OutOfBoundsHandling.ZERO
    );

    // serialisation
    public static final Codec<BodyType> CODEC = StringIdentifiable.createCodec(BodyType::values);
    public static final PacketCodec<ByteBuf, BodyType> PACKET_CODEC = PacketCodecs.indexed(
            INDEX_TO_VALUE, BodyType::getId
    );

    // --- properties ---
    private final int id;
    private final String name;
    private final String display_name;
    private final Identifier texture;
    // private final int bodyColor;
    // private final int wheelColor;

    BodyType(final int id, final String name, final String display_name) {
        this.id = id;
        this.name = name;
        this.display_name = display_name;

        // dynamically creating a texture/model path using the enum's name
        this.texture = BoKarts.id("textures/entity/kartv2/body/" + name + ".png");
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

    public static BodyType fromId(int id) {
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