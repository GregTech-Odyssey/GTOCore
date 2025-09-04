package com.gtocore.mixin.gtm.registry;

import com.gtocore.common.machine.multiblock.electric.DistillationTowerMachine;
import com.gtocore.common.machine.multiblock.electric.LargeChemicalReactorMachine;
import com.gtocore.common.machine.multiblock.steam.SteamMultiblockMachine;

import com.gtolib.api.annotation.NewDataAttributes;
import com.gtolib.api.lang.CNEN;
import com.gtolib.api.registries.GTORegistration;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import com.gregtechceu.gtceu.api.registry.registrate.MultiblockMachineBuilder;
import com.gregtechceu.gtceu.common.data.machines.GTMultiMachines;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Function;

@Mixin(GTMultiMachines.class)
public class GTMultiMachinesMixin {

    @Redirect(method = "<clinit>", at = @At(value = "INVOKE", target = "Lcom/gregtechceu/gtceu/api/registry/registrate/GTRegistrate;multiblock(Ljava/lang/String;Ljava/util/function/Function;)Lcom/gregtechceu/gtceu/api/registry/registrate/MultiblockMachineBuilder;", ordinal = 3), remap = false)
    private static MultiblockMachineBuilder large_chemical_reactor(GTRegistrate instance, String name, Function<MetaMachineBlockEntity, ? extends MultiblockControllerMachine> metaMachine) {
        return GTORegistration.GTM.multiblock(name, LargeChemicalReactorMachine::new).tooltipsText("线圈等级不低于运行的配方等级时启用无损超频", "Perfect overclocking is enabled when the coil tier is not less than the running recipe tier");
    }

    @Redirect(method = "<clinit>", at = @At(value = "INVOKE", target = "Lcom/gregtechceu/gtceu/api/registry/registrate/GTRegistrate;multiblock(Ljava/lang/String;Ljava/util/function/Function;)Lcom/gregtechceu/gtceu/api/registry/registrate/MultiblockMachineBuilder;", ordinal = 8), remap = false)
    private static MultiblockMachineBuilder distillation_tower(GTRegistrate instance, String name, Function<MetaMachineBlockEntity, ? extends MultiblockControllerMachine> metaMachine) {
        return GTORegistration.GTM.multiblock(name, DistillationTowerMachine::new).tooltips(NewDataAttributes.RUNTIME_REQUIREMENT.create(CNEN.create("配方中每种产物都需要一层蒸馏塔节", "Each product in the recipe requires a layer of distillation tower.")).getArray());
    }

    @Redirect(method = "<clinit>", at = @At(value = "INVOKE", target = "Lcom/gregtechceu/gtceu/api/registry/registrate/GTRegistrate;multiblock(Ljava/lang/String;Ljava/util/function/Function;)Lcom/gregtechceu/gtceu/api/registry/registrate/MultiblockMachineBuilder;", ordinal = 12), remap = false)
    private static MultiblockMachineBuilder steam_grinder(GTRegistrate instance, String name, Function<MetaMachineBlockEntity, ? extends MultiblockControllerMachine> metaMachine) {
        return GTORegistration.GTM.multiblock(name, SteamMultiblockMachine::new).addTooltipsFromClass(SteamMultiblockMachine.class).steamOverclock().addRecipeTypeTooltips();
    }

    @Redirect(method = "<clinit>", at = @At(value = "INVOKE", target = "Lcom/gregtechceu/gtceu/api/registry/registrate/GTRegistrate;multiblock(Ljava/lang/String;Ljava/util/function/Function;)Lcom/gregtechceu/gtceu/api/registry/registrate/MultiblockMachineBuilder;", ordinal = 13), remap = false)
    private static MultiblockMachineBuilder steam_oven(GTRegistrate instance, String name, Function<MetaMachineBlockEntity, ? extends MultiblockControllerMachine> metaMachine) {
        return GTORegistration.GTM.multiblock(name, SteamMultiblockMachine::new).addTooltipsFromClass(SteamMultiblockMachine.class).steamOverclock().addRecipeTypeTooltips();
    }
}
