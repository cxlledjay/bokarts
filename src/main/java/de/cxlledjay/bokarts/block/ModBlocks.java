package de.cxlledjay.bokarts.block;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.block.custom.*;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;

import java.util.List;

public class ModBlocks {

    // kart lift
    public static final Block KART_LIFT = registerBlock("kart_lift_block", new KartLiftCoreBlock(AbstractBlock.Settings.create().strength(3.0f).requiresTool().nonOpaque().pistonBehavior(PistonBehavior.BLOCK)) {
        @Override
        public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
            tooltip.add(Text.translatable("tooltip.bokarts.kart_lift_block.crafting"));
            tooltip.add(Text.translatable("tooltip.bokarts.kart_lift_block.setup"));
            super.appendTooltip(stack, context, tooltip, options);
        }
    });
    public static final Block KART_LIFT_DUMMY = registerBlockWithoutBlockItem("kart_lift_dummy_block", new KartLiftDummyBlock(AbstractBlock.Settings.create().strength(3.0f).requiresTool().nonOpaque().pistonBehavior(PistonBehavior.BLOCK)));


    // workbenches
    public static final Block LIVERY_WORKBENCH = registerBlock("livery_workbench", new LiveryWorkbench(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BRIGHT_RED)
                    .sounds(BlockSoundGroup.METAL)
                    .strength(4.0f)
                    .requiresTool()
                    .nonOpaque()
                    .pistonBehavior(PistonBehavior.BLOCK)
    ));
    public static final Block PAINT_BUCKET = registerBlock("paint_bucket", new PaintBucket(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.GRAY)
                    .sounds(BlockSoundGroup.METAL)
                    .strength(2.5f)
                    .requiresTool()
                    .nonOpaque()
                    .pistonBehavior(PistonBehavior.BLOCK)
    ));

    // trophy
    public static final Block TROPHY = registerBlock("trophy", new Trophy(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.GOLD)
                    .sounds(BlockSoundGroup.METAL)
                    .strength(1.5f)
                    .requiresTool()
                    .nonOpaque()
                    .pistonBehavior(PistonBehavior.BLOCK)
    ));

    // silly fun
    public static final Block HORN_BLOCK = registerBlock("horn_block", new HornBlock(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .sounds(BlockSoundGroup.WOOD)
                    .strength(0.8F)
                    .burnable()
    ));

    // racing weekend blocks
    public static final Block TRACK_TIMER_BLOCK = registerBlock("track_timer_block", new TrackTimer(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DEEPSLATE_GRAY)
                    .sounds(BlockSoundGroup.TRIAL_SPAWNER)
                    .strength(2.5f)
                    .requiresTool()
                    .pistonBehavior(PistonBehavior.BLOCK)
    ));
    public static final Block TRACK_TIMER_REFLECTOR_BLOCK = registerBlock("track_timer_reflector_block", new TrackTimerReflector(
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DEEPSLATE_GRAY)
                    .sounds(BlockSoundGroup.TRIAL_SPAWNER)
                    .strength(2.5f)
                    .requiresTool()
                    .pistonBehavior(PistonBehavior.BLOCK)
    ));




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
