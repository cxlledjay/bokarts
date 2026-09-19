package de.cxlledjay.bokarts.component;

import com.mojang.serialization.Codec;
import de.cxlledjay.bokarts.BoKarts;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.function.UnaryOperator;

public class ModDataComponentTypes {

    public static final ComponentType<String> KART_ITEM_HORN_SOUND = register("kart_item_horn_sound", builder -> builder.codec(Codec.STRING));
    public static final ComponentType<Float> KART_ITEM_FUEL = register("kart_item_fuel", builder -> builder.codec(Codec.FLOAT));
    public static final ComponentType<Float> KART_ITEM_ODOMETER = register("kart_item_odometer", builder -> builder.codec(Codec.FLOAT));



    private static <T>ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, BoKarts.id(name), builderOperator.apply(ComponentType.builder()).build());
    }

    public static void register() {
        BoKarts.LOGGER.info("Registering ModDataComponentTypes for " + BoKarts.MOD_ID);
    }

}
