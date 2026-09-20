package de.cxlledjay.bokarts.recipes;

import de.cxlledjay.bokarts.component.ModDataComponentTypes;
import de.cxlledjay.bokarts.item.custom.KartItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;

public class KartShapelessPaintColorRecipe extends ShapelessRecipe {

    public KartShapelessPaintColorRecipe(String group, CraftingRecipeCategory category, ItemStack result, DefaultedList<Ingredient> ingredients) {
        super(group, category, result, ingredients);
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {

        // shapeless recipe handles crafting
        ItemStack output = super.craft(input, lookup);
        ItemStack kartInGrid = ItemStack.EMPTY;

        // steal kart in crafting grid
        for (int i = 0; i < input.getSize(); i++) {
            ItemStack stack = input.getStackInSlot(i);
            if (stack.getItem() instanceof KartItem) {
                kartInGrid = stack;
                break; // found!
            }
        }

        // return vanilla output with DataComponentTypes stolen from grid
        return createKartStack(kartInGrid, output);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.KART_SHAPELESS_SERIALIZER;
    }


    // helper to transfer DataComponentTypes
    private static ItemStack createKartStack(ItemStack oldKartStack, ItemStack newKartStack) {

        // copy DataComponentTypes
        if (oldKartStack.contains(ModDataComponentTypes.KART_ITEM_FUEL)) {
            newKartStack.set(ModDataComponentTypes.KART_ITEM_FUEL, oldKartStack.get(ModDataComponentTypes.KART_ITEM_FUEL));
        }
        if (oldKartStack.contains(ModDataComponentTypes.KART_ITEM_ODOMETER)) {
            newKartStack.set(ModDataComponentTypes.KART_ITEM_ODOMETER, oldKartStack.get(ModDataComponentTypes.KART_ITEM_ODOMETER));
        }
        if (oldKartStack.contains(ModDataComponentTypes.KART_ITEM_HORN_SOUND)) {
            newKartStack.set(ModDataComponentTypes.KART_ITEM_HORN_SOUND, oldKartStack.get(ModDataComponentTypes.KART_ITEM_HORN_SOUND));
        }

        // return modified new ItemStack
        return newKartStack;
    }
}