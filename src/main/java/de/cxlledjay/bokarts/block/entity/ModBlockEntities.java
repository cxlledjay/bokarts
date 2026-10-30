package de.cxlledjay.bokarts.block.entity;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.block.ModBlocks;
import de.cxlledjay.bokarts.block.entity.custom.KartLiftBlockEntity;
import de.cxlledjay.bokarts.block.entity.custom.TrackTimerBlockEntity;
import de.cxlledjay.bokarts.block.entity.custom.TrophyBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModBlockEntities {

    // kart lift
    public static final BlockEntityType<KartLiftBlockEntity> KART_LIFT_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, BoKarts.id("kart_lift"),
            BlockEntityType.Builder.create(KartLiftBlockEntity::new, ModBlocks.KART_LIFT).build());

    // track timer
    public static final BlockEntityType<TrackTimerBlockEntity> TRACK_TIMER_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, BoKarts.id("track_timer"),
                    BlockEntityType.Builder.create(TrackTimerBlockEntity::new, ModBlocks.TRACK_TIMER_BLOCK).build());

    // trophy
    public static final BlockEntityType<TrophyBlockEntity> TROPHY_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, BoKarts.id("trophy"),
                    BlockEntityType.Builder.create(TrophyBlockEntity::new, ModBlocks.TROPHY_GOLD, ModBlocks.TROPHY_SILVER, ModBlocks.TROPHY_BRONZE).build());



    public static void registerBlockEntities() {
        BoKarts.LOGGER.info("Registering block entities for " + BoKarts.MOD_ID);
    }

}
