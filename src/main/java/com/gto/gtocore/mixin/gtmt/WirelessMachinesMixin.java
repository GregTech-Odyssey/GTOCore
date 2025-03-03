package com.gto.gtocore.mixin.gtmt;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;

import net.minecraft.network.chat.Component;

import com.hepdd.gtmthings.data.WirelessMachines;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WirelessMachines.class)
public final class WirelessMachinesMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static MachineDefinition[] registerWirelessEnergyHatch(IO io, int amperage, PartAbility ability, int[] tiers) {
        return new MachineDefinition[GTValues.TIER_COUNT];
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static MachineDefinition[] registerWirelessLaserHatch(IO io, int amperage, PartAbility ability, int[] tiers) {
        return null;
    }

    @Inject(method = "<clinit>", at = @At("TAIL"), remap = false)
    private static void init(CallbackInfo ci) {
        WirelessMachines.WIRELESS_ENERGY_INTERFACE.setTooltipBuilder((a, b) -> b.add(Component.translatable("gtocore.machine.energy_loss", "10%")));
        WirelessMachines.WIRELESS_ENERGY_INPUT_HATCH[GTValues.MAX] = WirelessMachines.WIRELESS_ENERGY_MONITOR;
    }
}
