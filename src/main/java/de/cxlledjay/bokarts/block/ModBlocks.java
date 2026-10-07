package de.cxlledjay.bokarts.block;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.block.custom.KartLiftCoreBlock;
import de.cxlledjay.bokarts.block.custom.KartLiftDummyBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModBlocks {

    // 1. Register Blocks
    public static final Block KART_LIFT = registerBlock("kart_lift_block", new KartLiftCoreBlock(AbstractBlock.Settings.create().strength(3.0f).requiresTool().nonOpaque().pistonBehavior(PistonBehavior.BLOCK)));
    public static final Block KART_LIFT_DUMMY = registerBlockWithoutBlockItem("kart_lift_dummy_block", new KartLiftDummyBlock(AbstractBlock.Settings.create().strength(3.0f).requiresTool().nonOpaque().pistonBehavior(PistonBehavior.BLOCK)));




    // helper methods

    private static Block registerBlockWithoutBlockItem(String name, Block block) {
        return Registry.register(Registries.BLOCK, BoKarts.id(name), block);
    }

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, BoKarts.id(name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, BoKarts.id(name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void register() {
        BoKarts.LOGGER.info("Registering ModBlocks for " + BoKarts.MOD_ID);
    }
}
