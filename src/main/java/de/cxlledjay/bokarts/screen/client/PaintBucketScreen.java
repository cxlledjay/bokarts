package de.cxlledjay.bokarts.screen.client;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.component.ModDataComponentTypes;
import de.cxlledjay.bokarts.entity.ModEntities;
import de.cxlledjay.bokarts.entity.custom.kart.KartEntity;
import de.cxlledjay.bokarts.entity.custom.kart.property.*;
import de.cxlledjay.bokarts.screen.custom.PaintBucketScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.util.Random;

public class PaintBucketScreen extends HandledScreen<PaintBucketScreenHandler> {

    // textures
    private static final Identifier BACKGROUND_TEXTURE = BoKarts.id("textures/gui/paint_bucket/background.png");
    private static final ButtonTextures MODE_BODY_BUTTON_TEXTURES = new ButtonTextures(
            BoKarts.id("textures/gui/paint_bucket/mode_body_unhovered.png"),
            BoKarts.id("textures/gui/paint_bucket/mode_body_hovered.png")
    );
    private static final ButtonTextures MODE_WHEELS_BUTTON_TEXTURES = new ButtonTextures(
            BoKarts.id("textures/gui/paint_bucket/mode_wheels_unhovered.png"),
            BoKarts.id("textures/gui/paint_bucket/mode_wheels_hovered.png")
    );

    // color input
    private PaintBucketSliderWidget hueSlider;
    private PaintBucketSliderWidget saturationSlider;
    private PaintBucketSliderWidget valueSlider;
    private TextFieldWidget hexStringInputWidget;
    private int localColorPrev, localColor;

    private boolean isUpdatingFromSlider;

    // preview
    private final KartEntity kartPreview;

    public PaintBucketScreen(PaintBucketScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);

        // size
        this.backgroundWidth = 230;
        this.backgroundHeight = 186;

        // titles
        this.titleY--;
        this.playerInventoryTitleY = 92;
        this.titleX += 27;
        this.playerInventoryTitleX += 27;

        // init random preview kart
        this.kartPreview = new KartEntity(ModEntities.KART_ENTITY_TYPE, inventory.player.getWorld());
        Random rng = new Random();
        this.kartPreview.setWheelType(WheelType.fromId(rng.nextInt(1, WheelType.values().length)));
        this.kartPreview.setEngineType(EngineType.fromId(rng.nextInt(EngineType.values().length)));
        this.kartPreview.setSpoilerType(SpoilerType.fromId(rng.nextInt(SpoilerType.values().length)));
        this.kartPreview.setAeroType(AeroType.fromId(rng.nextInt(AeroType.values().length)));

