package com.gto.gtocore.common.data.machines;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.GTOValues;
import com.gto.gtocore.api.machine.part.GTOPartAbility;
import com.gto.gtocore.api.pattern.GTOPredicates;
import com.gto.gtocore.api.registries.GTOMachineBuilder;
import com.gto.gtocore.client.renderer.machine.ExResearchPartRenderer;
import com.gto.gtocore.common.block.BlockMap;
import com.gto.gtocore.common.data.GTOMachines;
import com.gto.gtocore.common.machine.multiblock.electric.SupercomputingCenterMachine;
import com.gto.gtocore.common.machine.multiblock.part.research.ExResearchComputationPartMachine;
import com.gto.gtocore.common.machine.multiblock.part.research.ExResearchCoolerPartMachine;
import com.gto.gtocore.common.machine.multiblock.part.research.ExResearchEmptyPartMachine;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import net.minecraft.network.chat.Component;

import java.util.function.Function;

import static com.gregtechceu.gtceu.api.machine.multiblock.PartAbility.*;
import static com.gregtechceu.gtceu.api.machine.multiblock.PartAbility.HPCA_COMPONENT;
import static com.gregtechceu.gtceu.api.pattern.Predicates.*;
import static com.gregtechceu.gtceu.api.pattern.Predicates.abilities;
import static com.gregtechceu.gtceu.common.data.machines.GTResearchMachines.OVERHEAT_TOOLTIPS;
import static com.gto.gtocore.api.registries.GTORegistration.REGISTRATE;
import static com.gto.gtocore.utils.register.BlockRegisterUtils.addLang;
import static com.gto.gtocore.utils.register.MachineRegisterUtils.multiblock;

public interface ExResearchMachines {

    static void init() {}

