package de.cxlledjay.bokarts.recipes;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.item.ModItems;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModRecipes {

    public static final RecipeSerializer<KartPaintColorRecipe> KART_PAINT_COLOR_SERIALIZER = Registry.register(
            Registries.RECIPE_SERIALIZER,
            BoKarts.id("kart_paint_color"),
            new SpecialRecipeSerializer<>(KartPaintColorRecipe::new)
    );

    public static void register() {
        BoKarts.LOGGER.info("Registering Recipes for " + BoKarts.MOD_ID);

        // -------------------- add kart crafting patterns --------------------

        // default
        KartPaintColorRecipe.addPattern(ModItems.KART_DEFAULT,      Items.WATER_BUCKET);

        // vanilla dye
        KartPaintColorRecipe.addPattern(ModItems.KART_WHITE,        Items.WHITE_DYE);
        KartPaintColorRecipe.addPattern(ModItems.KART_ORANGE,       Items.ORANGE_DYE);
        KartPaintColorRecipe.addPattern(ModItems.KART_MAGENTA,      Items.MAGENTA_DYE);
        KartPaintColorRecipe.addPattern(ModItems.KART_LIGHT_BLUE,   Items.LIGHT_BLUE_DYE);
        KartPaintColorRecipe.addPattern(ModItems.KART_YELLOW,       Items.YELLOW_DYE);
        KartPaintColorRecipe.addPattern(ModItems.KART_LIME,         Items.LIME_DYE);
        KartPaintColorRecipe.addPattern(ModItems.KART_PINK,         Items.PINK_DYE);
        KartPaintColorRecipe.addPattern(ModItems.KART_GRAY,         Items.GRAY_DYE);
        KartPaintColorRecipe.addPattern(ModItems.KART_LIGHT_GRAY,   Items.LIGHT_GRAY_DYE);
        KartPaintColorRecipe.addPattern(ModItems.KART_CYAN,         Items.CYAN_DYE);
        KartPaintColorRecipe.addPattern(ModItems.KART_PURPLE,       Items.PURPLE_DYE);
        KartPaintColorRecipe.addPattern(ModItems.KART_BLUE,         Items.BLUE_DYE);
        KartPaintColorRecipe.addPattern(ModItems.KART_BROWN,        Items.BROWN_DYE);
        KartPaintColorRecipe.addPattern(ModItems.KART_GREEN,        Items.GREEN_DYE);
        KartPaintColorRecipe.addPattern(ModItems.KART_RED,          Items.RED_DYE);
        KartPaintColorRecipe.addPattern(ModItems.KART_BLACK,        Items.BLACK_DYE);

        // special
        KartPaintColorRecipe.addPattern(ModItems.KART_PURPLE_GOLD,  Items.PURPLE_DYE,       Items.GOLD_INGOT);
        KartPaintColorRecipe.addPattern(ModItems.KART_FADE,         Items.ORANGE_DYE,       Items.MAGENTA_DYE,      Items.BLUE_DYE);
    }
}