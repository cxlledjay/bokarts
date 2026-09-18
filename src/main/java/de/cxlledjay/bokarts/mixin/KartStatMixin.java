package de.cxlledjay.bokarts.mixin;

import de.cxlledjay.bokarts.entity.custom.KartEntity;
import de.cxlledjay.bokarts.stats.ModStats;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ServerPlayerEntity.class)
public abstract class KartStatMixin {

    @Inject(method = "increaseRidingMotionStats", at = @At("HEAD"), cancellable = true)
    private void onIncreaseTravelMotionStats(double dx, double dy, double dz, CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

        if (player.getVehicle() instanceof KartEntity) {
            int distance = Math.round((float) Math.sqrt(dx * dx + dy * dy + dz * dz) * 100.0F);
            if (distance > 0) {
                player.increaseStat(ModStats.KART_ONE_CM, distance);
            }
            ci.cancel();
        }
    }
}