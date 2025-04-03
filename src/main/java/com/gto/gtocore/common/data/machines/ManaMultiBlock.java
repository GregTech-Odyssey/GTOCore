package com.gto.gtocore.common.data.machines;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.machine.part.GTOPartAbility;
import com.gto.gtocore.common.data.GTOBlocks;
import com.gto.gtocore.common.data.GTORecipeModifiers;
import com.gto.gtocore.common.data.GTORecipeTypes;
import com.gto.gtocore.common.machine.mana.multiblock.ManaDistributorMachine;
import com.gto.gtocore.common.machine.mana.multiblock.ManaMultiblockMachine;
import com.gto.gtocore.utils.RLUtils;
import com.gto.gtocore.utils.RegistriesUtils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import net.minecraft.world.level.block.Blocks;

import static com.gregtechceu.gtceu.api.machine.multiblock.PartAbility.*;
import static com.gregtechceu.gtceu.api.pattern.Predicates.*;
import static com.gto.gtocore.utils.register.MachineRegisterUtils.multiblock;

public interface ManaMultiBlock {

    static void init() {}

    MultiblockMachineDefinition BASE_MANA_DISTRIBUTOR = multiblock("base_mana_distributor", "基础魔力分配器", ManaDistributorMachine.create(16, 8))
            .nonYAxisRotation()
            .tooltipsText("Automatically provides mana to nearby mana machines", "自动为附近的魔力机器提供魔力")
            .tooltipsKey("gtocore.machine.maximum_amount", 16)
            .tooltipsKey("gui.ae2.WirelessRange", 8)
            .recipe(GTRecipeTypes.DUMMY_RECIPES)
            .block(RegistriesUtils.getSupplierBlock("botania:livingrock_bricks"))
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle(" AbA ", "     ", "     ", "     ", "     ", " ACA ")
                    .aisle("ADDDA", " EBE ", " F F ", " A A ", " A A ", "ABEBA")
                    .aisle("bD Db", " BEB ", "  G  ", "  H  ", "  I  ", "CEIEC")
                    .aisle("ADDDA", " EBE ", " F F ", " A A ", " A A ", "ABEBA")
                    .aisle(" A~A ", "     ", "     ", "     ", "     ", " ACA ")
                    .where('~', controller(blocks(definition.get())))
                    .where('A', blocks(RegistriesUtils.getBlock("botania:livingrock_bricks_wall")))
                    .where('B', blocks(RegistriesUtils.getBlock("botania:livingrock_bricks")))
                    .where('b', blocks(RegistriesUtils.getBlock("botania:livingrock_bricks")).or(abilities(GTOPartAbility.INPUT_MANA)).or(abilities(GTOPartAbility.EXTRACT_MANA)))
                    .where('C', blocks(Blocks.BIRCH_FENCE_GATE))
                    .where('D', blocks(RegistriesUtils.getBlock("botania:livingrock")))
                    .where('E', blocks(RegistriesUtils.getBlock("botania:chiseled_livingrock_bricks")))
                    .where('F', blocks(RegistriesUtils.getBlock("botania:apothecary_livingrock")))
                    .where('G', blocks(RegistriesUtils.getBlock("botania:mana_pool")))
                    .where('H', blocks(RegistriesUtils.getBlock("botania:mana_pylon")))
                    .where('I', blocks(RegistriesUtils.getBlock("botania:mana_glass")))
                    .where(' ', any())
                    .build())
            .workableCasingRenderer(RLUtils.bot("block/livingrock_bricks"), GTOCore.id("block/multiblock/mana"))
            .register();

    MultiblockMachineDefinition ADVANCED_MANA_DISTRIBUTOR = multiblock("advanced_mana_distributor", "进阶魔力分配器", ManaDistributorMachine.create(64, 32))
            .nonYAxisRotation()
            .tooltipsKey("gtocore.machine.base_mana_distributor.tooltip.0")
            .tooltipsKey("gtocore.machine.maximum_amount", 64)
            .tooltipsKey("gui.ae2.WirelessRange", 32)
            .recipe(GTRecipeTypes.DUMMY_RECIPES)
            .block(RegistriesUtils.getSupplierBlock("botania:livingrock_bricks"))
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("  BcccB  ", "         ", "         ", "         ", "         ", "         ", "         ", "         ", "         ", "         ")
                    .aisle(" BAAAAAB ", " BAB BAB ", "         ", "         ", "         ", "         ", "  B   B  ", " BCCBCCB ", "         ", "         ")
                    .aisle("BAAAAAAAB", " AAAcAAA ", "  D   D  ", "  B   B  ", "  E   E  ", "  B   B  ", " BCCFCCB ", " CCB BCC ", "   GGG   ", "         ")
                    .aisle("cAAAAAAAc", " BAFCFAB ", "         ", "         ", "         ", "         ", "  C   C  ", " CB   BC ", "  GGGGG  ", "    G    ")
                    .aisle("cAAAAAAAc", "  cCFCc  ", "    H    ", "    I    ", "         ", "         ", "  F   F  ", " B     B ", "  GGGGG  ", "   GGG   ")
                    .aisle("cAAAAAAAc", " BAFCFAB ", "         ", "         ", "         ", "         ", "  C   C  ", " CB   BC ", "  GGGGG  ", "    G    ")
                    .aisle("BAAAAAAAB", " AAA~AAA ", "  D   D  ", "  B   B  ", "  E   E  ", "  B   B  ", " BCCFCCB ", " CCB BCC ", "   GGG   ", "         ")
                    .aisle(" BAAAAAB ", " BAB BAB ", "         ", "         ", "         ", "         ", "  B   B  ", " BCCBCCB ", "         ", "         ")
                    .aisle("  BcccB  ", "         ", "         ", "         ", "         ", "         ", "         ", "         ", "         ", "         ")
                    .where('~', controller(blocks(definition.get())))
                    .where('A', blocks(RegistriesUtils.getBlock("botania:livingrock")))
                    .where('B', blocks(RegistriesUtils.getBlock("botania:livingrock_bricks_wall")))
                    .where('C', blocks(RegistriesUtils.getBlock("botania:livingrock_bricks")))
                    .where('c', blocks(RegistriesUtils.getBlock("botania:livingrock_bricks")).or(abilities(GTOPartAbility.INPUT_MANA)).or(abilities(GTOPartAbility.EXTRACT_MANA)))
                    .where('D', blocks(RegistriesUtils.getBlock("botania:apothecary_livingrock")))
                    .where('E', blocks(RegistriesUtils.getBlock("botania:mana_pylon")))
                    .where('F', blocks(RegistriesUtils.getBlock("botania:chiseled_livingrock_bricks")))
                    .where('G', blocks(RegistriesUtils.getBlock("botania:mana_glass")))
                    .where('H', blocks(RegistriesUtils.getBlock("botania:mana_pool")))
                    .where('I', blocks(RegistriesUtils.getBlock("botania:natura_pylon")))
                    .where(' ', any())
                    .build())
            .workableCasingRenderer(RLUtils.bot("block/livingrock_bricks"), GTOCore.id("block/multiblock/mana"))
            .register();

    MultiblockMachineDefinition MANA_INFUSER = multiblock("mana_infuser", "魔力灌注机", ManaMultiblockMachine::new)
            .nonYAxisRotation()
            .parallelizableTooltips()
            .recipeModifiers(GTORecipeModifiers.HATCH_PARALLEL)
            .recipe(GTORecipeTypes.MANA_INFUSER_RECIPES)
            .block(GTOBlocks.MANASTEEL_CASING)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("    AAA ", "    ABA ", "    ABA ", "    ABA ", "    AAA ", "        ", "        ")
                    .aisle("AAAAAAAA", "AAAA   A", " AAA   A", "   A   A", "   AAAAA", "        ", "        ")
                    .aisle("ADDAAAAA", "AEEA F B", "AEAA F B", " G A F B", " G AAEAA", " G   G  ", " GGGGG  ")
                    .aisle("AAAAAAAA", "ACAA   A", " AAA   A", "   A   A", "   AAAAA", "        ", "        ")
                    .aisle("    AAA ", "    ABA ", "    ABA ", "    ABA ", "    AAA ", "        ", "        ")
                    .where('A', blocks(GTOBlocks.MANASTEEL_CASING.get())
                            .or(abilities(GTOPartAbility.INPUT_MANA).setMaxGlobalLimited(4, 1))
                            .or(abilities(PARALLEL_HATCH).setMaxGlobalLimited(1))
                            .or(abilities(EXPORT_ITEMS).setMaxGlobalLimited(1, 1))
                            .or(abilities(IMPORT_ITEMS).setMaxGlobalLimited(1, 1)))
                    .where('B', blocks(RegistriesUtils.getBlock("botania:mana_glass")))
                    .where('C', controller(blocks(definition.get())))
                    .where('D', blocks(GTOBlocks.MANASTEEL_CASING.get()))
                    .where('E', blocks(RegistriesUtils.getBlock("botania:mana_fluxfield")))
                    .where('F', blocks(RegistriesUtils.getBlock("botania:mana_distributor")))
                    .where('G', blocks(RegistriesUtils.getBlock("botania:livingrock")))
                    .where(' ', any())
                    .build())
            .workableCasingRenderer(GTOCore.id("block/casings/manasteel_casing"), GTCEu.id("block/multiblock/gcym/large_chemical_bath"))
            .register();
}
