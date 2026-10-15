package de.cxlledjay.bokarts.item.custom;

import de.cxlledjay.bokarts.component.ModDataComponentTypes;
import de.cxlledjay.bokarts.config.ClientSyncedConfig;
import de.cxlledjay.bokarts.entity.custom.kart.KartEntity;
import de.cxlledjay.bokarts.entity.custom.kart.property.HornType;
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

    public KartItem(Item.Settings settings) {
        super(settings);

        // default data tracking
        getDefaultStack().set(ModDataComponentTypes.KART_ITEM_HORN_TYPE, HornType.HORN1);
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
                .component(ModDataComponentTypes.KART_ITEM_HORN_TYPE, HornType.HORN1);
    }
}