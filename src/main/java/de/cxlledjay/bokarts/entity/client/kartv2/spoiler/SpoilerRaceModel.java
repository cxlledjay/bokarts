package de.cxlledjay.bokarts.entity.client.kartv2.spoiler;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.entity.custom.kart.KartEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class SpoilerRaceModel<T extends KartEntity> extends SinglePartEntityModel<T> {

	public static final EntityModelLayer ENTITY_MODEL_LAYER = new EntityModelLayer(BoKarts.id("kartv2_spoiler_race"), "main");

	private final ModelPart spoiler;

	public SpoilerRaceModel(ModelPart root) {
		this.spoiler = root.getChild("spoiler");
	}


	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData spoiler = modelPartData.addChild("spoiler", ModelPartBuilder.create().uv(11, 33).cuboid(3.5F, -12.75F, 16.75F, 1.0F, 3.0F, 4.0F, new Dilation(-0.25F))
		.uv(0, 33).cuboid(-4.5F, -12.75F, 16.75F, 1.0F, 3.0F, 4.0F, new Dilation(-0.25F))
		.uv(0, 0).cuboid(-13.0F, -11.55F, 17.5F, 26.0F, 1.0F, 5.0F, new Dilation(-0.25F))
		.uv(15, 7).cuboid(12.5F, -12.15F, 17.0F, 1.0F, 3.0F, 6.0F, new Dilation(-0.25F))
		.uv(0, 7).cuboid(-13.5F, -12.15F, 17.0F, 1.0F, 3.0F, 6.0F, new Dilation(-0.25F)), ModelTransform.pivot(0.0F, 23.0F, -1.0F));

		ModelPartData cube_r1 = spoiler.addChild("cube_r1", ModelPartBuilder.create().uv(1, 19).cuboid(-0.5F, -7.0F, -1.0F, 1.0F, 10.0F, 3.0F, new Dilation(-0.25F))
		.uv(12, 19).cuboid(7.5F, -7.0F, -1.0F, 1.0F, 10.0F, 3.0F, new Dilation(-0.25F)), ModelTransform.of(-4.0F, -6.0F, 15.0F, -0.3927F, 0.0F, 0.0F));
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