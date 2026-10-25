package de.cxlledjay.bokarts.block.custom;

import com.mojang.serialization.MapCodec;
import de.cxlledjay.bokarts.block.entity.ModBlockEntities;
import de.cxlledjay.bokarts.block.entity.custom.KartLiftBlockEntity;
import de.cxlledjay.bokarts.block.entity.custom.TrackTimerBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TrackTimer extends BlockWithEntity {

    public static final MapCodec<TrackTimer> CODEC = TrackTimer.createCodec(TrackTimer::new);
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    @Override
    public MapCodec<TrackTimer> getCodec() {
        return CODEC;
    }


    public TrackTimer(Settings settings) {
        super(settings);
        this.getDefaultState().with(FACING, Direction.NORTH);
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
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TrackTimerBlockEntity(pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }


    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {

        if (!world.isClient()) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof TrackTimerBlockEntity trackTimer) {
                if (trackTimer.tryOpen(player)) {
                    player.openHandledScreen(trackTimer);
                }
            }
        }

        return ActionResult.SUCCESS;
    }


    // -------------------- block entity --------------------


    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (type == ModBlockEntities.TRACK_TIMER_BE) {
            return (world1, pos, state1, blockEntity) -> {
                TrackTimerBlockEntity.tick(world1, pos, state1, (TrackTimerBlockEntity) blockEntity);
            };
        }
        return null;
    }
}
