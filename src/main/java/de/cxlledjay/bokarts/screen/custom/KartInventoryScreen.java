package de.cxlledjay.bokarts.screen.custom;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.entity.custom.KartEntity;
import de.cxlledjay.bokarts.networking.packet.AddFuelPayloadC2S;
import de.cxlledjay.bokarts.networking.packet.SetHornPayloadC2S;
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
    ButtonTextures REFUEL_BUTTON_TEXTURES = new ButtonTextures(
            BoKarts.id("refuel"),
            BoKarts.id("refuel_highlighted")
    );
    ButtonTextures HORN_PREVIOUS_TEXTURES = new ButtonTextures(
            BoKarts.id("horn_prev"),
            BoKarts.id("horn_prev_highlighted")
    );
    ButtonTextures HORN_NEXT_TEXTURES = new ButtonTextures(
            BoKarts.id("horn_next"),
            BoKarts.id("horn_next_highlighted")
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
                this.x + 8, this.y + 34,
                16, 16,
                REFUEL_BUTTON_TEXTURES,
                button -> {
                    // click logic: send packet to add fuel
                    AddFuelPayloadC2S payloadC2S = new AddFuelPayloadC2S();
                    ClientPlayNetworking.send(payloadC2S);
                }
        ) {
            @Override
            public void setFocused(boolean focused) {
                // ignore focusing
                super.setFocused(false);
            }
        });

        // horn cycle buttons
        this.addDrawableChild(new TexturedButtonWidget(
                this.x + 147, this.y + 57,
                9, 10,
                HORN_PREVIOUS_TEXTURES,
                button -> {
                    // click logic: send packet server
                    SetHornPayloadC2S payloadC2S = new SetHornPayloadC2S(-1);
                    ClientPlayNetworking.send(payloadC2S);
                }
        ) {
            @Override
            public void setFocused(boolean focused) {
                // ignore focusing
                super.setFocused(false);
            }
        });

        this.addDrawableChild(new TexturedButtonWidget(
                this.x + 156, this.y + 57,
                9, 10,
                HORN_NEXT_TEXTURES,
                button -> {
                    // click logic: send packet server
                    SetHornPayloadC2S payloadC2S = new SetHornPayloadC2S(1);
                    ClientPlayNetworking.send(payloadC2S);
                }
        ) {
            @Override
            public void setFocused(boolean focused) {
                // ignore focusing
                super.setFocused(false);
            }
        });
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(BACKGROUND_TEXTURE, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);

        // TODO: render kart like horses
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // render stuff like normal inventory
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);

        // read from tracked data
        String fuelCapacityString = "";
        String odoString = "";
        String hornString = "";
        int fuelColor = 0xB71C1C; //red
        if(this.kart != null){
            fuelCapacityString = this.kart.getFormattedFuelCapacityString(this.kart.currentFuel);
            odoString = this.kart.getFormattedDistanceString(this.kart.currentOdometer);
            hornString = "[" + (this.kart.getHornSound().ordinal()+1) + "/10]";

            float fuelCapacityPercentage = this.kart.currentFuel / KartEntity.fuelTankMaxCapacity;
            if(fuelCapacityPercentage > 0.5) {
                fuelColor = 0x0E6B1F; //green
            } else if(fuelCapacityPercentage > 0.2) {
                fuelColor = 0xD35B1B; //orange
            }
        }

        // display information
        context.drawText(this.textRenderer, fuelCapacityString  , this.x + 102, this.y + 23,        fuelColor, false);
        context.drawText(this.textRenderer, odoString           , this.x + 102, this.y + 23 + 18,   0xE29700, false);
        context.drawText(this.textRenderer, hornString   ,       this.x + 102, this.y + 23 + 36,    0x3E349E, false);
    }
}
