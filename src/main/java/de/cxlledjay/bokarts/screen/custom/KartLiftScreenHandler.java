package de.cxlledjay.bokarts.screen.custom;

import de.cxlledjay.bokarts.block.entity.custom.KartLiftBlockEntity;
import de.cxlledjay.bokarts.item.ModItems;
import de.cxlledjay.bokarts.item.custom.KartItemv2;
import de.cxlledjay.bokarts.item.custom.modification.AeroItem;
import de.cxlledjay.bokarts.item.custom.modification.EngineItem;
import de.cxlledjay.bokarts.item.custom.modification.SpoilerItem;
import de.cxlledjay.bokarts.item.custom.modification.WheelsItem;
import de.cxlledjay.bokarts.screen.ModScreenHandlers;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
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
    private KartLiftTab tab = KartLiftTab.INSERT_KART_ITEM;




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


        // --- slots ---
        // insert kart
        this.addSlot(new TabbedKartLiftSlot(this.kartInventory, 0, 7, 14, KartLiftTab.INSERT_KART_ITEM, this){
            @Override
            public boolean canInsert(ItemStack stack) {
                return ((stack.getItem() instanceof KartItemv2) || (stack.getItem() == ModItems.CHASSIS_BLUEPRINT));
            }
        });
        // modification
        this.addSlot(new TabbedKartLiftSlot(this.liftInventory, 0, 131, 48, KartLiftTab.MODIFICATION, this) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return (stack.getItem() instanceof WheelsItem);
            }
        });
        this.addSlot(new TabbedKartLiftSlot(this.liftInventory, 1, 157, 48, KartLiftTab.MODIFICATION, this) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return (stack.getItem() instanceof EngineItem);
            }
        });
        this.addSlot(new TabbedKartLiftSlot(this.liftInventory, 2, 183, 48, KartLiftTab.MODIFICATION, this) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return (stack.getItem() instanceof SpoilerItem);
            }
        });
        this.addSlot(new TabbedKartLiftSlot(this.liftInventory, 3, 209, 48, KartLiftTab.MODIFICATION, this) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return (stack.getItem() instanceof AeroItem);
            }
        });
        // customization
        this.addSlot(new TabbedKartLiftSlot(this.liftInventory, 4, 131, 48, KartLiftTab.CUSTOMIZATION, this) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return (stack.getItem() == ModItems.LIVERY_TEMPLATE);
            }
        });
        // key
        this.addSlot(new TabbedKartLiftSlot(this.liftInventory, 5, 131, 48, KartLiftTab.KEY, this) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return (stack.getItem() == ModItems.KEY_BLANK);
            }
        });
        this.addSlot(new TabbedKartLiftSlot(this.liftInventory, 6, 152, 48, KartLiftTab.KEY, this) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return (stack.getItem() == ModItems.KEY_PAIRING_TOOL);
            }
        });
        this.addSlot(new TabbedKartLiftSlot(this.liftInventory, 7, 198, 48, KartLiftTab.KEY, this) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }
        });
        // upgrades
        this.addSlot(new TabbedKartLiftSlot(this.liftInventory, 8, 131, 48, KartLiftTab.UPGRADE, this) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false; //< TODO: add upgrades
            }
        });
        this.addSlot(new TabbedKartLiftSlot(this.liftInventory, 9, 157, 48, KartLiftTab.UPGRADE, this) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false; //< TODO: add upgrades
            }
        });
        this.addSlot(new TabbedKartLiftSlot(this.liftInventory, 10, 183, 48, KartLiftTab.UPGRADE, this) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false; //< TODO: add upgrades
            }
        });
        this.addSlot(new TabbedKartLiftSlot(this.liftInventory, 11, 209, 48, KartLiftTab.UPGRADE, this) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false; //< TODO: add upgrades
            }
        });


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

    private static final int TOTAL_LIFT_INVENTORY_SIZE = LIFT_INVENTORY_SIZE + 1;

    // boilerplate shift click code
    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < TOTAL_LIFT_INVENTORY_SIZE) {
                // clicking on item inside lift (=> into player inventory)
                if (!this.insertItem(originalStack, TOTAL_LIFT_INVENTORY_SIZE, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            else {
                boolean insertedIntoLift = false;

                // clicking on item inside player inventory (=> into lift)
                if(this.getLiftEntity().liftState == KartLiftBlockEntity.LiftState.EMPTY) {
                    // only allow items to go from player inv into kart item slot (0)
                    insertedIntoLift = this.insertItem(originalStack, 0, 1, false);
                } else if(this.getLiftEntity().liftState == KartLiftBlockEntity.LiftState.ACTIVE) {
                    // only allow items to go to tuning slots
                    insertedIntoLift = this.insertItem(originalStack, 1, TOTAL_LIFT_INVENTORY_SIZE, false);
                }

                // not accepted by lift: move between hotbar
                if (!insertedIntoLift) {
                    int playerMainEnd = TOTAL_LIFT_INVENTORY_SIZE + 27; // End of 3x9 grid

                    if (invSlot < playerMainEnd) {
                        // Moving from Main Inv -> Hotbar
                        if (!this.insertItem(originalStack, playerMainEnd, this.slots.size(), false)) {
                            return ItemStack.EMPTY;
                        }
                    } else {
                        // Moving from Hotbar -> Main Inv
                        if (!this.insertItem(originalStack, TOTAL_LIFT_INVENTORY_SIZE, playerMainEnd, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
            }

            // moved out of slot check
            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            // check if anything moved
            if (originalStack.getCount() == newStack.getCount()) {
                return ItemStack.EMPTY;
            }
        }
        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.liftInventory.canPlayerUse(player);
    }

    @Override
    public void onClosed(PlayerEntity player) {

        // security
        if (player instanceof ServerPlayerEntity) {

        // drop the key pairing slots
        int[] slotsToDrop = {5, 6, 7};

            for (int index : slotsToDrop) {
                ItemStack stack = this.liftInventory.getStack(index);

                if (!stack.isEmpty()) {
                    player.getInventory().offerOrDrop(stack.copy());
                    this.liftInventory.setStack(index, ItemStack.EMPTY);
                }
            }
        }

        super.onClosed(player);
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
