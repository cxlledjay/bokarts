package de.cxlledjay.bokarts.screen.custom;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.block.entity.custom.KartLiftBlockEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;

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

            // and sync data
            kartLiftBlockEntity.syncKartData();
        }
    }
}
