package de.cxlledjay.bokarts.sound;

import de.cxlledjay.bokarts.BoKarts;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {

    public static final SoundEvent KART_ENGINE = registerSoundEvent("kart_engine");
    public static final SoundEvent HORN_AUGHH = registerSoundEvent("horn_aughh");
    public static final SoundEvent HORN_BIKE = registerSoundEvent("horn_bike");
    public static final SoundEvent HORN_CIVIC = registerSoundEvent("horn_civic");
    public static final SoundEvent HORN_DISCORD_JOIN = registerSoundEvent("horn_discord_join");
    public static final SoundEvent HORN_DISCORD_LEAVE = registerSoundEvent("horn_discord_leave");
    public static final SoundEvent HORN_METAL_PIPE = registerSoundEvent("horn_metal_pipe");
    public static final SoundEvent HORN_MINI = registerSoundEvent("horn_mini");
    public static final SoundEvent HORN_RIZZ = registerSoundEvent("horn_rizz");
    public static final SoundEvent HORN_YODA = registerSoundEvent("horn_yoda");


    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = BoKarts.id(name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }


    public static void registerSounds() {
        BoKarts.LOGGER.info("Registering sounds for " + BoKarts.MOD_ID);
    }

}
