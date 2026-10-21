package de.cxlledjay.bokarts.block.custom;

import com.mojang.serialization.MapCodec;
import de.cxlledjay.bokarts.entity.custom.kart.property.HornType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HornBlock extends Block {

    public static final MapCodec<HornBlock> CODEC = createCodec(HornBlock::new);
    public static final BooleanProperty POWERED = Properties.POWERED;
    public static final EnumProperty<HornType> HORN_SOUND = EnumProperty.of("horn_sound", HornType.class);

    @Override
    public MapCodec<HornBlock> getCodec() {
        return CODEC;
    }

    public HornBlock(Settings settings) {
        super(settings);
        this.setDefaultState(
                this.stateManager.getDefaultState()
                        .with(HORN_SOUND, HornType.HORN1)
                        .with(POWERED, false)
        );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HORN_SOUND, POWERED);
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        boolean bl = world.isReceivingRedstonePower(pos);
        if (bl != state.get(POWERED)) {
            if (bl) {
                this.playHorn(state, world, pos);
            }
            world.setBlockState(pos, state.with(POWERED, bl), Block.NOTIFY_ALL);
        }
    }

    private void playHorn(BlockState state, World world, BlockPos pos) {
        if (!world.isClient && world.getBlockState(pos.up()).isAir()) {
            HornType hornType = state.get(HORN_SOUND);
            if(hornType == null) return; //< safety

            world.playSound(
                    null,
                    pos.getX(), pos.getY(), pos.getZ(),
                    hornType.getSoundEvent(),
                    SoundCategory.RECORDS,
                    0.75f,
                    1.0f
            );
        }
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }

        state = state.cycle(HORN_SOUND);
        world.setBlockState(pos, state, Block.NOTIFY_ALL);
        this.playHorn(state, world, pos);
        player.incrementStat(Stats.TUNE_NOTEBLOCK);
        return ActionResult.CONSUME;
    }

    @Override
    protected void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        if (!world.isClient) {
            this.playHorn(state, world, pos);
            player.incrementStat(Stats.PLAY_NOTEBLOCK);
        }
    }
}
