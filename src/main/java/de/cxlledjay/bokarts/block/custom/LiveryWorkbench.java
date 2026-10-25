package de.cxlledjay.bokarts.block.custom;

import com.mojang.serialization.MapCodec;
import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.screen.custom.KartInventoryScreenHandler;
import de.cxlledjay.bokarts.screen.custom.LiveryWorkbenchScreenHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
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

public class LiveryWorkbench extends HorizontalFacingBlock {

    // codec
    public static final MapCodec<LiveryWorkbench> CODEC = LiveryWorkbench.createCodec(LiveryWorkbench::new);

    // facing
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    // hitbox
    protected static final VoxelShape SHAPE_NORTH_SOUTH = Block.createCuboidShape(-8, 0, 0, 24, 16, 16);
    protected static final VoxelShape SHAPE_EAST_WEST = Block.createCuboidShape(0, 0, -8, 16, 16, 24);


    public LiveryWorkbench(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case EAST, WEST -> SHAPE_EAST_WEST;
            default -> SHAPE_NORTH_SOUTH;
        };
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient()) {
            NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);

            if (screenHandlerFactory != null) {
                // open livery workbench screen
                player.openHandledScreen(screenHandlerFactory);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    protected NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedScreenHandlerFactory(
                (syncId, playerInventory, player1) ->
                        new LiveryWorkbenchScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(world, pos)), Text.translatable("block.bokarts.livery_workbench")
        );
    }
}
