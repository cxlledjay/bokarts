package de.cxlledjay.bokarts.block.entity.custom;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.block.custom.TrackTimer;
import de.cxlledjay.bokarts.block.entity.ModBlockEntities;
import de.cxlledjay.bokarts.screen.custom.KartLiftScreenHandler;
import de.cxlledjay.bokarts.screen.custom.TrackTimerScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
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
    private TrackTimerMode currentMode;
    private int lapCount;
    private boolean hasStarted;




    public TrackTimerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TRACK_TIMER_BE, pos, state);
        this.currentMode = TrackTimerMode.TIME_ATTACK;
        this.lapCount = 1;
        this.hasStarted = false;
    }


    // -------------------- interface for screen handler --------------------

    public void cycleMode() {
        if(this.currentMode ==  TrackTimerMode.GRAND_PRIX) this.currentMode = TrackTimerMode.TIME_ATTACK;
        else this.currentMode = TrackTimerMode.GRAND_PRIX;
    }

    public void setLapCount(int lapCount) {
        if(lapCount > MAX_LAPS) lapCount = MAX_LAPS;
        else if(lapCount < 1) lapCount = 1;
        this.lapCount = lapCount;
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
                case 0 -> currentMode.ordinal();
                case 1 -> lapCount;
                case 2 -> Boolean.compare(hasStarted, true);
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> {
                    if (value >= 0 && value < TrackTimerMode.values().length) {
                        currentMode = TrackTimerMode.values()[value];
                    }
                }
                case 1 -> lapCount = value;
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
                this
        );
    }
}
