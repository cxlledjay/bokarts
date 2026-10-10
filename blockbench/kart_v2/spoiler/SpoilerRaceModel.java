// Made with Blockbench 5.2.2
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class SpoilerRaceModel extends EntityModel<Entity> {
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
	public void setAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		spoiler.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}