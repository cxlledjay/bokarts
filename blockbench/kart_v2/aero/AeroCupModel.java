// Made with Blockbench 5.2.2
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class AeroCupModel extends EntityModel<Entity> {
	private final ModelPart aero;
	private final ModelPart front;
	private final ModelPart rear;
	private final ModelPart skirts;
	public AeroCupModel(ModelPart root) {
		this.aero = root.getChild("aero");
		this.front = this.aero.getChild("front");
		this.rear = this.aero.getChild("rear");
		this.skirts = this.aero.getChild("skirts");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData aero = modelPartData.addChild("aero", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 23.0F, -1.0F));

		ModelPartData front = aero.addChild("front", ModelPartBuilder.create().uv(40, 7).cuboid(-5.5F, -4.75F, -20.5F, 11.0F, 1.0F, 5.0F, new Dilation(-0.245F))
		.uv(96, 7).cuboid(8.5F, -4.75F, -18.75F, 3.0F, 1.0F, 5.0F, new Dilation(-0.245F))
		.uv(0, 7).cuboid(-11.5F, -4.75F, -18.75F, 3.0F, 1.0F, 5.0F, new Dilation(-0.245F)), ModelTransform.pivot(0.0F, 4.0F, 0.0F));

		ModelPartData cube_r1 = front.addChild("cube_r1", ModelPartBuilder.create().uv(17, 7).cuboid(-1.46F, -2.5F, -3.75F, 6.0F, 1.0F, 5.0F, new Dilation(-0.245F)), ModelTransform.of(-8.05F, -2.25F, -15.5F, 0.0F, 0.3491F, 0.0F));

		ModelPartData cube_r2 = front.addChild("cube_r2", ModelPartBuilder.create().uv(82, 0).cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(-0.1F))
		.uv(26, 0).cuboid(-9.5F, -3.0F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(-0.1F)), ModelTransform.of(4.5F, -5.85F, -17.9F, -0.6545F, 0.0F, 0.0F));

		ModelPartData cube_r3 = front.addChild("cube_r3", ModelPartBuilder.create().uv(73, 7).cuboid(-4.54F, -2.5F, -3.75F, 6.0F, 1.0F, 5.0F, new Dilation(-0.245F)), ModelTransform.of(8.05F, -2.25F, -15.5F, 0.0F, -0.3491F, 0.0F));

		ModelPartData rear = aero.addChild("rear", ModelPartBuilder.create().uv(0, 83).cuboid(-8.0F, -2.25F, 11.0F, 16.0F, 1.0F, 5.0F, new Dilation(-0.25F))
		.uv(85, 90).cuboid(3.5F, -2.25F, 9.0F, 1.0F, 3.0F, 7.0F, new Dilation(-0.25F))
		.uv(102, 90).cuboid(5.5F, -2.25F, 9.0F, 1.0F, 3.0F, 7.0F, new Dilation(-0.25F))
		.uv(17, 90).cuboid(-4.5F, -2.25F, 9.0F, 1.0F, 3.0F, 7.0F, new Dilation(-0.25F))
		.uv(0, 90).cuboid(-6.5F, -2.25F, 9.0F, 1.0F, 3.0F, 7.0F, new Dilation(-0.25F))
		.uv(34, 90).cuboid(-2.5F, -2.25F, 9.0F, 1.0F, 3.0F, 7.0F, new Dilation(-0.25F))
		.uv(68, 90).cuboid(1.5F, -2.25F, 9.0F, 1.0F, 3.0F, 7.0F, new Dilation(-0.25F))
		.uv(51, 90).cuboid(-0.5F, -2.25F, 9.0F, 1.0F, 3.0F, 7.0F, new Dilation(-0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData skirts = aero.addChild("skirts", ModelPartBuilder.create().uv(0, 33).cuboid(27.75F, -0.75F, -16.75F, 4.0F, 1.0F, 14.0F, new Dilation(-0.245F))
		.uv(37, 32).cuboid(31.25F, -1.75F, -16.75F, 1.0F, 2.0F, 14.0F, new Dilation(-0.245F))
		.uv(37, 49).cuboid(7.25F, -1.75F, -16.75F, 1.0F, 2.0F, 14.0F, new Dilation(-0.245F))
		.uv(0, 50).cuboid(7.75F, -0.75F, -16.75F, 4.0F, 1.0F, 14.0F, new Dilation(-0.245F)), ModelTransform.pivot(-19.75F, 0.0F, 11.0F));
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