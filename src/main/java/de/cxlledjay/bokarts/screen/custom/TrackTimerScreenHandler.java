package de.cxlledjay.bokarts.screen.custom;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.block.ModBlocks;
import de.cxlledjay.bokarts.block.entity.custom.TrackTimerBlockEntity;
import de.cxlledjay.bokarts.screen.ModScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TrackTimerScreenHandler extends ScreenHandler {

    // button ids
    public static final int BUTTON_ID_MODE = 0;
    public static final int BUTTON_ID_START = 1;
    public static final int BUTTON_ID_RESET = 2;
    public static final int BUTTON_ID_PRINT_TA = 3;
    public static final int BUTTON_ID_SET_LAPS = 4;

    // info from block entity
    private final PropertyDelegate propertyDelegate;
    private final ScreenHandlerContext context;
    public final TrackTimerBlockEntity blockEntity;
    private final Inventory blockInventory;
    private final World world;



    // client constructor
    public TrackTimerScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY, new ArrayPropertyDelegate(2), null, new SimpleInventory(10));
    }

    public TrackTimerScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, PropertyDelegate delegate, TrackTimerBlockEntity blockEntity, Inventory blockInventory) {
        super(ModScreenHandlers.TRACK_TIMER_SCREEN_HANDLER, syncId);

        this.context = context;
        this.blockEntity = blockEntity;
        this.world = playerInventory.player.getWorld();
        this.blockInventory = blockInventory;

        this.propertyDelegate = delegate;
        this.addProperties(delegate);

        // paper input slot
        this.addSlot(new Slot(blockInventory, 0, 8, 18) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(Items.PAPER);
            }
        });
        // output slots
        for(int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(blockInventory, 1+ i, 8 + 18 * i, 60) {
                @Override
                public boolean canInsert(ItemStack stack) {
                    return false;
                }
            });
        }

        // player inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 92 + i * 18));
            }
        }

        // player hotbar
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 150));
        }

    }


    // -------------------- button click --------------------
    public static int encodeButtonId(int buttonId, int payload) {
        return (buttonId << 24) | (payload & 0xFFFFFF);
    }
    @Override
    public boolean onButtonClick(PlayerEntity player, int encodedId) {
        int id = encodedId >> 24;
        int payload = encodedId & 0xFFFFFF;

        switch (id) {
            case BUTTON_ID_MODE:
                this.cycleMode();
                break;
            case BUTTON_ID_START:
                this.startTrackTimer(player);
                break;
            case BUTTON_ID_RESET:
                this.resetTrackTimer(player);
                break;
            case BUTTON_ID_PRINT_TA:
                this.printResultOfPlayer(player);
                break;
            case BUTTON_ID_SET_LAPS:
                this.setLaps(payload);
                break;
            default:
                return false;
        }
        return true;
    }


    // -------------------- interface for screen --------------------



    @Nullable
    public TrackTimerBlockEntity.TrackTimerMode getCurrentMode() {
        return TrackTimerBlockEntity.TrackTimerMode.values()[this.propertyDelegate.get(0)];
    }

    public int getLapAmount() {
        return this.propertyDelegate.get(1);
    }

    public String getModeString() {
        if(this.getCurrentMode() == TrackTimerBlockEntity.TrackTimerMode.TIME_ATTACK) return "Mode: Time Attack";
        else if(this.getCurrentMode() == TrackTimerBlockEntity.TrackTimerMode.GRAND_PRIX) return "Mode: Grand Prix";
        else return "Error";
    }


    // -------------------- interface for block entity --------------------

    private void cycleMode() {
        if(this.blockEntity != null) {
            this.blockEntity.cycleMode();
        }
    }

    private void startTrackTimer(PlayerEntity player) {
        if(this.blockEntity == null) return;
        this.blockEntity.start(player);
    }

    private void resetTrackTimer(PlayerEntity player) {
        if(this.blockEntity != null) {
            this.blockEntity.stopOrReset(player);
        }
    }

    private void printResultOfPlayer(PlayerEntity player) {
        if(this.blockEntity != null) this.blockEntity.printTimeAttackTrackRecord(player);
    }

    private void setLaps(int laps) {
        if(this.blockEntity != null) this.blockEntity.setLapCount(laps);
    }





    // -------------------- vanilla logic --------------------

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(context, player, ModBlocks.TRACK_TIMER_BLOCK);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);

        if (this.blockEntity != null) {
            this.blockEntity.close(player);
        }
    }
}
