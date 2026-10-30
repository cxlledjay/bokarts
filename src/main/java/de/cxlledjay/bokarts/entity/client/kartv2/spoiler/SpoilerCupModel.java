package de.cxlledjay.bokarts.entity.client.kartv2.spoiler;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.entity.custom.kart.KartEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class SpoilerCupModel<T extends KartEntity> extends SinglePartEntityModel<T> {

	public static final EntityModelLayer ENTITY_MODEL_LAYER = new EntityModelLayer(BoKarts.id("kartv2_spoiler_cup"), "main");

	private final ModelPart spoiler;

	public SpoilerCupModel(ModelPart root) {
		this.spoiler = root.getChild("spoiler");
	}


	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData spoiler = modelPartData.addChild("spoiler", ModelPartBuilder.create().uv(35, 39).cuboid(3.5F, -11.9F, 16.35F, 1.0F, 3.0F, 6.0F, new Dilation(-0.1F))
				.uv(10, 39).cuboid(-4.5F, -11.9F, 16.35F, 1.0F, 3.0F, 6.0F, new Dilation(-0.1F))
				.uv(0, 0).cuboid(-14.0F, -11.35F, 16.85F, 28.0F, 1.0F, 6.0F, new Dilation(-0.35F))
				.uv(0, 8).cuboid(-14.0F, -9.95F, 16.35F, 28.0F, 1.0F, 6.0F, new Dilation(-0.35F))
				.uv(24, 26).cuboid(13.65F, -10.25F, 15.5F, 1.0F, 2.0F, 6.0F, new Dilation(-0.1F))
				.uv(0, 26).cuboid(13.65F, -12.05F, 17.65F, 1.0F, 2.0F, 6.0F, new Dilation(-0.1F))
				.uv(0, 16).cuboid(-14.65F, -12.05F, 17.65F, 1.0F, 2.0F, 6.0F, new Dilation(-0.1F))
				.uv(24, 16).cuboid(-14.65F, -10.25F, 15.5F, 1.0F, 2.0F, 6.0F, new Dilation(-0.1F)), ModelTransform.pivot(0.0F, 23.0F, -1.0F));

		ModelPartData cube_r1 = spoiler.addChild("cube_r1", ModelPartBuilder.create().uv(39, 19).cuboid(-0.5F, -1.0F, -3.0F, 1.0F, 2.0F, 3.0F, new Dilation(-0.1F))
				.uv(39, 29).cuboid(27.8F, -1.0F, -3.0F, 1.0F, 2.0F, 3.0F, new Dilation(-0.1F)), ModelTransform.of(-14.15F, -11.3F, 18.4F, 0.6981F, 0.0F, 0.0F));

		ModelPartData cube_r2 = spoiler.addChild("cube_r2", ModelPartBuilder.create().uv(15, 19).cuboid(-0.5F, -1.0F, -3.0F, 1.0F, 2.0F, 3.0F, new Dilation(-0.1F))
				.uv(15, 29).cuboid(27.8F, -1.0F, -3.0F, 1.0F, 2.0F, 3.0F, new Dilation(-0.1F)), ModelTransform.of(-14.15F, -10.9F, 23.05F, 0.6981F, 0.0F, 0.0F));

		ModelPartData cube_r3 = spoiler.addChild("cube_r3", ModelPartBuilder.create().uv(0, 36).cuboid(-0.5F, -6.0F, -1.0F, 1.0F, 9.0F, 3.0F, new Dilation(-0.1F))
				.uv(26, 36).cuboid(7.5F, -6.0F, -1.0F, 1.0F, 9.0F, 3.0F, new Dilation(-0.1F)), ModelTransform.of(-4.0F, -6.0F, 15.0F, -0.3927F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 128, 128);
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