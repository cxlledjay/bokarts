package de.cxlledjay.bokarts.item.custom;

import de.cxlledjay.bokarts.component.ModDataComponentTypes;
import de.cxlledjay.bokarts.config.ClientSyncedConfig;
import de.cxlledjay.bokarts.entity.custom.kart.KartEntity;
import de.cxlledjay.bokarts.entity.custom.kart.property.*;
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
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class KartItemv2 extends Item {

    private static final Predicate<Entity> PREDICATE_RIDERS = EntityPredicates.EXCEPT_SPECTATOR.and(Entity::canHit);

    public KartItemv2(Settings settings) {
        super(settings);

        // default data tracking
        getDefaultStack().set(ModDataComponentTypes.KART_ITEM_HORN_TYPE, HornType.HORN1);
        getDefaultStack().set(ModDataComponentTypes.KART_ITEM_FUEL, 0.0f);
        getDefaultStack().set(ModDataComponentTypes.KART_ITEM_ODOMETER, 0.0f);

        // v2 tracking
        getDefaultStack().set(ModDataComponentTypes.KART_ITEM_BODY_TYPE, BodyType.SOLID_COLOR_OVERLAY);
        getDefaultStack().set(ModDataComponentTypes.KART_ITEM_WHEEL_TYPE, WheelType.NONE);
        getDefaultStack().set(ModDataComponentTypes.KART_ITEM_ENGINE_TYPE, EngineType.ENGINE_NONE);
        getDefaultStack().set(ModDataComponentTypes.KART_ITEM_SPOILER_TYPE, SpoilerType.NONE);
        getDefaultStack().set(ModDataComponentTypes.KART_ITEM_AERO_TYPE, AeroType.NONE);
        getDefaultStack().set(ModDataComponentTypes.KART_ITEM_BODY_COLOR, 0xFFFFFF);
        getDefaultStack().set(ModDataComponentTypes.KART_ITEM_WHEEL_COLOR, 0xFFFFFF);
        getDefaultStack().set(ModDataComponentTypes.KART_ITEM_RACE_NUMBER, 0);
    }

    // ==================== item appearance ====================

    public static Item.Settings defaultKartSettings() {
        return new Item.Settings()
                .maxCount(1)
                .fireproof()
                .rarity(Rarity.UNCOMMON)
                // Initialize default stats directly onto the base item
                .component(ModDataComponentTypes.KART_ITEM_HORN_TYPE, HornType.HORN1)
                .component(ModDataComponentTypes.KART_ITEM_FUEL, 0.0f)
                .component(ModDataComponentTypes.KART_ITEM_ODOMETER, 0.0f)
                // v2
                .component(ModDataComponentTypes.KART_ITEM_BODY_TYPE, BodyType.SOLID_COLOR_OVERLAY)
                .component(ModDataComponentTypes.KART_ITEM_WHEEL_TYPE, WheelType.NONE)
                .component(ModDataComponentTypes.KART_ITEM_ENGINE_TYPE, EngineType.ENGINE_NONE)
                .component(ModDataComponentTypes.KART_ITEM_SPOILER_TYPE, SpoilerType.NONE)
                .component(ModDataComponentTypes.KART_ITEM_AERO_TYPE, AeroType.NONE)
                .component(ModDataComponentTypes.KART_ITEM_BODY_COLOR, 0xFFFFFF)
                .component(ModDataComponentTypes.KART_ITEM_WHEEL_COLOR, 0xFFFFFF)
                .component(ModDataComponentTypes.KART_ITEM_RACE_NUMBER, 0);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {

        // default tooltip
        if(!Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("tooltip.bokarts.kart_item.shift"));
        }

        // shift pressed tooltip
        else {

            // ----- banner + livery info -----
            tooltip.add(Text.translatable("tooltip.bokarts.kart_item.shift_heading"));
            // livery + opt color
            BodyType bodyType = stack.getOrDefault(ModDataComponentTypes.KART_ITEM_BODY_TYPE, BodyType.SOLID_COLOR_OVERLAY);
            int bodyColor = stack.getOrDefault(ModDataComponentTypes.KART_ITEM_BODY_COLOR, 0xFFFFFF);

            if (bodyType != null) {
                tooltip.add(Text.translatable("tooltip.bokarts.kart_item.shift1_livery")
                        .append(getFormattedLiveryName(bodyType, bodyColor)));
            }
            tooltip.add(Text.translatable("tooltip.bokarts.kart_item.shift_heading2"));




            // ----- tuning info -----
            tooltip.add(Text.translatable("tooltip.bokarts.kart_item.shift_tuning"));

            // wheels, + opt color
            WheelType wheelType = stack.getOrDefault(ModDataComponentTypes.KART_ITEM_WHEEL_TYPE, WheelType.NONE);
            int wheelColor = stack.getOrDefault(ModDataComponentTypes.KART_ITEM_WHEEL_COLOR,  0xFFFFFF);

            if (wheelType != null) {

                MutableText text = Text.translatable("tooltip.bokarts.kart_item.shift2_wheels")
                        .append(Text.literal(wheelType.getDisplayName()));
                // add color
                if (wheelType != WheelType.NONE) {
                    text
                            .append(Text.literal(" (").formatted(Formatting.WHITE))
                            .append(getColoredColorString(wheelColor))
                            .append(Text.literal(")").formatted(Formatting.WHITE));
                }
                tooltip.add(text);
            }

            // engine
            EngineType engineType = stack.getOrDefault(ModDataComponentTypes.KART_ITEM_ENGINE_TYPE,  EngineType.ENGINE_NONE);
            if (engineType != null) {
                String s = Text.translatable("tooltip.bokarts.kart_item.shift2_engine").getString()
                        + engineType.getDisplayName();
                tooltip.add(Text.literal(s));
            }

            // spoiler
            SpoilerType spoilerType = stack.getOrDefault(ModDataComponentTypes.KART_ITEM_SPOILER_TYPE, SpoilerType.NONE);
            if ((spoilerType != null) && (spoilerType != SpoilerType.NONE)) {
                String s = Text.translatable("tooltip.bokarts.kart_item.shift2_spoiler").getString()
                        + spoilerType.getDisplayName();
                tooltip.add(Text.literal(s));
            }

            // aero
            AeroType aeroType = stack.getOrDefault(ModDataComponentTypes.KART_ITEM_AERO_TYPE, AeroType.NONE);
            if ((aeroType != null) && (aeroType != AeroType.NONE)) {
                String s = Text.translatable("tooltip.bokarts.kart_item.shift2_aero").getString()
                        + aeroType.getDisplayName();
                tooltip.add(Text.literal(s));
            }

            // race number
            int raceNumber = stack.getOrDefault(ModDataComponentTypes.KART_ITEM_RACE_NUMBER, 0);
            if (KartEntity.isValidRaceNumber(raceNumber)) {
                String s = Text.translatable("tooltip.bokarts.kart_item.shift2_race_number").getString()
                        + String.format("%02d", raceNumber);
                tooltip.add(Text.literal(s));
            }




            // ----- kart stats -----
            tooltip.add(Text.translatable("tooltip.bokarts.kart_item.shift_stats"));

            // horn
            HornType hornType = stack.getOrDefault(ModDataComponentTypes.KART_ITEM_HORN_TYPE, HornType.HORN1);
            if (hornType != null) {
                String s = Text.translatable("tooltip.bokarts.kart_item.shift3_horn").getString()
                        + KartEntity.getFormattedHornName(hornType);
                tooltip.add(Text.literal(s));
            }

            // fuel
            float fuel = stack.getOrDefault(ModDataComponentTypes.KART_ITEM_FUEL, 0.0f);
            float percentage = fuel / ClientSyncedConfig.getMaxFuelCapacity();
            String color = "§c"; //< empty color
            if (percentage > 0.5f) {
                // full color
                color = "§a";
            } else if (percentage > 0.2f) {
                // half full color
                color = "§6";
            }

            String s1 = Text.translatable("tooltip.bokarts.kart_item.shift3_fuel").getString()
                    + KartEntity.getFormattedFuelCapacityString(fuel)
                    + color + " (" +String.format("%2d", ((int) (percentage * 100))) + "%)";
            tooltip.add(Text.literal(s1));
            // odo
            float odo = stack.getOrDefault(ModDataComponentTypes.KART_ITEM_ODOMETER, 0.0f);
            String s2 = Text.translatable("tooltip.bokarts.kart_item.shift3_odo").getString()
                    + KartEntity.getFormattedDistanceString(odo);
            tooltip.add(Text.literal(s2));

            tooltip.add(Text.translatable("tooltip.bokarts.kart_item.shift_end"));

        }

        super.appendTooltip(stack, context, tooltip, type);
    }


    public static MutableText getFormattedLiveryName(BodyType bodyType, int bodyColor) {
        // get name
        MutableText result = Text.literal(bodyType.getDisplayName()).formatted(Formatting.WHITE);

        // get color, if applicable
        if(bodyType != BodyType.NONE) {
            result
                    .append(Text.literal(" (").formatted(Formatting.WHITE))
                    .append(getColoredColorString(bodyColor))
                    .append(Text.literal(")").formatted(Formatting.WHITE));
        }

        return result;
    }

    private static MutableText getColoredColorString (int color) {
        String colorAsHex = "#" + String.format("%06x", (color & 0xFFFFFF)).toUpperCase();
        return Text.literal(colorAsHex).withColor(color);
    }


    // ==================== spawning KartEntity ====================

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        // raytracing
        ItemStack itemStack = user.getStackInHand(hand);
        HitResult hitResult = raycast(world, user, RaycastContext.FluidHandling.NONE);
        if (hitResult.getType() == HitResult.Type.MISS) {
            return TypedActionResult.pass(itemStack);
        }

        // check collision
        Vec3d vec3d = user.getRotationVec(1.0f);
        List<Entity> list = world.getOtherEntities(user, user.getBoundingBox().stretch(vec3d.multiply(5.0)).expand(1.0), PREDICATE_RIDERS);
        if (!list.isEmpty()) {
            Vec3d vec3d2 = user.getEyePos();

            for (Entity entity : list) {
                Box box = entity.getBoundingBox().expand(entity.getTargetingMargin());
                if (box.contains(vec3d2)) {
                    return TypedActionResult.pass(itemStack);
                }
            }
        }

        // check kart
        EngineType engineType = itemStack.getOrDefault(ModDataComponentTypes.KART_ITEM_ENGINE_TYPE, EngineType.ENGINE_NONE);
        if(engineType == EngineType.ENGINE_NONE) {
            if (!world.isClient) user.sendMessage(Text.translatable("message.bokarts.kart_item.use_no_engine"));
            return TypedActionResult.pass(itemStack);
        }
        WheelType wheelType = itemStack.getOrDefault(ModDataComponentTypes.KART_ITEM_WHEEL_TYPE, WheelType.NONE);
        if(wheelType == WheelType.NONE) {
            if (!world.isClient) user.sendMessage(Text.translatable("message.bokarts.kart_item.use_no_wheels"));
            return TypedActionResult.pass(itemStack);
        }

        // create kart
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            KartEntity kart = this.createEntity(world, hitResult, itemStack, user);
            kart.setYaw(user.getYaw());
            if (!world.isSpaceEmpty(kart, kart.getBoundingBox())) {
                return TypedActionResult.fail(itemStack);
            }

            applyDataFromItemStack(kart, itemStack);

            // spawn kart
            if (!world.isClient) {
                kart.setCustomName(null);
                world.spawnEntity(kart);
                world.emitGameEvent(user, GameEvent.ENTITY_PLACE, hitResult.getPos());
                itemStack.decrementUnlessCreative(1, user);
            }

            // stats
            user.incrementStat(Stats.USED.getOrCreateStat(this));

            return TypedActionResult.success(itemStack, world.isClient());
        } else {
            return TypedActionResult.pass(itemStack);
        }
    }

    // apply data components to tracked data of entity
    public static void applyDataFromItemStack(KartEntity kart, ItemStack itemStack) {
        // add tracked data to kart
        kart.setHornType(itemStack.getOrDefault(ModDataComponentTypes.KART_ITEM_HORN_TYPE, HornType.HORN1));
        kart.setFuelSynced(itemStack.getOrDefault(ModDataComponentTypes.KART_ITEM_FUEL, 0.0f));
        kart.setOdometerSynced(itemStack.getOrDefault(ModDataComponentTypes.KART_ITEM_ODOMETER, 0.0f));

        // v2 tracked data
        kart.setBodyType(itemStack.getOrDefault(ModDataComponentTypes.KART_ITEM_BODY_TYPE, BodyType.NONE));
        kart.setWheelType(itemStack.getOrDefault(ModDataComponentTypes.KART_ITEM_WHEEL_TYPE, WheelType.NONE));
        kart.setEngineType(itemStack.getOrDefault(ModDataComponentTypes.KART_ITEM_ENGINE_TYPE, EngineType.ENGINE_NONE));
        kart.setSpoilerType(itemStack.getOrDefault(ModDataComponentTypes.KART_ITEM_SPOILER_TYPE, SpoilerType.NONE));
        kart.setAeroType(itemStack.getOrDefault(ModDataComponentTypes.KART_ITEM_AERO_TYPE, AeroType.NONE));
        kart.setBodyColor(itemStack.getOrDefault(ModDataComponentTypes.KART_ITEM_BODY_COLOR, 0xFFFFFF));
        kart.setWheelColor(itemStack.getOrDefault(ModDataComponentTypes.KART_ITEM_WHEEL_COLOR, 0xFFFFFF));
        kart.setRaceNumber(itemStack.getOrDefault(ModDataComponentTypes.KART_ITEM_RACE_NUMBER, 0));

        // key data
        kart.setKeyInIgnition(itemStack.getOrDefault(ModDataComponentTypes.KART_ITEM_HAS_KEY, false));
        kart.setKeyUuid(itemStack.getOrDefault(ModDataComponentTypes.KART_KEY_PAIRED_UUID, null));
    }

    // create entity helper
    private KartEntity createEntity(World world, HitResult hitResult, ItemStack stack, PlayerEntity player) {
        Vec3d vec3d = hitResult.getPos();
        KartEntity kart = new KartEntity(world, vec3d.x, vec3d.y, vec3d.z);
        if (world instanceof ServerWorld serverWorld) {
            EntityType.copier(serverWorld, stack, player).accept(kart);
        }
        return kart;
    }


}
