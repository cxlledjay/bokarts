package de.cxlledjay.bokarts.screen;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.screen.custom.*;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;

public class ModScreenHandlers {


    // kart inventory
    public static final ScreenHandlerType<KartInventoryScreenHandler> KART_INVENTORY_SCREEN_HANDLER =
            Registry.register(
                    Registries.SCREEN_HANDLER,
                    BoKarts.id("kart_inventory_screen_handler"),
                    new ScreenHandlerType<>(KartInventoryScreenHandler::new, FeatureSet.empty())
            );

    // kart lift
    public static final ExtendedScreenHandlerType<KartLiftScreenHandler, BlockPos> KART_LIFT_SCREEN_HANDLER =
            Registry.register(
                    Registries.SCREEN_HANDLER,
                    BoKarts.id("kart_lift_screen_handler"),
                    new ExtendedScreenHandlerType<>(KartLiftScreenHandler::new, BlockPos.PACKET_CODEC)
            );

    // livery workbench
    public static final ScreenHandlerType<LiveryWorkbenchScreenHandler> LIVERY_WORKBENCH_SCREEN_HANDLER =
            Registry.register(
                    Registries.SCREEN_HANDLER,
                    BoKarts.id("livery_workbench_screen_handler"),
                    new ScreenHandlerType<>(LiveryWorkbenchScreenHandler::new, FeatureSet.empty())
            );

    // paint bucket
    public static final ScreenHandlerType<PaintBucketScreenHandler> PAINT_BUCKET_SCREEN_HANDLER =
            Registry.register(
                    Registries.SCREEN_HANDLER,
                    BoKarts.id("paint_bucket_screen_handler"),
                    new ScreenHandlerType<>(PaintBucketScreenHandler::new, FeatureSet.empty())
            );

    // track timer
    public static final ScreenHandlerType<TrackTimerScreenHandler> TRACK_TIMER_SCREEN_HANDLER =
            Registry.register(
                    Registries.SCREEN_HANDLER,
                    BoKarts.id("track_timer_screen_handler"),
                    new ScreenHandlerType<>(TrackTimerScreenHandler::new, FeatureSet.empty())
            );


    public static void register() {
        BoKarts.LOGGER.info("Registering ScreenHandlers for " + BoKarts.MOD_ID);
    }


}
