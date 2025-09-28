package com.gtocore.utils.register;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.*;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.ICoilMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.registry.registrate.MultiblockMachineBuilder;
import com.gregtechceu.gtceu.client.renderer.machine.OverlayTieredMachineRenderer;
import com.gregtechceu.gtceu.client.renderer.machine.SimpleGeneratorMachineRenderer;
import com.gregtechceu.gtceu.common.data.GCYMBlocks;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.machines.GTMachineUtils;
import com.gregtechceu.gtceu.common.machine.multiblock.part.LaserHatchPartMachine;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gtocore.api.pattern.GTOPredicates;
import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.GTOMachines;
import com.gtocore.common.data.GTORecipeTypes;
import com.gtocore.common.data.machines.MultiBlockA;
import com.gtocore.common.data.translation.GTOMachineTooltips;
import com.gtocore.common.machine.mana.SimpleWorkManaMachine;
import com.gtocore.common.machine.multiblock.generator.CombustionEngineMachine;
import com.gtocore.common.machine.multiblock.generator.TurbineMachine;
import com.gtocore.common.machine.multiblock.part.WirelessEnergyHatchPartMachine;
import com.gtolib.GTOCore;
import com.gtolib.api.GTOValues;
import com.gtolib.api.blockentity.ManaMachineBlockEntity;
import com.gtolib.api.machine.SimpleNoEnergyMachine;
import com.gtolib.api.recipe.modifier.RecipeModifierFunction;
import com.gtolib.api.registries.GTOMachineBuilder;
import com.gtolib.api.registries.GTORegistration;
import com.gtolib.api.registries.MultiblockBuilder;
import com.gtolib.utils.GTOUtils;
import com.hepdd.gtmthings.GTMThings;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.function.TriFunction;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.machine.multiblock.PartAbility.*;
import static com.gregtechceu.gtceu.api.pattern.Predicates.*;
import static com.gtolib.api.GTOValues.MANACN;
import static com.gtolib.api.GTOValues.MANAN;
import static com.gtolib.api.registries.GTORegistration.GTM;
import static com.gtolib.api.registries.GTORegistration.GTO;
import static com.gtolib.utils.register.BlockRegisterUtils.addLang;

public final class MachineRegisterUtils {

    private MachineRegisterUtils() {}

    public static final int[] MANA_TIERS = GTValues.tiersBetween(LV, ZPM);

    private static final MultiblockMachineDefinition DUMMY_MULTIBLOCK = MultiblockMachineDefinition.createDefinition(GTOCore.id("dummy"));

    public static final BiConsumer<IMultiController, List<Component>> CHEMICAL_PLANT_DISPLAY = (controller, components) -> {
        double value = 1 - ((ICoilMachine) controller).getCoilTier() * 0.05;
        components.add(Component.translatable("gtocore.machine.eut_multiplier.tooltip", FormattingUtil.formatNumbers(value * 0.8)));
        components.add(Component.translatable("gtocore.machine.duration_multiplier.tooltip", FormattingUtil.formatNumbers(value * 0.6)));
    };

    public static GTOMachineBuilder machine(String name, String cn, Function<MetaMachineBlockEntity, MetaMachine> metaMachine) {
        addLang(name, cn);
        return GTO.machine(name, metaMachine);
    }

    public static MultiblockBuilder multiblock(String name, String cn, Function<MetaMachineBlockEntity, ? extends MultiblockControllerMachine> metaMachine) {
        addLang(name, cn);
        return GTO.multiblock(name, metaMachine);
    }

    public static MachineDefinition[] registerWirelessEnergyHatch(IO io, int amperage, PartAbility ability) {
        String id = io == IO.IN ? "input" : "output";
        String iao = io == IO.IN ? "in" : "out";
        String render = "wireless_energy_hatch";
        render = switch (amperage) {
            case 2 -> render;
            case 4 -> render + "_4a";
            case 16 -> render + "_16a";
            case 64 -> render + "_64a";
            default -> "wireless_laser_hatch.target";
        };
        String finalRender = render;
        int t = LV;
        if (amperage == 64) t = EV;
        else if (amperage > 64) t = IV;
        return registerTieredMachines("wireless_" + id + "_hatch" + (amperage > 2 ? "_" + amperage + "a" : ""), tier -> (amperage > 2 ? amperage + (amperage > 64 ? "§e安§r" : "安") : "") + GTOValues.VNFR[tier] + "无线" + (io == IO.IN ? "能源" : "动力") + "仓",
                (holder, tier) -> new WirelessEnergyHatchPartMachine(holder, tier, io, amperage), (tier, builder) -> builder
                        .langValue(GTOValues.VNFR[tier] + " " + (amperage > 2 ? FormattingUtil.formatNumbers(amperage) + (amperage > 64 ? "§eA§r " : "A ") : "") + "Wireless " + (io == IO.IN ? "Energy" : "Dynamo") + " Hatch")
                        .allRotation()
                        .abilities(ability)
                        .tooltips(Component.translatable("gtceu.universal.tooltip.voltage_" + iao, FormattingUtil.formatNumbers(V[tier]), VNF[tier]),
                                Component.translatable("gtceu.universal.tooltip.amperage_" + iao, amperage),
                                Component.translatable("gtceu.universal.tooltip.energy_storage_capacity", FormattingUtil.formatNumbers(WirelessEnergyHatchPartMachine.getHatchEnergyCapacity(tier, amperage))),
                                Component.translatable("gtmthings.machine.wireless_energy_hatch." + id + ".tooltip"))
                        .renderer(() -> new OverlayTieredMachineRenderer(tier, GTMThings.id("block/machine/part/" + finalRender)))
                        .register(),
                tiersBetween(t, MAX));
    }

