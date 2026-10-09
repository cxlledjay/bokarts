// Made with Blockbench 5.2.2
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class WheelsNormal extends EntityModel<Entity> {
	private final ModelPart wheels;
	private final ModelPart rear_axle;
	private final ModelPart rear_left;
	private final ModelPart tire3;
	private final ModelPart rim3;
	private final ModelPart rear_right;
	private final ModelPart tire4;
	private final ModelPart rim4;
	private final ModelPart front_axle;
	private final ModelPart front_left;
	private final ModelPart tire;
	private final ModelPart rim;
	private final ModelPart front_right;
	private final ModelPart tire2;
	private final ModelPart rim2;
	public WheelsNormal(ModelPart root) {
		this.wheels = root.getChild("wheels");
		this.rear_axle = this.wheels.getChild("rear_axle");
		this.rear_left = this.rear_axle.getChild("rear_left");
		this.tire3 = this.rear_left.getChild("tire3");
		this.rim3 = this.rear_left.getChild("rim3");
		this.rear_right = this.rear_axle.getChild("rear_right");
		this.tire4 = this.rear_right.getChild("tire4");
		this.rim4 = this.rear_right.getChild("rim4");
		this.front_axle = this.wheels.getChild("front_axle");
		this.front_left = this.front_axle.getChild("front_left");
		this.tire = this.front_left.getChild("tire");
		this.rim = this.front_left.getChild("rim");
		this.front_right = this.front_axle.getChild("front_right");
		this.tire2 = this.front_right.getChild("tire2");
		this.rim2 = this.front_right.getChild("rim2");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData wheels = modelPartData.addChild("wheels", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 23.0F, -1.0F));

		ModelPartData rear_axle = wheels.addChild("rear_axle", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -2.5F, 12.0F));

		ModelPartData rear_left = rear_axle.addChild("rear_left", ModelPartBuilder.create(), ModelTransform.pivot(9.2F, 0.0F, 0.0F));

		ModelPartData tire3 = rear_left.addChild("tire3", ModelPartBuilder.create().uv(19, 28).cuboid(9.0F, -8.0F, 0.5F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F))
		.uv(19, 46).cuboid(9.0F, -2.4F, 0.5F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.pivot(-10.0F, 4.7F, -1.0F));

		ModelPartData cube_r1 = tire3.addChild("cube_r1", ModelPartBuilder.create().uv(19, 42).cuboid(-1.0F, -0.51F, -0.48F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -4.7F, -1.83F, -1.5708F, 0.0F, 0.0F));

		ModelPartData cube_r2 = tire3.addChild("cube_r2", ModelPartBuilder.create().uv(19, 52).cuboid(-1.0F, -0.56F, -0.6F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -3.71F, 3.7F, 1.1781F, 0.0F, 0.0F));

		ModelPartData cube_r3 = tire3.addChild("cube_r3", ModelPartBuilder.create().uv(19, 50).cuboid(-1.0F, -0.5148F, -0.5135F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -2.7252F, 3.0135F, 0.7854F, 0.0F, 0.0F));

		ModelPartData cube_r4 = tire3.addChild("cube_r4", ModelPartBuilder.create().uv(19, 48).cuboid(-1.0F, -0.935F, -0.99F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -1.9F, 2.7F, 0.3927F, 0.0F, 0.0F));

		ModelPartData cube_r5 = tire3.addChild("cube_r5", ModelPartBuilder.create().uv(19, 40).cuboid(-1.0F, -0.935F, -0.01F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -1.9F, -0.7F, -0.3927F, 0.0F, 0.0F));

		ModelPartData cube_r6 = tire3.addChild("cube_r6", ModelPartBuilder.create().uv(19, 38).cuboid(-1.0F, -0.5148F, -0.4865F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -2.7252F, -1.0135F, -0.7854F, 0.0F, 0.0F));

		ModelPartData cube_r7 = tire3.addChild("cube_r7", ModelPartBuilder.create().uv(19, 36).cuboid(-1.0F, -0.56F, -0.4F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -3.71F, -1.7F, -1.1781F, 0.0F, 0.0F));

		ModelPartData cube_r8 = tire3.addChild("cube_r8", ModelPartBuilder.create().uv(19, 34).cuboid(-1.0F, -0.44F, -0.4F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -5.69F, -1.7F, 1.1781F, 0.0F, 0.0F));

		ModelPartData cube_r9 = tire3.addChild("cube_r9", ModelPartBuilder.create().uv(19, 32).cuboid(-1.0F, -0.4852F, -0.4865F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -6.6748F, -1.0135F, 0.7854F, 0.0F, 0.0F));

		ModelPartData cube_r10 = tire3.addChild("cube_r10", ModelPartBuilder.create().uv(19, 30).cuboid(-1.0F, -0.065F, -0.01F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -7.5F, -0.7F, 0.3927F, 0.0F, 0.0F));

		ModelPartData cube_r11 = tire3.addChild("cube_r11", ModelPartBuilder.create().uv(19, 26).cuboid(-1.0F, -0.065F, -0.99F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -7.5F, 2.7F, -0.3927F, 0.0F, 0.0F));

		ModelPartData cube_r12 = tire3.addChild("cube_r12", ModelPartBuilder.create().uv(19, 24).cuboid(-1.0F, -0.4852F, -0.5135F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -6.6748F, 3.0135F, -0.7854F, 0.0F, 0.0F));

		ModelPartData cube_r13 = tire3.addChild("cube_r13", ModelPartBuilder.create().uv(19, 22).cuboid(-1.0F, -0.44F, -0.6F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -5.69F, 3.7F, -1.1781F, 0.0F, 0.0F));

		ModelPartData cube_r14 = tire3.addChild("cube_r14", ModelPartBuilder.create().uv(19, 44).cuboid(-1.0F, -0.51F, -0.52F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -4.7F, 3.83F, 1.5708F, 0.0F, 0.0F));

		ModelPartData rim3 = rear_left.addChild("rim3", ModelPartBuilder.create(), ModelTransform.pivot(-9.0F, 4.7F, -1.0F));

		ModelPartData cube_r15 = rim3.addChild("cube_r15", ModelPartBuilder.create().uv(19, 0).cuboid(-2.5F, -3.0F, -0.5F, 3.0F, 6.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(11.25F, -4.7F, 1.0F, 0.5236F, 0.0F, 0.0F));

		ModelPartData cube_r16 = rim3.addChild("cube_r16", ModelPartBuilder.create().uv(19, 14).cuboid(-2.5F, -4.0F, -0.5F, 3.0F, 6.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(11.25F, -4.7F, 0.0F, -1.5708F, 0.0F, 0.0F));

		ModelPartData cube_r17 = rim3.addChild("cube_r17", ModelPartBuilder.create().uv(19, 7).cuboid(-2.5F, -3.0F, -0.5F, 3.0F, 6.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(11.25F, -4.7F, 1.0F, -0.5236F, 0.0F, 0.0F));

		ModelPartData rear_right = rear_axle.addChild("rear_right", ModelPartBuilder.create(), ModelTransform.pivot(-9.2F, 0.0F, 0.0F));

		ModelPartData tire4 = rear_right.addChild("tire4", ModelPartBuilder.create().uv(30, 52).cuboid(-13.0F, -8.0F, 0.5F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F))
		.uv(30, 22).cuboid(-13.0F, -2.4F, 0.5F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.pivot(10.0F, 4.7F, -1.0F));

		ModelPartData cube_r18 = tire4.addChild("cube_r18", ModelPartBuilder.create().uv(30, 30).cuboid(-3.0F, -0.51F, -0.48F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -4.7F, -1.83F, -1.5708F, 0.0F, 0.0F));

		ModelPartData cube_r19 = tire4.addChild("cube_r19", ModelPartBuilder.create().uv(30, 28).cuboid(-3.0F, -0.56F, -0.6F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -3.71F, 3.7F, 1.1781F, 0.0F, 0.0F));

		ModelPartData cube_r20 = tire4.addChild("cube_r20", ModelPartBuilder.create().uv(30, 26).cuboid(-3.0F, -0.5148F, -0.5135F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -2.7252F, 3.0135F, 0.7854F, 0.0F, 0.0F));

		ModelPartData cube_r21 = tire4.addChild("cube_r21", ModelPartBuilder.create().uv(30, 24).cuboid(-3.0F, -0.935F, -0.99F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -1.9F, 2.7F, 0.3927F, 0.0F, 0.0F));

		ModelPartData cube_r22 = tire4.addChild("cube_r22", ModelPartBuilder.create().uv(30, 38).cuboid(-3.0F, -0.935F, -0.01F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -1.9F, -0.7F, -0.3927F, 0.0F, 0.0F));

		ModelPartData cube_r23 = tire4.addChild("cube_r23", ModelPartBuilder.create().uv(30, 42).cuboid(-3.0F, -0.5148F, -0.4865F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -2.7252F, -1.0135F, -0.7854F, 0.0F, 0.0F));

		ModelPartData cube_r24 = tire4.addChild("cube_r24", ModelPartBuilder.create().uv(30, 32).cuboid(-3.0F, -0.56F, -0.4F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -3.71F, -1.7F, -1.1781F, 0.0F, 0.0F));

		ModelPartData cube_r25 = tire4.addChild("cube_r25", ModelPartBuilder.create().uv(30, 34).cuboid(-3.0F, -0.44F, -0.4F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -5.69F, -1.7F, 1.1781F, 0.0F, 0.0F));

		ModelPartData cube_r26 = tire4.addChild("cube_r26", ModelPartBuilder.create().uv(30, 36).cuboid(-3.0F, -0.4852F, -0.4865F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -6.6748F, -1.0135F, 0.7854F, 0.0F, 0.0F));

		ModelPartData cube_r27 = tire4.addChild("cube_r27", ModelPartBuilder.create().uv(30, 40).cuboid(-3.0F, -0.065F, -0.01F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -7.5F, -0.7F, 0.3927F, 0.0F, 0.0F));

		ModelPartData cube_r28 = tire4.addChild("cube_r28", ModelPartBuilder.create().uv(30, 50).cuboid(-3.0F, -0.065F, -0.99F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -7.5F, 2.7F, -0.3927F, 0.0F, 0.0F));

		ModelPartData cube_r29 = tire4.addChild("cube_r29", ModelPartBuilder.create().uv(30, 48).cuboid(-3.0F, -0.4852F, -0.5135F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -6.6748F, 3.0135F, -0.7854F, 0.0F, 0.0F));

		ModelPartData cube_r30 = tire4.addChild("cube_r30", ModelPartBuilder.create().uv(30, 46).cuboid(-3.0F, -0.44F, -0.6F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -5.69F, 3.7F, -1.1781F, 0.0F, 0.0F));

		ModelPartData cube_r31 = tire4.addChild("cube_r31", ModelPartBuilder.create().uv(30, 44).cuboid(-3.0F, -0.51F, -0.52F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -4.7F, 3.83F, 1.5708F, 0.0F, 0.0F));

		ModelPartData rim4 = rear_right.addChild("rim4", ModelPartBuilder.create(), ModelTransform.pivot(9.0F, 4.7F, -1.0F));

		ModelPartData cube_r32 = rim4.addChild("cube_r32", ModelPartBuilder.create().uv(30, 7).cuboid(-0.5F, -3.0F, -0.5F, 3.0F, 6.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(-11.25F, -4.7F, 1.0F, 0.5236F, 0.0F, 0.0F));

		ModelPartData cube_r33 = rim4.addChild("cube_r33", ModelPartBuilder.create().uv(30, 0).cuboid(-0.5F, -4.0F, -0.5F, 3.0F, 6.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(-11.25F, -4.7F, 0.0F, -1.5708F, 0.0F, 0.0F));

		ModelPartData cube_r34 = rim4.addChild("cube_r34", ModelPartBuilder.create().uv(30, 14).cuboid(-0.5F, -3.0F, -0.5F, 3.0F, 6.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(-11.25F, -4.7F, 1.0F, -0.5236F, 0.0F, 0.0F));

		ModelPartData front_axle = wheels.addChild("front_axle", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -2.5F, -9.5F));

		ModelPartData front_left = front_axle.addChild("front_left", ModelPartBuilder.create(), ModelTransform.pivot(9.2F, 0.0F, 0.0F));

		ModelPartData tire = front_left.addChild("tire", ModelPartBuilder.create().uv(0, 52).cuboid(9.0F, -8.0F, 0.5F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F))
		.uv(0, 22).cuboid(9.0F, -2.4F, 0.5F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.pivot(-10.0F, 4.7F, -1.0F));

		ModelPartData cube_r35 = tire.addChild("cube_r35", ModelPartBuilder.create().uv(0, 24).cuboid(-1.0F, -0.51F, -0.48F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -4.7F, -1.83F, -1.5708F, 0.0F, 0.0F));

		ModelPartData cube_r36 = tire.addChild("cube_r36", ModelPartBuilder.create().uv(0, 30).cuboid(-1.0F, -0.56F, -0.6F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -3.71F, 3.7F, 1.1781F, 0.0F, 0.0F));

		ModelPartData cube_r37 = tire.addChild("cube_r37", ModelPartBuilder.create().uv(0, 40).cuboid(-1.0F, -0.5148F, -0.5135F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -2.7252F, 3.0135F, 0.7854F, 0.0F, 0.0F));

		ModelPartData cube_r38 = tire.addChild("cube_r38", ModelPartBuilder.create().uv(0, 36).cuboid(-1.0F, -0.935F, -0.99F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -1.9F, 2.7F, 0.3927F, 0.0F, 0.0F));

		ModelPartData cube_r39 = tire.addChild("cube_r39", ModelPartBuilder.create().uv(0, 32).cuboid(-1.0F, -0.935F, -0.01F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -1.9F, -0.7F, -0.3927F, 0.0F, 0.0F));

		ModelPartData cube_r40 = tire.addChild("cube_r40", ModelPartBuilder.create().uv(0, 28).cuboid(-1.0F, -0.5148F, -0.4865F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -2.7252F, -1.0135F, -0.7854F, 0.0F, 0.0F));

		ModelPartData cube_r41 = tire.addChild("cube_r41", ModelPartBuilder.create().uv(0, 38).cuboid(-1.0F, -0.56F, -0.4F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -3.71F, -1.7F, -1.1781F, 0.0F, 0.0F));

		ModelPartData cube_r42 = tire.addChild("cube_r42", ModelPartBuilder.create().uv(0, 34).cuboid(-1.0F, -0.44F, -0.4F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -5.69F, -1.7F, 1.1781F, 0.0F, 0.0F));

		ModelPartData cube_r43 = tire.addChild("cube_r43", ModelPartBuilder.create().uv(0, 42).cuboid(-1.0F, -0.4852F, -0.4865F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -6.6748F, -1.0135F, 0.7854F, 0.0F, 0.0F));

		ModelPartData cube_r44 = tire.addChild("cube_r44", ModelPartBuilder.create().uv(0, 26).cuboid(-1.0F, -0.065F, -0.01F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -7.5F, -0.7F, 0.3927F, 0.0F, 0.0F));

		ModelPartData cube_r45 = tire.addChild("cube_r45", ModelPartBuilder.create().uv(0, 50).cuboid(-1.0F, -0.065F, -0.99F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -7.5F, 2.7F, -0.3927F, 0.0F, 0.0F));

		ModelPartData cube_r46 = tire.addChild("cube_r46", ModelPartBuilder.create().uv(0, 48).cuboid(-1.0F, -0.4852F, -0.5135F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -6.6748F, 3.0135F, -0.7854F, 0.0F, 0.0F));

		ModelPartData cube_r47 = tire.addChild("cube_r47", ModelPartBuilder.create().uv(0, 46).cuboid(-1.0F, -0.44F, -0.6F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -5.69F, 3.7F, -1.1781F, 0.0F, 0.0F));

		ModelPartData cube_r48 = tire.addChild("cube_r48", ModelPartBuilder.create().uv(0, 44).cuboid(-1.0F, -0.51F, -0.52F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -4.7F, 3.83F, 1.5708F, 0.0F, 0.0F));

		ModelPartData rim = front_left.addChild("rim", ModelPartBuilder.create(), ModelTransform.pivot(-10.0F, 4.7F, -1.0F));

		ModelPartData cube_r49 = rim.addChild("cube_r49", ModelPartBuilder.create().uv(0, 14).cuboid(-0.5F, -3.0F, -0.5F, 2.0F, 6.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(10.25F, -4.7F, 1.0F, 0.5236F, 0.0F, 0.0F));

		ModelPartData cube_r50 = rim.addChild("cube_r50", ModelPartBuilder.create().uv(0, 0).cuboid(-0.5F, -4.0F, -0.5F, 2.0F, 6.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(10.25F, -4.7F, 0.0F, -1.5708F, 0.0F, 0.0F));

		ModelPartData cube_r51 = rim.addChild("cube_r51", ModelPartBuilder.create().uv(0, 7).cuboid(-0.5F, -3.0F, -0.5F, 2.0F, 6.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(10.25F, -4.7F, 1.0F, -0.5236F, 0.0F, 0.0F));

		ModelPartData front_right = front_axle.addChild("front_right", ModelPartBuilder.create(), ModelTransform.pivot(-9.2F, 0.0F, 0.0F));

		ModelPartData tire2 = front_right.addChild("tire2", ModelPartBuilder.create().uv(9, 22).cuboid(-12.0F, -8.0F, 0.5F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F))
		.uv(9, 46).cuboid(-12.0F, -2.4F, 0.5F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.pivot(10.0F, 4.7F, -1.0F));

		ModelPartData cube_r52 = tire2.addChild("cube_r52", ModelPartBuilder.create().uv(9, 26).cuboid(-2.0F, -0.51F, -0.48F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -4.7F, -1.83F, -1.5708F, 0.0F, 0.0F));

		ModelPartData cube_r53 = tire2.addChild("cube_r53", ModelPartBuilder.create().uv(9, 24).cuboid(-2.0F, -0.56F, -0.6F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -3.71F, 3.7F, 1.1781F, 0.0F, 0.0F));

		ModelPartData cube_r54 = tire2.addChild("cube_r54", ModelPartBuilder.create().uv(9, 30).cuboid(-2.0F, -0.5148F, -0.5135F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -2.7252F, 3.0135F, 0.7854F, 0.0F, 0.0F));

		ModelPartData cube_r55 = tire2.addChild("cube_r55", ModelPartBuilder.create().uv(9, 52).cuboid(-2.0F, -0.935F, -0.99F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -1.9F, 2.7F, 0.3927F, 0.0F, 0.0F));

		ModelPartData cube_r56 = tire2.addChild("cube_r56", ModelPartBuilder.create().uv(9, 44).cuboid(-2.0F, -0.935F, -0.01F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -1.9F, -0.7F, -0.3927F, 0.0F, 0.0F));

		ModelPartData cube_r57 = tire2.addChild("cube_r57", ModelPartBuilder.create().uv(9, 42).cuboid(-2.0F, -0.5148F, -0.4865F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -2.7252F, -1.0135F, -0.7854F, 0.0F, 0.0F));

		ModelPartData cube_r58 = tire2.addChild("cube_r58", ModelPartBuilder.create().uv(9, 36).cuboid(-2.0F, -0.56F, -0.4F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -3.71F, -1.7F, -1.1781F, 0.0F, 0.0F));

		ModelPartData cube_r59 = tire2.addChild("cube_r59", ModelPartBuilder.create().uv(9, 40).cuboid(-2.0F, -0.44F, -0.4F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -5.69F, -1.7F, 1.1781F, 0.0F, 0.0F));

		ModelPartData cube_r60 = tire2.addChild("cube_r60", ModelPartBuilder.create().uv(9, 38).cuboid(-2.0F, -0.4852F, -0.4865F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -6.6748F, -1.0135F, 0.7854F, 0.0F, 0.0F));

		ModelPartData cube_r61 = tire2.addChild("cube_r61", ModelPartBuilder.create().uv(9, 32).cuboid(-2.0F, -0.065F, -0.01F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -7.5F, -0.7F, 0.3927F, 0.0F, 0.0F));

		ModelPartData cube_r62 = tire2.addChild("cube_r62", ModelPartBuilder.create().uv(9, 28).cuboid(-2.0F, -0.065F, -0.99F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -7.5F, 2.7F, -0.3927F, 0.0F, 0.0F));

		ModelPartData cube_r63 = tire2.addChild("cube_r63", ModelPartBuilder.create().uv(9, 48).cuboid(-2.0F, -0.4852F, -0.5135F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -6.6748F, 3.0135F, -0.7854F, 0.0F, 0.0F));

		ModelPartData cube_r64 = tire2.addChild("cube_r64", ModelPartBuilder.create().uv(9, 50).cuboid(-2.0F, -0.44F, -0.6F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -5.69F, 3.7F, -1.1781F, 0.0F, 0.0F));

		ModelPartData cube_r65 = tire2.addChild("cube_r65", ModelPartBuilder.create().uv(9, 34).cuboid(-2.0F, -0.51F, -0.52F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -4.7F, 3.83F, 1.5708F, 0.0F, 0.0F));

		ModelPartData rim2 = front_right.addChild("rim2", ModelPartBuilder.create(), ModelTransform.pivot(10.0F, 4.7F, -1.0F));

		ModelPartData cube_r66 = rim2.addChild("cube_r66", ModelPartBuilder.create().uv(9, 14).cuboid(-1.5F, -3.0F, -0.5F, 2.0F, 6.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(-10.25F, -4.7F, 1.0F, 0.5236F, 0.0F, 0.0F));

		ModelPartData cube_r67 = rim2.addChild("cube_r67", ModelPartBuilder.create().uv(9, 7).cuboid(-1.5F, -4.0F, -0.5F, 2.0F, 6.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(-10.25F, -4.7F, 0.0F, -1.5708F, 0.0F, 0.0F));

		ModelPartData cube_r68 = rim2.addChild("cube_r68", ModelPartBuilder.create().uv(9, 0).cuboid(-1.5F, -3.0F, -0.5F, 2.0F, 6.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(-10.25F, -4.7F, 1.0F, -0.5236F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}
	@Override
	public void setAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		wheels.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}