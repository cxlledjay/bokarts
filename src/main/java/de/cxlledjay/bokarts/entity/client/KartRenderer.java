package de.cxlledjay.bokarts.entity.client;

import com.mojang.datafixers.util.Pair;
import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.entity.custom.KartEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.client.render.entity.model.ModelWithWaterPatch;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;

public class KartRenderer extends EntityRenderer<KartEntity> {

    private final KartModel<KartEntity> model;

    public KartRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.shadowRadius = 0.8F;
        this.model = new KartModel<>(ctx.getPart(KartModel.KART_ENTITY_MODEL_LAYER));
    }

    @Override
    public Identifier getTexture(KartEntity entity) {
        return BoKarts.id("textures/entity/kart/" + entity.getPaintColor().asString() + ".png");
    }

    @Override
    public void render(KartEntity kartEntity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {

        matrixStack.push();
        matrixStack.translate(0.0F, 1.55F, 0.0F);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - yaw));

        float h = kartEntity.getDamageWobbleTicks() - tickDelta;
        float j = kartEntity.getDamageWobbleStrength() - tickDelta;

        if (j < 0.0F) {
            j = 0.0F;
        }

        if (h > 0.0F) {
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(MathHelper.sin(h) * h * j / 10.0F * kartEntity.getDamageWobbleSide()));
        }

        float k = kartEntity.interpolateBubbleWobble(tickDelta);
        if (!MathHelper.approximatelyEquals(k, 0.0F)) {
            matrixStack.multiply(new Quaternionf().setAngleAxis(kartEntity.interpolateBubbleWobble(tickDelta) * (float) (Math.PI / 180.0), 1.0F, 0.0F, 1.0F));
        }

        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        this.model.setAngles(kartEntity, tickDelta, 0.0F, -0.1F, 0.0F, 0.0F);
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(this.model.getLayer(this.getTexture(kartEntity)));
        this.model.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV);

        matrixStack.pop();

        super.render(kartEntity, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
    }
}
