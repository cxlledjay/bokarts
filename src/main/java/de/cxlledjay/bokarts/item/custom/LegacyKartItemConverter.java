package de.cxlledjay.bokarts.item.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LegacyKartItemConverter extends Item {

    private final Item newItem;

    public LegacyKartItemConverter(Settings settings, Item newItem) {
        super(settings);
        this.newItem = newItem;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient() && entity instanceof PlayerEntity player) {
            ItemStack newStack = new ItemStack(this.newItem, stack.getCount());
            newStack.applyChanges(stack.getComponentChanges());
            player.getInventory().setStack(slot, newStack);
        }
    }
}
