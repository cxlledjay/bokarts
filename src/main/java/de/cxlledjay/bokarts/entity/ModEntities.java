package de.cxlledjay.bokarts.entity;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.entity.custom.KartEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {

    public static final EntityType<KartEntity> KART_ENTITY_TYPE = Registry.register(Registries.ENTITY_TYPE,
            BoKarts.id("kart"),
            EntityType.Builder.create(KartEntity::new, SpawnGroup.MISC)
                    .dimensions(1.25f, 0.75f).build());



    public static void registerModEntities() {
        BoKarts.LOGGER.info("Registering Mod Entities for " + BoKarts.MOD_ID);

    }

}
