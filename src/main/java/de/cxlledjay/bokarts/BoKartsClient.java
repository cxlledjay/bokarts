package de.cxlledjay.bokarts;

import de.cxlledjay.bokarts.entity.ModEntities;
import de.cxlledjay.bokarts.entity.client.KartModel;
import de.cxlledjay.bokarts.entity.client.KartRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class BoKartsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        // register Kart Entity
        EntityModelLayerRegistry.registerModelLayer(KartModel.KART_ENTITY_MODEL_LAYER, KartModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.KART_ENTITY_TYPE, KartRenderer::new);

    }
}
