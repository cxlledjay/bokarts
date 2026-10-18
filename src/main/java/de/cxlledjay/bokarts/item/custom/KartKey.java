package de.cxlledjay.bokarts.item.custom;

import de.cxlledjay.bokarts.component.ModDataComponentTypes;
import de.cxlledjay.bokarts.entity.custom.kart.KartEntity;
import de.cxlledjay.bokarts.item.ModItems;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class KartKey extends Item {

    public KartKey(Settings settings) {
        super(settings);
    }


    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);

        String string = stack.getOrDefault(ModDataComponentTypes.KART_KEY_INFO, null);
        Text info = Text.translatable("tooltip.bokarts.kart_key.no_info");

        // default tooltip
        if(!Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("tooltip.bokarts.kart_key.shift"));
        }

        // info tooltip
        else {
            if(string != null) {
                info = Text.translatable("tooltip.bokarts.kart_key.info").append(string);
            }
            tooltip.add(info);
        }
    }


    public static ItemStack createPairedKey(UUID uuid, KartEntity kartEntity) {
        ItemStack pairedKey =  new ItemStack(ModItems.KEY_PAIRED);
        pairedKey.setCount(1);
        pairedKey.set(ModDataComponentTypes.KART_KEY_PAIRED_UUID, uuid);

        // add info string
        pairedKey.set(ModDataComponentTypes.KART_KEY_INFO, KartKey.createKartKeyInfoStringFromKart(kartEntity));

        return pairedKey;
    }


    private static String createKartKeyInfoStringFromKart (KartEntity kart) {
        // safety
        if(kart == null) return "";

        String info = "Kart: " + kart.getBodyType().getDisplayName() + " [#" + String.format("%06X", kart.getBodyColor() & 0xFFFFFF) + "]";
        if(KartEntity.isValidRaceNumber(kart.getRaceNumber())) {
            info = info +  " (" + kart.getRaceNumber() + ")";
        }
        return info;
    }
}
