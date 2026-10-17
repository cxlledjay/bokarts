package de.cxlledjay.bokarts.block.entity.custom;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.block.entity.ModBlockEntities;
import de.cxlledjay.bokarts.component.ModDataComponentTypes;
import de.cxlledjay.bokarts.entity.ModEntities;
import de.cxlledjay.bokarts.entity.custom.kart.KartEntity;
import de.cxlledjay.bokarts.entity.custom.kart.property.*;
import de.cxlledjay.bokarts.item.ModItems;
import de.cxlledjay.bokarts.item.custom.KartItemv2;
import de.cxlledjay.bokarts.screen.custom.KartLiftScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

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



    public KartLiftBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.KART_LIFT_BE, pos, state);
    }



    // interface for interaction

    public boolean hasKartLoaded() {
        return this.liftState == LiftState.ACTIVE;
    }

    public void toggleLiftState() {
        switch (this.liftState) {
            case EMPTY:
                // check if there is a blueprint or kart item inside the inventory ==> use this one
                // check for entities ==> use this one

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
        this.markDirty();
        if(this.world != null) this.world.updateListeners(this.getPos(),this.getCachedState(), this.getCachedState(), 3);
    }

    public KartEntity getKartEntity() {
        // create new kart from nbt
        if (this.storedKart == null && this.pendingKartNbt != null && this.world != null) {
            this.storedKart = new KartEntity(ModEntities.KART_ENTITY_TYPE, this.world);
            this.storedKart.readNbt(this.pendingKartNbt);

            this.pendingKartNbt = null; // prevent duping
        }
        return this.storedKart;
    }

    public void setKartEntity(KartEntity kart) {
        this.storedKart = kart;
        this.syncKartData();
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
                    entity.liftState = LiftState.EMPTY;

                    if(!world.isClient) entity.releaseKart();

                    // AND sync
                    if (!world.isClient) {
                        entity.markDirty();
                        world.updateListeners(pos, state, state, 3);
                    }
                }
                break;
        }


    }

    private void releaseKart() {


        // dont forget to set as empty
    }

    // -------------------- screen --------------------

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.bokarts.kart_lift_block");
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new KartLiftScreenHandler(syncId, playerInventory, new SimpleInventory(KartLiftScreenHandler.LIFT_INVENTORY_SIZE), virtualKartInventory, this.pos);
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
                    storedKart = new KartEntity(ModEntities.KART_ENTITY_TYPE, world);
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
