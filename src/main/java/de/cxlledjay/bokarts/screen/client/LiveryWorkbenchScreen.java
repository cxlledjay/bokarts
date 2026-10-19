package de.cxlledjay.bokarts.screen.client;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.screen.custom.LiveryWorkbenchScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

// basically a stonecutter

@Environment(EnvType.CLIENT)
public class LiveryWorkbenchScreen extends HandledScreen<LiveryWorkbenchScreenHandler> {

    // textures
    private static final Identifier BACKGROUND_TEXTURE = BoKarts.id("textures/gui/livery_workbench/background.png");
    private static final Identifier SCROLLER_TEXTURE = Identifier.ofVanilla("container/stonecutter/scroller");
    private static final Identifier SCROLLER_DISABLED_TEXTURE = Identifier.ofVanilla("container/stonecutter/scroller_disabled");
    private static final Identifier LIVERY_SELECTED_TEXTURE = BoKarts.id("textures/gui/sprites/livery_workbench/livery_option_selected.png");
    private static final Identifier LIVERY_HIGHLIGHTED_TEXTURE = BoKarts.id("textures/gui/sprites/livery_workbench/livery_option_highlighted.png");
    private static final Identifier LIVERY_TEXTURE = BoKarts.id("textures/gui/sprites/livery_workbench/livery_option.png");

    public LiveryWorkbenchScreen(LiveryWorkbenchScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);

        this.titleY--;

        this.backgroundWidth = 176;
        this.backgroundHeight = 230;
        this.playerInventoryTitleY = 136;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {

        // background
        context.drawTexture(BACKGROUND_TEXTURE, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }
}
