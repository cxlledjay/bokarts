// Made with Blockbench 5.2.2
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class AeroTypeOneModel extends EntityModel<Entity> {
	private final ModelPart aero;
	private final ModelPart rear;
	private final ModelPart front;
	public AeroTypeOneModel(ModelPart root) {
		this.aero = root.getChild("aero");
		this.rear = this.aero.getChild("rear");
		this.front = this.aero.getChild("front");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData aero = modelPartData.addChild("aero", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 23.0F, -1.0F));

		ModelPartData rear = aero.addChild("rear", ModelPartBuilder.create().uv(6, 59).cuboid(-8.0F, -2.25F, 14.0F, 16.0F, 1.0F, 3.0F, new Dilation(-0.25F))
		.uv(0, 75).cuboid(5.5F, -2.25F, 9.0F, 1.0F, 3.0F, 7.0F, new Dilation(-0.25F))
		.uv(17, 75).cuboid(3.5F, -2.25F, 9.0F, 1.0F, 3.0F, 7.0F, new Dilation(-0.25F))
		.uv(34, 75).cuboid(1.5F, -2.25F, 9.0F, 1.0F, 3.0F, 7.0F, new Dilation(-0.25F))
		.uv(34, 64).cuboid(-6.5F, -2.25F, 9.0F, 1.0F, 3.0F, 7.0F, new Dilation(-0.25F))
		.uv(17, 64).cuboid(-4.5F, -2.25F, 9.0F, 1.0F, 3.0F, 7.0F, new Dilation(-0.25F))
		.uv(0, 64).cuboid(-2.5F, -2.25F, 9.0F, 1.0F, 3.0F, 7.0F, new Dilation(-0.25F)), ModelTransform.pivot(0.0F, 0.0F, -1.0F));

		ModelPartData front = aero.addChild("front", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, -4.35F, -13.65F, 4.0F, 2.0F, 7.0F, new Dilation(0.0F))
		.uv(25, 16).cuboid(-5.5F, -1.0F, -24.4F, 11.0F, 1.0F, 6.0F, new Dilation(-0.25F))
		.uv(20, 25).cuboid(9.8F, -2.0F, -23.7F, 1.0F, 2.0F, 7.0F, new Dilation(-0.2F))
		.uv(11, 29).cuboid(9.8F, -3.1F, -20.3F, 1.0F, 2.0F, 3.0F, new Dilation(-0.195F))
		.uv(11, 39).cuboid(-10.8F, -3.1F, -20.3F, 1.0F, 2.0F, 3.0F, new Dilation(-0.195F))
		.uv(20, 35).cuboid(-10.8F, -2.0F, -23.7F, 1.0F, 2.0F, 7.0F, new Dilation(-0.2F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r1 = front.addChild("cube_r1", ModelPartBuilder.create().uv(60, 16).cuboid(-2.46F, -2.5F, -1.75F, 6.0F, 1.0F, 6.0F, new Dilation(-0.25F)), ModelTransform.of(-8.2F, 1.5F, -22.25F, 0.0F, 0.1309F, 0.0F));

		ModelPartData cube_r2 = front.addChild("cube_r2", ModelPartBuilder.create().uv(0, 38).cuboid(-0.5F, -1.0F, -2.0F, 1.0F, 2.0F, 4.0F, new Dilation(-0.205F))
		.uv(0, 28).cuboid(20.1F, -1.0F, -2.0F, 1.0F, 2.0F, 4.0F, new Dilation(-0.205F)), ModelTransform.of(-10.3F, -1.6F, -21.55F, 0.3054F, 0.0F, 0.0F));

		ModelPartData cube_r3 = front.addChild("cube_r3", ModelPartBuilder.create().uv(0, 16).cuboid(-3.54F, -2.5F, -1.75F, 6.0F, 1.0F, 6.0F, new Dilation(-0.25F)), ModelTransform.of(8.2F, 1.5F, -22.25F, 0.0F, -0.1309F, 0.0F));

		ModelPartData cube_r4 = front.addChild("cube_r4", ModelPartBuilder.create().uv(50, 2).cuboid(-2.0F, 1.5F, -4.5F, 4.0F, 1.0F, 7.0F, new Dilation(0.0F))
		.uv(23, 0).cuboid(-2.0F, 0.5F, -6.5F, 4.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -3.85F, -16.15F, 0.3927F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 128, 128);
	}
	@Override
	public void setAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		aero.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}