package de.cxlledjay.bokarts;

import de.cxlledjay.bokarts.block.entity.ModBlockEntities;
import de.cxlledjay.bokarts.block.entity.client.KartLiftArmsModel;
import de.cxlledjay.bokarts.block.entity.client.KartLiftBlockEntityRenderer;
import de.cxlledjay.bokarts.config.ClientSyncedConfig;
import de.cxlledjay.bokarts.entity.ModEntities;
import de.cxlledjay.bokarts.entity.client.KartModel;
import de.cxlledjay.bokarts.entity.client.KartRenderer;
import de.cxlledjay.bokarts.event.ModClientEvents;
import de.cxlledjay.bokarts.keymapping.ModKeyMappings;
import de.cxlledjay.bokarts.networking.ModPackets;
import de.cxlledjay.bokarts.screen.ModScreenHandlers;
import de.cxlledjay.bokarts.screen.custom.KartInventoryScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class BoKartsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        // Kart Entity rendering
        EntityModelLayerRegistry.registerModelLayer(KartModel.KART_ENTITY_MODEL_LAYER, KartModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.KART_ENTITY_TYPE, KartRenderer::new);

        // other client stuff
        ModKeyMappings.register();
        ModClientEvents.register();
        ModPackets.registerS2CPackets();

        // screens
        HandledScreens.register(ModScreenHandlers.KART_INVENTORY_SCREEN_HANDLER, KartInventoryScreen::new);

        // kart lift rendering
        EntityModelLayerRegistry.registerModelLayer(KartLiftArmsModel.KART_LIFT_ARMS_LAYER, KartLiftArmsModel::getTexturedModelData);
        BlockEntityRendererFactories.register(ModBlockEntities.KART_LIFT_BE, KartLiftBlockEntityRenderer::new);

        // init config sync server sided
        ClientSyncedConfig.initSyncedConfigClientEvent();
    }
}
