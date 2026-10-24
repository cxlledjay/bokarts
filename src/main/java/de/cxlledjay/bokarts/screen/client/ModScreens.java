package de.cxlledjay.bokarts.screen.client;

import de.cxlledjay.bokarts.screen.ModScreenHandlers;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class ModScreens {



    public static void register() {
        HandledScreens.register(ModScreenHandlers.KART_INVENTORY_SCREEN_HANDLER, KartInventoryScreen::new);
        HandledScreens.register(ModScreenHandlers.KART_LIFT_SCREEN_HANDLER, KartLiftScreen::new);
        HandledScreens.register(ModScreenHandlers.LIVERY_WORKBENCH_SCREEN_HANDLER, LiveryWorkbenchScreen::new);
        HandledScreens.register(ModScreenHandlers.PAINT_BUCKET_SCREEN_HANDLER, PaintBucketScreen::new);
        HandledScreens.register(ModScreenHandlers.TRACK_TIMER_SCREEN_HANDLER, TrackTimerScreen::new);
    }


}
