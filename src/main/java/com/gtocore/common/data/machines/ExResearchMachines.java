package com.gtocore.common.data.machines;

import com.gtocore.api.machine.part.GTOPartAbility;
import com.gtocore.api.pattern.GTOPredicates;
import com.gtocore.client.renderer.machine.ExResearchPartRenderer;
import com.gtocore.common.block.BlockMap;
import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.GTOMachines;
import com.gtocore.common.data.GTOMaterials;
import com.gtocore.common.machine.multiblock.electric.AnalysisAndResearchCenterMachine;
import com.gtocore.common.machine.multiblock.electric.ScanningStationMachine;
import com.gtocore.common.machine.multiblock.electric.SupercomputingCenterMachine;
import com.gtocore.common.machine.multiblock.electric.SyntheticDataAssemblyPlantMachine;
import com.gtocore.common.machine.multiblock.part.AnalyzeHolderMachine;
import com.gtocore.common.machine.multiblock.part.DataGenerateHolderMachine;
import com.gtocore.common.machine.multiblock.part.ResearchHolderMachine;
import com.gtocore.common.machine.multiblock.part.ScanningHolderMachine;
import com.gtocore.common.machine.multiblock.part.research.ExResearchBridgePartMachine;
import com.gtocore.common.machine.multiblock.part.research.ExResearchComputationPartMachine;
import com.gtocore.common.machine.multiblock.part.research.ExResearchCoolerPartMachine;
import com.gtocore.common.machine.multiblock.part.research.ExResearchEmptyPartMachine;

import com.gtolib.GTOCore;
import com.gtolib.api.GTOValues;
import com.gtolib.api.annotation.NewDataAttributes;
import com.gtolib.api.annotation.component_builder.StyleBuilder;
import com.gtolib.api.registries.GTOMachineBuilder;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.MultiblockShapeInfo;
import com.gregtechceu.gtceu.client.renderer.machine.OverlayTieredActiveMachineRenderer;
import com.gregtechceu.gtceu.client.renderer.machine.OverlayTieredMachineRenderer;
import com.gregtechceu.gtceu.common.data.*;
import com.gregtechceu.gtceu.common.data.machines.GTResearchMachines;
import com.gregtechceu.gtceu.common.machine.multiblock.part.DataAccessHatchMachine;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.machine.multiblock.PartAbility.*;
import static com.gregtechceu.gtceu.api.pattern.Predicates.*;
import static com.gregtechceu.gtceu.common.data.GTBlocks.ADVANCED_COMPUTER_CASING;
import static com.gregtechceu.gtceu.common.data.GTBlocks.COMPUTER_CASING;
import static com.gregtechceu.gtceu.common.data.GTBlocks.COMPUTER_HEAT_VENT;
import static com.gregtechceu.gtceu.common.data.machines.GTResearchMachines.OVERHEAT_TOOLTIPS;
import static com.gtocore.common.data.GTORecipeTypes.*;
import static com.gtolib.api.registries.GTORegistration.GTO;
import static com.gtolib.utils.register.BlockRegisterUtils.addLang;
import static com.gtolib.utils.register.MachineRegisterUtils.machine;
import static com.gtolib.utils.register.MachineRegisterUtils.multiblock;

public final class ExResearchMachines {

    public static void init() {}

    /////////////////////////////////////
    // *********** 算力机器 *********** //
    /////////////////////////////////////

