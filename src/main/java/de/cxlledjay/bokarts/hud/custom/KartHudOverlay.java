package de.cxlledjay.bokarts.hud.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.config.BoKartsConfig;
import de.cxlledjay.bokarts.entity.custom.kart.KartEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

import java.util.EnumMap;
import java.util.Map;

public class KartHudOverlay {

    // textures
    private final static Identifier SPEEDOMETER_TEXTURE = BoKarts.id("textures/gui/kart_hud/speedometer.png");
    private final static Identifier SPEEDOMETER_NEEDLE = BoKarts.id("textures/gui/kart_hud/speedometer_needle.png");

    // animations
    private final static float NEEDLE_ROTATION_OFF = -120.0f;
    private final static float NEEDLE_ROTATION_IDLE = -93.0f;
    private final static float NEEDLE_ROTATION_MAX = 42.0f;

    // speed calculation
    private static final Map<BoKartsConfig.KartSpeedUnit, Float> SPEED_CONVERSION_MAP = Map.of(
            BoKartsConfig.KartSpeedUnit.BLOCKS_PER_SECOND, 20.0f,
            BoKartsConfig.KartSpeedUnit.KILOMETRES_PER_HOUR, (20.0f * 3.6f),
            BoKartsConfig.KartSpeedUnit.BURGERS_PER_FREEDOM, (20.0f * 2.237f)
    );

    // callback registered in the overlay rendering
    public static void render(DrawContext context, RenderTickCounter tickCounter) {

        if(!BoKartsConfig.enableKartHud) return;

        // safety
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options.hudHidden || client.currentScreen != null) return;
        ClientPlayerEntity player = client.player;
        if (player == null) return;

        // when in kart
        if (player.getVehicle() instanceof KartEntity kart) {

            int screenWidth = context.getScaledWindowWidth();
            int screenHeight = context.getScaledWindowHeight();

            // ----- render speedometer -----
            int speedoSize = (int) (BoKartsConfig.kartHudScale * ((float) screenHeight / 3));
            int speedoX = screenWidth - speedoSize - BoKartsConfig.kartHudPadding;
            int speedoY = screenHeight - speedoSize - BoKartsConfig.kartHudPadding;

            // lerping
            float delta = tickCounter.getTickDelta(true);

            // speedo
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, BoKartsConfig.kartHudTransparency);
            RenderSystem.enableBlend();
            context.drawTexture(
                    SPEEDOMETER_TEXTURE,
                    speedoX, speedoY,
                    0, 0,
                    speedoSize, speedoSize,
                    speedoSize, speedoSize
            );

            // get needle rotation
            float smoothRevs = MathHelper.lerp(delta, kart.engineRevsPrev, kart.engineRevs);
            float needleRotation= NEEDLE_ROTATION_OFF;
            if(!kart.canNotDrive()) needleRotation = NEEDLE_ROTATION_IDLE + Math.abs(smoothRevs / KartEntity.ENGINE_REVS_MAX) * (NEEDLE_ROTATION_MAX - NEEDLE_ROTATION_IDLE);

            // render rotated needle
            context.getMatrices().push();
            context.getMatrices().translate(speedoX + ((float) speedoSize / 2), speedoY + ((float) speedoSize / 2), 0);
            context.getMatrices().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(needleRotation));
            context.drawTexture(
                    SPEEDOMETER_NEEDLE,
                    (int) ((float) -speedoSize / 2), (int) ((float) -speedoSize / 2),
                    0, 0,
                    speedoSize, speedoSize,
                    speedoSize, speedoSize
            );
            context.getMatrices().pop();

            // calculate speed
            float speed = ((float) kart.distanceThisTick) * SPEED_CONVERSION_MAP.get(BoKartsConfig.kartHudSpeedUnit);
            String speedString = String.format("%05.1f", speed);
            int speedStringX = speedoX + (int) (speedoSize / (64.0f/19.0f));
            int speedStringY = speedoY + (int) (speedoSize / (64.0f/44.0f));
            float sizeScaling = (float) speedoSize / 55;

            // draw speed
            context.getMatrices().push();
            context.getMatrices().translate(speedStringX, speedStringY, 0);
            context.getMatrices().scale(sizeScaling * 0.9f, sizeScaling, 1.0f);
            context.drawText(
                    client.textRenderer,
                    speedString,
                    0, 0,
                    BoKartsConfig.kartHudTextColor,
                    false
            );
            context.getMatrices().pop();


            // set transparency back to normal...
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);



        }
    }

}
