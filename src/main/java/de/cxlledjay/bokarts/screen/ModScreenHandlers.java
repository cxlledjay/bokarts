package de.cxlledjay.bokarts.screen;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.screen.custom.KartInventoryScreenHandler;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;

public class ModScreenHandlers {

    public static final ScreenHandlerType<KartInventoryScreenHandler> KART_INVENTORY_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, BoKarts.id("kart_inventory_screen_handler"),
                    new ScreenHandlerType<>(KartInventoryScreenHandler::new, FeatureSet.empty()));


    public static void register() {
        BoKarts.LOGGER.info("Registering ScreenHandlers for " + BoKarts.MOD_ID);
    }


}
