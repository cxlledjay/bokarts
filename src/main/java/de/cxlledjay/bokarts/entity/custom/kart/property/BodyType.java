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
    SOLID_COLOR_OVERLAY             (0, "solid_color_overlay",              "Single Color",             0xFFFFFFFF, 0xFFFFFFFF),
    LIVERY_FADE                     (1, "livery_fade",                      "Fade",                     0xFFbf8e4b, 0xFFa12e45),
    LIVERY_RED_COW                  (2, "livery_red_cow",                   "Red Cow",                  0xFFa12e45, 0xFFcaa202),
    LIVERY_ENGLISH_RACING_ORANGE    (3, "livery_english_racing_orange",     "English Racing Orange",    0xFFcaa202, 0xFF696969),
    LIVERY_MUNICH                   (4, "livery_munich",                    "Munich",                   0xFF0065b0, 0xFFdfe5e5),
    LIVERY_RALLY                    (5, "livery_rally",                     "Rally",                    0xFF002c9c, 0xFFCD7F32),
    LIVERY_CAUTION                  (6, "livery_caution",                   "Caution!",                 0xFFfcf761, 0xFFfcf761),
    LIVERY_FULL_CARBON              (7, "livery_full_carbon",               "Cabron",                   0xFF272728, 0xFF696969),
    LIVERY_CREEPER                  (8, "livery_creeper",                   "Creeper, aww man!",        0xFF0ba10b, 0xFF85dc74),
    LIVERY_GROTTI                   (9, "livery_grotti",                    "Grotti",                   0xFFe91a1a, 0xFFe2e2e2),
    LIVERY_BENEFACTOR               (10, "livery_benefactor",               "Benefactor",               0xFF1db8c6, 0xFFe2e2e2),
    LIVERY_SUPER_A_CAR              (11, "livery_super_a_car",              "Super A Car",              0xFFfb9c0f, 0xFFFFF6FC),
    LIVERY_JAPANESE_RACE_CAR        (12, "livery_japanese_race_car",        "Japanese Race Car",        0xFF4b56fa, 0xFFFFF6FC),
    LIVERY_POLIZFI                  (13, "livery_polizfi",                  "Police",                   0xFF234fde, 0xFF39393a);


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
    private final int bodyColor;
    private final int wheelColor;

    BodyType(final int id, final String name, final String display_name, final int  bodyColor, final int wheelColor) {
        this.id = id;
        this.name = name;
        this.display_name = display_name;
        this.bodyColor = bodyColor;
        this.wheelColor = wheelColor;

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

    public int getDefaultBodyColor() {
        return this.bodyColor;
    }

    public int getDefaultWheelColor() {
        return this.wheelColor;
    }

    // --- items ---
    public String getDisplayName() {
        return this.display_name;
    }

}