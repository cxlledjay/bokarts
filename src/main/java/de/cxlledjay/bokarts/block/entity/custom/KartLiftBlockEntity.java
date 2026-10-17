package de.cxlledjay.bokarts.block.entity.custom;

import de.cxlledjay.bokarts.block.entity.ModBlockEntities;
import de.cxlledjay.bokarts.screen.custom.KartLiftScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
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

    // animations (server sided tracking, rendering done on client)
    public LiftState liftState = LiftState.EMPTY;
    public float animationProgressPrev = 0.0f;
    public float animationProgress = 0.0f;
    public static final float ANIMATION_INCREASE = 0.025f;

    public KartLiftBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.KART_LIFT_BE, pos, state);
    }



    // -------------------- sync --------------------
    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        nbt.putInt("animationState", this.liftState.ordinal());
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        if (nbt.contains("animationState")) {
            this.liftState = LiftState.values()[nbt.getInt("animationState")];

            // load animation progress upon loading block entity
            if (this.liftState == LiftState.ACTIVE) {
                this.animationProgress = 1.0f;
                this.animationProgressPrev = 1.0f;
            } else if (this.liftState == LiftState.EMPTY) {
                this.animationProgress = 0.0f;
                this.animationProgressPrev = 0.0f;
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

                    // AND sync
                    //TODO: create wrapper for state changes with automatic sync
                    if (!world.isClient) {
                        entity.markDirty();
                        world.updateListeners(pos, state, state, 3);
                    }
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

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.bokarts.kart_lift_block");
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new KartLiftScreenHandler(syncId, playerInventory, this.pos);
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity player) {
        return this.pos;
    }

    public static enum LiftState {
        EMPTY,
        ANIMATION_UP,
        ANIMATION_DOWN,
        ACTIVE
    }

}
