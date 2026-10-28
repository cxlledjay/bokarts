package de.cxlledjay.bokarts.entity.client;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.entity.client.kartv2.ChassisModel;
import de.cxlledjay.bokarts.entity.client.kartv2.EngineModel;
import de.cxlledjay.bokarts.entity.client.kartv2.aero.AeroRaceModel;
import de.cxlledjay.bokarts.entity.client.kartv2.spoiler.SpoilerRaceModel;
import de.cxlledjay.bokarts.entity.client.kartv2.spoiler.SpoilerRaceProMaxModel;
import de.cxlledjay.bokarts.entity.client.kartv2.spoiler.SpoilerStreetModel;
import de.cxlledjay.bokarts.entity.client.kartv2.wheels.WheelsModelBase;
import de.cxlledjay.bokarts.entity.client.kartv2.wheels.WheelsModelNormal;
import de.cxlledjay.bokarts.entity.client.kartv2.wheels.WheelsModelOffroad;
import de.cxlledjay.bokarts.entity.custom.kart.KartEntity;
import de.cxlledjay.bokarts.entity.custom.kart.property.*;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
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
    private final Map<SpoilerType, SinglePartEntityModel<KartEntity>> modelSpoiler = new EnumMap<>(SpoilerType.class);
    private final Map<AeroType, SinglePartEntityModel<KartEntity>> modelAero = new EnumMap<>(AeroType.class);

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
        this.modelWheels.put(WheelType.SEMI_SLICK, new WheelsModelNormal<>(ctx.getPart(WheelsModelNormal.ENTITY_MODEL_LAYER)));
        this.modelWheels.put(WheelType.SOFT, new WheelsModelNormal<>(ctx.getPart(WheelsModelNormal.ENTITY_MODEL_LAYER)));
        this.modelWheels.put(WheelType.HARD, new WheelsModelNormal<>(ctx.getPart(WheelsModelNormal.ENTITY_MODEL_LAYER)));
        this.modelWheels.put(WheelType.DRIFT, new WheelsModelNormal<>(ctx.getPart(WheelsModelNormal.ENTITY_MODEL_LAYER)));
        this.modelWheels.put(WheelType.DRIFT_PLUS, new WheelsModelNormal<>(ctx.getPart(WheelsModelNormal.ENTITY_MODEL_LAYER)));
        this.modelWheels.put(WheelType.OFFROAD, new WheelsModelOffroad<>(ctx.getPart(WheelsModelOffroad.ENTITY_MODEL_LAYER)));
        this.modelWheels.put(WheelType.SPIKES, new WheelsModelOffroad<>(ctx.getPart(WheelsModelOffroad.ENTITY_MODEL_LAYER)));

        // engine
        this.modelEngine = new EngineModel<>(ctx.getPart(EngineModel.ENTITY_MODEL_LAYER));

        // spoiler
        this.modelSpoiler.put(SpoilerType.STREET, new SpoilerStreetModel<>(ctx.getPart(SpoilerStreetModel.ENTITY_MODEL_LAYER)));
        this.modelSpoiler.put(SpoilerType.STREET_CARBON, new SpoilerStreetModel<>(ctx.getPart(SpoilerStreetModel.ENTITY_MODEL_LAYER)));
        this.modelSpoiler.put(SpoilerType.RACE, new SpoilerRaceModel<>(ctx.getPart(SpoilerRaceModel.ENTITY_MODEL_LAYER)));
        this.modelSpoiler.put(SpoilerType.RACE_CARBON, new SpoilerRaceModel<>(ctx.getPart(SpoilerRaceModel.ENTITY_MODEL_LAYER)));
        this.modelSpoiler.put(SpoilerType.RACE_PLUS, new SpoilerRaceProMaxModel<>(ctx.getPart(SpoilerRaceProMaxModel.ENTITY_MODEL_LAYER)));
        this.modelSpoiler.put(SpoilerType.RACE_PLUS_CARBON, new SpoilerRaceProMaxModel<>(ctx.getPart(SpoilerRaceProMaxModel.ENTITY_MODEL_LAYER)));

        // aero
        this.modelAero.put(AeroType.RACE, new AeroRaceModel<>(ctx.getPart(AeroRaceModel.ENTITY_MODEL_LAYER)));
        this.modelAero.put(AeroType.RACE_CARBON, new AeroRaceModel<>(ctx.getPart(AeroRaceModel.ENTITY_MODEL_LAYER)));
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


        // -------------------- multipart model rendering --------------------
        // flip from blockbench model
        matrixStack.scale(-1.0f, -1.0f, 1.0f);

        // call each rendering step
        renderChassis(kartEntity, tickDelta, matrixStack, vertexConsumerProvider, light);
        renderWheels(kartEntity, tickDelta, matrixStack, vertexConsumerProvider, light);
        renderEngine(kartEntity, tickDelta, matrixStack, vertexConsumerProvider, light);
        renderOptionalSpoiler(kartEntity, tickDelta, matrixStack, vertexConsumerProvider, light);
        renderOptionalAero(kartEntity, tickDelta, matrixStack, vertexConsumerProvider, light);

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

        // get body type info
        BodyType bodyType = entity.getBodyType();

        // ----- draw base texture -----
        VertexConsumer baseConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(TEXTURE_CHASSIS));
        this.modelChassis.render(matrices, baseConsumer, light, OverlayTexture.DEFAULT_UV, 0xFFFFFFFF);

        // stop if it has no body type
        if (bodyType == BodyType.NONE) return;

        // ----- draw solid color overlay texture -----
        Identifier colorOverlayTexture = BodyType.SOLID_COLOR_OVERLAY.getTexture();
        int overlayColor = entity.getBodyColor() | 0xFF000000;

        VertexConsumer colorOverlayConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(colorOverlayTexture));
        this.modelChassis.render(matrices, colorOverlayConsumer, light, OverlayTexture.DEFAULT_UV, overlayColor);

        // stop if it is "only" solid color overlay
        if(bodyType == BodyType.SOLID_COLOR_OVERLAY) return;

        // ----- draw livery overlay texture -----
        Identifier overlayTexture = bodyType.getTexture();

        VertexConsumer liveryConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(overlayTexture));
        this.modelChassis.render(matrices, liveryConsumer, light, OverlayTexture.DEFAULT_UV, 0xFFFFFFFF);
    }

    private void renderWheels(KartEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {

        // ----- retrieve data from KartEntity -----
        // wheel type
        WheelType wheelType = entity.getWheelType();

        // no wheels
        if(wheelType == WheelType.NONE) return;

        // model
        WheelsModelBase<KartEntity> wheelModel = this.modelWheels.get(wheelType);
        if (wheelModel == null) return;

        // textures
        Identifier wheelBaseTexture = wheelType.getBaseTexture();
        Identifier wheelOverlayTexture = wheelType.getOverlayTexture();

        // rim color
        int rimColor = entity.getWheelColor() | 0xFF000000;

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

        // no engine
        if(entity.getEngineType() == EngineType.ENGINE_NONE) return;

        // animate model
        this.modelEngine.setAngles(entity, tickDelta, 0.0f, -0.1f, 0.0f, 0.0f);

        // get texture
        Identifier engineTexture = entity.getEngineType().getTexture();

        // draw base texture
        VertexConsumer baseConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(engineTexture));
        this.modelEngine.render(matrices, baseConsumer, light, OverlayTexture.DEFAULT_UV, 0xFFFFFFFF);
    }

    private void renderOptionalSpoiler(KartEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        // get data from kart
        SpoilerType spoilerType = entity.getSpoilerType();

        if(spoilerType.equals(SpoilerType.NONE)) return; // no spoiler

        // get model and texture
        SinglePartEntityModel<KartEntity> spoilerModel = this.modelSpoiler.get(spoilerType);
        Identifier spoilerTexture = spoilerType.getTexture();

        // render model
        VertexConsumer baseConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(spoilerTexture));
        spoilerModel.render(matrices, baseConsumer, light, OverlayTexture.DEFAULT_UV, 0xFFFFFFFF);
    }

    private void renderOptionalAero(KartEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        // get data from kart
        AeroType aeroType = entity.getAeroType();

        if(aeroType.equals(AeroType.NONE)) return; // no aero

        // get model and texture
        SinglePartEntityModel<KartEntity> aeroModel = this.modelAero.get(aeroType);
        Identifier aeroTexture = aeroType.getTexture();

        // render model
        VertexConsumer baseConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(aeroTexture));
        aeroModel.render(matrices, baseConsumer, light, OverlayTexture.DEFAULT_UV, 0xFFFFFFFF);
    }





    // ==================== rendering text ====================

    private void renderRacingNumber(KartEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, boolean left) {

        // retrieve data from entity
        int racingNumber = entity.getRaceNumber();

        // no (valid) number
        if(!KartEntity.isValidRaceNumber(racingNumber)) return;

        // convert racing number to text
        Text textRacingNumber = Text.literal(String.format("%02d",entity.getRaceNumber())).formatted(Formatting.BOLD);

        // get text renderer
        TextRenderer textRenderer = this.getTextRenderer();

        // get new 3d space
        matrices.push();

        // positioning
        final float x = (left) ? (10.255f / 16.0f) : (-10.255f / 16.0f);
        final float y = 1.5f - (4.625f / 16.0f);
        final float z = 3.25f / 16.0f;
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



    // prevent name rendering
    @Override
    protected boolean hasLabel(KartEntity entity) {
        return false;
    }
}
