package de.cxlledjay.bokarts.datagen;

import de.cxlledjay.bokarts.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends FabricBlockLootTableProvider {

    public ModLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        addDrop(ModBlocks.KART_LIFT);
        addDrop(ModBlocks.LIVERY_WORKBENCH);
        addDrop(ModBlocks.PAINT_BUCKET);
        addDrop(ModBlocks.TROPHY);
        addDrop(ModBlocks.HORN_BLOCK);
        addDrop(ModBlocks.TRACK_TIMER_BLOCK);
        addDrop(ModBlocks.TRACK_TIMER_REFLECTOR_BLOCK);
    }
}
