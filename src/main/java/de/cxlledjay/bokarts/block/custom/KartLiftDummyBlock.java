package de.cxlledjay.bokarts.block.custom;

import com.mojang.serialization.MapCodec;
import de.cxlledjay.bokarts.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class KartLiftDummyBlock extends KartLift {

    public static final MapCodec<KartLiftDummyBlock> CODEC = createCodec(KartLiftDummyBlock::new);

    public KartLiftDummyBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends Block> getCodec() {
        return CODEC;
    }









    // -------------------- multiblock breaking --------------------

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        // find core block
        BlockPos corePos = findCorePos(world, pos);

        if (corePos != null) {
            // break core block
            world.breakBlock(corePos, !player.isCreative(), player);
        }

        return super.onBreak(world, pos, state, player);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        // This catches explosions (Creepers, TNT) or water washing the dummy away
        if (!state.isOf(newState.getBlock())) {

            // find core pos
            BlockPos corePos = findCorePos(world, pos);

            if (corePos != null) {
                // Explosions should always drop the item
                world.breakBlock(corePos, true);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }
}
