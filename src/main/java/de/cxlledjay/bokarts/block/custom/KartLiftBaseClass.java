package de.cxlledjay.bokarts.block.custom;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.block.ModBlocks;
import de.cxlledjay.bokarts.block.entity.custom.KartLiftBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

// base class for the KartLiftBlock and KartLiftDummyBlock
public abstract class KartLiftBaseClass extends Block {


    // rotation
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    protected static final VoxelShape SHAPE_NORTH = Block.createCuboidShape(2, 0, 5, 14, 16, 13);
    protected static final VoxelShape SHAPE_SOUTH = Block.createCuboidShape(2, 0, 3, 14, 16, 11);
    protected static final VoxelShape SHAPE_EAST = Block.createCuboidShape(3, 0, 2, 11, 16, 14);
    protected static final VoxelShape SHAPE_WEST = Block.createCuboidShape(5, 0, 2, 13, 16, 14);


    public KartLiftBaseClass(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    // -------------------- basic rotation --------------------

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case SOUTH -> SHAPE_SOUTH;
            case EAST -> SHAPE_EAST;
            case WEST -> SHAPE_WEST;
            default -> SHAPE_NORTH;
        };
    }






    // -------------------- multiblock helpers --------------------
    public static BlockPos findCorePos(World world, BlockPos pos) {
        return world.getBlockState(pos).isOf(ModBlocks.KART_LIFT) ? pos
                        : world.getBlockState(pos.down()).isOf(ModBlocks.KART_LIFT) ? pos.down()
                        : world.getBlockState(pos.up()).isOf(ModBlocks.KART_LIFT) ? pos.up()
                        : null;
    }



    // -------------------- interaction --------------------

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {

        // find core
        BlockPos corePos = findCorePos(world, pos);
        if(corePos == null) return ActionResult.PASS;

        // check for control
        if(world.getBlockState(corePos).get(KartLiftCoreBlock.IS_CONTROL) == false) return ActionResult.PASS;


        // We only want to run this on the Server side
        if (!world.isClient) {

            // get block entity
            BlockEntity lift_be = world.getBlockEntity(corePos);

            // test if it exists and is the correct type
            if (lift_be instanceof KartLiftBlockEntity lift) {

                if(lift.animationState == KartLiftBlockEntity.LiftState.EMPTY) lift.animationState = KartLiftBlockEntity.LiftState.ANIMATION_UP;
                else if(lift.animationState == KartLiftBlockEntity.LiftState.ACTIVE) lift.animationState = KartLiftBlockEntity.LiftState.ANIMATION_DOWN;
                world.updateListeners(corePos, state, state, 3);

            } else {
                BoKarts.LOGGER.info("ERROR: No BlockEntity found at {}", corePos.toShortString());
                player.sendMessage(Text.literal("§cERROR: Block Entity is missing!"), false);
            }
        }

        return ActionResult.SUCCESS;
    }


}