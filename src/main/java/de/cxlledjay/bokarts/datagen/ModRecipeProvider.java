package de.cxlledjay.bokarts.datagen;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.item.ModItems;
import de.cxlledjay.bokarts.recipes.KartShapelessPaintColorRecipe;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {

    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }



    // -------------------- add kart crafting patterns --------------------
    private void generateKartShapelessPaintColorRecipes(RecipeExporter exporter) {
        // default
        offerKartRecipe(exporter, ModItems.KART_DEFAULT,        Items.WATER_BUCKET);

        // dyes
        offerKartRecipe(exporter, ModItems.KART_WHITE,          Items.WHITE_DYE);
        offerKartRecipe(exporter, ModItems.KART_ORANGE,         Items.ORANGE_DYE);
        offerKartRecipe(exporter, ModItems.KART_MAGENTA,        Items.MAGENTA_DYE);
        offerKartRecipe(exporter, ModItems.KART_LIGHT_BLUE,     Items.LIGHT_BLUE_DYE);
        offerKartRecipe(exporter, ModItems.KART_YELLOW,         Items.YELLOW_DYE);
        offerKartRecipe(exporter, ModItems.KART_LIME,           Items.LIME_DYE);
        offerKartRecipe(exporter, ModItems.KART_PINK,           Items.PINK_DYE);
        offerKartRecipe(exporter, ModItems.KART_GRAY,           Items.GRAY_DYE);
        offerKartRecipe(exporter, ModItems.KART_LIGHT_GRAY,     Items.LIGHT_GRAY_DYE);
        offerKartRecipe(exporter, ModItems.KART_CYAN,           Items.CYAN_DYE);
        offerKartRecipe(exporter, ModItems.KART_PURPLE,         Items.PURPLE_DYE);
        offerKartRecipe(exporter, ModItems.KART_BLUE,           Items.BLUE_DYE);
        offerKartRecipe(exporter, ModItems.KART_BROWN,          Items.BROWN_DYE);
        offerKartRecipe(exporter, ModItems.KART_GREEN,          Items.GREEN_DYE);
        offerKartRecipe(exporter, ModItems.KART_RED,            Items.RED_DYE);
        offerKartRecipe(exporter, ModItems.KART_BLACK,          Items.BLACK_DYE);

        // special
        offerKartRecipe(exporter, ModItems.KART_PURPLE_GOLD,    Items.PURPLE_DYE,       Items.GOLD_INGOT);
        offerKartRecipe(exporter, ModItems.KART_FADE,           Items.ORANGE_DYE,       Items.MAGENTA_DYE,      Items.BLUE_DYE);
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
                .pattern(" RH")
                .pattern("LSF")
                .pattern("WCW")
                .input('H', Items.HOPPER)
                .input('F', Items.FURNACE)
                .input('C', Items.COPPER_INGOT)
                .input('R', Items.LEVER)
                .input('S', ModItems.KART_CRAFTING_SEAT)
                .input('W', ModItems.KART_CRAFTING_WHEEL)
                .input('L', ModItems.KART_CRAFTING_STEERING_WHEEL)
                .criterion(hasItem(Items.HOPPER), conditionsFromItem(Items.HOPPER))
                .criterion(hasItem(Items.FURNACE), conditionsFromItem(Items.FURNACE))
                .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                .criterion(hasItem(Items.LEVER), conditionsFromItem(Items.LEVER))
                .criterion(hasItem(ModItems.KART_CRAFTING_SEAT), conditionsFromItem( ModItems.KART_CRAFTING_SEAT))
                .criterion(hasItem(ModItems.KART_CRAFTING_WHEEL), conditionsFromItem( ModItems.KART_CRAFTING_WHEEL))
                .criterion(hasItem(ModItems.KART_CRAFTING_STEERING_WHEEL), conditionsFromItem( ModItems.KART_CRAFTING_STEERING_WHEEL))
                .offerTo(exporter, BoKarts.id("kart/default_from_parts"));

        // generate shapeless recipes for all variants
        generateKartShapelessPaintColorRecipes(exporter);

    }



    /**
     * Helper method that uses Vanilla's builder but intercepts the output to inject our custom Recipe class.
     */
    private void offerKartRecipe(RecipeExporter exporter, Item outputKart, Item... modifiers) {
        // The tag representing any of your karts
        TagKey<Item> KART_TAG = TagKey.of(RegistryKeys.ITEM, BoKarts.id("karts"));

        // Build the vanilla shapeless recipe
        ShapelessRecipeJsonBuilder builder = ShapelessRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, outputKart)
                .input(KART_TAG);

        for (Item modifier : modifiers) {
            builder.input(modifier);
        }

        // Unlocks the recipe in the book the moment a player gets any Kart
        builder.criterion("has_kart", conditionsFromTag(KART_TAG));

        // Generate a clean JSON file name (e.g. "paint_kart_red")
        String kartName = Registries.ITEM.getId(outputKart).getPath();
        Identifier recipeId = BoKarts.id(kartName);

        // Intercept the exporter to swap to KartShapelessPaintColorRecipe
        RecipeExporter interceptor = new RecipeExporter() {
            @Override
            public void accept(Identifier id, Recipe<?> recipe, @Nullable AdvancementEntry advancement) {
                if (recipe instanceof ShapelessRecipe sr) {
                    // Create our custom component-preserving recipe using the vanilla builder's data
                    KartShapelessPaintColorRecipe customRecipe = new KartShapelessPaintColorRecipe(
                            sr.getGroup(),
                            sr.getCategory(),
                            new ItemStack(outputKart),
                            sr.getIngredients()
                    );
                    // Save it using the REAL exporter
                    exporter.accept(id, customRecipe, advancement);
                } else {
                    exporter.accept(id, recipe, advancement);
                }
            }

            @Override
            public Advancement.Builder getAdvancementBuilder() {
                return exporter.getAdvancementBuilder();
            }
        };

        // Offer it to our interceptor instead of the main exporter
        builder.offerTo(interceptor, recipeId);
    }
}
