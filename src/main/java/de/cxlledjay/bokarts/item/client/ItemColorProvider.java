package de.cxlledjay.bokarts.item.client;

import de.cxlledjay.bokarts.component.ModDataComponentTypes;
import de.cxlledjay.bokarts.item.ModItems;
import de.cxlledjay.bokarts.item.custom.TrackReport;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;

public class ItemColorProvider {

    public static void registerKartItemOverlay() {
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            int color = 0xFFFFFF; //< default, if no color or layer 2
            if(tintIndex == 0) {
                // set to body color
                color = stack.getOrDefault(ModDataComponentTypes.KART_ITEM_BODY_COLOR, 0xFFFFFF);
            } else if (tintIndex == 1) {
                // set to wheels color
                color = stack.getOrDefault(ModDataComponentTypes.KART_ITEM_WHEEL_COLOR, 0xFFFFFF);
            }
            return color | 0xFF000000;
        }, ModItems.KART_ITEM, ModItems.LIVERY_TEMPLATE);
    }

    public static void registerTrackReportOverlay() {
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            int color = 0xFFFFFF; //< default
            if (tintIndex == 1) {
                // set to win color
                color = TrackReport.getPlacementColor(stack.getOrDefault(ModDataComponentTypes.TRACK_REPORT_PLACEMENT, -1));
            } else if(tintIndex == 0) {
                color = 0xA5A5A5;
            }
            return color | 0xFF000000;
        }, ModItems.TRACK_REPORT);
    }
}
