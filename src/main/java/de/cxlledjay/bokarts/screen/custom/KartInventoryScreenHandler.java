package de.cxlledjay.bokarts.screen.custom;

import de.cxlledjay.bokarts.entity.custom.KartEntity;
import de.cxlledjay.bokarts.screen.ModScreenHandlers;
import de.cxlledjay.bokarts.util.KartFuelItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class KartInventoryScreenHandler extends ScreenHandler {

    private final Inventory fuelInventory = new SimpleInventory(1);

    public KartInventoryScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ModScreenHandlers.KART_INVENTORY_SCREEN_HANDLER, syncId);

        // fuel slot
        this.addSlot(
                new Slot(this.fuelInventory, 0, 8, 18) {
                    @Override
                    public boolean canInsert(ItemStack stack) {
                        return KartFuelItems.isFuelItem(stack);
                    }
                }
        );

        // player inventory
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return player.getVehicle() instanceof KartEntity;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        // drop fuel item
        this.dropInventory(player, this.fuelInventory);
    }

    // boilerplate shift click code
    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.fuelInventory.size()) {
                if (!this.insertItem(originalStack, this.fuelInventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.fuelInventory.size(), false)) {
                return ItemStack.EMPTY;
            }
            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return newStack;
    }

    public ItemStack getFuelItemStack() {
        return this.fuelInventory.getStack(0);
    }



    // -------------------- helper methods --------------------

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
