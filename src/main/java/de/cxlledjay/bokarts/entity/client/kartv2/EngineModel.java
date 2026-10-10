package de.cxlledjay.bokarts.entity.client.kartv2;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.entity.custom.kart.KartEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class EngineModel<T extends KartEntity> extends SinglePartEntityModel<T>  {

    public static final EntityModelLayer ENTITY_MODEL_LAYER = new EntityModelLayer(BoKarts.id("kartv2_engine"), "main");

    private final ModelPart engine;
    private final ModelPart drive_gear;

    public EngineModel(ModelPart root) {
        this.engine = root.getChild("engine");
        this.drive_gear = this.engine.getChild("drive_gear");
    }


    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData engine = modelPartData.addChild("engine", ModelPartBuilder.create().uv(0, 0).cuboid(-4.5F, -8.0F, 11.0F, 5.0F, 5.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 23.0F, -1.5F));

        ModelPartData hopper = engine.addChild("hopper", ModelPartBuilder.create().uv(0, 11).cuboid(1.0F, -8.0F, 11.0F, 5.0F, 2.0F, 5.0F, new Dilation(0.0F))
                .uv(4, 19).cuboid(2.0F, -6.5F, 12.0F, 3.0F, 3.0F, 3.0F, new Dilation(0.0F))
                .uv(8, 26).cuboid(1.0F, -5.0F, 13.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.5F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData drive_gear = engine.addChild("drive_gear", ModelPartBuilder.create().uv(24, 7).cuboid(-1.0F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, new Dilation(-0.25F))
                .uv(24, 0).cuboid(-1.0F, -1.5F, -1.5F, 1.0F, 3.0F, 3.0F, new Dilation(0.01F)), ModelTransform.pivot(-5.0F, -4.25F, 13.5F));
        return TexturedModelData.of(modelData, 32, 32);
    }


    @Override
    public ModelPart getPart() {
        return engine;
    }


    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

        // get lerps
        float tickDelta = MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true);

        // ---------------- wheels ----------------
        this.drive_gear.pitch = -MathHelper.lerp(tickDelta, entity.rearAxleRotationPrev, entity.rearAxleRotation);
    }


    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
        engine.render(matrices, vertexConsumer, light, overlay, color);
    }
}