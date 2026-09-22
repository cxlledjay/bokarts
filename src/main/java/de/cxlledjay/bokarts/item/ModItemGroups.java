package de.cxlledjay.bokarts.item;

import de.cxlledjay.bokarts.BoKarts;
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
                        // crafting ingredients
                        entries.add(new ItemStack(ModItems.KART_CRAFTING_SEAT));
                        entries.add(new ItemStack(ModItems.KART_CRAFTING_WHEEL));
                        entries.add(new ItemStack(ModItems.KART_CRAFTING_STEERING_WHEEL));
                        entries.add(new ItemStack(ModItems.KART_KEY));
                        // vanilla dye colors
                        entries.add(new ItemStack(ModItems.KART_WHITE));
                        entries.add(new ItemStack(ModItems.KART_LIGHT_GRAY));
                        entries.add(new ItemStack(ModItems.KART_GRAY));
                        entries.add(new ItemStack(ModItems.KART_BLACK));
                        entries.add(new ItemStack(ModItems.KART_BROWN));
                        entries.add(new ItemStack(ModItems.KART_RED));
                        entries.add(new ItemStack(ModItems.KART_ORANGE));
                        entries.add(new ItemStack(ModItems.KART_YELLOW));
                        entries.add(new ItemStack(ModItems.KART_LIME));
                        entries.add(new ItemStack(ModItems.KART_GREEN));
                        entries.add(new ItemStack(ModItems.KART_CYAN));
                        entries.add(new ItemStack(ModItems.KART_LIGHT_BLUE));
                        entries.add(new ItemStack(ModItems.KART_BLUE));
                        entries.add(new ItemStack(ModItems.KART_PURPLE));
                        entries.add(new ItemStack(ModItems.KART_MAGENTA));
                        entries.add(new ItemStack(ModItems.KART_PINK));
                        // special variants
                        entries.add(new ItemStack(ModItems.KART_DEFAULT));
                        entries.add(new ItemStack(ModItems.KART_PURPLE_GOLD));
                        entries.add(new ItemStack(ModItems.KART_FADE));
                    }).build());

    public static void registerItemGroups() {
        BoKarts.LOGGER.info("Registering ModItemGroups for " + BoKarts.MOD_ID);
    }
}
