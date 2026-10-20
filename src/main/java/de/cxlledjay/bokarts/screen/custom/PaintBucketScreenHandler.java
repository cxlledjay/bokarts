package de.cxlledjay.bokarts.screen.custom;

import de.cxlledjay.bokarts.block.ModBlocks;
import de.cxlledjay.bokarts.component.ModDataComponentTypes;
import de.cxlledjay.bokarts.entity.custom.kart.property.BodyType;
import de.cxlledjay.bokarts.item.ModItems;
import de.cxlledjay.bokarts.screen.ModScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;

public class PaintBucketScreenHandler extends ScreenHandler {

    private final ScreenHandlerContext context;
    private final Inventory inventory;
    private final Slot liveryInputSlot;
    private final Slot colorMixInputSlot;
    private final Slot dyeStealerSlot;
    private final Slot outputSlot;

    // color brains
    private int bodyColor;
    private int wheelColor;
    public enum ActiveSelection {BODY, WHEELS}
    private ActiveSelection mode;
    private Runnable onColorUpdated = () -> {};


    public PaintBucketScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public PaintBucketScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ModScreenHandlers.PAINT_BUCKET_SCREEN_HANDLER, syncId);
        this.context = context;
        this.bodyColor = 0xFFFFFF;
        this.wheelColor = 0xFFFFFF;
        this.mode = ActiveSelection.BODY;
        this.inventory = new SimpleInventory(4) {
            @Override
            public void markDirty() {
                super.markDirty();

                // create output livery if not yet created
                if(!liveryInputSlot.getStack().isEmpty() && !colorMixInputSlot.getStack().isEmpty() && outputSlot.getStack().isEmpty()) {
                    ItemStack originalItem = liveryInputSlot.getStack();
                    ItemStack outputItemStack = originalItem.copy();
                    outputItemStack.setCount(1);
                    outputItemStack.set(ModDataComponentTypes.KART_ITEM_BODY_TYPE, originalItem.getOrDefault(ModDataComponentTypes.KART_ITEM_BODY_TYPE, BodyType.NONE));
                    outputItemStack.set(ModDataComponentTypes.KART_ITEM_BODY_COLOR, bodyColor);
                    outputItemStack.set(ModDataComponentTypes.KART_ITEM_WHEEL_COLOR, wheelColor);
                    outputSlot.setStack(outputItemStack);
                }
            }
        };

        this.liveryInputSlot = this.addSlot(new Slot(inventory, 0, 35, 72) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(ModItems.LIVERY_TEMPLATE);
            }

            @Override
            public void setStack(ItemStack stack) {
                bodyColor = stack.getOrDefault(ModDataComponentTypes.KART_ITEM_BODY_COLOR, 0xFFFFFF);
                wheelColor = stack.getOrDefault(ModDataComponentTypes.KART_ITEM_WHEEL_COLOR, 0xFFFFFF);
                onColorUpdated.run(); //< update client
                super.setStack(stack);
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                outputSlot.setStack(ItemStack.EMPTY);
                bodyColor = 0xFFFFFF;
                wheelColor = 0xFFFFFF;
                onColorUpdated.run(); //< update client
                super.onTakeItem(player, stack);
            }
        });
        this.colorMixInputSlot = this.addSlot(new Slot(inventory, 1, 65, 72) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(Items.CLAY_BALL);
            }

            @Override
            public int getMaxItemCount() {
                return 1;
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                outputSlot.setStack(ItemStack.EMPTY);
                super.onTakeItem(player, stack);
            }
        });
        this.dyeStealerSlot = this.addSlot(new Slot(inventory, 2, 86, 72) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.getItem() instanceof DyeItem;
            }

            @Override
            public int getMaxItemCount() {
                return 1;
            }

            @Override
            public void setStack(ItemStack stack) {
                if(stack.getItem() instanceof DyeItem) {
                    setActiveColor(((DyeItem) stack.getItem()).getColor().getEntityColor() & 0xFFFFFF);
                    onColorUpdated.run(); //< update client
                }
                super.setStack(stack);
            }
        });
        this.outputSlot = this.addSlot(new Slot(inventory, 3, 179, 72) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                liveryInputSlot.setStack(ItemStack.EMPTY);
                colorMixInputSlot.setStack(ItemStack.EMPTY);
                outputSlot.setStack(ItemStack.EMPTY);

                super.onTakeItem(player, stack);
            }
        });

        // player inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 35 + j * 18, 104 + i * 18));
            }
        }
        // player hotbar
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 35 + i * 18, 162));
        }
    }


    // interface for screen

    public ItemStack getLiveryItem() {
        return this.liveryInputSlot.getStack();
    }

    public int getBodyColor() {
        return this.bodyColor;
    }

    public int getWheelColor() {
        return this.wheelColor;
    }

    public ActiveSelection getMode() {
        return this.mode;
    }

    private void setMode(ActiveSelection mode) {
        this.mode = mode;
    }

    public int getActiveColor() {
        if(this.mode == ActiveSelection.BODY) return this.bodyColor;
        else return this.wheelColor;
    }

    public void setActiveColor(int color) {
        if(this.mode == ActiveSelection.BODY) this.bodyColor = color;
        else this.wheelColor = color;
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        if(id == 0) this.setMode(ActiveSelection.BODY);
        else if (id == 1) this.setMode(ActiveSelection.WHEELS);
        else return false;
        return true;
    }

    public void setOnColorUpdated(Runnable callback) {
        this.onColorUpdated = callback;
    }


    // this should only be called by the server callback of the received update payload from the screen
    public void setActiveColorServerBound(int color) {
        this.setActiveColor(color);

        // also update output here (ON THE SERVER)
        this.setColorOfOutputItem();
    }

    public void setColorOfOutputItem() {
        if(this.outputSlot.getStack().isEmpty()) return; // do nothing
        this.outputSlot.getStack().set(ModDataComponentTypes.KART_ITEM_BODY_COLOR, bodyColor);
        this.outputSlot.getStack().set(ModDataComponentTypes.KART_ITEM_WHEEL_COLOR, wheelColor);
    }

    // -------------------- vanilla logic --------------------

    @Override
    public void onClosed(PlayerEntity player) {
        // security
        if (player instanceof ServerPlayerEntity) {
            // drop the input slots
            Slot[] toBeDropped = {this.liveryInputSlot, this.colorMixInputSlot, this.dyeStealerSlot};
            for (Slot slot : toBeDropped) {
                if (!slot.getStack().isEmpty()) {
                    player.getInventory().offerOrDrop(slot.getStack().copy());
                }
            }
            this.inventory.clear();
        }
        super.onClosed(player);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, ModBlocks.PAINT_BUCKET);
    }
}
