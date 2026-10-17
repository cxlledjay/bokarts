package de.cxlledjay.bokarts.screen.custom;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.config.ClientSyncedConfig;
import de.cxlledjay.bokarts.entity.custom.kart.KartEntity;
import de.cxlledjay.bokarts.networking.packet.ChangeKartLiftStateC2S;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class KartLiftScreen extends HandledScreen<KartLiftScreenHandler> {

    // ----- textures -----
    // background
    private static final Identifier BACKGROUND_TEXTURE = BoKarts.id("textures/gui/kart_lift/background.png");
    // overlays
    private static final Identifier OVERLAY_KART_ITEM_INSERT = BoKarts.id("textures/gui/kart_lift/kart_item_insert_overlay.png");
    private static final Identifier OVERLAY_KART_ITEM_INSERT_LOCKED = BoKarts.id("textures/gui/kart_lift/kart_item_insert_overlay_locked.png");
    private static final Identifier OVERLAY_MODIFICATION = BoKarts.id("textures/gui/kart_lift/modification_overlay.png");
    private static final Identifier OVERLAY_CUSTOMIZATION = BoKarts.id("textures/gui/kart_lift/customization_overlay.png");
    private static final Identifier OVERLAY_KEY = BoKarts.id("textures/gui/kart_lift/key_overlay.png");
    // tab overlays
    private static final Identifier TAB_OVERLAY_MODIFICATION = BoKarts.id("textures/gui/kart_lift/tab_modification_selected.png");
    private static final Identifier TAB_OVERLAY_CUSTOMIZATION  = BoKarts.id("textures/gui/kart_lift/tab_customization_selected.png");
    private static final Identifier TAB_OVERLAY_KEY = BoKarts.id("textures/gui/kart_lift/tab_key_selected.png");

    // tab buttons
    ButtonTextures TAB_BUTTON_MODIFICATION_TEXTURES = new ButtonTextures(
            BoKarts.id("kart_lift/tab/tab_modification_unhovered"),
            BoKarts.id("kart_lift/tab/tab_modification_hovered")
    );
    ButtonTextures TAB_BUTTON_CUSTOMIZATION_TEXTURES = new ButtonTextures(
            BoKarts.id("kart_lift/tab/tab_customization_unhovered"),
            BoKarts.id("kart_lift/tab/tab_customization_hovered")
    );
    ButtonTextures TAB_BUTTON_KEY_TEXTURES = new ButtonTextures(
            BoKarts.id("kart_lift/tab/tab_key_unhovered"),
            BoKarts.id("kart_lift/tab/tab_key_hovered")
    );
    // lift control buttons
    ButtonTextures LIFT_CONTROL_UP_TEXTURES = new ButtonTextures(
            BoKarts.id("kart_lift/lift_control/lift_up_unhovered"),
            BoKarts.id("kart_lift/lift_control/lift_up_hovered")
    );
    ButtonTextures LIFT_CONTROL_DOWN_TEXTURES = new ButtonTextures(
            BoKarts.id("kart_lift/lift_control/lift_down_unhovered"),
            BoKarts.id("kart_lift/lift_control/lift_down_hovered")
    );

    // ----- positioning -----
    // tabs
    private static final int TAB_WIDTH = 27;
    private static final int TAB_HEIGHT = 21;
    private static final int TAB_X_START = 127;
    private static final int TAB_Y_START = 0;

    public KartLiftScreen(KartLiftScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);

        // set to correct background size
        this.backgroundWidth = 235;
        this.backgroundHeight = 182;

        // set title position (title + "inventory")
        this.titleX = 36;
        this.titleY = 6;
        this.playerInventoryTitleX = 50;
        this.playerInventoryTitleY = 94;
    }

    @Override
    protected void init() {
        super.init();

        // starting tab
        if(this.handler.getLiftEntity().hasKartLoaded()) this.handler.setCurrentTab(KartLiftScreenHandler.KartLiftTab.MODIFICATION);
        else this.handler.setCurrentTab(KartLiftScreenHandler.KartLiftTab.INSERT_KART_ITEM);

        // -------------------- tab buttons --------------------
        {
            this.addDrawableChild(new TexturedButtonWidget(
                    this.x + TAB_X_START, this.y + TAB_Y_START,
                    TAB_WIDTH, TAB_HEIGHT,
                    TAB_BUTTON_MODIFICATION_TEXTURES,
                    button -> {
                        if (this.handler.getLiftEntity().hasKartLoaded())
                            this.handler.setCurrentTab(KartLiftScreenHandler.KartLiftTab.MODIFICATION);
                    }
            ) {
                @Override
                public void setFocused(boolean focused) {
                    // ignore focusing
                    super.setFocused(false);
                }
            });

            this.addDrawableChild(new TexturedButtonWidget(
                    this.x + TAB_X_START + TAB_WIDTH, this.y + TAB_Y_START,
                    TAB_WIDTH, TAB_HEIGHT,
                    TAB_BUTTON_CUSTOMIZATION_TEXTURES,
                    button -> {
                        if (this.handler.getLiftEntity().hasKartLoaded())
                            this.handler.setCurrentTab(KartLiftScreenHandler.KartLiftTab.CUSTOMIZATION);
                    }
            ) {
                @Override
                public void setFocused(boolean focused) {
                    // ignore focusing
                    super.setFocused(false);
                }
            });

            this.addDrawableChild(new TexturedButtonWidget(
                    this.x + TAB_X_START + (TAB_WIDTH * 2), this.y + TAB_Y_START,
                    TAB_WIDTH, TAB_HEIGHT,
                    TAB_BUTTON_KEY_TEXTURES,
                    button -> {
                        if (this.handler.getLiftEntity().hasKartLoaded())
                            this.handler.setCurrentTab(KartLiftScreenHandler.KartLiftTab.KEY);
                    }
            ) {
                @Override
                public void setFocused(boolean focused) {
                    // ignore focusing
                    super.setFocused(false);
                }
            });
        }

        // -------------------- lift control --------------------
        if (this.handler.getLiftEntity().hasKartLoaded()) {
            // lift down
            this.addDrawableChild(new TexturedButtonWidget(
                    this.x, this.y + 56,
                    27, 30,
                    LIFT_CONTROL_DOWN_TEXTURES,
                    button -> {
                        ClientPlayNetworking.send(new ChangeKartLiftStateC2S(this.handler.getLiftBlockEntityBlockPos()));
                        this.close();
                    }
            ) {
                @Override
                public void setFocused(boolean focused) {
                    // ignore focusing
                    super.setFocused(false);
                }
            });
        } else {
            // lift up
            this.addDrawableChild(new TexturedButtonWidget(
                    this.x, this.y + 56,
                    27, 30,
                    LIFT_CONTROL_UP_TEXTURES,
                    button -> {
                        ClientPlayNetworking.send(new ChangeKartLiftStateC2S(this.handler.getLiftBlockEntityBlockPos()));
                        this.close();
                    }
            ) {
                @Override
                public void setFocused(boolean focused) {
                    // ignore focusing
                    super.setFocused(false);
                }
            });
        }


    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        // background
        context.drawTexture(BACKGROUND_TEXTURE, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);


        // tab overlays
        Identifier overlay_tuning = switch(this.handler.getCurrentTab()) {
            case MODIFICATION ->  OVERLAY_MODIFICATION;
            case CUSTOMIZATION ->  OVERLAY_CUSTOMIZATION;
            case KEY ->  OVERLAY_KEY;
            default -> null;
        };
        if(overlay_tuning != null) {
            context.drawTexture(overlay_tuning, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
        }


        // tab selected overlay
        Identifier overlay_tab_selected = switch(this.handler.getCurrentTab()) {
            case MODIFICATION ->  TAB_OVERLAY_MODIFICATION;
            case CUSTOMIZATION ->  TAB_OVERLAY_CUSTOMIZATION;
            case KEY ->  TAB_OVERLAY_KEY;
            default -> null;
        };
        if(overlay_tab_selected != null) {
            context.getMatrices().push();
            context.getMatrices().translate(0,0,50);
            context.drawTexture(overlay_tab_selected, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
            context.getMatrices().pop();
        }



        // insert kart item overlay
        if(this.handler.getLiftEntity().hasKartLoaded()) {
            context.drawTexture(OVERLAY_KART_ITEM_INSERT_LOCKED, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
        } else {
            context.drawTexture(OVERLAY_KART_ITEM_INSERT, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
        }
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // render stuff like normal inventory
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
