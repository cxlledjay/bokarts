package de.cxlledjay.bokarts.block.custom;

import com.mojang.serialization.MapCodec;
import de.cxlledjay.bokarts.block.ModBlocks;
import de.cxlledjay.bokarts.block.entity.custom.KartLiftBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class KartLiftBlock extends KartLift implements BlockEntityProvider {

    public static final MapCodec<KartLiftBlock> CODEC = KartLiftBlock.createCodec(KartLiftBlock::new);

    public KartLiftBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends Block> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new KartLiftBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }








    // -------------------- multiblock placement --------------------

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos pos = ctx.getBlockPos();
        World world = ctx.getWorld();

        // Check if there is room for the two blocks above (ensures it doesn't build past world height, and blocks are replaceable)
        if (pos.getY() < (world.getTopY() - 2)
                && world.getBlockState(pos.up(1)).canReplace(ctx)
                && world.getBlockState(pos.up(2)).canReplace(ctx)) {

            // If there is room, return the state with the rotation facing the player!
            return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
        }

        // otherwise cancel
        return null;
    }



    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);

        if (!world.isClient) {
            // get rotation
            Direction facing = state.get(FACING);

            // create dummy blocks
            BlockState dummyState = ModBlocks.KART_LIFT_DUMMY.getDefaultState().with(KartLift.FACING, facing);

            // build 1x1x3 multiblock
            world.setBlockState(pos, dummyState, 3);
            world.setBlockState(pos.up(1), state, 3);
            world.setBlockState(pos.up(2), dummyState, 3);
        }
    }







    // -------------------- multiblock breaking --------------------
    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        // Check if the block is actually being destroyed (not just updated)
        if (!state.isOf(newState.getBlock())) {

            // 1. Quietly delete the Bottom Dummy (if it exists)
            if (world.getBlockState(pos.down()).isOf(ModBlocks.KART_LIFT_DUMMY)) {
                // The '35' flag quietly updates the world without triggering infinite loops
                world.setBlockState(pos.down(), Blocks.AIR.getDefaultState(), 35);
            }

            // 2. Quietly delete the Top Dummy (if it exists)
            if (world.getBlockState(pos.up()).isOf(ModBlocks.KART_LIFT_DUMMY)) {
                world.setBlockState(pos.up(), Blocks.AIR.getDefaultState(), 35);
            }

            // --- (Later: We will add the code here to scatter your BlockEntity's inventory on the ground!) ---

            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }



}
