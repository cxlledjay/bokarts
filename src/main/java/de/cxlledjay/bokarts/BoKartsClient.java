package de.cxlledjay.bokarts;

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

public class BoKartsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        // register Kart Entity
        EntityModelLayerRegistry.registerModelLayer(KartModel.KART_ENTITY_MODEL_LAYER, KartModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.KART_ENTITY_TYPE, KartRenderer::new);

        ModKeyMappings.register();
        ModClientEvents.register();
        ModPackets.registerS2CPackets();
        HandledScreens.register(ModScreenHandlers.KART_INVENTORY_SCREEN_HANDLER, KartInventoryScreen::new);

        // init config sync server sided
        ClientSyncedConfig.initSyncedConfigClientEvent();
    }
}
