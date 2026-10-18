package de.cxlledjay.bokarts.screen.custom;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.block.entity.custom.KartLiftBlockEntity;
import de.cxlledjay.bokarts.component.ModDataComponentTypes;
import de.cxlledjay.bokarts.entity.custom.kart.KartEntity;
import de.cxlledjay.bokarts.item.ModItems;
import de.cxlledjay.bokarts.item.custom.KartKey;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;

import java.util.Objects;
import java.util.UUID;

public class KartLiftInventory extends SimpleInventory {

    private final KartLiftBlockEntity kartLiftBlockEntity;

    public KartLiftInventory(int size, KartLiftBlockEntity kartLiftBlockEntity) {
        super(size);
        this.kartLiftBlockEntity = kartLiftBlockEntity;
    }

    @Override
    public void markDirty() {
        super.markDirty();

        if(this.kartLiftBlockEntity != null && kartLiftBlockEntity.getKartEntity() != null) {
            // apply tuning
            kartLiftBlockEntity.setWheelsFromItemStack(this.getStack(0));
            kartLiftBlockEntity.setEngineFromItemStack(this.getStack(1));
            kartLiftBlockEntity.setSpoilerFromItemStack(this.getStack(2));
            kartLiftBlockEntity.setAeroFromItemStack(this.getStack(3));
            kartLiftBlockEntity.setLiveryFromItemStack(this.getStack(4));

            // key pairing
            if(
                    this.getStack(5).isOf(ModItems.KEY_BLANK)
                    && this.getStack(5).getCount() == 1
                    && this.getStack(6).isOf(ModItems.KEY_PAIRING_TOOL)
                    && this.getStack(6).getCount() == 1
            ) {
                // safety
                if(kartLiftBlockEntity.getKartEntity() == null) return;

                this.setStack(5, ItemStack.EMPTY);
                this.setStack(6, ItemStack.EMPTY);

                // generate uuid for key
                UUID pairedUuid = UUID.randomUUID();

                // create paired key
                ItemStack pairedKey = KartKey.createPairedKey(pairedUuid, kartLiftBlockEntity.getKartEntity());
                this.setStack(7, pairedKey);

                // pair the kart and remove old key
                this.kartLiftBlockEntity.getKartEntity().setKeyUuid(pairedUuid);
                this.kartLiftBlockEntity.getKartEntity().setKeyInIgnition(false);
            }

            // TODO: apply upgrades

            // and sync data
            kartLiftBlockEntity.syncKartData();
        }
    }
}
