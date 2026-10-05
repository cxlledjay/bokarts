package de.cxlledjay.bokarts.block.entity.custom;

import de.cxlledjay.bokarts.block.entity.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class KartLiftBlockEntity extends BlockEntity {


    public KartLiftBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.KART_LIFT_BE, pos, state);
    }

}
