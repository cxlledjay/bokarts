package de.cxlledjay.bokarts.item.custom;

import de.cxlledjay.bokarts.component.ModDataComponentTypes;
import de.cxlledjay.bokarts.config.ClientSyncedConfig;
import de.cxlledjay.bokarts.entity.custom.kart.KartEntity;
import de.cxlledjay.bokarts.entity.custom.kart.property.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;

import java.util.List;

public class LiveryTemplate extends Item {

    public LiveryTemplate(Settings settings) {
        super(settings);

        // livery tracks the livery/body type and both colors
        getDefaultStack().set(ModDataComponentTypes.KART_ITEM_BODY_TYPE, BodyType.NONE);
        getDefaultStack().set(ModDataComponentTypes.KART_ITEM_BODY_COLOR, 0xFFFFFF);
        getDefaultStack().set(ModDataComponentTypes.KART_ITEM_WHEEL_COLOR, 0xFFFFFF);
    }

    public static Item.Settings defaultLiverySettings() {
        return new Item.Settings()
                .maxCount(1)
                .component(ModDataComponentTypes.KART_ITEM_BODY_TYPE, BodyType.NONE)
                .component(ModDataComponentTypes.KART_ITEM_BODY_COLOR, 0xFFFFFF)
                .component(ModDataComponentTypes.KART_ITEM_WHEEL_COLOR, 0xFFFFFF);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        // default tooltip
        if(!Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("tooltip.bokarts.livery_template.shift"));
        }

        // shift pressed tooltip
        else {
            // retrieve data
            BodyType bodyType = stack.getOrDefault(ModDataComponentTypes.KART_ITEM_BODY_TYPE, BodyType.NONE);
            int bodyColor = stack.getOrDefault(ModDataComponentTypes.KART_ITEM_BODY_COLOR, 0xFFFFFF);
            int wheelColor = stack.getOrDefault(ModDataComponentTypes.KART_ITEM_WHEEL_COLOR, 0xFFFFFF);

            // build strings
            String bodyTypeString = Text.translatable("tooltip.bokarts.livery_template.body_type").getString()
                    + bodyType.getDisplayName();
            String bodyColorString = Text.translatable("tooltip.bokarts.livery_template.body_color").getString()
                    + String.format("#%06X", (bodyColor & 0xFFFFFF));
            String wheelColorString = Text.translatable("tooltip.bokarts.livery_template.wheel_color").getString()
                    + String.format("#%06X", (wheelColor & 0xFFFFFF));

            // add to tooltip
            tooltip.add(Text.literal(bodyTypeString));
            tooltip.add(Text.literal(bodyColorString));
            tooltip.add(Text.literal(wheelColorString));
        }

        super.appendTooltip(stack, context, tooltip, type);
    }
}
