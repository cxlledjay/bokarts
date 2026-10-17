package de.cxlledjay.bokarts.screen.custom;

import de.cxlledjay.bokarts.block.entity.custom.KartLiftBlockEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;

public class KartLiftInventory extends SimpleInventory {

    private final KartLiftBlockEntity kartLiftBlockEntity;

    public KartLiftInventory(int size, KartLiftBlockEntity kartLiftBlockEntity) {
        super(size);
        this.kartLiftBlockEntity = kartLiftBlockEntity;
    }

    // called when stack changes
    @Override
    public void setStack(int slot, ItemStack newStack) {
        // get stack before change
        ItemStack oldStack = this.getStack(slot);

        // remove from kart
        if (!oldStack.isEmpty() && !ItemStack.areEqual(oldStack, newStack)) {
            this.kartLiftBlockEntity.removePropertyFromItemStack(oldStack);
        }

        // perform swap
        super.setStack(slot, newStack);

        // apply new stack to kart
        if (!newStack.isEmpty() && !ItemStack.areEqual(oldStack, newStack)) {
            this.kartLiftBlockEntity.setPropertyFromItemStack(newStack);
        }
    }

    // --- 2. Intercepting Standard Extractions (e.g. Shift-Clicking) ---
    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack oldStack = super.removeStack(slot, amount);
        if (!oldStack.isEmpty()) {
            this.kartLiftBlockEntity.removePropertyFromItemStack(oldStack);
        }
        return oldStack;
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack oldStack = super.removeStack(slot);
        if (!oldStack.isEmpty()) {
            this.kartLiftBlockEntity.removePropertyFromItemStack(oldStack);
        }
        return oldStack;
    }

}
