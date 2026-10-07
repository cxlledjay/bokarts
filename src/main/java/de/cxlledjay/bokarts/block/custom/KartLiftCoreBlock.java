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
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class KartLiftCoreBlock extends KartLiftBaseClass implements BlockEntityProvider {

    // codec
    public static final MapCodec<KartLiftCoreBlock> CODEC = KartLiftCoreBlock.createCodec(KartLiftCoreBlock::new);

    // core lift
    public static final BooleanProperty IS_CONTROL = BooleanProperty.of("is_control");
    public static final BooleanProperty ASSEMBLED = BooleanProperty.of("is_assembled");

    public KartLiftCoreBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState()
                .with(KartLiftBaseClass.FACING, Direction.NORTH)
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
        builder.add(IS_CONTROL, ASSEMBLED);
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
            BlockState dummyState = ModBlocks.KART_LIFT_DUMMY.getDefaultState().with(KartLiftBaseClass.FACING, facing);

            // get position of possible second lift
            BlockPos secondLiftPos = pos.offset(facing, 3).offset(Direction.UP, 1);
            BlockState secondLiftState = world.getBlockState(secondLiftPos);

            // check if there is a lift
            if(secondLiftState.getBlock() == ModBlocks.KART_LIFT) {
                // and it is facing us and unassembled (w/o control bugged somehow to true)
                if(secondLiftState.get(KartLiftBaseClass.FACING) == state.get(KartLiftBaseClass.FACING).getOpposite() &&
                    !secondLiftState.get(ASSEMBLED) &&
                    !secondLiftState.get(IS_CONTROL)) {

                    // update states
                    state = state.with(IS_CONTROL, true).with(ASSEMBLED, true);
                    secondLiftState = secondLiftState.with(ASSEMBLED, true);
                    world.setBlockState(secondLiftPos, secondLiftState, 35);
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

        // block is broken (=replaced by another block which is not the dummy from the placing)
        if (!state.isOf(newState.getBlock()) && !newState.isOf(ModBlocks.KART_LIFT_DUMMY)) {

            // --- delete this multiblock
            // delete bottom
            if (world.getBlockState(pos.down()).isOf(ModBlocks.KART_LIFT_DUMMY)) {
                world.setBlockState(pos.down(), Blocks.AIR.getDefaultState(), 35);
            }

            // delete top
            if (world.getBlockState(pos.up()).isOf(ModBlocks.KART_LIFT_DUMMY)) {
                world.setBlockState(pos.up(), Blocks.AIR.getDefaultState(), 35);
            }


            // --- delete assembly, if there is any
            if (state.contains(ASSEMBLED) && state.get(ASSEMBLED)) {

                // get position of second lift
                BlockPos secondLiftPos = pos.offset(state.get(FACING), 3);
                BlockState secondLiftState = world.getBlockState(secondLiftPos);

                // check if there is a lift there, which is facing us and is assembled
                if (secondLiftState.isOf(ModBlocks.KART_LIFT) &&
                    secondLiftState.get(KartLiftBaseClass.FACING) == state.get(KartLiftBaseClass.FACING).getOpposite() &&
                    secondLiftState.get(ASSEMBLED)) {

                    // delete this one too
                    world.breakBlock(secondLiftPos, true);
                }
            }


            // --- drop inventory (only control lift will drop!)
            if (state.contains(IS_CONTROL) && state.get(IS_CONTROL)) {
                BlockEntity be = world.getBlockEntity(pos);
                if (be instanceof KartLiftBlockEntity liftBE) {
                    // drop inventory logic => propagate to be class!
                    BoKarts.LOGGER.info("dropping inv at {}", be.getPos().toShortString());
                }
            }

            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }







}
