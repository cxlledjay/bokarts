package de.cxlledjay.bokarts.block.entity.custom;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.block.entity.ModBlockEntities;
import de.cxlledjay.bokarts.screen.custom.TrackTimerScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class TrackTimerBlockEntity extends BlockEntity implements NamedScreenHandlerFactory {

    // limits
    private static final int MAX_LAPS = 1000;

    // mode
    public enum TrackTimerMode {
        TIME_ATTACK, GRAND_PRIX
    }

    // settings by user
    private TrackTimerMode settingCurrentMode;
    private int settingLapCount;
    private boolean hasStarted;

    // inventory tracking
    public final Inventory inventory;

    // modify by only one player
    public PlayerEntity currentUser = null;




    public TrackTimerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TRACK_TIMER_BE, pos, state);
        this.settingCurrentMode = TrackTimerMode.TIME_ATTACK;
        this.settingLapCount = 1;
        this.hasStarted = false;
        this.inventory = new SimpleInventory(10);
    }


    // -------------------- interface for screen handler --------------------

    public void cycleMode() {
        if(this.settingCurrentMode ==  TrackTimerMode.GRAND_PRIX) this.settingCurrentMode = TrackTimerMode.TIME_ATTACK;
        else this.settingCurrentMode = TrackTimerMode.GRAND_PRIX;
    }

    public void setLapCount(int settingLapCount) {
        if(settingLapCount > MAX_LAPS) settingLapCount = MAX_LAPS;
        else if(settingLapCount < 1) settingLapCount = 1;
        this.settingLapCount = settingLapCount;
    }

    public void start() {
        if(this.hasStarted) return;
        // start logic
        BoKarts.LOGGER.info("Starting Track Timer BlockEntity");
        this.hasStarted = true;
    }

    public void stopOrReset() {
        if(!this.hasStarted) return;
        // stop / reset logic
        BoKarts.LOGGER.info("Stopping or Resetting Track Timer BlockEntity");
        this.hasStarted = false;
    }

    // property delegate for server -> client (screen + screen handler) sync
    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> settingCurrentMode.ordinal();
                case 1 -> settingLapCount;
                case 2 -> Boolean.compare(hasStarted, true);
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> {
                    if (value >= 0 && value < TrackTimerMode.values().length) {
                        settingCurrentMode = TrackTimerMode.values()[value];
                    }
                }
                case 1 -> settingLapCount = value;
            }
        }

        @Override
        public int size() {
            return 3; //< 3 variables tracked
        }
    };

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.bokarts.track_timer_block");
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new TrackTimerScreenHandler(
                syncId,
                playerInventory,
                ScreenHandlerContext.create(this.world, this.pos),
                this.propertyDelegate,
                this,
                this.inventory
        );
    }



    // -------------------- allow only one user --------------------

    public boolean tryOpen(PlayerEntity player) {
        if (this.currentUser == null || this.currentUser.isRemoved()) {
            this.currentUser = player;
            return true;
        }
        return this.currentUser == player;
    }

    public void close(PlayerEntity player) {
        if (this.currentUser == player) {
            this.currentUser = null;
        }
    }
}
