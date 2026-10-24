package de.cxlledjay.bokarts.screen.client;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.block.entity.custom.TrackTimerBlockEntity;
import de.cxlledjay.bokarts.screen.custom.TrackTimerScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class TrackTimerScreen extends HandledScreen<TrackTimerScreenHandler> {

    private static final Identifier BACKGROUND_TEXTURE = BoKarts.id("textures/gui/track_timer/background.png");

    // buttons
    private static final ButtonTextures START_BUTTON_TEXTURES = new ButtonTextures(
            BoKarts.id("track_timer/start"),
            BoKarts.id("track_timer/start_hovered")
    );
    private static final ButtonTextures RESET_BUTTON_TEXTURES = new ButtonTextures(
            BoKarts.id("track_timer/reset"),
            BoKarts.id("track_timer/reset_hovered")
    );


    private ButtonWidget modeButton;
    private ButtonWidget printButton;
    private TextFieldWidget lapsTextInput;



    public TrackTimerScreen(TrackTimerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);

        // size
        this.backgroundHeight = 172;
        this.backgroundWidth = 176;

        // titles
        this.playerInventoryTitleY += 8;
    }

    @Override
    protected void init() {
        super.init();

        // mode button
        this.modeButton = this.addDrawableChild(new ButtonWidget.Builder(
                Text.literal(this.handler.getModeString()),
                button -> {
                    int encodedButtonId = TrackTimerScreenHandler.encodeButtonId(TrackTimerScreenHandler.BUTTON_ID_MODE, 0);
                    if(this.handler.onButtonClick(this.client.player, encodedButtonId)) {
                        this.client.interactionManager.clickButton(this.handler.syncId, encodedButtonId);
                    }
                }
        ).dimensions(this.x + 34, this.y + 17, 135, 18).build());

        // start button
        this.addDrawableChild(new TexturedButtonWidget(
                this.x + 34, this.y + 38,
                18, 18,
                START_BUTTON_TEXTURES,
                button -> {
                    int encodedButtonId = TrackTimerScreenHandler.encodeButtonId(TrackTimerScreenHandler.BUTTON_ID_START, 0);
                    if(this.handler.onButtonClick(this.client.player, encodedButtonId)) {
                        this.client.interactionManager.clickButton(this.handler.syncId, encodedButtonId);
                    }
                }
        ));

        // reset button
        this.addDrawableChild(new TexturedButtonWidget(
                this.x + 61, this.y + 38,
                18, 18,
                RESET_BUTTON_TEXTURES,
                button -> {
                    int encodedButtonId = TrackTimerScreenHandler.encodeButtonId(TrackTimerScreenHandler.BUTTON_ID_RESET, 0);
                    if(this.handler.onButtonClick(this.client.player, encodedButtonId)) {
                        this.client.interactionManager.clickButton(this.handler.syncId, encodedButtonId);
                    }
                }
        ));

        // optional print button
        this.printButton = this.addDrawableChild(new ButtonWidget.Builder(
                Text.literal("Print results"),
                button -> {
                    int encodedButtonId = TrackTimerScreenHandler.encodeButtonId(TrackTimerScreenHandler.BUTTON_ID_PRINT_TA, 0);
                    if(this.handler.onButtonClick(this.client.player, encodedButtonId)) {
                        this.client.interactionManager.clickButton(this.handler.syncId, encodedButtonId);
                    }
                }
        ).dimensions(this.x + 88, this.y + 38, 81, 18).build());

        // optional lap input
        this.lapsTextInput = new TextFieldWidget(
                this.textRenderer,
                this.x + 88, this.y + 38,
                81, 18,
                Text.literal("Lap count")
        );
        this.lapsTextInput.setTooltip(Tooltip.of(Text.literal("Set the lap count")));
        this.lapsTextInput.setPlaceholder(Text.literal("Lap count"));
        this.lapsTextInput.setMaxLength(3);
        this.lapsTextInput.setDrawsBackground(true);
        this.lapsTextInput.setChangedListener((newString) -> {

            // parse the string
            try {
                int newLaps = Integer.parseInt(newString);
                int encodedButtonId = TrackTimerScreenHandler.encodeButtonId(TrackTimerScreenHandler.BUTTON_ID_SET_LAPS, newLaps);
                if(this.handler.onButtonClick(this.client.player, encodedButtonId)) {
                    this.client.interactionManager.clickButton(this.handler.syncId, encodedButtonId);
                }
            } catch (NumberFormatException e) {
                return;
            }
        });
        this.addDrawableChild(this.lapsTextInput);
    }


    @Override
    protected void handledScreenTick() {
        super.handledScreenTick();

        if(this.handler.getCurrentMode() == null) return;
        else if(this.handler.getCurrentMode() == TrackTimerBlockEntity.TrackTimerMode.TIME_ATTACK) {
            this.lapsTextInput.visible = false;
            this.lapsTextInput.active = false;
            this.printButton.visible = true;
            this.printButton.active = true;
        } else if(this.handler.getCurrentMode() == TrackTimerBlockEntity.TrackTimerMode.GRAND_PRIX) {
            this.lapsTextInput.visible = true;
            this.lapsTextInput.active = true;
            this.printButton.visible = false;
            this.printButton.active = false;
        }
        modeButton.setMessage(Text.literal(this.handler.getModeString()));
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(BACKGROUND_TEXTURE,  this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
