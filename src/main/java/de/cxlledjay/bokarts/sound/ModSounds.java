package de.cxlledjay.bokarts.sound;

import de.cxlledjay.bokarts.BoKarts;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {

    public static final SoundEvent KART_ENGINE = registerSoundEvent("kart_engine");


    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = BoKarts.id(name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }


    public static void registerSounds() {
        BoKarts.LOGGER.info("Registering sounds for " + BoKarts.MOD_ID);
    }

}
