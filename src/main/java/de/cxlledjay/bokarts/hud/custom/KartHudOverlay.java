package de.cxlledjay.bokarts.hud.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.config.BoKartsConfig;
import de.cxlledjay.bokarts.config.ClientSyncedConfig;
import de.cxlledjay.bokarts.entity.custom.kart.KartEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

import java.util.Map;

public class KartHudOverlay {

    // textures
    private final static Identifier SPEEDOMETER_TEXTURE = BoKarts.id("textures/gui/kart_hud/speedometer.png");
    private final static Identifier SPEEDOMETER_NEEDLE = BoKarts.id("textures/gui/kart_hud/speedometer_needle.png");
    private final static Identifier FUEL_GAUGE_TEXTURE = BoKarts.id("textures/gui/kart_hud/fuel_gauge.png");
    private final static Identifier FUEL_GAUGE_NEEDLE = BoKarts.id("textures/gui/kart_hud/fuel_gauge_needle.png");
    private final static Identifier LOW_FUEL_WARNING_TEXTURE = BoKarts.id("textures/gui/kart_hud/low_fuel_warning.png");

    // animations
    private final static float SPEEDO_NEEDLE_ROTATION_OFF = -120.0f;
    private final static float SPEEDO_NEEDLE_ROTATION_IDLE = -93.0f;
    private final static float SPEEDO_NEEDLE_ROTATION_MAX = 42.0f;
    private final static float FUEL_GAUGE_NEEDLE_ROTATION_MIN = -60.0f;
    private final static float FUEL_GAUGE_NEEDLE_ROTATION_MAX = 60.0f;

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

            // lerping
            float delta = tickCounter.getTickDelta(true);

            // ----- render speedometer -----
            int speedoSize = (int) (BoKartsConfig.kartHudScale * ((float) screenHeight / 3));
            int speedoX = screenWidth - speedoSize - BoKartsConfig.kartHudPadding;
            int speedoY = screenHeight - speedoSize - BoKartsConfig.kartHudPadding;


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
            float speedoNeedleRotation= SPEEDO_NEEDLE_ROTATION_OFF;
            if(!kart.canNotDrive()) speedoNeedleRotation =
                    SPEEDO_NEEDLE_ROTATION_IDLE + Math.abs(smoothRevs / KartEntity.ENGINE_REVS_MAX) * (SPEEDO_NEEDLE_ROTATION_MAX - SPEEDO_NEEDLE_ROTATION_IDLE);

            // render rotated needle
            context.getMatrices().push();
            context.getMatrices().translate(speedoX + ((float) speedoSize / 2), speedoY + ((float) speedoSize / 2), 0);
            context.getMatrices().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(speedoNeedleRotation));
            context.drawTexture(
                    SPEEDOMETER_NEEDLE,
                    (int) ((float) -speedoSize / 2), (int) ((float) -speedoSize / 2),
                    0, 0,
                    speedoSize, speedoSize,
                    speedoSize, speedoSize
            );
            context.getMatrices().pop();

            // calculate speed
            BoKartsConfig.KartSpeedUnit unit = BoKartsConfig.kartHudSpeedUnit;
            if(unit == null) unit =  BoKartsConfig.KartSpeedUnit.BLOCKS_PER_SECOND;
            float speed = ((float) kart.distanceThisTick) * SPEED_CONVERSION_MAP.get(unit);
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


            // ----- render fuel gauge -----

            // fuel gauge
            int fuelGaugeSize = speedoSize / 2;
            int fuelGaugeX = speedoX - (fuelGaugeSize) - BoKartsConfig.kartHudPadding * 2;
            int fuelGaugeY = speedoY + (fuelGaugeSize);

            // draw fuel gauge
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, BoKartsConfig.kartHudTransparency);
            RenderSystem.enableBlend();
            context.drawTexture(
                    FUEL_GAUGE_TEXTURE,
                    fuelGaugeX, fuelGaugeY,
                    0, 0,
                    fuelGaugeSize, fuelGaugeSize,
                    fuelGaugeSize, fuelGaugeSize
            );

            // get needle rotation
            float fuelLevelPercentage = kart.getFuelSynced() / ClientSyncedConfig.getMaxFuelCapacity();
            float fuelNeedleRotation = FUEL_GAUGE_NEEDLE_ROTATION_MIN + fuelLevelPercentage * (FUEL_GAUGE_NEEDLE_ROTATION_MAX - FUEL_GAUGE_NEEDLE_ROTATION_MIN);


            // render rotated needle
            context.getMatrices().push();
            context.getMatrices().translate(
                    fuelGaugeX + ((float) fuelGaugeSize / 2),
                    fuelGaugeY + ((float) fuelGaugeSize / 1.4f),
                    0
            );
            context.getMatrices().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(fuelNeedleRotation));
            context.drawTexture(
                    FUEL_GAUGE_NEEDLE,
                    (int) ((float) -fuelGaugeSize / 2), (int) ((float) -fuelGaugeSize / 2),
                    0, 0,
                    fuelGaugeSize, fuelGaugeSize,
                    fuelGaugeSize, fuelGaugeSize
            );
            context.getMatrices().pop();


            // -------------------- low fuel warning --------------------
            if(fuelLevelPercentage < 1f/6f) {
                int lowFuelWarningSize = (int) (BoKartsConfig.kartHudScale * ((float) screenHeight / 8));
                int lowFuelWarningX = screenWidth - lowFuelWarningSize - BoKartsConfig.kartHudPadding;
                int lowFuelWarningY = BoKartsConfig.kartHudPadding;

                // draw low fuel warning
                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, BoKartsConfig.kartHudTransparency);
                RenderSystem.enableBlend();
                context.drawTexture(
                        LOW_FUEL_WARNING_TEXTURE,
                        lowFuelWarningX, lowFuelWarningY,
                        0, 0,
                        lowFuelWarningSize, lowFuelWarningSize,
                        lowFuelWarningSize, lowFuelWarningSize
                );
            }



            // set transparency back to normal...
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);



        }
    }

}
