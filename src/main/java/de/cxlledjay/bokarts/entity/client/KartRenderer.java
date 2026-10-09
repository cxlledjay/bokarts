package de.cxlledjay.bokarts.entity.client;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.entity.client.kartv2.ChassisModel;
import de.cxlledjay.bokarts.entity.client.kartv2.wheels.WheelsModelBase;
import de.cxlledjay.bokarts.entity.client.kartv2.wheels.WheelsModelNormal;
import de.cxlledjay.bokarts.entity.client.kartv2.wheels.WheelsModelOffroad;
import de.cxlledjay.bokarts.entity.custom.kart.KartEntity;
import de.cxlledjay.bokarts.entity.custom.kart.property.WheelType;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;

import java.util.EnumMap;
import java.util.Map;

public class KartRenderer extends EntityRenderer<KartEntity> {

    // models
    private final ChassisModel<KartEntity> modelChassis;
    private final Map<WheelType, WheelsModelBase<KartEntity>> modelWheels = new EnumMap<>(WheelType.class);

    // textures
    private static final Identifier TEXTURE_CHASSIS = BoKarts.id("textures/entity/kartv2/chassis.png");


    public KartRenderer(EntityRendererFactory.Context ctx) {
        // ----- vanilla stuff -----
        super(ctx);
        this.shadowRadius = 0.8F;

        // ----- models -----
        // chassis
        this.modelChassis = new ChassisModel<>(ctx.getPart(ChassisModel.ENTITY_MODEL_LAYER));

        // wheels
        this.modelWheels.put(WheelType.STREET, new WheelsModelNormal<>(ctx.getPart(WheelsModelNormal.ENTITY_MODEL_LAYER)));
        this.modelWheels.put(WheelType.DRIFT, new WheelsModelNormal<>(ctx.getPart(WheelsModelNormal.ENTITY_MODEL_LAYER)));
        this.modelWheels.put(WheelType.OFFROAD, new WheelsModelOffroad<>(ctx.getPart(WheelsModelOffroad.ENTITY_MODEL_LAYER)));
    }

    @Override
    public Identifier getTexture(KartEntity entity) {
        return TEXTURE_CHASSIS;
    }

    @Override
    public void render(KartEntity kartEntity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {

        // start kart rendering
        matrixStack.push();

        // initial positioning
        matrixStack.translate(0.0f, 1.4875f, 0.0f);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f - yaw));

        // damage wobble rendering
        float wobbleTime = kartEntity.getDamageWobbleTicks() - tickDelta;
        float wobbleStrength = Math.max(0.0f, kartEntity.getDamageWobbleStrength() - tickDelta);
        if (wobbleTime > 0.0f) {
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.sin(wobbleTime) * wobbleTime * wobbleStrength / 10.0f * kartEntity.getDamageWobbleSide()));
        }

        // bubble wobble rendering
        float bubbleWobble = kartEntity.interpolateBubbleWobble(tickDelta);
        if (!MathHelper.approximatelyEquals(bubbleWobble, 0.0f)) {
            matrixStack.multiply(new Quaternionf().setAngleAxis(kartEntity.interpolateBubbleWobble(tickDelta) * (float) (Math.PI / 180.0), 1.0f, 0.0f, 1.0f));
        }


        // -------------------- multipart model rendering --------------------
        // flip from blockbench model
        matrixStack.scale(-1.0f, -1.0f, 1.0f);

        // call each rendering step
        renderChassis(kartEntity, tickDelta, matrixStack, vertexConsumerProvider, light);
        renderWheels(kartEntity, tickDelta, matrixStack, vertexConsumerProvider, light);

        // done
        matrixStack.pop();
        super.render(kartEntity, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
    }





    // ==================== multipart rendering helper ====================

    private void renderChassis(KartEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        // animate model
        this.modelChassis.setAngles(entity, tickDelta, 0.0f, -0.1f, 0.0f, 0.0f);

        // draw base texture
        VertexConsumer baseConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(TEXTURE_CHASSIS));
        this.modelChassis.render(matrices, baseConsumer, light, OverlayTexture.DEFAULT_UV, 0xFFFFFFFF);

        // draw overlay texture
        // Identifier liveryTexture = BoKarts.id("textures/entity/kart/livery/" + entity.getLivery().asString() + ".png");
        // VertexConsumer liveryConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(liveryTexture));
        // this.modelChassis.render(matrices, liveryConsumer, light, OverlayTexture.DEFAULT_UV, 0xFFFFFFFF);
    }

    private void renderWheels(KartEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {

        // ----- retrieve data from KartEntity -----
        // wheel type
        WheelType wheelType = entity.getWheelType();

        // model
        WheelsModelBase<KartEntity> wheelModel = this.modelWheels.get(wheelType);
        if (wheelModel == null) return;

        // textures
        Identifier wheelBaseTexture = wheelType.getBaseTexture();
        Identifier wheelOverlayTexture = wheelType.getOverlayTexture();

        // rim color
        int rimColor = 0xFFFF0000; // ARGB integer (e.g. 0xFFFF0000 for red)

        // animate model
        wheelModel.setAngles(entity, tickDelta, 0.0f, -0.1f, 0.0f, 0.0f);

        // draw base texture
        VertexConsumer baseConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(wheelBaseTexture));
        wheelModel.render(matrices, baseConsumer, light, OverlayTexture.DEFAULT_UV, 0xFFFFFFFF);

        // draw overlay texture
        // VertexConsumer overlayConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(wheelOverlayTexture));
        // wheelModel.render(matrices, overlayConsumer, light, OverlayTexture.DEFAULT_UV, rimColor);
    }




}
