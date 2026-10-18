package de.cxlledjay.bokarts.component;

import com.mojang.serialization.Codec;
import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.entity.custom.kart.property.*;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Uuids;

import java.util.Optional;
import java.util.UUID;
import java.util.function.UnaryOperator;

public class ModDataComponentTypes {

    public static final ComponentType<HornType> KART_ITEM_HORN_TYPE = register("kart_item_horn_type", builder -> builder.codec(HornType.CODEC));
    public static final ComponentType<Float> KART_ITEM_FUEL = register("kart_item_fuel", builder -> builder.codec(Codec.FLOAT));
    public static final ComponentType<Float> KART_ITEM_ODOMETER = register("kart_item_odometer", builder -> builder.codec(Codec.FLOAT));

    // v2 component types
    public static final ComponentType<BodyType> KART_ITEM_BODY_TYPE = register("kart_item_body_type", builder -> builder.codec(BodyType.CODEC));
    public static final ComponentType<WheelType> KART_ITEM_WHEEL_TYPE = register("kart_item_wheel_type", builder -> builder.codec(WheelType.CODEC));
    public static final ComponentType<EngineType> KART_ITEM_ENGINE_TYPE = register("kart_item_engine_type", builder -> builder.codec(EngineType.CODEC));
    public static final ComponentType<SpoilerType> KART_ITEM_SPOILER_TYPE = register("kart_item_spoler_type", builder -> builder.codec(SpoilerType.CODEC));
    public static final ComponentType<AeroType> KART_ITEM_AERO_TYPE = register("kart_item_aero_type", builder -> builder.codec(AeroType.CODEC));
    public static final ComponentType<Integer> KART_ITEM_BODY_COLOR = register("kart_item_body_color", builder -> builder.codec(Codec.INT));
    public static final ComponentType<Integer> KART_ITEM_WHEEL_COLOR = register("kart_item_wheel_color", builder -> builder.codec(Codec.INT));
    public static final ComponentType<Integer> KART_ITEM_RACE_NUMBER = register("kart_item_race_number", builder -> builder.codec(Codec.INT));

    // key
    public static final ComponentType<Boolean> KART_ITEM_HAS_KEY = register("kart_item_has_key", builder -> builder.codec(Codec.BOOL));
    public static final ComponentType<UUID> KART_KEY_PAIRED_UUID = register("kart_key_paired_uuid", builder -> builder.codec(Uuids.CODEC));
    public static final ComponentType<String> KART_KEY_INFO = register("kart_key_info", builder -> builder.codec(Codec.STRING));


    private static <T>ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, BoKarts.id(name), builderOperator.apply(ComponentType.builder()).build());
    }

    public static void register() {
        BoKarts.LOGGER.info("Registering ModDataComponentTypes for " + BoKarts.MOD_ID);
    }

}
