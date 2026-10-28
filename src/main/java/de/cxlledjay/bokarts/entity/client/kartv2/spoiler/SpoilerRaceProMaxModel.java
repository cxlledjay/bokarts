package de.cxlledjay.bokarts.entity.client.kartv2.spoiler;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.entity.custom.kart.KartEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class SpoilerRaceProMaxModel<T extends KartEntity> extends SinglePartEntityModel<T> {

	public static final EntityModelLayer ENTITY_MODEL_LAYER = new EntityModelLayer(BoKarts.id("kartv2_spoiler_race_pro_max"), "main");

	private final ModelPart spoiler_pro_max;

	public SpoilerRaceProMaxModel(ModelPart root) {
		this.spoiler_pro_max = root.getChild("spoiler_pro_max");
	}


	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData spoiler_pro_max = modelPartData.addChild("spoiler_pro_max", ModelPartBuilder.create().uv(0, 33).cuboid(3.5F, -13.75F, 17.05F, 1.0F, 4.0F, 8.0F, new Dilation(-0.25F))
				.uv(18, 33).cuboid(-4.5F, -13.75F, 17.05F, 1.0F, 4.0F, 8.0F, new Dilation(-0.25F))
				.uv(0, 0).cuboid(-16.0F, -11.15F, 17.5F, 32.0F, 1.0F, 8.0F, new Dilation(-0.25F))
				.uv(0, 9).cuboid(-16.0F, -13.15F, 18.25F, 32.0F, 1.0F, 8.0F, new Dilation(-0.25F))
				.uv(0, 18).cuboid(15.5F, -13.75F, 17.0F, 1.0F, 5.0F, 10.0F, new Dilation(-0.25F))
				.uv(22, 18).cuboid(-16.5F, -13.75F, 17.0F, 1.0F, 5.0F, 10.0F, new Dilation(-0.25F)), ModelTransform.pivot(0.0F, 22.5F, -1.75F));

		ModelPartData cube_r1 = spoiler_pro_max.addChild("cube_r1", ModelPartBuilder.create().uv(10, 45).cuboid(-0.5F, -8.0F, -1.0F, 1.0F, 12.0F, 4.0F, new Dilation(-0.25F))
				.uv(0, 45).cuboid(7.5F, -8.0F, -1.0F, 1.0F, 12.0F, 4.0F, new Dilation(-0.25F)), ModelTransform.of(-4.0F, -6.0F, 15.0F, -0.3927F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 128, 128);
	}


	@Override
	public ModelPart getPart() {
		return spoiler_pro_max;
	}

	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		return;
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
		spoiler_pro_max.render(matrices, vertexConsumer, light, overlay, color);
	}
}