package de.cxlledjay.bokarts.recipes;

import com.mojang.serialization.MapCodec;
import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.item.ModItems;
import net.minecraft.item.Items;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModRecipes {

    public static final RecipeSerializer<KartShapelessPaintColorRecipe> KART_SHAPELESS_SERIALIZER = Registry.register(
            Registries.RECIPE_SERIALIZER,
            BoKarts.id("kart_shapeless_paint_color"),
            new RecipeSerializer<KartShapelessPaintColorRecipe>() {
                @Override
                public MapCodec<KartShapelessPaintColorRecipe> codec() {
                    return RecipeSerializer.SHAPELESS.codec().xmap(
                            s -> new KartShapelessPaintColorRecipe(s.getGroup(), s.getCategory(), s.getResult(null), s.getIngredients()),
                            k -> new ShapelessRecipe(k.getGroup(), k.getCategory(), k.getResult(null), k.getIngredients())
                    );
                }

                @Override
                public PacketCodec<RegistryByteBuf, KartShapelessPaintColorRecipe> packetCodec() {
                    return RecipeSerializer.SHAPELESS.packetCodec().xmap(
                            s -> new KartShapelessPaintColorRecipe(s.getGroup(), s.getCategory(), s.getResult(null), s.getIngredients()),
                            k -> new ShapelessRecipe(k.getGroup(), k.getCategory(), k.getResult(null), k.getIngredients())
                    );
                }
            }
    );

    public static void register() {
        BoKarts.LOGGER.info("Registering Recipes for " + BoKarts.MOD_ID);
    }
}