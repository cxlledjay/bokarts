package de.cxlledjay.bokarts.item.custom;

import de.cxlledjay.bokarts.component.ModDataComponentTypes;
import de.cxlledjay.bokarts.entity.custom.kart.property.BodyType;
import de.cxlledjay.bokarts.entity.custom.kart.property.EngineType;
import de.cxlledjay.bokarts.entity.custom.kart.property.WheelType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LegacyKartItemConverter extends Item {

    private final Item newItem;
    private final String oldId;

    public LegacyKartItemConverter(Settings settings, Item newItem, String oldId) {
        super(settings);
        this.newItem = newItem;
        this.oldId =  oldId;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient() && entity instanceof PlayerEntity player) {
            ItemStack newStack = new ItemStack(this.newItem, stack.getCount());
            newStack.applyChanges(stack.getComponentChanges());

            // apply old paint job color system
            BodyType bodyType = switch (oldId) {
                case "fade" -> BodyType.LIVERY_FADE;
                default -> BodyType.SOLID_COLOR_OVERLAY;
            };

            int bodyColor = switch (oldId) {
                case "orange" -> 0xF9801D;
                case "magenta" -> 0xC74EBD;
                case "light_blue" -> 0x3AB3DA;
                case "yellow" -> 0xFED83D;
                case "lime" -> 0x80C71F;
                case "pink" -> 0xF38BAA;
                case "gray" -> 0x474F52;
                case "light_gray" -> 0x9D9D97;
                case "cyan" -> 0x169C9C;
                case "purple" -> 0x8932B8;
                case "blue" -> 0x3C44AA;
                case "brown" -> 0x835432;
                case "green" -> 0x5E7C16;
                case "red" -> 0xB02E26;
                case "black" -> 0x1D1D21;
                case "purple_gold" -> 0x70569C;
                case "default" -> 0xA086D3;
                default -> 0xFFFFFF;
            };

            int wheelColor = switch (oldId) {
                case "purple_gold" -> 0xC0996F;
                case "fade" -> 0xA43148;
                default -> 0xFFFFFF;
            };

            WheelType wheelType = WheelType.DRIFT;
            EngineType engineType = EngineType.ENGINE_GOLD;

            newStack.set(ModDataComponentTypes.KART_ITEM_BODY_TYPE, bodyType);
            newStack.set(ModDataComponentTypes.KART_ITEM_BODY_COLOR, bodyColor);
            newStack.set(ModDataComponentTypes.KART_ITEM_WHEEL_COLOR, wheelColor);
            newStack.set(ModDataComponentTypes.KART_ITEM_WHEEL_TYPE, wheelType);
            newStack.set(ModDataComponentTypes.KART_ITEM_ENGINE_TYPE, engineType);

            player.getInventory().setStack(slot, newStack);
        }
    }
}
