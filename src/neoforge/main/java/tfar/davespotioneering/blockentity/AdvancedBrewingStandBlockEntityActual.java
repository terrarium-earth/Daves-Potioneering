package tfar.davespotioneering.blockentity;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.level.Level;
import net.msrandom.multiplatform.annotations.Actual;
import net.neoforged.neoforge.event.EventHooks;

public class AdvancedBrewingStandBlockEntityActual {

    @Actual
    public static boolean isInput(Level level, ItemStack itemStack) {
        PotionBrewing potionBrewing = level != null ? level.potionBrewing() : PotionBrewing.EMPTY;
        return (potionBrewing.isInput(itemStack) || itemStack.is(Items.GLASS_BOTTLE));
    }

    @Actual
    public static boolean shouldBrew(NonNullList<ItemStack> items) {
        return EventHooks.onPotionAttemptBrew(items);
    }

    @Actual
    public static void brewPotions(NonNullList<ItemStack> items) {
        EventHooks.onPotionBrewed(items);
    }
}
