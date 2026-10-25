package de.cxlledjay.bokarts.block.entity.custom;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.block.ModBlocks;
import de.cxlledjay.bokarts.block.custom.KartLiftBaseClass;
import de.cxlledjay.bokarts.block.custom.TrackTimer;
import de.cxlledjay.bokarts.block.entity.ModBlockEntities;
import de.cxlledjay.bokarts.entity.custom.kart.KartEntity;
import de.cxlledjay.bokarts.screen.custom.TrackTimerScreenHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TrackTimerBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, Inventory {



    // mode
    public enum TrackTimerMode {
        TIME_ATTACK, GRAND_PRIX
    }

    // ---------- constants ----------
    private static final int MAX_LAPS = 1000;
    private static final int BOUNDING_BOX_RADIUS = 8;
    private static final int MAX_TRACK_WIDTH = 32;




    // ---------- local dynamic values ----------

    // modify by only one player
    public PlayerEntity currentUser = null;

    // bounding box (used to search each tick for karts inside of it)
    private Box boundingBox = null;
    private Direction drivingDirection = Direction.NORTH;
    private final Map<UUID, Vec3d> previousKartPosition = new HashMap<>();




    // ---------- persisted data ----------

    // the inventory
    public DefaultedList<ItemStack> inventory = DefaultedList.ofSize(10, ItemStack.EMPTY);

    // settings by user
    private TrackTimerMode settingCurrentMode = TrackTimerMode.TIME_ATTACK;
    private int settingLapCount = 1;
    // where is the reflector => calculate bounding box from it each time it loads.
    private int trackWidth = 0;

    // --- race stats
    // ongoing race?
    private boolean hasStarted = false;
    private int currentTopPlace = 1;
    Map<UUID, TrackTimerPlayerStats> playerInfo = new HashMap<>();




    public TrackTimerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TRACK_TIMER_BE, pos, state);
    }



    // -------------------- racing business logic --------------------



    private void handleLineCrossing(KartEntity kart) {
        if(kart.getControllingPassenger() == null) return;
        if(kart.getControllingPassenger() instanceof PlayerEntity player) {
            player.sendMessage(Text.literal("passed the finish line"));
        }
    }










    public static void tick(World world, BlockPos pos, BlockState state, TrackTimerBlockEntity entity) {

        // do nothing when race is not yet started
        if(world == null || world.isClient() || !entity.hasStarted || entity.boundingBox == null) return;

        // search in the bounding box for karts
        List<KartEntity> karts = world.getEntitiesByClass(
                KartEntity.class,
                entity.boundingBox,
                kart -> kart.hasControllingPassenger() && kart.getControllingPassenger() instanceof PlayerEntity
        );

        // found kart(s)
        Set<UUID> currentlyNearbyUuids = new HashSet<>();

        for (KartEntity kart : karts) {
            if(kart.getControllingPassenger() == null) continue;
            if(kart.getControllingPassenger() instanceof PlayerEntity) {

                UUID uuid = kart.getUuid();
                currentlyNearbyUuids.add(uuid);

                // get positions
                Vec3d currentPos = kart.getPos();
                Vec3d previousPos = entity.previousKartPosition.get(uuid);

                // already seen this kart
                if (previousPos != null) {

                    // check crossing
                    if (hasCrossedFinishLine(entity, previousPos, currentPos)) {

                        // has completed one lap!
                        entity.handleLineCrossing(kart);
                    }
                }

                // update position
                entity.previousKartPosition.put(uuid, currentPos);
            }
        }
        // memory cleanup
        entity.previousKartPosition.keySet().retainAll(currentlyNearbyUuids);
    }




    // helpers
    private void calculateBoundingBox() {
        if(this.trackWidth == 0) return; //< loaded wrong data from nbt

        // calculate driving direction
        Direction facing = this.getCachedState().get(TrackTimer.FACING);
        this.drivingDirection = facing.rotateCounterclockwise(Direction.Axis.Y);

        // calculate corner points
        BlockPos corner1 = this.pos
                .offset(facing, 1)
                .offset(this.drivingDirection, BOUNDING_BOX_RADIUS);
        BlockPos corner2 = this.pos
                .offset(facing, this.trackWidth)
                .offset(this.drivingDirection.getOpposite(), BOUNDING_BOX_RADIUS);

        // create union of corner points
        this.boundingBox = new Box(corner1).union(new Box(corner2));
    }

    private static boolean hasCrossedFinishLine(TrackTimerBlockEntity entity, Vec3d previousPos, Vec3d currentPos) {
        double timerBlockEntityX = entity.pos.getX() + 0.5;
        double timerBlockEntityZ = entity.pos.getZ() + 0.5;

        return switch (entity.drivingDirection) {
            case NORTH -> // Driving North (Z is decreasing)
                    previousPos.z > timerBlockEntityZ && currentPos.z <= timerBlockEntityZ;
            case SOUTH -> // Driving South (Z is increasing)
                    previousPos.z < timerBlockEntityZ && currentPos.z >= timerBlockEntityZ;
            case WEST ->  // Driving West (X is decreasing)
                    previousPos.x > timerBlockEntityX && currentPos.x <= timerBlockEntityX;
            case EAST ->  // Driving East (X is increasing)
                    previousPos.x < timerBlockEntityX && currentPos.x >= timerBlockEntityX;
            default -> false;
        };
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
        // cannot start twice
        if(this.hasStarted) return;

        // search for a reflector block
        if (this.world != null && !this.world.isClient()) {

            BoKarts.LOGGER.info("server code running!");

            // get rotation of this block
            Direction facing = this.getCachedState().get(TrackTimer.FACING);

            // loop trough blocks in facing direction
            for(int width = 0; width < MAX_TRACK_WIDTH; width++) {
                // get block
                BlockPos inspectedPos = this.pos.offset(facing, width + 1);
                Block inspectedBlock =  this.world.getBlockState(inspectedPos).getBlock();
                BoKarts.LOGGER.info("checking width: {}", width);

                // check block
                if(inspectedBlock.equals(ModBlocks.TRACK_TIMER_REFLECTOR_BLOCK)) {
                    if(this.world.getBlockState(inspectedPos).get(TrackTimer.FACING) == facing.getOpposite()) {
                        // correct placement => start
                            this.trackWidth = width;
                            calculateBoundingBox();
                            this.hasStarted = true;
                            this.markDirty();
                    }
                    return;
                }
                else if(!inspectedBlock.equals(Blocks.AIR)) return; //< only air is valid
            }
        }
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

        // track width
        nbt.putInt("TrackWidth", this.trackWidth);
        calculateBoundingBox(); //< also recalculate bounding box!

        // race stats
        nbt.putBoolean("HasStarted", this.hasStarted);
        nbt.putInt("CurrentTopPlace", this.currentTopPlace);

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

        // track width
        this.trackWidth = nbt.getInt("TrackWidth");

        // race stats
        this.hasStarted = nbt.getBoolean("HasStarted");
        this.currentTopPlace = nbt.getInt("CurrentTopPlace");
        this.trackWidth = nbt.getInt("TrackWidth");

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
