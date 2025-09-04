package com.gtocore.mixin.gtm.api.machine;

import com.gtolib.api.machine.MultiblockDefinition;

import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;

import net.minecraft.resources.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(MultiblockMachineDefinition.class)
public final class MultiblockMachineDefinitionMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static MultiblockMachineDefinition createDefinition(ResourceLocation id) {
        return MultiblockDefinition.createDefinition(id);
    }
}
