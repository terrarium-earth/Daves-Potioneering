package tfar.davespotioneering.blockentity;

import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.msrandom.multiplatform.annotations.Expect;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;
import tfar.davespotioneering.DavesPotioneering;
import tfar.davespotioneering.Util;
import tfar.davespotioneering.duck.BrewingStandDuck;
import tfar.davespotioneering.init.ModBlockEntityTypes;
import tfar.davespotioneering.menu.AdvancedBrewingStandMenu;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


public class AdvancedBrewingStandBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, BrewingStandDuck {

    public static final int BREW_TIME = 200;
    public static final int FUEL_USES = 40;
    public static final int DATA_BREW_TIME = 0;
    public static final int DATA_FUEL_USES = 1;
    //potions are 0,1,2
    public static final int[] POTION_SLOTS = new int[]{0, 1, 2};
    //ingredients are 3,4,5,6,7
    public static final int[] INGREDIENT_SLOTS = new int[]{3,4,5,6,7};
    //fuel is 8
    public static final int FUEL_SLOT = 8;
    public static final int[] INGREDIENT_AND_POTION_SLOTS;
    public static final int[] FUEL_AND_POTION_SLOTS;
    public static final int NUM_SLOTS = POTION_SLOTS.length + INGREDIENT_SLOTS.length + 1; // Plus 1 because FUEL_SLOT

    static {
        Set<Integer> ingSet = Arrays.stream(INGREDIENT_SLOTS).boxed().collect(Collectors.toSet());
        Set<Integer> potSet = Arrays.stream(POTION_SLOTS).boxed().collect(Collectors.toSet());
        Set<Integer> union = Sets.union(ingSet,potSet);
        INGREDIENT_AND_POTION_SLOTS = union.stream().mapToInt(i -> i).toArray();

        Set<Integer> potion_fuel = new HashSet<>(potSet);
        potion_fuel.add(AdvancedBrewingStandBlockEntity.FUEL_SLOT);
        FUEL_AND_POTION_SLOTS = potion_fuel.stream().mapToInt(i -> i).toArray();
    }


    private NonNullList<ItemStack> items = NonNullList.withSize(POTION_SLOTS.length + INGREDIENT_SLOTS.length + 1, ItemStack.EMPTY);

    private int storedXp;
    @Nullable
    private Component name;

    protected int brewTime;
    /** used to check if the current ingredient has been removed from the brewing stand during brewing */
    protected Item ingredient;
    protected int fuel;
    protected final ContainerData data = new ContainerData() {
        public int get(int index) {
            switch(index) {
                case DATA_BREW_TIME:
                    return brewTime;
                case DATA_FUEL_USES:
                    return fuel;
                default:
                    return 0;
            }
        }

        public void set(int index, int value) {
            switch(index) {
                case DATA_BREW_TIME:
                    brewTime = value;
                    break;
                case DATA_FUEL_USES:
                    fuel = value;
            }

        }

        public int getCount() {
            return 2;
        }
    };

