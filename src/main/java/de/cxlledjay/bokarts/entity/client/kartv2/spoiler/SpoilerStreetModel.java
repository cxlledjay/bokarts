package de.cxlledjay.bokarts.entity.client.kartv2.spoiler;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.entity.custom.kart.KartEntity;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class SpoilerStreetModel<T extends KartEntity> extends SinglePartEntityModel<T> {

	public static final EntityModelLayer ENTITY_MODEL_LAYER = new EntityModelLayer(BoKarts.id("kartv2_spoiler_street"), "main");

	private final ModelPart spoiler;

	public SpoilerStreetModel(ModelPart root) {
		this.spoiler = root.getChild("spoiler");
	}


	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData spoiler = modelPartData.addChild("spoiler", ModelPartBuilder.create().uv(10, 27).cuboid(3.5F, -10.85F, 15.75F, 1.0F, 1.0F, 2.0F, new Dilation(-0.25F))
		.uv(3, 27).cuboid(-4.5F, -10.85F, 15.75F, 1.0F, 1.0F, 2.0F, new Dilation(-0.25F))
		.uv(0, 0).cuboid(-11.0F, -11.05F, 15.4F, 22.0F, 1.0F, 4.0F, new Dilation(-0.25F))
		.uv(0, 6).cuboid(-11.5F, -11.35F, 14.9F, 1.0F, 2.0F, 5.0F, new Dilation(-0.25F))
		.uv(13, 6).cuboid(10.5F, -11.35F, 14.9F, 1.0F, 2.0F, 5.0F, new Dilation(-0.25F)), ModelTransform.pivot(0.0F, 23.0F, -1.0F));

		ModelPartData cube_r1 = spoiler.addChild("cube_r1", ModelPartBuilder.create().uv(3, 16).cuboid(-0.5F, -5.0F, -1.0F, 1.0F, 8.0F, 2.0F, new Dilation(-0.25F))
		.uv(10, 16).cuboid(7.5F, -5.0F, -1.0F, 1.0F, 8.0F, 2.0F, new Dilation(-0.25F)), ModelTransform.of(-4.0F, -6.0F, 15.0F, -0.3927F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}


	@Override
	public ModelPart getPart() {
		return spoiler;
	}

	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		return;
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
		spoiler.render(matrices, vertexConsumer, light, overlay, color);
	}
}