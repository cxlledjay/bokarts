package de.cxlledjay.bokarts.entity.client;

import de.cxlledjay.bokarts.entity.ModEntities;
import de.cxlledjay.bokarts.entity.client.kartv2.ChassisModel;
import de.cxlledjay.bokarts.entity.client.kartv2.wheels.WheelsModelNormal;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class ModEntityLayers {


    public static void register() {
        EntityModelLayerRegistry.registerModelLayer(ChassisModel.ENTITY_MODEL_LAYER, ChassisModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(WheelsModelNormal.ENTITY_MODEL_LAYER, WheelsModelNormal::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.KART_ENTITY_TYPE, KartRenderer::new);
    }
}
