package de.cxlledjay.bokarts.item;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
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
                    .icon(() -> new ItemStack(ModItems.KART_CRAFTING_WHEEL))
                    .displayName(Text.translatable("itemgroup.bokarts.bokart_items"))
                    .entries((displayContext, entries) -> {

                        // the kart
                        entries.add(new ItemStack(ModItems.KART_ITEM));

                        // crafting ingredients
                        entries.add(new ItemStack(ModItems.LIVERY_TEMPLATE));
                        entries.add(new ItemStack(ModItems.KART_CRAFTING_SEAT));
                        entries.add(new ItemStack(ModItems.KART_CRAFTING_WHEEL));
                        entries.add(new ItemStack(ModItems.KART_CRAFTING_STEERING_WHEEL));

                        // accessories
                        entries.add(new ItemStack(ModItems.KART_KEY));

                        // blocks
                        entries.add(new ItemStack(ModBlocks.KART_LIFT));
                    }).build());

    public static void registerItemGroups() {
        BoKarts.LOGGER.info("Registering ModItemGroups for " + BoKarts.MOD_ID);
    }
}
