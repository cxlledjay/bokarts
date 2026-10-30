package de.cxlledjay.bokarts.block.entity.custom;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.block.custom.KartLiftBaseClass;
import de.cxlledjay.bokarts.block.custom.KartLiftCoreBlock;
import de.cxlledjay.bokarts.block.entity.ModBlockEntities;
import de.cxlledjay.bokarts.component.ModDataComponentTypes;
import de.cxlledjay.bokarts.entity.ModEntities;
import de.cxlledjay.bokarts.entity.custom.kart.KartEntity;
import de.cxlledjay.bokarts.entity.custom.kart.property.*;
import de.cxlledjay.bokarts.item.ModItems;
import de.cxlledjay.bokarts.item.custom.KartItemv2;
import de.cxlledjay.bokarts.item.custom.modification.AeroItem;
import de.cxlledjay.bokarts.item.custom.modification.EngineItem;
import de.cxlledjay.bokarts.item.custom.modification.SpoilerItem;
import de.cxlledjay.bokarts.item.custom.modification.WheelsItem;
import de.cxlledjay.bokarts.screen.custom.KartLiftInventory;
import de.cxlledjay.bokarts.screen.custom.KartLiftScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class KartLiftBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<BlockPos> {


    // state machine
    public enum LiftState {
        EMPTY,
        ANIMATION_UP,
        ANIMATION_DOWN,
        ACTIVE
    }
    public LiftState liftState = LiftState.EMPTY;

    // animations (server sided tracking, rendering done on client)
    public float animationProgressPrev = 0.0f;
    public float animationProgress = 0.0f;
    public static final float ANIMATION_INCREASE = 0.025f;

    // single source of truth
    private KartEntity storedKart = null;
    private NbtCompound pendingKartNbt = null; //< holds data until the world is ready

    // modify by only one player
    public PlayerEntity currentUser = null;



    public KartLiftBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.KART_LIFT_BE, pos, state);
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


    // interface for interaction

    public boolean hasKartLoaded() {
        return this.liftState == LiftState.ACTIVE;
    }

    public void toggleLiftState() {
        switch (this.liftState) {
            case EMPTY:
                // get kart from world; if there was none, there is nothing to do
                if(this.getKartEntity() == null)
                    if(!this.stealKartFromWorld()) return;

                // start animation
                this.liftState = LiftState.ANIMATION_UP;
                this.animationProgressPrev = 0.0f;
                this.animationProgress = 0.0f;
                break;
            case ACTIVE:
                // start animation
                this.liftState = LiftState.ANIMATION_DOWN;
                this.animationProgressPrev = 1.0f;
                this.animationProgress = 1.0f;
                // letting entity go free is done by tick() function animation handling
                break;
            default:
                return;
        }

        // and sync
        syncKartData();
    }


    // returns true if steal was successful
    private boolean stealKartFromWorld () {

        // security
        if (this.world == null || this.world.isClient) {
            return false;
        }

        // get facing direction
        Direction facing = this.getCachedState().get(KartLiftCoreBlock.FACING);

        // build hitbox
        BlockPos pos1 = this.pos.offset(facing, 1);
        BlockPos pos2 = this.pos.offset(facing, 2);
        Box searchArea = new Box(pos1).union(new Box(pos2)).expand(0, 1.0, 0);

        // get a list of all karts inside the hitbox
        List<KartEntity> karts = this.world.getEntitiesByClass(
                KartEntity.class,
                searchArea,
                EntityPredicates.VALID_ENTITY
        );

        // check if there is something in the list
        if (!karts.isEmpty()) {
            // get first one if there are multiple
            KartEntity targetKart = karts.getFirst();

            // dont steal if there are passengers
            if(targetKart.hasPassengers()) return false;

            // save to nbt
            NbtCompound kartData = new NbtCompound();
            targetKart.saveSelfNbt(kartData);

            // init the entity for getKartEntity()
            this.pendingKartNbt = kartData;
            this.storedKart = null;

            // delete actual kart
            targetKart.discard();

            // and sync
            this.syncKartData();

            // return success
            return true;
        }

        // nothing to steal :(
        return false;
    }




    public void releaseKart() {
        // get kart
        KartEntity kartEntity = this.getKartEntity();

        // only spawn on the server, and only if a kart exists
        if (kartEntity != null && this.world != null && !this.world.isClient) {

            // calculate position and rotation
            Direction facing = this.getCachedState().get(KartLiftBaseClass.FACING);
            Vec3d center = this.getPos().toCenterPos();

            double spawnX = center.x + (facing.getOffsetX() * 1.5);
            double spawnY = center.y - 1.42;
            double spawnZ = center.z + (facing.getOffsetZ() * 1.5);

            float yaw = facing.asRotation() - 90.0F;

            if(kartEntity.isDrivable()) {
                // apply position and rotation
                kartEntity.refreshPositionAndAngles(spawnX, spawnY, spawnZ, yaw, 0.0F);
                kartEntity.setBodyYaw(yaw);
                kartEntity.setHeadYaw(yaw);
                // recalculate its physics
                kartEntity.recalculateKartPhysics();
                // and spawn it
                this.world.spawnEntity(kartEntity);
            } else {
                // invalid kart (not drivable) => spawn item
                ItemScatterer.spawn(this.world,spawnX,spawnY,spawnZ,kartEntity.getKartAsCustomStack());
            }

            // delete from lift
            this.storedKart = null;
            this.pendingKartNbt = null;
            this.syncKartData();
        }
    }

    public KartEntity getKartEntity() {
        // create new kart from nbt
        if (this.storedKart == null && this.pendingKartNbt != null && this.world != null) {
            this.storedKart = new KartEntity(this.world, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
            this.storedKart.readNbt(this.pendingKartNbt);

            this.pendingKartNbt = null; // prevent duping
        }
        return this.storedKart;
    }


    // -------------------- modification business logic --------------------


    public void setWheelsFromItemStack(ItemStack stack) {
        if(this.world == null || this.getKartEntity() == null) return;
        Item item = stack.getItem();

        WheelType wheelType = WheelType.NONE;
        if(item instanceof WheelsItem) {
            if(item == ModItems.WHEELS_STREET) wheelType = WheelType.STREET;
            if(item == ModItems.WHEELS_SEMI_SLICK) wheelType = WheelType.SEMI_SLICK;
            if(item == ModItems.WHEELS_SOFT) wheelType = WheelType.SOFT;
            if(item == ModItems.WHEELS_HARD) wheelType = WheelType.HARD;
            else if(item == ModItems.WHEELS_DRIFT) wheelType = WheelType.DRIFT;
            else if(item == ModItems.WHEELS_DRIFT_PLUS) wheelType = WheelType.DRIFT_PLUS;
            else if(item == ModItems.WHEELS_OFFROAD) wheelType = WheelType.OFFROAD;
            else if(item == ModItems.WHEELS_SPIKES) wheelType = WheelType.SPIKES;
        }
        storedKart.setWheelType(wheelType);
    }

    public void setEngineFromItemStack(ItemStack stack) {
        if(this.world == null || this.getKartEntity() == null) return;
        Item item = stack.getItem();

        EngineType engineType = EngineType.ENGINE_NONE;
        if(item instanceof EngineItem) {
            if(item == ModItems.ENGINE_COPPER) engineType = EngineType.ENGINE_COPPER;
            else if(item == ModItems.ENGINE_IRON) engineType = EngineType.ENGINE_IRON;
            else if(item == ModItems.ENGINE_GOLD) engineType = EngineType.ENGINE_GOLD;
            else if(item == ModItems.ENGINE_DIAMOND) engineType = EngineType.ENGINE_DIAMOND;
            else if(item == ModItems.ENGINE_NETHERITE) engineType = EngineType.ENGINE_NETHERITE;
        }
        storedKart.setEngineType(engineType);
    }

    public void setSpoilerFromItemStack(ItemStack stack) {
        if(this.world == null || this.getKartEntity() == null) return;
        Item item = stack.getItem();

        SpoilerType spoilerType = SpoilerType.NONE;
        if(item instanceof SpoilerItem) {
            if(item == ModItems.SPOILER_STREET) spoilerType = SpoilerType.STREET;
            else if(item == ModItems.SPOILER_STREET_CARBON) spoilerType = SpoilerType.STREET_CARBON;
            else if(item == ModItems.SPOILER_RACE) spoilerType = SpoilerType.RACE;
            else if(item == ModItems.SPOILER_RACE_CARBON) spoilerType = SpoilerType.RACE_CARBON;
            else if(item == ModItems.SPOILER_RACE_PRO_MAX) spoilerType = SpoilerType.RACE_PLUS;
            else if(item == ModItems.SPOILER_RACE_PRO_MAX_CARBON) spoilerType = SpoilerType.RACE_PLUS_CARBON;
        }
        storedKart.setSpoilerType(spoilerType);
    }

    public void setAeroFromItemStack(ItemStack stack) {
        if(this.world == null || this.getKartEntity() == null) return;
        Item item = stack.getItem();

        AeroType aeroType = AeroType.NONE;
        if(item instanceof AeroItem) {
            if(item == ModItems.AERO_RACE)  aeroType = AeroType.RACE;
            else if(item == ModItems.AERO_RACE_CARBON)  aeroType = AeroType.RACE_CARBON;
        }
        storedKart.setAeroType(aeroType);
    }

    public void setLiveryFromItemStack(ItemStack stack) {
        if(this.world == null || this.getKartEntity() == null) return;
        Item item = stack.getItem();

        BodyType bodyType = BodyType.NONE;
        int bodyColor = 0xFFFFFFFF;
        int wheelColor = 0xFFFFFFFF;
        if(item == ModItems.LIVERY_TEMPLATE) {
            bodyType = stack.getOrDefault(ModDataComponentTypes.KART_ITEM_BODY_TYPE, BodyType.NONE);
            bodyColor = stack.getOrDefault(ModDataComponentTypes.KART_ITEM_BODY_COLOR, 0xFFFFFFFF);
            wheelColor = stack.getOrDefault(ModDataComponentTypes.KART_ITEM_WHEEL_COLOR, 0xFFFFFFFF);
        }
        storedKart.setBodyType(bodyType);
        storedKart.setBodyColor(bodyColor);
        storedKart.setWheelColor(wheelColor);
    }

    public void setRaceNumber(int num) {
        if(this.world == null || this.getKartEntity() == null) return;

        int raceNumber = 0;
        if(KartEntity.isValidRaceNumber(num)) raceNumber = num;

        storedKart.setRaceNumber(raceNumber);
        this.syncKartData();
    }


    public Inventory getInventoryFromKart() {

        // security
        if(this.world == null) return null;

        // no kart stored => empty inventory
        if(this.getKartEntity() == null) return new KartLiftInventory(KartLiftScreenHandler.LIFT_INVENTORY_SIZE, this);

        // get all item stacks from kart

        // wheels
        ItemStack wheelItem = switch(this.getKartEntity().getWheelType()) {
            case STREET -> new ItemStack(ModItems.WHEELS_STREET);
            case SEMI_SLICK -> new ItemStack(ModItems.WHEELS_SEMI_SLICK);
            case SOFT -> new ItemStack(ModItems.WHEELS_SOFT);
            case HARD -> new ItemStack(ModItems.WHEELS_HARD);
            case DRIFT -> new ItemStack(ModItems.WHEELS_DRIFT);
            case DRIFT_PLUS -> new ItemStack(ModItems.WHEELS_DRIFT_PLUS);
            case OFFROAD -> new ItemStack(ModItems.WHEELS_OFFROAD);
            case SPIKES -> new ItemStack(ModItems.WHEELS_SPIKES);
            default -> ItemStack.EMPTY;
        };
        // engines
        ItemStack engineItem = switch(this.getKartEntity().getEngineType()) {
            case ENGINE_COPPER -> new ItemStack(ModItems.ENGINE_COPPER);
            case ENGINE_IRON -> new ItemStack(ModItems.ENGINE_IRON);
            case ENGINE_GOLD -> new ItemStack(ModItems.ENGINE_GOLD);
            case ENGINE_DIAMOND -> new ItemStack(ModItems.ENGINE_DIAMOND);
            case ENGINE_NETHERITE ->  new ItemStack(ModItems.ENGINE_NETHERITE);
            default -> ItemStack.EMPTY;
        };
        // spoilers
        ItemStack spoilerItem = switch(this.getKartEntity().getSpoilerType()) {
            case STREET -> new ItemStack(ModItems.SPOILER_STREET);
            case STREET_CARBON ->  new ItemStack(ModItems.SPOILER_STREET_CARBON);
            case RACE  -> new ItemStack(ModItems.SPOILER_RACE);
            case RACE_CARBON -> new ItemStack(ModItems.SPOILER_RACE_CARBON);
            case RACE_PLUS -> new ItemStack(ModItems.SPOILER_RACE_PRO_MAX);
            case RACE_PLUS_CARBON -> new ItemStack(ModItems.SPOILER_RACE_PRO_MAX_CARBON);
            default -> ItemStack.EMPTY;
        };
        // aero
        ItemStack aeroItem = switch(this.getKartEntity().getAeroType()) {
            case RACE  -> new ItemStack(ModItems.AERO_RACE);
            case RACE_CARBON -> new ItemStack(ModItems.AERO_RACE_CARBON);
            default -> ItemStack.EMPTY;
        };

        // livery
        ItemStack liveryItem = ItemStack.EMPTY;
        if(storedKart.getBodyType() != BodyType.NONE) liveryItem = new ItemStack(ModItems.LIVERY_TEMPLATE);

        liveryItem.set(ModDataComponentTypes.KART_ITEM_BODY_TYPE, this.getKartEntity().getBodyType());
        liveryItem.set(ModDataComponentTypes.KART_ITEM_BODY_COLOR, this.getKartEntity().getBodyColor());
        liveryItem.set(ModDataComponentTypes.KART_ITEM_WHEEL_COLOR, this.getKartEntity().getWheelColor());


        // apply to inventory
        KartLiftInventory temp = new KartLiftInventory(KartLiftScreenHandler.LIFT_INVENTORY_SIZE, this);
        temp.setStack(0,wheelItem);
        temp.setStack(1,engineItem);
        temp.setStack(2,spoilerItem);
        temp.setStack(3,aeroItem);
        temp.setStack(4,liveryItem);

        return temp;
    }



    // -------------------- sync --------------------

    public void syncKartData() {
        // save to disk
        this.markDirty();

        // update listeners
        if (this.world != null && !this.world.isClient) {
            this.world.updateListeners(this.pos, this.getCachedState(), this.getCachedState(), Block.NOTIFY_LISTENERS);
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        // state
        nbt.putInt("lift_state", this.liftState.ordinal());

        // kart
        if (this.storedKart != null) {
            NbtCompound kartNbt = new NbtCompound();
            this.storedKart.saveSelfNbt(kartNbt);
            nbt.put("stored_kart", kartNbt);
        } else if (this.pendingKartNbt != null) {
            nbt.put("stored_kart", this.pendingKartNbt);
        }
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        if (nbt.contains("lift_state")) {
            this.liftState = LiftState.values()[nbt.getInt("lift_state")];

            // load animation progress upon loading block entity
            if (this.liftState == LiftState.ACTIVE) {
                this.animationProgress = 1.0f;
                this.animationProgressPrev = 1.0f;
            } else if (this.liftState == LiftState.EMPTY) {
                this.animationProgress = 0.0f;
                this.animationProgressPrev = 0.0f;
            }

            // load kart
            if (nbt.contains("stored_kart")) {
                this.pendingKartNbt = nbt.getCompound("stored_kart");
                this.storedKart = null; // force creation once world is loaded
            } else {
                this.pendingKartNbt = null;
                this.storedKart = null;
            }
        }
    }

    @Nullable
    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }

    // -------------------- animations --------------------

    public static void tick(World world, BlockPos pos, BlockState state, KartLiftBlockEntity entity) {
        // tick animations
        switch (entity.liftState) {
            case LiftState.ANIMATION_UP:
                entity.animationProgressPrev = entity.animationProgress;
                entity.animationProgress = Math.min(1.0f, entity.animationProgress + ANIMATION_INCREASE);
                if(entity.animationProgress >= 1.0f) {
                    entity.animationProgress = 1.0f;
                    entity.liftState = LiftState.ACTIVE; // stop animation => lift can be accessed

                    // and sync
                    if (!world.isClient) entity.syncKartData();
                }
                break;
            case LiftState.ANIMATION_DOWN:
                entity.animationProgressPrev = entity.animationProgress;
                entity.animationProgress = Math.max(0.0f, entity.animationProgress - ANIMATION_INCREASE);
                if(entity.animationProgress <= 0.0f){
                    entity.animationProgress = 0.0f;
                    entity.releaseKart();
                    entity.liftState = LiftState.EMPTY;
                    entity.syncKartData();
                }
                break;
        }


    }

    // -------------------- screen --------------------

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.bokarts.kart_lift_block");
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new KartLiftScreenHandler(syncId, playerInventory, getInventoryFromKart(), virtualKartInventory, ScreenHandlerContext.create(this.world, this.pos) , this.pos);
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity player) {
        return this.pos;
    }



    // -------------------- item <-> entity --------------------

    public final Inventory virtualKartInventory = new Inventory() {
        @Override
        public int size() { return 1; }

        @Override
        public boolean isEmpty() { return storedKart == null; }

        @Override
        public ItemStack getStack(int slot) {
            if (getKartEntity() == null) return ItemStack.EMPTY;

            // create item from entity
            return getKartEntity().getKartAsCustomStack();
        }

        @Override
        public void setStack(int slot, ItemStack stack) {
            if (stack.isEmpty()) {
                // player took kart => no longer store entity; should not happen
                storedKart = null;
                pendingKartNbt = null;
            } else if(stack.getItem() instanceof KartItemv2 || stack.getItem() == ModItems.CHASSIS_BLUEPRINT) {
                // player inserted item => parse to entity
                if (world != null) {
                    storedKart = new KartEntity(world, getPos().getX(), getPos().getY(), getPos().getZ());
                    pendingKartNbt = null;

                    // apply item data to entity
                    KartItemv2.applyDataFromItemStack(storedKart, stack);
                }
            } else {
                // entered something invalid; should not happen
                this.clear();
            }
            // and sync
            KartLiftBlockEntity.this.syncKartData();
        }

        @Override
        public ItemStack removeStack(int slot, int amount) {
            ItemStack stack = getStack(slot);
            setStack(slot, ItemStack.EMPTY);
            return stack;
        }

        @Override
        public ItemStack removeStack(int slot) {
            return removeStack(slot, 1);
        }

        @Override
        public void markDirty() {
            KartLiftBlockEntity.this.markDirty();
        }

        @Override
        public boolean canPlayerUse(PlayerEntity player) { return true; }

        @Override
        public void clear() { setStack(0, ItemStack.EMPTY); }
    };

}
