package de.cxlledjay.bokarts.screen.client;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.component.ModDataComponentTypes;
import de.cxlledjay.bokarts.entity.ModEntities;
import de.cxlledjay.bokarts.entity.custom.kart.KartEntity;
import de.cxlledjay.bokarts.entity.custom.kart.property.*;
import de.cxlledjay.bokarts.screen.custom.LiveryWorkbenchScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

// basically a stonecutter

@Environment(EnvType.CLIENT)
public class LiveryWorkbenchScreen extends HandledScreen<LiveryWorkbenchScreenHandler> {

    // textures
    private static final Identifier BACKGROUND_TEXTURE = BoKarts.id("textures/gui/livery_workbench/background.png");
    private static final Identifier SCROLLER_TEXTURE = Identifier.ofVanilla("container/stonecutter/scroller");
    private static final Identifier SCROLLER_DISABLED_TEXTURE = Identifier.ofVanilla("container/stonecutter/scroller_disabled");
    private static final Identifier LIVERY_SELECTED_TEXTURE = BoKarts.id("textures/gui/livery_workbench/livery_option_selected.png");
    private static final Identifier LIVERY_HIGHLIGHTED_TEXTURE = BoKarts.id("textures/gui/livery_workbench/livery_option_highlighted.png");
    private static final Identifier LIVERY_TEXTURE = BoKarts.id("textures/gui/livery_workbench/livery_option.png");

    // scrolling
    private float scrollAmount; // percentage of travel (0.0f -- 1.0f)
    private int scrollOffset;
    private boolean mouseClicked;

    // client sided entity preview
    private final KartEntity kartPreview;

    public LiveryWorkbenchScreen(LiveryWorkbenchScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);

        this.titleY--;

        this.backgroundWidth = 176;
        this.backgroundHeight = 230;
        this.playerInventoryTitleY = 136;

        this.scrollAmount = 0.0f;
        this.scrollOffset = 0;
        this.mouseClicked = false;

