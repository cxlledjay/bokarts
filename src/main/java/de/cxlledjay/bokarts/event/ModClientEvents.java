package de.cxlledjay.bokarts.event;

import de.cxlledjay.bokarts.keymapping.ModKeyMappings;
import de.cxlledjay.bokarts.networking.packet.HornPlayPayloadC2S;
import de.cxlledjay.bokarts.networking.packet.HornCyclePayloadC2S;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;

public class ModClientEvents {

    private static boolean wasHornKeyPressed = false;
    private static boolean wasHornNextPressed = false;
    private static boolean wasHornPrevPressed = false;


    public static void onEndTick(MinecraftClient client) {

        boolean hornKeyIsPressed = ModKeyMappings.KART_HORN_KEYBINDING.isPressed();
        boolean hornNextIsPressed = ModKeyMappings.KART_HORN_NEXT_KEYBINDING.isPressed();
        boolean hornPrevIsPressed = ModKeyMappings.KART_HORN_PREV_KEYBINDING.isPressed();

        if (hornKeyIsPressed && !wasHornKeyPressed) {
            HornPlayPayloadC2S payloadC2S = new HornPlayPayloadC2S();
            ClientPlayNetworking.send(payloadC2S);
        }

        if (hornNextIsPressed && !wasHornNextPressed) {
            HornCyclePayloadC2S payloadC2S = new HornCyclePayloadC2S(1);
            ClientPlayNetworking.send(payloadC2S);
        }

        if (hornPrevIsPressed && !wasHornPrevPressed) {
            HornCyclePayloadC2S payloadC2S = new HornCyclePayloadC2S(-1);
            ClientPlayNetworking.send(payloadC2S);
        }

        wasHornKeyPressed = hornKeyIsPressed;
        wasHornNextPressed = hornNextIsPressed;
        wasHornPrevPressed = hornPrevIsPressed;
    }



    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(ModClientEvents::onEndTick);
    }

}
