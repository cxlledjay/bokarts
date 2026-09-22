package de.cxlledjay.bokarts.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.config.ClientSyncedConfig;
import de.cxlledjay.bokarts.entity.custom.KartEntity;
import de.cxlledjay.bokarts.networking.packet.AddFuelPayloadC2S;
import de.cxlledjay.bokarts.networking.packet.SetHornPayloadC2S;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Quaternionf;


@Environment(EnvType.CLIENT)
public class KartInventoryScreen extends HandledScreen<KartInventoryScreenHandler> {

    private KartEntity kart;

    private static final Identifier BACKGROUND_TEXTURE = BoKarts.id("textures/gui/kart_inventory/kart_inventory.png");
    ButtonTextures REFUEL_BUTTON_TEXTURES = new ButtonTextures(
            BoKarts.id("refuel"),
            BoKarts.id("refuel_highlighted")
    );
    private static final Identifier FURNACE_UNLIT = Identifier.of("minecraft", "textures/block/furnace_front.png");
    private static final Identifier FURNACE_LIT = Identifier.of("minecraft", "textures/block/furnace_front_on.png");
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
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(BACKGROUND_TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);

        // render furnace unlit or lit, depending on fuel level
        boolean isBurning = this.kart != null && this.kart.currentFuel > 0;
        Identifier furnaceTexture = isBurning ? FURNACE_LIT : FURNACE_UNLIT;

        int furnaceX = this.x + 8;
        int furnaceY = this.y + 54;
        context.drawTexture(furnaceTexture, furnaceX, furnaceY, 0, 0, 16, 16, 16, 16);

        // render kart like horses
        this.drawKart(context, i + 52, j + 42, 21, mouseX, mouseY, this.kart);
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
            fuelCapacityString = KartEntity.getFormattedFuelCapacityString(this.kart.currentFuel);
            odoString = KartEntity.getFormattedDistanceString(this.kart.currentOdometer);
            hornString = "[" + (this.kart.getHornSound().ordinal()+1) + "/10]";

            float fuelCapacityPercentage = this.kart.currentFuel / ClientSyncedConfig.getMaxFuelCapacity();
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



    // helper method to draw kart following the mouse
    private void drawKart(DrawContext context, int x, int y, int size, float mouseX, float mouseY, KartEntity kart) {
        // 1. Swap the subtraction order to fix the inverted tracking
        float lookX = (float)Math.atan((mouseX - x) / 40.0f);
        float lookY = (float)Math.atan((mouseY - y) / 40.0f);

        MatrixStack matrices = context.getMatrices();
        matrices.push();

        matrices.translate(x, y, 50.0);
        matrices.scale((float)size, (float)size, (float)-size);

        Quaternionf rotation = new Quaternionf().rotateZ((float)Math.PI);

        // 2. Adjust Pitch (Up/Down)
        // If up/down feels inverted to you as well, just remove the negative sign here:
        rotation.rotateX(-lookY * 20.0F * ((float)Math.PI / 180F));

        // 3. Change 135.0F to 180.0F so the kart faces straight out of the screen
        rotation.rotateY((lookX * 40.0F + 180.0F) * ((float)Math.PI / 180F));

        matrices.multiply(rotation);

        // 4. Set up the Render Dispatcher
        EntityRenderDispatcher dispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
        dispatcher.setRenderShadows(false); // No ground shadow in the UI

        // 5. Render the entity
        RenderSystem.runAsFancy(() -> {
            // 0xF000F0 is the magic number for "Full Brightness" in the lighting map
            dispatcher.render(kart, 0.0, 0.0, 0.0, 0.0F, 1.0F, matrices, context.getVertexConsumers(), 0xF000F0);
        });

        // 6. Flush the buffers to draw to the screen
        context.draw();

        // 7. Cleanup
        dispatcher.setRenderShadows(true);
        matrices.pop();
    }
}
