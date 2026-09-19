package de.cxlledjay.bokarts.util;

import com.google.common.collect.Maps;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Map;

public class KartFuelItems {

    private static Map<Item, Float> fuelRangeMap = null;
    private static final float fuelTimeToRangeConversion = 2.5f;


    public static Map<Item, Float> createFuelRangeMap() {
        if(fuelRangeMap == null) {
            // get fuel time map from furnace
            Map<Item, Integer> map = AbstractFurnaceBlockEntity.createFuelTimeMap();

            // convert to ranges map
            fuelRangeMap = Maps.newLinkedHashMap();
            map.forEach( (item, value) -> {
                fuelRangeMap.put(item, value * fuelTimeToRangeConversion);
            } );
        }

        return fuelRangeMap;
    }

    public static boolean isFuelItem(ItemStack stack) {
        return createFuelRangeMap().containsKey(stack.getItem());
    }

    public static Float getFuelAmountFromItemStack(ItemStack stack) {
        if (stack.isEmpty()) {
            return 0.0f;
        }

        Item item = stack.getItem();
        return createFuelRangeMap().getOrDefault(item, 0.0f);
    }

}
