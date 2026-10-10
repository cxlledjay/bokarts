package de.cxlledjay.bokarts.entity.client;

import de.cxlledjay.bokarts.entity.ModEntities;
import de.cxlledjay.bokarts.entity.client.kartv2.ChassisModel;
import de.cxlledjay.bokarts.entity.client.kartv2.EngineModel;
import de.cxlledjay.bokarts.entity.client.kartv2.aero.AeroRaceModel;
import de.cxlledjay.bokarts.entity.client.kartv2.spoiler.SpoilerRaceModel;
import de.cxlledjay.bokarts.entity.client.kartv2.spoiler.SpoilerStreetModel;
import de.cxlledjay.bokarts.entity.client.kartv2.wheels.WheelsModelNormal;
import de.cxlledjay.bokarts.entity.client.kartv2.wheels.WheelsModelOffroad;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class ModEntityLayers {


    public static void register() {
        EntityModelLayerRegistry.registerModelLayer(ChassisModel.ENTITY_MODEL_LAYER, ChassisModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(WheelsModelNormal.ENTITY_MODEL_LAYER, WheelsModelNormal::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(WheelsModelOffroad.ENTITY_MODEL_LAYER, WheelsModelOffroad::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(EngineModel.ENTITY_MODEL_LAYER, EngineModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(AeroRaceModel.ENTITY_MODEL_LAYER, AeroRaceModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(SpoilerStreetModel.ENTITY_MODEL_LAYER, SpoilerStreetModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(SpoilerRaceModel.ENTITY_MODEL_LAYER, SpoilerRaceModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.KART_ENTITY_TYPE, KartRenderer::new);
    }
}
