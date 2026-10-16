package de.cxlledjay.bokarts.item;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

public class ModItemGroups {

    public static final ItemGroup BOKART_ITEM_GROUP = Registry.register(
            Registries.ITEM_GROUP,
            BoKarts.id("bokart_items"),
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(ModItems.STEERING_WHEEL))
                    .displayName(Text.translatable("itemgroup.bokarts.bokart_items"))
                    .entries((displayContext, entries) -> {

                        // the kart
                        entries.add(new ItemStack(ModItems.KART_ITEM));

                        // ----- crafting ingredients -----
                        // chassis
                        entries.add(new ItemStack(ModItems.CHASSIS_BLUEPRINT));
                        entries.add(new ItemStack(ModItems.STEERING_WHEEL));
                        entries.add(new ItemStack(ModItems.SEAT));
                        // wheels
                        entries.add(new ItemStack(ModItems.WHEELS_STREET));
                        entries.add(new ItemStack(ModItems.WHEELS_DRIFT));
                        entries.add(new ItemStack(ModItems.WHEELS_OFFROAD));
                        // engines
                        entries.add(new ItemStack(ModItems.ENGINE_COPPER));
                        entries.add(new ItemStack(ModItems.ENGINE_IRON));
                        entries.add(new ItemStack(ModItems.ENGINE_GOLD));
                        entries.add(new ItemStack(ModItems.ENGINE_DIAMOND));
                        entries.add(new ItemStack(ModItems.ENGINE_NETHERITE));
                        // spoilers
                        entries.add(new ItemStack(ModItems.SPOILER_STREET));
                        entries.add(new ItemStack(ModItems.SPOILER_STREET_CARBON));
                        entries.add(new ItemStack(ModItems.SPOILER_RACE));
                        entries.add(new ItemStack(ModItems.SPOILER_RACE_CARBON));
                        // aero
                        entries.add(new ItemStack(ModItems.AERO_RACE));
                        entries.add(new ItemStack(ModItems.AERO_RACE_CARBON));


                        // ----- customization -----
                        // livery
                        entries.add(new ItemStack(ModItems.LIVERY_TEMPLATE));

                        // ----- accessories -----
                        entries.add(new ItemStack(ModItems.KEY));

                        // blocks
                        entries.add(new ItemStack(ModBlocks.KART_LIFT));
                    }).build());

    public static void registerItemGroups() {
        BoKarts.LOGGER.info("Registering ModItemGroups for " + BoKarts.MOD_ID);
    }
}
