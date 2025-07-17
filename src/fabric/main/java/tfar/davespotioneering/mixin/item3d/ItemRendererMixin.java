package tfar.davespotioneering.mixin.item3d;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import tfar.davespotioneering.DavesPotioneering;
import tfar.davespotioneering.init.ModItems;

// TODO - 1.21.4+: Move to Item Model Definition
@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Shadow
    public abstract ItemModelShaper getItemModelShaper();

    @ModifyVariable(
            method = "render",
            at = @At(value = "HEAD"),
            argsOnly = true
    )
    public BakedModel overrideBakedModel(BakedModel bakedModel, @Local(argsOnly = true) ItemStack stack, @Local(argsOnly = true) ItemDisplayContext itemDisplayContext) {

        if (itemDisplayContext == ItemDisplayContext.GUI || itemDisplayContext == ItemDisplayContext.GROUND || itemDisplayContext == ItemDisplayContext.FIXED) {
            if (stack.is(ModItems.NETHERITE_GAUNTLET)) {
                return getItemModelShaper().getModelManager().getModel(ModelResourceLocation.inventory(ResourceLocation.fromNamespaceAndPath(DavesPotioneering.MODID, "3d/netherite_gauntlet")));
            }
            if (stack.is(ModItems.POTIONEER_GAUNTLET)) {
                return getItemModelShaper().getModelManager().getModel(ModelResourceLocation.inventory(ResourceLocation.fromNamespaceAndPath(DavesPotioneering.MODID, "3d/potioneer_gauntlet")));
            }
            if (stack.is(ModItems.RUDIMENTARY_GAUNTLET)) {
                return getItemModelShaper().getModelManager().getModel(ModelResourceLocation.inventory(ResourceLocation.fromNamespaceAndPath(DavesPotioneering.MODID, "3d/rudimentary_gauntlet")));
            }
        }

        return bakedModel;
    }

    @ModifyVariable(
            method = "getModel",
            at = @At(value = "STORE"),
            ordinal = 1
    )
    public BakedModel getNormalModel(BakedModel bakedModel, @Local(argsOnly = true) ItemStack stack) {
        if (stack.is(ModItems.NETHERITE_GAUNTLET)) {
            return getItemModelShaper().getModelManager().getModel(ModelResourceLocation.inventory(ResourceLocation.fromNamespaceAndPath(DavesPotioneering.MODID, "netherite_gauntlet")));
        }
        if (stack.is(ModItems.POTIONEER_GAUNTLET)) {
            return getItemModelShaper().getModelManager().getModel(ModelResourceLocation.inventory(ResourceLocation.fromNamespaceAndPath(DavesPotioneering.MODID, "potioneer_gauntlet")));
        }
        if (stack.is(ModItems.RUDIMENTARY_GAUNTLET)) {
            return getItemModelShaper().getModelManager().getModel(ModelResourceLocation.inventory(ResourceLocation.fromNamespaceAndPath(DavesPotioneering.MODID, "rudimentary_gauntlet")));
        }

        return bakedModel;
    }
}
