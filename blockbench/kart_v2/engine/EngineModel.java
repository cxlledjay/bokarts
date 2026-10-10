// Made with Blockbench 5.2.2
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class EngineModel extends EntityModel<Entity> {
	private final ModelPart engine;
	private final ModelPart hopper;
	private final ModelPart drive_gear;
	public EngineModel(ModelPart root) {
		this.engine = root.getChild("engine");
		this.hopper = this.engine.getChild("hopper");
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
	public void setAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		engine.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}