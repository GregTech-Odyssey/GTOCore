package com.gto.gtocore.mixin.gtm.registry;

import com.gto.gtocore.common.data.GTOMachines;
import com.gto.gtocore.common.data.GTORecipeModifiers;
import com.gto.gtocore.common.data.GTORecipeTypes;
import com.gto.gtocore.common.data.machines.GCYMMachines;

import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.client.util.TooltipHelper;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.machines.GTAEMachines;
import com.gregtechceu.gtceu.common.data.machines.GTMultiMachines;
import com.gregtechceu.gtceu.common.data.machines.GTResearchMachines;

import net.minecraft.network.chat.Component;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(GTMachines.class)
public final class GTMachinesMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static void init() {
        GTMultiMachines.init();
        GTMultiMachines.LARGE_CHEMICAL_REACTOR.setRecipeTypes(new GTRecipeType[] { GTORecipeTypes.CHEMICAL });
        GTMultiMachines.LARGE_CHEMICAL_REACTOR.setTooltipBuilder((a, b) -> b.add(Component.translatable("gtceu.machine.perfect_oc").withStyle(style -> style.withColor(TooltipHelper.BLINKING_RED.getCurrent()))));
        GTMultiMachines.LARGE_BOILER_BRONZE.setRecipeModifier(GTORecipeModifiers::largeBoilerModifier);
        GTMultiMachines.LARGE_BOILER_STEEL.setRecipeModifier(GTORecipeModifiers::largeBoilerModifier);
        GTMultiMachines.LARGE_BOILER_TITANIUM.setRecipeModifier(GTORecipeModifiers::largeBoilerModifier);
        GTMultiMachines.LARGE_BOILER_TUNGSTENSTEEL.setRecipeModifier(GTORecipeModifiers::largeBoilerModifier);
        GTMultiMachines.ELECTRIC_BLAST_FURNACE.setRecipeModifier(GTORecipeModifiers::ebfOverclock);
        GTMultiMachines.PYROLYSE_OVEN.setRecipeModifier(GTORecipeModifiers::pyrolyseOvenOverclock);
        GTMultiMachines.CRACKER.setRecipeModifier(GTORecipeModifiers::crackerOverclock);
        GTMultiMachines.LARGE_CHEMICAL_REACTOR.setRecipeModifier(GTORecipeModifiers.PERFECT_OVERCLOCKING);
        GTMultiMachines.IMPLOSION_COMPRESSOR.setRecipeModifier(GTORecipeModifiers.OVERCLOCKING);
        GTMultiMachines.DISTILLATION_TOWER.setRecipeModifier(GTORecipeModifiers.OVERCLOCKING);
        GTMultiMachines.EVAPORATION_PLANT.setRecipeModifier(GTORecipeModifiers.OVERCLOCKING);
        GTMultiMachines.VACUUM_FREEZER.setRecipeModifier(GTORecipeModifiers.OVERCLOCKING);
        GTMultiMachines.ASSEMBLY_LINE.setRecipeModifier(GTORecipeModifiers.OVERCLOCKING);
        GTResearchMachines.init();
        GTAEMachines.init();
        GCYMMachines.init();
        GTOMachines.init();
        GTRegistries.MACHINES.freeze();
    }
}
