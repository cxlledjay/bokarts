package de.cxlledjay.bokarts.item.custom.modification;

import de.cxlledjay.bokarts.entity.custom.kart.property.WheelType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class WheelsItem extends Item {

    private final WheelType wheels;

    public WheelsItem(WheelType wheels, Settings settings) {
        super(settings);
        this.wheels = wheels;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {

        float baseF = wheels.getBaseF();
        float penaltyF = wheels.getPenalityF();
        float multiA = wheels.getMultiA();
        float multiV = wheels.getMultiV();

        Text baseFText = Text.literal("Grip: ").formatted(Formatting.GRAY)
                .append(Text.literal(String.format("%.2f", baseF * 20) + " µ").formatted(Formatting.DARK_AQUA));
        Text penaltyFText = Text.literal("Grip Penalty: ").formatted(Formatting.GRAY)
                .append(Text.literal(String.format("%.2f", penaltyF * 20) + " µ").formatted(Formatting.DARK_AQUA));

        Text multiAText = Text.literal("Acceleration: ").formatted(Formatting.GRAY)
                .append(Text.literal(((multiA >= 0) ? ('+') : "") + String.format("%.1f", multiA * 100) + "%")
                .formatted((multiA >= 0) ? (Formatting.GREEN) : (Formatting.RED)));
        Text multiVText = Text.literal("Top Speed: ").formatted(Formatting.GRAY)
                .append(Text.literal(((multiV >= 0) ? ('+') : "") + String.format("%.1f", multiV * 100) + "%")
                .formatted((multiV >= 0) ? (Formatting.GREEN) : (Formatting.RED)));

        tooltip.add(baseFText);
        tooltip.add(penaltyFText);
        tooltip.add(multiAText);
        tooltip.add(multiVText);

        super.appendTooltip(stack, context, tooltip, type);
    }
}
