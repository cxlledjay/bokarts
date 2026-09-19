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

public class ModItems {




    // kart crafting ingredients

    public static final Item KART_CRAFTING_WHEEL = registerItem("kart_wheel", new Item(new Item.Settings()));
    public static final Item KART_CRAFTING_STEERING_WHEEL = registerItem("kart_steering_wheel", new Item(new Item.Settings()));
    public static final Item KART_CRAFTING_SEAT = registerItem("kart_seat", new Item(new Item.Settings()));








    public static final Item KART_DEFAULT    = registerItem("kart/default",    new KartItem(KartEntity.PaintColor.DEFAULT,    new Item.Settings().maxCount(1)));
    // Vanilla dye colors
    public static final Item KART_WHITE      = registerItem("kart/white",      new KartItem(KartEntity.PaintColor.WHITE,      new Item.Settings().maxCount(1)));
    public static final Item KART_ORANGE     = registerItem("kart/orange",     new KartItem(KartEntity.PaintColor.ORANGE,     new Item.Settings().maxCount(1)));
    public static final Item KART_MAGENTA    = registerItem("kart/magenta",    new KartItem(KartEntity.PaintColor.MAGENTA,    new Item.Settings().maxCount(1)));
    public static final Item KART_LIGHT_BLUE = registerItem("kart/light_blue", new KartItem(KartEntity.PaintColor.LIGHT_BLUE, new Item.Settings().maxCount(1)));
    public static final Item KART_YELLOW     = registerItem("kart/yellow",     new KartItem(KartEntity.PaintColor.YELLOW,     new Item.Settings().maxCount(1)));
    public static final Item KART_LIME       = registerItem("kart/lime",       new KartItem(KartEntity.PaintColor.LIME,       new Item.Settings().maxCount(1)));
    public static final Item KART_PINK       = registerItem("kart/pink",       new KartItem(KartEntity.PaintColor.PINK,       new Item.Settings().maxCount(1)));
    public static final Item KART_GRAY       = registerItem("kart/gray",       new KartItem(KartEntity.PaintColor.GRAY,       new Item.Settings().maxCount(1)));
    public static final Item KART_LIGHT_GRAY = registerItem("kart/light_gray", new KartItem(KartEntity.PaintColor.LIGHT_GRAY, new Item.Settings().maxCount(1)));
    public static final Item KART_CYAN       = registerItem("kart/cyan",       new KartItem(KartEntity.PaintColor.CYAN,       new Item.Settings().maxCount(1)));
    public static final Item KART_PURPLE     = registerItem("kart/purple",     new KartItem(KartEntity.PaintColor.PURPLE,     new Item.Settings().maxCount(1)));
    public static final Item KART_BLUE       = registerItem("kart/blue",       new KartItem(KartEntity.PaintColor.BLUE,       new Item.Settings().maxCount(1)));
    public static final Item KART_BROWN      = registerItem("kart/brown",      new KartItem(KartEntity.PaintColor.BROWN,      new Item.Settings().maxCount(1)));
    public static final Item KART_GREEN      = registerItem("kart/green",      new KartItem(KartEntity.PaintColor.GREEN,      new Item.Settings().maxCount(1)));
    public static final Item KART_RED        = registerItem("kart/red",        new KartItem(KartEntity.PaintColor.RED,        new Item.Settings().maxCount(1)));
    public static final Item KART_BLACK      = registerItem("kart/black",      new KartItem(KartEntity.PaintColor.BLACK,      new Item.Settings().maxCount(1)));
    // special variants
    public static final Item KART_PURPLE_GOLD = registerItem("kart/purple_gold",      new KartItem(KartEntity.PaintColor.PURPLE_GOLD,      new Item.Settings().maxCount(1)));
    public static final Item KART_FADE        = registerItem("kart/fade",      new KartItem(KartEntity.PaintColor.FADE,      new Item.Settings().maxCount(1)));




    // helpers

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, BoKarts.id(name), item);
    }

    public static void registerModItems() {
        BoKarts.LOGGER.info("Registering Mod Items for " + BoKarts.MOD_ID);
    }

}
