package de.cxlledjay.bokarts.entity.client.kartv2.spoiler;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.entity.custom.kart.KartEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class SpoilerTypeOneModel<T extends KartEntity> extends SinglePartEntityModel<T> {

	public static final EntityModelLayer ENTITY_MODEL_LAYER = new EntityModelLayer(BoKarts.id("kartv2_spoiler_type_one"), "main");

	private final ModelPart spoiler;

	public SpoilerTypeOneModel(ModelPart root) {
		this.spoiler = root.getChild("spoiler");
	}


	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData spoiler = modelPartData.addChild("spoiler", ModelPartBuilder.create().uv(23, 47).cuboid(7.0F, -4.2027F, -0.9454F, 1.0F, 11.0F, 4.0F, new Dilation(-0.25F))
				.uv(1, 53).cuboid(7.0F, -4.2027F, -4.4454F, 1.0F, 5.0F, 4.0F, new Dilation(-0.25F))
				.uv(1, 37).cuboid(-8.0F, -4.2027F, -4.4454F, 1.0F, 5.0F, 4.0F, new Dilation(-0.25F))
				.uv(23, 31).cuboid(-8.0F, -4.2027F, -0.9454F, 1.0F, 11.0F, 4.0F, new Dilation(-0.25F))
				.uv(0, 0).cuboid(-7.5F, -2.4527F, -3.9454F, 15.0F, 1.0F, 4.0F, new Dilation(-0.25F))
				.uv(34, 49).cuboid(3.0F, 3.75F, -1.75F, 2.0F, 2.0F, 4.0F, new Dilation(-0.25F))
				.uv(34, 33).cuboid(-5.0F, 3.75F, -1.75F, 2.0F, 2.0F, 4.0F, new Dilation(-0.25F))
				.uv(26, 20).cuboid(0.0F, -4.0F, -4.25F, 1.0F, 3.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 13.9527F, 18.0454F));

		ModelPartData cube_r1 = spoiler.addChild("cube_r1", ModelPartBuilder.create().uv(15, 24).cuboid(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 4.0F, new Dilation(-0.01F)), ModelTransform.of(0.5F, -2.6F, -6.0F, 0.2182F, 0.0F, 0.0F));

		ModelPartData cube_r2 = spoiler.addChild("cube_r2", ModelPartBuilder.create().uv(0, 15).cuboid(0.0F, -4.0F, -3.0F, 1.0F, 9.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 1.6F, -6.15F, -0.2618F, 0.0F, 0.0F));

		ModelPartData cube_r3 = spoiler.addChild("cube_r3", ModelPartBuilder.create().uv(34, 40).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 4.0F, new Dilation(-0.25F))
				.uv(34, 56).cuboid(7.0F, -2.0F, -1.0F, 2.0F, 2.0F, 4.0F, new Dilation(-0.25F)), ModelTransform.of(-4.0F, 7.0F, -2.75F, 0.6109F, 0.0F, 0.0F));

		ModelPartData cube_r4 = spoiler.addChild("cube_r4", ModelPartBuilder.create().uv(1, 10).cuboid(-7.0F, -1.0F, -2.0F, 15.0F, 1.0F, 3.0F, new Dilation(-0.25F)), ModelTransform.of(-0.5F, 5.2973F, 1.0546F, 0.0873F, 0.0F, 0.0F));

		ModelPartData cube_r5 = spoiler.addChild("cube_r5", ModelPartBuilder.create().uv(1, 6).cuboid(-7.0F, -1.0F, -0.3F, 15.0F, 1.0F, 3.0F, new Dilation(-0.25F)), ModelTransform.of(-0.5F, -1.4527F, 0.1046F, 0.6981F, 0.0F, 0.0F));

		ModelPartData cube_r6 = spoiler.addChild("cube_r6", ModelPartBuilder.create().uv(12, 35).cuboid(-0.5F, -3.5F, -2.0F, 1.0F, 7.0F, 4.0F, new Dilation(-0.255F))
				.uv(12, 51).cuboid(14.5F, -3.5F, -2.0F, 1.0F, 7.0F, 4.0F, new Dilation(-0.255F)), ModelTransform.of(-7.5F, 2.1973F, -0.8454F, 0.6109F, 0.0F, 0.0F));
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