    public static MachineDefinition[] registerLaserHatch(IO io, int amperage, PartAbility ability) {
        String name = io == IO.IN ? "target" : "source";
        return registerTieredMachines(amperage + "a_laser_" + name + "_hatch", tier -> amperage + "§e安§r" + GTOValues.VNFR[tier] + "激光" + (io == IO.IN ? "靶" : "源") + "仓",
                (holder, tier) -> new LaserHatchPartMachine(holder, io, tier, amperage), (tier, builder) -> builder
                        .langValue(GTOValues.VNFR[tier] + " " + FormattingUtil.formatNumbers(amperage) + "§eA§r Laser " + FormattingUtil.toEnglishName(name) + " Hatch")
                        .allRotation()
                        .tooltips(Component.translatable("gtceu.machine.laser_hatch." + name + ".tooltip"),
                                Component.translatable("gtceu.machine.laser_hatch.both.tooltip"))
                        .notAllowSharedTooltips()
                        .abilities(ability)
                        .renderer(() -> new OverlayTieredMachineRenderer(tier, GTCEu.id("block/machine/part/laser_hatch." + name)))
                        .register(),
                tiersBetween(IV, MAX));
    }

    public static MachineDefinition[] registerSimpleGenerator(String name, String cn, GTRecipeType recipeType, Int2IntFunction tankScalingFunction, int... tiers) {
        return registerTieredMachines(name, tier -> "%s%s %s".formatted(GTOValues.VLVHCN[tier], cn, VLVT[tier]),
                (holder, tier) -> new SimpleGeneratorMachine(holder, tier, 0.1F * tier, tankScalingFunction),
                (tier, builder) -> builder
                        .langValue("%s %s %s".formatted(VLVH[tier], FormattingUtil.toEnglishName(name), VLVT[tier]))
                        .editableUI(SimpleGeneratorMachine.EDITABLE_UI_CREATOR.apply(GTCEu.id(name), recipeType))
                        .allRotation()
                        .recipeType(recipeType)
                        .recipeModifier(RecipeModifierFunction.SIMPLE_GENERATOR_MACHINEMODIFIER)
                        .addOutputLimit(ItemRecipeCapability.CAP, 0)
                        .addOutputLimit(FluidRecipeCapability.CAP, 0)
                        .renderer(() -> new SimpleGeneratorMachineRenderer(tier, GTOCore.id("block/generators/" + name)))
                        .tooltips(Component.translatable("gtocore.machine.efficiency.tooltip", GTOUtils.getGeneratorEfficiency(recipeType, tier)).append("%"))
                        .tooltips(Component.translatable("gtceu.universal.tooltip.amperage_out", GTOUtils.getGeneratorAmperage(tier)))
                        .tooltips(GTMachineUtils.workableTiered(tier, V[tier], V[tier] << 6, recipeType, tankScalingFunction.apply(tier), false))
                        .register(),
                tiers);
    }

    public static MachineDefinition[] registerSimpleMachines(String name, String cn, GTRecipeType recipeType,
                                                             Int2IntFunction tankScalingFunction) {
        return registerSimpleMachines(name, cn, recipeType, tankScalingFunction, GTMachineUtils.ELECTRIC_TIERS);
    }

    public static MachineDefinition[] registerSimpleMachines(String name, String cn,
                                                             GTRecipeType recipeType,
                                                             Int2IntFunction tankScalingFunction,
                                                             int... tiers) {
        return registerSimpleMachines(name, cn, recipeType, tankScalingFunction, GTOCore.id("block/machines/" + name), tiers);
    }

    public static MachineDefinition[] registerSimpleMachines(String name, String cn,
                                                             GTRecipeType recipeType,
                                                             Int2IntFunction tankScalingFunction,
                                                             ResourceLocation workableModel, int... tiers) {
        return registerTieredMachines(name, tier -> "%s%s %s".formatted(GTOValues.VLVHCN[tier], cn, VLVT[tier]),
                (holder, tier) -> new SimpleTieredMachine(holder, tier, tankScalingFunction), (tier, builder) -> {
                    builder.recipeModifier(RecipeModifierFunction.OVERCLOCKING);
                    return builder
                            .langValue("%s %s %s".formatted(VLVH[tier], FormattingUtil.toEnglishName(name), VLVT[tier]))
                            .editableUI(SimpleTieredMachine.EDITABLE_UI_CREATOR.apply(GTCEu.id(name), recipeType))
                            .nonYAxisRotation()
                            .recipeType(recipeType)
                            .workableTieredHullRenderer(workableModel)
                            .tooltips(GTMachineUtils.workableTiered(tier, V[tier], V[tier] << 6, recipeType,
                                    tankScalingFunction.apply(tier), true))
                            .register();
                },
                tiers);
    }

    public static MachineDefinition[] registerSimpleNoEnergyMachines(String name, String cn,
                                                                     GTRecipeType recipeType,
                                                                     Int2IntFunction tankScalingFunction,
                                                                     int... tiers) {
        return registerSimpleNoEnergyMachines(name, cn, recipeType, tankScalingFunction, GTOCore.id("block/machines/" + name), tiers);
    }

