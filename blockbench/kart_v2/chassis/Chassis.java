// Made with Blockbench 5.2.2
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class Chassis extends EntityModel<Entity> {
	private final ModelPart kart_v2;
	private final ModelPart frame;
	private final ModelPart axles;
	private final ModelPart rear_axle;
	private final ModelPart front_axle;
	private final ModelPart steering;
	private final ModelPart steering_column;
	private final ModelPart steering_wheel;
	private final ModelPart wheel;
	private final ModelPart spokes;
	private final ModelPart seat;
	private final ModelPart accessoirs;
	private final ModelPart lever;
	private final ModelPart brake;
	private final ModelPart gas;
	private final ModelPart body;
	public Chassis(ModelPart root) {
		this.kart_v2 = root.getChild("kart_v2");
		this.frame = this.kart_v2.getChild("frame");
		this.axles = this.kart_v2.getChild("axles");
		this.rear_axle = this.axles.getChild("rear_axle");
		this.front_axle = this.axles.getChild("front_axle");
		this.steering = this.kart_v2.getChild("steering");
		this.steering_column = this.steering.getChild("steering_column");
		this.steering_wheel = this.steering.getChild("steering_wheel");
		this.wheel = this.steering_wheel.getChild("wheel");
		this.spokes = this.steering_wheel.getChild("spokes");
		this.seat = this.kart_v2.getChild("seat");
		this.accessoirs = this.kart_v2.getChild("accessoirs");
		this.lever = this.accessoirs.getChild("lever");
		this.brake = this.accessoirs.getChild("brake");
		this.gas = this.accessoirs.getChild("gas");
		this.body = this.kart_v2.getChild("body");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData kart_v2 = modelPartData.addChild("kart_v2", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData frame = kart_v2.addChild("frame", ModelPartBuilder.create().uv(18, 126).cuboid(-5.0F, -2.0F, -15.25F, 10.0F, 1.0F, 1.0F, new Dilation(0.0005F))
		.uv(0, 117).cuboid(-2.0F, -2.0F, -15.0F, 1.0F, 1.0F, 6.0F, new Dilation(0.0005F))
		.uv(16, 117).cuboid(1.0F, -2.0F, -15.0F, 1.0F, 1.0F, 6.0F, new Dilation(0.0005F))
		.uv(101, 121).cuboid(3.75F, -2.0F, -6.0F, 1.0F, 1.0F, 6.0F, new Dilation(0.0F))
		.uv(78, 98).cuboid(7.0F, -2.0F, 2.75F, 1.0F, 1.0F, 13.0F, new Dilation(0.0F))
		.uv(61, 101).cuboid(-8.0F, -2.0F, 2.75F, 1.0F, 1.0F, 13.0F, new Dilation(0.0F))
		.uv(101, 113).cuboid(-4.75F, -2.0F, -6.0F, 1.0F, 1.0F, 6.0F, new Dilation(0.0F))
		.uv(32, 122).cuboid(-7.0F, -2.0F, 3.0F, 14.0F, 1.0F, 1.0F, new Dilation(0.001F))
		.uv(60, 126).cuboid(3.75F, -2.0F, -4.0F, 4.0F, 1.0F, 1.0F, new Dilation(0.007F))
		.uv(72, 126).cuboid(-7.75F, -2.0F, -4.0F, 4.0F, 1.0F, 1.0F, new Dilation(0.007F))
		.uv(29, 102).cuboid(-4.5F, -2.0F, 3.75F, 1.0F, 1.0F, 12.0F, new Dilation(0.0F))
		.uv(45, 99).cuboid(3.5F, -2.0F, 3.75F, 1.0F, 1.0F, 12.0F, new Dilation(0.0F))
		.uv(12, 98).cuboid(3.5F, -5.0F, 10.75F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
		.uv(12, 103).cuboid(3.5F, -5.0F, 14.25F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
		.uv(0, 95).cuboid(-4.5F, -1.6F, -15.0F, 9.0F, 1.0F, 19.0F, new Dilation(-0.45F))
		.uv(0, 96).cuboid(-2.0F, -5.0F, -6.0F, 4.0F, 1.0F, 1.0F, new Dilation(-0.102F)), ModelTransform.pivot(0.0F, -2.0F, -1.0F));

		ModelPartData cube_r1 = frame.addChild("cube_r1", ModelPartBuilder.create().uv(6, 99).cuboid(-0.5F, -3.5F, -1.5F, 1.0F, 7.0F, 1.0F, new Dilation(-0.101F)), ModelTransform.of(-2.0638F, -4.1919F, -4.5F, 0.0F, 0.0F, 0.7505F));

		ModelPartData cube_r2 = frame.addChild("cube_r2", ModelPartBuilder.create().uv(0, 99).cuboid(-0.5F, -3.5F, -1.5F, 1.0F, 7.0F, 1.0F, new Dilation(-0.1F)), ModelTransform.of(2.0638F, -4.1919F, -4.5F, 0.0F, 0.0F, -0.7505F));

		ModelPartData cube_r3 = frame.addChild("cube_r3", ModelPartBuilder.create().uv(116, 114).cuboid(-2.9042F, -0.5F, -5.8991F, 1.0F, 1.0F, 5.0F, new Dilation(0.0005F)), ModelTransform.of(-5.15F, -1.5F, -2.75F, 0.0F, 2.3562F, 0.0F));

		ModelPartData cube_r4 = frame.addChild("cube_r4", ModelPartBuilder.create().uv(42, 126).cuboid(-3.0F, -0.5F, -0.5F, 7.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-6.25F, -1.5F, -11.3509F, 0.0F, 1.1432F, 0.0F));

		ModelPartData cube_r5 = frame.addChild("cube_r5", ModelPartBuilder.create().uv(88, 114).cuboid(-0.4535F, -0.5F, -2.5F, 1.0F, 1.0F, 5.0F, new Dilation(0.0005F)), ModelTransform.of(-5.8964F, -1.5F, -7.3964F, 0.0F, 0.7854F, 0.0F));

		ModelPartData cube_r6 = frame.addChild("cube_r6", ModelPartBuilder.create().uv(0, 126).cuboid(-4.0F, -0.5F, -0.5F, 7.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(6.25F, -1.5F, -11.3509F, 0.0F, -1.1432F, 0.0F));

		ModelPartData cube_r7 = frame.addChild("cube_r7", ModelPartBuilder.create().uv(116, 122).cuboid(-0.5F, -0.5F, -3.0F, 1.0F, 1.0F, 5.0F, new Dilation(0.0005F)), ModelTransform.of(5.5F, -1.5F, 1.0F, 0.0F, -2.3562F, 0.0F));

		ModelPartData cube_r8 = frame.addChild("cube_r8", ModelPartBuilder.create().uv(88, 122).cuboid(-0.5465F, -0.5F, -2.5F, 1.0F, 1.0F, 5.0F, new Dilation(0.0005F)), ModelTransform.of(5.8964F, -1.5F, -7.3964F, 0.0F, -0.7854F, 0.0F));

		ModelPartData axles = kart_v2.addChild("axles", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -1.0F, -1.0F));

		ModelPartData rear_axle = axles.addChild("rear_axle", ModelPartBuilder.create().uv(8, 61).cuboid(-8.25F, -0.5F, -0.5F, 22.0F, 1.0F, 1.0F, new Dilation(-0.25F))
		.uv(0, 59).cuboid(-3.25F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.75F, -2.5F, 12.0F));

		ModelPartData front_axle = axles.addChild("front_axle", ModelPartBuilder.create().uv(56, 61).cuboid(-10.0F, -0.5F, -0.5F, 20.0F, 1.0F, 1.0F, new Dilation(-0.25F)), ModelTransform.pivot(0.0F, -2.5F, -9.5F));

		ModelPartData steering = kart_v2.addChild("steering", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -1.0F, -1.0F));

		ModelPartData steering_column = steering.addChild("steering_column", ModelPartBuilder.create().uv(0, 66).cuboid(-9.5F, -3.0F, -11.25F, 19.0F, 1.0F, 1.0F, new Dilation(-0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData steering_wheel = steering.addChild("steering_wheel", ModelPartBuilder.create(), ModelTransform.of(-0.0198F, -9.8981F, -0.4686F, 1.5708F, -0.9599F, -1.5708F));

		ModelPartData cube_r9 = steering_wheel.addChild("cube_r9", ModelPartBuilder.create().uv(0, 69).cuboid(-0.6F, -0.5F, -7.011F, 1.0F, 1.0F, 14.0F, new Dilation(-0.75F)), ModelTransform.of(-6.4802F, 0.0981F, -0.0314F, 1.5708F, 0.0F, 1.5708F));

		ModelPartData wheel = steering_wheel.addChild("wheel", ModelPartBuilder.create().uv(44, 66).cuboid(-0.5F, -3.655F, -0.15F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F))
		.uv(49, 66).cuboid(-0.5F, -3.655F, -0.85F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F))
		.uv(68, 79).cuboid(-0.5F, -0.85F, 2.655F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F))
		.uv(38, 73).cuboid(-0.5F, -0.15F, 2.655F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F))
		.uv(38, 76).cuboid(-0.5F, -0.85F, -3.655F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F))
		.uv(63, 82).cuboid(-0.5F, -0.15F, -3.655F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F))
		.uv(33, 82).cuboid(-0.5F, 2.655F, -0.15F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F))
		.uv(58, 79).cuboid(-0.5F, 2.655F, -0.85F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r10 = wheel.addChild("cube_r10", ModelPartBuilder.create().uv(43, 82).cuboid(-0.5F, 0.04F, -0.6522F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F))
		.uv(48, 76).cuboid(-0.5F, 0.04F, -1.3522F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(0.0F, 2.2F, -1.4978F, -0.7854F, 0.0F, 0.0F));

		ModelPartData cube_r11 = wheel.addChild("cube_r11", ModelPartBuilder.create().uv(53, 79).cuboid(-0.5F, -0.523F, -0.4384F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F))
		.uv(33, 79).cuboid(-0.5F, -0.523F, -1.1384F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(0.0F, 1.4831F, -2.8216F, -1.1781F, 0.0F, 0.0F));

		ModelPartData cube_r12 = wheel.addChild("cube_r12", ModelPartBuilder.create().uv(43, 76).cuboid(-0.5F, -0.5F, -0.3039F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F))
		.uv(58, 82).cuboid(-0.5F, -0.5F, -1.0039F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(0.0F, 2.9738F, -1.0661F, -0.3927F, 0.0F, 0.0F));

		ModelPartData cube_r13 = wheel.addChild("cube_r13", ModelPartBuilder.create().uv(33, 73).cuboid(-0.5F, -1.0039F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F))
		.uv(63, 79).cuboid(-0.5F, -0.3039F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(0.0F, -1.0661F, -2.9738F, -0.3927F, 0.0F, 0.0F));

		ModelPartData cube_r14 = wheel.addChild("cube_r14", ModelPartBuilder.create().uv(33, 76).cuboid(-0.5F, -1.1384F, -0.4769F, 1.0F, 1.0F, 1.0F, new Dilation(-0.149F))
		.uv(38, 82).cuboid(-0.5F, -0.4384F, -0.4769F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(0.0F, -2.8216F, -1.483F, -1.1781F, 0.0F, 0.0F));

		ModelPartData cube_r15 = wheel.addChild("cube_r15", ModelPartBuilder.create().uv(48, 82).cuboid(-0.5F, -1.3522F, -1.04F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F))
		.uv(68, 82).cuboid(-0.5F, -0.6522F, -1.04F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(0.0F, -1.4978F, -2.2F, -0.7854F, 0.0F, 0.0F));

		ModelPartData cube_r16 = wheel.addChild("cube_r16", ModelPartBuilder.create().uv(68, 76).cuboid(-0.5F, -0.3478F, 0.04F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F))
		.uv(63, 76).cuboid(-0.5F, 0.3522F, 0.04F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(0.0F, 1.4978F, 2.2F, -0.7854F, 0.0F, 0.0F));

		ModelPartData cube_r17 = wheel.addChild("cube_r17", ModelPartBuilder.create().uv(53, 82).cuboid(-0.5F, -0.5616F, -0.5231F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F))
		.uv(58, 76).cuboid(-0.5F, 0.1384F, -0.5231F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(0.0F, 2.8216F, 1.483F, -1.1781F, 0.0F, 0.0F));

		ModelPartData cube_r18 = wheel.addChild("cube_r18", ModelPartBuilder.create().uv(53, 76).cuboid(-0.5F, -0.6961F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F))
		.uv(58, 73).cuboid(-0.5F, 0.0039F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(0.0F, 1.0661F, 2.9738F, -0.3927F, 0.0F, 0.0F));

		ModelPartData cube_r19 = wheel.addChild("cube_r19", ModelPartBuilder.create().uv(53, 73).cuboid(-0.5F, -0.5F, 0.0039F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F))
		.uv(48, 79).cuboid(-0.5F, -0.5F, -0.6961F, 1.0F, 1.0F, 1.0F, new Dilation(-0.149F)), ModelTransform.of(0.0F, -2.9738F, 1.0661F, -0.3927F, 0.0F, 0.0F));

		ModelPartData cube_r20 = wheel.addChild("cube_r20", ModelPartBuilder.create().uv(43, 79).cuboid(-0.5F, -0.477F, 0.1384F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F))
		.uv(48, 73).cuboid(-0.5F, -0.477F, -0.5616F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(0.0F, -1.483F, 2.8216F, -1.1781F, 0.0F, 0.0F));

		ModelPartData cube_r21 = wheel.addChild("cube_r21", ModelPartBuilder.create().uv(43, 73).cuboid(-0.5F, -1.04F, 0.3522F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F))
		.uv(38, 79).cuboid(-0.5F, -1.04F, -0.3478F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(0.0F, -2.2F, 1.4978F, -0.7854F, 0.0F, 0.0F));

		ModelPartData spokes = steering_wheel.addChild("spokes", ModelPartBuilder.create().uv(0, 77).cuboid(-0.5F, -0.137F, -0.5057F, 1.0F, 3.0F, 1.0F, new Dilation(-0.151F))
		.uv(5, 77).cuboid(-0.5F, -1.15F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(-0.151F)), ModelTransform.pivot(0.0F, 0.1875F, -0.0617F));

		ModelPartData cube_r22 = spokes.addChild("cube_r22", ModelPartBuilder.create().uv(6, 70).cuboid(-0.5F, -41.9F, -24.15F, 1.0F, 4.0F, 1.0F, new Dilation(-0.151F)), ModelTransform.of(0.0F, -1.237F, 44.9943F, 1.0472F, 0.0F, 0.0F));

		ModelPartData cube_r23 = spokes.addChild("cube_r23", ModelPartBuilder.create().uv(0, 70).cuboid(-0.5F, -2.4F, 0.4F, 1.0F, 4.0F, 1.0F, new Dilation(-0.151F)), ModelTransform.of(0.0F, -1.237F, 0.4943F, -1.0472F, 0.0F, 0.0F));

		ModelPartData seat = kart_v2.addChild("seat", ModelPartBuilder.create().uv(21, 45).cuboid(-5.5F, -3.75F, 0.0F, 11.0F, 1.0F, 9.0F, new Dilation(-0.249F))
		.uv(62, 44).cuboid(5.0F, -4.75F, 0.0F, 1.0F, 2.0F, 9.0F, new Dilation(-0.255F))
		.uv(0, 44).cuboid(-6.0F, -4.75F, 0.0F, 1.0F, 2.0F, 9.0F, new Dilation(-0.255F)), ModelTransform.pivot(0.0F, -1.0F, -1.0F));

		ModelPartData cube_r24 = seat.addChild("cube_r24", ModelPartBuilder.create().uv(0, 30).cuboid(-0.5F, -1.0F, -6.0F, 1.0F, 2.0F, 10.0F, new Dilation(-0.25F))
		.uv(66, 30).cuboid(10.5F, -1.0F, -6.0F, 1.0F, 2.0F, 10.0F, new Dilation(-0.25F)), ModelTransform.of(-5.5F, -8.7566F, 9.4605F, 1.309F, 0.0F, 0.0F));

		ModelPartData cube_r25 = seat.addChild("cube_r25", ModelPartBuilder.create().uv(23, 31).cuboid(-5.0F, -0.5F, -5.0F, 11.0F, 1.0F, 10.0F, new Dilation(-0.25F)), ModelTransform.of(-0.5F, -7.65F, 9.7F, 1.309F, 0.0F, 0.0F));

		ModelPartData accessoirs = kart_v2.addChild("accessoirs", ModelPartBuilder.create().uv(77, 80).cuboid(-7.75F, -3.75F, 2.5F, 2.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -1.0F, -1.0F));

		ModelPartData lever = accessoirs.addChild("lever", ModelPartBuilder.create(), ModelTransform.pivot(-6.75F, -3.8566F, 4.0452F));

		ModelPartData cube_r26 = lever.addChild("cube_r26", ModelPartBuilder.create().uv(78, 75).cuboid(-0.5F, -0.5F, -1.75F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -0.0452F, -0.1066F, -0.9599F, 0.0F, 0.0F));

		ModelPartData brake = accessoirs.addChild("brake", ModelPartBuilder.create(), ModelTransform.pivot(2.0F, -2.1464F, -3.6036F));

		ModelPartData cube_r27 = brake.addChild("cube_r27", ModelPartBuilder.create().uv(76, 68).cuboid(-1.0F, -2.0F, -0.5F, 2.0F, 2.0F, 1.0F, new Dilation(-0.25F)), ModelTransform.of(0.0F, 0.2071F, -0.0429F, 0.7854F, 0.0F, 0.0F));

		ModelPartData gas = accessoirs.addChild("gas", ModelPartBuilder.create(), ModelTransform.pivot(-2.0F, -2.0F, -3.0F));

		ModelPartData cube_r28 = gas.addChild("cube_r28", ModelPartBuilder.create().uv(83, 67).cuboid(-1.0F, -3.0F, -0.5F, 2.0F, 3.0F, 1.0F, new Dilation(-0.25F)), ModelTransform.of(0.0F, 0.0607F, 0.0607F, 0.7854F, 0.0F, 0.0F));

		ModelPartData body = kart_v2.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-10.5F, -5.75F, -6.75F, 3.0F, 5.0F, 14.0F, new Dilation(-0.25F))
		.uv(92, 0).cuboid(7.5F, -5.75F, -6.75F, 3.0F, 5.0F, 14.0F, new Dilation(-0.25F))
		.uv(50, 12).cuboid(-5.5F, -5.75F, -17.75F, 11.0F, 5.0F, 2.0F, new Dilation(-0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r29 = body.addChild("cube_r29", ModelPartBuilder.create().uv(77, 12).cuboid(-2.46F, -2.5F, -0.75F, 5.0F, 5.0F, 2.0F, new Dilation(-0.255F)), ModelTransform.of(-7.25F, -3.25F, -16.25F, 0.0F, 0.3491F, 0.0F));

		ModelPartData cube_r30 = body.addChild("cube_r30", ModelPartBuilder.create().uv(35, 12).cuboid(-2.54F, -2.5F, -0.75F, 5.0F, 5.0F, 2.0F, new Dilation(-0.255F)), ModelTransform.of(7.25F, -3.25F, -16.25F, 0.0F, -0.3491F, 0.0F));
		return TexturedModelData.of(modelData, 128, 128);
	}
	@Override
	public void setAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		kart_v2.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}