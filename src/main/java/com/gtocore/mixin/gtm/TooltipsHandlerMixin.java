package com.gtocore.mixin.gtm;

import com.gtocore.common.machine.multiblock.generator.FullCellGenerator;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.client.TooltipsHandler;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.hepdd.gtmthings.utils.FormatUtil;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.math.BigDecimal;
import java.util.function.Consumer;

@Mixin(TooltipsHandler.class)
public class TooltipsHandlerMixin {

    @Inject(method = "appendFluidTooltips", at = @At("TAIL"), remap = false)
    private static void gappendFluidTooltips(FluidStack fluidStack, Consumer<Component> tooltips, TooltipFlag flag, CallbackInfo ci,
                                             @Local Fluid fluid) {
        var material = ChemicalHelper.getMaterial(fluid);
        if (FullCellGenerator.Wrapper.ELECTROLYTES_PER_MATERIAL_PER_MILLIBUCKET.containsKey(material)) {
            long euPerMb = FullCellGenerator.Wrapper.ELECTROLYTES_PER_MATERIAL_PER_MILLIBUCKET.get(material);
            tooltips.accept(Component.translatable("gtocore.tooltip.fluid.electrolyte_energy_density",
                    FormattingUtil.formatNumbers(euPerMb)));
            tooltips.accept(Component.translatable("gtocore.tooltip.fluid.electrolyte_energy_density.va",
                    FormatUtil.voltageName(BigDecimal.valueOf(euPerMb)),
                    FormatUtil.voltageAmperage(BigDecimal.valueOf(euPerMb)).toEngineeringString()));
        }
    }
}
