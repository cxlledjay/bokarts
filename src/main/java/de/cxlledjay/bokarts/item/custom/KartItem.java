package de.cxlledjay.bokarts.item.custom;

import de.cxlledjay.bokarts.component.ModDataComponentTypes;
import de.cxlledjay.bokarts.entity.custom.KartEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import java.util.List;
import java.util.function.Predicate;

public class KartItem extends Item {

    private static final Predicate<Entity> RIDERS = EntityPredicates.EXCEPT_SPECTATOR.and(Entity::canHit);
    private final KartEntity.PaintColor paintColor;

    public KartItem(KartEntity.PaintColor paintColor, Item.Settings settings) {
        super(settings);
        this.paintColor = paintColor;

        // default data tracking
        getDefaultStack().set(ModDataComponentTypes.KART_ITEM_HORN_SOUND, "horn_1");
        getDefaultStack().set(ModDataComponentTypes.KART_ITEM_FUEL, 0.0f);
        getDefaultStack().set(ModDataComponentTypes.KART_ITEM_ODOMETER, 0.0f);
    }

    // ==================== item appearance ====================

    public static Item.Settings defaultKartSettings() {
        return new Item.Settings()
                .maxCount(1)
                // Initialize default stats directly onto the base item
                .component(ModDataComponentTypes.KART_ITEM_FUEL, 0.0f)
                .component(ModDataComponentTypes.KART_ITEM_ODOMETER, 0.0f)
                .component(ModDataComponentTypes.KART_ITEM_HORN_SOUND, "horn_1"); // Adjust type/value to match your horn component
    }


    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {

        if(Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("tooltip.bokarts.kart_item.details"));
            if(stack.get(ModDataComponentTypes.KART_ITEM_HORN_SOUND) != null) {
                String s = Text.translatable("tooltip.bokarts.kart_item.horn").getString()
                        + " : §5\"" + stack.get(ModDataComponentTypes.KART_ITEM_HORN_SOUND) + "\"";
                tooltip.add(Text.literal(s));
            }
            if(stack.get(ModDataComponentTypes.KART_ITEM_FUEL) != null) {
                float percentage = stack.get(ModDataComponentTypes.KART_ITEM_FUEL) / KartEntity.FUEL_TANK_MAX_CAPACITY;
                String color = "§c"; //< empty color
                if(percentage > 0.5f) {
                    // full color
                    color = "§a";
                } else if(percentage > 0.2f) {
                    // half full color
                    color = "§6";
                }

                String s = Text.translatable("tooltip.bokarts.kart_item.fuel").getString()
                        + " : " + color + KartEntity.getFormattedFuelCapacityString(stack.get(ModDataComponentTypes.KART_ITEM_FUEL));
                tooltip.add(Text.literal(s));
            }
            if(stack.get(ModDataComponentTypes.KART_ITEM_ODOMETER) != null) {
                String s = Text.translatable("tooltip.bokarts.kart_item.odometer").getString()
                        + " : §e" + KartEntity.getFormattedDistanceString(stack.get(ModDataComponentTypes.KART_ITEM_ODOMETER));
                tooltip.add(Text.literal(s));
            }
        } else {
            tooltip.add(Text.translatable("tooltip.bokarts.kart_item.shift"));
        }

        super.appendTooltip(stack, context, tooltip, type);
    }


    // ==================== spawning KartEntity ====================

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        HitResult hitResult = raycast(world, user, RaycastContext.FluidHandling.NONE);
        if (hitResult.getType() == HitResult.Type.MISS) {
            return TypedActionResult.pass(itemStack);
        }

        Vec3d vec3d = user.getRotationVec(1.0F);
        List<Entity> list = world.getOtherEntities(user, user.getBoundingBox().stretch(vec3d.multiply(5.0)).expand(1.0), RIDERS);
        if (!list.isEmpty()) {
            Vec3d vec3d2 = user.getEyePos();

            for (Entity entity : list) {
                Box box = entity.getBoundingBox().expand(entity.getTargetingMargin());
                if (box.contains(vec3d2)) {
                    return TypedActionResult.pass(itemStack);
                }
            }
        }

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            KartEntity kart = this.createEntity(world, hitResult, itemStack, user);
            kart.setPaintColor(this.paintColor);
            kart.setYaw(user.getYaw());
            if (!world.isSpaceEmpty(kart, kart.getBoundingBox())) {
                return TypedActionResult.fail(itemStack);
            }

            // add tracked data to kart
            kart.setHornSound(KartEntity.HornSounds.getHornSound(itemStack.getOrDefault(ModDataComponentTypes.KART_ITEM_HORN_SOUND, "horn_1")));
            kart.setFuelSynced(itemStack.getOrDefault(ModDataComponentTypes.KART_ITEM_FUEL, 0.0f));
            kart.setOdometerSynced(itemStack.getOrDefault(ModDataComponentTypes.KART_ITEM_ODOMETER, 0.0f));

            if (!world.isClient) {
                world.spawnEntity(kart);
                world.emitGameEvent(user, GameEvent.ENTITY_PLACE, hitResult.getPos());
                itemStack.decrementUnlessCreative(1, user);
            }

            user.incrementStat(Stats.USED.getOrCreateStat(this));
            return TypedActionResult.success(itemStack, world.isClient());
        } else {
            return TypedActionResult.pass(itemStack);
        }
    }

    private KartEntity createEntity(World world, HitResult hitResult, ItemStack stack, PlayerEntity player) {
        Vec3d vec3d = hitResult.getPos();
        KartEntity kart = new KartEntity(world, vec3d.x, vec3d.y, vec3d.z);
        if (world instanceof ServerWorld serverWorld) {
            EntityType.<KartEntity>copier(serverWorld, stack, player).accept(kart);
        }

        return kart;
    }
}