        // init local values
        this.localColor = handler.getActiveColor();
        this.localColorPrev = this.localColor;
        this.isUpdatingFromSlider = false;
    }


    private void setColorLocally(int h, int s, int v) {
        // update color locally
        this.localColor = Color.HSBtoRGB((float)h/360.0f, (float)s/100.0f, (float)v/100.0f);
        this.handler.setActiveColor(this.localColor);

        // set to text widget without causing updates
        String hexString = String.format("%06X", this.localColor & 0xFFFFFF);
        this.isUpdatingFromSlider = true;
        this.hexStringInputWidget.setText(hexString);
        this.isUpdatingFromSlider = false;
    }

    private void setColorLocally(String hexString) {
        if(hexString == null || hexString.length() != 6) return;
        int col;
        try {
            col = Integer.parseInt(hexString, 16);
        }  catch (NumberFormatException e) {
            return;
        }
        // conversion was successful
        this.localColor = col;
        this.handler.setActiveColor(this.localColor);

        // update sliders
        float[] hsbVals = new float[3];
        int r, g, b;
        r = (this.localColor >> 16) & 0xFF;
        g = (this.localColor >> 8) & 0xFF;
        b = (this.localColor) & 0xFF;
        Color.RGBtoHSB(r, g, b, hsbVals);
        this.hueSlider.setValue((int) (hsbVals[0] * 360));
        this.saturationSlider.setValue((int) (hsbVals[1] * 100));
        this.valueSlider.setValue((int) (hsbVals[2] * 100));
    }

    private void sendUpdateToServer() {
        if(this.localColor != this.localColorPrev) {
            // TODO: send update
            BoKarts.LOGGER.info("Sending update to server");

            // and set prev to current
            this.localColorPrev = this.localColor;
        }
    }

    private void setWidgetsOnly(int newColor) {
        // update for sending packets
        this.localColor = newColor;
        this.localColorPrev = this.localColor;

        // update sliders
        float[] hsbVals = new float[3];
        int r, g, b;
        r = (this.localColor >> 16) & 0xFF;
        g = (this.localColor >> 8) & 0xFF;
        b = (this.localColor) & 0xFF;
        Color.RGBtoHSB(r, g, b, hsbVals);
        this.hueSlider.setValue((int) (hsbVals[0] * 360));
        this.saturationSlider.setValue((int) (hsbVals[1] * 100));
        this.valueSlider.setValue((int) (hsbVals[2] * 100));

        // update text widget
        String hexString = String.format("%06X", this.localColor);
        this.isUpdatingFromSlider = true;
        this.hexStringInputWidget.setText(hexString);
        this.isUpdatingFromSlider = false;
    }



    private void onModeChange(PaintBucketScreenHandler.ActiveSelection mode) {
        BoKarts.LOGGER.info("Mode changed to: {}", mode.toString());
        this.setWidgetsOnly(this.handler.getActiveColor());
    }





    @Override
    protected void init() {
        super.init();

        // change mode button
        this.addDrawableChild(new PaintBucketModeSelectorWidget(
                this.x, this.y + 8,
                27, 30,
                PaintBucketScreenHandler.ActiveSelection.BODY
        ));


        // hsv sliders
        hueSlider = this.addDrawableChild( new PaintBucketSliderWidget(
                this.x + 34, this.y + 16,
                76, 14,
                Text.translatable("gui.bokarts.paint_bucket.hue").getString(), 360, 0,
                (newHue) -> {
                    this.setColorLocally(newHue, saturationSlider.getValue(), valueSlider.getValue());
                }
        ));
        saturationSlider = this.addDrawableChild( new PaintBucketSliderWidget(
                this.x + 34, this.y + 34,
                76, 14,
                Text.translatable("gui.bokarts.paint_bucket.saturation").getString(), 100, 0,
                (newSaturation) -> {
                    this.setColorLocally(hueSlider.getValue(), newSaturation, valueSlider.getValue());
                }
        ));
        valueSlider = this.addDrawableChild( new PaintBucketSliderWidget(
                this.x + 34, this.y + 52,
                76, 14,
                Text.translatable("gui.bokarts.paint_bucket.value").getString(), 100, 0,
                (newValue) -> {
                    this.setColorLocally(hueSlider.getValue(), saturationSlider.getValue(), newValue);
                }
        ));


        // hex value input
        this.hexStringInputWidget = new TextFieldWidget(
                this.textRenderer,
                this.x + 114, this.y + 73,
                52, 14,
                Text.literal("Color input as Hex String")
        );
        this.hexStringInputWidget.setMaxLength(6);
        this.hexStringInputWidget.setDrawsBackground(true);
        this.isUpdatingFromSlider = true;
        this.hexStringInputWidget.setText(String.format("%06X", this.localColor));
        this.isUpdatingFromSlider = false;
        this.hexStringInputWidget.setChangedListener((newString) -> {
            if(!isUpdatingFromSlider) {
                this.setColorLocally(newString);
                this.sendUpdateToServer();
            }
        });
        this.addDrawableChild(this.hexStringInputWidget);

        // init color
        this.setWidgetsOnly(this.handler.getActiveColor());

        // dye stealer run code
        this.handler.setOnColorUpdated( () -> this.setWidgetsOnly(this.handler.getActiveColor()));
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        // background
        context.drawTexture(BACKGROUND_TEXTURE, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);

        // render entity
        if(this.kartPreview == null) return; // safety
        if(this.handler.getLiveryItem() != null && this.handler.getLiveryItem() != ItemStack.EMPTY) {
            this.kartPreview.setBodyColor(this.handler.getBodyColor());
            this.kartPreview.setWheelColor(this.handler.getWheelColor());
            this.kartPreview.setBodyType(this.handler.getLiveryItem().getOrDefault(ModDataComponentTypes.KART_ITEM_BODY_TYPE, BodyType.NONE));
            KartInventoryScreen.drawKart(context,this.x + 155, this.y + 52, 27, mouseX, mouseY, this.kartPreview);
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {

        // check if something changed and update the server
        this.sendUpdateToServer();

        return super.mouseReleased(mouseX, mouseY, button);
    }

    // window resize protection
    @Override
    public void resize(MinecraftClient client, int width, int height) {
        int h, s, v;
        h = hueSlider.getValue();
        s = saturationSlider.getValue();
        v = valueSlider.getValue();

        this.init(client, width, height);

        // keep input values
        this.hueSlider.setValue(h);
        this.saturationSlider.setValue(s);
        this.valueSlider.setValue(v);
    }


    // mode selection
    private class PaintBucketModeSelectorWidget extends ButtonWidget {

        private PaintBucketScreenHandler.ActiveSelection mode;

        public PaintBucketModeSelectorWidget(int x, int y, int width, int height, PaintBucketScreenHandler.ActiveSelection mode) {
            super(x, y, width, height, Text.literal("change mode"), button -> {}, DEFAULT_NARRATION_SUPPLIER);
            this.mode = mode;
        }

        @Override
        public void onPress() {
            if(this.mode == PaintBucketScreenHandler.ActiveSelection.BODY) this.mode = PaintBucketScreenHandler.ActiveSelection.WHEELS;
            else this.mode = PaintBucketScreenHandler.ActiveSelection.BODY;

            // update handler
            if(handler.onButtonClick(client.player, this.mode.ordinal())) {
                client.interactionManager.clickButton(handler.syncId, this.mode.ordinal()); // update handler (server synced)
                onModeChange(mode); // update screen to new mode
            }
        }

        @Override
        protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
            ButtonTextures textures = switch(this.mode) {
                case  BODY -> MODE_BODY_BUTTON_TEXTURES;
                case WHEELS -> MODE_WHEELS_BUTTON_TEXTURES;
            };

            Identifier textureToDraw = textures.get(this.active, this.isSelected());
            context.drawTexture(textureToDraw, this.getX(), this.getY(), 0, 0, this.width, this.height, this.width, this.height);
        }

        @Override
        public boolean isFocused() {
            return false;
        }
    }
}
