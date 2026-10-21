package de.cxlledjay.bokarts.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.Direction;

public class TrackTimerReflector extends Block {

    public static final MapCodec<TrackTimerReflector> CODEC = TrackTimerReflector.createCodec(TrackTimerReflector::new);
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    @Override
    public MapCodec<TrackTimerReflector> getCodec() {
        return CODEC;
    }


    public TrackTimerReflector(Settings settings) {
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

}
