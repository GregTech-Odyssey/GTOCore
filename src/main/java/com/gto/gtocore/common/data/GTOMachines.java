package com.gto.gtocore.common.data;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.machine.SimpleNoEnergyMachine;
import com.gto.gtocore.api.machine.multiblock.GTOPartAbility;
import com.gto.gtocore.api.machine.multiblock.IParallelMachine;
import com.gto.gtocore.api.machine.part.ItemHatchPartMachine;
import com.gto.gtocore.api.pattern.GTOPredicates;
import com.gto.gtocore.client.renderer.machine.BallHatchRenderer;
import com.gto.gtocore.client.renderer.machine.WindMillTurbineRenderer;
import com.gto.gtocore.common.data.machines.AdvancedMultiBlockMachineA;
import com.gto.gtocore.common.data.machines.GTMMachineTooltips;
import com.gto.gtocore.common.data.machines.GeneratorMachine;
import com.gto.gtocore.common.data.machines.MultiBlockMachineA;
import com.gto.gtocore.common.machine.electric.*;
import com.gto.gtocore.common.machine.generator.*;
import com.gto.gtocore.common.machine.magic.*;
import com.gto.gtocore.common.machine.multiblock.generator.CombustionEngineMachine;
import com.gto.gtocore.common.machine.multiblock.generator.GeneratorArrayMachine;
import com.gto.gtocore.common.machine.multiblock.generator.TurbineMachine;
import com.gto.gtocore.common.machine.multiblock.part.*;
import com.gto.gtocore.common.machine.multiblock.part.maintenance.*;
import com.gto.gtocore.common.machine.steam.SteamVacuumPumpMachine;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.*;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.api.machine.multiblock.CoilWorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.machine.steam.SimpleSteamMachine;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import com.gregtechceu.gtceu.api.registry.registrate.MachineBuilder;
import com.gregtechceu.gtceu.api.registry.registrate.MultiblockMachineBuilder;
import com.gregtechceu.gtceu.client.renderer.machine.*;
import com.gregtechceu.gtceu.client.util.TooltipHelper;
import com.gregtechceu.gtceu.common.data.*;
import com.gregtechceu.gtceu.common.item.TurbineRotorBehaviour;
import com.gregtechceu.gtceu.common.machine.multiblock.part.EnergyHatchPartMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.LaserHatchPartMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.RotorHolderPartMachine;
import com.gregtechceu.gtceu.data.lang.LangHandler;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import com.lowdragmc.lowdraglib.side.fluid.FluidHelper;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import com.hepdd.gtmthings.GTMThings;
import com.hepdd.gtmthings.common.block.machine.multiblock.part.WirelessEnergyHatchPartMachine;
import com.hepdd.gtmthings.data.WirelessMachines;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.Int2LongFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.gto.gtocore.api.registries.GTORegistration.REGISTRATE;

@SuppressWarnings("unused")
public class GTOMachines {

    public static final BiConsumer<ItemStack, List<Component>> GTO_MODIFY = (stack, components) -> components
            .add(Component.translatable("gtocore.registry.modify")
                    .withStyle(style -> style.withColor(TooltipHelper.RAINBOW.getCurrent())));

    public static final BiConsumer<IMultiController, List<Component>> CHEMICAL_PLANT_DISPLAY = (controller, components) -> {
        if (controller.isFormed()) {
            double value = 1 - ((CoilWorkableElectricMultiblockMachine) controller).getCoilTier() * 0.05;
            components.add(Component.translatable("gtocore.machine.eut_multiplier.tooltip", FormattingUtil.formatNumbers(value * 0.8)));
            components.add(Component.translatable("gtocore.machine.duration_multiplier.tooltip", FormattingUtil.formatNumbers(value * 0.6)));
        }
    };

    public static final BiConsumer<IMultiController, List<Component>> MAX_TEMPERATURE = (controller, components) -> {
        if (controller instanceof CoilWorkableElectricMultiblockMachine coilMachine && controller.isFormed()) {
            components.add(Component.translatable("gtceu.multiblock.blast_furnace.max_temperature",
                    Component.translatable(FormattingUtil.formatNumbers(coilMachine.getCoilType().getCoilTemperature() + 100L * Math.max(0, coilMachine.getTier() - GTValues.MV)) + "K")
                            .setStyle(Style.EMPTY.withColor(ChatFormatting.RED))));
        }
    };

    public static final BiConsumer<IMultiController, List<Component>> TEMPERATURE = (controller, components) -> {
        if (controller instanceof CoilWorkableElectricMultiblockMachine coilMachine && controller.isFormed()) {
            components.add(Component.translatable("gtceu.multiblock.blast_furnace.max_temperature",
                    Component.translatable(FormattingUtil.formatNumbers(coilMachine.getCoilType().getCoilTemperature()) + "K")
                            .setStyle(Style.EMPTY.withColor(ChatFormatting.RED))));
        }
    };

    public static final BiConsumer<IMultiController, List<Component>> COIL_PARALLEL = (controller, components) -> {
        if (controller instanceof IParallelMachine machine && controller.isFormed()) {
            components.add(Component.translatable("gtceu.multiblock.parallel", Component.literal(FormattingUtil.formatNumbers(machine.getParallel())).withStyle(ChatFormatting.DARK_PURPLE)).withStyle(ChatFormatting.GRAY));
        }
    };

    static {
        REGISTRATE.creativeModeTab(() -> GTOCreativeModeTabs.GTO_CORE);
    }

    public static void init() {
        GTMMachineTooltips.init();
        GeneratorMachine.init();
        MultiBlockMachineA.init();
        AdvancedMultiBlockMachineA.init();
    }

