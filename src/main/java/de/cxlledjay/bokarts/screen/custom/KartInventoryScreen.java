package de.cxlledjay.bokarts.screen.custom;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.entity.custom.KartEntity;
import de.cxlledjay.bokarts.networking.packet.AddFuelPayloadC2S;
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
public class KartInventoryScreen extends HandledScreen<KartInventoryScreenHandler> {

    private KartEntity kart;

    private static final Identifier BACKGROUND_TEXTURE = BoKarts.id("textures/gui/kart_inventory/kart_inventory.png");
    ButtonTextures MY_BUTTON_TEXTURES = new ButtonTextures(
            BoKarts.id("refuel"),
            BoKarts.id("refuel_highlighted")
    );

    public KartInventoryScreen(KartInventoryScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 176;
        this.backgroundHeight = 166;
    }

    @Override
    protected void init() {
        super.init();

        // get kart of player
        if (this.client != null && this.client.player != null) {
            if (this.client.player.getVehicle() instanceof KartEntity kartEntity) {
                this.kart = kartEntity;
            }
        }

        // refuel button
        this.addDrawableChild(new TexturedButtonWidget(
                this.x + 8, this.y + 31,
                16, 16,
                MY_BUTTON_TEXTURES,
                button -> {
                    // click logic: send packet to add fuel
                    AddFuelPayloadC2S payloadC2S = new AddFuelPayloadC2S();
                    ClientPlayNetworking.send(payloadC2S);
                    button.setFocused(false);
                }
        ));

    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(BACKGROUND_TEXTURE, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);

        // TODO: render kart like horses
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);

        // read from tracked data
        String rangeString = "";
        if(this.kart != null) rangeString = this.kart.getFuelRangeString();



        // TODO: fixme
        context.drawText(this.textRenderer, "Range: " + rangeString, this.x + 20, this.y + 70, 0x404040, false);
    }
}
