package de.cxlledjay.bokarts.item;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.item.custom.KartItem;
import de.cxlledjay.bokarts.item.custom.KartItemv2;
import de.cxlledjay.bokarts.item.custom.LiveryTemplate;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import static de.cxlledjay.bokarts.item.custom.KartItem.defaultKartSettings;

public class ModItems {

    // the kart
    public static final Item KART_ITEM    = registerItem("kart_item",    new KartItemv2(KartItemv2.defaultKartSettings()));

    // ----- kart crafting ingredients -----
    // chassis
    public static final Item STEERING_WHEEL = registerItem("steering_wheel", new Item(new Item.Settings()));
    public static final Item SEAT = registerItem("seat", new Item(new Item.Settings()));
    public static final Item CHASSIS_BLUEPRINT = registerItem("chassis_blueprint", new Item(new Item.Settings()));
    // wheels
    public static final Item WHEELS_STREET = registerItem("wheels_street", new Item(new Item.Settings()));
    public static final Item WHEELS_DRIFT = registerItem("wheels_drift", new Item(new Item.Settings()));
    public static final Item WHEELS_OFFROAD = registerItem("wheels_offroad", new Item(new Item.Settings()));
    // engines
    public static final Item ENGINE_COPPER = registerItem("engine_copper", new Item(new Item.Settings()));
    public static final Item ENGINE_IRON = registerItem("engine_iron", new Item(new Item.Settings()));
    public static final Item ENGINE_GOLD = registerItem("engine_gold", new Item(new Item.Settings()));
    public static final Item ENGINE_DIAMOND = registerItem("engine_diamond", new Item(new Item.Settings()));
    public static final Item ENGINE_NETHERITE = registerItem("engine_netherite", new Item(new Item.Settings()));

    // kart tuning parts
    public static final Item SPOILER_STREET = registerItem("spoiler_street", new Item(new Item.Settings()));
    public static final Item SPOILER_STREET_CARBON = registerItem("spoiler_street_carbon", new Item(new Item.Settings()));
    public static final Item SPOILER_RACE = registerItem("spoiler_race", new Item(new Item.Settings()));
    public static final Item SPOILER_RACE_CARBON = registerItem("spoiler_race_carbon", new Item(new Item.Settings()));
    public static final Item AERO_RACE = registerItem("aero_race", new Item(new Item.Settings()));
    public static final Item AERO_RACE_CARBON = registerItem("aero_race_carbon", new Item(new Item.Settings()));

    // customization
    public static final Item LIVERY_TEMPLATE = registerItem("livery_template", new LiveryTemplate(LiveryTemplate.defaultLiverySettings()));


    // accessories
    public static final Item KEY = registerItem("key", new Item(new Item.Settings()));




    // LEGACY CODE; TO BE REMOVED; USED FOR BACKWARDS COMPATIBILITY!
    public static final Item KART_DEFAULT    = registerItem("kart/default",         new KartItem(defaultKartSettings()));
    public static final Item KART_WHITE      = registerItem("kart/white",           new KartItem(defaultKartSettings()));
    public static final Item KART_ORANGE     = registerItem("kart/orange",          new KartItem(defaultKartSettings()));
    public static final Item KART_MAGENTA    = registerItem("kart/magenta",         new KartItem(defaultKartSettings()));
    public static final Item KART_LIGHT_BLUE = registerItem("kart/light_blue",      new KartItem(defaultKartSettings()));
    public static final Item KART_YELLOW     = registerItem("kart/yellow",          new KartItem(defaultKartSettings()));
    public static final Item KART_LIME       = registerItem("kart/lime",            new KartItem(defaultKartSettings()));
    public static final Item KART_PINK       = registerItem("kart/pink",            new KartItem(defaultKartSettings()));
    public static final Item KART_GRAY       = registerItem("kart/gray",            new KartItem(defaultKartSettings()));
    public static final Item KART_LIGHT_GRAY = registerItem("kart/light_gray",      new KartItem(defaultKartSettings()));
    public static final Item KART_CYAN       = registerItem("kart/cyan",            new KartItem(defaultKartSettings()));
    public static final Item KART_PURPLE     = registerItem("kart/purple",          new KartItem(defaultKartSettings()));
    public static final Item KART_BLUE       = registerItem("kart/blue",            new KartItem(defaultKartSettings()));
    public static final Item KART_BROWN      = registerItem("kart/brown",           new KartItem(defaultKartSettings()));
    public static final Item KART_GREEN      = registerItem("kart/green",           new KartItem(defaultKartSettings()));
    public static final Item KART_RED        = registerItem("kart/red",             new KartItem(defaultKartSettings()));
    public static final Item KART_BLACK      = registerItem("kart/black",           new KartItem(defaultKartSettings()));
    public static final Item KART_PURPLE_GOLD = registerItem("kart/purple_gold",    new KartItem(defaultKartSettings()));
    public static final Item KART_FADE        = registerItem("kart/fade",           new KartItem(defaultKartSettings()));




    // helpers

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, BoKarts.id(name), item);
    }

    public static void registerModItems() {
        BoKarts.LOGGER.info("Registering Mod Items for " + BoKarts.MOD_ID);
    }

}
