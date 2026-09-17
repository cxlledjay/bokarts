package de.cxlledjay.bokarts.keymapping;

import de.cxlledjay.bokarts.BoKarts;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ModKeyMappings {

    public static final String KART_KEY_CATEGORY = "key.category.bokarts.controls";
    public static final KeyBinding KART_HORN_KEYBINDING = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.bokarts.horn",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_H,
            KART_KEY_CATEGORY
    ));

    public static void register() {
            BoKarts.LOGGER.info("Registering KeyMappings for " + BoKarts.MOD_ID);
    }

}
