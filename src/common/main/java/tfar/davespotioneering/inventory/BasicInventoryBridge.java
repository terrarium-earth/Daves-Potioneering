package tfar.davespotioneering.inventory;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public interface BasicInventoryBridge {

    ItemStack $getStackInSlot(int slot);
    void $setStackInSlot(int slot,ItemStack stack);

    NonNullList<ItemStack> $getStacks();
    int $getSlots();
    int $getSlotLimit(int slot);

    CompoundTag $save(HolderLookup.Provider provider);
    void $load(CompoundTag tag, HolderLookup.Provider provider);
    ItemStack $extractItem(int slot, int amount,boolean simulate);
    ItemStack $insertItem(int slot,ItemStack stack,boolean simulate);


}