    private static MachineDefinition[] registerSimpleNoEnergyMachines(String name, String cn,
                                                                      GTRecipeType recipeType,
                                                                      Int2IntFunction tankScalingFunction,
                                                                      ResourceLocation workableModel, int... tiers) {
        return registerTieredMachines(name, tier -> "%s%s %s".formatted(GTOValues.VLVHCN[tier], cn, VLVT[tier]),
                (holder, tier) -> new SimpleNoEnergyMachine(holder, tier, tankScalingFunction), (tier, builder) -> builder
                        .langValue("%s %s %s".formatted(VLVH[tier], FormattingUtil.toEnglishName(name), VLVT[tier]))
                        .editableUI(SimpleNoEnergyMachine.EDITABLE_UI_CREATOR.apply(GTCEu.id(name), recipeType))
                        .nonYAxisRotation()
                        .recipeType(recipeType)
                        .noRecipeModifier()
                        .workableTieredHullRenderer(workableModel)
                        .tooltips(workableNoEnergy(recipeType, tankScalingFunction.apply(tier)))
                        .register(),
                tiers);
    }

    public static Component[] workableNoEnergy(GTRecipeType recipeType, long tankCapacity) {
        List<Component> tooltipComponents = new ObjectArrayList<>();
        if (recipeType.getMaxInputs(FluidRecipeCapability.CAP) > 0 ||
                recipeType.getMaxOutputs(FluidRecipeCapability.CAP) > 0)
            tooltipComponents
                    .add(Component.translatable("gtceu.universal.tooltip.fluid_storage_capacity",
                            FormattingUtil.formatNumbers(tankCapacity)));
        return tooltipComponents.toArray(Component[]::new);
    }

    public static MachineDefinition[] registerTieredMachines(String name, Function<Integer, String> cn, BiFunction<MetaMachineBlockEntity, Integer, MetaMachine> factory, BiFunction<Integer, GTOMachineBuilder, MachineDefinition> builder, int... tiers) {
        MachineDefinition[] definitions = new MachineDefinition[TIER_COUNT];
        for (int tier : tiers) {
            String n = VN[tier].toLowerCase(Locale.ROOT) + "_" + name;
            if (cn != null) addLang(n, cn.apply(tier));
            GTOMachineBuilder register = GTO.machine(n, holder -> factory.apply(holder, tier)).tier(tier);
            definitions[tier] = builder.apply(tier, register);
        }
        return definitions;
    }

    public static Pair<MachineDefinition, MachineDefinition> registerSteamMachines(String name, String cn,
                                                                                   BiFunction<MetaMachineBlockEntity, Boolean, MetaMachine> factory,
                                                                                   BiFunction<Boolean, GTOMachineBuilder, MachineDefinition> builder) {
        MachineDefinition lowTier = builder.apply(false,
                machine("lp_%s".formatted(name), "低压蒸汽%s".formatted(cn), holder -> factory.apply(holder, false))
                        .langValue("Low Pressure " + FormattingUtil.toEnglishName(name))
                        .tier(0));
        MachineDefinition highTier = builder.apply(true,
                machine("hp_%s".formatted(name), "高压蒸汽%s".formatted(cn), holder -> factory.apply(holder, true))
                        .langValue("High Pressure " + FormattingUtil.toEnglishName(name))
                        .tier(1));
        return Pair.of(lowTier, highTier);
    }

    public static MultiblockMachineDefinition[] registerTieredMultis(String name, Function<Integer, String> cn,
                                                                     BiFunction<MetaMachineBlockEntity, Integer, MultiblockControllerMachine> factory,
                                                                     BiFunction<Integer, MultiblockBuilder, MultiblockMachineDefinition> builder,
                                                                     int... tiers) {
        MultiblockMachineDefinition[] definitions = new MultiblockMachineDefinition[TIER_COUNT];
        for (int tier : tiers) {
            MultiblockBuilder register = multiblock(VN[tier].toLowerCase(Locale.ROOT) + "_" + name, cn.apply(tier), holder -> factory.apply(holder, tier)).tier(tier);
            definitions[tier] = builder.apply(tier, register);
        }
        return definitions;
    }

