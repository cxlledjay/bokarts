package de.cxlledjay.bokarts.screen.custom;

import de.cxlledjay.bokarts.block.entity.custom.KartLiftBlockEntity;
import de.cxlledjay.bokarts.screen.ModScreenHandlers;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class KartLiftScreenHandler extends ScreenHandler {

    // tracked inventory
    private static final int LIFT_INVENTORY_SIZE = 13;
    private final Inventory liftInventory = new SimpleInventory(LIFT_INVENTORY_SIZE);

    // corresponding lift block entity block pos
    private BlockPos liftBlockEntityBlockPos;
    private World world;


    // keeping track of the current tab
    public enum KartLiftTab {
        INSERT_KART_ITEM,
        MODIFICATION,
        CUSTOMIZATION,
        KEY,
        UPGRADE
    }
    private KartLiftTab tab;




    // server sided constructor
    public KartLiftScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos liftBlockEntityBlockPos) {
        this(syncId, playerInventory);

        // init block entity
        this.liftBlockEntityBlockPos = liftBlockEntityBlockPos;
        this.world = playerInventory.player.getWorld();
    }


    // client sided constructor
    public KartLiftScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ModScreenHandlers.KART_LIFT_SCREEN_HANDLER, syncId);

        // check inventory
        checkSize(this.liftInventory, LIFT_INVENTORY_SIZE);

        // TODO: create inventory from kart


        // --- slots ---
        // insert kart
        this.addSlot(new TabbedKartLiftSlot(liftInventory, 0, 7, 14, KartLiftTab.INSERT_KART_ITEM, this));
        // modification
        this.addSlot(new TabbedKartLiftSlot(liftInventory, 1, 131, 48, KartLiftTab.MODIFICATION, this));
        this.addSlot(new TabbedKartLiftSlot(liftInventory, 2, 157, 48, KartLiftTab.MODIFICATION, this));
        this.addSlot(new TabbedKartLiftSlot(liftInventory, 3, 183, 48, KartLiftTab.MODIFICATION, this));
        this.addSlot(new TabbedKartLiftSlot(liftInventory, 4, 209, 48, KartLiftTab.MODIFICATION, this));
        // customization
        this.addSlot(new TabbedKartLiftSlot(liftInventory, 5, 131, 48, KartLiftTab.CUSTOMIZATION, this));
        // key
        this.addSlot(new TabbedKartLiftSlot(liftInventory, 6, 131, 48, KartLiftTab.KEY, this));
        this.addSlot(new TabbedKartLiftSlot(liftInventory, 7, 152, 48, KartLiftTab.KEY, this));
        this.addSlot(new TabbedKartLiftSlot(liftInventory, 8, 198, 48, KartLiftTab.KEY, this));
        // upgrades
        this.addSlot(new TabbedKartLiftSlot(liftInventory, 9, 131, 48, KartLiftTab.UPGRADE, this));
        this.addSlot(new TabbedKartLiftSlot(liftInventory, 10, 157, 48, KartLiftTab.UPGRADE, this));
        this.addSlot(new TabbedKartLiftSlot(liftInventory, 11, 183, 48, KartLiftTab.UPGRADE, this));
        this.addSlot(new TabbedKartLiftSlot(liftInventory, 12, 209, 48, KartLiftTab.UPGRADE, this));


        // player inventory
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    public KartLiftTab getCurrentTab() {
        return this.tab;
    }

    public void setCurrentTab(KartLiftTab tab) {
        this.tab = tab;
    }

    private KartLiftBlockEntity getLiftEntity() {
        if (this.world != null) {
            BlockEntity be = this.world.getBlockEntity(this.liftBlockEntityBlockPos);
            if (be instanceof KartLiftBlockEntity lift) {
                return lift;
            }
        }
        return null;
    }

    public boolean hasKartLoaded() {
        if(getLiftEntity() == null) return false;
        return getLiftEntity().liftState == KartLiftBlockEntity.LiftState.ACTIVE;
    }



    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.liftInventory.canPlayerUse(player);
    }



    // -------------------- helper methods --------------------

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 51 + l * 18, 106 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 51 + i * 18, 164));
        }
    }

}
