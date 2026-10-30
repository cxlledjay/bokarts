package de.cxlledjay.bokarts.item;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.entity.custom.kart.property.AeroType;
import de.cxlledjay.bokarts.entity.custom.kart.property.EngineType;
import de.cxlledjay.bokarts.entity.custom.kart.property.SpoilerType;
import de.cxlledjay.bokarts.entity.custom.kart.property.WheelType;
import de.cxlledjay.bokarts.item.custom.*;
import de.cxlledjay.bokarts.item.custom.modification.AeroItem;
import de.cxlledjay.bokarts.item.custom.modification.EngineItem;
import de.cxlledjay.bokarts.item.custom.modification.SpoilerItem;
import de.cxlledjay.bokarts.item.custom.modification.WheelsItem;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Rarity;

public class ModItems {

    // the kart
    public static final Item KART_ITEM    = registerItem("kart_item",    new KartItemv2(KartItemv2.defaultKartSettings()));

    // ----- kart crafting ingredients -----
    // chassis
    public static final Item STEERING_WHEEL = registerItem("steering_wheel", new Item(new Item.Settings()));
    public static final Item SEAT = registerItem("seat", new Item(new Item.Settings()));
    public static final Item CHASSIS_BLUEPRINT = registerItem("chassis_blueprint", new Item(new Item.Settings()));
    // wheels
    public static final Item WHEELS_STREET = registerItem("wheels_street", new WheelsItem(WheelType.STREET, new Item.Settings()));
    public static final Item WHEELS_SEMI_SLICK = registerItem("wheels_semi_slick", new WheelsItem(WheelType.SEMI_SLICK, new Item.Settings()));
    public static final Item WHEELS_SOFT = registerItem("wheels_soft", new WheelsItem(WheelType.SOFT, new Item.Settings()));
    public static final Item WHEELS_HARD = registerItem("wheels_hard", new WheelsItem(WheelType.HARD, new Item.Settings()));
    public static final Item WHEELS_DRIFT = registerItem("wheels_drift", new WheelsItem(WheelType.DRIFT, new Item.Settings()));
    public static final Item WHEELS_DRIFT_PLUS = registerItem("wheels_drift_plus", new WheelsItem(WheelType.DRIFT_PLUS, new Item.Settings()));
    public static final Item WHEELS_OFFROAD = registerItem("wheels_offroad", new WheelsItem(WheelType.OFFROAD, new Item.Settings()));
    public static final Item WHEELS_SPIKES = registerItem("wheels_spikes", new WheelsItem(WheelType.SPIKES, new Item.Settings()));
    // engines
    public static final Item ENGINE_COPPER = registerItem("engine_copper", new EngineItem(EngineType.ENGINE_COPPER, new Item.Settings()));
    public static final Item ENGINE_IRON = registerItem("engine_iron", new EngineItem(EngineType.ENGINE_IRON, new Item.Settings()));
    public static final Item ENGINE_GOLD = registerItem("engine_gold", new EngineItem(EngineType.ENGINE_GOLD, new Item.Settings()));
    public static final Item ENGINE_DIAMOND = registerItem("engine_diamond", new EngineItem(EngineType.ENGINE_DIAMOND, new Item.Settings()));
    public static final Item ENGINE_NETHERITE = registerItem("engine_netherite", new EngineItem(EngineType.ENGINE_NETHERITE, new Item.Settings()));
    // spoilers
    public static final Item SPOILER_STREET = registerItem("spoiler_street", new SpoilerItem(SpoilerType.STREET, new Item.Settings()));
    public static final Item SPOILER_STREET_CARBON = registerItem("spoiler_street_carbon", new SpoilerItem(SpoilerType.STREET_CARBON, new Item.Settings()));
    public static final Item SPOILER_RACE = registerItem("spoiler_race", new SpoilerItem(SpoilerType.RACE, new Item.Settings()));
    public static final Item SPOILER_RACE_CARBON = registerItem("spoiler_race_carbon", new SpoilerItem(SpoilerType.RACE_CARBON, new Item.Settings()));
    public static final Item SPOILER_RACE_PRO_MAX = registerItem("spoiler_race_pro_max", new SpoilerItem(SpoilerType.RACE_PLUS, new Item.Settings()));
    public static final Item SPOILER_RACE_PRO_MAX_CARBON = registerItem("spoiler_race_pro_max_carbon", new SpoilerItem(SpoilerType.RACE_PLUS_CARBON, new Item.Settings()));
    // aero kits
    public static final Item AERO_RACE = registerItem("aero_race", new AeroItem(AeroType.RACE, new Item.Settings()));
    public static final Item AERO_RACE_CARBON = registerItem("aero_race_carbon", new AeroItem(AeroType.RACE_CARBON, new Item.Settings()));

