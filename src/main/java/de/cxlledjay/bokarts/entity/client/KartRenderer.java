package de.cxlledjay.bokarts.entity.client;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.entity.client.kartv2.ChassisModel;
import de.cxlledjay.bokarts.entity.client.kartv2.EngineModel;
import de.cxlledjay.bokarts.entity.client.kartv2.wheels.WheelsModelBase;
import de.cxlledjay.bokarts.entity.client.kartv2.wheels.WheelsModelNormal;
import de.cxlledjay.bokarts.entity.client.kartv2.wheels.WheelsModelOffroad;
import de.cxlledjay.bokarts.entity.custom.kart.KartEntity;
import de.cxlledjay.bokarts.entity.custom.kart.property.BodyType;
import de.cxlledjay.bokarts.entity.custom.kart.property.WheelType;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.EnumMap;
import java.util.Map;

public class KartRenderer extends EntityRenderer<KartEntity> {

    // models
    private final ChassisModel<KartEntity> modelChassis;
    private final Map<WheelType, WheelsModelBase<KartEntity>> modelWheels = new EnumMap<>(WheelType.class);
    private final EngineModel<KartEntity> modelEngine;

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

        // engine
        this.modelEngine = new EngineModel<>(ctx.getPart(EngineModel.ENTITY_MODEL_LAYER));
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
        renderEngine(kartEntity, tickDelta, matrixStack, vertexConsumerProvider, light);

        // render racing number
        renderRacingNumber(kartEntity, tickDelta, matrixStack, vertexConsumerProvider, light, true);
        renderRacingNumber(kartEntity, tickDelta, matrixStack, vertexConsumerProvider, light, false);

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
        Identifier overlayTexture = BoKarts.id("textures/entity/kartv2/body/" + entity.getBodyType().asString() + ".png");
        int overlayColor = 0xFFFFFFFF;
        if(entity.getBodyType() == BodyType.SOLID_COLOR_OVERLAY) {
            // apply custom color
            overlayColor = entity.getBodyColor();
        }
        VertexConsumer liveryConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(overlayTexture));
        this.modelChassis.render(matrices, liveryConsumer, light, OverlayTexture.DEFAULT_UV, overlayColor);
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
        int rimColor = entity.getWheelColor();

        // animate model
        wheelModel.setAngles(entity, tickDelta, 0.0f, -0.1f, 0.0f, 0.0f);

        // draw base texture
        VertexConsumer baseConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(wheelBaseTexture));
        wheelModel.render(matrices, baseConsumer, light, OverlayTexture.DEFAULT_UV, 0xFFFFFFFF);

        // draw overlay texture
        VertexConsumer overlayConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(wheelOverlayTexture));
        wheelModel.render(matrices, overlayConsumer, light, OverlayTexture.DEFAULT_UV, rimColor);
    }

    private void renderEngine(KartEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        // animate model
        this.modelEngine.setAngles(entity, tickDelta, 0.0f, -0.1f, 0.0f, 0.0f);

        // get texture
        Identifier engineTexture = entity.getEngineType().getTexture();

        // draw base texture
        VertexConsumer baseConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(engineTexture));
        this.modelEngine.render(matrices, baseConsumer, light, OverlayTexture.DEFAULT_UV, 0xFFFFFFFF);
    }





    // ==================== rendering text ====================

    private void renderRacingNumber(KartEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, boolean left) {

        // get text renderer
        TextRenderer textRenderer = this.getTextRenderer();

        // convert racing number to text; if it is applicable
        Text textRacingNumber =
                ((entity.getRaceNumber() >= 0) && (entity.getRaceNumber() <= 99))
                    ? (Text.literal(String.format("%02d",entity.getRaceNumber())).formatted(Formatting.BOLD))
                    : (Text.literal(""));

        // get new 3d space
        matrices.push();

        // positioning
        final float x = (left) ? (10.255f / 16.0f) : (-10.255f / 16.0f);
        final float y = 1.5f - (4.625f / 16.0f);
        final float z = 2.0f / 16.0f;
        matrices.translate(x, y, z);

        // rotation
        final float rot = (left) ? (90.0f) : (-90.0f);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rot));

        // size
        final float size = 0.025F;
        matrices.scale(-size, size, size);

        // center text
        float textWidth = textRenderer.getWidth(textRacingNumber);

        // draw text w/ outline
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        textRenderer.drawWithOutline(
                textRacingNumber.asOrderedText(),
                -textWidth / 2.0f,
                0.0f,
                0xFF1E1E1E, // base text color
                0xFFA3A3A3, // outline color
                matrix4f,
                vertexConsumers,
                light
        );

        // done
        matrices.pop();
    }

}
