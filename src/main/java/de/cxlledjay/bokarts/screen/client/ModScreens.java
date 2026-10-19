package de.cxlledjay.bokarts.screen.client;

import de.cxlledjay.bokarts.screen.ModScreenHandlers;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class ModScreens {



    public static void register() {
        HandledScreens.register(ModScreenHandlers.KART_INVENTORY_SCREEN_HANDLER, KartInventoryScreen::new);
        HandledScreens.register(ModScreenHandlers.KART_LIFT_SCREEN_HANDLER, KartLiftScreen::new);
        HandledScreens.register(ModScreenHandlers.LIVERY_WORKBENCH_SCREEN_HANDLER, LiveryWorkbenchScreen::new);
    }


}
