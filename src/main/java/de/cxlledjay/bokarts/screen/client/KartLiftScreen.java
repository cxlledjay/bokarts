package de.cxlledjay.bokarts.screen.client;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.block.entity.custom.KartLiftBlockEntity;
import de.cxlledjay.bokarts.entity.custom.kart.KartEntity;
import de.cxlledjay.bokarts.networking.packet.KartLiftChangeStateC2S;
import de.cxlledjay.bokarts.networking.packet.KartLiftModifyRaceNumberC2S;
import de.cxlledjay.bokarts.screen.custom.KartLiftScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
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
    private static final ButtonTextures TAB_BUTTON_MODIFICATION_TEXTURES = new ButtonTextures(
            BoKarts.id("kart_lift/tab/tab_modification_unhovered"),
            BoKarts.id("kart_lift/tab/tab_modification_hovered")
    );
    private static final ButtonTextures TAB_BUTTON_CUSTOMIZATION_TEXTURES = new ButtonTextures(
            BoKarts.id("kart_lift/tab/tab_customization_unhovered"),
            BoKarts.id("kart_lift/tab/tab_customization_hovered")
    );
    private static final ButtonTextures TAB_BUTTON_KEY_TEXTURES = new ButtonTextures(
            BoKarts.id("kart_lift/tab/tab_key_unhovered"),
            BoKarts.id("kart_lift/tab/tab_key_hovered")
    );
    // lift control buttons
    private static final ButtonTextures LIFT_CONTROL_UP_TEXTURES = new ButtonTextures(
            BoKarts.id("kart_lift/lift_control/lift_up_unhovered"),
            BoKarts.id("kart_lift/lift_control/lift_up_hovered")
    );
    private static final ButtonTextures LIFT_CONTROL_DOWN_TEXTURES = new ButtonTextures(
            BoKarts.id("kart_lift/lift_control/lift_down_unhovered"),
            BoKarts.id("kart_lift/lift_control/lift_down_hovered")
    );

    // local tab
    private KartLiftScreenHandler.KartLiftTab currentTab;

    // race number text widget
    private TextFieldWidget raceNumberInputWidget;

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
        if(this.handler.getLiftEntity().hasKartLoaded()) this.changeTab(KartLiftScreenHandler.KartLiftTab.MODIFICATION);
        else this.changeTab(KartLiftScreenHandler.KartLiftTab.INSERT_KART_ITEM);

        // -------------------- tab buttons --------------------
        {
            this.addDrawableChild(new TexturedButtonWidget(
                    this.x + TAB_X_START, this.y + TAB_Y_START,
                    TAB_WIDTH, TAB_HEIGHT,
                    TAB_BUTTON_MODIFICATION_TEXTURES,
                    button -> {
                        if (this.handler.getLiftEntity().hasKartLoaded())
                            this.changeTab(KartLiftScreenHandler.KartLiftTab.MODIFICATION);
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
                            this.changeTab(KartLiftScreenHandler.KartLiftTab.CUSTOMIZATION);
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
                            this.changeTab(KartLiftScreenHandler.KartLiftTab.KEY);
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
                        ClientPlayNetworking.send(new KartLiftChangeStateC2S(this.handler.getLiftBlockEntityBlockPos()));
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
        else {
            // lift up
            this.addDrawableChild(new TexturedButtonWidget(
                    this.x, this.y + 56,
                    27, 30,
                    LIFT_CONTROL_UP_TEXTURES,
                    button -> {
                        ClientPlayNetworking.send(new KartLiftChangeStateC2S(this.handler.getLiftBlockEntityBlockPos()));
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

        // -------------------- race number --------------------
        this.raceNumberInputWidget = new TextFieldWidget(
                this.textRenderer,
                this.x + 160, this.y + 54,
                38, 9,
                Text.literal("Race Number Input")
        );
        this.raceNumberInputWidget.setMaxLength(2);
        this.raceNumberInputWidget.setDrawsBackground(false);
        this.raceNumberInputWidget.setFocused(false);
        this.raceNumberInputWidget.setVisible(false);
        if(this.handler.getLiftEntity().getKartEntity() != null) {
            int oldNumber =  this.handler.getLiftEntity().getKartEntity().getRaceNumber();
            this.raceNumberInputWidget.setText(String.valueOf(oldNumber));
            this.raceNumberInputWidget.setCursorToEnd(false);
        }
        this.raceNumberInputWidget.setChangedListener(newText -> {
            // get number
            int num;
            try {
                num = Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                if(newText.isEmpty()) num = 0;
                else return;
            }

            // send to kart
            KartLiftBlockEntity lift = this.handler.getLiftEntity();
            if(lift != null && lift.getKartEntity() != null) {
                if (num != lift.getKartEntity().getRaceNumber()) {
                    ClientPlayNetworking.send(new KartLiftModifyRaceNumberC2S(this.handler.getLiftBlockEntityBlockPos(), num));
                }
            }
        });
        this.addDrawableChild(this.raceNumberInputWidget);

    }


    private void changeTab(KartLiftScreenHandler.KartLiftTab tab) {
        // update locally
        this.currentTab = tab;

        // set visibility of text input for race number
        if(this.currentTab == KartLiftScreenHandler.KartLiftTab.CUSTOMIZATION) {
            if(this.raceNumberInputWidget != null) this.raceNumberInputWidget.setVisible(true);
        } else {
            if(this.raceNumberInputWidget != null) this.raceNumberInputWidget.setVisible(false);
        }

        // update in screen handler
        this.handler.setCurrentTab(tab);
    }

    // window resize protection
    @Override
    public void resize(MinecraftClient client, int width, int height) {
        KartLiftScreenHandler.KartLiftTab currentTab1 = currentTab;
        String savedText = this.raceNumberInputWidget.getText();
        this.init(client, width, height);
        this.raceNumberInputWidget.setText(savedText); // keep text
        this.changeTab(currentTab1); // keep tab
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

        // and also render the kart
        KartEntity kart = this.handler.getLiftEntity().getKartEntity();
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        if(kart != null) KartInventoryScreen.drawKart(context, i + 77, j + 67, 27, mouseX, mouseY, kart);
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // render stuff like normal inventory
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
