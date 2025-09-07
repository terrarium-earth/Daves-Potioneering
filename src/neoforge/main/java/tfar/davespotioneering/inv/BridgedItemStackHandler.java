package tfar.davespotioneering.inv;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import tfar.davespotioneering.inventory.BasicInventoryBridge;

public class BridgedItemStackHandler extends ItemStackHandler implements BasicInventoryBridge {

    public BridgedItemStackHandler(int size) {
        super(size);
    }

    @Override
    public NonNullList<ItemStack> $getStacks() {
        return stacks;
    }

    @Override
    public ItemStack $getStackInSlot(int slot) {
        return getStackInSlot(slot);
    }

    @Override
    public void $setStackInSlot(int slot, ItemStack stack) {
        setStackInSlot(slot,stack);
    }

    @Override
    public ItemStack $extractItem(int slot, int amount, boolean simulate) {
        return extractItem(slot, amount, simulate);
    }

    @Override
    public ItemStack $insertItem(int slot, ItemStack stack, boolean simulate) {
        return insertItem(slot, stack, simulate);
    }

    @Override
    public CompoundTag $save(HolderLookup.Provider provider) {
        return serializeNBT(provider);
    }

    @Override
    public void $load(CompoundTag tag, HolderLookup.Provider provider) {
        deserializeNBT(provider, tag);
    }

    @Override
    public int $getSlots() {
        return getSlots();
    }

    @Override
    public int $getSlotLimit(int slot) {
        return getSlotLimit(slot);
    }
}