        // init random preview kart
        this.kartPreview = new KartEntity(ModEntities.KART_ENTITY_TYPE, inventory.player.getWorld());
        Random rng = new Random();
        this.kartPreview.setWheelType(WheelType.fromId(rng.nextInt(WheelType.values().length)));
        this.kartPreview.setEngineType(EngineType.fromId(rng.nextInt(EngineType.values().length)));
        this.kartPreview.setSpoilerType(SpoilerType.fromId(rng.nextInt(SpoilerType.values().length)));
        this.kartPreview.setAeroType(AeroType.fromId(rng.nextInt(AeroType.values().length)));
    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.mouseClicked = false;
        if (this.handler.canCraft()) {
            int selectBoxStartX = this.x + 8;
            int selectBoxStartY = this.y + 72;
            int currentlyShownMaxId = this.scrollOffset + 4;

            for (int shownId = this.scrollOffset; shownId < currentlyShownMaxId; shownId++) {
                int slotIdx = shownId - this.scrollOffset; //< slot index; 0 - 3 (4 possible slots)
                double dX = mouseX - selectBoxStartX;
                double dY = mouseY - (selectBoxStartY + (slotIdx * 15));
                if (dX >= 0.0 && dY >= 0.0 && dX < 143.0 && dY < 15.0 && this.handler.onButtonClick(this.client.player, shownId)) {
                    MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_LOOM_SELECT_PATTERN, 1.0F));
                    this.client.interactionManager.clickButton(this.handler.syncId, shownId);
                    return true;
                }
            }

            selectBoxStartX = this.x + 156;
            selectBoxStartY = this.y + 72;
            if (mouseX >= selectBoxStartX && mouseX < selectBoxStartX + 12 && mouseY >= selectBoxStartY && mouseY < selectBoxStartY + 60) {
                this.mouseClicked = true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.mouseClicked && this.shouldScroll()) {
            int scrollbarTopY = this.y + 72;
            int scrollbarBottomY = scrollbarTopY + 60;
            this.scrollAmount = ((float)mouseY - scrollbarTopY - 7.5F) / (scrollbarBottomY - scrollbarTopY - 15.0F);
            this.scrollAmount = MathHelper.clamp(this.scrollAmount, 0.0F, 1.0F);
            this.scrollOffset = (int)(this.scrollAmount * this.getMaxScroll() + 0.5);
            return true;
        } else {
            return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (this.shouldScroll()) {
            int i = this.getMaxScroll();
            float f = (float)verticalAmount / i;
            this.scrollAmount = MathHelper.clamp(this.scrollAmount - f, 0.0F, 1.0F);
            this.scrollOffset = (int)(this.scrollAmount * i + 0.5);
        }

        return true;
    }





    private boolean shouldScroll() {
        return this.handler.canCraft() && LiveryWorkbenchScreenHandler.LIVERY_AMOUNT > 4;
    }

    private int getMaxScroll() {
        return LiveryWorkbenchScreenHandler.LIVERY_AMOUNT - 4;
    }


    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {

        // background
        context.drawTexture(BACKGROUND_TEXTURE, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);

        // scroll bar
        if(!this.handler.canCraft()) {
            this.scrollOffset = 0;
            this.scrollAmount = 0.0f;
        }
        int progress = (int) (45.0f * this.scrollAmount); //< scroll Amount is between 0.0 and 1.0 => multiply by max allowed travel
        Identifier identifier = this.shouldScroll() ? SCROLLER_TEXTURE : SCROLLER_DISABLED_TEXTURE;
        context.drawGuiTexture(identifier, this.x + 156, this.y + 72 + progress, 12, 15);


        // stop rendering here, if there is nothing to craft
        if(!this.handler.canCraft()) return;

        // livery selectors
        int boxStartX = this.x + 8;
        int boxStartY = this.y + 72;
        int currentMaxVisibleId = this.scrollOffset + 4;
        this.renderLiveryOptionsBackground(context, mouseX, mouseY, boxStartX, boxStartY, currentMaxVisibleId);
        this.renderLiveryOptionName(context, boxStartX, boxStartY, currentMaxVisibleId);

        // render entity
        if(this.kartPreview == null) return; // safety
        BodyType previewBodyType = BodyType.NONE;
        if(this.handler.getLiveryItem() != null) {
            this.kartPreview.setBodyColor(this.handler.getLiveryItem().getOrDefault(ModDataComponentTypes.KART_ITEM_BODY_COLOR, 0xFFFFFF));
            this.kartPreview.setWheelColor(this.handler.getLiveryItem().getOrDefault(ModDataComponentTypes.KART_ITEM_WHEEL_COLOR, 0xFFFFFF));
            if(this.handler.getSelectedLiveryId() == -1) previewBodyType = this.handler.getLiveryItem().getOrDefault(ModDataComponentTypes.KART_ITEM_BODY_TYPE, BodyType.NONE);
        }
        if(this.handler.getSelectedLiveryId() != -1) previewBodyType = LiveryWorkbenchScreenHandler.LIVERIES_SORTED.get(this.handler.getSelectedLiveryId());
        this.kartPreview.setBodyType(previewBodyType);

        KartInventoryScreen.drawKart(context,this.x + 128, this.y + 48, 27, mouseX, mouseY, this.kartPreview);
    }

    private void renderLiveryOptionsBackground(DrawContext context, int mouseX, int mouseY, int selectBoxStartX, int selectBoxStartY, int maxOffset) {
        for (int currentId = this.scrollOffset; currentId < maxOffset && currentId < LiveryWorkbenchScreenHandler.LIVERY_AMOUNT; currentId++) {
            int slotIdx = currentId - this.scrollOffset;

            double dX = mouseX - selectBoxStartX;
            double dY = mouseY - (selectBoxStartY + (slotIdx * 15));

            Identifier identifier;
            if (currentId == this.handler.getSelectedLiveryId()) {
                identifier = LIVERY_SELECTED_TEXTURE;
            } else if (dX >= 0.0 && dY >= 0.0 && dX < 143.0 && dY < 15.0) {
                identifier = LIVERY_HIGHLIGHTED_TEXTURE;
            } else {
                identifier = LIVERY_TEXTURE;
            }

            context.drawTexture(identifier, selectBoxStartX, selectBoxStartY + slotIdx * 15, 0, 0, 143, 15, 143, 15);
        }
    }

    private void renderLiveryOptionName(DrawContext context, int x, int y, int maxOffset) {
        for (int currentId = this.scrollOffset; currentId < maxOffset && currentId < LiveryWorkbenchScreenHandler.LIVERY_AMOUNT; currentId++) {
            int slotIdx = currentId - this.scrollOffset;
            int dX = x + 5;
            int dY = y + slotIdx * 15 + 3;
            context.drawText(this.textRenderer, LiveryWorkbenchScreenHandler.LIVERIES_SORTED.get(currentId).getDisplayName(), dX, dY, 0xFFFFFFFF, true);
        }
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }


}