    MultiblockMachineDefinition SUPERCOMPUTING_CENTER = multiblock("supercomputing_center", "超算中心", SupercomputingCenterMachine::new)
            .nonYAxisRotation()
            .block(GTBlocks.COMPUTER_CASING)
            .recipe(GTRecipeTypes.DUMMY_RECIPES)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("  AAAAAAAAAAA  ", " AA         AA ", "AA           AA", "A             A", "A             A", "A             A", "A             A", "AA           AA", " AA         AA ", "  AAAAAAAAAAA  ")
                    .aisle(" AAABBBBBBBAAA ", "AACCCCCCCCCCCAA", "ACCKKKKKKKKKCCA", " CKKKKKKKKKKKC ", " CKKKKKKKKKKKC ", " CKKKKKKKKKKKC ", " CKKKKKKKKKKKC ", "ACCKKKKKKKKKCCA", "AACCCCCCCCCCCAA", " AA         AA ")
                    .aisle("AAABBBBBBBBBAAA", "ACC         CCA", " C           C ", " K           K ", " K           K ", " K           K ", " K           K ", " C           C ", "ACCKKKKKKKKKCCA", "AA           AA")
                    .aisle("AABBBBBBBBBBBAA", " C  CC   CC  C ", " K  CC   CC  K ", " K  CC   CC  K ", " K  CC   CC  K ", " K  CC   CC  K ", " K  CC   CC  K ", " K  CC   CC  K ", " CKKKKKKKKKKKC ", "A             A")
                    .aisle("ABBBBBBBBBBBBBA", " C  CC   CC  C ", " K  DE   ED  K ", " K  DE   ED  K ", " K  DE   ED  K ", " K  DE   ED  K ", " K  DE   ED  K ", " K  CC   CC  K ", " CKKKKKKKKKKKC ", "A             A")
                    .aisle("ABBBBBBBBBBBBBA", " C  CC   CC  C ", " K  DE   ED  K ", " K  DE   ED  K ", " K  DE   ED  K ", " K  DE   ED  K ", " K  DE   ED  K ", " K  CC   CC  K ", " CKKKKKKKKKKKC ", "A             A")
                    .aisle("ABBBBBBBBBBBBBA", " C  CC   CC  C ", " K  DE   ED  K ", " K  DE   ED  K ", " K  DE   ED  K ", " K  DE   ED  K ", " K  DE   ED  K ", " K  CC   CC  K ", " CKKKKKKKKKKKC ", "A             A")
                    .aisle("ABBBBBBBBBBBBBA", " C  CC   CC  C ", " K  DE   ED  K ", " K  DE   ED  K ", " K  DE   ED  K ", " K  DE   ED  K ", " K  DE   ED  K ", " K  CC   CC  K ", " CKKKKKKKKKKKC ", "A             A")
                    .aisle("ABBBBBBBBBBBBBA", " C  CC   CC  C ", " K  DE   ED  K ", " K  DE   ED  K ", " K  DE   ED  K ", " K  DE   ED  K ", " K  DE   ED  K ", " K  CC   CC  K ", " CKKKKKKKKKKKC ", "A             A")
                    .aisle("AABBBBBBBBBBBAA", " C  CC   CC  C ", " K  CC   CC  K ", " K  CC   CC  K ", " K  CC   CC  K ", " K  CC   CC  K ", " K  CC   CC  K ", " K  CC   CC  K ", " CKKKKKKKKKKKC ", "A             A")
                    .aisle("AAABBBBBBBBBAAA", "ACC         CCA", " C           C ", " K           K ", " K           K ", " K           K ", " K           K ", " C           C ", "ACCKKKKKKKKKCCA", "AA           AA")
                    .aisle(" AAABBBBBBBAAA ", "AACCCCCCCCCCCAA", "ACCKKKKKKKKKCCA", " CKKKVVVVVKKKC ", " CKKVVV~VVVKKC ", " CKKKVVVVVKKKC ", " CKKKKKKKKKKKC ", "ACCKKKKKKKKKCCA", "AACCCCCCCCCCCAA", " AA         AA ")
                    .aisle("  AAAAAAAAAAA  ", " AA         AA ", "AA           AA", "A             A", "A             A", "A             A", "A             A", "AA           AA", " AA         AA ", "  AAAAAAAAAAA  ")
                    .where('A', blocks(GTBlocks.ADVANCED_COMPUTER_CASING.get()))
                    .where('B', blocks(GTBlocks.HIGH_POWER_CASING.get()))
                    .where('~', controller(blocks(definition.get())))
                    .where(' ', any())
                    .where('V', blocks(GTBlocks.COMPUTER_CASING.get())
                            .or(blocks(GTOMachines.THERMAL_CONDUCTOR_HATCH.get()).setMaxGlobalLimited(1))
                            .or(abilities(IMPORT_ITEMS))
                            .or(abilities(IMPORT_FLUIDS))
                            .or(abilities(EXPORT_ITEMS))
                            .or(abilities(EXPORT_FLUIDS))
                            .or(abilities(INPUT_ENERGY).setMaxGlobalLimited(2))
                            .or(abilities(COMPUTATION_DATA_TRANSMISSION).setMaxGlobalLimited(1))
                            .or(abilities(MAINTENANCE).setExactLimit(1)))
                    .where('C', GTOPredicates.tierBlock(BlockMap.COMPUTER_CASING_MAP, GTOValues.COMPUTER_CASING_TIER))
                    .where('K', GTOPredicates.glass())
                    .where('E', abilities(GTOPartAbility.COMPUTING_COMPONENT, HPCA_COMPONENT))
                    .where('D', GTOPredicates.tierBlock(BlockMap.COMPUTER_CASING_MAP, GTOValues.COMPUTER_HEAT_TIER))
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/hpca/computer_casing/back"), GTCEu.id("block/multiblock/large_miner"))
            .register();

    MachineDefinition NICH_EMPTY_COMPONENT = registerHPCAPart(
            "nich_empty_component", "空NICH组件",
            ExResearchEmptyPartMachine::new, false, false, 3)
            .register();

