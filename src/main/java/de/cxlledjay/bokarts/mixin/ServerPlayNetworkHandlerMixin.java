package de.cxlledjay.bokarts.mixin;

import de.cxlledjay.bokarts.entity.custom.KartEntity;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {

    // We shadow the player so we can check what they are driving
    @Shadow public ServerPlayerEntity player;

    @ModifyConstant(
            method = "onVehicleMove",
            // There are three 0.0625 constants in this method.
            // We use @Slice to target only the one between move() and updatePositionAndAngles()
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;updatePositionAndAngles(DDDFF)V")
            ),
            constant = @Constant(doubleValue = 0.0625)
    )
    private double disableKartMovedWrongly(double originalThreshold) {
        // Check if the vehicle the player is in is your Kart
        Entity vehicle = this.player.getRootVehicle();

        if (vehicle instanceof KartEntity) {
            return 4d;
        }

        // If they are driving a normal boat or pig, keep the vanilla strict rubberband rules
        return originalThreshold;
    }
}