    public static final MultiblockMachineDefinition SUPERCOMPUTING_CENTER = multiblock("supercomputing_center", "运算中心", SupercomputingCenterMachine::new)
            // 基本功能描述 - 使用更保守的样式
            .tooltips(NewDataAttributes.EMPTY_WITH_BAR.create(
                    h -> h.addLines("计算机超级计算中心", "Computer Supercomputing Center", StyleBuilder::setGold),
                    c -> c.addLines(
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("将计算机放置在一起提供大量算力", "Putting computers together to provide lots of computing power", StyleBuilder::setGray), p -> p, StyleBuilder::setOneTab),
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("采用先进的冷却方案使其能够输出更多的算力", "The use of advanced cooling solutions enables it to output more computing power", StyleBuilder::setGray), p -> p, StyleBuilder::setOneTab),
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("机器有三个等级，通过在主机内放置物品切换", "The machine has three levels, switched by placing items in the mainframe", StyleBuilder::setGray), p -> p, StyleBuilder::setOneTab),
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("每个等级的机器结构方块需要与机器等级匹配", "The structure blocks of each level of the machine need to match the machine level", StyleBuilder::setRed), p -> p, StyleBuilder::setOneTab))))

            // 算力系统 - 合并算力修正系数和衰减机制
            .tooltips(NewDataAttributes.EMPTY_WITH_BAR.create(
                    h -> h.addLines("算力系统", "Computing Power System", StyleBuilder::setWhite),
                    c -> c.addLines(
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("机器算力受到算力修正系数的影响", "The machine's computing power is affected by the correction factor", StyleBuilder::setGray), p -> p, StyleBuilder::setOneTab),
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("最大输出算力 = 计算组件的算力和 * 算力修正系数", "Maximum output = computing components power * correction factor", StyleBuilder::setGray), p -> p, StyleBuilder::setOneTab),
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("当等级为2或3时，算力修正系数会随时间衰减", "At levels 2 or 3, the correction factor will decay over time", StyleBuilder::setGray), p -> p, StyleBuilder::setOneTab),
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("衰减公式: ((系数-0.4)²/5000)*(0.8/log(系数+6))", "Decay: ((factor-0.4)²/5000)*(0.8/log(factor+6))", StyleBuilder::setGray), p -> p, StyleBuilder::setOneTab),
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("算力修正系数最低不会小于0.8", "Correction factor will not fall below 0.8", StyleBuilder::setGray), p -> p, StyleBuilder::setOneTab))))

            // 导热剂系统 - 合并导热剂系统和效率表
            .tooltips(NewDataAttributes.EMPTY_WITH_BAR.create(
                    h -> h.addLines("导热剂系统", "Thermal Conductivity System", StyleBuilder::setWhite),
                    c -> c.addLines(
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("可通过导热剂仓输入导热剂增加算力修正系数", "Thermal conductivity can be input to increase the correction factor", StyleBuilder::setGray), p -> p, StyleBuilder::setOneTab),
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("导热剂使用后会失效", "Thermal conductivity will become invalid after use", StyleBuilder::setRed), p -> p, StyleBuilder::setOneTab),
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("等级2时提升上限为4，等级3时提升上限为16", "Level 2 max is 4, Level 3 max is 16", StyleBuilder::setGray), p -> p, StyleBuilder::setOneTab),
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("MFPC / Cascade-MFPC 效率: 块(0.18/0.54) 条(0.02/0.06) 粒(0.0022/0.0066)", "MFPC/Cascade-MFPC: Block(0.18/0.54) Ingot(0.02/0.06) Nugget(0.0022/0.0066)", StyleBuilder::setGray), p -> p, StyleBuilder::setOneTab),
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("寒冰碎片: 0.0001 (极低效率)", "Ice Shards: 0.0001 (extremely inefficient)", StyleBuilder::setRed), p -> p, StyleBuilder::setOneTab))))

            // 保持原有的三个Tier信息样式，放在最下面
            .tooltips(NewDataAttributes.EMPTY_WITH_BAR.create(
                    h -> h.addLines("Tier 1 : 支持 HPCA系列组件", "Tier 1 : Allow HPCA series components", StyleBuilder::setAqua),
                    c -> c.addLines(
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("不需要安装任何物品", "No Any Slot Requirement", StyleBuilder::setGray), p -> p, StyleBuilder::setOneTab),
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("需要 钨强化硼玻璃 + 计算机外壳 + 计算机散热口", "Require Tungsten Borosilicate Glass + Computer Casing + Computer Heat Vent", StyleBuilder::setMixedRedPurple), p -> p, StyleBuilder::setOneTab))))

            .tooltips(NewDataAttributes.EMPTY_WITH_BAR.create(
                    h -> h.addLines("Tier 2 : 支持 NICH系列组件", "Tier 2 : Allow NICH series components", StyleBuilder::setAqua),
                    c -> c.addLines(
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("需要 放入生物主机", "Slot Requirement : Place biological host", StyleBuilder::setGreen), p -> p, StyleBuilder::setOneTab),
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("需要 安普洛强化硼玻璃 + 生物计算机外壳 + 相变计算机散热口", "Require Neutronium Borosilicate Glass + Biocomputer Casing + Phase Change Biocomputer Cooling Vents", StyleBuilder::setMixedRedPurple), p -> p, StyleBuilder::setOneTab))))

            .tooltips(NewDataAttributes.EMPTY_WITH_BAR.create(
                    h -> h.addLines("Tier 3 : 支持 GWCA系列组件 (自带桥接)", "Tier 3 : Allow GWCA series components (with built-in bridge)", StyleBuilder::setAqua),
                    c -> c.addLines(
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("需要 放入超因果主机", "Slot Requirement : Place the Hyper-Causal Host", StyleBuilder::setRainbow), p -> p, StyleBuilder::setOneTab),
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("需要 塔兰强化硼玻璃 + 引力子计算机外壳 + 逆熵计算机冷凝矩阵", "Require Taranium Borosilicate Glass + Graviton Computer Casing + Anti Entropy Computer Condensation Matrix", StyleBuilder::setMixedRedPurple), p -> p, StyleBuilder::setOneTab))))
            .nonYAxisRotation()
            .recipeTypes(GTRecipeTypes.DUMMY_RECIPES)
            .block(GTOBlocks.OXIDATION_RESISTANT_HASTELLOY_N_MECHANICAL_CASING)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle(" BBB                   BBB  ", " BKB                   BKB  ", " BKB                   BKB  ", " BKB                   BKB  ", " BKB                   BKB  ", " BKBBBBBBBBBBBBBBBBBBBBBKB  ", " BKB                   BKB  ", " BKB                   BKB  ", " BKB                   BKB  ", " BKBBBBBBBBBBBBBBBBBBBBBKB  ", " BKB   UUUUUUUUUUUUU   BKB  ", " BKB  U   H     H   U  BKB  ", " BKB  U   H     H   U  BKB  ", " BKB  U   H     H   U  BKB  ", " BKB   UUUUUUUUUUUUU   BKB  ", " BKB                   BKB  ", " BKB   UUUUUUUUUUUUU   BKB  ", " BKB  U   H     H   U  BKB  ", " BKB  U   H     H   U  BKB  ", " BKB  U   H     H   U  BKB  ", " BKB   UUUUUUUUUUUUU   BKB  ", " BKB                   BKB  ", " BKB   UUUUUUUUUUUUU   BKB  ", " BKB  U   H     H   U  BKB  ", " BKB  U   H     H   U  BKB  ", " BKB  U   H     H   U  BKB  ", " BKB   UUUUUUUUUUUUU   BKB  ", " BKB                   BKB  ", " BKB   UUUUUUUUUUUUU   BKB  ", " BKB  U   H     H   U  BKB  ", " BKB  U   H     H   U  BKB  ", " BKB  U   H     H   U  BKB  ", " BKB   UUUUUUUUUUUUU   BKB  ", " BKB                   BKB  ", " BKB   UUUUUUUUUUUUU   BKB  ", " BKB  U   H     H   U  BKB  ", " BKB  U   H     H   U  BKB  ", " BKB  U   H     H   U  BKB  ", " BKB   UUUUUUUUUUUUU   BKB  ", " BKB                   BKB  ", " BKB   UUUUUUUUUUUUU   BKB  ", " BKB  U   H     H   U  BKB  ", " BKB  U   H     H   U  BKB  ", " BKB  U   H     H   U  BKB  ", " BKB   UUUUUUUUUUUUU   BKB  ", " BKB                   BKB  ", " BKB   UUUUUUUUUUUUU   BKB  ", " BKB  U   H     H   U  BKB  ", " BKB  U   H     H   U  BKB  ", " BKB  U   H     H   U  BKB  ", " BKB   UUUUUUUUUUUUU   BKB  ", " BKB                   BKB  ", " BKBBBBBBBBBBBBBBBBBBBBBKB  ", " BKBBBBBBBBBBBBBBBBBBBBBKB  ", " BKB                   BKB  ", " BKB                   BKB  ", " BKBAAAAAAAAAAAAAAAAAAABKB  ", " BBB                   BBB  ", "                            ")
                    .aisle(" BIBAAAAAAAAAAAAAAAAAAABIBF ", " BLB   GG         GG   BLBF ", " BLB   GG         GG   BLBF ", " BLB   GG         GG   BLBF ", " BLBAAAAAAAAAAAAAAAAAAABLBF ", " BNNNNNNNNNNNNNNNNNNNNNBLBF ", " BLBNKKKKKKKKKKKKKKKKKNBLBF ", " BLBNKKKKKKKKKKKKKKKKKNBLBF ", " BLBNKKKKKKKKKKKKKKKKKNBLBF ", " BLBNKKKKKKKKKKKKKKKKKNBLBF ", " BLBNKKKKKKKKKKKKKKKKKNBLBF ", " BLBNFMVVVVVVVVVVVVVMFNBLBF ", " BLBNFMVVVVVVVVVVVVVMFNBLBF ", " BLBNFMVVVVVVVVVVVVVMFNBLBF ", " BLBNKKKKKKKKKKKKKKKKKNBLBF ", " BLBNKKKKKKKKKKKKKKKKKNBLBF ", " BLBNKKKKKKKKKKKKKKKKKNBLBF ", " BLBNFMVVVVVVVVVVVVVMFNBLBF ", " BLBNFMVVVVVVVVVVVVVMFNBLBF ", " BLBNFMVVVVVVVVVVVVVMFNBLBF ", " BLBNKKKKKKKKKKKKKKKKKNBLBF ", " BLBNKKKKKKKKKKKKKKKKKNBLBF ", " BLBNKKKKKKKKKKKKKKKKKNBLBF ", " BLBNFMVVVVVVVVVVVVVMFNBLBF ", " BLBNFMVVVVVVVVVVVVVMFNBLBF ", " BLBNFMVVVVVVVVVVVVVMFNBLBF ", " BLBNKKKKKKKKKKKKKKKKKNBLBF ", " BLBNKKKKKKKKKKKKKKKKKNBLBF ", " BLBNKKKKKKKKKKKKKKKKKNBLBF ", " BLBNFMVVVVVVVVVVVVVMFNBLBF ", " BLBNFMVVVVVVVVVVVVVMFNBLBF ", " BLBNFMVVVVVVVVVVVVVMFNBLBF ", " BLBNKKKKKKKKKKKKKKKKKNBLBF ", " BLBNKKKKKKKKKKKKKKKKKNBLBF ", " BLBNKKKKKKKKKKKKKKKKKNBLBF ", " BLBNFMVVVVVVVVVVVVVMFNBLBF ", " BLBNFMVVVVVVVVVVVVVMFNBLBF ", " BLBNFMVVVVVVVVVVVVVMFNBLBF ", " BLBNKKKKKKKKKKKKKKKKKNBLBF ", " BLBNKKKKKKKKKKKKKKKKKNBLBF ", " BLBNKKKKKKKKKKKKKKKKKNBLBF ", " BLBNFMVVVVVVVVVVVVVMFNBLBF ", " BLBNFMVVVVVVVVVVVVVMFNBLBF ", " BLBNFMVVVVVVVVVVVVVMFNBLBF ", " BLBNKKKKKKKKKKKKKKKKKNBLBF ", " BLBNKKKKKKKKKKKKKKKKKNBLBF ", " BLBNKKKKKKKKKKKKKKKKKNBLBF ", " BLBNFMVVVVVVVVVVVVVMFNBLBF ", " BLBNFMVVVVVVVVVVVVVMFNBLBF ", " BLBNFMVVVVVVVVVVVVVMFNBLBF ", " BLBNKKKKKKKKKKKKKKKKKNBLBF ", " BLBNKKKKKKKKKKKKKKKKKNBLBF ", " BLBNKKKKKKKKKKKKKKKKKNBLBF ", " BLBNKKKKKKKKKKKKKKKKKNBLBF ", " BLBNKKKKKKKKKKKKKKKKKNBLBF ", " BLBNKKKKKKKKKKKKKKKKKNBLBF ", " BLBGGGGGGGGGGGGGGGGGGGBLB  ", " BBBGGGGGGGGGGGGGGGGGGGBBB  ", " BBBBBBBBBBBBBBBBBBBBBBBBB  ")
                    .aisle(" BJBJJJJJJJJJJJJJJJJJJJBIB  ", " BJBJJJJJJJJJJJJJJJJJJJBBB  ", " BJBJJJJJJJJJJJJJJJJJJJBBB  ", " BJBJJJJJJJJJJJJJJJJJJJBBBBB", " BJBJJJJJJJJJJJJJJJJJJJBBB  ", " BNBJJJJJJJJJJJJJJJJJJNBBB  ", " BBBJJJJJJJJJJJJJJJJJJJBBB  ", " BBBJJJJJJJJJJJJJJJJJJJBBBHH", " BBBJJJJJJJJJJJJJJJJJJJBBB  ", " BBBQQQQQQQQQQQQQQQQQQQBBBF ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBBQQQQQQQQQQQQQQQQQQQBBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBBQQQQQQQQQQQQQQQQQQQBBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBBQQQQQQQQQQQQQQQQQQQBBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBBQQQQQQQQQQQQQQQQQQQBBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBBQQQQQQQQQQQQQQQQQQQBBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBBQQQQQQQQQQQQQQQQQQQBBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBBQQQQQQQQQQQQQQQQQQQBBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBBGGGGGGGGGGGGGGGGGGGBBB  ", " BBBBBBBBBBBBBBBBBBBBBBBBB  ")
                    .aisle(" AAQQQQQQQQQQQQQQQQQQQQGGGG ", "  AJJJJJJJJJJJJJJJJJJJGGGGG ", "  AJJJJJJJJJJJJJJJJJJJGGGGG ", "  AJJJJJJJJJJJJJJJJJJJGGGGGB", " DAJJJJJJJJJJJJJJJJJJJDDDDD ", " ENJJJJJJJJJJJJJJJJJJJNNNNN ", " ENJJJJJJJJJJJJJJJJJJJJJNJJ ", " ENJJJJJJJJJJJJJJJJJJJJJNJJH", " BNJJJJJJJJJJJJJJJJJJJJJNJJ ", " FNQQQQQQQQQQQQQQQQQQQQQNEF ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQ                   QN   ", " BNQ                   QNB  ", "  NQ                   QN   ", "  NQQQQQQQQQQQQQQQQQQQQQN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQQQQQQQQQQQQQQQQQQQQQN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQQQQQQQQQQQQQQQQQQQQQN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQQQQQQQQQQQQQQQQQQQQQN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQQQQQQQQQQQQQQQQQQQQQN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQQQQQQQQQQQQQQQQQQQQQN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQQQQQQQQQQQQQQQQQQQQQN   ", "  N                     N   ", " BN                     NB  ", "  Q                     Q   ", "  Q                     Q   ", " HQ                     QH  ", "  QGGGGGGGGGGGGGGGGGGGGGQ   ", " BBB                   BBB  ")
                    .aisle(" CJQQQQQQQQQQQQQQQQQQQQQQQG ", " CJ                   FF  G ", " CJ                   FF  G ", " CJ                   FF  GB", " DJ                   FFFFD ", " ENPPPPPPPPPPPPPPPPPPBFFK N ", " EO                   FFK J ", " EK                   FFK JH", " BK                   FFK J ", " FKQQQQQQQQQQQQQQQQQQQQQKEF ", "  DQPTTTTTTU   UTTTTTTPQD   ", "  DQPTTTTTTU   UTTTTTTPQD   ", "  DQPTTTTTTU   UTTTTTTPQD   ", " BKQPTTTTTTU   UTTTTTTPQKB  ", "  KQPTTTTTTU   UTTTTTTPQK   ", " AKQQQQQQQQQQQQQQQQQQQQQKA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " APPPTTTTTTU   UTTTTTTPPPA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " AKQQQQQQQQQQQQQQQQQQQQQKA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " APPPTTTTTTU   UTTTTTTPPPA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " AKQQQQQQQQQQQQQQQQQQQQQKA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " APPPTTTTTTU   UTTTTTTPPPA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " AKQQQQQQQQQQQQQQQQQQQQQKA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " APPPTTTTTTU   UTTTTTTPPPA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " AKQQQQQQQQQQQQQQQQQQQQQKA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " APPPTTTTTTU   UTTTTTTPPPA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " AKQQQQQQQQQQQQQQQQQQQQQKA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " APPPTTTTTTU   UTTTTTTPPPA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " AKQQQQQQQQQQQQQQQQQQQQQKA  ", "  K                     K   ", " BK                     KB  ", "  Q                     Q   ", "  Q                     Q   ", " HQ                     QH  ", "  QGGGGGGGGGGGGGGGGGGGGGQ   ", " BBB                   BBB  ")
                    .aisle(" AAQQQQQQQQQQQQQQQQQQQQQQQGA", "  A                       GA", "  A                       GA", "  A                       GB", " DA                   FFFFDA", " ENPPPPPPPPPPPPPPPPPPB  K NA", " EO                     K JA", " EK                     K JH", " BK                     K J ", " FKQQQQQQQQQQQQQQQQQQQQQKEF ", "  DQSSSSSSSU   USSSSSSSQD   ", "  DQSSSSSSSU   USSSSSSSQD   ", "  DQSSSSSSSU   USSSSSSSQD   ", " BKQSSSSSSSU   USSSSSSSQKB  ", "  KQSSSSSSSU   USSSSSSSQK   ", "  KQQQQQQQQQQQQQQQQQQQQQK   ", " GGQSSSSSSSU   USSSSSSSQGG  ", "  DQSSSSSSSU   USSSSSSSQD   ", "  OQSSSSSSSU   USSSSSSSQO   ", " GGQSSSSSSSU   USSSSSSSQGG  ", "  DQSSSSSSSU   USSSSSSSQD   ", "  KQQQQQQQQQQQQQQQQQQQQQK   ", " GGQSSSSSSSU   USSSSSSSQGG  ", "  DQSSSSSSSU   USSSSSSSQD   ", "  OQSSSSSSSU   USSSSSSSQO   ", " GGQSSSSSSSU   USSSSSSSQGG  ", "  DQSSSSSSSU   USSSSSSSQD   ", "  KQQQQQQQQQQQQQQQQQQQQQK   ", " GGQSSSSSSSU   USSSSSSSQGG  ", "  DQSSSSSSSU   USSSSSSSQD   ", "  OQSSSSSSSU   USSSSSSSQO   ", " GGQSSSSSSSU   USSSSSSSQGG  ", "  DQSSSSSSSU   USSSSSSSQD   ", "  KQQQQQQQQQQQQQQQQQQQQQK   ", " GGQSSSSSSSU   USSSSSSSQGG  ", "  DQSSSSSSSU   USSSSSSSQD   ", "  OQSSSSSSSU   USSSSSSSQO   ", " GGQSSSSSSSU   USSSSSSSQGG  ", "  DQSSSSSSSU   USSSSSSSQD   ", "  KQQQQQQQQQQQQQQQQQQQQQK   ", " GGQSSSSSSSU   USSSSSSSQGG  ", "  DQSSSSSSSU   USSSSSSSQD   ", "  OQSSSSSSSU   USSSSSSSQO   ", " GGQSSSSSSSU   USSSSSSSQGG  ", "  DQSSSSSSSU   USSSSSSSQD   ", "  KQQQQQQQQQQQQQQQQQQQQQK   ", " GGQSSSSSSSU   USSSSSSSQGG  ", "  DQSSSSSSSU   USSSSSSSQD   ", "  OQSSSSSSSU   USSSSSSSQO   ", " GGQSSSSSSSU   USSSSSSSQGG  ", "  DQSSSSSSSU   USSSSSSSQD   ", "  KQQQQQQQQQQQQQQQQQQQQQK   ", "  K                     K   ", " BK                     KB  ", "  Q                     Q   ", "  Q                     Q   ", " HQ                     QH  ", "  QGGGGGGGGGGGGGGGGGGGGGQ   ", " BBB                   BBB  ")
                    .aisle(" CJQQQQQQQQQQQQQQQQQQQQQQQG ", " CJ                       G ", " CJ                       G ", " CJ                       GB", " DJ                   FFFFD ", " ENPPPPPPPPPPPPPPPPPPB  K N ", " EO                     K J ", " EK                     K JH", " BK                     K J ", " FKQQQQQQQQQQQQQQQQQQQQQKEF ", "  DQ                   QD   ", "  DQ                   QD   ", "  DQ                   QD   ", " BKQ                   QKB  ", "  KQ                   QK   ", "  KQQQQQQQQQQQQQQQQQQQQQK   ", " GGQ                   QGG  ", "  DQ                   QD   ", "  OQ                   QO   ", " GGQ                   QGG  ", "  DQ                   QD   ", "  KQQQQQQQQQQQQQQQQQQQQQK   ", " GGQ                   QGG  ", "  DQ                   QD   ", "  OQ                   QO   ", " GGQ                   QGG  ", "  DQ                   QD   ", "  KQQQQQQQQQQQQQQQQQQQQQK   ", " GGQ                   QGG  ", "  DQ                   QD   ", "  OQ                   QO   ", " GGQ                   QGG  ", "  DQ                   QD   ", "  KQQQQQQQQQQQQQQQQQQQQQK   ", " GGQ                   QGG  ", "  DQ                   QD   ", "  OQ                   QO   ", " GGQ                   QGG  ", "  DQ                   QD   ", "  KQQQQQQQQQQQQQQQQQQQQQK   ", " GGQ                   QGG  ", "  DQ                   QD   ", "  OQ                   QO   ", " GGQ                   QGG  ", "  DQ                   QD   ", "  KQQQQQQQQQQQQQQQQQQQQQK   ", " GGQ                   QGG  ", "  DQ                   QD   ", "  OQ                   QO   ", " GGQ                   QGG  ", "  DQ                   QD   ", "  KQQQQQQQQQQQQQQQQQQQQQK   ", "  K                     K   ", " BK                     KB  ", "  Q                     Q   ", "  Q                     Q   ", " HQ                     QH  ", "  QGGGGGGGGGGGGGGGGGGGGGQ   ", " BBB                   BBB  ")
                    .aisle(" AAQQQQQQQQQQQQQQQQQQQQQQQGA", "  A                       GA", "  A                       GA", "  A                       GB", " DA                   FFFFDA", " ENPPPPPPPPPPPPPPPPPPB  K NA", " EO                     K JA", " EK                     K JH", " BK                     K J ", " FKQQQQQQQQQQQQQQQQQQQQQKEF ", "  DQSSSSSSSU   USSSSSSSQD   ", "  DQSSSSSSSU   USSSSSSSQD   ", "  DQSSSSSSSU   USSSSSSSQD   ", " BKQSSSSSSSU   USSSSSSSQKB  ", "  KQSSSSSSSU   USSSSSSSQK   ", "  KQQQQQQQQQQQQQQQQQQQQQK   ", " GGQSSSSSSSU   USSSSSSSQGG  ", "  DQSSSSSSSU   USSSSSSSQD   ", "  OQSSSSSSSU   USSSSSSSQO   ", " GGQSSSSSSSU   USSSSSSSQGG  ", "  DQSSSSSSSU   USSSSSSSQD   ", "  KQQQQQQQQQQQQQQQQQQQQQK   ", " GGQSSSSSSSU   USSSSSSSQGG  ", "  DQSSSSSSSU   USSSSSSSQD   ", "  OQSSSSSSSU   USSSSSSSQO   ", " GGQSSSSSSSU   USSSSSSSQGG  ", "  DQSSSSSSSU   USSSSSSSQD   ", "  KQQQQQQQQQQQQQQQQQQQQQK   ", " GGQSSSSSSSU   USSSSSSSQGG  ", "  DQSSSSSSSU   USSSSSSSQD   ", "  OQSSSSSSSU   USSSSSSSQO   ", " GGQSSSSSSSU   USSSSSSSQGG  ", "  DQSSSSSSSU   USSSSSSSQD   ", "  KQQQQQQQQQQQQQQQQQQQQQK   ", " GGQSSSSSSSU   USSSSSSSQGG  ", "  DQSSSSSSSU   USSSSSSSQD   ", "  OQSSSSSSSU   USSSSSSSQO   ", " GGQSSSSSSSU   USSSSSSSQGG  ", "  DQSSSSSSSU   USSSSSSSQD   ", "  KQQQQQQQQQQQQQQQQQQQQQK   ", " GGQSSSSSSSU   USSSSSSSQGG  ", "  DQSSSSSSSU   USSSSSSSQD   ", "  OQSSSSSSSU   USSSSSSSQO   ", " GGQSSSSSSSU   USSSSSSSQGG  ", "  DQSSSSSSSU   USSSSSSSQD   ", "  KQQQQQQQQQQQQQQQQQQQQQK   ", " GGQSSSSSSSU   USSSSSSSQGG  ", "  DQSSSSSSSU   USSSSSSSQD   ", "  OQSSSSSSSU   USSSSSSSQO   ", " GGQSSSSSSSU   USSSSSSSQGG  ", "  DQSSSSSSSU   USSSSSSSQD   ", "  KQQQQQQQQQQQQQQQQQQQQQK   ", "  K                     K   ", " BK                     KB  ", "  Q                     Q   ", "  Q                     Q   ", " HQ                     QH  ", "  QGGGGGGGGGGGGGGGGGGGGGQ   ", " BBB                   BBB  ")
                    .aisle(" CJQQQQQQQQQQQQQQQQQQQQQQQG ", " CJ                       G ", " CJ                       G ", " CJ                       GB", " DJ                   FFFFD ", " ENPPPPPPPPPPPPPPPPPPB  K N ", " EO                     K J ", " EK                     K JH", " BK                     K J ", " FKQQQQQQQQQQQQQQQQQQQQQKEF ", "  DQPTTTTTTU   UTTTTTTPQD   ", "  DQPTTTTTTU   UTTTTTTPQD   ", "  DQPTTTTTTU   UTTTTTTPQD   ", " BKQPTTTTTTU   UTTTTTTPQKB  ", "  KQPTTTTTTU   UTTTTTTPQK   ", " AKQQQQQQQQQQQQQQQQQQQQQKA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " APPPTTTTTTU   UTTTTTTPPPA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " AKQQQQQQQQQQQQQQQQQQQQQKA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " APPPTTTTTTU   UTTTTTTPPPA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " AKQQQQQQQQQQQQQQQQQQQQQKA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " APPPTTTTTTU   UTTTTTTPPPA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " AKQQQQQQQQQQQQQQQQQQQQQKA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " APPPTTTTTTU   UTTTTTTPPPA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " AKQQQQQQQQQQQQQQQQQQQQQKA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " APPPTTTTTTU   UTTTTTTPPPA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " AKQQQQQQQQQQQQQQQQQQQQQKA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " APPPTTTTTTU   UTTTTTTPPPA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " APQPTTTTTTU   UTTTTTTPQPA  ", " AKQQQQQQQQQQQQQQQQQQQQQKA  ", "  K                     K   ", " BK                     KB  ", "  Q                     Q   ", "  Q                     Q   ", " HQ                     QH  ", "  QGGGGGGGGGGGGGGGGGGGGGQ   ", " BBB                   BBB  ")
                    .aisle(" AAQQQQQQQQQQQQQQQQQQQQQQQG ", "  A                       G ", "  A                       G ", "  A                       GB", " DA                   FFFFD ", " ENPPPPPPPPPPPPPPPPPPB  NNN ", " EN                     NJJ ", " EN                     NJJH", " BN                     NJJ ", " FNQQQQQQQQQQQQQQQQQQQQQNEF ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQ                   QN   ", " BNQ                   QNB  ", "  NQ                   QN   ", "  NQQQQQQQQQQQQQQQQQQQQQN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQQQQQQQQQQQQQQQQQQQQQN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQQQQQQQQQQQQQQQQQQQQQN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQQQQQQQQQQQQQQQQQQQQQN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQQQQQQQQQQQQQQQQQQQQQN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQQQQQQQQQQQQQQQQQQQQQN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQ                   QN   ", "  NQQQQQQQQQQQQQQQQQQQQQN   ", "  N                     N   ", " BN                     NB  ", "  Q                     Q   ", "  Q                     Q   ", " HQ                     QH  ", "  QGGGGGGGGGGGGGGGGGGGGGQ   ", " BBB                   BBB  ")
                    .aisle(" BJQQQQQQQQQQQQQQQQQQQQQQQBB", " BJ                   BBBBBB", " BJ                   BBBBBB", " BJ                   BBBBBB", " BJ                   BBBBBB", " BNPPPPPPPPPPPPPPPPPPBBBBBNB", " BBB                  BBBBBB", " BBB                  BBBBBB", " BBB                  BBBBBB", " BBBQQQQQQQQQQQQQQQQQQQBBBFC", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBBQQQQQQQQQQQQQQQQQQQBBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBBQQQQQQQQQQQQQQQQQQQBBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBBQQQQQQQQQQQQQQQQQQQBBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBBQQQQQQQQQQQQQQQQQQQBBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBBQQQQQQQQQQQQQQQQQQQBBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBBQQQQQQQQQQQQQQQQQQQBBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBBQQQQQQQQQQQQQQQQQQQBBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBB                   BBB  ", " BBBGGGGGGGGGGGGGGGGGGGBBB  ", " BBBBBBBBBBBBBBBBBBBBBBBBB  ")
                    .aisle("ABBQQQQQQQQQQQQQQQQQQQQQQQBB", "ABB                       BB", "ABB                       BB", "ABB                       BB", "ABB                       BB", "ABNNNNNNNNNNNNNNNNNNNNNNNNNB", "ABBBNBBBBBBBBBBBBBBBBBNBBBBB", "ABLBNKKKKKKKKKKKKKKKKKNBLBBB", "ABLBNKKKKKKKKKKKKKKKKKNBLBBB", "ABLBNKKKKKKKKKKKKKKKKKNBLBFC", "ABLBNKKKKKKKKKKKKKKKKKNBLB  ", "ABLBNFMVVVVVVVVVVVVVMFNBLB  ", "ABLBNFMVVVVVVVVVVVVVMFNBLB  ", "ABLBNFMVVVVVVVVVVVVVMFNBLB  ", "ABLBNKKKKKKKKKKKKKKKKKNBLB  ", "ABLBNKKKKKKKKKKKKKKKKKNBLB  ", "ABLBNKKKKKKKKKKKKKKKKKNBLB  ", "ABLBNFMVVVVVVVVVVVVVMFNBLB  ", "ABLBNFMVVVVVVVVVVVVVMFNBLB  ", "ABLBNFMVVVVVVVVVVVVVMFNBLB  ", "ABLBNKKKKKKKKKKKKKKKKKNBLB  ", "ABLBNKKKKKKKKKKKKKKKKKNBLB  ", "ABLBNKKKKKKKKKKKKKKKKKNBLB  ", "ABLBNFMVVVVVVVVVVVVVMFNBLB  ", "ABLBNFMVVVVVVVVVVVVVMFNBLB  ", "ABLBNFMVVVVVVVVVVVVVMFNBLB  ", "ABLBNKKKKKKKKKKKKKKKKKNBLB  ", "ABLBNKKKKKKKKKKKKKKKKKNBLB  ", "ABLBNKKKKKKKKKKKKKKKKKNBLB  ", "ABLBNFMVVVVVVVVVVVVVMFNBLB  ", "ABLBNFMVVVVVVVVVVVVVMFNBLB  ", "ABLBNFMVVVVVVVVVVVVVMFNBLB  ", "ABLBNKKKKKKKKKKKKKKKKKNBLB  ", "ABLBNKKKKKKKKKKKKKKKKKNBLB  ", "ABLBNKKKKKKKKKKKKKKKKKNBLB  ", "ABLBNFMVVVVVVVVVVVVVMFNBLB  ", "ABLBNFMVVVVVVVVVVVVVMFNBLB  ", "ABLBNFMVVVVVVVVVVVVVMFNBLB  ", "ABLBNKKKKKKKKKKKKKKKKKNBLB  ", "ABLBNKKKKKKKKKKKKKKKKKNBLB  ", "ABLBNKKKKKKKKKKKKKKKKKNBLB  ", "ABLBNFMVVVVVVVVVVVVVMFNBLB  ", "ABLBNFMVVVVVVVVVVVVVMFNBLB  ", "ABLBNFMVVVVVVVVVVVVVMFNBLB  ", "ABLBNKKKKKKKKKKKKKKKKKNBLB  ", "ABLBNKKKKKKKKKKKKKKKKKNBLB  ", "ABLBNKKKKKKKKKKKKKKKKKNBLB  ", "ABLBNFMVVVVVVVVVVVVVMFNBLB  ", "ABLBNFMVVVVVVVVVVVVVMFNBLB  ", "ABLBNFMVVVVVVVVVVVVVMFNBLB  ", "ABLBNKKKKKKKKKKKKKKKKKNBLB  ", "ABLBNKKKKKKKKKKKKKKKKKNBLB  ", "ABLBNKKKKKKKKKKKKKKKKKNBLB  ", "ABLBNKKKKKKKKKKKKKKKKKNBLB  ", "ABLBNKKKKKKKKKKKKKKKKKNBLB  ", "ABLBNKKKKKKKKKKKKKKKKKNBLB  ", " BLBGGGGGGGGGGGGGGGGGGGBLB  ", " BBBGGGGGGGGGGGGGGGGGGGBBB  ", " BBBBBBBBBBBBBBBBBBBBBBBBB  ")
                    .aisle(" BIQQQQQQQQQQQQQQQQQQQQQQQBB", " BG                       BB", " BG                       BB", " BI                       BB", " BM                       BB", " BMMMMMMMMMMMMMMMMMMMMMMMMNB", " BJJJJJJJJJJJJJJJJJJJJJBBBBB", " BBB                   BBBBB", " BBB                   BBBBB", " BBBBBBBBBBBBBBBBBBBBBBBBBFC", " BKB   UUUUUUUUUUUUU   BKB  ", " BKB  U   H     H   U  BKB  ", " BKB  U   H     H   U  BKB  ", " BKB  U   H     H   U  BKB  ", " BKB   UUUUUUUUUUUUU   BKB  ", " BKB                   BKB  ", " BKB   UUUUUUUUUUUUU   BKB  ", " BKB  U   H     H   U  BKB  ", " BKB  U   H     H   U  BKB  ", " BKB  U   H     H   U  BKB  ", " BKB   UUUUUUUUUUUUU   BKB  ", " BKB                   BKB  ", " BKB   UUUUUUUUUUUUU   BKB  ", " BKB  U   H     H   U  BKB  ", " BKB  U   H     H   U  BKB  ", " BKB  U   H     H   U  BKB  ", " BKB   UUUUUUUUUUUUU   BKB  ", " BKB                   BKB  ", " BKB   UUUUUUUUUUUUU   BKB  ", " BKB  U   H     H   U  BKB  ", " BKB  U   H     H   U  BKB  ", " BKB  U   H     H   U  BKB  ", " BKB   UUUUUUUUUUUUU   BKB  ", " BKB                   BKB  ", " BKB   UUUUUUUUUUUUU   BKB  ", " BKB  U   H     H   U  BKB  ", " BKB  U   H     H   U  BKB  ", " BKB  U   H     H   U  BKB  ", " BKB   UUUUUUUUUUUUU   BKB  ", " BKB                   BKB  ", " BKB   UUUUUUUUUUUUU   BKB  ", " BKB  U   H     H   U  BKB  ", " BKB  U   H     H   U  BKB  ", " BKB  U   H     H   U  BKB  ", " BKB   UUUUUUUUUUUUU   BKB  ", " BKB                   BKB  ", " BKB   UUUUUUUUUUUUU   BKB  ", " BKB  U   H     H   U  BKB  ", " BKB  U   H     H   U  BKB  ", " BKB  U   H     H   U  BKB  ", " BKB   UUUUUUUUUUUUU   BKB  ", " BKB                   BKB  ", " BKBBBBBBBBBBBBBBBBBBBBBKB  ", " BKBBBBBBBBBBBBBBBBBBBBBKB  ", " BKB                   BKB  ", " BKB                   BKB  ", " BKBAAAAAAAAAAAAAAAAAAABKB  ", " BBB                   BBB  ", "                            ")
                    .aisle("  IQQQQQQQQQQQQQQQQQQQQQQQI ", "  G                       J ", "  G                       J ", "  I                       J ", " DM                       IE", " EMMMMMMMMMMMMMMMMMMMMMMMMNE", " EJGGGGGGGGGGGGGGGGGGGGGGGBE", " E                        F ", " B                        F ", "                          F ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ")
                    .aisle("  IQQQQQQQQQQQQQQQQQQQQQQQI ", "  G                       J ", "  G                       J ", "  I                       J ", " DM                       IE", " EMMMMMMMMMMMMMMMMMMMMMMMMNE", " EJGHHHHHHHHHHHHHHHHHHHHHGBE", " E                        F ", " B                          ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ")
                    .aisle("  IQQQQQQQQQQQQQQQQQQQQQQQI ", "  G                       J ", "  G                       J ", "  I                       J ", " DM                       IE", " EMMMMMMMMMMMMMMMMMMMMMMMMNE", " EJGHHHHHHHHHHHHHHHHHHHHHGBE", " E                        F ", " B                          ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ")
                    .aisle("  IQQQQQQQQQQQQQQQQQQQQQQQI ", "  G                       J ", "  G                       J ", "  I                       J ", " DM                       IE", " EMMMMMMMMMMMMMMMMMMMMMMMMNE", " EJGHHHHHHHHHHHHHHHHHHHHHGBE", " E                        F ", " B                          ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ")
                    .aisle("  IQQQQQQQQQQQQQQQQQQQQQQQI ", "  G                       J ", "  G                       J ", "  I                       J ", " DM                       IE", " EMMMMMMMMMMMMMMMMMMMMMMMMNE", " EJGGGGGGGGGGGGGGGGGGGGGGGBE", " E                        F ", " B                          ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ")
                    .aisle(" BIBIIIIIIIIIIIIIIIIQQQQQQBB", " BGBRDDRRDDRRDDRIIIIPPPPPPBB", " BGBRDDRRDDRRDDRIIIIPPPPPPBB", " BIBRDDRRDDRRDDRIIIIPPPPPPBB", " BBBBBBBBBBBBBBBBBBBPPPPPPBB", " BBBBBBBBBBBBBBOBBBBMMMMMMBB", " BBBJJJJJJJJJJJJJBBBJJJJJJBB", " BBB             BBB     BBB", " BBB             BBB     BBB", "                          H ", "                          H ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ")
                    .aisle(" BIBAAAAAAAAAAAAABBBRRRRRBBB", " BLB            ABLBRRWRRBLB", " BLB            ABLBRRRRRBLB", " BLB            ABLBRRRRRBLB", " BLBCCCCCCCCCCCCABLBIIIIIBLB", " BLBOOOOOOOOOOOOABLBBBBBBBLB", " BLBAAAAAAAAAAAAABLBBBBBBBLB", " BBBFFFFFFFFFFFFFBBBFFFFFBBB", " BBB             BBB     BBB", "                          BB", "                          B ", "                          B ", "                          H ", "                          H ", "                          X ", "                          X ", "                          Y ", "                          Y ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ")
                    .aisle(" BBB             BBB     BBB", " BBB             BBB     BBB", " BBB             BBB     BBB", " BBB             BBB     BBB", " BBBEEEEEEEEEEEEEBBBEEEEEBBB", " BBBEEEEEEEEEEEEEBBBEEEEEBBB", " BBBEEEEEEEEEEEEEBBBEEEEEBBB", " BBB             BBB     BBB", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ", "                            ")
                    .where('A', blocks(GTOBlocks.NAQUADAH_ALLOY_CASING.get()))
                    .where('B', blocks(GTOBlocks.IRIDIUM_CASING.get()))
                    .where('C', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTOMaterials.BabbittAlloy)))
                    .where('D', blocks(GTOBlocks.STRONTIUM_CARBONATE_CERAMIC_RAY_ABSORBING_MECHANICAL_CUBE.get()))
                    .where('E', blocks(GTBlocks.MACHINE_CASING_UHV.get()))
                    .where('F', blocks(GCYMBlocks.CASING_NONCONDUCTING.get()))
                    .where('G', blocks(GTOBlocks.ANTIFREEZE_HEATPROOF_MACHINE_CASING.get()))
                    .where('H', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTOMaterials.HastelloyN)))
                    .where('I', blocks(GTOBlocks.OXIDATION_RESISTANT_HASTELLOY_N_MECHANICAL_CASING.get()))
                    .where('J', blocks(GTOBlocks.PRESSURE_CONTAINMENT_CASING.get()))
                    .where('K', GTOPredicates.absBlocks())
                    .where('L', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.Ruridit)))
                    .where('M', blocks(GTOBlocks.LITHIUM_OXIDE_CERAMIC_HEAT_RESISTANT_SHOCK_RESISTANT_MECHANICAL_CUBE.get()))
                    .where('N', blocks(GTBlocks.ADVANCED_COMPUTER_CASING.get()))
                    .where('O', blocks(GTBlocks.HIGH_POWER_CASING.get()))
                    .where('P', blocks(GTOBlocks.COBALT_OXIDE_CERAMIC_STRONG_THERMALLY_CONDUCTIVE_MECHANICAL_BLOCK.get()))
                    .where('Q', blocks(GTOBlocks.MC_NYLON_TENSILE_MECHANICAL_SHELL.get()))
                    .where('R', blocks(GTOBlocks.OXIDATION_RESISTANT_HASTELLOY_N_MECHANICAL_CASING.get())
                            .or(blocks(GTOMachines.THERMAL_CONDUCTOR_HATCH.get()).setMaxGlobalLimited(1))
                            .or(abilities(IMPORT_ITEMS))
                            .or(abilities(IMPORT_FLUIDS))
                            .or(abilities(EXPORT_ITEMS))
                            .or(abilities(EXPORT_FLUIDS))
                            .or(abilities(INPUT_ENERGY).setMaxGlobalLimited(2))
                            .or(abilities(COMPUTATION_DATA_TRANSMISSION).setMaxGlobalLimited(1))
                            .or(abilities(MAINTENANCE).setExactLimit(1)))
                    .where('S', GTOPredicates.tierBlock(BlockMap.COMPUTER_HEAT_MAP, GTOValues.COMPUTER_HEAT_TIER))
                    .where('T', abilities(GTOPartAbility.COMPUTING_COMPONENT, HPCA_COMPONENT))
                    .where('U', GTOPredicates.tierBlock(BlockMap.COMPUTER_CASING_MAP, GTOValues.COMPUTER_CASING_TIER))
                    .where('V', GTOPredicates.glass())
                    .where('W', controller(blocks(definition.get())))
                    .where('X', blocks(Blocks.ANDESITE_WALL))
                    .where('Y', blocks(Blocks.IRON_BARS))
                    .where(' ', any())
                    .build())
            .workableCasingRenderer(GTOCore.id("block/casings/oxidation_resistant_hastelloy_n_mechanical_casing"), GTCEu.id("block/multiblock/large_miner"))
            .register();

    public static final MachineDefinition NICH_EMPTY_COMPONENT = registerHPCAPart(
            "nich_empty_component", "空NICH组件",
            ExResearchEmptyPartMachine::new, false, false, 3)
            .register();

    public static final MachineDefinition NICH_COMPUTING_COMPONENTS = registerHPCAPart(
            "nich_computing_components", "NICH计算组件",
            holder -> new ExResearchComputationPartMachine(holder, 3), true, true, 3)
            .tooltips(
                    Component.translatable("gtceu.machine.hpca.component_general.upkeep_eut", GTValues.VA[ZPM]),
                    Component.translatable("gtceu.machine.hpca.component_general.max_eut", GTValues.VA[GTValues.UHV]),
                    Component.translatable("gtceu.machine.hpca.component_type.computation_cwut", 64),
                    Component.translatable("gtceu.machine.hpca.component_type.computation_cooling", 16))
            .tooltipBuilder(OVERHEAT_TOOLTIPS)
            .register();

    public static final MachineDefinition NICH_COOLING_COMPONENTS = registerHPCAPart(
            "nich_cooling_components", "NICH冷却组件",
            holder -> new ExResearchCoolerPartMachine(holder, 3), true, false, 3)
            .tooltips(Component.translatable("gtceu.machine.hpca.component_general.max_eut", GTValues.VA[GTValues.UV]),
                    Component.translatable("gtceu.machine.hpca.component_type.cooler_active"),
                    Component.translatable("gtceu.machine.hpca.component_type.cooler_active_coolant",
                            80, GTMaterials.Helium.getLocalizedName()),
                    Component.translatable("gtceu.machine.hpca.component_type.cooler_cooling", 8))
            .register();

    public static final MachineDefinition NICH_BRIDGE_COMPONENT = registerHPCAPart(
            "nich_bridge_component", "NICH桥接组件",
            holder -> new ExResearchBridgePartMachine(holder, 3), true, false, 3)
            .tooltips(Component.translatable("gtceu.machine.hpca.component_type.bridge"),
                    Component.translatable("gtceu.machine.hpca.component_general.max_eut", GTValues.VA[GTValues.UHV]))
            .register();

    public static final MachineDefinition GWCA_EMPTY_COMPONENT = registerHPCAPart(
            "gwca_empty_component", "空GWCA组件",
            ExResearchEmptyPartMachine::new, false, false, 4)
            .register();

    public static final MachineDefinition GWCA_COMPUTING_COMPONENTS = registerHPCAPart(
            "gwca_computing_components", "GWCA计算组件",
            holder -> new ExResearchComputationPartMachine(holder, 4), true, true, 4)
            .tooltips(
                    Component.translatable("gtceu.machine.hpca.component_general.upkeep_eut", GTValues.VA[GTValues.UV]),
                    Component.translatable("gtceu.machine.hpca.component_general.max_eut", GTValues.VA[GTValues.UEV]),
                    Component.translatable("gtceu.machine.hpca.component_type.computation_cwut", 1024),
                    Component.translatable("gtceu.machine.hpca.component_type.computation_cooling", 256))
            .tooltipBuilder(OVERHEAT_TOOLTIPS)
            .register();

    public static final MachineDefinition GWCA_COOLING_COMPONENTS = registerHPCAPart(
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
        return GTO.machine(name, constructor)
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

    /////////////////////////////////////
    // *********** 数据机器 *********** //
    /////////////////////////////////////

    public static final MachineDefinition BIO_DATA_ACCESS_HATCH = machine("bio_data_access_hatch", "生物数据访问仓", (holder) -> new DataAccessHatchMachine(holder, UHV, false))
            .tier(UHV)
            .allRotation()
            .abilities(DATA_ACCESS)
            .tooltips(Component.translatable("gtceu.machine.data_access_hatch.tooltip.0"),
                    Component.translatable("gtceu.machine.data_access_hatch.tooltip.1", 25))
            .notAllowSharedTooltips()

            .renderer(() -> new OverlayTieredMachineRenderer(UHV, GTCEu.id("block/machine/part/data_access_hatch")))
            .register();

    public static final MachineDefinition BLACK_HOLE_DATA_ACCESS_HATCH = machine("black_hole_data_access_hatch", "黑洞数据访问仓", (holder) -> new DataAccessHatchMachine(holder, UIV, false))
            .tier(UIV)
            .allRotation()
            .abilities(DATA_ACCESS)
            .tooltips(Component.translatable("gtceu.machine.data_access_hatch.tooltip.0"),
                    Component.translatable("gtceu.machine.data_access_hatch.tooltip.1", 36))
            .notAllowSharedTooltips()

            .renderer(() -> new OverlayTieredMachineRenderer(UIV, GTCEu.id("block/machine/part/data_access_hatch")))
            .register();

    public static final MachineDefinition VIRTUAL_UNIVERSE_DATA_ACCESS_HATCH = machine("virtual_universe_data_access_hatch", "虚拟宇宙数据访问仓", (holder) -> new DataAccessHatchMachine(holder, OpV, false))
            .tier(OpV)
            .allRotation()
            .abilities(DATA_ACCESS)
            .tooltips(Component.translatable("gtceu.machine.data_access_hatch.tooltip.0"),
                    Component.translatable("gtceu.machine.data_access_hatch.tooltip.1", 49))
            .notAllowSharedTooltips()

            .renderer(() -> new OverlayTieredMachineRenderer(OpV, GTCEu.id("block/machine/part/data_access_hatch")))
            .register();

    /////////////////////////////////////
    // *********** 研究机器 *********** //
    /////////////////////////////////////

    public static final MachineDefinition SCANNING_HOLDER = machine("scanning_holder", "扫描支架", ScanningHolderMachine::new)
            .tier(IV)
            .allRotation()
            .renderer(() -> new OverlayTieredActiveMachineRenderer(IV, GTCEu.id("block/machine/part/object_holder"),
                    GTCEu.id("block/machine/part/object_holder_active")))
            .notAllowSharedTooltips()
            .register();

    public static final MachineDefinition ANALYZE_HOLDER = machine("analyze_holder", "分析支架", AnalyzeHolderMachine::new)
            .tier(IV)
            .allRotation()
            .renderer(() -> new OverlayTieredActiveMachineRenderer(IV, GTCEu.id("block/machine/part/object_holder"),
                    GTCEu.id("block/machine/part/object_holder_active")))
            .notAllowSharedTooltips()
            .register();

    public static final MachineDefinition RESEARCH_HOLDER = machine("research_holder", "研究支架", ResearchHolderMachine::new)
            .tier(IV)
            .allRotation()
            .renderer(() -> new OverlayTieredActiveMachineRenderer(IV, GTCEu.id("block/machine/part/object_holder"),
                    GTCEu.id("block/machine/part/object_holder_active")))
            .notAllowSharedTooltips()
            .register();

    public static final MachineDefinition DATA_GENERATE_HOLDER = machine("data_generate_holder", "数据构建支架", DataGenerateHolderMachine::new)
            .tier(IV)
            .allRotation()
            .renderer(() -> new OverlayTieredActiveMachineRenderer(IV, GTCEu.id("block/machine/part/object_holder"),
                    GTCEu.id("block/machine/part/object_holder_active")))
            .notAllowSharedTooltips()
            .register();

    public static final MultiblockMachineDefinition PRIMORDIAL_SCANNING_STATION = multiblock("primordial_scanning_station", "基元扫描站", ScanningStationMachine::new)
            .tooltipsText("精密的多方块扫描仪。", "Precision multi-block scanner.")
            .tooltipsText("用于扫描§b数据晶片§r。", "Used to scan onto §fData Crystal§7.")
            .tooltipsText("需要§b算力§r来进行工作。", "Requires §fComputation§7 to work.")
            .tooltipsText("提供更多的算力可以使研究进展的更快。", "Providing more Computation allows the recipe to run faster.")
            .nonYAxisRotation()
            .recipeTypes(CRYSTAL_SCAN_RECIPES)
            .block(ADVANCED_COMPUTER_CASING)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("XXX", "VVV", "PPP", "PPP", "PPP", "VVV", "XXX")
                    .aisle("XXX", "VAV", "AAA", "AAA", "AAA", "VAV", "XXX")
                    .aisle("XXX", "VAV", "XAX", "XSX", "XAX", "VAV", "XXX")
                    .aisle("XXX", "XAX", "---", "---", "---", "XAX", "XXX")
                    .aisle(" X ", "XAX", "---", "---", "---", "XAX", " X ")
                    .aisle(" X ", "XAX", "-A-", "-H-", "-A-", "XAX", " X ")
                    .aisle("   ", "XXX", "---", "---", "---", "XXX", "   ")
                    .where('S', controller(blocks(definition.getBlock())))
                    .where('X', blocks(COMPUTER_CASING.get()))
                    .where(' ', any())
                    .where('-', air())
                    .where('V', blocks(COMPUTER_HEAT_VENT.get()))
                    .where('A', blocks(ADVANCED_COMPUTER_CASING.get()))
                    .where('P', blocks(COMPUTER_CASING.get())
                            .or(abilities(PartAbility.INPUT_ENERGY).setMinGlobalLimited(1).setMaxGlobalLimited(2, 1))
                            .or(abilities(PartAbility.COMPUTATION_DATA_RECEPTION).setExactLimit(1))
                            .or(autoAbilities(true, false, false)))
                    .where('H', blocks(SCANNING_HOLDER.get()))
                    .build())
            .shapeInfo(definition -> MultiblockShapeInfo.builder()
                    .aisle("---", "XXX", "---", "---", "---", "XXX", "---")
                    .aisle("-X-", "XAX", "-A-", "-H-", "-A-", "XAX", "-X-")
                    .aisle("-X-", "XAX", "---", "---", "---", "XAX", "-X-")
                    .aisle("XXX", "XAX", "---", "---", "---", "XAX", "XXX")
                    .aisle("XXX", "VAV", "XAX", "XSX", "XAX", "VAV", "XXX")
                    .aisle("XXX", "VAV", "AAA", "AAA", "AAA", "VAV", "XXX")
                    .aisle("XXX", "VVV", "POP", "PEP", "PMP", "VVV", "XXX")
                    .where('S', ExResearchMachines.PRIMORDIAL_SCANNING_STATION, Direction.NORTH)
                    .where('X', COMPUTER_CASING.get())
                    .where('-', Blocks.AIR)
                    .where('V', COMPUTER_HEAT_VENT.get())
                    .where('A', ADVANCED_COMPUTER_CASING.get())
                    .where('P', COMPUTER_CASING.get())
                    .where('O', GTResearchMachines.COMPUTATION_HATCH_RECEIVER, Direction.SOUTH)
                    .where('E', GTMachines.ENERGY_INPUT_HATCH[GTValues.LuV], Direction.SOUTH)
                    .where('M', GTMachines.MAINTENANCE_HATCH.get(), Direction.SOUTH)
                    .where('H', SCANNING_HOLDER.get(), Direction.SOUTH)
                    .build())
            .sidedWorkableCasingRenderer("block/casings/hpca/advanced_computer_casing",
                    GTCEu.id("block/multiblock/research_station"))
            .register();

    public static final MultiblockMachineDefinition ANALYSIS_AND_RESEARCH_CENTER = multiblock("analysis_and_research_center", "分析推演中心", AnalysisAndResearchCenterMachine::new)
            .tooltipsText("分析/推演的一体化机器。", "An all-in-one analysis/deduction machine.")
            .tooltipsText("根据§b扫描数据§r得到§b研究数据§r。", "§bResearch data§r is obtained based on §bscanning data§r.")
            .tooltipsText("需要§b算力§r来进行工作。", "Requires §fComputation§7 to work.")
            .tooltipsText("提供更多的算力可以使研究进展的更快。", "Providing more Computation allows the recipe to run faster.")
            .nonYAxisRotation()
            .recipeTypes(DATA_ANALYSIS_RECIPES, DATA_INTEGRATION_RECIPES)
            .block(ADVANCED_COMPUTER_CASING)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("AAABAAA", "CCCBCCC", "CC B CC", "C     C")
                    .aisle("AAABAAA", "CC   CC", "C     C", "       ")
                    .aisle("AAABAAA", "C  B  C", "       ", "       ")
                    .aisle("BBBDBBB", "B BBB B", "B  B  B", "   E   ")
                    .aisle("AAABAAA", "C  B  C", "       ", "       ")
                    .aisle("AAABAAA", "CC   CC", "C     C", "       ")
                    .aisle("AAABAAA", "CCCBCCC", "CC F CC", "C     C")
                    .where('A', blocks(GTBlocks.HIGH_POWER_CASING.get()))
                    .where('B', blocks(GTBlocks.ADVANCED_COMPUTER_CASING.get())
                            .or(abilities(PartAbility.INPUT_ENERGY).setMinGlobalLimited(1).setMaxGlobalLimited(2, 1))
                            .or(abilities(PartAbility.COMPUTATION_DATA_RECEPTION).setExactLimit(1))
                            .or(abilities(MAINTENANCE).setExactLimit(1)))
                    .where('C', blocks(GTBlocks.COMPUTER_CASING.get()))
                    .where('D', abilities(PartAbility.EXPORT_ITEMS))
                    .where('E', blocks(ANALYZE_HOLDER.get())
                            .or(blocks(RESEARCH_HOLDER.get())))
                    .where('F', controller(blocks(definition.getBlock())))
                    .where(' ', any())
                    .build())
            .shapeInfos(definition -> {
                List<MultiblockShapeInfo> shapeInfo = new ArrayList<>();
                MultiblockShapeInfo.ShapeInfoBuilder builder = MultiblockShapeInfo.builder()
                        .aisle("AAABAAA", "CCCHCCC", "CC H CC", "C     C")
                        .aisle("AAABAAA", "CC   CC", "C     C", "       ")
                        .aisle("AAABAAA", "C  B  C", "       ", "       ")
                        .aisle("BBBDBBB", "B BBB B", "B  B  B", "   E   ")
                        .aisle("AAABAAA", "C  B  C", "       ", "       ")
                        .aisle("AAABAAA", "CC   CC", "C     C", "       ")
                        .aisle("AAAIAAA", "CCCGCCC", "CC F CC", "C     C")
                        .where('A', GTBlocks.HIGH_POWER_CASING.get())
                        .where('B', GTBlocks.ADVANCED_COMPUTER_CASING.get())
                        .where('C', GTBlocks.COMPUTER_CASING.get())
                        .where('D', GTMachines.ITEM_EXPORT_BUS[GTValues.ZPM].get(), Direction.DOWN)
                        .where('F', ExResearchMachines.ANALYSIS_AND_RESEARCH_CENTER, Direction.SOUTH)
                        .where(' ', Blocks.AIR)
                        .where('G', GTMachines.MAINTENANCE_HATCH.get(), Direction.SOUTH)
                        .where('H', GTMachines.ENERGY_INPUT_HATCH[IV], Direction.NORTH)
                        .where('I', GTResearchMachines.COMPUTATION_HATCH_RECEIVER, Direction.SOUTH);
                shapeInfo.add(builder.shallowCopy()
                        .where('E', ANALYZE_HOLDER.get(), Direction.UP)
                        .build());
                shapeInfo.add(builder.shallowCopy()
                        .where('E', RESEARCH_HOLDER.get(), Direction.UP)
                        .build());
                return shapeInfo;
            })
            .sidedWorkableCasingRenderer("block/casings/hpca/advanced_computer_casing", GTCEu.id("block/multiblock/research_station"))
            .register();

    public static final MultiblockMachineDefinition SYNTHETIC_DATA_ASSEMBLY_PLANT = multiblock("synthetic_data_assembly_plant", "合成数据组装厂", SyntheticDataAssemblyPlantMachine::new)
            .tooltipsText("分析/推演的一体化机器。", "Precision multi-block scanner.")
            .tooltipsText("根据§b扫描数据§r得到§b研究数据§r。", "Precision multi-block scanner.")
            .tooltipsText("需要§b算力§r来进行工作。", "Requires §fComputation§7 to work.")
            .tooltipsText("提供更多的算力可以使研究进展的更快。", "Providing more Computation allows the recipe to run faster.")
            .nonYAxisRotation()
            .recipeTypes(RECIPES_DATA_GENERATE_RECIPES)
            .block(ADVANCED_COMPUTER_CASING)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("  AABAA   ", " AACCCAA  ", "CCCCECCCC ", " AACCCAA  ", "  AABAA   ")
                    .aisle(" AABBBAA  ", "AAACCCAAA ", "CACCCCCAC ", "AAACCCAAA ", " AABBBAA  ")
                    .aisle("  AABAA   ", " AACCCAA  ", "CCCCDCCCC ", " AACCCAA  ", "  AABAA   ")
                    .where('A', blocks(GTBlocks.HIGH_POWER_CASING.get()))
                    .where('B', blocks(GTBlocks.COMPUTER_HEAT_VENT.get()))
                    .where('C', blocks(GTBlocks.ADVANCED_COMPUTER_CASING.get())
                            .or(abilities(PartAbility.INPUT_ENERGY).setMinGlobalLimited(1).setMaxGlobalLimited(2, 1))
                            .or(abilities(PartAbility.COMPUTATION_DATA_RECEPTION).setExactLimit(1))
                            .or(abilities(MAINTENANCE).setExactLimit(1)))
                    .where('D', controller(blocks(definition.getBlock())))
                    .where('E', blocks(DATA_GENERATE_HOLDER.get()))
                    .where(' ', any())
                    .build())
            .shapeInfo(definition -> MultiblockShapeInfo.builder()
                    .aisle("  AABAA   ", " AACCCAA  ", "CCCCECCCC ", " AACCCAA  ", "  AABAA   ")
                    .aisle(" AABBBAA  ", "AAACCCAAA ", "CACCCCCAC ", "AAACCCAAA ", " AABBBAA  ")
                    .aisle("  AABAA   ", " AAHIJAA  ", "CCCCDCCCC ", " AACCCAA  ", "  AABAA   ")
                    .where('A', GTBlocks.HIGH_POWER_CASING.get())
                    .where('B', COMPUTER_HEAT_VENT.get())
                    .where('C', ADVANCED_COMPUTER_CASING.get())
                    .where('E', ExResearchMachines.SYNTHETIC_DATA_ASSEMBLY_PLANT, Direction.NORTH)
                    .where('D', DATA_GENERATE_HOLDER, Direction.SOUTH)
                    .where(' ', Blocks.AIR)
                    .where('H', GTResearchMachines.COMPUTATION_HATCH_RECEIVER, Direction.SOUTH)
                    .where('I', GTMachines.ENERGY_INPUT_HATCH[GTValues.LuV], Direction.SOUTH)
                    .where('J', GTMachines.MAINTENANCE_HATCH.get(), Direction.SOUTH)
                    .build())
            .sidedWorkableCasingRenderer("block/casings/hpca/advanced_computer_casing", GTCEu.id("block/multiblock/research_station"))
            .register();
}
