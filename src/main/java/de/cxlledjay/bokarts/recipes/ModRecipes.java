package de.cxlledjay.bokarts.recipes;

import de.cxlledjay.bokarts.BoKarts;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModRecipes {

    public static final RecipeSerializer<KartDyeRecipe> KART_DYE_SERIALIZER = Registry.register(
            Registries.RECIPE_SERIALIZER,
            BoKarts.id("kart_dye"),
            new SpecialRecipeSerializer<>(KartDyeRecipe::new)
    );

    public static void register() {
        BoKarts.LOGGER.info("Registering Recipes for " + BoKarts.MOD_ID);
    }
}