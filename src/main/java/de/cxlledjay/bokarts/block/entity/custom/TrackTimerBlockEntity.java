package de.cxlledjay.bokarts.block.entity.custom;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.block.entity.ModBlockEntities;
import de.cxlledjay.bokarts.screen.custom.TrackTimerScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TrackTimerBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, Inventory {



    // mode
    public enum TrackTimerMode {
        TIME_ATTACK, GRAND_PRIX
    }

    // ---------- constants ----------
    private static final int MAX_LAPS = 1000;
    private static final int BOUNDING_BOX_RADIUS = 6;




    // ---------- local dynamic values ----------

    // modify by only one player
    public PlayerEntity currentUser = null;

    // bounding box (used to search each tick for karts inside of it)
    private Box boundingBox;




    // ---------- persisted data ----------

    // the inventory
    public DefaultedList<ItemStack> inventory = DefaultedList.ofSize(10, ItemStack.EMPTY);

    // settings by user
    private TrackTimerMode settingCurrentMode = TrackTimerMode.TIME_ATTACK;
    private int settingLapCount = 1;
    // where is the reflector => calculate bounding box from it each time it loads.
    private BlockPos reflectorPos = null;

    // --- race stats
    // ongoing race?
    private boolean hasStarted = false;
    Map<UUID, TrackTimerPlayerStats> playerInfo = new HashMap<>();




    public TrackTimerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TRACK_TIMER_BE, pos, state);
    }



    // -------------------- racing business logic --------------------













    public static void tick(World world, BlockPos pos, BlockState state, TrackTimerBlockEntity entity) {

        // do nothing when race is not yet started
        if(!entity.hasStarted) return;

        // search in the bounding box for karts


    }


    // helpers
    private void calculateBoundingBox() {

    }






    // -------------------- interface for screen handler --------------------

    public void cycleMode() {
        // not allowed during race
        if(hasStarted) return;

        // cycle mode
        if(this.settingCurrentMode ==  TrackTimerMode.GRAND_PRIX) this.settingCurrentMode = TrackTimerMode.TIME_ATTACK;
        else this.settingCurrentMode = TrackTimerMode.GRAND_PRIX;
        this.markDirty();
    }

    public void setLapCount(int settingLapCount) {
        // not allowed during race
        if(hasStarted) return;

        // set laps
        if(settingLapCount > MAX_LAPS) settingLapCount = MAX_LAPS;
        else if(settingLapCount < 1) settingLapCount = 1;
        this.settingLapCount = settingLapCount;
        this.markDirty();
    }

    public void start() {
        if(this.hasStarted) return;
        // start logic
        BoKarts.LOGGER.info("Starting Track Timer BlockEntity");
        this.hasStarted = true;
        this.markDirty();
    }

    public void stopOrReset() {
        if(!this.hasStarted) return;
        // stop / reset logic
        BoKarts.LOGGER.info("Stopping or Resetting Track Timer BlockEntity");
        this.hasStarted = false;
        this.markDirty();
    }








    // -------------------- persist data --------------------


    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);

        // settings
        if (this.settingCurrentMode != null) {
            nbt.putInt("SettingCurrentMode", this.settingCurrentMode.ordinal());
        }
        nbt.putInt("SettingLapCount", this.settingLapCount);

        // reflector pos
        if (this.reflectorPos != null) {
            nbt.putInt("ReflectorX", this.reflectorPos.getX());
            nbt.putInt("ReflectorY", this.reflectorPos.getY());
            nbt.putInt("ReflectorZ", this.reflectorPos.getZ());
            nbt.putBoolean("HasReflector", true);
        } else {
            nbt.putBoolean("HasReflector", false);
        }

        // race stats
        nbt.putBoolean("HasStarted", this.hasStarted);

        NbtList playerListTag = new NbtList();
        for (Map.Entry<UUID, TrackTimerPlayerStats> entry : this.playerInfo.entrySet()) {
            NbtCompound playerTag = new NbtCompound();
            playerTag.putUuid("PlayerUUID", entry.getKey());
            playerTag.put("Stats", entry.getValue().toNbt());
            playerListTag.add(playerTag);
        }
        nbt.put("PlayerInfo", playerListTag);

        // inventory
        Inventories.writeNbt(nbt, this.inventory, registryLookup);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);

        // settings
        if (nbt.contains("SettingCurrentMode")) {
            int ordinal = nbt.getInt("SettingCurrentMode");
            if(ordinal >= 0 && ordinal < TrackTimerMode.values().length) this.settingCurrentMode = TrackTimerMode.values()[ordinal];
        }
        this.settingLapCount = nbt.getInt("SettingLapCount");

        // reflector
        if (nbt.getBoolean("HasReflector")) {
            this.reflectorPos = new BlockPos(
                    nbt.getInt("ReflectorX"),
                    nbt.getInt("ReflectorY"),
                    nbt.getInt("ReflectorZ")
            );
        } else {
            this.reflectorPos = null;
        }

        // race stats
        this.hasStarted = nbt.getBoolean("HasStarted");

        this.playerInfo.clear();
        if (nbt.contains("PlayerInfo", NbtElement.LIST_TYPE)) {
            NbtList playerListTag = nbt.getList("PlayerInfo", NbtElement.COMPOUND_TYPE);
            for (int i = 0; i < playerListTag.size(); i++) {
                NbtCompound playerTag = playerListTag.getCompound(i);

                UUID uuid = playerTag.getUuid("PlayerUUID");
                TrackTimerPlayerStats stats = TrackTimerPlayerStats.fromNbt(playerTag.getCompound("Stats"));

                this.playerInfo.put(uuid, stats);
            }
        }

        // inventory
        this.inventory.clear();
        Inventories.readNbt(nbt, this.inventory, registryLookup);
    }





    // -------------------- inventory interface --------------------

    @Override
    public int size() {
        return this.inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.inventory) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return this.inventory.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack result = Inventories.splitStack(this.inventory, slot, amount);
        if (!result.isEmpty()) {
            this.markDirty();
        }
        return result;
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack result = Inventories.removeStack(this.inventory, slot);
        this.markDirty();
        return result;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.inventory.set(slot, stack);
        if (stack.getCount() > stack.getMaxCount()) {
            stack.setCount(stack.getMaxCount());
        }
        this.markDirty();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        this.inventory.clear();
        this.markDirty();
    }








    // -------------------- stuff for screen handler --------------------


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
                this
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
