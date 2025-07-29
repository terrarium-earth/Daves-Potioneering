package tfar.davespotioneering.blockentity;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.level.Level;
import net.msrandom.multiplatform.annotations.Actual;
import net.msrandom.multiplatform.annotations.Expect;

public class AdvancedBrewingStandBlockEntityActual {

    @Actual
    public static boolean isInput(Level level, ItemStack itemStack) {
        return (itemStack.is(Items.POTION) || itemStack.is(Items.SPLASH_POTION) || itemStack.is(Items.LINGERING_POTION) || itemStack.is(Items.GLASS_BOTTLE));
    }

    @Actual
    public static boolean shouldBrew(NonNullList<ItemStack> items) {
        return true;
    }

    @Actual
    public static void brewPotions(NonNullList<ItemStack> items) {

    }
}
