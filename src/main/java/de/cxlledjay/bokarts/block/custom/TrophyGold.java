package de.cxlledjay.bokarts.block.custom;

import com.mojang.serialization.MapCodec;
import de.cxlledjay.bokarts.block.entity.custom.TrophyBlockEntity;
import de.cxlledjay.bokarts.item.ModItems;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TrophyGold extends TrophyBase {

    // codec
    public static final MapCodec<TrophyGold> CODEC = TrophyGold.createCodec(TrophyGold::new);


    public TrophyGold(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<TrophyGold> getCodec() {
        return CODEC;
    }


    public boolean isValidPlacement(int placement) {
        return placement == 1;
    }
}
