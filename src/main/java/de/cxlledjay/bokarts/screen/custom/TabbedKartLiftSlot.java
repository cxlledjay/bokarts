package de.cxlledjay.bokarts.screen.custom;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class TabbedKartLiftSlot extends Slot {
    public final KartLiftScreenHandler.KartLiftTab tabId;
    private final KartLiftScreenHandler handler;

    public TabbedKartLiftSlot(Inventory inventory, int index, int x, int y, KartLiftScreenHandler.KartLiftTab tabId, KartLiftScreenHandler handler) {
        super(inventory, index, x, y);
        this.tabId = tabId;
        this.handler = handler;
    }

    // logic for tabbed inventory
    @Override
    public boolean isEnabled() {
        return handler.getCurrentTab() == this.tabId;
    }

    // only allow one item in all slots
    @Override
    public int getMaxItemCount() {
        return 1;
    }
}
