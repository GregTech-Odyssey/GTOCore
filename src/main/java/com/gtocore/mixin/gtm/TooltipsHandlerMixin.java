package com.gtocore.mixin.gtm;

import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.item.TagPrefixItem;
import com.gregtechceu.gtceu.client.TooltipsHandler;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.fluids.FluidStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.function.Consumer;

@Mixin(value = TooltipsHandler.class, remap = false)
public abstract class TooltipsHandlerMixin {

    @Unique
    private static final String SPACETIME_TOOLTIP_KEY = "gtocore.spacetime.element";

    @Redirect(
              method = "appendTooltips(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/TooltipFlag;Ljava/util/List;)V",
              at = @At(
                       value = "INVOKE",
                       target = "Lnet/minecraft/network/chat/Component;literal(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;",
                       remap = true),
              remap = false)
    private static MutableComponent gtocore$localizeChemicalFormula(String text, ItemStack stack, TooltipFlag flag, List<Component> tooltips) {
        if (stack.getItem() instanceof TagPrefixItem item && item.material == GTOMaterials.SpaceTime) {
            return Component.translatable(SPACETIME_TOOLTIP_KEY);
        }
        return Component.literal(text);
    }

    @Redirect(
              method = "appendFluidTooltips(Lnet/minecraftforge/fluids/FluidStack;Ljava/util/function/Consumer;Lnet/minecraft/world/item/TooltipFlag;)V",
              at = @At(
                       value = "INVOKE",
                       target = "Lnet/minecraft/network/chat/Component;literal(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;",
                       ordinal = 0,
                       remap = true),
              remap = false)
    private static MutableComponent gtocore$localizeFluidChemicalFormula(String text, FluidStack fluidStack, Consumer<Component> tooltips, TooltipFlag flag) {
        if (ChemicalHelper.getMaterial(fluidStack.getFluid()) == GTOMaterials.SpaceTime) {
            return Component.translatable(SPACETIME_TOOLTIP_KEY);
        }
        return Component.literal(text);
    }
}
