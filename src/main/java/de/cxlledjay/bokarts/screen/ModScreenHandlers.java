package de.cxlledjay.bokarts.screen;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.screen.custom.KartInventoryScreenHandler;
import de.cxlledjay.bokarts.screen.custom.KartLiftScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;

public class ModScreenHandlers {

    public static final ScreenHandlerType<KartInventoryScreenHandler> KART_INVENTORY_SCREEN_HANDLER =
            Registry.register(
                    Registries.SCREEN_HANDLER,
                    BoKarts.id("kart_inventory_screen_handler"),
                    new ScreenHandlerType<>(KartInventoryScreenHandler::new, FeatureSet.empty())
            );

    public static final ExtendedScreenHandlerType<KartLiftScreenHandler, BlockPos> KART_LIFT_SCREEN_HANDLER =
            Registry.register(
                    Registries.SCREEN_HANDLER,
                    BoKarts.id("kart_lift_screen_handler"),
                    new ExtendedScreenHandlerType<>(KartLiftScreenHandler::new, BlockPos.PACKET_CODEC)
            );

    public static void register() {
        BoKarts.LOGGER.info("Registering ScreenHandlers for " + BoKarts.MOD_ID);
    }


}
