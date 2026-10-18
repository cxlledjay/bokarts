package de.cxlledjay.bokarts.item.custom;

import de.cxlledjay.bokarts.component.ModDataComponentTypes;
import de.cxlledjay.bokarts.entity.custom.kart.KartEntity;
import de.cxlledjay.bokarts.item.ModItems;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public class KartKey extends Item {

    public KartKey(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        // get key
        ItemStack pairedKey = user.getStackInHand(hand);

        // run on server only
        if(!world.isClient()) {

            // get key uuid
            UUID keyUuid = pairedKey.getOrDefault(ModDataComponentTypes.KART_KEY_PAIRED_UUID, null);

            if(keyUuid != null) {
                // search in two chunks radius
                Box searchBox = user.getBoundingBox().expand(32.0);

                List<KartEntity> karts = world.getEntitiesByClass(
                        KartEntity.class,
                        searchBox,
                        // match kart key uuid
                        kart -> keyUuid.equals(kart.getKeyUuid())
                );

                // found something!
                if(!karts.isEmpty()) {
                    KartEntity matchingKart = karts.getFirst();
                    matchingKart.triggerKeyFob();
                }
            }
        }
        return TypedActionResult.success(pairedKey, world.isClient());
    }


    // -------------------- appearance --------------------

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {

        String string = stack.getOrDefault(ModDataComponentTypes.KART_KEY_INFO, null);
        Text info = Text.translatable("tooltip.bokarts.kart_key.no_info");

        // default tooltip
        if(!Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("tooltip.bokarts.kart_key.shift"));
        }

        // info tooltip
        else {
            if(string != null) {
                info = Text.literal(string);
            }
            tooltip.add(info);
        }

        super.appendTooltip(stack, context, tooltip, type);
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
