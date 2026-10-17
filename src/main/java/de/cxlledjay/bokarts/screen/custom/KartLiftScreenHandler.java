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
    public static final int LIFT_INVENTORY_SIZE = 12;
    private final Inventory liftInventory;
    private final Inventory kartInventory;

    // corresponding lift block entity block pos
    private final BlockPos liftBlockEntityBlockPos;
    private final World world;


    // keeping track of the current tab
    public enum KartLiftTab {
        INSERT_KART_ITEM,
        MODIFICATION,
        CUSTOMIZATION,
        KEY,
        UPGRADE
    }
    private KartLiftTab tab;




    // client constructor
    public KartLiftScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos pos) {
        this(
                syncId,
                playerInventory,
                new SimpleInventory(LIFT_INVENTORY_SIZE),
                new SimpleInventory(1),
                pos);
    }


    // server constructor
    public KartLiftScreenHandler(int syncId, PlayerInventory playerInventory, Inventory liftInventory, Inventory blockEntityInventory, BlockPos liftBlockEntityBlockPos) {
        super(ModScreenHandlers.KART_LIFT_SCREEN_HANDLER, syncId);

        // init final fields
        this.liftBlockEntityBlockPos = liftBlockEntityBlockPos;
        this.world = playerInventory.player.getWorld();
        this.liftInventory = liftInventory;
        this.kartInventory = blockEntityInventory;

        // check inventories
        checkSize(this.liftInventory, LIFT_INVENTORY_SIZE);
        checkSize(this.kartInventory, 1);

        // TODO: create inventory from kart


        // --- slots ---
        // insert kart
        this.addSlot(new TabbedKartLiftSlot(this.kartInventory, 0, 7, 14, KartLiftTab.INSERT_KART_ITEM, this));
        // modification
        this.addSlot(new TabbedKartLiftSlot(this.liftInventory, 0, 131, 48, KartLiftTab.MODIFICATION, this));
        this.addSlot(new TabbedKartLiftSlot(this.liftInventory, 1, 157, 48, KartLiftTab.MODIFICATION, this));
        this.addSlot(new TabbedKartLiftSlot(this.liftInventory, 2, 183, 48, KartLiftTab.MODIFICATION, this));
        this.addSlot(new TabbedKartLiftSlot(this.liftInventory, 3, 209, 48, KartLiftTab.MODIFICATION, this));
        // customization
        this.addSlot(new TabbedKartLiftSlot(this.liftInventory, 4, 131, 48, KartLiftTab.CUSTOMIZATION, this));
        // key
        this.addSlot(new TabbedKartLiftSlot(this.liftInventory, 5, 131, 48, KartLiftTab.KEY, this));
        this.addSlot(new TabbedKartLiftSlot(this.liftInventory, 6, 152, 48, KartLiftTab.KEY, this));
        this.addSlot(new TabbedKartLiftSlot(this.liftInventory, 7, 198, 48, KartLiftTab.KEY, this));
        // upgrades
        this.addSlot(new TabbedKartLiftSlot(this.liftInventory, 8, 131, 48, KartLiftTab.UPGRADE, this));
        this.addSlot(new TabbedKartLiftSlot(this.liftInventory, 9, 157, 48, KartLiftTab.UPGRADE, this));
        this.addSlot(new TabbedKartLiftSlot(this.liftInventory, 10, 183, 48, KartLiftTab.UPGRADE, this));
        this.addSlot(new TabbedKartLiftSlot(this.liftInventory, 11, 209, 48, KartLiftTab.UPGRADE, this));


        // player inventory
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }




    // -------------------- tabs --------------------

    public KartLiftTab getCurrentTab() {
        return this.tab;
    }

    public void setCurrentTab(KartLiftTab tab) {
        this.tab = tab;
    }


    // -------------------- the lift --------------------

    public BlockPos getLiftBlockEntityBlockPos() {
        return this.liftBlockEntityBlockPos;
    }

    public KartLiftBlockEntity getLiftEntity() {
        if (this.world != null) {
            BlockEntity be = this.world.getBlockEntity(this.liftBlockEntityBlockPos);
            if (be instanceof KartLiftBlockEntity lift) {
                return lift;
            }
        }
        return null;
    }



    // -------------------- vanilla logic --------------------

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
