package de.cxlledjay.bokarts.item.client;

import de.cxlledjay.bokarts.component.ModDataComponentTypes;
import de.cxlledjay.bokarts.item.ModItems;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;

public class KartItemColorProvider {

    public static void registerKartItemOverlay() {
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            int color = 0xFFFFFFFF; //< default, if no color or layer 0
            if(tintIndex == 1) {
                // set to body color
                color = stack.getOrDefault(ModDataComponentTypes.KART_ITEM_BODY_COLOR, 0xFFFFFFFF);
            } else if (tintIndex == 2) {
                // set to wheels color
                color = stack.getOrDefault(ModDataComponentTypes.KART_ITEM_WHEEL_COLOR, 0xFFFFFFFF);
            }
            return color | 0xFF000000;
        }, ModItems.KART_ITEM);
    }
}
