package com.gtocore.common.data.machines;

import com.gtocore.common.data.translation.GTOMachineTooltips;
import com.gtocore.common.machine.multiblock.electric.ChiselMachine;
import com.gtocore.common.machine.multiblock.part.ae.MESimplePatternBufferPartMachine;
import com.gtocore.integration.Mods;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.client.renderer.machine.OverlayTieredMachineRenderer;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.level.block.Blocks;

import static com.gregtechceu.gtceu.api.GTValues.MV;
import static com.gregtechceu.gtceu.api.machine.multiblock.PartAbility.*;
import static com.gregtechceu.gtceu.api.machine.multiblock.PartAbility.MAINTENANCE;
import static com.gregtechceu.gtceu.api.pattern.Predicates.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.DUMMY_RECIPES;
import static com.gtocore.utils.register.MachineRegisterUtils.machine;
import static com.gtocore.utils.register.MachineRegisterUtils.multiblock;

public final class OptionalMachine {

    public static void init() {}

    public static final MachineDefinition ME_SIMPLE_PATTERN_BUFFER = GTCEu.isDev() || GTOCore.isEasy() ?

            machine("me_simple_pattern_buffer", "ME简单样板总成", MESimplePatternBufferPartMachine::new)
                    .langValue("ME Simple Pattern Buffer")
                    .addTooltipsFromClass(MESimplePatternBufferPartMachine.class)
                    .tier(MV)
                    .allRotation()
                    .abilities(PartAbility.IMPORT_ITEMS, PartAbility.IMPORT_FLUIDS)
                    .tooltipsKey("gtceu.part_sharing.enabled")
                    .renderer(() -> new OverlayTieredMachineRenderer(MV, GTCEu.id("block/machine/part/me_pattern_buffer")))
                    .register() :
            null;

    public static final MultiblockMachineDefinition CARVING_CENTER = GTCEu.isDev() || Mods.chisel() ? multiblock("carving_center", "雕刻中心", ChiselMachine::new)
            .allRotation()
            .tooltips(GTOMachineTooltips.INSTANCE.getCarvingCenterTooltips().getSupplier())
            .recipeTypes(DUMMY_RECIPES)
            .block(GTBlocks.CASING_STEEL_SOLID)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("AAAAA", "ABABA", "ABABA", " CCC ")
                    .aisle("ADDDA", "C   C", "C E C", "CCCCC")
                    .aisle("ADDDA", "C F C", "CEFEC", "CCCCC")
                    .aisle("ADDDA", "C   C", "C   C", "CCCCC")
                    .aisle("AAGAA", "AHHHA", "AHHHA", " CCC ")
                    .where('A', blocks(GTBlocks.CASING_STEEL_SOLID.get())
                            .or(abilities(IMPORT_ITEMS).setMaxGlobalLimited(4))
                            .or(abilities(EXPORT_ITEMS).setMaxGlobalLimited(1))
                            .or(abilities(INPUT_ENERGY).setMaxGlobalLimited(2))
                            .or(abilities(MAINTENANCE).setExactLimit(1)))
                    .where('B', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.Steel)))
                    .where('C', blocks(GTBlocks.CASING_STEEL_SOLID.get()))
                    .where('D', blocks(GTBlocks.STEEL_HULL.get()))
                    .where('E', blocks(GTBlocks.CASING_STEEL_GEARBOX.get()))
                    .where('F', blocks(Blocks.IRON_BARS))
                    .where('G', controller(blocks(definition.get())))
                    .where('H', blocks(GTBlocks.CASING_TEMPERED_GLASS.get()))
                    .where(' ', any())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/solid/machine_casing_solid_steel"), GTCEu.id("block/multiblock/fusion_reactor"))
            .register() : null;
}
