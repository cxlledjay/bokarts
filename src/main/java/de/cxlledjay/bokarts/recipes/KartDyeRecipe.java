package de.cxlledjay.bokarts.recipes;

import de.cxlledjay.bokarts.component.ModDataComponentTypes;
import de.cxlledjay.bokarts.item.ModItems;
import de.cxlledjay.bokarts.item.custom.KartItem;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.DyeColor;
import net.minecraft.world.World;

public class KartDyeRecipe extends SpecialCraftingRecipe {

    public KartDyeRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        int kartCount = 0;
        int dyeCount = 0;

        for (int i = 0; i < input.getSize(); i++) {
            ItemStack stack = input.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof KartItem) {
                    kartCount++;
                } else if (stack.getItem() instanceof DyeItem || stack.isOf(net.minecraft.item.Items.WATER_BUCKET)) {
                    // found a color to dye kart; or water bucket to undye kart
                    dyeCount++;
                } else {
                    return false; // item found that does not belong to this recipe
                }
            }
        }

        // recipe only works with: 1 kart + 1 dye
        return kartCount == 1 && dyeCount == 1;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        ItemStack oldKartStack = ItemStack.EMPTY;
        ItemStack dyeItemStack = null;

        // find items in grid
        for (int i = 0; i < input.getSize(); i++) {
            ItemStack stack = input.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof KartItem) {
                    oldKartStack = stack;
                } else if (stack.getItem() instanceof DyeItem || stack.isOf(net.minecraft.item.Items.WATER_BUCKET)) {
                    dyeItemStack = stack;
                }
            }
        }

        if (oldKartStack.isEmpty() || dyeItemStack == null) return ItemStack.EMPTY; // fallback

        // get new kart item by color
        Item newKartItem = null;
        if (dyeItemStack.getItem() instanceof DyeItem dyeItem) {
            newKartItem = getKartByColor(dyeItem.getColor()); // dye
        } else if (dyeItemStack.isOf(net.minecraft.item.Items.WATER_BUCKET)) {
            newKartItem = ModItems.KART_DEFAULT; // undye
        }
        if (newKartItem == null) return ItemStack.EMPTY; // fallback

        // create new kart
        ItemStack newKartStack = new ItemStack(newKartItem);

        // copy DataComponentTypes for
        if (oldKartStack.contains(ModDataComponentTypes.KART_ITEM_FUEL)) {
            newKartStack.set(ModDataComponentTypes.KART_ITEM_FUEL, oldKartStack.get(ModDataComponentTypes.KART_ITEM_FUEL));
        }
        if (oldKartStack.contains(ModDataComponentTypes.KART_ITEM_ODOMETER)) {
            newKartStack.set(ModDataComponentTypes.KART_ITEM_ODOMETER, oldKartStack.get(ModDataComponentTypes.KART_ITEM_ODOMETER));
        }
        if (oldKartStack.contains(ModDataComponentTypes.KART_ITEM_HORN_SOUND)) {
            newKartStack.set(ModDataComponentTypes.KART_ITEM_HORN_SOUND, oldKartStack.get(ModDataComponentTypes.KART_ITEM_HORN_SOUND));
        }

        return newKartStack;
    }

    // helper to get KartItem by DyeColor
    private Item getKartByColor(DyeColor color) {
        return switch (color) {
            case WHITE -> ModItems.KART_WHITE;
            case ORANGE -> ModItems.KART_ORANGE;
            case MAGENTA -> ModItems.KART_MAGENTA;
            case LIGHT_BLUE -> ModItems.KART_LIGHT_BLUE;
            case YELLOW -> ModItems.KART_YELLOW;
            case LIME -> ModItems.KART_LIME;
            case PINK -> ModItems.KART_PINK;
            case GRAY -> ModItems.KART_GRAY;
            case LIGHT_GRAY -> ModItems.KART_LIGHT_GRAY;
            case CYAN -> ModItems.KART_CYAN;
            case PURPLE -> ModItems.KART_PURPLE;
            case BLUE -> ModItems.KART_BLUE;
            case BROWN -> ModItems.KART_BROWN;
            case GREEN -> ModItems.KART_GREEN;
            case RED -> ModItems.KART_RED;
            case BLACK -> ModItems.KART_BLACK;
            default -> ModItems.KART_DEFAULT;
        };
    }


    @Override
    public boolean fits(int width, int height) {
        // at least two slots
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.KART_DYE_SERIALIZER;
    }
}