// Made with Blockbench 5.2.1
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class KartLiftArms extends EntityModel<Entity> {
	private final ModelPart root;
	private final ModelPart right;
	private final ModelPart arm1;
	private final ModelPart arm2;
	private final ModelPart left;
	private final ModelPart arm3;
	private final ModelPart arm4;
	public KartLiftArms(ModelPart root) {
		this.root = root.getChild("root");
		this.right = this.root.getChild("right");
		this.arm1 = this.right.getChild("arm1");
		this.arm2 = this.right.getChild("arm2");
		this.left = this.root.getChild("left");
		this.arm3 = this.left.getChild("arm3");
		this.arm4 = this.left.getChild("arm4");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData root = modelPartData.addChild("root", ModelPartBuilder.create(), ModelTransform.of(0.0F, 24.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		ModelPartData right = root.addChild("right", ModelPartBuilder.create().uv(0, 5).cuboid(-23.0F, -17.0F, -2.0F, 2.0F, 17.0F, 4.0F, new Dilation(0.001F))
		.uv(12, 5).cuboid(-21.0F, -3.0F, -4.0F, 2.0F, 3.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r1 = right.addChild("cube_r1", ModelPartBuilder.create().uv(12, 16).cuboid(-1.0F, -3.0F, -2.0F, 2.0F, 9.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-21.4F, -8.35F, 0.0F, 0.0F, 0.0F, -0.192F));

		ModelPartData arm1 = right.addChild("arm1", ModelPartBuilder.create(), ModelTransform.of(-19.7732F, -2.1667F, 1.5002F, 0.0F, 0.3927F, 0.0F));

		ModelPartData cube_r2 = arm1.addChild("cube_r2", ModelPartBuilder.create().uv(24, 20).cuboid(0.0F, -1.0F, 0.0F, 1.0F, 2.0F, 1.0F, new Dilation(-0.2F))
		.uv(24, 16).cuboid(-1.0F, -1.3F, -1.0F, 3.0F, 1.0F, 3.0F, new Dilation(-0.4F)), ModelTransform.of(8.4401F, 0.0667F, 6.7156F, 0.0F, -0.7418F, 0.0F));

		ModelPartData cube_r3 = arm1.addChild("cube_r3", ModelPartBuilder.create().uv(0, 0).cuboid(-6.0F, -1.0F, -1.5F, 13.0F, 2.0F, 3.0F, new Dilation(-0.25F)), ModelTransform.of(4.1901F, 0.6667F, 3.4656F, 0.0F, -0.7418F, 0.0F));

		ModelPartData arm2 = right.addChild("arm2", ModelPartBuilder.create(), ModelTransform.of(-19.7732F, -2.1667F, -1.5002F, 0.0F, -0.3927F, 0.0F));

		ModelPartData cube_r4 = arm2.addChild("cube_r4", ModelPartBuilder.create().uv(24, 20).cuboid(0.0F, -1.0F, -1.0F, 1.0F, 2.0F, 1.0F, new Dilation(-0.2F))
		.uv(24, 16).cuboid(-1.0F, -1.3F, -2.0F, 3.0F, 1.0F, 3.0F, new Dilation(-0.4F)), ModelTransform.of(8.4401F, 0.0667F, -6.7156F, 0.0F, 0.7418F, 0.0F));

		ModelPartData cube_r5 = arm2.addChild("cube_r5", ModelPartBuilder.create().uv(0, 0).cuboid(-6.0F, -1.0F, -1.5F, 13.0F, 2.0F, 3.0F, new Dilation(-0.25F)), ModelTransform.of(4.1901F, 0.6667F, -3.4656F, 0.0F, 0.7418F, 0.0F));

		ModelPartData left = root.addChild("left", ModelPartBuilder.create().uv(0, 5).mirrored().cuboid(21.0F, -17.0F, -2.0F, 2.0F, 17.0F, 4.0F, new Dilation(0.001F)).mirrored(false)
		.uv(12, 5).mirrored().cuboid(19.0F, -3.0F, -4.0F, 2.0F, 3.0F, 8.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r6 = left.addChild("cube_r6", ModelPartBuilder.create().uv(12, 16).mirrored().cuboid(-1.0F, -3.0F, -2.0F, 2.0F, 9.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(21.4F, -8.35F, 0.0F, 0.0F, 0.0F, 0.192F));

		ModelPartData arm3 = left.addChild("arm3", ModelPartBuilder.create(), ModelTransform.of(19.7732F, -2.1667F, 1.5002F, 0.0F, -0.3927F, 0.0F));

		ModelPartData cube_r7 = arm3.addChild("cube_r7", ModelPartBuilder.create().uv(24, 20).mirrored().cuboid(-1.0F, -1.0F, 0.0F, 1.0F, 2.0F, 1.0F, new Dilation(-0.2F)).mirrored(false)
		.uv(24, 16).mirrored().cuboid(-2.0F, -1.3F, -1.0F, 3.0F, 1.0F, 3.0F, new Dilation(-0.4F)).mirrored(false), ModelTransform.of(-8.4401F, 0.0667F, 6.7156F, 0.0F, 0.7418F, 0.0F));

		ModelPartData cube_r8 = arm3.addChild("cube_r8", ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-7.0F, -1.0F, -1.5F, 13.0F, 2.0F, 3.0F, new Dilation(-0.25F)).mirrored(false), ModelTransform.of(-4.1901F, 0.6667F, 3.4656F, 0.0F, 0.7418F, 0.0F));

		ModelPartData arm4 = left.addChild("arm4", ModelPartBuilder.create(), ModelTransform.of(19.7732F, -2.1667F, -1.5002F, 0.0F, 0.3927F, 0.0F));

		ModelPartData cube_r9 = arm4.addChild("cube_r9", ModelPartBuilder.create().uv(24, 20).mirrored().cuboid(-1.0F, -1.0F, -1.0F, 1.0F, 2.0F, 1.0F, new Dilation(-0.2F)).mirrored(false)
		.uv(24, 16).mirrored().cuboid(-2.0F, -1.3F, -2.0F, 3.0F, 1.0F, 3.0F, new Dilation(-0.4F)).mirrored(false), ModelTransform.of(-8.4401F, 0.0667F, -6.7156F, 0.0F, -0.7418F, 0.0F));

		ModelPartData cube_r10 = arm4.addChild("cube_r10", ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-7.0F, -1.0F, -1.5F, 13.0F, 2.0F, 3.0F, new Dilation(-0.25F)).mirrored(false), ModelTransform.of(-4.1901F, 0.6667F, -3.4656F, 0.0F, -0.7418F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}
	@Override
	public void setAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		root.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}