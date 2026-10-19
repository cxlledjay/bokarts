package de.cxlledjay.bokarts.datagen;

import de.cxlledjay.bokarts.block.ModBlocks;
import de.cxlledjay.bokarts.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;

public class ModModelProvider extends FabricModelProvider {

    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerNorthDefaultHorizontalRotation(ModBlocks.LIVERY_WORKBENCH);
        blockStateModelGenerator.registerNorthDefaultHorizontalRotation(ModBlocks.PAINT_BUCKET);
        blockStateModelGenerator.registerNorthDefaultHorizontalRotation(ModBlocks.TROPHY);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {

        // chassis
        itemModelGenerator.register(ModItems.CHASSIS_BLUEPRINT, Models.GENERATED);
        itemModelGenerator.register(ModItems.STEERING_WHEEL, Models.GENERATED);
        itemModelGenerator.register(ModItems.SEAT, Models.GENERATED);
        // wheels
        itemModelGenerator.register(ModItems.WHEELS_STREET, Models.GENERATED);
        itemModelGenerator.register(ModItems.WHEELS_DRIFT, Models.GENERATED);
        itemModelGenerator.register(ModItems.WHEELS_OFFROAD, Models.GENERATED);
        // engines
        itemModelGenerator.register(ModItems.ENGINE_COPPER, Models.GENERATED);
        itemModelGenerator.register(ModItems.ENGINE_IRON, Models.GENERATED);
        itemModelGenerator.register(ModItems.ENGINE_GOLD, Models.GENERATED);
        itemModelGenerator.register(ModItems.ENGINE_DIAMOND, Models.GENERATED);
        itemModelGenerator.register(ModItems.ENGINE_NETHERITE, Models.GENERATED);
        // spoiler
        itemModelGenerator.register(ModItems.SPOILER_STREET, Models.GENERATED);
        itemModelGenerator.register(ModItems.SPOILER_STREET_CARBON, Models.GENERATED);
        itemModelGenerator.register(ModItems.SPOILER_RACE, Models.GENERATED);
        itemModelGenerator.register(ModItems.SPOILER_RACE_CARBON, Models.GENERATED);
        // aero
        itemModelGenerator.register(ModItems.AERO_RACE, Models.GENERATED);
        itemModelGenerator.register(ModItems.AERO_RACE_CARBON, Models.GENERATED);

        // accessories
        itemModelGenerator.register(ModItems.KEY_PAIRING_TOOL, Models.GENERATED);
    }
}