    public AdvancedBrewingStandBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntityTypes.COMPOUND_BREWING_STAND, blockPos, blockState);
    }

    public void setCustomName(Component name) {
        this.name = name;
    }

    public Component getName() {
        return this.name != null ? this.name : this.getDefaultName();
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.davespotioneering.compound_brewing");
    }

    @Override
    public Component getDisplayName() {
        return this.getName();
    }

    @Nullable
    @Override
    public Component getCustomName() {
        return this.name;
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, AdvancedBrewingStandBlockEntity blockEntity) {
        ItemStack fuelStack = blockEntity.getItem(FUEL_SLOT);
        if (blockEntity.fuel <= 0 && fuelStack.is(Items.BLAZE_POWDER)) {
            blockEntity.fuel = FUEL_USES;
            fuelStack.shrink(1);
            setChanged(level, blockPos, blockState);
        }

        boolean canBrew = isBrewable(level.potionBrewing(), blockEntity.items);
        boolean brewing = blockEntity.brewTime > 0;
        ItemStack ing = getPriorityIngredient(level.potionBrewing(), blockEntity.items).getRight();
        if (brewing) {
            blockEntity.brewTime--;
            boolean done = blockEntity.brewTime == 0;
            if (done && canBrew) {
                brewPotions(level, blockPos, blockEntity.items);
            } else if (!canBrew) {
                blockEntity.brewTime = 0;
            } else if (blockEntity.ingredient != ing.getItem()) {
                blockEntity.brewTime = 0;
            }
            blockEntity.setChanged();
        } else if (canBrew && blockEntity.fuel > 0) {
            blockEntity.fuel--;
            blockEntity.brewTime = BREW_TIME;
            blockEntity.ingredient = ing.getItem();
            blockEntity.setChanged();
        }
    }

    private static Pair<Integer, ItemStack> getPriorityIngredient(PotionBrewing potionBrewing, NonNullList<ItemStack> items) {
        int maxSlot = Arrays.stream(INGREDIENT_SLOTS).max().getAsInt();
        int minSlot = Arrays.stream(INGREDIENT_SLOTS).min().getAsInt();
        for (int i = maxSlot; i >= minSlot; i--) {
            ItemStack ingredient = items.get(i);
            if (!ingredient.isEmpty() && isThereARecipe(potionBrewing, items, ingredient)) {
                return Pair.of(i, ingredient);
            }
        }
        return Pair.of(-1, ItemStack.EMPTY);
    }

    private static boolean isThereARecipe(PotionBrewing potionBrewing, NonNullList<ItemStack> items, ItemStack ingredient) {
        for (int slot : POTION_SLOTS) {
            ItemStack potion = items.get(slot);
            if (potionBrewing.hasMix(potion, ingredient)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isBrewable(PotionBrewing potionBrewing, NonNullList<ItemStack> items) {
        ItemStack ingredient = getPriorityIngredient(potionBrewing, items).getRight();
        if (ingredient.isEmpty() || !potionBrewing.isIngredient(ingredient)) {
            return false;
        }
        return isThereARecipe(potionBrewing, items, ingredient);
    }

    private static void brewPotions(Level level, BlockPos blockPos, NonNullList<ItemStack> items) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (!shouldBrew(items) || !(blockEntity instanceof AdvancedBrewingStandBlockEntity)) return;
        PotionBrewing potionBrewing = level.potionBrewing();
        Pair<Integer, ItemStack> pair = getPriorityIngredient(potionBrewing, items);
        ItemStack ingredient = pair.getRight();

        //Note: This is changed from the BrewingRecipeRegistry version to allow for >1 potion in a stack
//        for (int i : POTION_SLOTS) {
//            ItemStack output = potionBrewing.mix(items.get(i), ingredient);
//            output.setCount(items.get(i).getCount());
//            if (!output.isEmpty()) {
//                items.set(i, output);
//            }
//        }

        brewPotions(items);
        DavesPotioneering.potionBrew(blockEntity, ingredient);

        ingredient.shrink(1);
        if (ingredient.getItem().hasCraftingRemainingItem()) {
            ItemStack craftingRemainder = new ItemStack(ingredient.getItem().getCraftingRemainingItem());
            if (ingredient.isEmpty()) {
                ingredient = craftingRemainder;
            } else {
                Containers.dropItemStack(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), craftingRemainder);
            }
        }

        items.set(pair.getLeft(), ingredient);
        level.levelEvent(LevelEvent.SOUND_BREWING_STAND_BREW, blockPos, 0);
    }

    // Used for firing events pre-brew
    @SuppressWarnings("NoMatchingActual")
    @Expect
    public static boolean shouldBrew(NonNullList<ItemStack> items);

    @SuppressWarnings("NoMatchingActual")
    @Expect
    public static void brewPotions(NonNullList<ItemStack> items);

    @Override
    public int[] getSlotsForFace(Direction direction) {
        switch (direction) {
            case UP -> {
                return INGREDIENT_SLOTS;
            }
            case DOWN -> {
                return INGREDIENT_AND_POTION_SLOTS;
            }
            default -> {
                return FUEL_AND_POTION_SLOTS;
            }
        }
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (ArrayUtils.contains(INGREDIENT_SLOTS, slot)) {
            PotionBrewing potionBrewing = this.level != null ? this.level.potionBrewing() : PotionBrewing.EMPTY;
            return potionBrewing.isIngredient(stack);
        } else {
            Item item = stack.getItem();
            if (slot == FUEL_SLOT) {
                return item == Items.BLAZE_POWDER;
            } else {
                return isInput(this.level, stack);
            }
        }
    }

    @SuppressWarnings("NoMatchingActual")
    @Expect
    public static boolean isInput(Level level, ItemStack stack);

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction direction) {
        return this.canPlaceItem(slot, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
        return !ArrayUtils.contains(INGREDIENT_SLOTS, slot) || stack.is(Items.GLASS_BOTTLE);
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new AdvancedBrewingStandMenu(id, inventory, this, this.data,this);
    }

    @Override
    public void addXp(double xp) {
        this.storedXp += xp;
    }

    @Override
    public void dump(Player player) {
        if (this.storedXp > 0 && this.getLevel() != null) {
            Util.splitAndSpawnExperience(this.getLevel(), player.position(), this.storedXp);
            this.storedXp = 0;
            this.setChanged();
        }
    }

    @Override
    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        super.loadAdditional(nbt, provider);
        this.brewTime = nbt.getShort("BrewTime");
        this.fuel = nbt.getInt("Fuel");
        storedXp = nbt.getInt("storedXp");
        ContainerHelper.loadAllItems(nbt, this.items, provider);
        if (nbt.contains("CustomName", Tag.TAG_STRING)) {
            this.name = Component.Serializer.fromJson(nbt.getString("CustomName"), provider);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider provider) {
        super.saveAdditional(compound, provider);
        compound.putShort("BrewTime", (short)this.brewTime);
        compound.putInt("Fuel", this.fuel);
        compound.putInt("storedXp", storedXp);
        ContainerHelper.saveAllItems(compound, this.items, provider);
        if (this.name != null) {
            compound.putString("CustomName", Component.Serializer.toJson(this.name, provider));
        }
    }
}
