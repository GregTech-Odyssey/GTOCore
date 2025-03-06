package com.gto.gtocore.common.data.machines;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.registries.GTOMachineBuilder;
import com.gto.gtocore.client.renderer.machine.ExResearchPartRenderer;
import com.gto.gtocore.common.machine.multiblock.part.research.ExResearchComputationPartMachine;
import com.gto.gtocore.common.machine.multiblock.part.research.ExResearchCoolerPartMachine;
import com.gto.gtocore.common.machine.multiblock.part.research.ExResearchEmptyPartMachine;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.network.chat.Component;

import java.util.function.Function;

import static com.gregtechceu.gtceu.common.data.machines.GTResearchMachines.OVERHEAT_TOOLTIPS;
import static com.gto.gtocore.api.registries.GTORegistration.REGISTRATE;
import static com.gto.gtocore.utils.register.BlockRegisterUtils.addLang;

public interface ExResearchMachines {

    static void init() {}

    MachineDefinition NICH_EMPTY_COMPONENT = registerHPCAPart(
            "nich_empty_component", "空NICH组件",
            ExResearchEmptyPartMachine::new, false, false, 3)
            .allRotation()
            .abilities(PartAbility.HPCA_COMPONENT)
            .register();

    MachineDefinition NICH_COMPUTING_COMPONENTS = registerHPCAPart(
            "nich_computing_components", "NICH计算组件",
            holder -> new ExResearchComputationPartMachine(holder, 3), true, true, 3)
            .tooltips(
                    Component.translatable("gtceu.machine.hpca.component_general.upkeep_eut", GTValues.VA[GTValues.ZPM]),
                    Component.translatable("gtceu.machine.hpca.component_general.max_eut", GTValues.VA[GTValues.UHV]),
                    Component.translatable("gtceu.machine.hpca.component_type.computation_cwut", 64),
                    Component.translatable("gtceu.machine.hpca.component_type.computation_cooling", 16))
            .allRotation()
            .abilities(PartAbility.HPCA_COMPONENT)
            .tooltipBuilder(OVERHEAT_TOOLTIPS)
            .register();

    MachineDefinition NICH_COOLING_COMPONENTS = registerHPCAPart(
            "nich_cooling_components", "NICH冷却组件",
            holder -> new ExResearchCoolerPartMachine(holder, 3), true, false, 3)
            .tooltips(Component.translatable("gtceu.machine.hpca.component_general.max_eut", GTValues.VA[GTValues.UHV]),
                    Component.translatable("gtceu.machine.hpca.component_type.cooler_active"),
                    Component.translatable("gtceu.machine.hpca.component_type.cooler_active_coolant",
                            800, GTMaterials.PCBCoolant.getLocalizedName()),
                    Component.translatable("gtceu.machine.hpca.component_type.cooler_cooling", 8))
            .allRotation()
            .abilities(PartAbility.HPCA_COMPONENT)
            .register();

    private static GTOMachineBuilder registerHPCAPart(String name, String cn,
                                                      Function<IMachineBlockEntity, MetaMachine> constructor,
                                                      boolean activeTexture,
                                                      boolean damagedTexture,
                                                      int tire) {
        addLang(name, cn);
        return REGISTRATE.machine(name, constructor)
                .renderer(() -> new ExResearchPartRenderer(
                        tire, GTOCore.id("block/casings/about_computer/" + name),
                        !activeTexture ? null : GTOCore.id("block/casings/about_computer/" + name + "_active"),
                        !activeTexture ? null : GTOCore.id("block/casings/about_computer/" + name + "_active_emissive"),
                        !damagedTexture ? null : GTOCore.id("block/casings/about_computer/" + "damaged_" + name),
                        !damagedTexture ? null : GTOCore.id("block/casings/about_computer/" + "damaged_" + name + "_active"),
                        !damagedTexture ? null : GTOCore.id("block/casings/about_computer/" + "damaged_" + name + "_active_emissive")));
    }
}
