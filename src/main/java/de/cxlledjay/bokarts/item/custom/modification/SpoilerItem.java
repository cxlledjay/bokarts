package de.cxlledjay.bokarts.item.custom.modification;

import de.cxlledjay.bokarts.entity.custom.kart.property.SpoilerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class SpoilerItem extends Item {

    private final SpoilerType spoiler;

    public SpoilerItem(SpoilerType spoiler, Settings settings) {
        super(settings);
        this.spoiler = spoiler;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {

        float multiA = spoiler.getMultiA();
        float multiV = spoiler.getMultiV();
        float multiF = spoiler.getMultiF();

        Text multiAText = Text.literal("Acceleration: ").formatted(Formatting.GRAY)
                .append(Text.literal(((multiA >= 0) ? ('+') : "") + String.format("%.1f", multiA * 100) + "%")
                .formatted((multiA >= 0) ? (Formatting.GREEN) : (Formatting.RED)));
        Text multiVText = Text.literal("Top Speed: ").formatted(Formatting.GRAY)
                .append(Text.literal(((multiV >= 0) ? ('+') : "") + String.format("%.1f", multiV * 100) + "%")
                .formatted((multiV >= 0) ? (Formatting.GREEN) : (Formatting.RED)));
        Text multiFText = Text.literal("Grip: ").formatted(Formatting.GRAY)
                .append(Text.literal(((multiF >= 0) ? ('+') : "") + String.format("%.1f", multiF * 100) + "%")
                .formatted((multiF >= 0) ? (Formatting.GREEN) : (Formatting.RED)));

        tooltip.add(multiAText);
        tooltip.add(multiVText);
        tooltip.add(multiFText);

        super.appendTooltip(stack, context, tooltip, type);
    }

    public SpoilerType getSpoilerType() {
        return spoiler;
    }
}
