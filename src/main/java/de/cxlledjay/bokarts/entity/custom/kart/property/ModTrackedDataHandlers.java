package de.cxlledjay.bokarts.entity.custom.kart.property;

import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;

public class ModTrackedDataHandlers {


    // tracked data handlers
    public static final TrackedDataHandler<WheelType> TRACKED_HANDLER = TrackedDataHandler.create(WheelType.PACKET_CODEC);


    public static void register() {
        TrackedDataHandlerRegistry.register(TRACKED_HANDLER);
    }


}
