package de.cxlledjay.bokarts.screen.custom;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.block.ModBlocks;
import de.cxlledjay.bokarts.component.ModDataComponentTypes;
import de.cxlledjay.bokarts.entity.custom.kart.property.BodyType;
import de.cxlledjay.bokarts.item.ModItems;
import de.cxlledjay.bokarts.screen.ModScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class LiveryWorkbenchScreenHandler extends ScreenHandler {

    private final ScreenHandlerContext context;
    private final Inventory workbenchInventory;

    private boolean hasLiveryTemplateInSlot0;
    private int selectedLiveryId;

    public static final int LIVERY_AMOUNT = BodyType.values().length - 1;
    public static final List<BodyType> LIVERIES_SORTED = Arrays.stream(BodyType.values()).filter(e -> e != BodyType.NONE).sorted(Comparator.comparing(BodyType::getDisplayName)).toList();

    public LiveryWorkbenchScreenHandler(int syncId, PlayerInventory inventory) {
        this(syncId, inventory, ScreenHandlerContext.EMPTY);
    }


    public LiveryWorkbenchScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ModScreenHandlers.LIVERY_WORKBENCH_SCREEN_HANDLER, syncId);

        this.context = context;
        this.hasLiveryTemplateInSlot0 = false;
        this.selectedLiveryId = -1;

        this.workbenchInventory = new SimpleInventory(2) {
            @Override
            public void markDirty() {
                super.markDirty();
                // update status of item in slot 0
                hasLiveryTemplateInSlot0 = this.getStack(0).isOf(ModItems.LIVERY_TEMPLATE);
                if(hasLiveryTemplateInSlot0 && selectedLiveryId == -1) {
                    BodyType bodyType = this.getStack(0).getOrDefault(ModDataComponentTypes.KART_ITEM_BODY_TYPE, BodyType.NONE);
                    if(bodyType != BodyType.NONE) selectedLiveryId = LIVERIES_SORTED.indexOf(bodyType);
                }
            }
        };

        // input slot
        this.addSlot(new Slot(this.workbenchInventory, 0, 14, 45) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(ModItems.LIVERY_TEMPLATE);
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                workbenchInventory.clear();
                selectedLiveryId = -1;
                super.onTakeItem(player, stack);
            }
        });

        // output slot
        this.addSlot(new Slot(this.workbenchInventory, 1, 61, 45) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                workbenchInventory.clear();
                selectedLiveryId = -1;
                super.onTakeItem(player, stack);
            }
        });

        // player inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 148 + i * 18));
            }
        }
        // player hotbar
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 206));
        }
    }







    public boolean canCraft() {
        return hasLiveryTemplateInSlot0;
    }

    public ItemStack getLiveryItem() {
        if(this.getSlot(0).getStack().isEmpty()) return ItemStack.EMPTY;
        return this.getSlot(0).getStack();
    }

    public int getSelectedLiveryId() {
        return selectedLiveryId;
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        // set selection
        this.selectedLiveryId = id;

        if(this.getSlot(0).getStack().isEmpty()) return false; // safety

        // create outputStack
        ItemStack outputStack = this.getSlot(0).getStack().copy();
        outputStack.set(ModDataComponentTypes.KART_ITEM_BODY_TYPE, LIVERIES_SORTED.get(this.selectedLiveryId));
        this.getSlot(1).setStack(outputStack);
        return true;
    }

    // -------------------- vanilla logic --------------------

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.workbenchInventory.size()) {
                if (!this.insertItem(originalStack, this.workbenchInventory.size(), this.slots.size(), false)) {
                    return ItemStack.EMPTY;
                }
                this.getSlot(invSlot).onTakeItem(player, originalStack);
            } else if (!this.insertItem(originalStack, 0, this.workbenchInventory.size(), false)) {
                return ItemStack.EMPTY;
            }
            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return newStack;
    }
    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, ModBlocks.LIVERY_WORKBENCH);
    }

    @Override
    public void onClosed(PlayerEntity player) {

        // security
        if (player instanceof ServerPlayerEntity) {

            // drop the input slot
            ItemStack stack = this.workbenchInventory.getStack(0);

            if (!stack.isEmpty()) {
                player.getInventory().offerOrDrop(stack.copy());
                this.workbenchInventory.setStack(0, ItemStack.EMPTY);
                this.workbenchInventory.setStack(1, ItemStack.EMPTY);
            }
        }

        super.onClosed(player);
    }
}
