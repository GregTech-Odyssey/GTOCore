package com.gtocore.mixin.gtm.api.machine;

import com.gtolib.api.machine.BasicMachineDefinition;

import com.gregtechceu.gtceu.api.machine.MachineDefinition;

import net.minecraft.resources.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(MachineDefinition.class)
public final class MachineDefinitionMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static MachineDefinition createDefinition(ResourceLocation id) {
        return BasicMachineDefinition.createDefinition(id);
    }
}
