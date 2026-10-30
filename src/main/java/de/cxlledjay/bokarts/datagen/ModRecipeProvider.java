package de.cxlledjay.bokarts.datagen;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.block.ModBlocks;
import de.cxlledjay.bokarts.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.data.server.recipe.SmithingTransformRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {

    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {

        // --- chassis ---
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.CHASSIS_BLUEPRINT)
                .pattern(" L ")
                .pattern("WSR")
                .pattern("CIC")
                .input('W', ModItems.STEERING_WHEEL)
                .input('S', ModItems.SEAT)
                .input('L', Items.LEVER)
                .input('R', Items.REDSTONE_BLOCK)
                .input('C', Items.COPPER_INGOT)
                .input('I', Items.IRON_BLOCK)
                .criterion(hasItem(ModItems.STEERING_WHEEL), conditionsFromItem(ModItems.STEERING_WHEEL))
                .criterion(hasItem(ModItems.SEAT), conditionsFromItem(ModItems.SEAT))
                .criterion(hasItem(Items.LEVER), conditionsFromItem(Items.LEVER))
                .criterion(hasItem(Items.REDSTONE_BLOCK), conditionsFromItem(Items.REDSTONE_BLOCK))
                .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                .criterion(hasItem(Items.IRON_BLOCK), conditionsFromItem(Items.IRON_BLOCK))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.STEERING_WHEEL)
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

        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.SEAT)
                .pattern("  W")
                .pattern("  W")
                .pattern("WWW")
                .input('W', Items.GRAY_WOOL)
                .criterion(hasItem(Items.GRAY_WOOL), conditionsFromItem(Items.GRAY_WOOL))
                .offerTo(exporter);

        // --- wheels ---
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.WHEELS_STREET)
                .pattern("MWM")
                .pattern("WIW")
                .pattern("MWM")
                .input('W', Items.BLACK_WOOL)
                .input('I', Items.IRON_BLOCK)
                .input('M', Items.GRAY_CONCRETE_POWDER)
                .criterion(hasItem(Items.BLACK_WOOL), conditionsFromItem(Items.BLACK_WOOL))
                .criterion(hasItem(Items.IRON_BLOCK), conditionsFromItem(Items.IRON_BLOCK))
                .criterion(hasItem(Items.GRAY_CONCRETE_POWDER), conditionsFromItem(Items.GRAY_CONCRETE_POWDER))
                .offerTo(exporter);

        // race wheels
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.WHEELS_SUPER_SOFT)
                .pattern("HWH")
                .pattern("WIW")
                .pattern("HWH")
                .input('W', Items.RED_WOOL)
                .input('I', Items.IRON_BLOCK)
                .input('H', Items.HONEY_BLOCK)
                .criterion(hasItem(Items.RED_WOOL), conditionsFromItem(Items.RED_WOOL))
                .criterion(hasItem(Items.IRON_BLOCK), conditionsFromItem(Items.IRON_BLOCK))
                .criterion(hasItem(Items.HONEY_BLOCK), conditionsFromItem(Items.HONEY_BLOCK))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.WHEELS_SOFT)
                .pattern("MWM")
                .pattern("WIW")
                .pattern("MWM")
                .input('W', Items.YELLOW_WOOL)
                .input('I', Items.IRON_BLOCK)
                .input('M', Items.HONEY_BOTTLE)
                .criterion(hasItem(Items.YELLOW_WOOL), conditionsFromItem(Items.YELLOW_WOOL))
                .criterion(hasItem(Items.IRON_BLOCK), conditionsFromItem(Items.IRON_BLOCK))
                .criterion(hasItem(Items.HONEY_BOTTLE), conditionsFromItem(Items.HONEY_BOTTLE))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.WHEELS_MEDIUM)
                .pattern("MWM")
                .pattern("WIW")
                .pattern("MWM")
                .input('W', Items.WHITE_WOOL)
                .input('I', Items.IRON_BLOCK)
                .input('M', Items.GRAY_WOOL)
                .criterion(hasItem(Items.WHITE_WOOL), conditionsFromItem(Items.WHITE_WOOL))
                .criterion(hasItem(Items.IRON_BLOCK), conditionsFromItem(Items.IRON_BLOCK))
                .criterion(hasItem(Items.GRAY_WOOL), conditionsFromItem(Items.GRAY_WOOL))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.WHEELS_HARD)
                .pattern("MWM")
                .pattern("WIW")
                .pattern("MWM")
                .input('W', Items.LIGHT_BLUE_WOOL)
                .input('I', Items.IRON_BLOCK)
                .input('M', Items.GRAY_CONCRETE)
                .criterion(hasItem(Items.LIGHT_BLUE_WOOL), conditionsFromItem(Items.LIGHT_BLUE_WOOL))
                .criterion(hasItem(Items.IRON_BLOCK), conditionsFromItem(Items.IRON_BLOCK))
                .criterion(hasItem(Items.GRAY_CONCRETE), conditionsFromItem(Items.GRAY_CONCRETE))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.WHEELS_SUPER_HARD)
                .pattern("MWM")
                .pattern("WIW")
                .pattern("MWM")
                .input('W', Items.ORANGE_WOOL)
                .input('I', Items.IRON_BLOCK)
                .input('M', Items.OBSIDIAN)
                .criterion(hasItem(Items.ORANGE_WOOL), conditionsFromItem(Items.ORANGE_WOOL))
                .criterion(hasItem(Items.IRON_BLOCK), conditionsFromItem(Items.IRON_BLOCK))
                .criterion(hasItem(Items.OBSIDIAN), conditionsFromItem(Items.OBSIDIAN))
                .offerTo(exporter);

        // other wheels
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.WHEELS_DRIFT)
                .pattern("MWM")
                .pattern("WIW")
                .pattern("MWM")
                .input('W', Items.BLACK_WOOL)
                .input('I', Items.IRON_BLOCK)
                .input('M', Items.PACKED_ICE)
                .criterion(hasItem(Items.BLACK_WOOL), conditionsFromItem(Items.BLACK_WOOL))
                .criterion(hasItem(Items.IRON_BLOCK), conditionsFromItem(Items.IRON_BLOCK))
                .criterion(hasItem(Items.PACKED_ICE), conditionsFromItem(Items.PACKED_ICE))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.WHEELS_DRIFT_PLUS)
                .pattern("MWM")
                .pattern("WIW")
                .pattern("MWM")
                .input('W', Items.BLACK_WOOL)
                .input('I', Items.IRON_BLOCK)
                .input('M', Items.BLUE_ICE)
                .criterion(hasItem(Items.BLACK_WOOL), conditionsFromItem(Items.BLACK_WOOL))
                .criterion(hasItem(Items.IRON_BLOCK), conditionsFromItem(Items.IRON_BLOCK))
                .criterion(hasItem(Items.BLUE_ICE), conditionsFromItem(Items.BLUE_ICE))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.WHEELS_OFFROAD)
                .pattern("MWM")
                .pattern("WIW")
                .pattern("MWM")
                .input('W', Items.BLACK_WOOL)
                .input('I', Items.IRON_BLOCK)
                .input('M', Items.DIRT)
                .criterion(hasItem(Items.BLACK_WOOL), conditionsFromItem(Items.BLACK_WOOL))
                .criterion(hasItem(Items.IRON_BLOCK), conditionsFromItem(Items.IRON_BLOCK))
                .criterion(hasItem(Items.DIRT), conditionsFromItem(Items.DIRT))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.WHEELS_SPIKES)
                .pattern("MWM")
                .pattern("WIW")
                .pattern("MWM")
                .input('W', Items.BLACK_WOOL)
                .input('I', Items.IRON_BLOCK)
                .input('M', Items.POINTED_DRIPSTONE)
                .criterion(hasItem(Items.BLACK_WOOL), conditionsFromItem(Items.BLACK_WOOL))
                .criterion(hasItem(Items.IRON_BLOCK), conditionsFromItem(Items.IRON_BLOCK))
                .criterion(hasItem(Items.POINTED_DRIPSTONE), conditionsFromItem(Items.POINTED_DRIPSTONE))
                .offerTo(exporter);

        // --- engines ---
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.ENGINE_COPPER)
                .pattern("PP ")
                .pattern("HFC")
                .input('P', Items.PISTON)
                .input('H', Items.HOPPER)
                .input('F', Items.FURNACE)
                .input('C', Items.COPPER_BLOCK)
                .criterion(hasItem(Items.PISTON), conditionsFromItem(Items.PISTON))
                .criterion(hasItem(Items.HOPPER), conditionsFromItem(Items.HOPPER))
                .criterion(hasItem(Items.FURNACE), conditionsFromItem(Items.FURNACE))
                .criterion(hasItem(Items.COPPER_BLOCK), conditionsFromItem(Items.COPPER_BLOCK))
                .offerTo(exporter, BoKarts.id("engine_copper_crafting"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.ENGINE_COPPER)
                .pattern("PP ")
                .pattern("HFC")
                .input('P', Items.PISTON)
                .input('H', Items.HOPPER)
                .input('F', Items.FURNACE)
                .input('C', Items.WAXED_COPPER_BLOCK)
                .criterion(hasItem(Items.PISTON), conditionsFromItem(Items.PISTON))
                .criterion(hasItem(Items.HOPPER), conditionsFromItem(Items.HOPPER))
                .criterion(hasItem(Items.FURNACE), conditionsFromItem(Items.FURNACE))
                .criterion(hasItem(Items.WAXED_COPPER_BLOCK), conditionsFromItem(Items.WAXED_COPPER_BLOCK))
                .offerTo(exporter, BoKarts.id("engine_copper_crafting1"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.ENGINE_IRON)
                .pattern("III")
                .pattern("IEI")
                .pattern("III")
                .input('E', ModItems.ENGINE_COPPER)
                .input('I', Items.IRON_INGOT)
                .criterion(hasItem(ModItems.ENGINE_COPPER), conditionsFromItem(ModItems.ENGINE_COPPER))
                .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.ENGINE_GOLD)
                .pattern("III")
                .pattern("IEI")
                .pattern("III")
                .input('E', ModItems.ENGINE_IRON)
                .input('I', Items.GOLD_INGOT)
                .criterion(hasItem(ModItems.ENGINE_IRON), conditionsFromItem(ModItems.ENGINE_IRON))
                .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.ENGINE_DIAMOND)
                .pattern("III")
                .pattern("IEI")
                .pattern("III")
                .input('E', ModItems.ENGINE_GOLD)
                .input('I', Items.DIAMOND)
                .criterion(hasItem(ModItems.ENGINE_GOLD), conditionsFromItem(ModItems.ENGINE_GOLD))
                .criterion(hasItem(Items.DIAMOND), conditionsFromItem(Items.DIAMOND))
                .offerTo(exporter);

        SmithingTransformRecipeJsonBuilder.create(
                        Ingredient.ofItems(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                        Ingredient.ofItems(ModItems.ENGINE_DIAMOND),
                        Ingredient.ofItems(Items.NETHERITE_INGOT),
                        RecipeCategory.TRANSPORTATION,
                        ModItems.ENGINE_NETHERITE
                )
                .criterion(hasItem(ModItems.ENGINE_DIAMOND), conditionsFromItem(ModItems.ENGINE_DIAMOND))
                .criterion(hasItem(Items.NETHERITE_INGOT), conditionsFromItem(Items.NETHERITE_INGOT))
                .criterion(hasItem(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), conditionsFromItem(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE))
                .offerTo(exporter, BoKarts.id("engine_netherite_smithing"));

        // --- spoilers
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.SPOILER_STREET)
                .pattern("BBB")
                .pattern("C C")
                .input('B', Items.BLACK_CONCRETE)
                .input('C', Items.CHAIN)
                .criterion(hasItem(Items.BLACK_CONCRETE), conditionsFromItem(Items.BLACK_CONCRETE))
                .criterion(hasItem(Items.CHAIN), conditionsFromItem(Items.CHAIN))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.SPOILER_STREET_CARBON)
                .pattern("BBB")
                .pattern("C C")
                .input('B', Items.COAL_BLOCK)
                .input('C', Items.CHAIN)
                .criterion(hasItem(Items.COAL_BLOCK), conditionsFromItem(Items.COAL_BLOCK))
                .criterion(hasItem(Items.CHAIN), conditionsFromItem(Items.CHAIN))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.SPOILER_RACE)
                .pattern("BBB")
                .pattern("BBB")
                .pattern("C C")
                .input('B', Items.BLACK_CONCRETE)
                .input('C', Items.CHAIN)
                .criterion(hasItem(Items.BLACK_CONCRETE), conditionsFromItem(Items.BLACK_CONCRETE))
                .criterion(hasItem(Items.CHAIN), conditionsFromItem(Items.CHAIN))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.SPOILER_RACE_CARBON)
                .pattern("BBB")
                .pattern("BBB")
                .pattern("C C")
                .input('B', Items.COAL_BLOCK)
                .input('C', Items.CHAIN)
                .criterion(hasItem(Items.COAL_BLOCK), conditionsFromItem(Items.COAL_BLOCK))
                .criterion(hasItem(Items.CHAIN), conditionsFromItem(Items.CHAIN))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.SPOILER_RACE_PRO_MAX)
                .pattern("BBB")
                .pattern("DDD")
                .pattern("C C")
                .input('B', Items.BLACK_CONCRETE)
                .input('C', Items.CHAIN)
                .input('D', Items.DIAMOND)
                .criterion(hasItem(Items.BLACK_CONCRETE), conditionsFromItem(Items.BLACK_CONCRETE))
                .criterion(hasItem(Items.CHAIN), conditionsFromItem(Items.CHAIN))
                .criterion(hasItem(Items.DIAMOND), conditionsFromItem(Items.DIAMOND))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.SPOILER_RACE_PRO_MAX_CARBON)
                .pattern("BBB")
                .pattern("DDD")
                .pattern("C C")
                .input('B', Items.COAL_BLOCK)
                .input('C', Items.CHAIN)
                .input('D', Items.DIAMOND)
                .criterion(hasItem(Items.COAL_BLOCK), conditionsFromItem(Items.COAL_BLOCK))
                .criterion(hasItem(Items.CHAIN), conditionsFromItem(Items.CHAIN))
                .criterion(hasItem(Items.DIAMOND), conditionsFromItem(Items.DIAMOND))
                .offerTo(exporter);

        // --- aero ---
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.AERO_RACE)
                .pattern("CIC")
                .pattern("CBC")
                .pattern("BBB")
                .input('I', Items.IRON_BARS)
                .input('B', Items.BLACK_CONCRETE)
                .input('C', Items.CHAIN)
                .criterion(hasItem(Items.IRON_BARS), conditionsFromItem(Items.IRON_BARS))
                .criterion(hasItem(Items.BLACK_CONCRETE), conditionsFromItem(Items.BLACK_CONCRETE))
                .criterion(hasItem(Items.CHAIN), conditionsFromItem(Items.CHAIN))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.AERO_RACE_CARBON)
                .pattern("CIC")
                .pattern("CBC")
                .pattern("BBB")
                .input('I', Items.IRON_BARS)
                .input('B', Items.COAL_BLOCK)
                .input('C', Items.CHAIN)
                .criterion(hasItem(Items.IRON_BARS), conditionsFromItem(Items.IRON_BARS))
                .criterion(hasItem(Items.COAL_BLOCK), conditionsFromItem(Items.COAL_BLOCK))
                .criterion(hasItem(Items.CHAIN), conditionsFromItem(Items.CHAIN))
                .offerTo(exporter);



        // --- liveries ---
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.LIVERY_TEMPLATE)
                .pattern("RGB")
                .pattern("PPP")
                .input('R', Items.RED_DYE)
                .input('G', Items.GREEN_DYE)
                .input('B', Items.BLUE_DYE)
                .input('P', Items.PAPER)
                .criterion(hasItem(Items.RED_DYE), conditionsFromItem(Items.RED_DYE))
                .criterion(hasItem(Items.GREEN_DYE), conditionsFromItem(Items.GREEN_DYE))
                .criterion(hasItem(Items.BLUE_DYE), conditionsFromItem(Items.BLUE_DYE))
                .criterion(hasItem(Items.PAPER), conditionsFromItem(Items.PAPER))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.DYE_MIX)
                .input(Items.RED_DYE)
                .input(Items.GREEN_DYE)
                .input(Items.BLUE_DYE)
                .criterion(hasItem(Items.RED_DYE), conditionsFromItem(Items.RED_DYE))
                .criterion(hasItem(Items.GREEN_DYE), conditionsFromItem(Items.GREEN_DYE))
                .criterion(hasItem(Items.BLUE_DYE), conditionsFromItem(Items.BLUE_DYE))
                .offerTo(exporter);


        // --- key ---
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.KEY_BLANK)
                .pattern("II ")
                .pattern(" I ")
                .pattern(" F ")
                .input('I', Items.IRON_INGOT)
                .input('F', Items.BLACK_DYE)
                .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                .criterion(hasItem(Items.BLACK_DYE), conditionsFromItem(Items.BLACK_DYE))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.KEY_PAIRING_TOOL)
                .pattern("IDI")
                .pattern("CKC")
                .pattern("IDI")
                .input('I', Items.IRON_INGOT)
                .input('C', Items.COMPARATOR)
                .input('D', Items.DIAMOND)
                .input('K', ModItems.KEY_BLANK)
                .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                .criterion(hasItem(Items.COMPARATOR), conditionsFromItem(Items.COMPARATOR))
                .criterion(hasItem(Items.DIAMOND), conditionsFromItem(Items.DIAMOND))
                .criterion(hasItem(ModItems.KEY_BLANK), conditionsFromItem(ModItems.KEY_BLANK))
                .offerTo(exporter);



        // ========== blocks ==========


        // --- lift ---
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModBlocks.KART_LIFT)
                .pattern(" I ")
                .pattern(" RL")
                .pattern("CIC")
                .input('I', Items.IRON_BLOCK)
                .input('R', Items.REDSTONE_BLOCK)
                .input('L', Items.LEVER)
                .input('C', Items.OXIDIZED_COPPER)
                .criterion(hasItem(Items.IRON_BLOCK), conditionsFromItem(Items.IRON_BLOCK))
                .criterion(hasItem(Items.REDSTONE_BLOCK), conditionsFromItem(Items.REDSTONE_BLOCK))
                .criterion(hasItem(Items.LEVER), conditionsFromItem(Items.LEVER))
                .criterion(hasItem(Items.OXIDIZED_COPPER), conditionsFromItem(Items.OXIDIZED_COPPER))
                .offerTo(exporter, BoKarts.id("kart_lift_crafting"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModBlocks.KART_LIFT)
                .pattern(" I ")
                .pattern(" RL")
                .pattern("CIC")
                .input('I', Items.IRON_BLOCK)
                .input('R', Items.REDSTONE_BLOCK)
                .input('L', Items.LEVER)
                .input('C', Items.WAXED_OXIDIZED_COPPER)
                .criterion(hasItem(Items.IRON_BLOCK), conditionsFromItem(Items.IRON_BLOCK))
                .criterion(hasItem(Items.REDSTONE_BLOCK), conditionsFromItem(Items.REDSTONE_BLOCK))
                .criterion(hasItem(Items.LEVER), conditionsFromItem(Items.LEVER))
                .criterion(hasItem(Items.WAXED_OXIDIZED_COPPER), conditionsFromItem(Items.WAXED_OXIDIZED_COPPER))
                .offerTo(exporter, BoKarts.id("kart_lift_crafting1"));

        // workbenches for liveries
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModBlocks.LIVERY_WORKBENCH)
                .pattern("GGG")
                .pattern("RLR")
                .pattern("III")
                .input('G', Items.GRAY_CARPET)
                .input('R', Items.RED_CONCRETE)
                .input('I', Items.IRON_BLOCK)
                .input('L', ModItems.LIVERY_TEMPLATE)
                .criterion(hasItem(Items.GRAY_CARPET), conditionsFromItem(Items.GRAY_CARPET))
                .criterion(hasItem(Items.RED_CONCRETE), conditionsFromItem(Items.RED_CONCRETE))
                .criterion(hasItem(Items.IRON_BLOCK), conditionsFromItem(Items.IRON_BLOCK))
                .criterion(hasItem(ModItems.LIVERY_TEMPLATE), conditionsFromItem(ModItems.LIVERY_TEMPLATE))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModBlocks.PAINT_BUCKET)
                .pattern(" D ")
                .pattern("IBI")
                .pattern(" I ")
                .input('B', Items.BUCKET)
                .input('I', Items.IRON_INGOT)
                .input('D', ModItems.DYE_MIX)
                .criterion(hasItem(Items.BUCKET), conditionsFromItem(Items.BUCKET))
                .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                .criterion(hasItem(ModItems.DYE_MIX), conditionsFromItem(ModItems.DYE_MIX))
                .offerTo(exporter);

        // trophy
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModBlocks.TROPHY_GOLD)
                .pattern("IBI")
                .pattern(" I ")
                .pattern("GGG")
                .input('B', Items.GOLD_BLOCK)
                .input('I', Items.GOLD_INGOT)
                .input('G', Items.GRAY_CONCRETE)
                .criterion(hasItem(Items.GOLD_BLOCK), conditionsFromItem(Items.GOLD_BLOCK))
                .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                .criterion(hasItem(Items.GRAY_CONCRETE), conditionsFromItem(Items.GRAY_CONCRETE))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModBlocks.TROPHY_SILVER)
                .pattern("IBI")
                .pattern(" I ")
                .pattern("GGG")
                .input('B', Items.IRON_BLOCK)
                .input('I', Items.IRON_INGOT)
                .input('G', Items.GRAY_CONCRETE)
                .criterion(hasItem(Items.IRON_BLOCK), conditionsFromItem(Items.IRON_BLOCK))
                .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                .criterion(hasItem(Items.GRAY_CONCRETE), conditionsFromItem(Items.GRAY_CONCRETE))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModBlocks.TROPHY_BRONZE)
                .pattern("IBI")
                .pattern(" I ")
                .pattern("GGG")
                .input('B', Items.COPPER_BLOCK)
                .input('I', Items.COPPER_INGOT)
                .input('G', Items.GRAY_CONCRETE)
                .criterion(hasItem(Items.COPPER_BLOCK), conditionsFromItem(Items.COPPER_BLOCK))
                .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                .criterion(hasItem(Items.GRAY_CONCRETE), conditionsFromItem(Items.GRAY_CONCRETE))
                .offerTo(exporter);


        // silly blocks
        ShapelessRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModBlocks.HORN_BLOCK)
                .input(Items.NOTE_BLOCK)
                .input(ModItems.STEERING_WHEEL)
                .criterion(hasItem(Items.NOTE_BLOCK), conditionsFromItem(Items.NOTE_BLOCK))
                .criterion(hasItem(ModItems.STEERING_WHEEL), conditionsFromItem(ModItems.STEERING_WHEEL))
                .offerTo(exporter);


    }
}
