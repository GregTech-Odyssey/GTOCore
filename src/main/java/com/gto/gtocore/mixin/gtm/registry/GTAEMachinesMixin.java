package com.gto.gtocore.mixin.gtm.registry;

import com.gto.gtocore.common.machine.multiblock.part.ae.MEPatternBufferPartMachine;
import com.gto.gtocore.common.machine.multiblock.part.ae.MEPatternBufferProxyPartMachine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import com.gregtechceu.gtceu.api.registry.registrate.MachineBuilder;
import com.gregtechceu.gtceu.common.data.machines.GTAEMachines;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Function;

@Mixin(GTAEMachines.class)
public class GTAEMachinesMixin {

    @Redirect(method = "<clinit>", at = @At(value = "INVOKE", target = "Lcom/gregtechceu/gtceu/api/registry/registrate/GTRegistrate;machine(Ljava/lang/String;Ljava/util/function/Function;)Lcom/gregtechceu/gtceu/api/registry/registrate/MachineBuilder;", ordinal = 6), remap = false)
    private static MachineBuilder<MachineDefinition> mePatternBuffer(GTRegistrate instance, String name, Function<IMachineBlockEntity, MetaMachine> metaMachine) {
        return instance.machine("me_pattern_buffer", MEPatternBufferPartMachine::new);
    }

    @Redirect(method = "<clinit>", at = @At(value = "INVOKE", target = "Lcom/gregtechceu/gtceu/api/registry/registrate/GTRegistrate;machine(Ljava/lang/String;Ljava/util/function/Function;)Lcom/gregtechceu/gtceu/api/registry/registrate/MachineBuilder;", ordinal = 7), remap = false)
    private static MachineBuilder<MachineDefinition> mePatternBufferProxy(GTRegistrate instance, String name, Function<IMachineBlockEntity, MetaMachine> metaMachine) {
        return instance.machine("me_pattern_buffer_proxy", MEPatternBufferProxyPartMachine::new);
    }
}