    MachineDefinition NICH_COMPUTING_COMPONENTS = registerHPCAPart(
            "nich_computing_components", "NICH计算组件",
            holder -> new ExResearchComputationPartMachine(holder, 3), true, true, 3)
            .tooltips(
                    Component.translatable("gtceu.machine.hpca.component_general.upkeep_eut", GTValues.VA[GTValues.ZPM]),
                    Component.translatable("gtceu.machine.hpca.component_general.max_eut", GTValues.VA[GTValues.UHV]),
                    Component.translatable("gtceu.machine.hpca.component_type.computation_cwut", 64),
                    Component.translatable("gtceu.machine.hpca.component_type.computation_cooling", 16))
            .tooltipBuilder(OVERHEAT_TOOLTIPS)
            .register();

    MachineDefinition NICH_COOLING_COMPONENTS = registerHPCAPart(
            "nich_cooling_components", "NICH冷却组件",
            holder -> new ExResearchCoolerPartMachine(holder, 3), true, false, 3)
            .tooltips(Component.translatable("gtceu.machine.hpca.component_general.max_eut", GTValues.VA[GTValues.UV]),
                    Component.translatable("gtceu.machine.hpca.component_type.cooler_active"),
                    Component.translatable("gtceu.machine.hpca.component_type.cooler_active_coolant",
                            80, GTMaterials.Helium.getLocalizedName()),
                    Component.translatable("gtceu.machine.hpca.component_type.cooler_cooling", 8))
            .register();

    MachineDefinition GWCA_EMPTY_COMPONENT = registerHPCAPart(
            "gwca_empty_component", "空GWCA组件",
            ExResearchEmptyPartMachine::new, false, false, 4)
            .register();

    MachineDefinition GWCA_COMPUTING_COMPONENTS = registerHPCAPart(
            "gwca_computing_components", "GWCA计算组件",
            holder -> new ExResearchComputationPartMachine(holder, 4), true, true, 4)
            .tooltips(
                    Component.translatable("gtceu.machine.hpca.component_general.upkeep_eut", GTValues.VA[GTValues.UV]),
                    Component.translatable("gtceu.machine.hpca.component_general.max_eut", GTValues.VA[GTValues.UEV]),
                    Component.translatable("gtceu.machine.hpca.component_type.computation_cwut", 1024),
                    Component.translatable("gtceu.machine.hpca.component_type.computation_cooling", 256))
            .tooltipBuilder(OVERHEAT_TOOLTIPS)
            .register();

    MachineDefinition GWCA_COOLING_COMPONENTS = registerHPCAPart(
            "gwca_cooling_components", "GWCA冷却组件",
            holder -> new ExResearchCoolerPartMachine(holder, 4), true, false, 4)
            .tooltips(Component.translatable("gtceu.machine.hpca.component_general.max_eut", GTValues.VA[GTValues.UHV]),
                    Component.translatable("gtceu.machine.hpca.component_type.cooler_active"),
                    Component.translatable("gtceu.machine.hpca.component_type.cooler_active_coolant",
                            320, GTMaterials.Helium.getLocalizedName()),
                    Component.translatable("gtceu.machine.hpca.component_type.cooler_cooling", 64))
            .register();

    private static GTOMachineBuilder registerHPCAPart(String name, String cn,
                                                      Function<IMachineBlockEntity, MetaMachine> constructor,
                                                      boolean activeTexture,
                                                      boolean damagedTexture,
                                                      int tire) {
        addLang(name, cn);
        return REGISTRATE.machine(name, constructor)
                .allRotation()
                .abilities(GTOPartAbility.COMPUTING_COMPONENT)
                .renderer(() -> new ExResearchPartRenderer(
                        tire, GTOCore.id("block/casings/about_computer/" + name),
                        !activeTexture ? null : GTOCore.id("block/casings/about_computer/" + name + "_active"),
                        !activeTexture ? null : GTOCore.id("block/casings/about_computer/" + name + "_active_emissive"),
                        !damagedTexture ? null : GTOCore.id("block/casings/about_computer/" + "damaged_" + name),
                        !damagedTexture ? null : GTOCore.id("block/casings/about_computer/" + "damaged_" + name + "_active"),
                        !damagedTexture ? null : GTOCore.id("block/casings/about_computer/" + "damaged_" + name + "_active_emissive")));
    }
}