    public static MultiblockMachineDefinition registerLargeCombustionEngine(GTORegistration registrate, String name, String cn,
                                                                            int tier, GTRecipeType recipeType,
                                                                            Supplier<? extends Block> casing,
                                                                            Supplier<? extends Block> gear,
                                                                            Supplier<? extends Block> intake,
                                                                            ResourceLocation casingTexture,
                                                                            ResourceLocation overlayModel, boolean isGTM) {
        if (!isGTM) addLang(name, cn);
        MultiblockMachineBuilder builder = registrate.multiblock(name, holder -> new CombustionEngineMachine(holder, tier))
                .nonYAxisRotation()
                .recipeTypes(recipeType)
                .tooltips(GTOMachineTooltips.INSTANCE.getLargeCombustionTooltips()
                        .invoke(V[tier] << 1, V[tier] * 6, tier > EV, V[tier] << 3)
                        .getSupplier())
                .moduleTooltips()
                .generator()
                .block(casing)
                .pattern(definition -> FactoryBlockPattern.start(definition)
                        .aisle("XXX", "XDX", "XXX")
                        .aisle("XCX", "CGC", "XCX")
                        .aisle("XCX", "CGC", "XCX")
                        .aisle("AAA", "AYA", "AAA")
                        .where('X', blocks(casing.get()))
                        .where('G', blocks(gear.get()))
                        .where('C', blocks(casing.get()).setMinGlobalLimited(3).or(autoAbilities(definition.getRecipeTypes(), false, false, true, true, true, true)).or(autoAbilities(true, true, false)))
                        .where('D', ability(PartAbility.OUTPUT_ENERGY, Stream.of(EV, IV, LuV, ZPM, UV, UHV).filter(t -> t >= tier).mapToInt(Integer::intValue).toArray()).addTooltips(Component.translatable("gtceu.machine.large_combustion_engine.tooltip.boost_regular", VN[tier])))
                        .where('A', blocks(intake.get()).addTooltips(Component.translatable("gtceu.multiblock.pattern.clear_amount_1")))
                        .where('Y', controller(blocks(definition.getBlock())))
                        .build())
                .workableCasingRenderer(casingTexture, overlayModel);
        if (tier == EV) {
            if (recipeType == GTORecipeTypes.SEMI_FLUID_GENERATOR_FUELS) {
                // 大型半流质
                builder.addSubPattern(definition -> FactoryBlockPattern.start(definition)
                        .aisle("ADDDA", "BFBFB", "BFFFB", "BBBBB")
                        .aisle("ACCCA", "ACBCA", "ACCCA", " AAA ")
                        .aisle("ACCCA", " EBE ", " CCC ", " GGG ")
                        .aisle("A   A", "A   A", "A   A", " AAA ")
                        .aisle("A   A", "     ", "     ", " GGG ")
                        .aisle("A   A", "A   A", "A   A", " AAA ")
                        .aisle("A   A", "  H  ", "     ", "     ")
                        .where('A', blocks(GTBlocks.CASING_TITANIUM_TURBINE.get()))
                        .where('B', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.StainlessSteel)))
                        .where('C', blocks(GTBlocks.CASING_TITANIUM_STABLE.get()))
                        .where('D', blocks(GTBlocks.FIREBOX_TITANIUM.get()))
                        .where('E', abilities(MUFFLER))
                        .where('F', blocks(GTBlocks.CASING_TITANIUM_STABLE.get())
                                .or(abilities(PartAbility.OUTPUT_ENERGY).setMaxGlobalLimited(3)))
                        .where('G', blocks(GTBlocks.CASING_ENGINE_INTAKE.get()))
                        .where('H', controller(blocks(definition.get())))
                        .where(' ', any())
                        .build());
            } else {
                // 大型内燃
                builder.addSubPattern(definition -> FactoryBlockPattern.start(definition)
                        .aisle("ADDDA", "BFBFB", "BFFFB", "BBBBB")
                        .aisle("ACCCA", "ACBCA", "ACCCA", " AAA ")
                        .aisle("ACCCA", " EBE ", " CCC ", " GGG ")
                        .aisle("A   A", "A   A", "A   A", " AAA ")
                        .aisle("A   A", "     ", "     ", " GGG ")
                        .aisle("A   A", "A   A", "A   A", " AAA ")
                        .aisle("A   A", "  H  ", "     ", "     ")
                        .where('A', blocks(GTBlocks.CASING_TITANIUM_TURBINE.get()))
                        .where('B', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.BlueSteel)))
                        .where('C', blocks(GTBlocks.CASING_TITANIUM_STABLE.get()))
                        .where('D', blocks(GTBlocks.FIREBOX_TITANIUM.get()))
                        .where('E', abilities(MUFFLER))
                        .where('F', blocks(GTBlocks.CASING_TITANIUM_STABLE.get())
                                .or(abilities(PartAbility.OUTPUT_ENERGY).setMaxGlobalLimited(3)))
                        .where('G', blocks(GTBlocks.CASING_ENGINE_INTAKE.get()))
                        .where('H', controller(blocks(definition.get())))
                        .where(' ', any())
                        .build());
            }
        } else {
            // 极限内燃
            builder.addSubPattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("ADDDA", "BFBFB", "BFFFB", "BBBBB")
                    .aisle("ACCCA", "ACBCA", "ACCCA", " AAA ")
                    .aisle("ACCCA", " EBE ", " CCC ", " GGG ")
                    .aisle("A   A", "A   A", "A   A", " AAA ")
                    .aisle("A   A", "     ", "     ", " GGG ")
                    .aisle("A   A", "A   A", "A   A", " AAA ")
                    .aisle("A   A", "  H  ", "     ", "     ")
                    .where('A', blocks(GTBlocks.CASING_TUNGSTENSTEEL_TURBINE.get()))
                    .where('B', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.BlackSteel)))
                    .where('C', blocks(GTBlocks.CASING_TUNGSTENSTEEL_ROBUST.get()))
                    .where('D', blocks(GTBlocks.FIREBOX_TUNGSTENSTEEL.get()))
                    .where('E', abilities(MUFFLER))
                    .where('F', blocks(GTBlocks.CASING_TUNGSTENSTEEL_ROBUST.get())
                            .or(abilities(PartAbility.OUTPUT_ENERGY).setMaxGlobalLimited(3)))
                    .where('G', blocks(GTBlocks.CASING_EXTREME_ENGINE_INTAKE.get()))
                    .where('H', controller(blocks(definition.get())))
                    .where(' ', any())
                    .build());
        }
        return builder.register();
    }

    public static MultiblockMachineDefinition registerLargeTurbine(GTORegistration registrate, String name, String cn, int tier, boolean special, GTRecipeType recipeType, Supplier<? extends Block> casing, Supplier<? extends Block> gear, ResourceLocation casingTexture, ResourceLocation overlayModel, boolean isGTM) {
        if (Objects.equals(name, "plasma_large_turbine")) {
            DUMMY_MULTIBLOCK.setItemSupplier(MultiBlockA.VOID_MINER::getItem);
            return DUMMY_MULTIBLOCK;
        }
        if (!isGTM) addLang(name, cn);
        MultiblockMachineBuilder builder = registrate.multiblock(name, holder -> new TurbineMachine(holder, tier, special, false))
                .addTooltipsFromClass(TurbineMachine.class)
                .tooltips(GTOMachineTooltips.INSTANCE.getLargeTurbineTooltips().invoke((long) (V[tier] * (special ? 2.5 : 2)), tier).getSupplier())
                .tooltips(GTOMachineTooltips.INSTANCE.getTurbineHighSpeedTooltips().getSupplier())
                .moduleTooltips()
                .nonYAxisRotation()
                .recipeTypes(recipeType)
                .generator()
                .block(casing)
                .pattern(definition -> FactoryBlockPattern.start(definition)
                        .aisle("CCCC", "CHHC", "CCCC")
                        .aisle("CHHC", "RGGR", "CHHC")
                        .aisle("CCCC", "CSHC", "CCCC")
                        .where('S', controller(blocks(definition.getBlock())))
                        .where('G', blocks(gear.get()))
                        .where('C', blocks(casing.get()))
                        .where('R', GTOPredicates.RotorBlock(tier).setExactLimit(1)
                                .or(abilities(PartAbility.OUTPUT_ENERGY)).setExactLimit(1))
                        .where('H', blocks(casing.get()).or(autoAbilities(definition.getRecipeTypes(), false, false, true, true, true, true).or(autoAbilities(true, true, false))))
                        .build())
                .workableCasingRenderer(casingTexture, overlayModel);
        if (recipeType == GTORecipeTypes.STEAM_TURBINE_FUELS) {
            builder.addSubPattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("AAAAAAA", "A   ABA", "A   ABA", "AAAAAAA")
                    .aisle("    CCD", "    CCD", "    CCD", "A   ABA")
                    .aisle("    CCD", "    FFF", "    CCD", "A   ABA")
                    .aisle("    CCD", " E  CCD", "    CCD", "A   ABA")
                    .aisle("AAAAAAA", "A   ABA", "A   ABA", "AAAAAAA")
                    .where('A', blocks(GTBlocks.CASING_STEEL_SOLID.get()))
                    .where('B', blocks(GTBlocks.CASING_STEEL_PIPE.get()))
                    .where('C', blocks(GTBlocks.CASING_STEEL_TURBINE.get()))
                    .where('D', blocks(GTBlocks.CASING_STEEL_TURBINE.get())
                            .or(abilities(PartAbility.OUTPUT_ENERGY).setMaxGlobalLimited(3)))
                    .where('E', controller(blocks(definition.get())))
                    .where('F', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.StainlessSteel)))
                    .where(' ', any())
                    .build());
        } else if (recipeType == GTORecipeTypes.GAS_TURBINE_FUELS) {
            builder.addSubPattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("AAAAAAA", "A   ABA", "A   ABA", "AAAAAAA")
                    .aisle("    CCD", "    CCD", "    CCD", "A   ABA")
                    .aisle("    CCD", "    FFF", "    CCD", "A   ABA")
                    .aisle("    CCD", " E  CCD", "    CCD", "A   ABA")
                    .aisle("AAAAAAA", "A   ABA", "A   ABA", "AAAAAAA")
                    .where('A', blocks(GTBlocks.CASING_STAINLESS_CLEAN.get()))
                    .where('B', blocks(GTBlocks.CASING_ENGINE_INTAKE.get()))
                    .where('C', blocks(GTBlocks.CASING_STAINLESS_TURBINE.get()))
                    .where('D', blocks(GTBlocks.CASING_STAINLESS_TURBINE.get())
                            .or(abilities(PartAbility.OUTPUT_ENERGY).setMaxGlobalLimited(3)))
                    .where('E', controller(blocks(definition.get())))
                    .where('F', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.BlackSteel)))
                    .where(' ', any())
                    .build());
        } else if (recipeType == GTORecipeTypes.ROCKET_ENGINE_FUELS) {
            builder.addSubPattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("AAAAAAA", "A   ABA", "A   ABA", "AAAAAAA")
                    .aisle("    CCD", "    CCD", "    CCD", "A   ABA")
                    .aisle("    CCD", "    FFF", "    CCD", "A   ABA")
                    .aisle("    CCD", " E  CCD", "    CCD", "A   ABA")
                    .aisle("AAAAAAA", "A   ABA", "A   ABA", "AAAAAAA")
                    .where('A', blocks(GTBlocks.CASING_TITANIUM_STABLE.get()))
                    .where('B', blocks(GTBlocks.CASING_ENGINE_INTAKE.get()))
                    .where('C', blocks(GTBlocks.CASING_TITANIUM_TURBINE.get()))
                    .where('D', blocks(GTBlocks.CASING_TITANIUM_TURBINE.get())
                            .or(abilities(PartAbility.OUTPUT_ENERGY).setMaxGlobalLimited(3)))
                    .where('E', controller(blocks(definition.get())))
                    .where('F', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.BlueSteel)))
                    .where(' ', any())
                    .build());
        } else if (recipeType == GTORecipeTypes.SUPERCRITICAL_STEAM_TURBINE_FUELS) {
            builder.addSubPattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("AAAAAAA", "A   ABA", "A   ABA", "AAAAAAA")
                    .aisle("    CCD", "    CCD", "    CCD", "A   ABA")
                    .aisle("    CCD", "    FFF", "    CCD", "A   ABA")
                    .aisle("    CCD", " E  CCD", "    CCD", "A   ABA")
                    .aisle("AAAAAAA", "A   ABA", "A   ABA", "AAAAAAA")
                    .where('A', blocks(GCYMBlocks.CASING_HIGH_TEMPERATURE_SMELTING.get()))
                    .where('B', blocks(GCYMBlocks.ELECTROLYTIC_CELL.get()))
                    .where('C', blocks(GTOBlocks.SUPERCRITICAL_TURBINE_CASING.get()))
                    .where('D', blocks(GTOBlocks.SUPERCRITICAL_TURBINE_CASING.get())
                            .or(abilities(PartAbility.OUTPUT_ENERGY).setMaxGlobalLimited(3)))
                    .where('E', controller(blocks(definition.get())))
                    .where('F', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.TungstenSteel)))
                    .where(' ', any())
                    .build());
        }
        return builder.register();
    }

    public static MultiblockMachineDefinition registerMegaTurbine(String name, String cn, int tier, boolean special, GTRecipeType recipeType,
                                                                  Supplier<Block> casing, Supplier<Block> gear, ResourceLocation baseCasing,
                                                                  ResourceLocation overlayModel, Function<MultiblockMachineDefinition, BlockPattern> subPattern) {
        return multiblock(name, cn, holder -> new TurbineMachine.MegaTurbine(holder, tier, special))
                .nonYAxisRotation()
                .recipeTypes(recipeType)
                .generator()
                .tooltips(GTOMachineTooltips.INSTANCE.getMegaTurbineGenerateTooltips()
                        .invoke(V[tier] * (special ? 12 : 8), tier).getSupplier())
                .tooltips(GTOMachineTooltips.INSTANCE.getTurbineHighSpeedTooltips().getSupplier())
                .moduleTooltips()
                .addTooltipsFromClass(TurbineMachine.class)
                .block(casing)
                .pattern(definition -> FactoryBlockPattern.start(definition)
                        .aisle("   AAAAA   ", "  A  A  A  ", " AA  A  AA ", "A  A A A  A", "A   A A   A", "AAAA A AAAA", "A   A A   A", "A  A A A  A", " AA  A  AA ", "  A  A  A  ", "   AAAAA   ")
                        .aisle("   ABABA   ", "  BBBBBBB  ", " BBBBBBBBB ", "ABBBBBBBBBA", "BBBBBBBBBBB", "ABBBBBBBBBA", "BBBBBBBBBBB", "ABBBBBBBBBA", " BBBBBBBBB ", "  BBBBBBB  ", "   ABABA   ")
                        .aisle("   BBBBB   ", "  BBEEEBB  ", " B   E   B ", "BB   E   BB", "BE   E   EB", "BEEEEIEEEEB", "BE   E   EB", "BB   E   BB", " B   E   B ", "  BBEEEBB  ", "   BBBBB   ")
                        .aisle("   BBBBB   ", "  BB   BB  ", " B       B ", "BB       BB", "B         B", "B         B", "B         B", "BB       BB", " B       B ", "  BB   BB  ", "   BBBBB   ")
                        .aisle("   BBBBB   ", "  BBEEEBB  ", " B   E   B ", "BB   E   BB", "BE   E   EB", "BEEEEIEEEEB", "BE   E   EB", "BB   E   BB", " B   E   B ", "  BBEEEBB  ", "   BBBBB   ")
                        .aisle("   BBBBB   ", "  BBEEEBB  ", " B   E   B ", "BB   E   BB", "BE   E   EB", "BEEEEIEEEEB", "BE   E   EB", "BB   E   BB", " B   E   B ", "  BBEEEBB  ", "   BBBBB   ")
                        .aisle("   BBBBB   ", "  BB   BB  ", " B       B ", "BB       BB", "B         B", "B         B", "B         B", "BB       BB", " B       B ", "  BB   BB  ", "   BBBBB   ")
                        .aisle("   BBBBB   ", "  BBEEEBB  ", " B   E   B ", "BB   E   BB", "BE   E   EB", "BEEEEIEEEEB", "BE   E   EB", "BB   E   BB", " B   E   B ", "  BBEEEBB  ", "   BBBBB   ")
                        .aisle("   ABABA   ", "  BBBBBBB  ", " BBBBBBBBB ", "ABBBBBBBBBA", "BBBBBBBBBBB", "ABBBBEBBBBA", "BBBBBBBBBBB", "ABBBBBBBBBA", " BBBBBBBBB ", "  BBBBBBB  ", "   ABABA   ")
                        .aisle("   AAAAA   ", "  A  A  A  ", " AA  A  AA ", "A  A A A  A", "A   AAA   A", "AAAAAEAAAAA", "A   AAA   A", "A  A A A  A", " AA  A  AA ", "  A  A  A  ", "   AAAAA   ")
                        .aisle("           ", "           ", "           ", "           ", "    AAA    ", "    AEA    ", "    AAA    ", "           ", "           ", "           ", "           ")
                        .aisle("           ", "           ", "           ", "           ", "    AAA    ", "    AEA    ", "    AAA    ", "           ", "           ", "           ", "           ")
                        .aisle("   AAAAA   ", "  A  A  A  ", " AA  A  AA ", "A  A A A  A", "A   A A   A", "AAAA A AAAA", "A   A A   A", "A  A A A  A", " AA  A  AA ", "  A  A  A  ", "   AAAAA   ")
                        .aisle("   ABABA   ", "  BBBBBBB  ", " BBBBBBBBB ", "ABBBBBBBBBA", "BBBBBBBBBBB", "ABBBBBBBBBA", "BBBBBBBBBBB", "ABBBBBBBBBA", " BBBBBBBBB ", "  BBBBBBB  ", "   ABABA   ")
                        .aisle("   BBBBB   ", "  BBEEEBB  ", " B   E   B ", "BB   E   BB", "BE   E   EB", "BEEEEIEEEEB", "BE   E   EB", "BB   E   BB", " B   E   B ", "  BBEEEBB  ", "   BBBBB   ")
                        .aisle("   BBBBB   ", "  BB   BB  ", " B       B ", "BB       BB", "B         B", "B         B", "B         B", "BB       BB", " B       B ", "  BB   BB  ", "   BBBBB   ")
                        .aisle("   BBBBB   ", "  BBEEEBB  ", " B   E   B ", "BB   E   BB", "BE   E   EB", "BEEEEIEEEEB", "BE   E   EB", "BB   E   BB", " B   E   B ", "  BBEEEBB  ", "   BBBBB   ")
                        .aisle("   BBBBB   ", "  BBEEEBB  ", " B   E   B ", "BB   E   BB", "BE   E   EB", "BEEEEIEEEEB", "BE   E   EB", "BB   E   BB", " B   E   B ", "  BBEEEBB  ", "   BBBBB   ")
                        .aisle("   BBBBB   ", "  BB   BB  ", " B       B ", "BB       BB", "B         B", "B         B", "B         B", "BB       BB", " B       B ", "  BB   BB  ", "   BBBBB   ")
                        .aisle("   BBBBB   ", "  BBEEEBB  ", " B   E   B ", "BB   E   BB", "BE   E   EB", "BEEEEIEEEEB", "BE   E   EB", "BB   E   BB", " B   E   B ", "  BBEEEBB  ", "   BBBBB   ")
                        .aisle("   ABABA   ", "  BBBBBBB  ", " BBBBBBBBB ", "ABBBBBBBBBA", "BBBBBBBBBBB", "ABBBBEBBBBA", "BBBBBBBBBBB", "ABBBBBBBBBA", " BBBBBBBBB ", "  BBBBBBB  ", "   ABABA   ")
                        .aisle("   AAAAA   ", "  A  A  A  ", " AA  A  AA ", "A  A A A  A", "A   AAA   A", "AAAAAEAAAAA", "A   AAA   A", "A  A A A  A", " AA  A  AA ", "  A  A  A  ", "   AAAAA   ")
                        .aisle("           ", "           ", "           ", "           ", "    AAA    ", "    AEA    ", "    AAA    ", "           ", "           ", "           ", "           ")
                        .aisle("           ", "           ", "           ", "           ", "    AAA    ", "    AEA    ", "    AAA    ", "           ", "           ", "           ", "           ")
                        .aisle("           ", "           ", "    AAA    ", "   A A A   ", "  A AAA A  ", "  AAAEAAA  ", "  A AAA A  ", "   A A A   ", "    AAA    ", "           ", "           ")
                        .aisle("           ", "           ", "    ABA    ", "   BBBBB   ", "  ABBBBBA  ", "  BBBEBBB  ", "  ABBBBBA  ", "   BBBBB   ", "    ABA    ", "           ", "           ")
                        .aisle("           ", "           ", "    BBB    ", "   B   B   ", "  B     B  ", "  B  E  B  ", "  B     B  ", "   B   B   ", "    BBB    ", "           ", "           ")
                        .aisle("           ", "           ", "    BBB    ", "   BGGGB   ", "  BGAEAGB  ", "  HGEIEGH  ", "  BGAEAGB  ", "   BGGGB   ", "    BHB    ", "           ", "           ")
                        .aisle("           ", "           ", "    BBB    ", "   B   B   ", "  H     H  ", "  H     H  ", "  H     H  ", "   B   B   ", "    HHH    ", "           ", "           ")
                        .aisle("           ", "           ", "    BBB    ", "   BGGGB   ", "  HGAEAGH  ", "  HGEIEGH  ", "  HGAEAGH  ", "   BGGGB   ", "    HHH    ", "           ", "           ")
                        .aisle("           ", "           ", "    BBB    ", "   BGGGB   ", "  HGAEAGH  ", "  HGEIEGH  ", "  HGAEAGH  ", "   BGGGB   ", "    HHH    ", "           ", "           ")
                        .aisle("           ", "           ", "    BBB    ", "   B   B   ", "  H     H  ", "  H     H  ", "  H     H  ", "   B   B   ", "    HHH    ", "           ", "           ")
                        .aisle("           ", "           ", "    BBB    ", "   BGGGB   ", "  BGAEAGB  ", "  HGEIEGH  ", "  BGAEAGB  ", "   BGGGB   ", "    BHB    ", "           ", "           ")
                        .aisle("           ", "           ", "    BBB    ", "   B   B   ", "  B     B  ", "  B  E  B  ", "  B     B  ", "   B   B   ", "    BBB    ", "           ", "           ")
                        .aisle("           ", "           ", "    ABA    ", "   BBBBB   ", "  ABBBBBA  ", "  BBBEBBB  ", "  ABBBBBA  ", "   BBBBB   ", "    ABA    ", "           ", "           ")
                        .aisle("           ", "           ", "    AAA    ", "   A A A   ", "  A AAA A  ", "  AAAEAAA  ", "  A AAA A  ", "   A A A   ", "    AAA    ", "           ", "           ")
                        .aisle("           ", "           ", "           ", "           ", "     A     ", "    AEA    ", "     A     ", "           ", "           ", "           ", "           ")
                        .aisle("           ", "           ", "           ", "           ", "     A     ", "    AEA    ", "     A     ", "           ", "           ", "           ", "           ")
                        .aisle("           ", "           ", "   AAAAA   ", "   ABBBA   ", "   BBBBB   ", "   BBEBB   ", "   BBBBB   ", "   ABBBA   ", "   AAAAA   ", "           ", "           ")
                        .aisle("           ", "           ", "   ABBBA   ", "   B   B   ", "   C   C   ", "   C E C   ", "   C   C   ", "   B   B   ", "   ABBBA   ", "           ", "           ")
                        .aisle("           ", "           ", "   ABBBA   ", "   B   B   ", "   C   C   ", "   C E C   ", "   C   C   ", "   B   B   ", "   ABFBA   ", "           ", "           ")
                        .aisle("           ", "           ", "   ABBBA   ", "   B   B   ", "   C   C   ", "   C E C   ", "   C   C   ", "   B   B   ", "   ABBBA   ", "           ", "           ")
                        .aisle("           ", "           ", "   AAAAA   ", "   ABBBA   ", "   BCCCB   ", "   BCDCB   ", "   BCCCB   ", "   ABBBA   ", "   AAAAA   ", "           ", "           ")
                        .where('A', blocks(GTBlocks.CASING_STAINLESS_CLEAN.get()))
                        .where('B', blocks(casing.get()))
                        .where('C', blocks(casing.get())
                                .or(Predicates.blocks(GTMachines.CONTROL_HATCH.getBlock()).setMaxGlobalLimited(1).setPreviewCount(0))
                                .or(abilities(MAINTENANCE).setExactLimit(1))
                                .or(abilities(IMPORT_FLUIDS).setMaxGlobalLimited(8))
                                .or(abilities(EXPORT_FLUIDS).setMaxGlobalLimited(2))
                                .or(abilities(OUTPUT_ENERGY).setMaxGlobalLimited(4))
                                .or(blocks(GTOMachines.ROTOR_HATCH.getBlock()).setMaxGlobalLimited(1)))
                        .where('D', controller(blocks(definition.get())))
                        .where('E', blocks(gear.get()))
                        .where('F', abilities(MUFFLER))
                        .where('G', heatingCoils())
                        .where('H', GTOPredicates.glass())
                        .where('I', GTOPredicates.RotorBlock(tier))
                        .where(' ', any())
                        .build())
                .addSubPattern(subPattern)
                .workableCasingRenderer(baseCasing, overlayModel)
                .register();
    }

    //////////////////////////////////////
    // *** Mana Machine ***//

    /// ///////////////////////////////////
    public static GTOMachineBuilder manaMachine(String name, String cn, Function<MetaMachineBlockEntity, MetaMachine> metaMachine) {
        addLang(name, cn);
        return GTO.machine(name, metaMachine, ManaMachineBlockEntity::createBlockEntity);
    }

    public static MachineDefinition[] registerSimpleManaMachines(String name, String cn, GTRecipeType recipeType, Int2IntFunction tankScalingFunction, ResourceLocation workableModel, int... tiers) {
        return registerTieredManaMachines(name, tier -> "%s%s".formatted(MANACN[tier], cn), (holder, tier) -> new SimpleWorkManaMachine(holder, tier, tankScalingFunction), (tier, builder) -> {
            builder.noRecipeModifier();
            return builder
                    .langValue("%s %s".formatted(MANAN[tier], FormattingUtil.toEnglishName(name)))
                    .editableUI(SimpleNoEnergyMachine.EDITABLE_UI_CREATOR.apply(GTCEu.id(name), recipeType))
                    .nonYAxisRotation()
                    .recipeType(recipeType)
                    .tooltips(Component.translatable("gtocore.machine.mana_eu").withStyle(ChatFormatting.GREEN))
                    .tooltips(Component.translatable("gtceu.machine.perfect_oc").withStyle(ChatFormatting.YELLOW))
                    .tooltips(Component.translatable("gtocore.machine.mana_input", Component.literal(GTOValues.MANA[tier] + " /t").withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.AQUA))
                    .workableManaTieredHullRenderer(tier, workableModel)
                    .tooltips(workableNoEnergy(recipeType, tankScalingFunction.apply(tier)))
                    .register();
        },
                tiers);
    }

    public static MachineDefinition[] registerTieredManaMachines(String name, Function<Integer, String> cn, BiFunction<MetaMachineBlockEntity, Integer, MetaMachine> factory, BiFunction<Integer, GTOMachineBuilder, MachineDefinition> builder, int... tiers) {
        MachineDefinition[] definitions = new MachineDefinition[TIER_COUNT];
        for (int tier : tiers) {
            GTOMachineBuilder register = manaMachine(VN[tier].toLowerCase(Locale.ROOT) + "_" + name, cn.apply(tier), holder -> factory.apply(holder, tier)).tier(tier);
            definitions[tier] = builder.apply(tier, register);
        }
        return definitions;
    }

    public static MachineDefinition[] registerTieredGTMMachines(String name, BiFunction<MetaMachineBlockEntity, Integer, MetaMachine> factory, BiFunction<Integer, GTOMachineBuilder, MachineDefinition> builder, int... tiers) {
        MachineDefinition[] definitions = new MachineDefinition[GTValues.TIER_COUNT];
        for (int tier : tiers) {
            var register = GTM.machine(GTValues.VN[tier].toLowerCase(Locale.ROOT) + "_" + name,
                    holder -> factory.apply(holder, tier))
                    .tier(tier);
            definitions[tier] = builder.apply(tier, register);
        }
        return definitions;
    }

    public static GTOMachineBuilder blockEntityMachine(String name, String cn, Function<MetaMachineBlockEntity, MetaMachine> metaMachine, TriFunction<BlockEntityType<?>, BlockPos, BlockState, MetaMachineBlockEntity> blockEntityFactory) {
        addLang(name, cn);
        return GTO.machine(name, metaMachine, blockEntityFactory);
    }
}
