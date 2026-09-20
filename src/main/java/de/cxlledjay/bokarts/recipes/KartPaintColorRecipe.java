package de.cxlledjay.bokarts.recipes;

import de.cxlledjay.bokarts.component.ModDataComponentTypes;
import de.cxlledjay.bokarts.item.custom.KartItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

// recipe for KartItem + 1-3 Items (generic)
public class KartPaintColorRecipe extends SpecialCraftingRecipe {

    // helper class for patterns
    public static class Pattern {
        private final Item outputKart;
        private final List<Item> modifiers;

        public Pattern(Item outputKart, Item... modifiers) {
            this.outputKart = outputKart;
            this.modifiers = List.of(modifiers);
        }

        public Item getOutput() {
            return this.outputKart;
        }

        public boolean matches(List<Item> itemInGrid) {
            if(itemInGrid.size() != modifiers.size()) return false;

            List<Item> temp = new ArrayList<>(this.modifiers);
            for(Item item : itemInGrid) {
                if(!temp.remove(item)) return false;
            }
            return true;
        }
    }


    // data structure to keep track of patterns
    public static final List<Pattern> PATTERNS = new ArrayList<>();

    // create new crafting pattern
    public static void addPattern(Item outputKart, Item... modifiers) {
        PATTERNS.add(new Pattern(outputKart, modifiers));
    }



    // SpecialCraftingRecipe implementation

    public KartPaintColorRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {

        int kartItemCount = 0; //< count how many KartItems are in crafting grid
        List<Item> modifiers = new ArrayList<>(); //< all items around the kart

        // iterate trough grid
        for (int i = 0; i < input.getSize(); i++) {
            ItemStack stack = input.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof KartItem) {
                    kartItemCount++;
                } else {
                    modifiers.add(stack.getItem());
                }
            }
        }

        // check against constraints
        if (kartItemCount != 1 || modifiers.isEmpty() || modifiers.size() > 3) {
            return false;
        }

        // check against pattern
        for (Pattern pattern : PATTERNS) {
            if (pattern.matches(modifiers)) {
                return true;
            }
        }

        // no pattern matches
        return false;
    }




    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {

        ItemStack oldKartStack = ItemStack.EMPTY;
        List<Item> modifiers = new ArrayList<>();

        for (int i = 0; i < input.getSize(); i++) {
            ItemStack stack = input.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof KartItem) {
                    oldKartStack = stack;
                } else {
                    modifiers.add(stack.getItem());
                }
            }
        }

        if (oldKartStack.isEmpty()) return ItemStack.EMPTY;

        // Find which pattern we are crafting
        Item outputKartItem = null;
        for (Pattern pattern : PATTERNS) {
            if (pattern.matches(modifiers)) {
                outputKartItem = pattern.getOutput();
                break;
            }
        }

        // fallback
        if (outputKartItem == null) return ItemStack.EMPTY;

        return createKartStack(oldKartStack, outputKartItem);
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 4;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.KART_PAINT_COLOR_SERIALIZER;
    }


    // helper to transfer DataComponentTypes
    private static ItemStack createKartStack(ItemStack oldKartStack, Item kartItem) {
        // create new kart
        ItemStack newKartStack = new ItemStack(kartItem);

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