    public static final FactoryBlockPattern DTPF = FactoryBlockPattern.start()
            .aisle(" ddd   ddd             ddd   ddd ", "                                 ", "                                 ", "                                 ", " ddd   ddd             ddd   ddd ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " ddd   ddd             ddd   ddd ", "                                 ", "                                 ", "                                 ", " ddd   ddd   d     d   ddd   ddd ", "         d   d     d   d         ", "         d   d     d   d         ", "                                 ", "                                 ", "                                 ", "         d   d     d   d         ", "         d   d     d   d         ", "         d   d     d   d         ", "                                 ")
            .aisle("dbbbd dbbbd    d d    dbbbd dbbbd", " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ", "dbbbd dbbbd           dbbbd dbbbd", "  d     d               d     d  ", "  d     d               d     d  ", "                                 ", "  d     d               d     d  ", "  d     d               d     d  ", "dbbbd dbbbd           dbbbd dbbbd", " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ", " CCC   CCC   d     d   CCC   CCC ", "dbbbd dbbdCCCb     bCCCdbbd dbbbd", "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ", "         d   d     d   d         ", "                                 ", "         d   d     d   d         ", "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ", "         d   d     d   d         ")
            .aisle("dbbbd dbbbdddddsdsdddddbbbd dbbbd", " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ", "dbbbd dbbbd           dbbbd dbbbd", " ddd   ddd             ddd   ddd ", " ddd   ddd             ddd   ddd ", "  s     s               s     s  ", " ddd   ddd             ddd   ddd ", " ddd   ddd             ddd   ddd ", "dbbbd dbbbd           dbbbd dbbbd", " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ", " CbC   CbC   d     d   CbC   CbC ", "dbbbd dbbdCCCb     bCCCdbbd dbbbd", "  d     sbbbbbddsddbbbbbs     d  ", "  d      bCCCb     bCCCb      d  ", "  d      d   d     d   d      d  ", "   s                         s   ", "   s     d   d     d   d     s   ", "    ss   bCCCb     bCCCb   ss    ", "      dddbbbbbddsddbbbbbddd      ", "         bCCCb     bCCCb         ", "         d   d     d   d         ")
            .aisle("dbbbdddbbbd    ded    dbbbdddbbbd", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ", "dbbbdddbbbd           dbbbdddbbbd", "  d     d               d     d  ", "  d     d               d     d  ", "                                 ", "  d     d               d     d  ", "  d     d               d     d  ", "dbbbdddbbbd           dbbbdddbbbd", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC   d     d   CCCCCCCCC ", "dbbbdddbbdCCCb     bCCCdbbdddbbbd", "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ", "         dCCCd     dCCCd         ", "  s      dCCCd     dCCCd      s  ", "  s      dCCCd     dCCCd      s  ", "         bCCCb     bCCCb         ", "    ss   bCCCb     bCCCb   ss    ", "         bCCCb     bCCCb         ", "         d   d     d   d         ")
            .aisle(" ddd   ddd     ded     ddd   ddd ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " ddd   ddd             ddd   ddd ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " ddd   ddd             ddd   ddd ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " ddd   dd    d     d    dd   ddd ", "         d   d     d   d         ", "         dCCCd     dCCCd         ", "                                 ", "                                 ", "                                 ", "  s      dCCCd     dCCCd      s  ", "   s     d   d     d   d     s   ", "         d   d     d   d         ", "                                 ")
            .aisle("   d   d       ded       d   d   ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   d   d                 d   d   ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "   d   d                 d   d   ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   d   d                 d   d   ", "                                 ", "         dCCCd     dCCCd         ", "                                 ", "                                 ", "                                 ", "  s      dCCCd     dCCCd      s  ", "   s                         s   ", "                                 ", "                                 ")
            .aisle(" ddd   ddd     ded     ddd   ddd ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " ddd   ddd             ddd   ddd ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " ddd   ddd             ddd   ddd ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " ddd   dd    d     d    dd   ddd ", "         d   d     d   d         ", "         dCCCd     dCCCd         ", "                                 ", "                                 ", "                                 ", "         dCCCd     dCCCd         ", "  d      d   d     d   d      d  ", "         d   d     d   d         ", "                                 ")
            .aisle("dbbbdddbbbd    ded    dbbbdddbbbd", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ", "dbbbdddbbbd           dbbbdddbbbd", "  d     d               d     d  ", "  d     d               d     d  ", "                                 ", "  d     d               d     d  ", "  d     d               d     d  ", "dbbbdddbbbd           dbbbdddbbbd", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC   d     d   CCCCCCCCC ", "dbbbdddbbdCCCb     bCCCdbbdddbbbd", "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ", "         dCCCd     dCCCd         ", "         dCCCd     dCCCd         ", "         dCCCd     dCCCd         ", "         bCCCb     bCCCb         ", "  d      bCCCb     bCCCb      d  ", "         bCCCb     bCCCb         ", "         d   d     d   d         ")
            .aisle("dbbbd dbbbdddddsdsdddddbbbd dbbbd", " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ", "dbbbd dbbbd           dbbbd dbbbd", " ddd   ddd             ddd   ddd ", " ddd   ddd             ddd   ddd ", "  s     s               s     s  ", " ddd   ddd             ddd   ddd ", " ddd   ddd             ddd   ddd ", "dbbbd dbbbd           dbbbd dbbbd", " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ", " CbC   CbC   d     d   CbC   CbC ", "dbbbd dbbdCCCb     bCCCdbbd dbbbd", "  s     sbbbbbddsddbbbbbs     s  ", "         bCCCb     bCCCb         ", "         d   d     d   d         ", "                                 ", "         d   d     d   d         ", "         bCCCb     bCCCb         ", "  d     sbbbbbddsddbbbbbs     d  ", "         bCCCb     bCCCb         ", "         d   d     d   d         ")
            .aisle("dbbbd dbbbd    ded    dbbbd dbbbd", " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ", "dbbbd dbbbd           dbbbd dbbbd", "  d     d               d     d  ", "  d     d               d     d  ", "                                 ", "  d     d               d     d  ", "  d     d               d     d  ", "dbbbd dbbbd           dbbbd dbbbd", " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ", " CCC   CCC   d     d   CCC   CCC ", "dddd   dddCCCb     bCCCddd   dddd", "dbbbd dbbdCCCb     bCCCdbbd dbbbd", "dbbbdddbbdCCCb     bCCCdbbdddbbbd", " ddd   ddd   d     d   ddd   ddd ", "   d   d                 d   d   ", " ddd   ddd   d     d   ddd   ddd ", "dbbbdddbbdCCCb     bCCCdbbdddbbbd", "dbbbd dbbdCCCb     bCCCdbbd dbbbd", "dbbbd dbbdCCCb     bCCCdbbd dbbbd", " ddd   ddd   d     d   ddd   ddd ")
            .aisle(" ddd   ddd     ded     ddd   ddd ", "                                 ", "                                 ", "                                 ", " ddd   ddd             ddd   ddd ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " ddd   ddd             ddd   ddd ", "                                 ", "                                 ", "                                 ", " CCC   CCC   d     d   CCC   CCC ", " CbC   CbC   d     d   CbC   CbC ", " CCCCCCCCC   d     d   CCCCCCCCC ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " CCCCCCCCC   d     d   CCCCCCCCC ", " CbC   CbC   d     d   CbC   CbC ", " CCC   CCC   d     d   CCC   CCC ", "                                 ")
            .aisle("  d     d      ded      d     d  ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " CCC   CCC             CCC   CCC ", " CbC   CbC             CbC   CbC ", " CCCCCCCCC             CCCCCCCCC ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " CCCCCCCCC             CCCCCCCCC ", " CbC   CbC             CbC   CbC ", " CCC   CCC             CCC   CCC ", "                                 ")
            .aisle("  d     d      ded      d     d  ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " CCC   CCC             CCC   CCC ", " CbC   CbC             CbC   CbC ", " CCCCCCCCC             CCCCCCCCC ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " CCCCCCCCC             CCCCCCCCC ", " CbC   CbC             CbC   CbC ", " CCC   CCC             CCC   CCC ", "                                 ")
            .aisle("  d     d     dsdsd     d     d  ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " ddd   ddd             ddd   ddd ", "dbbbd dbbbd           dbbbd dbbbd", "dbbbd dbbbd           dbbbd dbbbd", "dbbbdddbbbd           dbbbdddbbbd", " ddd   ddd             ddd   ddd ", "   d   d                 d   d   ", " ddd   ddd             ddd   ddd ", "dbbbdddbbbd           dbbbdddbbbd", "dbbbd dbbbd           dbbbd dbbbd", "dbbbd dbbbd           dbbbd dbbbd", " ddd   ddd             ddd   ddd ")
            .aisle("  d     d    deeeeed    d     d  ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "  d     d               d     d  ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "  d     d               d     d  ", "                                 ", "                                 ")
            .aisle(" dsdddddsddddseedeesddddsdddddsd ", "                d                ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "  d     d               d     d  ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "  d     d               d     d  ", "                                 ", "                                 ")
            .aisle("  deeeeedeeeededddedeeeedeeeeed  ", "               ddd               ", "                a                ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "  s     s               s     s  ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "  s     s               s     s  ", "                                 ", "                                 ")
            .aisle(" dsdddddsddddseedeesddddsdddddsd ", "                d                ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "  d     d               d     d  ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "  d     d               d     d  ", "                                 ", "                                 ")
            .aisle("  d     d    deeeeed    d     d  ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "  d     d               d     d  ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "  d     d               d     d  ", "                                 ", "                                 ")
            .aisle("  d     d     dsdsd     d     d  ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " ddd   ddd             ddd   ddd ", "dbbbd dbbbd           dbbbd dbbbd", "dbbbd dbbbd           dbbbd dbbbd", "dbbbdddbbbd           dbbbdddbbbd", " ddd   ddd             ddd   ddd ", "   d   d                 d   d   ", " ddd   ddd             ddd   ddd ", "dbbbdddbbbd           dbbbdddbbbd", "dbbbd dbbbd           dbbbd dbbbd", "dbbbd dbbbd           dbbbd dbbbd", " ddd   ddd             ddd   ddd ")
            .aisle("  d     d      ded      d     d  ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " CCC   CCC             CCC   CCC ", " CbC   CbC             CbC   CbC ", " CCCCCCCCC             CCCCCCCCC ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " CCCCCCCCC             CCCCCCCCC ", " CbC   CbC             CbC   CbC ", " CCC   CCC             CCC   CCC ", "                                 ")
            .aisle("  d     d      ded      d     d  ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " CCC   CCC             CCC   CCC ", " CbC   CbC             CbC   CbC ", " CCCCCCCCC             CCCCCCCCC ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " CCCCCCCCC             CCCCCCCCC ", " CbC   CbC             CbC   CbC ", " CCC   CCC             CCC   CCC ", "                                 ")
            .aisle(" ddd   ddd     ded     ddd   ddd ", "                                 ", "                                 ", "                                 ", " ddd   ddd             ddd   ddd ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " ddd   ddd             ddd   ddd ", "                                 ", "                                 ", "                                 ", " CCC   CCC   d     d   CCC   CCC ", " CbC   CbC   d     d   CbC   CbC ", " CCCCCCCCC   d     d   CCCCCCCCC ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " CCCCCCCCC   d     d   CCCCCCCCC ", " CbC   CbC   d     d   CbC   CbC ", " CCC   CCC   d     d   CCC   CCC ", "                                 ")
            .aisle("dbbbd dbbbd    ded    dbbbd dbbbd", " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ", "dbbbd dbbbd           dbbbd dbbbd", "  d     d               d     d  ", "  d     d               d     d  ", "                                 ", "  d     d               d     d  ", "  d     d               d     d  ", "dbbbd dbbbd           dbbbd dbbbd", " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ", " CCC   CCC   d     d   CCC   CCC ", "dddd   dddCCCb     bCCCddd   dddd", "dbbbd dbbdCCCb     bCCCdbbd dbbbd", "dbbbdddbbdCCCb     bCCCdbbdddbbbd", " ddd   ddd   d     d   ddd   ddd ", "   d   d                 d   d   ", " ddd   ddd   d     d   ddd   ddd ", "dbbbdddbbdCCCb     bCCCdbbdddbbbd", "dbbbd dbbdCCCb     bCCCdbbd dbbbd", "dbbbd dbbdCCCb     bCCCdbbd dbbbd", " ddd   ddd   d     d   ddd   ddd ")
            .aisle("dbbbd dbbbdddddsdsdddddbbbd dbbbd", " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ", "dbbbd dbbbd           dbbbd dbbbd", " ddd   ddd             ddd   ddd ", " ddd   ddd             ddd   ddd ", "  s     s               s     s  ", " ddd   ddd             ddd   ddd ", " ddd   ddd             ddd   ddd ", "dbbbd dbbbd           dbbbd dbbbd", " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ", " CbC   CbC   d     d   CbC   CbC ", "dbbbd dbbdCCCb     bCCCdbbd dbbbd", "  s     sbbbbbddsddbbbbbs     s  ", "         bCCCb     bCCCb         ", "         d   d     d   d         ", "                                 ", "         d   d     d   d         ", "         bCCCb     bCCCb         ", "  d     sbbbbbddsddbbbbbs     d  ", "         bCCCb     bCCCb         ", "         d   d     d   d         ")
            .aisle("dbbbdddbbbd    ded    dbbbdddbbbd", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ", "dbbbdddbbbd           dbbbdddbbbd", "  d     d               d     d  ", "  d     d               d     d  ", "                                 ", "  d     d               d     d  ", "  d     d               d     d  ", "dbbbdddbbbd           dbbbdddbbbd", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC   d     d   CCCCCCCCC ", "dbbbdddbbdCCCb     bCCCdbbdddbbbd", "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ", "         dCCCd     dCCCd         ", "         dCCCd     dCCCd         ", "         dCCCd     dCCCd         ", "         bCCCb     bCCCb         ", "  d      bCCCb     bCCCb      d  ", "         bCCCb     bCCCb         ", "         d   d     d   d         ")
            .aisle(" ddd   ddd     ded     ddd   ddd ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " ddd   ddd             ddd   ddd ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " ddd   ddd             ddd   ddd ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " ddd   dd    d     d    dd   ddd ", "         d   d     d   d         ", "         dCCCd     dCCCd         ", "                                 ", "                                 ", "                                 ", "         dCCCd     dCCCd         ", "  d      d   d     d   d      d  ", "         d   d     d   d         ", "                                 ")
            .aisle("   d   d       ded       d   d   ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   d   d                 d   d   ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", "   d   d                 d   d   ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   d   d                 d   d   ", "                                 ", "         dCCCd     dCCCd         ", "                                 ", "                                 ", "                                 ", "  s      dCCCd     dCCCd      s  ", "   s                         s   ", "                                 ", "                                 ")
            .aisle(" ddd   ddd     ded     ddd   ddd ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " ddd   ddd             ddd   ddd ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " ddd   ddd             ddd   ddd ", "   C   C                 C   C   ", "   C   C                 C   C   ", "   C   C                 C   C   ", " ddd   dd    d     d    dd   ddd ", "         d   d     d   d         ", "         dCCCd     dCCCd         ", "                                 ", "                                 ", "                                 ", "  s      dCCCd     dCCCd      s  ", "   s     d   d     d   d     s   ", "         d   d     d   d         ", "                                 ")
            .aisle("dbbbdddbbbd    ded    dbbbdddbbbd", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ", "dbbbdddbbbd           dbbbdddbbbd", "  d     d               d     d  ", "  d     d               d     d  ", "                                 ", "  d     d               d     d  ", "  d     d               d     d  ", "dbbbdddbbbd           dbbbdddbbbd", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC             CCCCCCCCC ", " CCCCCCCCC   d     d   CCCCCCCCC ", "dbbbdddbbdCCCb     bCCCdbbdddbbbd", "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ", "         dCCCd     dCCCd         ", "  s      dCCCd     dCCCd      s  ", "  s      dCCCd     dCCCd      s  ", "         bCCCb     bCCCb         ", "    ss   bCCCb     bCCCb   ss    ", "         bCCCb     bCCCb         ", "         d   d     d   d         ")
            .aisle("dbbbd dbbbdddddsdsdddddbbbd dbbbd", " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ", "dbbbd dbbbd           dbbbd dbbbd", " ddd   ddd             ddd   ddd ", " ddd   ddd             ddd   ddd ", "  s     s               s     s  ", " ddd   ddd             ddd   ddd ", " ddd   ddd             ddd   ddd ", "dbbbd dbbbd           dbbbd dbbbd", " CbC   CbC             CbC   CbC ", " CbC   CbC             CbC   CbC ", " CbC   CbC   d     d   CbC   CbC ", "dbbbd dbbdCCCb     bCCCdbbd dbbbd", "  d     sbbbbbddsddbbbbbs     d  ", "  d      bCCCb     bCCCb      d  ", "  d      d   d     d   d      d  ", "   s                         s   ", "   s     d   d     d   d     s   ", "    ss   bCCCb     bCCCb   ss    ", "      dddbbbbbddsddbbbbbddd      ", "         bCCCb     bCCCb         ", "         d   d     d   d         ")
            .aisle("dbbbd dbbbd    d d    dbbbd dbbbd", " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ", "dbbbd dbbbd           dbbbd dbbbd", "  d     d               d     d  ", "  d     d               d     d  ", "                                 ", "  d     d               d     d  ", "  d     d               d     d  ", "dbbbd dbbbd           dbbbd dbbbd", " CCC   CCC             CCC   CCC ", " CCC   CCC             CCC   CCC ", " CCC   CCC   d     d   CCC   CCC ", "dbbbd dbbdCCCb     bCCCdbbd dbbbd", "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ", "         d   d     d   d         ", "                                 ", "         d   d     d   d         ", "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ", "         bCCCb     bCCCb         ", "         d   d     d   d         ")
            .aisle(" ddd   ddd             ddd   ddd ", "                                 ", "                                 ", "                                 ", " ddd   ddd             ddd   ddd ", "                                 ", "                                 ", "                                 ", "                                 ", "                                 ", " ddd   ddd             ddd   ddd ", "                                 ", "                                 ", "                                 ", " ddd   ddd   d     d   ddd   ddd ", "         d   d     d   d         ", "         d   d     d   d         ", "                                 ", "                                 ", "                                 ", "         d   d     d   d         ", "         d   d     d   d         ", "         d   d     d   d         ", "                                 ");

    //////////////////////////////////////
    // *** Simple Machine ***//
    //////////////////////////////////////
    public static final Pair<MachineDefinition, MachineDefinition> STEAM_VACUUM_PUMP = registerSteamMachines("steam_vacuum_pump", SteamVacuumPumpMachine::new, (pressure, builder) -> builder
            .rotationState(RotationState.ALL)
            .recipeType(GTORecipeTypes.VACUUM_PUMP_RECIPES)
            .recipeModifier(SimpleSteamMachine::recipeModifier)
            .tooltips(Component.translatable("gtocore.recipe.vacuum.tier", pressure ? 2 : 1))
            .renderer(() -> new WorkableSteamMachineRenderer(pressure, GTOCore.id("block/machines/vacuum_pump")))
            .register());

    public static final MachineDefinition[] SEMI_FLUID_GENERATOR = registerSimpleGenerator("semi_fluid",
            GTORecipeTypes.SEMI_FLUID_GENERATOR_FUELS, GTMachines.genericGeneratorTankSizeFunction, GTValues.LV, GTValues.MV,
            GTValues.HV);

    public static final MachineDefinition[] THERMAL_GENERATOR = registerSimpleGenerator("thermal_generator",
            GTORecipeTypes.THERMAL_GENERATOR_FUELS, tier -> 8000, GTValues.ULV);

    public static final MachineDefinition[] ROCKET_ENGINE_GENERATOR = registerSimpleGenerator("rocket_engine", GTRecipeTypes.get("rocket_engine"),
            GTMachines.genericGeneratorTankSizeFunction, GTValues.EV, GTValues.IV, GTValues.LuV);

    public static final MachineDefinition[] NAQUADAH_REACTOR_GENERATOR = registerSimpleGenerator("naquadah_reactor", GTRecipeTypes.get("naquadah_reactor"),
            GTMachines.genericGeneratorTankSizeFunction, GTValues.IV, GTValues.LuV, GTValues.ZPM);

    public static final MachineDefinition[] LIGHTNING_PROCESSOR = registerSimpleMachines("lightning_processor",
            GTORecipeTypes.LIGHTNING_PROCESSOR_RECIPES, GTMachines.defaultTankSizeFunction);

    public static final MachineDefinition[] DEHYDRATOR = registerSimpleMachines("dehydrator",
            GTORecipeTypes.DEHYDRATOR_RECIPES, GTMachines.defaultTankSizeFunction);

    public static final MachineDefinition[] UNPACKER = registerSimpleMachines("unpacker", GTORecipeTypes.UNPACKER_RECIPES, GTMachines.defaultTankSizeFunction);

    public static final MachineDefinition[] CLUSTER = registerSimpleMachines("cluster", GTORecipeTypes.CLUSTER_RECIPES, GTMachines.defaultTankSizeFunction);

    public static final MachineDefinition[] ROLLING = registerSimpleMachines("rolling", GTORecipeTypes.ROLLING_RECIPES, GTMachines.defaultTankSizeFunction);

    public static final MachineDefinition[] LAMINATOR = registerSimpleMachines("laminator", GTORecipeTypes.LAMINATOR_RECIPES, GTMachines.defaultTankSizeFunction);

    public static final MachineDefinition[] LOOM = registerSimpleMachines("loom", GTORecipeTypes.LOOM_RECIPES, GTMachines.defaultTankSizeFunction);

    public static final MachineDefinition[] LASER_WELDER = registerSimpleMachines("laser_welder", GTORecipeTypes.LASER_WELDER_RECIPES, GTMachines.defaultTankSizeFunction);

    public static final MachineDefinition[] WORLD_DATA_SCANNER = registerSimpleMachines("world_data_scanner",
            GTORecipeTypes.WORLD_DATA_SCANNER_RECIPES, tier -> 64000);

    public static final MachineDefinition[] NEUTRON_COMPRESSOR = registerSimpleMachines("neutron_compressor",
            GTORecipeTypes.NEUTRON_COMPRESSOR_RECIPES, GTMachines.defaultTankSizeFunction, GTValues.MAX);

    public static final MachineDefinition[] ULV_PACKER = registerSimpleMachines("packer", GTRecipeTypes.PACKER_RECIPES, GTMachines.defaultTankSizeFunction, GTOCore.id("block/machines/packer"), GTValues.ULV);
    public static final MachineDefinition[] ULV_UNPACKER = registerSimpleMachines("unpacker", GTORecipeTypes.UNPACKER_RECIPES, GTMachines.defaultTankSizeFunction, GTValues.ULV);

    public static final MachineDefinition[] VACUUM_PUMP = registerTieredMachines("vacuum_pump", VacuumPumpMachine::new,
            (tier, builder) -> builder
                    .langValue("%s Vacuum Pump %s".formatted(GTValues.VLVH[tier], GTValues.VLVT[tier]))
                    .rotationState(RotationState.NON_Y_AXIS)
                    .editableUI(SimpleTieredMachine.EDITABLE_UI_CREATOR.apply(GTCEu.id("vacuum_pump"), GTORecipeTypes.VACUUM_PUMP_RECIPES))
                    .recipeModifier(VacuumPumpMachine::recipeModifier)
                    .recipeType(GTORecipeTypes.VACUUM_PUMP_RECIPES)
                    .workableTieredHullRenderer(GTOCore.id("block/machines/vacuum_pump"))
                    .tooltips(Component.translatable("gtocore.recipe.vacuum.tier", tier + 1))
                    .tooltips(GTMachines.workableTiered(tier, GTValues.V[tier], GTValues.V[tier] * 64, GTORecipeTypes.VACUUM_PUMP_RECIPES, GTMachines.defaultTankSizeFunction.apply(tier), true))
                    .register(),
            GTValues.LV, GTValues.MV, GTValues.HV);

    public static final MachineDefinition[] LIGHTNING_ROD = registerTieredMachines(
            "lightning_rod",
            LightningRodMachine::new,
            (tier, builder) -> builder
                    .langValue("%s Lightning Rod %s".formatted(GTValues.VLVH[tier], GTValues.VLVT[tier]))
                    .rotationState(RotationState.NON_Y_AXIS)
                    .renderer(() -> new SimpleGeneratorMachineRenderer(tier, GTOCore.id("block/generators/lightning_rod")))
                    .tooltips(Component.translatable("gtocore.machine.lightning_rod.tooltip.0"))
                    .tooltips(Component.translatable("gtocore.machine.lightning_rod.tooltip.1"))
                    .tooltips(Component.translatable("gtocore.machine.lightning_rod.tooltip.2"))
                    .tooltips(Component.translatable("gtocore.universal.tooltip.ampere_out", 512))
                    .tooltips(Component.translatable("gtceu.universal.tooltip.voltage_out",
                            FormattingUtil.formatNumbers(GTValues.V[tier - 1]), GTValues.VNF[tier - 1]))
                    .tooltips(Component.translatable("gtceu.universal.tooltip.energy_storage_capacity",
                            FormattingUtil.formatNumbers((long) (48828 * Math.pow(4, tier)))))
                    .register(),
            GTValues.EV, GTValues.IV, GTValues.LuV);

    public static final MachineDefinition[] PRIMITIVE_MAGIC_ENERGY = registerTieredMachines(
            "primitive_magic_energy",
            MagicEnergyMachine::new,
            (tier, builder) -> builder
                    .langValue("%s Primitive Magic Energy %s".formatted(GTValues.VLVH[tier], GTValues.VLVT[tier]))
                    .rotationState(RotationState.NON_Y_AXIS)
                    .renderer(() -> new SimpleGeneratorMachineRenderer(tier,
                            GTOCore.id("block/generators/primitive_magic_energy")))
                    .tooltips(Component.translatable("gtocore.machine.primitive_magic_energy.tooltip.0"))
                    .tooltips(Component.translatable("gtocore.universal.tooltip.ampere_out", 16))
                    .tooltips(Component.translatable("gtceu.universal.tooltip.voltage_out",
                            FormattingUtil.formatNumbers(GTValues.V[tier]), GTValues.VNF[tier]))
                    .tooltips(Component.translatable("gtceu.universal.tooltip.energy_storage_capacity",
                            FormattingUtil.formatNumbers(GTValues.V[tier] * 512L)))
                    .register(),
            GTValues.ULV, GTValues.LV);

    public static final MachineDefinition[] WIND_MILL_TURBINE = registerTieredMachines(
            "wind_mill_turbine",
            WindMillTurbineMachine::new,
            (tier, builder) -> builder
                    .langValue("%s Wind Mill Turbine %s".formatted(GTValues.VLVH[tier], GTValues.VLVT[tier]))
                    .rotationState(RotationState.NON_Y_AXIS)
                    .renderer(() -> new WindMillTurbineRenderer(tier))
                    .hasTESR(true)
                    .tooltips(Component.translatable("gtocore.machine.wind_mill_turbine.tooltip.0"))
                    .tooltips(Component.translatable("gtocore.machine.wind_mill_turbine.tooltip.1"))
                    .tooltips(Component.translatable("gtocore.universal.tooltip.ampere_out", 2))
                    .tooltips(Component.translatable("gtceu.universal.tooltip.voltage_out",
                            FormattingUtil.formatNumbers(GTValues.V[tier]), GTValues.VNF[tier]))
                    .tooltips(Component.translatable("gtceu.universal.tooltip.energy_storage_capacity",
                            FormattingUtil.formatNumbers(GTValues.V[tier] * 64)))
                    .register(),
            GTValues.ULV, GTValues.LV, GTValues.MV, GTValues.HV);

    private static MachineDefinition[] registerHugeFluidHatches(String name, String displayname, String model,
                                                                String tooltip, IO io, PartAbility... abilities) {
        return registerTieredMachines(name,
                (holder, tier) -> new HugeFluidHatchPartMachine(holder, tier, io),
                (tier, builder) -> {
                    builder.langValue(GTValues.VNF[tier] + ' ' + displayname)
                            .rotationState(RotationState.ALL)
                            .renderer(() -> new OverlayTieredMachineRenderer(tier, GTCEu.id("block/machine/part/" + model)))
                            .abilities(abilities)
                            .tooltips(Component.translatable("gtceu.machine." + tooltip + ".tooltip"));
                    builder.tooltips(Component.translatable("gtceu.universal.tooltip.fluid_storage_capacity_mult",
                            tier, FormattingUtil.formatNumbers(Integer.MAX_VALUE)));
                    return builder.register();
                },
                GTValues.tiersBetween(GTValues.LV, GTValues.OpV));
    }

    private static MachineDefinition[] registerLaserHatch(IO io, int amperage, PartAbility ability) {
        String name = io == IO.IN ? "target" : "source";
        return registerTieredMachines(amperage + "a_laser_" + name + "_hatch",
                (holder, tier) -> new LaserHatchPartMachine(holder, io, tier, amperage), (tier, builder) -> builder
                        .langValue(GTValues.VNF[tier] + " " + FormattingUtil.formatNumbers(amperage) + "A Laser " +
                                FormattingUtil.toEnglishName(name) + " Hatch")
                        .rotationState(RotationState.ALL)
                        .tooltips(Component.translatable("gtceu.machine.laser_hatch." + name + ".tooltip"),
                                Component.translatable("gtceu.machine.laser_hatch.both.tooltip"),
                                Component.translatable("gtceu.universal.disabled"))
                        .abilities(ability)
                        .renderer(() -> new OverlayTieredMachineRenderer(tier, GTCEu.id("block/machine/part/laser_hatch." + name)))
                        .register(),
                GTValues.tiersBetween(GTValues.IV, GTValues.MAX));
    }

    private static MachineDefinition[] registerSimpleGenerator(String name, GTRecipeType recipeType, Int2LongFunction tankScalingFunction, int... tiers) {
        return registerTieredMachines(name,
                (holder, tier) -> new SimpleGeneratorMachine(holder, tier, 0.1F * tier, tankScalingFunction),
                (tier, builder) -> builder
                        .langValue("%s %s Generator %s".formatted(GTValues.VLVH[tier], FormattingUtil.toEnglishName(name), GTValues.VLVT[tier]))
                        .editableUI(SimpleGeneratorMachine.EDITABLE_UI_CREATOR.apply(GTCEu.id(name), recipeType))
                        .rotationState(RotationState.ALL)
                        .recipeType(recipeType)
                        .recipeModifier(SimpleGeneratorMachine::recipeModifier, true)
                        .addOutputLimit(ItemRecipeCapability.CAP, 0)
                        .addOutputLimit(FluidRecipeCapability.CAP, 0)
                        .renderer(() -> new SimpleGeneratorMachineRenderer(tier, GTOCore.id("block/generators/" + name)))
                        .tooltips(Component.translatable("gtocore.machine.efficiency.tooltip", GeneratorArrayMachine.getEfficiency(recipeType, tier)).append("%"))
                        .tooltips(Component.translatable("gtocore.universal.tooltip.ampere_out", GeneratorArrayMachine.getAmperage(tier)))
                        .tooltips(GTMachines.workableTiered(tier, GTValues.V[tier], GTValues.V[tier] * 64 * GeneratorArrayMachine.getAmperage(tier), recipeType, tankScalingFunction.apply(tier), false))
                        .register(),
                tiers);
    }

    private static MachineDefinition[] registerSimpleMachines(String name, GTRecipeType recipeType,
                                                              Int2LongFunction tankScalingFunction) {
        return registerSimpleMachines(name, recipeType, tankScalingFunction, GTMachines.ELECTRIC_TIERS);
    }

    private static MachineDefinition[] registerSimpleMachines(String name,
                                                              GTRecipeType recipeType,
                                                              Int2LongFunction tankScalingFunction,
                                                              int... tiers) {
        return registerSimpleMachines(name, recipeType, tankScalingFunction, GTOCore.id("block/machines/" + name), tiers);
    }

    private static MachineDefinition[] registerSimpleMachines(String name,
                                                              GTRecipeType recipeType,
                                                              Int2LongFunction tankScalingFunction,
                                                              ResourceLocation workableModel, int... tiers) {
        return registerTieredMachines(name,
                (holder, tier) -> new SimpleTieredMachine(holder, tier, tankScalingFunction), (tier, builder) -> {
                    builder.recipeModifier(GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK));
                    return builder
                            .langValue("%s %s %s".formatted(GTValues.VLVH[tier], FormattingUtil.toEnglishName(name), GTValues.VLVT[tier]))
                            .editableUI(SimpleTieredMachine.EDITABLE_UI_CREATOR.apply(GTCEu.id(name), recipeType))
                            .rotationState(RotationState.NON_Y_AXIS)
                            .recipeType(recipeType)
                            .workableTieredHullRenderer(workableModel)
                            .tooltips(GTMachines.workableTiered(tier, GTValues.V[tier], GTValues.V[tier] * 64, recipeType,
                                    tankScalingFunction.apply(tier), true))
                            .register();
                },
                tiers);
    }

    private static MachineDefinition[] registerSimpleNoEnergyMachines(String name,
                                                                      GTRecipeType recipeType,
                                                                      Int2LongFunction tankScalingFunction,
                                                                      int... tiers) {
        return registerSimpleNoEnergyMachines(name, recipeType, tankScalingFunction, GTOCore.id("block/machines/" + name), tiers);
    }

    private static MachineDefinition[] registerSimpleNoEnergyMachines(String name,
                                                                      GTRecipeType recipeType,
                                                                      Int2LongFunction tankScalingFunction,
                                                                      ResourceLocation workableModel, int... tiers) {
        return registerTieredMachines(name,
                (holder, tier) -> new SimpleNoEnergyMachine(holder, tier, tankScalingFunction), (tier, builder) -> {
                    builder.recipeModifier((machine, recipe, params, result) -> recipe);
                    return builder
                            .langValue("%s %s %s".formatted(GTValues.VLVH[tier], FormattingUtil.toEnglishName(name), GTValues.VLVT[tier]))
                            .editableUI(SimpleNoEnergyMachine.EDITABLE_UI_CREATOR.apply(GTCEu.id(name), recipeType))
                            .rotationState(RotationState.NON_Y_AXIS)
                            .recipeType(recipeType)
                            .workableTieredHullRenderer(workableModel)
                            .tooltips(workable(recipeType, tankScalingFunction.apply(tier)))
                            .register();
                },
                tiers);
    }

    private static Component[] workable(GTRecipeType recipeType, long tankCapacity) {
        List<Component> tooltipComponents = new ArrayList<>();
        if (recipeType.getMaxInputs(FluidRecipeCapability.CAP) > 0 ||
                recipeType.getMaxOutputs(FluidRecipeCapability.CAP) > 0)
            tooltipComponents
                    .add(Component.translatable("gtceu.universal.tooltip.fluid_storage_capacity",
                            FormattingUtil.formatNumbers(tankCapacity)));
        return tooltipComponents.toArray(Component[]::new);
    }

    private static MachineDefinition[] registerTieredMachines(String name,
                                                              BiFunction<IMachineBlockEntity, Integer, MetaMachine> factory,
                                                              BiFunction<Integer, MachineBuilder<MachineDefinition>, MachineDefinition> builder,
                                                              int... tiers) {
        return registerMachineDefinitions(name, factory, builder, REGISTRATE, tiers);
    }

    public static MachineDefinition[] registerMachineDefinitions(String name, BiFunction<IMachineBlockEntity, Integer, MetaMachine> factory, BiFunction<Integer, MachineBuilder<MachineDefinition>, MachineDefinition> builder, GTRegistrate registrate, int[] tiers) {
        MachineDefinition[] definitions = new MachineDefinition[GTValues.TIER_COUNT];
        for (int tier : tiers) {
            MachineBuilder<MachineDefinition> register = registrate.machine(GTValues.VN[tier].toLowerCase(Locale.ROOT) + "_" + name,
                    holder -> factory.apply(holder, tier))
                    .tier(tier);
            definitions[tier] = builder.apply(tier, register);
        }
        return definitions;
    }

    private static MachineDefinition[] registerWirelessEnergyHatch(IO io, PartAbility ability, int[] tiers) {
        var name = io == IO.IN ? "input" : "output";
        return registerTieredMachines(64 + "a_wireless_energy_" + name + "_hatch",
                (holder, tier) -> new WirelessEnergyHatchPartMachine(holder, tier, io, 64),
                (tier, builder) -> builder
                        .langValue(GTValues.VNF[tier] + " " + 64 + "A Wireless" + (io == IO.IN ? " Energy Hatch" : " Dynamo Hatch"))
                        .rotationState(RotationState.ALL)
                        .abilities(ability)
                        .tooltips(Component.translatable("gtmthings.machine.energy_hatch." + name + ".tooltip"), (Component.translatable("gtmthings.machine.wireless_energy_hatch." + name + ".tooltip")))
                        .renderer(() -> new OverlayTieredMachineRenderer(tier, GTMThings.id("block/machine/part/wireless_energy_hatch_64a")))
                        .compassNode("energy_hatch")
                        .register(),
                tiers);
    }

    private static MachineDefinition[] registerWirelessLaserHatch(IO io, int amperage, PartAbility ability) {
        var name = io == IO.IN ? "target" : "source";
        return registerTieredMachines(amperage + "a_wireless_laser_" + name + "_hatch",
                (holder, tier) -> new WirelessEnergyHatchPartMachine(holder, tier, io, amperage),
                (tier, builder) -> builder
                        .langValue(GTValues.VNF[tier] + " " + FormattingUtil.formatNumbers(amperage) + "A Laser " + FormattingUtil.toEnglishName(name) + " Hatch")
                        .rotationState(RotationState.ALL)
                        .abilities(ability)
                        .tooltips(Component.translatable("gtmthings.machine.energy_hatch." + name + ".tooltip"), (Component.translatable("gtmthings.machine.wireless_energy_hatch." + name + ".tooltip")))
                        .renderer(() -> new OverlayTieredMachineRenderer(tier, GTMThings.id("block/machine/part/wireless_laser_hatch.target")))
                        .compassNode("laser_hatch." + name)
                        .register(),
                WirelessMachines.WIRELL_ENERGY_HIGH_TIERS);
    }

    private static Pair<MachineDefinition, MachineDefinition> registerSteamMachines(String name,
                                                                                    BiFunction<IMachineBlockEntity, Boolean, MetaMachine> factory,
                                                                                    BiFunction<Boolean, MachineBuilder<MachineDefinition>, MachineDefinition> builder) {
        MachineDefinition lowTier = builder.apply(false,
                REGISTRATE.machine("lp_%s".formatted(name), holder -> factory.apply(holder, false))
                        .langValue("Low Pressure " + FormattingUtil.toEnglishName(name))
                        .compassSections(GTCompassSections.STEAM)
                        .compassNode(name)
                        .compassPreNodes(GTCompassNodes.STEAM)
                        .tier(0));
        MachineDefinition highTier = builder.apply(true,
                REGISTRATE.machine("hp_%s".formatted(name), holder -> factory.apply(holder, true))
                        .langValue("High Pressure " + FormattingUtil.toEnglishName(name))
                        .compassSections(GTCompassSections.STEAM)
                        .compassNode(name)
                        .compassPreNodes(GTCompassNodes.STEAM)
                        .tier(1));
        return Pair.of(lowTier, highTier);
    }

    public static MultiblockMachineDefinition[] registerTieredMultis(String name,
                                                                     BiFunction<IMachineBlockEntity, Integer, MultiblockControllerMachine> factory,
                                                                     BiFunction<Integer, MultiblockMachineBuilder, MultiblockMachineDefinition> builder,
                                                                     int... tiers) {
        MultiblockMachineDefinition[] definitions = new MultiblockMachineDefinition[GTValues.TIER_COUNT];
        for (int tier : tiers) {
            MultiblockMachineBuilder register = REGISTRATE.multiblock(GTValues.VN[tier].toLowerCase(Locale.ROOT) + "_" + name, holder -> factory.apply(holder, tier)).tier(tier);
            definitions[tier] = builder.apply(tier, register);
        }
        return definitions;
    }

    public static MultiblockMachineDefinition registerLargeCombustionEngine(GTRegistrate registrate, String name, int tier,
                                                                            GTRecipeType recipeType,
                                                                            Supplier<? extends Block> casing,
                                                                            Supplier<? extends Block> gear,
                                                                            Supplier<? extends Block> intake,
                                                                            ResourceLocation casingTexture,
                                                                            ResourceLocation overlayModel, boolean isGTM) {
        MultiblockMachineBuilder builder = registrate.multiblock(name, holder -> new CombustionEngineMachine(holder, tier))
                .rotationState(RotationState.ALL)
                .recipeType(recipeType)
                .generator(true)
                .recipeModifier(CombustionEngineMachine::recipeModifier, true)
                .appearanceBlock(casing)
                .pattern(definition -> FactoryBlockPattern.start()
                        .aisle("XXX", "XDX", "XXX")
                        .aisle("XCX", "CGC", "XCX")
                        .aisle("XCX", "CGC", "XCX")
                        .aisle("AAA", "AYA", "AAA")
                        .where('X', Predicates.blocks(casing.get()))
                        .where('G', Predicates.blocks(gear.get()))
                        .where('C', Predicates.blocks(casing.get()).setMinGlobalLimited(3).or(Predicates.autoAbilities(definition.getRecipeTypes(), false, false, true, true, true, true)).or(Predicates.autoAbilities(true, true, false)))
                        .where('D', Predicates.ability(PartAbility.OUTPUT_ENERGY, Stream.of(GTValues.EV, GTValues.IV, GTValues.LuV, GTValues.ZPM, GTValues.UV, GTValues.UHV).filter(t -> t >= tier).mapToInt(Integer::intValue).toArray()).addTooltips(Component.translatable("gtceu.machine.large_combustion_engine.tooltip.boost_regular", GTValues.VN[tier])))
                        .where('A', Predicates.blocks(intake.get()).addTooltips(Component.translatable("gtceu.multiblock.pattern.clear_amount_1")))
                        .where('Y', Predicates.controller(Predicates.blocks(definition.getBlock())))
                        .build())
                .workableCasingRenderer(casingTexture, overlayModel)
                .tooltips(Component.translatable("gtceu.universal.tooltip.base_production_eut", GTValues.V[tier] * 4), Component.translatable("gtceu.universal.tooltip.uses_per_hour_lubricant", FluidHelper.getBucket()), tier > GTValues.EV ? Component.translatable("gtceu.machine.large_combustion_engine.tooltip.boost_extreme", GTValues.V[tier] * 16) : Component.translatable("gtceu.machine.large_combustion_engine.tooltip.boost_regular", GTValues.V[tier] * 12));
        if (isGTM) builder.tooltipBuilder(GTOMachines.GTO_MODIFY);
        return builder.register();
    }

    public static MultiblockMachineDefinition registerLargeTurbine(GTRegistrate registrate, String name, int tier, boolean special, GTRecipeType recipeType, Supplier<? extends Block> casing, Supplier<? extends Block> gear, ResourceLocation casingTexture, ResourceLocation overlayModel, boolean isGTM) {
        MultiblockMachineBuilder builder = registrate.multiblock(name, holder -> new TurbineMachine(holder, tier, special, false))
                .rotationState(RotationState.ALL)
                .recipeType(recipeType)
                .generator(true)
                .recipeModifier(TurbineMachine::recipeModifier, true)
                .appearanceBlock(casing)
                .pattern(definition -> FactoryBlockPattern.start()
                        .aisle("CCCC", "CHHC", "CCCC")
                        .aisle("CHHC", "RGGR", "CHHC")
                        .aisle("CCCC", "CSHC", "CCCC")
                        .where('S', Predicates.controller(Predicates.blocks(definition.getBlock())))
                        .where('G', Predicates.blocks(gear.get()))
                        .where('C', Predicates.blocks(casing.get()))
                        .where('R', GTOPredicates.RotorBlock(tier).setExactLimit(1)
                                .or(Predicates.abilities(PartAbility.OUTPUT_ENERGY)).setExactLimit(1))
                        .where('H', Predicates.blocks(casing.get()).or(Predicates.autoAbilities(definition.getRecipeTypes(), false, false, true, true, true, true).or(Predicates.autoAbilities(true, true, false))))
                        .build())
                .workableCasingRenderer(casingTexture, overlayModel)
                .tooltips(Component.translatable("gtceu.universal.tooltip.base_production_eut", GTValues.V[tier] * (special ? 3 : 2)), Component.translatable("gtceu.multiblock.turbine.efficiency_tooltip", GTValues.VNF[tier]));
        if (isGTM) builder.tooltipBuilder(GTOMachines.GTO_MODIFY);
        return builder.register();
    }

    //////////////////////////////////////
    // ********** Part **********//
    //////////////////////////////////////
    public static final MachineDefinition[] MANA_INPUT_HATCH = registerTieredMachines("mana_input_hatch",
            (holder, tier) -> new ManaHatchPartMachine(holder, tier, IO.IN, 2),
            (tier, builder) -> builder
                    .langValue(GTValues.VLVH[tier] + " Mana Input Hatch")
                    .rotationState(RotationState.ALL)
                    .abilities(GTOPartAbility.INPUT_MANA)
                    .renderer(() -> new OverlayTieredMachineRenderer(tier, GTCEu.id("block/machine/part/" + "energy_hatch.input_64a")))
                    .register(),
            GTValues.LV);

    public static final MachineDefinition[] MANA_OUTPUT_HATCH = registerTieredMachines("mana_output_hatch",
            (holder, tier) -> new ManaHatchPartMachine(holder, tier, IO.OUT, 2),
            (tier, builder) -> builder
                    .langValue(GTValues.VLVH[tier] + " Mana Output Hatch")
                    .rotationState(RotationState.ALL)
                    .abilities(GTOPartAbility.OUTPUT_MANA)
                    .renderer(() -> new OverlayTieredMachineRenderer(tier, GTCEu.id("block/machine/part/" + "energy_hatch.output_64a")))
                    .register(),
            GTValues.LV);

    public static final MachineDefinition[] ENERGY_INPUT_HATCH_4A = registerTieredMachines("energy_input_hatch_4a",
            (holder, tier) -> new EnergyHatchPartMachine(holder, tier, IO.IN, 4),
            (tier, builder) -> builder
                    .langValue(GTValues.VNF[tier] + " 4A Energy Hatch")
                    .rotationState(RotationState.ALL)
                    .abilities(PartAbility.INPUT_ENERGY)
                    .tooltips(Component.translatable("gtceu.machine.energy_hatch.input_hi_amp.tooltip"))
                    .renderer(() -> new OverlayTieredMachineRenderer(tier, GTCEu.id("block/machine/part/" + "energy_hatch.input_4a")))
                    .register(),
            GTValues.tiersBetween(GTValues.LV, GTValues.HV));

    public static final MachineDefinition[] ENERGY_OUTPUT_HATCH_4A = registerTieredMachines("energy_output_hatch_4a",
            (holder, tier) -> new EnergyHatchPartMachine(holder, tier, IO.OUT, 4),
            (tier, builder) -> builder
                    .langValue(GTValues.VNF[tier] + " 4A Dynamo Hatch")
                    .rotationState(RotationState.ALL)
                    .abilities(PartAbility.OUTPUT_ENERGY)
                    .tooltips(Component.translatable("gtceu.machine.energy_hatch.output_hi_amp.tooltip"))
                    .renderer(() -> new OverlayTieredMachineRenderer(tier, GTCEu.id("block/machine/part/" + "energy_hatch.output_4a")))
                    .register(),
            GTValues.tiersBetween(GTValues.LV, GTValues.HV));

    public static final MachineDefinition[] ENERGY_INPUT_HATCH_16A = registerTieredMachines("energy_input_hatch_16a",
            (holder, tier) -> new EnergyHatchPartMachine(holder, tier, IO.IN, 16),
            (tier, builder) -> builder
                    .langValue(GTValues.VNF[tier] + " 16A Energy Hatch")
                    .rotationState(RotationState.ALL)
                    .abilities(PartAbility.INPUT_ENERGY)
                    .tooltips(Component.translatable("gtceu.machine.energy_hatch.input_hi_amp.tooltip"))
                    .renderer(() -> new OverlayTieredMachineRenderer(tier, GTCEu.id("block/machine/part/" + "energy_hatch.input_16a")))
                    .register(),
            GTValues.tiersBetween(GTValues.LV, GTValues.HV));

    public static final MachineDefinition[] ENERGY_OUTPUT_HATCH_16A = registerTieredMachines("energy_output_hatch_16a",
            (holder, tier) -> new EnergyHatchPartMachine(holder, tier, IO.OUT, 16),
            (tier, builder) -> builder
                    .langValue(GTValues.VNF[tier] + " 16A Dynamo Hatch")
                    .rotationState(RotationState.ALL)
                    .abilities(PartAbility.OUTPUT_ENERGY)
                    .tooltips(Component.translatable("gtceu.machine.energy_hatch.output_hi_amp.tooltip"))
                    .renderer(() -> new OverlayTieredMachineRenderer(tier, GTCEu.id("block/machine/part/" + "energy_hatch.output_16a")))
                    .register(),
            GTValues.tiersBetween(GTValues.LV, GTValues.HV));

    public final static MachineDefinition[] HUGE_FLUID_IMPORT_HATCH = registerHugeFluidHatches("huge_input_hatch", "Huge Input Hatch", "fluid_hatch.import", "fluid_hatch.import", IO.IN, PartAbility.IMPORT_FLUIDS);

    public final static MachineDefinition[] HUGE_FLUID_EXPORT_HATCH = registerHugeFluidHatches("huge_output_hatch", "Huge Output Hatch", "fluid_hatch.export", "fluid_hatch.export", IO.OUT, PartAbility.EXPORT_FLUIDS);

    public static final MachineDefinition[] LASER_INPUT_HATCH_16384 = registerLaserHatch(IO.IN, 16384,
            PartAbility.INPUT_LASER);
    public static final MachineDefinition[] LASER_OUTPUT_HATCH_16384 = registerLaserHatch(IO.OUT, 16384,
            PartAbility.OUTPUT_LASER);
    public static final MachineDefinition[] LASER_INPUT_HATCH_65536 = registerLaserHatch(IO.IN, 65536,
            PartAbility.INPUT_LASER);
    public static final MachineDefinition[] LASER_OUTPUT_HATCH_65536 = registerLaserHatch(IO.OUT, 65536,
            PartAbility.OUTPUT_LASER);
    public static final MachineDefinition[] LASER_INPUT_HATCH_262144 = registerLaserHatch(IO.IN, 262144,
            PartAbility.INPUT_LASER);
    public static final MachineDefinition[] LASER_OUTPUT_HATCH_262144 = registerLaserHatch(IO.OUT, 262144,
            PartAbility.OUTPUT_LASER);
    public static final MachineDefinition[] LASER_INPUT_HATCH_1048576 = registerLaserHatch(IO.IN, 1048576,
            PartAbility.INPUT_LASER);
    public static final MachineDefinition[] LASER_OUTPUT_HATCH_1048576 = registerLaserHatch(IO.OUT, 1048576,
            PartAbility.OUTPUT_LASER);
    public static final MachineDefinition[] LASER_INPUT_HATCH_4194304 = registerLaserHatch(IO.IN, 4194304,
            PartAbility.INPUT_LASER);
    public static final MachineDefinition[] LASER_OUTPUT_HATCH_4194304 = registerLaserHatch(IO.OUT, 4194304,
            PartAbility.OUTPUT_LASER);

    public static final MachineDefinition[] WIRELESS_ENERGY_INPUT_HATCH_64A = registerWirelessEnergyHatch(IO.IN, PartAbility.INPUT_ENERGY, GTValues.tiersBetween(GTValues.EV, GTValues.MAX));
    public static final MachineDefinition[] WIRELESS_ENERGY_OUTPUT_HATCH_64A = registerWirelessEnergyHatch(IO.OUT, PartAbility.OUTPUT_ENERGY, GTValues.tiersBetween(GTValues.EV, GTValues.MAX));

    public static final MachineDefinition[] WIRELESS_ENERGY_INPUT_HATCH_262144A = registerWirelessLaserHatch(IO.IN, 262144, PartAbility.INPUT_LASER);
    public static final MachineDefinition[] WIRELESS_ENERGY_INPUT_HATCH_1048576A = registerWirelessLaserHatch(IO.IN, 1048576, PartAbility.INPUT_LASER);
    public static final MachineDefinition[] WIRELESS_ENERGY_INPUT_HATCH_4194304A = registerWirelessLaserHatch(IO.IN, 4194304, PartAbility.INPUT_LASER);
    public static final MachineDefinition[] WIRELESS_ENERGY_OUTPUT_HATCH_262144A = registerWirelessLaserHatch(IO.OUT, 262144, PartAbility.OUTPUT_LASER);
    public static final MachineDefinition[] WIRELESS_ENERGY_OUTPUT_HATCH_1048576A = registerWirelessLaserHatch(IO.OUT, 1048576, PartAbility.OUTPUT_LASER);
    public static final MachineDefinition[] WIRELESS_ENERGY_OUTPUT_HATCH_4194304A = registerWirelessLaserHatch(IO.OUT, 4194304, PartAbility.OUTPUT_LASER);

    public static final MachineDefinition[] ROTOR_HOLDER = registerTieredMachines("rotor_holder",
            RotorHolderPartMachine::new,
            (tier, builder) -> builder.langValue("%s Rotor Holder".formatted(GTValues.VNF[tier]))
                    .rotationState(RotationState.ALL)
                    .abilities(PartAbility.ROTOR_HOLDER)
                    .renderer(() -> new RotorHolderMachineRenderer(tier))
                    .tooltips(LangHandler.getFromMultiLang("gtceu.machine.rotor_holder.tooltip", 0),
                            LangHandler.getFromMultiLang("gtceu.machine.rotor_holder.tooltip", 1),
                            Component.translatable("gtceu.universal.disabled"))
                    .register(),
            GTValues.UHV, GTValues.UEV);

    public static final MachineDefinition LARGE_STEAM_HATCH = REGISTRATE
            .machine("large_steam_input_hatch", holder -> new LargeSteamHatchPartMachine(holder, IO.IN, 8192, false))
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.STEAM)
            .renderer(() -> new OverlaySteamMachineRenderer(GTCEu.id("block/machine/part/" + "steam_hatch")))
            .tooltips(Component.translatable("gtocore.machine.large_steam_input_hatch.tooltip.0"),
                    Component.translatable("gtceu.universal.tooltip.fluid_storage_capacity",
                            8192 * FluidHelper.getBucket()),
                    Component.translatable("gtceu.machine.steam.steam_hatch.tooltip"))
            .register();

    public static final MachineDefinition STERILE_CLEANING_MAINTENANCE_HATCH = REGISTRATE
            .machine("sterile_cleaning_maintenance_hatch", holder -> new CMHatchPartMachine(holder, CMHatchPartMachine.STERILE_DUMMY_CLEANROOM))
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.MAINTENANCE)
            .tooltips(Component.translatable("gtceu.universal.disabled"),
                    Component.translatable("gtceu.machine.maintenance_hatch_cleanroom_auto.tooltip.0"),
                    Component.translatable("gtceu.machine.maintenance_hatch_cleanroom_auto.tooltip.1"))
            .tooltipBuilder((stack, tooltips) -> {
                for (CleanroomType type : CMHatchPartMachine
                        .getCleanroomTypes(CMHatchPartMachine.STERILE_DUMMY_CLEANROOM)) {
                    tooltips.add(Component.literal(String.format("  %s%s", ChatFormatting.GREEN,
                            Component.translatable(type.getTranslationKey()).getString())));
                }
            })
            .renderer(() -> new MaintenanceHatchPartRenderer(7, GTOCore.id("block/machine/part/maintenance.sterile_cleaning")))
            .register();

    public static final MachineDefinition LAW_CLEANING_MAINTENANCE_HATCH = REGISTRATE
            .machine("law_cleaning_maintenance_hatch", holder -> new CMHatchPartMachine(holder, CMHatchPartMachine.LAW_DUMMY_CLEANROOM))
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.MAINTENANCE)
            .tooltips(Component.translatable("gtceu.universal.disabled"),
                    Component.translatable("gtceu.machine.maintenance_hatch_cleanroom_auto.tooltip.0"),
                    Component.translatable("gtceu.machine.maintenance_hatch_cleanroom_auto.tooltip.1"))
            .tooltipBuilder((stack, tooltips) -> {
                for (CleanroomType type : CMHatchPartMachine
                        .getCleanroomTypes(CMHatchPartMachine.LAW_DUMMY_CLEANROOM)) {
                    tooltips.add(Component.literal(String.format("  %s%s", ChatFormatting.GREEN,
                            Component.translatable(type.getTranslationKey()).getString())));
                }
            })
            .renderer(() -> new MaintenanceHatchPartRenderer(10, GTOCore.id("block/machine/part/maintenance.law_cleaning")))
            .register();

    public static final MachineDefinition AUTO_CONFIGURATION_MAINTENANCE_HATCH = REGISTRATE
            .machine("auto_configuration_maintenance_hatch", ACMHatchPartMachine::new)
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.MAINTENANCE)
            .tooltips(Component.translatable("gtceu.universal.disabled"))
            .renderer(() -> new MaintenanceHatchPartRenderer(5, GTCEu.id("block/machine/part/maintenance.full_auto")))
            .register();

    public static final MachineDefinition CLEANING_CONFIGURATION_MAINTENANCE_HATCH = REGISTRATE
            .machine("cleaning_configuration_maintenance_hatch", holder -> new CCMHatchPartMachine(holder, CMHatchPartMachine.DUMMY_CLEANROOM))
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.MAINTENANCE)
            .tooltips(Component.translatable("gtceu.universal.disabled"),
                    Component.translatable("gtceu.machine.maintenance_hatch_cleanroom_auto.tooltip.0"),
                    Component.translatable("gtceu.machine.maintenance_hatch_cleanroom_auto.tooltip.1"))
            .tooltipBuilder((stack, tooltips) -> {
                for (CleanroomType type : CMHatchPartMachine
                        .getCleanroomTypes(CMHatchPartMachine.DUMMY_CLEANROOM)) {
                    tooltips.add(Component.literal(String.format("  %s%s", ChatFormatting.GREEN,
                            Component.translatable(type.getTranslationKey()).getString())));
                }
            })
            .renderer(() -> new MaintenanceHatchPartRenderer(5, GTCEu.id("block/machine/part/maintenance.cleaning")))
            .register();

    public static final MachineDefinition STERILE_CONFIGURATION_CLEANING_MAINTENANCE_HATCH = REGISTRATE
            .machine("sterile_configuration_cleaning_maintenance_hatch", holder -> new CCMHatchPartMachine(holder, CMHatchPartMachine.STERILE_DUMMY_CLEANROOM))
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.MAINTENANCE)
            .tooltips(Component.translatable("gtceu.universal.disabled"),
                    Component.translatable("gtceu.machine.maintenance_hatch_cleanroom_auto.tooltip.0"),
                    Component.translatable("gtceu.machine.maintenance_hatch_cleanroom_auto.tooltip.1"))
            .tooltipBuilder((stack, tooltips) -> {
                for (CleanroomType type : CMHatchPartMachine
                        .getCleanroomTypes(CMHatchPartMachine.STERILE_DUMMY_CLEANROOM)) {
                    tooltips.add(Component.literal(String.format("  %s%s", ChatFormatting.GREEN,
                            Component.translatable(type.getTranslationKey()).getString())));
                }
            })
            .renderer(() -> new MaintenanceHatchPartRenderer(9, GTOCore.id("block/machine/part/maintenance.sterile_cleaning")))
            .register();

