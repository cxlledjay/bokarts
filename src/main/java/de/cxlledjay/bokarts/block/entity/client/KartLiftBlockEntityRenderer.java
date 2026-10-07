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
import net.minecraft.util.math.RotationAxis;

public class KartLiftBlockEntityRenderer implements BlockEntityRenderer<KartLiftBlockEntity> {

    private final KartLiftArmsModel model;
    private static final Identifier TEXTURE = BoKarts.id("textures/entity/kart_lift/kart_lift_arms.png");

    public KartLiftBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.model = new KartLiftArmsModel(ctx.getLayerModelPart(KartLiftArmsModel.KART_LIFT_ARMS_LAYER));
    }

    @Override
    public void render(KartLiftBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

        // access block entity
        BlockState state = entity.getCachedState();

        // render only when fully assembled
        if (state.getBlock() instanceof KartLiftCoreBlock && state.get(KartLiftCoreBlock.ASSEMBLED) && state.get(KartLiftCoreBlock.IS_CONTROL)) {

            // get direction of kart
            Direction facing = state.get(KartLiftCoreBlock.FACING);

            matrices.push();

            // move in between columns
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
            matrices.translate(0.5 + x_offset, 0.5, 0.5 + z_offset);


            // rotate
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180F));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(facing.asRotation()));

            // render
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(TEXTURE));
            this.model.render(matrices, vertexConsumer, light, overlay, 0xFFFFFFFF);

            matrices.pop();
        }
    }


}
