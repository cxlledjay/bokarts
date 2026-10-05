package de.cxlledjay.bokarts.block.entity;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.block.ModBlocks;
import de.cxlledjay.bokarts.block.entity.custom.KartLiftBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModBlockEntities {


    // 3. Register the Block Entity
    public static final BlockEntityType<KartLiftBlockEntity> KART_LIFT_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, BoKarts.id("kart_lift"),
            BlockEntityType.Builder.create(KartLiftBlockEntity::new, ModBlocks.KART_LIFT).build());

    public static void registerBlockEntities() {
        BoKarts.LOGGER.info("Registering block entities for " + BoKarts.MOD_ID);
    }

}
