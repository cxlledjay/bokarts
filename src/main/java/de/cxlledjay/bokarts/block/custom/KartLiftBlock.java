package de.cxlledjay.bokarts.block.custom;

import com.mojang.serialization.MapCodec;
import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.block.ModBlocks;
import de.cxlledjay.bokarts.block.entity.custom.KartLiftBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class KartLiftBlock extends KartLift implements BlockEntityProvider {

    // codec
    public static final MapCodec<KartLiftBlock> CODEC = KartLiftBlock.createCodec(KartLiftBlock::new);

    // core lift
    public static final BooleanProperty IS_CONTROL = BooleanProperty.of("is_control");
    public static final BooleanProperty ASSEMBLED = BooleanProperty.of("is_assembled");

    public KartLiftBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState()
                .with(KartLift.FACING, Direction.NORTH)
                .with(IS_CONTROL, false)
                .with(ASSEMBLED, false));
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

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(IS_CONTROL);
        builder.add(ASSEMBLED);
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

            // get position of possible second lift
            BlockPos secondLiftPos = pos.offset(facing, 3).offset(Direction.UP, 1);
            BlockState secondLiftState = world.getBlockState(secondLiftPos);

            // check if there is a lift
            if(secondLiftState.getBlock() == ModBlocks.KART_LIFT) {
                // and it is facing us and unassembled
                if(secondLiftState.get(KartLift.FACING) == state.get(KartLift.FACING).getOpposite()
                    && secondLiftState.get(ASSEMBLED) == false) {

                    // we got a match!
                    state = state.with(IS_CONTROL, true).with(ASSEMBLED, true);
                    secondLiftState = secondLiftState.with(ASSEMBLED, true);

                    // update second lift one as well
                    world.setBlockState(secondLiftPos, secondLiftState, 3);
                }
            }



            // build the 1x1x3 multiblock
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
                world.setBlockState(pos.down(), Blocks.AIR.getDefaultState(), 3);
            }

            // 2. Quietly delete the Top Dummy (if it exists)
            if (world.getBlockState(pos.up()).isOf(ModBlocks.KART_LIFT_DUMMY)) {
                world.setBlockState(pos.up(), Blocks.AIR.getDefaultState(), 3);
            }



            // if assembled -> disassemble
            if (state.contains(ASSEMBLED) && state.get(ASSEMBLED)) {

                // get position of second lift
                BlockPos secondLiftPos = pos.offset(state.get(FACING), 3);
                BlockState secondLiftState = world.getBlockState(secondLiftPos);

                // check if there is a lift there
                if (secondLiftState.isOf(ModBlocks.KART_LIFT)) {
                    // and it is facing us and assembled
                    if (secondLiftState.get(KartLift.FACING) == state.get(KartLift.FACING).getOpposite()
                            && secondLiftState.get(ASSEMBLED) == true) {

                        // we got a match!

                        if (state.get(IS_CONTROL) == true) {
                            // DROP ITEM LOGIC
                        } else if (secondLiftState.get(IS_CONTROL) == true) {
                            // DROP ITEMS FROM HERE
                        }

                        // reset second lift
                        secondLiftState = secondLiftState.with(ASSEMBLED, false).with(IS_CONTROL, false);

                        // and update second lift
                        world.setBlockState(secondLiftPos, secondLiftState, 3);
                    }
                }
            }

            super.onStateReplaced(state, world, pos, newState, moved);
        }


    }



}
