package de.cxlledjay.bokarts.sound;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.entity.custom.KartEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;

@Environment(EnvType.CLIENT)
public class KartEngineSound extends MovingSoundInstance {
    // tracked entity
    private final KartEntity kart;

    /*
    Scaling:        Volume     Pitch
        Idle:       0.5f       0.9f
        MaxSpeed:   0.8f       1.25f
     */
    private static final float volumeIdle = 0.40f;
    private static final float volumeMaxSpeed = 0.9f;
    private static final float pitchIdle = 0.9f;
    private static final float pitchMaxSpeed = 1.25f;

    public KartEngineSound(KartEntity kartEntity) {
        super(ModSounds.KART_ENGINE, SoundCategory.PLAYERS, Random.create());
        this.kart = kartEntity;

        // sound attributes
        this.repeat = true;
        this.repeatDelay = 0;
        this.volume = 0.5F;
        this.pitch = 0.9F;

        // sound position
        this.x = kart.getX();
        this.y = kart.getY();
        this.z = kart.getZ();
    }

    @Override
    public void tick() {

        if (this.kart.isRemoved() || !this.kart.hasPassengers()) {
            this.setDone();
            return;
        }

        // sound following the kart
        this.x = this.kart.getX();
        this.y = this.kart.getY();
        this.z = this.kart.getZ();

        // get sound scaling from kart entity. min value: 0; max value: kart max speed * 5 + engine max speed * 10 = 1.5 * 5 + 2.5 * 10 = 7.5 + 25 = 32.5
        final float maxScaling = 32.5f;
        float scaling = this.kart.getSoundScaling();

        // calculate pitch + volume
        float targetPitch   = pitchIdle     + (scaling * ((pitchMaxSpeed - pitchIdle) / maxScaling));
        float targetVolume  = volumeIdle    + (scaling * ((volumeMaxSpeed - volumeIdle) / maxScaling));

        // Smoothly transition the audio so it doesn't snap instantly
        // (Lerping makes it sound like a real engine revving up and winding down)
        this.pitch = MathHelper.lerp(0.1F, this.pitch, MathHelper.clamp(targetPitch, pitchIdle, pitchMaxSpeed));
        this.volume = MathHelper.lerp(0.1F, this.volume, MathHelper.clamp(targetVolume, volumeIdle, volumeMaxSpeed));
    }

    public static void playEngineSound(KartEntity kart) {
        // Grabs the player's sound manager and plays our tickable loop
        MinecraftClient.getInstance().getSoundManager().play(new KartEngineSound(kart));
    }
}
