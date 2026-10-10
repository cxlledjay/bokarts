// Made with Blockbench 5.2.2
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class AeroRaceModel extends EntityModel<Entity> {
	private final ModelPart aero;
	private final ModelPart front;
	private final ModelPart rear;
	private final ModelPart skirts;
	public AeroRaceModel(ModelPart root) {
		this.aero = root.getChild("aero");
		this.front = this.aero.getChild("front");
		this.rear = this.aero.getChild("rear");
		this.skirts = this.aero.getChild("skirts");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData aero = modelPartData.addChild("aero", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 23.0F, -1.0F));

		ModelPartData front = aero.addChild("front", ModelPartBuilder.create().uv(10, 1).cuboid(-5.5F, -5.0F, -18.5F, 11.0F, 1.0F, 3.0F, new Dilation(0.0F))
		.uv(47, 6).cuboid(8.25F, -5.0F, -16.75F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F))
		.uv(0, 6).cuboid(-11.25F, -5.0F, -16.75F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 4.0F, 0.0F));

		ModelPartData cube_r1 = front.addChild("cube_r1", ModelPartBuilder.create().uv(5, 0).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(-0.2F))
		.uv(1, 0).cuboid(-9.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.of(4.5F, -6.3F, -17.1F, -0.5236F, 0.0F, 0.0F));

		ModelPartData cube_r2 = front.addChild("cube_r2", ModelPartBuilder.create().uv(13, 6).cuboid(-1.46F, -2.5F, -1.75F, 5.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-8.2F, -2.5F, -15.65F, 0.0F, 0.3491F, 0.0F));

		ModelPartData cube_r3 = front.addChild("cube_r3", ModelPartBuilder.create().uv(30, 6).cuboid(-3.54F, -2.5F, -1.75F, 5.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(8.2F, -2.5F, -15.65F, 0.0F, -0.3491F, 0.0F));

		ModelPartData rear = aero.addChild("rear", ModelPartBuilder.create().uv(34, 45).cuboid(-6.0F, -2.25F, 14.0F, 12.0F, 1.0F, 3.0F, new Dilation(-0.25F))
		.uv(40, 54).cuboid(3.5F, -2.25F, 9.0F, 1.0F, 3.0F, 7.0F, new Dilation(-0.25F))
		.uv(0, 54).cuboid(-4.5F, -2.25F, 9.0F, 1.0F, 3.0F, 7.0F, new Dilation(-0.25F))
		.uv(10, 50).cuboid(-2.5F, -2.25F, 9.0F, 1.0F, 3.0F, 7.0F, new Dilation(-0.25F))
		.uv(30, 50).cuboid(1.5F, -2.25F, 9.0F, 1.0F, 3.0F, 7.0F, new Dilation(-0.25F))
		.uv(20, 46).cuboid(-0.5F, -2.25F, 9.0F, 1.0F, 3.0F, 7.0F, new Dilation(-0.25F)), ModelTransform.pivot(0.0F, 0.0F, -1.0F));

		ModelPartData skirts = aero.addChild("skirts", ModelPartBuilder.create().uv(0, 18).cuboid(8.0F, -0.75F, -16.75F, 4.0F, 1.0F, 14.0F, new Dilation(-0.245F))
		.uv(23, 20).cuboid(27.5F, -0.75F, -16.75F, 4.0F, 1.0F, 14.0F, new Dilation(-0.245F)), ModelTransform.pivot(-19.75F, 0.0F, 11.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}
	@Override
	public void setAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		aero.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}