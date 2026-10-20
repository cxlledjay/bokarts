package de.cxlledjay.bokarts.hud;

import de.cxlledjay.bokarts.hud.custom.KartHudOverlay;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class ModHuds {

    public static void register() {
        HudRenderCallback.EVENT.register(KartHudOverlay::render);
    }

}
