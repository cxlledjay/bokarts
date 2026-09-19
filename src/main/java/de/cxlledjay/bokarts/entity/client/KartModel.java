package de.cxlledjay.bokarts.entity.client;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.entity.custom.KartEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class KartModel<T extends KartEntity> extends SinglePartEntityModel<T> {

    public static final EntityModelLayer KART_ENTITY_MODEL_LAYER = new EntityModelLayer(BoKarts.id("kart"), "main");

    // root
    private final ModelPart bokart;

    private final ModelPart wheels;
    private final ModelPart engine;
    private final ModelPart steering;
    // for animations
    private final ModelPart steering_column;
    private final ModelPart steering_wheel;
    private final ModelPart front_axle;
    private final ModelPart axle;
    private final ModelPart front_left;
    private final ModelPart front_right;
    private final ModelPart rear_axle;
    private final ModelPart drive_gear;

    public KartModel(ModelPart root) {
        this.bokart = root.getChild("bokart");

        this.wheels = this.bokart.getChild("wheels");
        this.engine = this.bokart.getChild("engine");
        this.steering = this.bokart.getChild("steering");

        this.steering_column = this.steering.getChild("steering_column");
        this.steering_wheel = this.steering.getChild("steering_wheel");
        this.front_axle = this.wheels.getChild("front_axle");
        this.axle = this.front_axle.getChild("axle");
        this.front_left = this.front_axle.getChild("front_left");
        this.front_right = this.front_axle.getChild("front_right");
        this.rear_axle = this.wheels.getChild("rear_axle");
        this.drive_gear = this.engine.getChild("drive_gear");
    }


    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bokart = modelPartData.addChild("bokart", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, -1.0F));

        ModelPartData frame = bokart.addChild("frame", ModelPartBuilder.create().uv(68, 34).cuboid(-5.0F, -2.0F, -15.25F, 10.0F, 1.0F, 1.0F, new Dilation(0.0005F))
                .uv(28, 77).cuboid(-2.0F, -2.0F, -15.0F, 1.0F, 1.0F, 6.0F, new Dilation(0.0005F))
                .uv(80, 64).cuboid(1.0F, -2.0F, -15.0F, 1.0F, 1.0F, 6.0F, new Dilation(0.0005F))
                .uv(0, 77).cuboid(3.75F, -2.0F, -6.0F, 1.0F, 1.0F, 6.0F, new Dilation(0.0F))
                .uv(30, 51).cuboid(7.0F, -2.0F, 2.75F, 1.0F, 1.0F, 13.0F, new Dilation(0.0F))
                .uv(56, 0).cuboid(-8.0F, -2.0F, 2.75F, 1.0F, 1.0F, 13.0F, new Dilation(0.0F))
                .uv(14, 77).cuboid(-4.75F, -2.0F, -6.0F, 1.0F, 1.0F, 6.0F, new Dilation(0.0F))
                .uv(56, 16).cuboid(-7.0F, -2.0F, 3.0F, 14.0F, 1.0F, 1.0F, new Dilation(0.001F))
                .uv(56, 18).cuboid(3.75F, -2.0F, -4.0F, 4.0F, 1.0F, 1.0F, new Dilation(0.007F))
                .uv(84, 12).cuboid(-7.75F, -2.0F, -4.0F, 4.0F, 1.0F, 1.0F, new Dilation(0.007F))
                .uv(58, 51).cuboid(-4.5F, -2.0F, 3.75F, 1.0F, 1.0F, 12.0F, new Dilation(0.0F))
                .uv(0, 64).cuboid(3.5F, -2.0F, 3.75F, 1.0F, 1.0F, 12.0F, new Dilation(0.0F))
                .uv(68, 96).cuboid(3.5F, -5.0F, 10.75F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(96, 79).cuboid(3.5F, -5.0F, 14.25F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-4.5F, -1.6F, -15.0F, 9.0F, 1.0F, 19.0F, new Dilation(-0.45F))
                .uv(94, 18).cuboid(-2.0F, -5.0F, -6.0F, 4.0F, 1.0F, 1.0F, new Dilation(-0.102F)), ModelTransform.pivot(0.0F, -1.0F, 0.0F));

        ModelPartData cube_r1 = frame.addChild("cube_r1", ModelPartBuilder.create().uv(60, 93).cuboid(-0.5F, -3.5F, -1.5F, 1.0F, 7.0F, 1.0F, new Dilation(-0.101F)), ModelTransform.of(-2.0638F, -4.1919F, -4.5F, 0.0F, 0.0F, 0.7505F));

        ModelPartData cube_r2 = frame.addChild("cube_r2", ModelPartBuilder.create().uv(36, 93).cuboid(-0.5F, -3.5F, -1.5F, 1.0F, 7.0F, 1.0F, new Dilation(-0.1F)), ModelTransform.of(2.0638F, -4.1919F, -4.5F, 0.0F, 0.0F, -0.7505F));

        ModelPartData cube_r3 = frame.addChild("cube_r3", ModelPartBuilder.create().uv(24, 84).cuboid(-2.9042F, -0.5F, -5.8991F, 1.0F, 1.0F, 5.0F, new Dilation(0.0005F)), ModelTransform.of(-5.15F, -1.5F, -2.75F, 0.0F, 2.3562F, 0.0F));

        ModelPartData cube_r4 = frame.addChild("cube_r4", ModelPartBuilder.create().uv(80, 71).cuboid(-3.0F, -0.5F, -0.5F, 7.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-6.25F, -1.5F, -11.3509F, 0.0F, 1.1432F, 0.0F));

        ModelPartData cube_r5 = frame.addChild("cube_r5", ModelPartBuilder.create().uv(12, 84).cuboid(-0.4535F, -0.5F, -2.5F, 1.0F, 1.0F, 5.0F, new Dilation(0.0005F)), ModelTransform.of(-5.8964F, -1.5F, -7.3964F, 0.0F, 0.7854F, 0.0F));

        ModelPartData cube_r6 = frame.addChild("cube_r6", ModelPartBuilder.create().uv(68, 36).cuboid(-4.0F, -0.5F, -0.5F, 7.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(6.25F, -1.5F, -11.3509F, 0.0F, -1.1432F, 0.0F));

        ModelPartData cube_r7 = frame.addChild("cube_r7", ModelPartBuilder.create().uv(84, 0).cuboid(-0.5F, -0.5F, -3.0F, 1.0F, 1.0F, 5.0F, new Dilation(0.0005F)), ModelTransform.of(5.5F, -1.5F, 1.0F, 0.0F, -2.3562F, 0.0F));

        ModelPartData cube_r8 = frame.addChild("cube_r8", ModelPartBuilder.create().uv(0, 84).cuboid(-0.5465F, -0.5F, -2.5F, 1.0F, 1.0F, 5.0F, new Dilation(0.0005F)), ModelTransform.of(5.8964F, -1.5F, -7.3964F, 0.0F, -0.7854F, 0.0F));

        ModelPartData wheels = bokart.addChild("wheels", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData rear_axle = wheels.addChild("rear_axle", ModelPartBuilder.create().uv(40, 38).cuboid(-11.0F, -0.5F, -0.5F, 22.0F, 1.0F, 1.0F, new Dilation(-0.25F))
                .uv(96, 14).cuboid(-6.0F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -2.5F, 12.0F));

        ModelPartData rear_left = rear_axle.addChild("rear_left", ModelPartBuilder.create(), ModelTransform.pivot(9.2F, 0.0F, 0.0F));

        ModelPartData tire3 = rear_left.addChild("tire3", ModelPartBuilder.create().uv(78, 89).cuboid(9.0F, -8.0F, 0.5F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F))
                .uv(78, 91).cuboid(9.0F, -2.4F, 0.5F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.pivot(-10.0F, 4.7F, -1.0F));

        ModelPartData cube_r9 = tire3.addChild("cube_r9", ModelPartBuilder.create().uv(94, 20).cuboid(-1.0F, -0.51F, -0.48F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -4.7F, -1.83F, -1.5708F, 0.0F, 0.0F));

        ModelPartData cube_r10 = tire3.addChild("cube_r10", ModelPartBuilder.create().uv(10, 92).cuboid(-1.0F, -0.56F, -0.6F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -3.71F, 3.7F, 1.1781F, 0.0F, 0.0F));

        ModelPartData cube_r11 = tire3.addChild("cube_r11", ModelPartBuilder.create().uv(0, 92).cuboid(-1.0F, -0.5148F, -0.5135F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -2.7252F, 3.0135F, 0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r12 = tire3.addChild("cube_r12", ModelPartBuilder.create().uv(88, 91).cuboid(-1.0F, -0.935F, -0.99F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -1.9F, 2.7F, 0.3927F, 0.0F, 0.0F));

        ModelPartData cube_r13 = tire3.addChild("cube_r13", ModelPartBuilder.create().uv(30, 91).cuboid(-1.0F, -0.935F, -0.01F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -1.9F, -0.7F, -0.3927F, 0.0F, 0.0F));

        ModelPartData cube_r14 = tire3.addChild("cube_r14", ModelPartBuilder.create().uv(90, 33).cuboid(-1.0F, -0.5148F, -0.4865F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -2.7252F, -1.0135F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r15 = tire3.addChild("cube_r15", ModelPartBuilder.create().uv(20, 90).cuboid(-1.0F, -0.56F, -0.4F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -3.71F, -1.7F, -1.1781F, 0.0F, 0.0F));

        ModelPartData cube_r16 = tire3.addChild("cube_r16", ModelPartBuilder.create().uv(10, 90).cuboid(-1.0F, -0.44F, -0.4F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -5.69F, -1.7F, 1.1781F, 0.0F, 0.0F));

        ModelPartData cube_r17 = tire3.addChild("cube_r17", ModelPartBuilder.create().uv(0, 90).cuboid(-1.0F, -0.4852F, -0.4865F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -6.6748F, -1.0135F, 0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r18 = tire3.addChild("cube_r18", ModelPartBuilder.create().uv(88, 89).cuboid(-1.0F, -0.065F, -0.01F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -7.5F, -0.7F, 0.3927F, 0.0F, 0.0F));

        ModelPartData cube_r19 = tire3.addChild("cube_r19", ModelPartBuilder.create().uv(88, 31).cuboid(-1.0F, -0.065F, -0.99F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -7.5F, 2.7F, -0.3927F, 0.0F, 0.0F));

        ModelPartData cube_r20 = tire3.addChild("cube_r20", ModelPartBuilder.create().uv(86, 81).cuboid(-1.0F, -0.4852F, -0.5135F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -6.6748F, 3.0135F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r21 = tire3.addChild("cube_r21", ModelPartBuilder.create().uv(86, 79).cuboid(-1.0F, -0.44F, -0.6F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -5.69F, 3.7F, -1.1781F, 0.0F, 0.0F));

        ModelPartData cube_r22 = tire3.addChild("cube_r22", ModelPartBuilder.create().uv(86, 16).cuboid(-1.0F, -0.51F, -0.52F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -4.7F, 3.83F, 1.5708F, 0.0F, 0.0F));

        ModelPartData rim3 = rear_left.addChild("rim3", ModelPartBuilder.create(), ModelTransform.pivot(-9.0F, 4.7F, -1.0F));

        ModelPartData cube_r23 = rim3.addChild("cube_r23", ModelPartBuilder.create().uv(36, 84).cuboid(-2.5F, -3.0F, -0.5F, 3.0F, 6.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(11.25F, -4.7F, 1.0F, 0.5236F, 0.0F, 0.0F));

        ModelPartData cube_r24 = rim3.addChild("cube_r24", ModelPartBuilder.create().uv(52, 86).cuboid(-2.5F, -3.0F, -0.5F, 3.0F, 5.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(11.25F, -4.7F, 0.5F, -1.5708F, 0.0F, 0.0F));

        ModelPartData cube_r25 = rim3.addChild("cube_r25", ModelPartBuilder.create().uv(48, 65).cuboid(-2.5F, -3.0F, -0.5F, 3.0F, 6.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(11.25F, -4.7F, 1.0F, -0.5236F, 0.0F, 0.0F));

        ModelPartData rear_right = rear_axle.addChild("rear_right", ModelPartBuilder.create(), ModelTransform.pivot(-9.2F, 0.0F, 0.0F));

        ModelPartData tire4 = rear_right.addChild("tire4", ModelPartBuilder.create().uv(92, 54).cuboid(-13.0F, -8.0F, 0.5F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F))
                .uv(88, 93).cuboid(-13.0F, -2.4F, 0.5F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.pivot(10.0F, 4.7F, -1.0F));

        ModelPartData cube_r26 = tire4.addChild("cube_r26", ModelPartBuilder.create().uv(94, 22).cuboid(-3.0F, -0.51F, -0.48F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -4.7F, -1.83F, -1.5708F, 0.0F, 0.0F));

        ModelPartData cube_r27 = tire4.addChild("cube_r27", ModelPartBuilder.create().uv(94, 12).cuboid(-3.0F, -0.56F, -0.6F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -3.71F, 3.7F, 1.1781F, 0.0F, 0.0F));

        ModelPartData cube_r28 = tire4.addChild("cube_r28", ModelPartBuilder.create().uv(10, 94).cuboid(-3.0F, -0.5148F, -0.5135F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -2.7252F, 3.0135F, 0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r29 = tire4.addChild("cube_r29", ModelPartBuilder.create().uv(0, 94).cuboid(-3.0F, -0.935F, -0.99F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -1.9F, 2.7F, 0.3927F, 0.0F, 0.0F));

        ModelPartData cube_r30 = tire4.addChild("cube_r30", ModelPartBuilder.create().uv(78, 93).cuboid(-3.0F, -0.935F, -0.01F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -1.9F, -0.7F, -0.3927F, 0.0F, 0.0F));

        ModelPartData cube_r31 = tire4.addChild("cube_r31", ModelPartBuilder.create().uv(26, 93).cuboid(-3.0F, -0.5148F, -0.4865F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -2.7252F, -1.0135F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r32 = tire4.addChild("cube_r32", ModelPartBuilder.create().uv(92, 62).cuboid(-3.0F, -0.56F, -0.4F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -3.71F, -1.7F, -1.1781F, 0.0F, 0.0F));

        ModelPartData cube_r33 = tire4.addChild("cube_r33", ModelPartBuilder.create().uv(92, 60).cuboid(-3.0F, -0.44F, -0.4F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -5.69F, -1.7F, 1.1781F, 0.0F, 0.0F));

        ModelPartData cube_r34 = tire4.addChild("cube_r34", ModelPartBuilder.create().uv(92, 58).cuboid(-3.0F, -0.4852F, -0.4865F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -6.6748F, -1.0135F, 0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r35 = tire4.addChild("cube_r35", ModelPartBuilder.create().uv(92, 56).cuboid(-3.0F, -0.065F, -0.01F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -7.5F, -0.7F, 0.3927F, 0.0F, 0.0F));

        ModelPartData cube_r36 = tire4.addChild("cube_r36", ModelPartBuilder.create().uv(92, 52).cuboid(-3.0F, -0.065F, -0.99F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -7.5F, 2.7F, -0.3927F, 0.0F, 0.0F));

        ModelPartData cube_r37 = tire4.addChild("cube_r37", ModelPartBuilder.create().uv(92, 50).cuboid(-3.0F, -0.4852F, -0.5135F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -6.6748F, 3.0135F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r38 = tire4.addChild("cube_r38", ModelPartBuilder.create().uv(50, 92).cuboid(-3.0F, -0.44F, -0.6F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -5.69F, 3.7F, -1.1781F, 0.0F, 0.0F));

        ModelPartData cube_r39 = tire4.addChild("cube_r39", ModelPartBuilder.create().uv(40, 92).cuboid(-3.0F, -0.51F, -0.52F, 4.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -4.7F, 3.83F, 1.5708F, 0.0F, 0.0F));

        ModelPartData rim4 = rear_right.addChild("rim4", ModelPartBuilder.create(), ModelTransform.pivot(9.0F, 4.7F, -1.0F));

        ModelPartData cube_r40 = rim4.addChild("cube_r40", ModelPartBuilder.create().uv(84, 57).cuboid(-0.5F, -3.0F, -0.5F, 3.0F, 6.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(-11.25F, -4.7F, 1.0F, 0.5236F, 0.0F, 0.0F));

        ModelPartData cube_r41 = rim4.addChild("cube_r41", ModelPartBuilder.create().uv(86, 73).cuboid(-0.5F, -3.0F, -0.5F, 3.0F, 5.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(-11.25F, -4.7F, 0.5F, -1.5708F, 0.0F, 0.0F));

        ModelPartData cube_r42 = rim4.addChild("cube_r42", ModelPartBuilder.create().uv(84, 50).cuboid(-0.5F, -3.0F, -0.5F, 3.0F, 6.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(-11.25F, -4.7F, 1.0F, -0.5236F, 0.0F, 0.0F));

        ModelPartData front_axle = wheels.addChild("front_axle", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -2.5F, -9.5F));

        ModelPartData front_left = front_axle.addChild("front_left", ModelPartBuilder.create(), ModelTransform.pivot(9.2F, 0.0F, 0.0F));

        ModelPartData tire = front_left.addChild("tire", ModelPartBuilder.create().uv(94, 35).cuboid(9.0F, -8.0F, 0.5F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F))
                .uv(94, 68).cuboid(9.0F, -2.4F, 0.5F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.pivot(-10.0F, 4.7F, -1.0F));

        ModelPartData cube_r43 = tire.addChild("cube_r43", ModelPartBuilder.create().uv(94, 47).cuboid(-1.0F, -0.51F, -0.48F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -4.7F, -1.83F, -1.5708F, 0.0F, 0.0F));

        ModelPartData cube_r44 = tire.addChild("cube_r44", ModelPartBuilder.create().uv(94, 77).cuboid(-1.0F, -0.56F, -0.6F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -3.71F, 3.7F, 1.1781F, 0.0F, 0.0F));

        ModelPartData cube_r45 = tire.addChild("cube_r45", ModelPartBuilder.create().uv(94, 75).cuboid(-1.0F, -0.5148F, -0.5135F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -2.7252F, 3.0135F, 0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r46 = tire.addChild("cube_r46", ModelPartBuilder.create().uv(94, 73).cuboid(-1.0F, -0.935F, -0.99F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -1.9F, 2.7F, 0.3927F, 0.0F, 0.0F));

        ModelPartData cube_r47 = tire.addChild("cube_r47", ModelPartBuilder.create().uv(94, 66).cuboid(-1.0F, -0.935F, -0.01F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -1.9F, -0.7F, -0.3927F, 0.0F, 0.0F));

        ModelPartData cube_r48 = tire.addChild("cube_r48", ModelPartBuilder.create().uv(94, 64).cuboid(-1.0F, -0.5148F, -0.4865F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -2.7252F, -1.0135F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r49 = tire.addChild("cube_r49", ModelPartBuilder.create().uv(48, 94).cuboid(-1.0F, -0.56F, -0.4F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -3.71F, -1.7F, -1.1781F, 0.0F, 0.0F));

        ModelPartData cube_r50 = tire.addChild("cube_r50", ModelPartBuilder.create().uv(94, 45).cuboid(-1.0F, -0.44F, -0.4F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -5.69F, -1.7F, 1.1781F, 0.0F, 0.0F));

        ModelPartData cube_r51 = tire.addChild("cube_r51", ModelPartBuilder.create().uv(94, 43).cuboid(-1.0F, -0.4852F, -0.4865F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -6.6748F, -1.0135F, 0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r52 = tire.addChild("cube_r52", ModelPartBuilder.create().uv(40, 94).cuboid(-1.0F, -0.065F, -0.01F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -7.5F, -0.7F, 0.3927F, 0.0F, 0.0F));

        ModelPartData cube_r53 = tire.addChild("cube_r53", ModelPartBuilder.create().uv(94, 28).cuboid(-1.0F, -0.065F, -0.99F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -7.5F, 2.7F, -0.3927F, 0.0F, 0.0F));

        ModelPartData cube_r54 = tire.addChild("cube_r54", ModelPartBuilder.create().uv(94, 26).cuboid(-1.0F, -0.4852F, -0.5135F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -6.6748F, 3.0135F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r55 = tire.addChild("cube_r55", ModelPartBuilder.create().uv(94, 24).cuboid(-1.0F, -0.44F, -0.6F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -5.69F, 3.7F, -1.1781F, 0.0F, 0.0F));

        ModelPartData cube_r56 = tire.addChild("cube_r56", ModelPartBuilder.create().uv(40, 96).cuboid(-1.0F, -0.51F, -0.52F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(10.0F, -4.7F, 3.83F, 1.5708F, 0.0F, 0.0F));

        ModelPartData rim = front_left.addChild("rim", ModelPartBuilder.create(), ModelTransform.pivot(-10.0F, 4.7F, -1.0F));

        ModelPartData cube_r57 = rim.addChild("cube_r57", ModelPartBuilder.create().uv(60, 86).cuboid(-0.5F, -3.0F, -0.5F, 2.0F, 6.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(10.25F, -4.7F, 1.0F, 0.5236F, 0.0F, 0.0F));

        ModelPartData cube_r58 = rim.addChild("cube_r58", ModelPartBuilder.create().uv(72, 89).cuboid(-0.5F, -3.0F, -0.5F, 2.0F, 5.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(10.25F, -4.7F, 0.5F, -1.5708F, 0.0F, 0.0F));

        ModelPartData cube_r59 = rim.addChild("cube_r59", ModelPartBuilder.create().uv(42, 77).cuboid(-0.5F, -3.0F, -0.5F, 2.0F, 6.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(10.25F, -4.7F, 1.0F, -0.5236F, 0.0F, 0.0F));

        ModelPartData front_right = front_axle.addChild("front_right", ModelPartBuilder.create(), ModelTransform.pivot(-9.2F, 0.0F, 0.0F));

        ModelPartData tire2 = front_right.addChild("tire2", ModelPartBuilder.create().uv(26, 95).cuboid(-12.0F, -8.0F, 0.5F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F))
                .uv(96, 4).cuboid(-12.0F, -2.4F, 0.5F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.pivot(10.0F, 4.7F, -1.0F));

        ModelPartData cube_r60 = tire2.addChild("cube_r60", ModelPartBuilder.create().uv(96, 10).cuboid(-2.0F, -0.51F, -0.48F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -4.7F, -1.83F, -1.5708F, 0.0F, 0.0F));

        ModelPartData cube_r61 = tire2.addChild("cube_r61", ModelPartBuilder.create().uv(96, 8).cuboid(-2.0F, -0.56F, -0.6F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -3.71F, 3.7F, 1.1781F, 0.0F, 0.0F));

        ModelPartData cube_r62 = tire2.addChild("cube_r62", ModelPartBuilder.create().uv(8, 96).cuboid(-2.0F, -0.5148F, -0.5135F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -2.7252F, 3.0135F, 0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r63 = tire2.addChild("cube_r63", ModelPartBuilder.create().uv(96, 6).cuboid(-2.0F, -0.935F, -0.99F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -1.9F, 2.7F, 0.3927F, 0.0F, 0.0F));

        ModelPartData cube_r64 = tire2.addChild("cube_r64", ModelPartBuilder.create().uv(96, 2).cuboid(-2.0F, -0.935F, -0.01F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -1.9F, -0.7F, -0.3927F, 0.0F, 0.0F));

        ModelPartData cube_r65 = tire2.addChild("cube_r65", ModelPartBuilder.create().uv(96, 0).cuboid(-2.0F, -0.5148F, -0.4865F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -2.7252F, -1.0135F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r66 = tire2.addChild("cube_r66", ModelPartBuilder.create().uv(0, 96).cuboid(-2.0F, -0.56F, -0.4F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -3.71F, -1.7F, -1.1781F, 0.0F, 0.0F));

        ModelPartData cube_r67 = tire2.addChild("cube_r67", ModelPartBuilder.create().uv(88, 95).cuboid(-2.0F, -0.44F, -0.4F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -5.69F, -1.7F, 1.1781F, 0.0F, 0.0F));

        ModelPartData cube_r68 = tire2.addChild("cube_r68", ModelPartBuilder.create().uv(80, 95).cuboid(-2.0F, -0.4852F, -0.4865F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -6.6748F, -1.0135F, 0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r69 = tire2.addChild("cube_r69", ModelPartBuilder.create().uv(72, 95).cuboid(-2.0F, -0.065F, -0.01F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -7.5F, -0.7F, 0.3927F, 0.0F, 0.0F));

        ModelPartData cube_r70 = tire2.addChild("cube_r70", ModelPartBuilder.create().uv(94, 87).cuboid(-2.0F, -0.065F, -0.99F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -7.5F, 2.7F, -0.3927F, 0.0F, 0.0F));

        ModelPartData cube_r71 = tire2.addChild("cube_r71", ModelPartBuilder.create().uv(94, 85).cuboid(-2.0F, -0.4852F, -0.5135F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -6.6748F, 3.0135F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r72 = tire2.addChild("cube_r72", ModelPartBuilder.create().uv(94, 83).cuboid(-2.0F, -0.44F, -0.6F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -5.69F, 3.7F, -1.1781F, 0.0F, 0.0F));

        ModelPartData cube_r73 = tire2.addChild("cube_r73", ModelPartBuilder.create().uv(48, 96).cuboid(-2.0F, -0.51F, -0.52F, 3.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-10.0F, -4.7F, 3.83F, 1.5708F, 0.0F, 0.0F));

        ModelPartData rim2 = front_right.addChild("rim2", ModelPartBuilder.create(), ModelTransform.pivot(10.0F, 4.7F, -1.0F));

        ModelPartData cube_r74 = rim2.addChild("cube_r74", ModelPartBuilder.create().uv(66, 89).cuboid(-1.5F, -3.0F, -0.5F, 2.0F, 6.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(-10.25F, -4.7F, 1.0F, 0.5236F, 0.0F, 0.0F));

        ModelPartData cube_r75 = rim2.addChild("cube_r75", ModelPartBuilder.create().uv(20, 92).cuboid(-1.5F, -3.0F, -0.5F, 2.0F, 5.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(-10.25F, -4.7F, 0.5F, -1.5708F, 0.0F, 0.0F));

        ModelPartData cube_r76 = rim2.addChild("cube_r76", ModelPartBuilder.create().uv(88, 24).cuboid(-1.5F, -3.0F, -0.5F, 2.0F, 6.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(-10.25F, -4.7F, 1.0F, -0.5236F, 0.0F, 0.0F));

        ModelPartData axle = front_axle.addChild("axle", ModelPartBuilder.create().uv(30, 49).cuboid(-10.0F, -0.5F, -0.5F, 20.0F, 1.0F, 1.0F, new Dilation(-0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData steering = bokart.addChild("steering", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData steering_column = steering.addChild("steering_column", ModelPartBuilder.create().uv(56, 14).cuboid(-9.5F, -3.0F, -11.25F, 19.0F, 1.0F, 1.0F, new Dilation(-0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData steering_wheel = steering.addChild("steering_wheel", ModelPartBuilder.create(), ModelTransform.of(-0.0198F, -9.8981F, -0.4686F, 1.5708F, -0.9599F, -1.5708F));

        ModelPartData cube_r77 = steering_wheel.addChild("cube_r77", ModelPartBuilder.create().uv(0, 49).cuboid(-0.6F, -0.5F, -7.011F, 1.0F, 1.0F, 14.0F, new Dilation(-0.75F)), ModelTransform.of(-6.4801F, 0.0981F, -0.0314F, 1.5708F, 0.0F, 1.5708F));

        ModelPartData wheel = steering_wheel.addChild("wheel", ModelPartBuilder.create().uv(30, 97).cuboid(-0.5F, -3.655F, -0.15F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F))
                .uv(92, 97).cuboid(-0.5F, -3.655F, -0.85F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F))
                .uv(12, 98).cuboid(-0.5F, -0.85F, 2.655F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F))
                .uv(20, 98).cuboid(-0.5F, -0.15F, 2.655F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F))
                .uv(98, 30).cuboid(-0.5F, -0.85F, -3.655F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F))
                .uv(40, 98).cuboid(-0.5F, -0.15F, -3.655F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F))
                .uv(32, 99).cuboid(-0.5F, 2.655F, -0.15F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F))
                .uv(56, 99).cuboid(-0.5F, 2.655F, -0.85F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r78 = wheel.addChild("cube_r78", ModelPartBuilder.create().uv(28, 99).cuboid(-0.5F, 0.04F, -0.6522F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F))
                .uv(24, 99).cuboid(-0.5F, 0.04F, -1.3522F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(0.0F, 2.2F, -1.4978F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r79 = wheel.addChild("cube_r79", ModelPartBuilder.create().uv(98, 93).cuboid(-0.5F, -0.523F, -0.4384F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F))
                .uv(98, 91).cuboid(-0.5F, -0.523F, -1.1384F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(0.0F, 1.4831F, -2.8216F, -1.1781F, 0.0F, 0.0F));

        ModelPartData cube_r80 = wheel.addChild("cube_r80", ModelPartBuilder.create().uv(98, 89).cuboid(-0.5F, -0.5F, -0.3039F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F))
                .uv(52, 98).cuboid(-0.5F, -0.5F, -1.0039F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(0.0F, 2.9738F, -1.0661F, -0.3927F, 0.0F, 0.0F));

        ModelPartData cube_r81 = wheel.addChild("cube_r81", ModelPartBuilder.create().uv(44, 84).cuboid(-0.5F, -1.0039F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F))
                .uv(72, 49).cuboid(-0.5F, -0.3039F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(0.0F, -1.0661F, -2.9738F, -0.3927F, 0.0F, 0.0F));

        ModelPartData cube_r82 = wheel.addChild("cube_r82", ModelPartBuilder.create().uv(48, 98).cuboid(-0.5F, -1.1384F, -0.4769F, 1.0F, 1.0F, 1.0F, new Dilation(-0.149F))
                .uv(44, 98).cuboid(-0.5F, -0.4384F, -0.4769F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(0.0F, -2.8216F, -1.483F, -1.1781F, 0.0F, 0.0F));

        ModelPartData cube_r83 = wheel.addChild("cube_r83", ModelPartBuilder.create().uv(80, 99).cuboid(-0.5F, -1.3522F, -1.04F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F))
                .uv(76, 99).cuboid(-0.5F, -0.6522F, -1.04F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(0.0F, -1.4978F, -2.2F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r84 = wheel.addChild("cube_r84", ModelPartBuilder.create().uv(8, 98).cuboid(-0.5F, -0.3478F, 0.04F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F))
                .uv(72, 99).cuboid(-0.5F, 0.3522F, 0.04F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(0.0F, 1.4978F, 2.2F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r85 = wheel.addChild("cube_r85", ModelPartBuilder.create().uv(4, 98).cuboid(-0.5F, -0.5616F, -0.5231F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F))
                .uv(0, 98).cuboid(-0.5F, 0.1384F, -0.5231F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(0.0F, 2.8216F, 1.483F, -1.1781F, 0.0F, 0.0F));

        ModelPartData cube_r86 = wheel.addChild("cube_r86", ModelPartBuilder.create().uv(96, 97).cuboid(-0.5F, -0.6961F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F))
                .uv(96, 70).cuboid(-0.5F, 0.0039F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(0.0F, 1.0661F, 2.9738F, -0.3927F, 0.0F, 0.0F));

        ModelPartData cube_r87 = wheel.addChild("cube_r87", ModelPartBuilder.create().uv(84, 97).cuboid(-0.5F, -0.5F, 0.0039F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F))
                .uv(26, 97).cuboid(-0.5F, -0.5F, -0.6961F, 1.0F, 1.0F, 1.0F, new Dilation(-0.149F)), ModelTransform.of(0.0F, -2.9738F, 1.0661F, -0.3927F, 0.0F, 0.0F));

        ModelPartData cube_r88 = wheel.addChild("cube_r88", ModelPartBuilder.create().uv(80, 97).cuboid(-0.5F, -0.477F, 0.1384F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F))
                .uv(96, 95).cuboid(-0.5F, -0.477F, -0.5616F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(0.0F, -1.483F, 2.8216F, -1.1781F, 0.0F, 0.0F));

        ModelPartData cube_r89 = wheel.addChild("cube_r89", ModelPartBuilder.create().uv(88, 97).cuboid(-0.5F, -1.04F, 0.3522F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F))
                .uv(72, 97).cuboid(-0.5F, -1.04F, -0.3478F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F)), ModelTransform.of(0.0F, -2.2F, 1.4978F, -0.7854F, 0.0F, 0.0F));

        ModelPartData spokes = steering_wheel.addChild("spokes", ModelPartBuilder.create().uv(64, 96).cuboid(-0.5F, -0.137F, -0.4057F, 1.0F, 3.0F, 1.0F, new Dilation(-0.151F)), ModelTransform.pivot(0.0F, 0.1875F, -0.0617F));

        ModelPartData cube_r90 = spokes.addChild("cube_r90", ModelPartBuilder.create().uv(16, 96).cuboid(-0.5F, -41.9F, -24.15F, 1.0F, 4.0F, 1.0F, new Dilation(-0.151F)), ModelTransform.of(0.0F, -1.237F, 44.9943F, 1.0472F, 0.0F, 0.0F));

        ModelPartData cube_r91 = spokes.addChild("cube_r91", ModelPartBuilder.create().uv(56, 94).cuboid(-0.5F, -2.4F, 0.4F, 1.0F, 4.0F, 1.0F, new Dilation(-0.151F)), ModelTransform.of(0.0F, -1.237F, 0.4943F, -1.0472F, 0.0F, 0.0F));

        ModelPartData seat = bokart.addChild("seat", ModelPartBuilder.create().uv(40, 40).cuboid(-5.0F, -3.75F, 1.0F, 10.0F, 1.0F, 8.0F, new Dilation(-0.249F))
                .uv(76, 40).cuboid(4.5F, -4.75F, 1.0F, 1.0F, 2.0F, 8.0F, new Dilation(-0.255F))
                .uv(48, 76).cuboid(-5.5F, -4.75F, 1.0F, 1.0F, 2.0F, 8.0F, new Dilation(-0.255F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r92 = seat.addChild("cube_r92", ModelPartBuilder.create().uv(26, 65).cuboid(-0.5F, -1.0F, -6.0F, 1.0F, 2.0F, 10.0F, new Dilation(-0.25F))
                .uv(58, 64).cuboid(9.5F, -1.0F, -6.0F, 1.0F, 2.0F, 10.0F, new Dilation(-0.25F)), ModelTransform.of(-5.0F, -8.7566F, 9.4605F, 1.309F, 0.0F, 0.0F));

        ModelPartData cube_r93 = seat.addChild("cube_r93", ModelPartBuilder.create().uv(0, 38).cuboid(-5.0F, -0.5F, -5.0F, 10.0F, 1.0F, 10.0F, new Dilation(-0.25F)), ModelTransform.of(0.0F, -7.65F, 9.7F, 1.309F, 0.0F, 0.0F));

        ModelPartData engine = bokart.addChild("engine", ModelPartBuilder.create().uv(68, 24).cuboid(-4.5F, -8.0F, 11.0F, 5.0F, 5.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, -0.5F));

        ModelPartData hopper = engine.addChild("hopper", ModelPartBuilder.create().uv(66, 76).cuboid(1.0F, -8.0F, 11.0F, 5.0F, 2.0F, 5.0F, new Dilation(0.0F))
                .uv(84, 6).cuboid(2.0F, -6.5F, 12.0F, 3.0F, 3.0F, 3.0F, new Dilation(0.0F))
                .uv(76, 97).cuboid(1.0F, -5.0F, 13.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.5F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData drive_gear = engine.addChild("drive_gear", ModelPartBuilder.create().uv(94, 37).cuboid(-1.0F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, new Dilation(-0.25F))
                .uv(44, 86).cuboid(-1.0F, -1.5F, -1.5F, 1.0F, 3.0F, 3.0F, new Dilation(0.01F)), ModelTransform.pivot(-5.0F, -4.25F, 13.5F));

        ModelPartData accessoirs = bokart.addChild("accessoirs", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData lever = accessoirs.addChild("lever", ModelPartBuilder.create().uv(48, 72).cuboid(-7.75F, -3.5F, 3.0F, 2.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r94 = lever.addChild("cube_r94", ModelPartBuilder.create().uv(86, 36).cuboid(-0.5F, -0.25F, -1.5F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-6.75F, -4.0F, 4.5F, -0.9599F, 0.0F, 0.0F));

        ModelPartData pedals = accessoirs.addChild("pedals", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r95 = pedals.addChild("cube_r95", ModelPartBuilder.create().uv(80, 73).cuboid(3.0F, -1.0F, -1.0F, 2.0F, 2.0F, 1.0F, new Dilation(-0.25F))
                .uv(94, 39).cuboid(-1.0F, -1.5F, -0.5F, 2.0F, 3.0F, 1.0F, new Dilation(-0.25F)), ModelTransform.of(-2.0F, -3.0F, -4.0F, 0.7854F, 0.0F, 0.0F));

        ModelPartData body = bokart.addChild("body", ModelPartBuilder.create().uv(0, 20).cuboid(-10.5F, -4.75F, -5.75F, 3.0F, 4.0F, 14.0F, new Dilation(-0.25F))
                .uv(34, 20).cuboid(7.5F, -4.75F, -5.75F, 3.0F, 4.0F, 14.0F, new Dilation(-0.25F))
                .uv(68, 18).cuboid(-5.5F, -4.75F, -16.75F, 11.0F, 4.0F, 2.0F, new Dilation(-0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r96 = body.addChild("cube_r96", ModelPartBuilder.create().uv(80, 83).cuboid(-2.46F, -2.5F, -0.75F, 5.0F, 4.0F, 2.0F, new Dilation(-0.255F)), ModelTransform.of(-7.25F, -2.25F, -15.25F, 0.0F, 0.3491F, 0.0F));

        ModelPartData cube_r97 = body.addChild("cube_r97", ModelPartBuilder.create().uv(66, 83).cuboid(-2.54F, -2.5F, -0.75F, 5.0F, 4.0F, 2.0F, new Dilation(-0.255F)), ModelTransform.of(7.25F, -2.25F, -15.25F, 0.0F, -0.3491F, 0.0F));
        return TexturedModelData.of(modelData, 128, 128);
    }


    @Override
    public void setAngles(KartEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

        // lerps
        float tickDelta = MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true);


        // ---------------- steering ----------------
        float smoothSteeringAngle = MathHelper.lerp(tickDelta, entity.steeringAnglePrev, entity.steeringAngle);

        float angle_steering_wheel = (float) Math.toRadians((smoothSteeringAngle + 90.0f));
        float angle_front_wheels = (float) (-Math.toRadians((smoothSteeringAngle / 3.25f)));
        float position_steering_column = smoothSteeringAngle / 106.0f;

        this.steering_wheel.pitch = angle_steering_wheel;
        this.front_left.yaw = angle_front_wheels;
        this.front_right.yaw = angle_front_wheels;
        this.steering_column.pivotX = position_steering_column;



        // ---------------- wheels ----------------
        float smoothRotationFront = MathHelper.lerp(tickDelta, entity.frontAxleRotationPrev, entity.frontAxleRotation);
        this.axle.pitch = smoothRotationFront;
        this.front_left.pitch = smoothRotationFront;
        this.front_right.pitch = smoothRotationFront;

        float smoothRotationBack = MathHelper.lerp(tickDelta, entity.rearAxleRotationPrev, entity.rearAxleRotation);
        this.rear_axle.pitch = smoothRotationBack;
        this.drive_gear.pitch = -smoothRotationBack;

    }


    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
        bokart.render(matrices, vertexConsumer, light, overlay, color);
    }

    @Override
    public ModelPart getPart() {
        return bokart;
    }
}