    // customization
    public static final Item LIVERY_TEMPLATE = registerItem("livery_template", new LiveryTemplate(LiveryTemplate.defaultLiverySettings()));
    public static final Item DYE_MIX = registerItem("dye_mix", new Item(new Item.Settings()));

    // racing
    public static final Item TRACK_REPORT = registerItem("track_report", new TrackReport(new Item.Settings()));



    // accessories
    public static final Item KEY_BLANK = registerItem("key_blank", new Item(new Item.Settings()));
    public static final Item KEY_PAIRED = registerItem("key_paired", new KartKey(
            new Item.Settings()
                    .component(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)
                    .maxCount(1)
                    .rarity(Rarity.UNCOMMON)
            )
    );
    public static final Item KEY_PAIRING_TOOL = registerItem("key_pairing_tool", new Item(new Item.Settings()));




    // backwards compatability code
    public static final Item KART_DEFAULT    = registerItem("kart/default",         new LegacyKartItemConverter(new Item.Settings(), ModItems.KART_ITEM));
    public static final Item KART_WHITE      = registerItem("kart/white",           new LegacyKartItemConverter(new Item.Settings(), ModItems.KART_ITEM));
    public static final Item KART_ORANGE     = registerItem("kart/orange",          new LegacyKartItemConverter(new Item.Settings(), ModItems.KART_ITEM));
    public static final Item KART_MAGENTA    = registerItem("kart/magenta",         new LegacyKartItemConverter(new Item.Settings(), ModItems.KART_ITEM));
    public static final Item KART_LIGHT_BLUE = registerItem("kart/light_blue",      new LegacyKartItemConverter(new Item.Settings(), ModItems.KART_ITEM));
    public static final Item KART_YELLOW     = registerItem("kart/yellow",          new LegacyKartItemConverter(new Item.Settings(), ModItems.KART_ITEM));
    public static final Item KART_LIME       = registerItem("kart/lime",            new LegacyKartItemConverter(new Item.Settings(), ModItems.KART_ITEM));
    public static final Item KART_PINK       = registerItem("kart/pink",            new LegacyKartItemConverter(new Item.Settings(), ModItems.KART_ITEM));
    public static final Item KART_GRAY       = registerItem("kart/gray",            new LegacyKartItemConverter(new Item.Settings(), ModItems.KART_ITEM));
    public static final Item KART_LIGHT_GRAY = registerItem("kart/light_gray",      new LegacyKartItemConverter(new Item.Settings(), ModItems.KART_ITEM));
    public static final Item KART_CYAN       = registerItem("kart/cyan",            new LegacyKartItemConverter(new Item.Settings(), ModItems.KART_ITEM));
    public static final Item KART_PURPLE     = registerItem("kart/purple",          new LegacyKartItemConverter(new Item.Settings(), ModItems.KART_ITEM));
    public static final Item KART_BLUE       = registerItem("kart/blue",            new LegacyKartItemConverter(new Item.Settings(), ModItems.KART_ITEM));
    public static final Item KART_BROWN      = registerItem("kart/brown",           new LegacyKartItemConverter(new Item.Settings(), ModItems.KART_ITEM));
    public static final Item KART_GREEN      = registerItem("kart/green",           new LegacyKartItemConverter(new Item.Settings(), ModItems.KART_ITEM));
    public static final Item KART_RED        = registerItem("kart/red",             new LegacyKartItemConverter(new Item.Settings(), ModItems.KART_ITEM));
    public static final Item KART_BLACK      = registerItem("kart/black",           new LegacyKartItemConverter(new Item.Settings(), ModItems.KART_ITEM));
    public static final Item KART_PURPLE_GOLD = registerItem("kart/purple_gold",    new LegacyKartItemConverter(new Item.Settings(), ModItems.KART_ITEM));
    public static final Item KART_FADE        = registerItem("kart/fade",           new LegacyKartItemConverter(new Item.Settings(), ModItems.KART_ITEM));




    // helpers

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, BoKarts.id(name), item);
    }

    public static void registerModItems() {
        BoKarts.LOGGER.info("Registering Mod Items for " + BoKarts.MOD_ID);
    }

}
