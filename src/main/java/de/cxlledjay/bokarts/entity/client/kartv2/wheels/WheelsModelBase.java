package de.cxlledjay.bokarts.entity.client.kartv2.wheels;

import de.cxlledjay.bokarts.entity.custom.kart.KartEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public abstract class WheelsModelBase<T extends KartEntity> extends SinglePartEntityModel<T> {

    // parts
    private final ModelPart wheels;
    private final ModelPart rear_axle;
    private final ModelPart rear_left;
    private final ModelPart rear_right;
    private final ModelPart front_axle;
    private final ModelPart front_left;
    private final ModelPart front_right;


    public WheelsModelBase(ModelPart root) {
        this.wheels = root.getChild("wheels");
        this.rear_axle = this.wheels.getChild("rear_axle");
        this.rear_left = this.rear_axle.getChild("rear_left");
        this.rear_right = this.rear_axle.getChild("rear_right");
        this.front_axle = this.wheels.getChild("front_axle");
        this.front_left = this.front_axle.getChild("front_left");
        this.front_right = this.front_axle.getChild("front_right");
    }

    @Override
    public ModelPart getPart() {
        return wheels;
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

        // get lerps
        float tickDelta = MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true);

        // ---------------- steering rotation ----------------
        float smoothSteeringAngle = MathHelper.lerp(tickDelta, entity.steeringAnglePrev, entity.steeringAngle);
        float angle_front_wheels = (float) (-Math.toRadians((smoothSteeringAngle / 3.25f)));
        this.front_left.yaw = angle_front_wheels;
        this.front_right.yaw = angle_front_wheels;

        // ---------------- driving rotation ----------------
        float smoothRotationFront = MathHelper.lerp(tickDelta, entity.frontAxleRotationPrev, entity.frontAxleRotation);
        this.front_left.pitch = smoothRotationFront;
        this.front_right.pitch = smoothRotationFront;
        this.rear_axle.pitch = MathHelper.lerp(tickDelta, entity.rearAxleRotationPrev, entity.rearAxleRotation);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
        wheels.render(matrices, vertexConsumer, light, overlay, color);
    }
}