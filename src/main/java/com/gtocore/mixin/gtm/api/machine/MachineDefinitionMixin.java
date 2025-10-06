package com.gtocore.mixin.gtm.api.machine;

import com.gtolib.api.machine.BasicMachineDefinition;
import com.gtolib.api.machine.IWorkInSpaceDefinition;

import com.gregtechceu.gtceu.api.machine.MachineDefinition;

import net.minecraft.resources.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@Mixin(MachineDefinition.class)
public final class MachineDefinitionMixin implements IWorkInSpaceDefinition {

    @Unique
    private boolean gto$canWorkInSpaceIndependently = false;

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static MachineDefinition createDefinition(ResourceLocation id) {
        return BasicMachineDefinition.createDefinition(id);
    }

    @Override
    public boolean gto$canWorkInSpaceIndependently() {
        return gto$canWorkInSpaceIndependently;
    }

    @Override
    public void gto$setCanWorkInSpaceIndependently(boolean canWorkInSpaceIndependently) {
        gto$canWorkInSpaceIndependently = canWorkInSpaceIndependently;
    }
}