    public static final MachineDefinition LAW_CONFIGURATION_CLEANING_MAINTENANCE_HATCH = REGISTRATE
            .machine("law_configuration_cleaning_maintenance_hatch", holder -> new CCMHatchPartMachine(holder, CMHatchPartMachine.LAW_DUMMY_CLEANROOM))
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.MAINTENANCE)
            .tooltips(Component.translatable("gtceu.universal.disabled"),
                    Component.translatable("gtceu.machine.maintenance_hatch_cleanroom_auto.tooltip.0"),
                    Component.translatable("gtceu.machine.maintenance_hatch_cleanroom_auto.tooltip.1"))
            .tooltipBuilder((stack, tooltips) -> {
                for (CleanroomType type : CMHatchPartMachine
                        .getCleanroomTypes(CMHatchPartMachine.LAW_DUMMY_CLEANROOM)) {
                    tooltips.add(Component.literal(String.format("  %s%s", ChatFormatting.GREEN,
                            Component.translatable(type.getTranslationKey()).getString())));
                }
            })
            .renderer(() -> new MaintenanceHatchPartRenderer(12, GTOCore.id("block/machine/part/maintenance.law_cleaning")))
            .register();

    public static final MachineDefinition GRAVITY_HATCH = REGISTRATE
            .machine("gravity_hatch", GravityHatchPartMachine::new)
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.MAINTENANCE)
            .tooltips(Component.translatable("gtceu.universal.disabled"))
            .renderer(() -> new MaintenanceHatchPartRenderer(8, GTCEu.id("block/machine/part/maintenance.full_auto")))
            .register();

    public static final MachineDefinition GRAVITY_CONFIGURATION_HATCH = REGISTRATE
            .machine("gravity_configuration_hatch", CGCHatchPartMachine::new)
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.MAINTENANCE)
            .tooltips(Component.translatable("gtceu.universal.disabled"))
            .renderer(() -> new MaintenanceHatchPartRenderer(10, GTCEu.id("block/machine/part/maintenance.full_auto")))
            .register();

    public static final MachineDefinition VACUUM_HATCH = REGISTRATE
            .machine("vacuum_hatch", VacuumHatchPartMachine::new)
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.MAINTENANCE)
            .tooltips(Component.translatable("gtocore.recipe.vacuum.tier", 4))
            .tooltips(Component.translatable("gtceu.universal.disabled"))
            .renderer(() -> new MaintenanceHatchPartRenderer(4, GTCEu.id("block/machine/part/maintenance.full_auto")))
            .register();

    public static final MachineDefinition VACUUM_CONFIGURATION_HATCH = REGISTRATE
            .machine("vacuum_configuration_hatch", CVCHatchPartMachine::new)
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.MAINTENANCE)
            .tooltips(Component.translatable("gtocore.recipe.vacuum.tier", 4))
            .tooltips(Component.translatable("gtceu.universal.disabled"))
            .renderer(() -> new MaintenanceHatchPartRenderer(6,
                    GTCEu.id("block/machine/part/maintenance.full_auto")))
            .register();

    public static final MachineDefinition[] NEUTRON_ACCELERATOR = registerTieredMachines("neutron_accelerator",
            NeutronAcceleratorPartMachine::new,
            (tier, builder) -> builder
                    .langValue(GTValues.VNF[tier] + "Neutron Accelerator")
                    .rotationState(RotationState.ALL)
                    .abilities(GTOPartAbility.NEUTRON_ACCELERATOR)
                    .tooltips(Component.translatable("gtceu.universal.tooltip.max_voltage_in", GTValues.V[tier], GTValues.VNF[tier]),
                            Component.translatable("gtocore.machine.neutron_accelerator.tooltip.0", GTValues.V[tier] * 8 / 10),
                            Component.translatable("gtocore.machine.neutron_accelerator.tooltip.1"),
                            Component.translatable("gtceu.universal.tooltip.energy_storage_capacity", 2 * GTValues.V[tier]))
                    .overlayTieredHullRenderer("neutron_accelerator")
                    .register(),
            GTMachines.ALL_TIERS);

    public final static MachineDefinition NEUTRON_SENSOR = REGISTRATE
            .machine("neutron_sensor", SensorPartMachine::new)
            .tier(GTValues.IV)
            .rotationState(RotationState.ALL)
            .tooltips(Component.translatable("gtocore.machine.neutron_sensor.tooltip.0"))
            .overlayTieredHullRenderer("neutron_sensor")
            .register();

    public final static MachineDefinition PH_SENSOR = REGISTRATE
            .machine("ph_sensor", SensorPartMachine::new)
            .langValue("pH Sensor")
            .tier(GTValues.EV)
            .rotationState(RotationState.ALL)
            .overlayTieredHullRenderer("neutron_sensor")
            .register();

    public final static MachineDefinition HEAT_SENSOR = REGISTRATE
            .machine("heat_sensor", SensorPartMachine::new)
            .tier(GTValues.MV)
            .rotationState(RotationState.ALL)
            .overlayTieredHullRenderer("neutron_sensor")
            .register();

    public static final MachineDefinition GRIND_BALL_HATCH = REGISTRATE
            .machine("grind_ball_hatch", BallHatchPartMachine::new)
            .tier(GTValues.IV)
            .rotationState(RotationState.ALL)
            .renderer(BallHatchRenderer::new)
            .register();

    public static final MachineDefinition RADIATION_HATCH = REGISTRATE
            .machine("radiation_hatch", RadiationHatchPartMachine::new)
            .tier(GTValues.ZPM)
            .recipeType(GTORecipeTypes.RADIATION_HATCH_RECIPES)
            .rotationState(RotationState.ALL)
            .overlayTieredHullRenderer("radiation_hatch")
            .register();

    public static final MachineDefinition ROTOR_HATCH = REGISTRATE
            .machine("rotor_hatch", h -> new ItemHatchPartMachine(h, 1, i -> TurbineRotorBehaviour.getBehaviour(i) != null))
            .tier(GTValues.EV)
            .rotationState(RotationState.ALL)
            .overlayTieredHullRenderer("rotor_hatch")
            .register();

    public static final MachineDefinition BLOCK_BUS = REGISTRATE
            .machine("block_bus", BlockBusPartMachine::new)
            .tier(GTValues.LuV)
            .rotationState(RotationState.ALL)
            .renderer(() -> new OverlayTieredMachineRenderer(GTValues.LuV, GTCEu.id("block/machine/part/item_bus.import")))
            .register();

    public static final MachineDefinition LENS_HOUSING = REGISTRATE
            .machine("lens_housing", h -> new ItemHatchPartMachine(h, 1, i -> ChemicalHelper.getPrefix(i.getItem()) == TagPrefix.lens))
            .tier(GTValues.EV)
            .rotationState(RotationState.ALL)
            .renderer(() -> new OverlayTieredMachineRenderer(GTValues.EV, GTCEu.id("block/machine/part/item_bus.import")))
            .register();

    public final static MachineDefinition LENS_INDICATOR_HATCH = REGISTRATE
            .machine("lens_indicator_hatch", IndicatorHatchPartMachine::new)
            .tier(GTValues.HV)
            .rotationState(RotationState.ALL)
            .overlayTieredHullRenderer("neutron_sensor")
            .register();

    public final static MachineDefinition DEGASSING_CONTROL_HATCH = REGISTRATE
            .machine("degassing_control_hatch", IndicatorHatchPartMachine::new)
            .tier(GTValues.LuV)
            .rotationState(RotationState.ALL)
            .overlayTieredHullRenderer("neutron_sensor")
            .register();

    public static final MachineDefinition[] MAGIC_SYNTHESIS = registerTieredMachines("magic_synthesis", MagicSynthesisMachine::new,
            (tier, builder) -> builder
                    .langValue("%s Magic Synthesis %s".formatted(GTValues.VLVH[tier], GTValues.VLVT[tier]))
                    .rotationState(RotationState.NON_Y_AXIS)
                    .editableUI(MagicSynthesisMachine.EDITABLE_UI_CREATOR.apply(GTCEu.id("magic_synthesis"), GTORecipeTypes.MAGIC_SYNTHESIS_RECIPES))
                    .recipeModifier((machine, recipe, params, result) -> recipe)
                    .recipeType(GTORecipeTypes.MAGIC_SYNTHESIS_RECIPES)
                    .workableTieredHullRenderer(GTOCore.id("block/machines/magic_synthesis"))
                    .tooltips(workable(GTORecipeTypes.MAGIC_SYNTHESIS_RECIPES, GTMachines.defaultTankSizeFunction.apply(tier)))
                    .register(),
            GTValues.LV);

    public static final MachineDefinition[] CRUCIBLE = registerTieredMachines("crucible", CrucibleMachine::new,
            (tier, builder) -> builder
                    .langValue("%s Crucible %s".formatted(GTValues.VLVH[tier], GTValues.VLVT[tier]))
                    .rotationState(RotationState.NON_Y_AXIS)
                    .editableUI(CrucibleMachine.EDITABLE_UI_CREATOR.apply(GTCEu.id("crucible"), GTORecipeTypes.CRUCIBLE_RECIPES))
                    .recipeModifier((machine, recipe, params, result) -> recipe)
                    .recipeType(GTORecipeTypes.CRUCIBLE_RECIPES)
                    .workableTieredHullRenderer(GTOCore.id("block/machines/crucible"))
                    .tooltips(workable(GTORecipeTypes.CRUCIBLE_RECIPES, GTMachines.defaultTankSizeFunction.apply(tier)))
                    .register(),
            GTValues.LV);

    public static final MachineDefinition[] ALCHEMICAL_BOILER = registerTieredMachines("alchemical_boiler", AlchemicalBoilerMachine::new,
            (tier, builder) -> builder
                    .langValue("%s Alchemical Boiler %s".formatted(GTValues.VLVH[tier], GTValues.VLVT[tier]))
                    .rotationState(RotationState.NON_Y_AXIS)
                    .editableUI(AlchemicalBoilerMachine.EDITABLE_UI_CREATOR.apply(GTCEu.id("alchemical_boiler"), GTORecipeTypes.ALCHEMICAL_BOILER_RECIPES))
                    .recipeModifier(AlchemicalBoilerMachine::recipeModifier)
                    .recipeType(GTORecipeTypes.ALCHEMICAL_BOILER_RECIPES)
                    .workableTieredHullRenderer(GTOCore.id("block/machines/alchemical_boiler"))
                    .tooltips(workable(GTORecipeTypes.ALCHEMICAL_BOILER_RECIPES, GTMachines.defaultTankSizeFunction.apply(tier)))
                    .register(),
            GTValues.LV);

    public static final MachineDefinition[] ALCHEMICAL_DISTILLATION = null; // 同理
}
