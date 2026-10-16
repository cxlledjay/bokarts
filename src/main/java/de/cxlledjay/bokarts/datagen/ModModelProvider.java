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

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        // the kart
        // itemModelGenerator.register(ModItems.KART_ITEM, Models.GENERATED_THREE_LAYERS);

        // crafting
        itemModelGenerator.register(ModItems.KART_CRAFTING_WHEEL, Models.GENERATED);
        itemModelGenerator.register(ModItems.KART_CRAFTING_STEERING_WHEEL, Models.GENERATED);
        itemModelGenerator.register(ModItems.KART_CRAFTING_SEAT, Models.GENERATED);

        // accessories
        itemModelGenerator.register(ModItems.KART_KEY, Models.GENERATED);
    }
}
