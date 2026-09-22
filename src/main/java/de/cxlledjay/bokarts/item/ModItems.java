package de.cxlledjay.bokarts.item;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.entity.custom.KartEntity;
import de.cxlledjay.bokarts.item.custom.KartItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import static de.cxlledjay.bokarts.item.custom.KartItem.defaultKartSettings;

public class ModItems {

    // kart crafting ingredients

    public static final Item KART_CRAFTING_WHEEL = registerItem("kart_wheel", new Item(new Item.Settings()));
    public static final Item KART_CRAFTING_STEERING_WHEEL = registerItem("kart_steering_wheel", new Item(new Item.Settings()));
    public static final Item KART_CRAFTING_SEAT = registerItem("kart_seat", new Item(new Item.Settings()));



    // accessories
    public static final Item KART_KEY = registerItem("kart_key", new Item(new Item.Settings()));



    public static final Item KART_DEFAULT    = registerItem("kart/default",    new KartItem(KartEntity.PaintColor.DEFAULT,    defaultKartSettings()));

    // Vanilla dye colors
    public static final Item KART_WHITE      = registerItem("kart/white",      new KartItem(KartEntity.PaintColor.WHITE,      defaultKartSettings()));
    public static final Item KART_ORANGE     = registerItem("kart/orange",     new KartItem(KartEntity.PaintColor.ORANGE,     defaultKartSettings()));
    public static final Item KART_MAGENTA    = registerItem("kart/magenta",    new KartItem(KartEntity.PaintColor.MAGENTA,    defaultKartSettings()));
    public static final Item KART_LIGHT_BLUE = registerItem("kart/light_blue", new KartItem(KartEntity.PaintColor.LIGHT_BLUE, defaultKartSettings()));
    public static final Item KART_YELLOW     = registerItem("kart/yellow",     new KartItem(KartEntity.PaintColor.YELLOW,     defaultKartSettings()));
    public static final Item KART_LIME       = registerItem("kart/lime",       new KartItem(KartEntity.PaintColor.LIME,       defaultKartSettings()));
    public static final Item KART_PINK       = registerItem("kart/pink",       new KartItem(KartEntity.PaintColor.PINK,       defaultKartSettings()));
    public static final Item KART_GRAY       = registerItem("kart/gray",       new KartItem(KartEntity.PaintColor.GRAY,       defaultKartSettings()));
    public static final Item KART_LIGHT_GRAY = registerItem("kart/light_gray", new KartItem(KartEntity.PaintColor.LIGHT_GRAY, defaultKartSettings()));
    public static final Item KART_CYAN       = registerItem("kart/cyan",       new KartItem(KartEntity.PaintColor.CYAN,       defaultKartSettings()));
    public static final Item KART_PURPLE     = registerItem("kart/purple",     new KartItem(KartEntity.PaintColor.PURPLE,     defaultKartSettings()));
    public static final Item KART_BLUE       = registerItem("kart/blue",       new KartItem(KartEntity.PaintColor.BLUE,       defaultKartSettings()));
    public static final Item KART_BROWN      = registerItem("kart/brown",      new KartItem(KartEntity.PaintColor.BROWN,      defaultKartSettings()));
    public static final Item KART_GREEN      = registerItem("kart/green",      new KartItem(KartEntity.PaintColor.GREEN,      defaultKartSettings()));
    public static final Item KART_RED        = registerItem("kart/red",        new KartItem(KartEntity.PaintColor.RED,        defaultKartSettings()));
    public static final Item KART_BLACK      = registerItem("kart/black",      new KartItem(KartEntity.PaintColor.BLACK,      defaultKartSettings()));
    // special variants
    public static final Item KART_PURPLE_GOLD = registerItem("kart/purple_gold",      new KartItem(KartEntity.PaintColor.PURPLE_GOLD,      defaultKartSettings()));
    public static final Item KART_FADE        = registerItem("kart/fade",      new KartItem(KartEntity.PaintColor.FADE,      defaultKartSettings()));




    // helpers

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, BoKarts.id(name), item);
    }

    public static void registerModItems() {
        BoKarts.LOGGER.info("Registering Mod Items for " + BoKarts.MOD_ID);
    }

}
