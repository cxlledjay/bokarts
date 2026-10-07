package de.cxlledjay.bokarts.block.entity.client;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.block.custom.KartLiftCoreBlock;
import de.cxlledjay.bokarts.block.entity.custom.KartLiftBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class KartLiftBlockEntityRenderer implements BlockEntityRenderer<KartLiftBlockEntity> {

    private final KartLiftArmsModel model;
    private static final Identifier TEXTURE = BoKarts.id("textures/entity/kart_lift/kart_lift_arms.png");

    // animation
    public static final float ANIMATION_PHASE_CHANGE = 0.3f;
    public static final float MAX_LIFT_HEIGHT = 1.5f;
    public static final float ARMS_TRAVEL = 70.0f;

    public KartLiftBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.model = new KartLiftArmsModel(ctx.getLayerModelPart(KartLiftArmsModel.KART_LIFT_ARMS_LAYER));
    }

    @Override
    public void render(KartLiftBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

        // access block entity
        BlockState state = entity.getCachedState();

        // render only when fully assembled
        if (state.getBlock() instanceof KartLiftCoreBlock && state.get(KartLiftCoreBlock.ASSEMBLED) && state.get(KartLiftCoreBlock.IS_CONTROL)) {

            // -------------------- position calculation --------------------
            Direction facing = state.get(KartLiftCoreBlock.FACING);
            double x_offset = switch(facing) {
                case WEST -> -1.5;
                case EAST -> 1.5;
                default -> 0;
            };
            double z_offset = switch(facing) {
                case NORTH -> -1.5;
                case SOUTH -> 1.5;
                default -> 0;
            };


            // -------------------- animation calculation --------------------
            // The master timeline (0.0 to 1.0)
            float smoothProgress = MathHelper.lerp(tickDelta, entity.animationProgressPrev, entity.animationProgress);

            // --- PHASE 1: Grip the Kart (Active from 0.0 to 0.3 on the timeline) ---
            // We divide by 0.3 to stretch the 0.0-0.3 range into a clean 0.0-1.0 range.
            float gripProgress = MathHelper.clamp(smoothProgress / ANIMATION_PHASE_CHANGE, 0.0f, 1.0f);

            // --- PHASE 2: Lift Up (Active from 0.3 to 1.0 on the timeline) ---
            // We subtract 0.3 to start at zero, then divide by the remaining 0.7 to stretch it to 0.0-1.0.
            float liftProgress = MathHelper.clamp((smoothProgress - ANIMATION_PHASE_CHANGE) / (1.0f - ANIMATION_PHASE_CHANGE), 0.0f, 1.0f);



            // -------------------- transform matrices --------------------
            matrices.push();

            // set position
            matrices.translate(0.5 + x_offset, 0.5 + (liftProgress * MAX_LIFT_HEIGHT), 0.5 + z_offset);

            // rotate
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180F));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(facing.asRotation()));

            // rotate arms
            final float INITIAL_YAW = (float) Math.toRadians(47.5);
            float travelRadians = (float) Math.toRadians(gripProgress * ARMS_TRAVEL);
            this.model.arm1.yaw = -INITIAL_YAW+travelRadians;
            this.model.arm2.yaw = INITIAL_YAW-travelRadians;
            this.model.arm3.yaw = INITIAL_YAW-travelRadians;
            this.model.arm4.yaw = -INITIAL_YAW+travelRadians;



            // -------------------- render --------------------
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(TEXTURE));
            this.model.render(matrices, vertexConsumer, light, overlay, 0xFFFFFFFF);

            matrices.pop();
        }
    }


}
