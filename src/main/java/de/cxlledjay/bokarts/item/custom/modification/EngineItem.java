package de.cxlledjay.bokarts.item.custom.modification;

import de.cxlledjay.bokarts.entity.custom.kart.property.EngineType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class EngineItem extends Item {

    private final EngineType engine;

    public EngineItem(EngineType engine, Settings settings) {
        super(settings);
        this.engine = engine;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {

        float engineA = engine.getbaseA();
        float engineV = engine.getbaseV();
        float fuelMulti = engine.getFuelMulti();

        Text baseA = Text.literal("Acceleration: ").formatted(Formatting.GRAY)
                .append(Text.literal(String.format("%.2f", engineA * 400) + " b/s²").formatted(Formatting.DARK_AQUA));
        Text baseV = Text.literal("Top Speed: ").formatted(Formatting.GRAY)
                .append(Text.literal(String.format("%.2f", engineV * 20) + " bps").formatted(Formatting.DARK_AQUA));
        Text fuelMultiText = Text.literal("Fuel Efficiency: ").formatted(Formatting.GRAY)
                .append(Text.literal(String.format("%.1f", (1 / fuelMulti) * 100) + "%")
                        .formatted((fuelMulti <= 1.0f) ? (Formatting.GREEN) : (Formatting.RED)));

        tooltip.add(baseA);
        tooltip.add(baseV);
        tooltip.add(fuelMultiText);

        super.appendTooltip(stack, context, tooltip, type);
    }

    public EngineType getEngineType() {
        return engine;
    }
}
