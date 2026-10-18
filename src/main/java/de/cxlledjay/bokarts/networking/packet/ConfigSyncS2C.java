package de.cxlledjay.bokarts.networking.packet;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.config.ClientSyncedConfig;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public record ConfigSyncS2C(
        float maxFuelCapacity,
        float fuelConsumptionPerTick,
        float fuelTimeToRangeConversion,
        int maxTicksInWater,
        float slipperinessDriveable,
        float slipperinessNonDriveable,
        float slipperinessSlowed,
        float slipperinessNoPassenger
) implements CustomPayload {

    public static final CustomPayload.Id<ConfigSyncS2C> ID = new CustomPayload.Id<>(BoKarts.id("config_sync"));

    public static final PacketCodec<RegistryByteBuf, ConfigSyncS2C> STREAM_CODEC = PacketCodec.of(
            ConfigSyncS2C::write,
            ConfigSyncS2C::read
    );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }

    private void write(RegistryByteBuf buf) {
        buf.writeFloat(this.maxFuelCapacity);
        buf.writeFloat(this.fuelConsumptionPerTick);
        buf.writeFloat(this.fuelTimeToRangeConversion);
        buf.writeInt(this.maxTicksInWater);
        buf.writeFloat(this.slipperinessDriveable);
        buf.writeFloat(this.slipperinessNonDriveable);
        buf.writeFloat(this.slipperinessSlowed);
        buf.writeFloat(this.slipperinessNoPassenger);
    }

    private static ConfigSyncS2C read(RegistryByteBuf buf) {
        return new ConfigSyncS2C(
                buf.readFloat(),
                buf.readFloat(),
                buf.readFloat(),
                buf.readInt(),
                buf.readFloat(),
                buf.readFloat(),
                buf.readFloat(),
                buf.readFloat()
        );
    }

    // Client-side receiver logic
    public static void receive(ConfigSyncS2C payload, ClientPlayNetworking.Context ctx) {
        ctx.client().execute(() -> {
            ClientSyncedConfig.isUsingServerConfig = true;
            ClientSyncedConfig.activeMaxFuelCapacity = payload.maxFuelCapacity();
            ClientSyncedConfig.activeFuelConsumptionPerTick = payload.fuelConsumptionPerTick();
            ClientSyncedConfig.activeFuelTimeToRangeConversion = payload.fuelTimeToRangeConversion();
            ClientSyncedConfig.activeMaxTicksInWater = payload.maxTicksInWater();
            ClientSyncedConfig.activeSlipperinessDriveable = payload.slipperinessDriveable();
            ClientSyncedConfig.activeSlipperinessNonDriveable = payload.slipperinessNonDriveable();
            ClientSyncedConfig.activeSlipperinessSlowed = payload.slipperinessSlowed();
            ClientSyncedConfig.activeSlipperinessNoPassenger = payload.slipperinessNoPassenger();
        });
    }
}
