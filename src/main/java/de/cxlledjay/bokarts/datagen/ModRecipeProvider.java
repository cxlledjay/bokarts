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
                .pattern(" W ")
                .pattern("WIW")
                .pattern(" W ")
                .input('W', Items.BLACK_WOOL)
                .input('I', Items.IRON_BLOCK)
                .criterion(hasItem(Items.BLACK_WOOL), conditionsFromItem(Items.BLACK_WOOL))
                .criterion(hasItem(Items.IRON_BLOCK), conditionsFromItem(Items.IRON_BLOCK))
                .offerTo(exporter);

        // race wheels
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.WHEELS_SUPER_SOFT)
                .pattern("MDM")
                .pattern("DBD")
                .pattern("MDM")
                .input('B', ModItems.WHEELS_STREET)
                .input('D', Items.RED_DYE)
                .input('M', Items.HONEY_BOTTLE)
                .criterion(hasItem(ModItems.WHEELS_STREET), conditionsFromItem(ModItems.WHEELS_STREET))
                .criterion(hasItem(Items.RED_DYE), conditionsFromItem(Items.RED_DYE))
                .criterion(hasItem(Items.HONEY_BOTTLE), conditionsFromItem(Items.HONEY_BOTTLE))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.WHEELS_SOFT)
                .pattern("MDM")
                .pattern("DBD")
                .pattern("MDM")
                .input('B', ModItems.WHEELS_STREET)
                .input('D', Items.YELLOW_DYE)
                .input('M', Items.HONEYCOMB)
                .criterion(hasItem(ModItems.WHEELS_STREET), conditionsFromItem(ModItems.WHEELS_STREET))
                .criterion(hasItem(Items.YELLOW_DYE), conditionsFromItem(Items.YELLOW_DYE))
                .criterion(hasItem(Items.HONEYCOMB), conditionsFromItem(Items.HONEYCOMB))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.WHEELS_MEDIUM)
                .pattern("MDM")
                .pattern("DBD")
                .pattern("MDM")
                .input('B', ModItems.WHEELS_STREET)
                .input('D', Items.WHITE_DYE)
                .input('M', Items.QUARTZ)
                .criterion(hasItem(ModItems.WHEELS_STREET), conditionsFromItem(ModItems.WHEELS_STREET))
                .criterion(hasItem(Items.WHITE_DYE), conditionsFromItem(Items.WHITE_DYE))
                .criterion(hasItem(Items.QUARTZ), conditionsFromItem(Items.QUARTZ))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.WHEELS_HARD)
                .pattern("MDM")
                .pattern("DBD")
                .pattern("MDM")
                .input('B', ModItems.WHEELS_STREET)
                .input('D', Items.LIGHT_BLUE_DYE)
                .input('M', Items.GRAY_CONCRETE)
                .criterion(hasItem(ModItems.WHEELS_STREET), conditionsFromItem(ModItems.WHEELS_STREET))
                .criterion(hasItem(Items.LIGHT_BLUE_DYE), conditionsFromItem(Items.LIGHT_BLUE_DYE))
                .criterion(hasItem(Items.GRAY_CONCRETE), conditionsFromItem(Items.GRAY_CONCRETE))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.WHEELS_SUPER_HARD)
                .pattern("MDM")
                .pattern("DBD")
                .pattern("MDM")
                .input('B', ModItems.WHEELS_STREET)
                .input('D', Items.ORANGE_DYE)
                .input('M', Items.OBSIDIAN)
                .criterion(hasItem(ModItems.WHEELS_STREET), conditionsFromItem(ModItems.WHEELS_STREET))
                .criterion(hasItem(Items.ORANGE_DYE), conditionsFromItem(Items.ORANGE_DYE))
                .criterion(hasItem(Items.OBSIDIAN), conditionsFromItem(Items.OBSIDIAN))
                .offerTo(exporter);



        // other wheels
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.WHEELS_DRIFT)
                .pattern("MMM")
                .pattern("MBM")
                .pattern("MMM")
                .input('B', ModItems.WHEELS_STREET)
                .input('M', Items.PACKED_ICE)
                .criterion(hasItem(ModItems.WHEELS_STREET), conditionsFromItem(ModItems.WHEELS_STREET))
                .criterion(hasItem(Items.PACKED_ICE), conditionsFromItem(Items.PACKED_ICE))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.WHEELS_DRIFT_PLUS)
                .pattern("MMM")
                .pattern("MBM")
                .pattern("MMM")
                .input('B', ModItems.WHEELS_STREET)
                .input('M', Items.BLUE_ICE)
                .criterion(hasItem(ModItems.WHEELS_STREET), conditionsFromItem(ModItems.WHEELS_STREET))
                .criterion(hasItem(Items.BLUE_ICE), conditionsFromItem(Items.BLUE_ICE))
                .offerTo(exporter);


        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.WHEELS_OFFROAD)
                .pattern("MDM")
                .pattern("DBD")
                .pattern("MDM")
                .input('B', ModItems.WHEELS_STREET)
                .input('D', Items.DIRT)
                .input('M', Items.GRAVEL)
                .criterion(hasItem(ModItems.WHEELS_STREET), conditionsFromItem(ModItems.WHEELS_STREET))
                .criterion(hasItem(Items.DIRT), conditionsFromItem(Items.DIRT))
                .criterion(hasItem(Items.GRAVEL), conditionsFromItem(Items.GRAVEL))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.WHEELS_SPIKES)
                .pattern("MDM")
                .pattern("DBD")
                .pattern("MDM")
                .input('B', ModItems.WHEELS_OFFROAD)
                .input('M', Items.IRON_NUGGET)
                .input('D', Items.POINTED_DRIPSTONE)
                .criterion(hasItem(ModItems.WHEELS_OFFROAD), conditionsFromItem(ModItems.WHEELS_OFFROAD))
                .criterion(hasItem(Items.IRON_NUGGET), conditionsFromItem(Items.IRON_NUGGET))
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
        // base variants
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.SPOILER_STREET)
                .pattern("SSS")
                .pattern("C C")
                .input('S', Items.POLISHED_DEEPSLATE_SLAB)
                .input('C', Items.CHAIN)
                .criterion(hasItem(Items.POLISHED_DEEPSLATE_SLAB), conditionsFromItem(Items.POLISHED_DEEPSLATE_SLAB))
                .criterion(hasItem(Items.CHAIN), conditionsFromItem(Items.CHAIN))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.SPOILER_RACE)
                .pattern("BSB")
                .pattern("C C")
                .input('S', Items.POLISHED_DEEPSLATE_SLAB)
                .input('B', Items.POLISHED_DEEPSLATE_STAIRS)
                .input('C', Items.CHAIN)
                .criterion(hasItem(Items.POLISHED_DEEPSLATE_SLAB), conditionsFromItem(Items.POLISHED_DEEPSLATE_SLAB))
                .criterion(hasItem(Items.POLISHED_DEEPSLATE_STAIRS), conditionsFromItem(Items.POLISHED_DEEPSLATE_STAIRS))
                .criterion(hasItem(Items.CHAIN), conditionsFromItem(Items.CHAIN))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.SPOILER_CUP)
                .pattern("BSB")
                .pattern("BSB")
                .pattern("C C")
                .input('S', Items.POLISHED_DEEPSLATE_SLAB)
                .input('B', Items.POLISHED_DEEPSLATE_STAIRS)
                .input('C', Items.CHAIN)
                .criterion(hasItem(Items.POLISHED_DEEPSLATE_SLAB), conditionsFromItem(Items.POLISHED_DEEPSLATE_SLAB))
                .criterion(hasItem(Items.POLISHED_DEEPSLATE_STAIRS), conditionsFromItem(Items.POLISHED_DEEPSLATE_STAIRS))
                .criterion(hasItem(Items.CHAIN), conditionsFromItem(Items.CHAIN))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.SPOILER_RACE_PRO_MAX)
                .pattern("SSS")
                .pattern("SSS")
                .pattern("C C")
                .input('S', Items.POLISHED_DEEPSLATE_SLAB)
                .input('C', Items.CHAIN)
                .criterion(hasItem(Items.POLISHED_DEEPSLATE_SLAB), conditionsFromItem(Items.POLISHED_DEEPSLATE_SLAB))
                .criterion(hasItem(Items.CHAIN), conditionsFromItem(Items.CHAIN))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.SPOILER_TYPE_ONE)
                .pattern("BSB")
                .pattern("BDB")
                .pattern("C C")
                .input('S', Items.POLISHED_DEEPSLATE_SLAB)
                .input('B', Items.POLISHED_DEEPSLATE)
                .input('C', Items.CHAIN)
                .input('D', Items.DIAMOND)
                .criterion(hasItem(Items.POLISHED_DEEPSLATE_SLAB), conditionsFromItem(Items.POLISHED_DEEPSLATE_SLAB))
                .criterion(hasItem(Items.POLISHED_DEEPSLATE), conditionsFromItem(Items.POLISHED_DEEPSLATE))
                .criterion(hasItem(Items.CHAIN), conditionsFromItem(Items.CHAIN))
                .criterion(hasItem(Items.DIAMOND), conditionsFromItem(Items.DIAMOND))
                .offerTo(exporter);
        // cabron variants
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.SPOILER_STREET_CARBON)
                .pattern("CCC")
                .pattern("CBC")
                .pattern("CCC")
                .input('B', ModItems.SPOILER_STREET)
                .input('C', Items.COAL)
                .criterion(hasItem(ModItems.SPOILER_STREET), conditionsFromItem(ModItems.SPOILER_STREET))
                .criterion(hasItem(Items.COAL), conditionsFromItem(Items.COAL))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.SPOILER_RACE_CARBON)
                .pattern("CCC")
                .pattern("CBC")
                .pattern("CCC")
                .input('B', ModItems.SPOILER_RACE)
                .input('C', Items.COAL)
                .criterion(hasItem(ModItems.SPOILER_RACE), conditionsFromItem(ModItems.SPOILER_RACE))
                .criterion(hasItem(Items.COAL), conditionsFromItem(Items.COAL))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.SPOILER_CUP_CARBON)
                .pattern("CCC")
                .pattern("CBC")
                .pattern("CCC")
                .input('B', ModItems.SPOILER_CUP)
                .input('C', Items.COAL)
                .criterion(hasItem(ModItems.SPOILER_CUP), conditionsFromItem(ModItems.SPOILER_CUP))
                .criterion(hasItem(Items.COAL), conditionsFromItem(Items.COAL))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.SPOILER_RACE_PRO_MAX_CARBON)
                .pattern("CCC")
                .pattern("CBC")
                .pattern("CCC")
                .input('B', ModItems.SPOILER_RACE_PRO_MAX)
                .input('C', Items.COAL)
                .criterion(hasItem(ModItems.SPOILER_RACE_PRO_MAX), conditionsFromItem(ModItems.SPOILER_RACE_PRO_MAX))
                .criterion(hasItem(Items.COAL), conditionsFromItem(Items.COAL))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.SPOILER_TYPE_ONE_CARBON)
                .pattern("CCC")
                .pattern("CBC")
                .pattern("CCC")
                .input('B', ModItems.SPOILER_TYPE_ONE)
                .input('C', Items.COAL)
                .criterion(hasItem(ModItems.SPOILER_TYPE_ONE), conditionsFromItem(ModItems.SPOILER_TYPE_ONE))
                .criterion(hasItem(Items.COAL), conditionsFromItem(Items.COAL))
                .offerTo(exporter);
        // colored variants
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.SPOILER_TYPE_ONE_COLORED)
                .pattern("PPP")
                .pattern("DBD")
                .pattern("PPP")
                .input('B', ModItems.SPOILER_TYPE_ONE)
                .input('D', ModItems.DYE_MIX)
                .input('P', Items.PAPER)
                .criterion(hasItem(ModItems.SPOILER_TYPE_ONE), conditionsFromItem(ModItems.SPOILER_TYPE_ONE))
                .criterion(hasItem(ModItems.DYE_MIX), conditionsFromItem(ModItems.DYE_MIX))
                .criterion(hasItem(Items.PAPER), conditionsFromItem(Items.PAPER))
                .offerTo(exporter);

        // --- aero ---
        // base variants
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.AERO_RACE)
                .pattern("CIC")
                .pattern("C C")
                .pattern("SSS")
                .input('I', Items.IRON_BARS)
                .input('S', Items.POLISHED_DEEPSLATE_SLAB)
                .input('C', Items.CHAIN)
                .criterion(hasItem(Items.IRON_BARS), conditionsFromItem(Items.IRON_BARS))
                .criterion(hasItem(Items.POLISHED_DEEPSLATE_SLAB), conditionsFromItem(Items.POLISHED_DEEPSLATE_SLAB))
                .criterion(hasItem(Items.CHAIN), conditionsFromItem(Items.CHAIN))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.AERO_CUP)
                .pattern("CIC")
                .pattern("CSC")
                .pattern("BSB")
                .input('I', Items.IRON_BARS)
                .input('S', Items.POLISHED_DEEPSLATE_SLAB)
                .input('B', Items.POLISHED_DEEPSLATE_STAIRS)
                .input('C', Items.CHAIN)
                .criterion(hasItem(Items.IRON_BARS), conditionsFromItem(Items.IRON_BARS))
                .criterion(hasItem(Items.POLISHED_DEEPSLATE_SLAB), conditionsFromItem(Items.POLISHED_DEEPSLATE_SLAB))
                .criterion(hasItem(Items.POLISHED_DEEPSLATE_STAIRS), conditionsFromItem(Items.POLISHED_DEEPSLATE_STAIRS))
                .criterion(hasItem(Items.CHAIN), conditionsFromItem(Items.CHAIN))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.AERO_TYPE_ONE)
                .pattern("CIC")
                .pattern("CDC")
                .pattern("BSB")
                .input('I', Items.IRON_BARS)
                .input('S', Items.POLISHED_DEEPSLATE_SLAB)
                .input('B', Items.POLISHED_DEEPSLATE)
                .input('D', Items.DIAMOND)
                .input('C', Items.CHAIN)
                .criterion(hasItem(Items.IRON_BARS), conditionsFromItem(Items.IRON_BARS))
                .criterion(hasItem(Items.POLISHED_DEEPSLATE_SLAB), conditionsFromItem(Items.POLISHED_DEEPSLATE_SLAB))
                .criterion(hasItem(Items.POLISHED_DEEPSLATE), conditionsFromItem(Items.POLISHED_DEEPSLATE))
                .criterion(hasItem(Items.DIAMOND), conditionsFromItem(Items.DIAMOND))
                .criterion(hasItem(Items.CHAIN), conditionsFromItem(Items.CHAIN))
                .offerTo(exporter);
        // cabron
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.AERO_RACE_CARBON)
                .pattern("CCC")
                .pattern("CBC")
                .pattern("CCC")
                .input('B', ModItems.AERO_RACE)
                .input('C', Items.COAL)
                .criterion(hasItem(ModItems.AERO_RACE), conditionsFromItem(ModItems.AERO_RACE))
                .criterion(hasItem(Items.COAL), conditionsFromItem(Items.COAL))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.AERO_CUP_CARBON)
                .pattern("CCC")
                .pattern("CBC")
                .pattern("CCC")
                .input('B', ModItems.AERO_CUP)
                .input('C', Items.COAL)
                .criterion(hasItem(ModItems.AERO_CUP), conditionsFromItem(ModItems.AERO_CUP))
                .criterion(hasItem(Items.COAL), conditionsFromItem(Items.COAL))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.AERO_TYPE_ONE_CARBON)
                .pattern("CCC")
                .pattern("CBC")
                .pattern("CCC")
                .input('B', ModItems.AERO_TYPE_ONE)
                .input('C', Items.COAL)
                .criterion(hasItem(ModItems.AERO_TYPE_ONE), conditionsFromItem(ModItems.AERO_TYPE_ONE))
                .criterion(hasItem(Items.COAL), conditionsFromItem(Items.COAL))
                .offerTo(exporter);
        // colored
        ShapedRecipeJsonBuilder.create(RecipeCategory.TRANSPORTATION, ModItems.AERO_TYPE_ONE_COLORED)
                .pattern("PPP")
                .pattern("DBD")
                .pattern("PPP")
                .input('B', ModItems.AERO_TYPE_ONE)
                .input('D', ModItems.DYE_MIX)
                .input('P', Items.PAPER)
                .criterion(hasItem(ModItems.AERO_TYPE_ONE), conditionsFromItem(ModItems.AERO_TYPE_ONE))
                .criterion(hasItem(ModItems.DYE_MIX), conditionsFromItem(ModItems.DYE_MIX))
                .criterion(hasItem(Items.PAPER), conditionsFromItem(Items.PAPER))
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
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.KART_LIFT)
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
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.KART_LIFT)
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
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.LIVERY_WORKBENCH)
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
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.PAINT_BUCKET)
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
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, ModBlocks.TROPHY_GOLD)
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
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, ModBlocks.TROPHY_SILVER)
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
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, ModBlocks.TROPHY_BRONZE)
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
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, ModBlocks.TROPHY_BRONZE)
                .pattern("IBI")
                .pattern(" I ")
                .pattern("GGG")
                .input('B', Items.WAXED_COPPER_BLOCK)
                .input('I', Items.COPPER_INGOT)
                .input('G', Items.GRAY_CONCRETE)
                .criterion(hasItem(Items.WAXED_COPPER_BLOCK), conditionsFromItem(Items.WAXED_COPPER_BLOCK))
                .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                .criterion(hasItem(Items.GRAY_CONCRETE), conditionsFromItem(Items.GRAY_CONCRETE))
                .offerTo(exporter, BoKarts.id("trophy_bronze_crafting1"));


        // silly blocks
        ShapelessRecipeJsonBuilder.create(RecipeCategory.REDSTONE, ModBlocks.HORN_BLOCK)
                .input(Items.NOTE_BLOCK)
                .input(ModItems.STEERING_WHEEL)
                .criterion(hasItem(Items.NOTE_BLOCK), conditionsFromItem(Items.NOTE_BLOCK))
                .criterion(hasItem(ModItems.STEERING_WHEEL), conditionsFromItem(ModItems.STEERING_WHEEL))
                .offerTo(exporter);

        // track timer
        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, ModBlocks.TRACK_TIMER_BLOCK)
                .pattern("SHS")
                .pattern("COG")
                .pattern("SRS")
                .input('O', Items.OBSERVER)
                .input('G', Items.GLASS_PANE)
                .input('C', Items.COMPARATOR)
                .input('R', Items.REDSTONE)
                .input('H', Items.HOPPER)
                .input('S', Items.STONE)
                .criterion(hasItem(Items.OBSERVER), conditionsFromItem(Items.OBSERVER))
                .criterion(hasItem(Items.GLASS_PANE), conditionsFromItem(Items.GLASS_PANE))
                .criterion(hasItem(Items.COMPARATOR), conditionsFromItem(Items.COMPARATOR))
                .criterion(hasItem(Items.REDSTONE), conditionsFromItem(Items.REDSTONE))
                .criterion(hasItem(Items.HOPPER), conditionsFromItem(Items.HOPPER))
                .criterion(hasItem(Items.STONE), conditionsFromItem(Items.STONE))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, ModBlocks.TRACK_TIMER_REFLECTOR_BLOCK)
                .pattern("SSS")
                .pattern("SOG")
                .pattern("SSS")
                .input('O', Items.OBSERVER)
                .input('G', Items.GLASS_PANE)
                .input('S', Items.STONE)
                .criterion(hasItem(Items.OBSERVER), conditionsFromItem(Items.OBSERVER))
                .criterion(hasItem(Items.GLASS_PANE), conditionsFromItem(Items.GLASS_PANE))
                .criterion(hasItem(Items.STONE), conditionsFromItem(Items.STONE))
                .offerTo(exporter);


    }
}
