package de.cxlledjay.bokarts.block.custom;

import com.mojang.serialization.MapCodec;
import de.cxlledjay.bokarts.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class KartLiftDummyBlock extends KartLiftBaseClass {

    public static final MapCodec<KartLiftDummyBlock> CODEC = createCodec(KartLiftDummyBlock::new);

    public KartLiftDummyBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends Block> getCodec() {
        return CODEC;
    }

// -------------------- vanilla block handling --------------------

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        return new ItemStack(ModBlocks.KART_LIFT);
    }


    // -------------------- multiblock breaking --------------------

    private void breakCoreBlock(World world, BlockPos pos, boolean drop, @Nullable PlayerEntity player) {
        BlockPos corePos = findCorePos(world, pos);

        if (corePos != null) {
            // break core block
            if(player == null) world.breakBlock(corePos, drop);
            else world.breakBlock(corePos, drop, player);
        }
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {

        // propagate breaking to core block
        breakCoreBlock(world, pos, !player.isCreative(), player);

        return super.onBreak(world, pos, state, player);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {

        // if block is broken (=replaced by another block)
        if (!state.isOf(newState.getBlock())) {

            // propagate breaking to core block
            breakCoreBlock(world, pos, true, null);

            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }






}
