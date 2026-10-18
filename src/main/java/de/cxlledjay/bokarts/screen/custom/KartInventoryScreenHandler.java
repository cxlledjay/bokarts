package de.cxlledjay.bokarts.screen.custom;

import de.cxlledjay.bokarts.component.ModDataComponentTypes;
import de.cxlledjay.bokarts.entity.custom.kart.KartEntity;
import de.cxlledjay.bokarts.item.ModItems;
import de.cxlledjay.bokarts.item.custom.KartKey;
import de.cxlledjay.bokarts.screen.ModScreenHandlers;
import de.cxlledjay.bokarts.util.KartFuelItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

import java.util.UUID;

public class KartInventoryScreenHandler extends ScreenHandler {

    KartEntity kartEntity = null;

    private final Inventory fuelInventory = new SimpleInventory(2) {
        @Override
        public void markDirty() {
            super.markDirty();

            // update key
            if(kartEntity != null) {
                kartEntity.setKeyInIgnition(!this.getStack(1).isEmpty());
            }
        }
    };

    public KartInventoryScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ModScreenHandlers.KART_INVENTORY_SCREEN_HANDLER, syncId);

        // remember kart of player
        if(playerInventory.player.getVehicle() instanceof KartEntity kart) {
            this.kartEntity = kart;
        }

        // fuel slot
        this.addSlot(
                new Slot(this.fuelInventory, 0, 8, 18) {
                    @Override
                    public boolean canInsert(ItemStack stack) {
                        return KartFuelItems.isFuelItem(stack);
                    }
                }
        );

        // ignition
        this.addSlot(
                new Slot(this.fuelInventory, 1, 26, 54) {
                    @Override
                    public boolean canInsert(ItemStack stack) {
                        if(stack.getItem() instanceof KartKey) {
                            UUID keyUuid = stack.getOrDefault(ModDataComponentTypes.KART_KEY_PAIRED_UUID, null);
                            if(keyUuid != null) {
                                if(kartEntity != null) {
                                    UUID kartKeyUuid = kartEntity.getKeyUuid();

                                    return kartKeyUuid != null && kartKeyUuid.equals(keyUuid);
                                }
                            }
                        }
                        return false;
                    }
                }
        );

        // prefill ignition slot, if kart has key
        if(this.kartEntity != null) {
            if(this.kartEntity.isKeyInIgnition()) {
                UUID keyUuid = this.kartEntity.getKeyUuid();
                if(keyUuid != null) {
                    ItemStack pairedKey = new ItemStack(ModItems.KEY_PAIRED);
                    pairedKey.setCount(1);
                    pairedKey.set(ModDataComponentTypes.KART_KEY_PAIRED_UUID, keyUuid);
                    this.getSlot(1).setStack(pairedKey);
                }
            }
        }

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
        player.getInventory().offerOrDrop(this.getSlot(0).getStack().copy());
    }

    // boilerplate shift click code
    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.fuelInventory.size()) {
                if (!this.insertItem(originalStack, this.fuelInventory.size(), this.slots.size(), false)) {
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
