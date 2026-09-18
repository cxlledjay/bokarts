package de.cxlledjay.bokarts.networking.packet;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.entity.custom.KartEntity;
import de.cxlledjay.bokarts.screen.custom.KartInventoryScreenHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record AddFuelPayloadC2S() implements CustomPayload {

    public static final CustomPayload.Id<AddFuelPayloadC2S> ID = new CustomPayload.Id<>(BoKarts.id("add_fuel"));

    public static final PacketCodec<RegistryByteBuf, AddFuelPayloadC2S> STREAM_CODEC = PacketCodec.unit(new AddFuelPayloadC2S());


    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void receive(AddFuelPayloadC2S payload, ServerPlayNetworking.Context ctx) {
        PlayerEntity player = ctx.player();

        // check valid screen interaction
        if(player.currentScreenHandler instanceof KartInventoryScreenHandler handler && player.getVehicle() instanceof KartEntity kart) {
            kart.addFuelFromItem(handler.getFuelItemStack());
        }
    }
}
