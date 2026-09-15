package de.cxlledjay.bokarts.datagen;

import de.cxlledjay.bokarts.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {

    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {

        // crafting ingredients

        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.KART_CRAFTING_WHEEL)
                .pattern(" W ")
                .pattern("WIW")
                .pattern(" W ")
                .input('W', Items.BLACK_WOOL)
                .input('I', Items.IRON_INGOT)
                .criterion(hasItem(Items.BLACK_WOOL), conditionsFromItem(Items.BLACK_WOOL))
                .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.KART_CRAFTING_STEERING_WHEEL)
                .pattern(" G ")
                .pattern("WLW")
                .pattern(" W ")
                .input('W', Items.GRAY_WOOL)
                .input('L', Items.LAPIS_LAZULI)
                .input('G', Items.GOLD_INGOT)
                .criterion(hasItem(Items.GRAY_WOOL), conditionsFromItem(Items.GRAY_WOOL))
                .criterion(hasItem(Items.LAPIS_LAZULI), conditionsFromItem(Items.LAPIS_LAZULI))
                .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.KART_CRAFTING_SEAT)
                .pattern("  W")
                .pattern("  W")
                .pattern("WWW")
                .input('W', Items.GRAY_WOOL)
                .criterion(hasItem(Items.GRAY_WOOL), conditionsFromItem(Items.GRAY_WOOL))
                .offerTo(exporter);



        // kart

        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.KART_DEFAULT)
                .pattern("  H")
                .pattern("LSF")
                .pattern("WBW")
                .input('H', Items.HOPPER)
                .input('F', Items.FURNACE)
                .input('B', Items.CYAN_TERRACOTTA)
                .input('S', ModItems.KART_CRAFTING_SEAT)
                .input('W', ModItems.KART_CRAFTING_WHEEL)
                .input('L', ModItems.KART_CRAFTING_STEERING_WHEEL)
                .criterion(hasItem(Items.HOPPER), conditionsFromItem(Items.HOPPER))
                .criterion(hasItem(Items.FURNACE), conditionsFromItem(Items.FURNACE))
                .criterion(hasItem(Items.CYAN_TERRACOTTA), conditionsFromItem(Items.CYAN_TERRACOTTA))
                .criterion(hasItem(ModItems.KART_CRAFTING_SEAT), conditionsFromItem( ModItems.KART_CRAFTING_SEAT))
                .criterion(hasItem(ModItems.KART_CRAFTING_WHEEL), conditionsFromItem( ModItems.KART_CRAFTING_WHEEL))
                .criterion(hasItem(ModItems.KART_CRAFTING_STEERING_WHEEL), conditionsFromItem( ModItems.KART_CRAFTING_STEERING_WHEEL))
                .offerTo(exporter);



        // kart paint colors


    }
}
