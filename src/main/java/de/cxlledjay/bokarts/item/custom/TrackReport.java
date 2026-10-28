package de.cxlledjay.bokarts.item.custom;

import de.cxlledjay.bokarts.component.ModDataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class TrackReport extends Item {

    public TrackReport(Settings settings) {
        super(settings);
    }

    public static int getPlacementColor(int placement) {
        return switch(placement) {
            case 1 -> 0xD4AF37;
            case 2 -> 0xC0C0C0;
            case 3 -> 0xCD7F32;
            default -> 0xFFFFFF;
        };
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {


        if(stack.contains(ModDataComponentTypes.TRACK_REPORT_DATE)) {
            String s = "Date: " + stack.get(ModDataComponentTypes.TRACK_REPORT_DATE);
            tooltip.add(Text.literal(s).formatted(Formatting.GRAY));
        }

        if(stack.contains(ModDataComponentTypes.TRACK_REPORT_PLAYER_NAME)) {
            String s = "Driver: " + stack.get(ModDataComponentTypes.TRACK_REPORT_PLAYER_NAME);

            if(stack.contains(ModDataComponentTypes.KART_ITEM_RACE_NUMBER)) {
                s += ", Kart: " + stack.get(ModDataComponentTypes.KART_ITEM_RACE_NUMBER);
            }

            tooltip.add(Text.literal(s).formatted(Formatting.GRAY));
        }

        if(stack.contains(ModDataComponentTypes.TRACK_REPORT_TOTAL_LAPS)) {
            String s = "Total Laps: " + stack.get(ModDataComponentTypes.TRACK_REPORT_TOTAL_LAPS);
            tooltip.add(Text.literal(s).formatted(Formatting.GRAY));
        }

        if(stack.contains(ModDataComponentTypes.TRACK_REPORT_DURATION)) {
            String s = "Duration: " + stack.get(ModDataComponentTypes.TRACK_REPORT_DURATION);
            tooltip.add(Text.literal(s).formatted(Formatting.GRAY));
        }

        if(stack.contains(ModDataComponentTypes.TRACK_REPORT_PLACEMENT)) {
            int placement = stack.getOrDefault(ModDataComponentTypes.TRACK_REPORT_PLACEMENT, -1);
            String s = "Placement: #" + placement;
            int color = getPlacementColor(placement);

            tooltip.add(Text.literal(s).setStyle(Style.EMPTY.withColor(color)));
        }

        if(stack.contains(ModDataComponentTypes.TRACK_REPORT_BEST_LAP_TIME)) {
            tooltip.add(
                    Text.literal("Best Lap: ").formatted(Formatting.WHITE).append(
                    Text.literal(stack.getOrDefault(ModDataComponentTypes.TRACK_REPORT_BEST_LAP_TIME, "N/A")).formatted(Formatting.LIGHT_PURPLE))
            );
        }

        super.appendTooltip(stack, context, tooltip, type);
    }
}
