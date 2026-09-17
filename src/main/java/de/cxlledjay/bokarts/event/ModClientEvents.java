package de.cxlledjay.bokarts.event;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.keymapping.ModKeyMappings;
import de.cxlledjay.bokarts.networking.packet.HornPayloadC2S;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
public class ModClientEvents {

    private static boolean wasHornKeyPressed = false;


    public static void onEndTick(MinecraftClient client) {
        // Check if the key is physically held down this exact frame
        boolean isPressed = ModKeyMappings.KART_HORN_KEYBINDING.isPressed();

        // FIRE ONCE: If it is down now, but was NOT down last frame
        if (isPressed && !wasHornKeyPressed) {
            //send message to server to play horn
            HornPayloadC2S payloadC2S = new HornPayloadC2S();
            ClientPlayNetworking.send(payloadC2S);
        }

        // Save the current state for the next frame to check against
        wasHornKeyPressed = isPressed;

        // (Optional but recommended) Empty the game's internal click counter
        // so the OS's key-repeats don't build up in memory and cause weird behavior later.
        while (ModKeyMappings.KART_HORN_KEYBINDING.wasPressed()) {}
    }



    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(ModClientEvents::onEndTick);
    }

}
