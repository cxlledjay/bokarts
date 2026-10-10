package de.cxlledjay.bokarts.entity.custom;

import de.cxlledjay.bokarts.entity.custom.kart.property.*;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;

public class ModTrackedDataHandlers {


    // tracked data handlers
    public static final TrackedDataHandler<WheelType> WHEEL_TYPE_TRACKED_DATA_HANDLER = TrackedDataHandler.create(WheelType.PACKET_CODEC);
    public static final TrackedDataHandler<EngineType> ENGINE_TYPE_TRACKED_DATA_HANDLER = TrackedDataHandler.create(EngineType.PACKET_CODEC);
    public static final TrackedDataHandler<BodyType> BODY_TYPE_TRACKED_DATA_HANDLER = TrackedDataHandler.create(BodyType.PACKET_CODEC);
    public static final TrackedDataHandler<SpoilerType> SPOILER_TYPE_TRACKED_DATA_HANDLER = TrackedDataHandler.create(SpoilerType.PACKET_CODEC);
    public static final TrackedDataHandler<AeroType> AERO_TYPE_TRACKED_DATA_HANDLER = TrackedDataHandler.create(AeroType.PACKET_CODEC);

    public static final TrackedDataHandler<HornType> HORN_TYPE_TRACKED_DATA_HANDLER = TrackedDataHandler.create(HornType.PACKET_CODEC);


    public static void register() {
        TrackedDataHandlerRegistry.register(WHEEL_TYPE_TRACKED_DATA_HANDLER);
        TrackedDataHandlerRegistry.register(ENGINE_TYPE_TRACKED_DATA_HANDLER);
        TrackedDataHandlerRegistry.register(BODY_TYPE_TRACKED_DATA_HANDLER);
        TrackedDataHandlerRegistry.register(SPOILER_TYPE_TRACKED_DATA_HANDLER);
        TrackedDataHandlerRegistry.register(AERO_TYPE_TRACKED_DATA_HANDLER);
        TrackedDataHandlerRegistry.register(HORN_TYPE_TRACKED_DATA_HANDLER);
    }


}
