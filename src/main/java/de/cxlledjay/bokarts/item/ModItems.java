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

    public static final Item KART_DEFAULT = registerItem("kart_default", new KartItem(KartEntity.PaintColor.DEBUG, new Item.Settings().maxCount(1)));






    // helpers

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, BoKarts.id(name), item);
    }

    public static void registerModItems() {
        BoKarts.LOGGER.info("Registering Mod Items for " + BoKarts.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> {
            entries.add(KART_DEFAULT);
        });
    }

}
