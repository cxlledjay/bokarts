package de.cxlledjay.bokarts;

import de.cxlledjay.bokarts.block.entity.ModBlockEntities;
import de.cxlledjay.bokarts.block.entity.client.KartLiftArmsModel;
import de.cxlledjay.bokarts.block.entity.client.KartLiftBlockEntityRenderer;
import de.cxlledjay.bokarts.config.ClientSyncedConfig;
import de.cxlledjay.bokarts.entity.client.ModEntityLayers;
import de.cxlledjay.bokarts.event.ModClientEvents;
import de.cxlledjay.bokarts.hud.ModHuds;
import de.cxlledjay.bokarts.item.client.ItemColorProvider;
import de.cxlledjay.bokarts.keymapping.ModKeyMappings;
import de.cxlledjay.bokarts.networking.ModPackets;
import de.cxlledjay.bokarts.screen.client.ModScreens;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class BoKartsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        // Kart Entity rendering
        ModEntityLayers.register();

        // item overlays
        ItemColorProvider.registerKartItemOverlay();
        ItemColorProvider.registerTrackReportOverlay();


        // other client stuff
        ModKeyMappings.register();
        ModClientEvents.register();
        ModPackets.registerS2CPackets();

        // screens
        ModScreens.register();

        // kart lift rendering
        EntityModelLayerRegistry.registerModelLayer(KartLiftArmsModel.KART_LIFT_ARMS_LAYER, KartLiftArmsModel::getTexturedModelData);
        BlockEntityRendererFactories.register(ModBlockEntities.KART_LIFT_BE, KartLiftBlockEntityRenderer::new);

        // init config sync server sided
        ClientSyncedConfig.initSyncedConfigClientEvent();

        // register kart hud
        ModHuds.register();
    }
}
