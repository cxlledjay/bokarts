package de.cxlledjay.bokarts.datagen;

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
        itemModelGenerator.register(ModItems.KART_CRAFTING_WHEEL, Models.GENERATED);
        itemModelGenerator.register(ModItems.KART_CRAFTING_STEERING_WHEEL, Models.GENERATED);
        itemModelGenerator.register(ModItems.KART_CRAFTING_SEAT, Models.GENERATED);
        itemModelGenerator.register(ModItems.KART_KEY, Models.GENERATED);

        // Vanilla dye colors
        itemModelGenerator.register(ModItems.KART_WHITE, Models.GENERATED);
        itemModelGenerator.register(ModItems.KART_ORANGE, Models.GENERATED);
        itemModelGenerator.register(ModItems.KART_MAGENTA, Models.GENERATED);
        itemModelGenerator.register(ModItems.KART_LIGHT_BLUE, Models.GENERATED);
        itemModelGenerator.register(ModItems.KART_YELLOW, Models.GENERATED);
        itemModelGenerator.register(ModItems.KART_LIME, Models.GENERATED);
        itemModelGenerator.register(ModItems.KART_PINK, Models.GENERATED);
        itemModelGenerator.register(ModItems.KART_GRAY, Models.GENERATED);
        itemModelGenerator.register(ModItems.KART_LIGHT_GRAY, Models.GENERATED);
        itemModelGenerator.register(ModItems.KART_CYAN, Models.GENERATED);
        itemModelGenerator.register(ModItems.KART_PURPLE, Models.GENERATED);
        itemModelGenerator.register(ModItems.KART_BLUE, Models.GENERATED);
        itemModelGenerator.register(ModItems.KART_BROWN, Models.GENERATED);
        itemModelGenerator.register(ModItems.KART_GREEN, Models.GENERATED);
        itemModelGenerator.register(ModItems.KART_RED, Models.GENERATED);
        itemModelGenerator.register(ModItems.KART_BLACK, Models.GENERATED);
        // special
        itemModelGenerator.register(ModItems.KART_DEFAULT, Models.GENERATED);
        itemModelGenerator.register(ModItems.KART_PURPLE_GOLD, Models.GENERATED);
        itemModelGenerator.register(ModItems.KART_FADE, Models.GENERATED);
    }
}
