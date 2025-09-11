package com.gtocore.common.data;

import com.gtocore.common.machine.multiblock.electric.PCBFactoryMachine;
import com.gtocore.common.machine.multiblock.part.InfiniteIntakeHatchPartMachine;
import com.gtocore.common.recipe.RecipeTypeModify;
import com.gtocore.common.recipe.custom.RecyclerLogic;
import com.gtocore.data.recipe.generated.GenerateDisassembly;

import com.gtolib.GTOCore;
import com.gtolib.api.gui.GTOGuiTextures;
import com.gtolib.api.machine.trait.TierCasingTrait;
import com.gtolib.api.recipe.CombinedRecipeType;
import com.gtolib.api.recipe.RecipeType;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.block.ICoilType;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.sound.ExistingSoundEntry;
import com.gregtechceu.gtceu.common.data.GCYMRecipeTypes;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.common.data.GTSoundEntries;
import com.gregtechceu.gtceu.common.item.armor.PowerlessJetpack;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;

import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import com.lowdragmc.lowdraglib.utils.CycleItemStackHandler;
import com.lowdragmc.lowdraglib.utils.LocalizationUtils;

import java.util.ArrayList;
import java.util.List;

import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;
import static com.gtocore.common.machine.multiblock.part.SpoolHatchPartMachine.SPOOL;
import static com.gtolib.api.GTOValues.*;
import static com.gtolib.utils.register.RecipeTypeRegisterUtils.*;
import static com.gtolib.utils.register.RecipeTypeRegisterUtils.register;
import static com.lowdragmc.lowdraglib.gui.texture.ProgressTexture.FillDirection.LEFT_TO_RIGHT;
import static com.lowdragmc.lowdraglib.gui.texture.ProgressTexture.FillDirection.UP_TO_DOWN;

public final class GTORecipeTypes {

    public static void init() {
        RecipeTypeModify.init();
    }

    public static final RecipeType ALLOY_BLAST_RECIPES = (RecipeType) GCYMRecipeTypes.ALLOY_BLAST_RECIPES;
    public static final RecipeType STEAM_BOILER_RECIPES = (RecipeType) GTRecipeTypes.STEAM_BOILER_RECIPES;
    public static final RecipeType FURNACE_RECIPES = (RecipeType) GTRecipeTypes.FURNACE_RECIPES;
    public static final RecipeType ALLOY_SMELTER_RECIPES = (RecipeType) GTRecipeTypes.ALLOY_SMELTER_RECIPES;
    public static final RecipeType ARC_FURNACE_RECIPES = (RecipeType) GTRecipeTypes.ARC_FURNACE_RECIPES;
    public static final RecipeType ASSEMBLER_RECIPES = (RecipeType) GTRecipeTypes.ASSEMBLER_RECIPES;
    public static final RecipeType AUTOCLAVE_RECIPES = (RecipeType) GTRecipeTypes.AUTOCLAVE_RECIPES;
    public static final RecipeType BENDER_RECIPES = (RecipeType) GTRecipeTypes.BENDER_RECIPES;
    public static final RecipeType BREWING_RECIPES = (RecipeType) GTRecipeTypes.BREWING_RECIPES;
    public static final RecipeType MACERATOR_RECIPES = (RecipeType) GTRecipeTypes.MACERATOR_RECIPES;
    public static final RecipeType CANNER_RECIPES = (RecipeType) GTRecipeTypes.CANNER_RECIPES;
    public static final RecipeType CENTRIFUGE_RECIPES = (RecipeType) GTRecipeTypes.CENTRIFUGE_RECIPES;
    public static final RecipeType CHEMICAL_BATH_RECIPES = (RecipeType) GTRecipeTypes.CHEMICAL_BATH_RECIPES;
    public static final RecipeType CHEMICAL_RECIPES = (RecipeType) GTRecipeTypes.CHEMICAL_RECIPES;
    public static final RecipeType COMPRESSOR_RECIPES = (RecipeType) GTRecipeTypes.COMPRESSOR_RECIPES;
    public static final RecipeType CUTTER_RECIPES = (RecipeType) GTRecipeTypes.CUTTER_RECIPES;
    public static final RecipeType DISTILLERY_RECIPES = (RecipeType) GTRecipeTypes.DISTILLERY_RECIPES;
    public static final RecipeType ELECTROLYZER_RECIPES = (RecipeType) GTRecipeTypes.ELECTROLYZER_RECIPES;
    public static final RecipeType ELECTROMAGNETIC_SEPARATOR_RECIPES = (RecipeType) GTRecipeTypes.ELECTROMAGNETIC_SEPARATOR_RECIPES;
    public static final RecipeType EXTRACTOR_RECIPES = (RecipeType) GTRecipeTypes.EXTRACTOR_RECIPES;
    public static final RecipeType EXTRUDER_RECIPES = (RecipeType) GTRecipeTypes.EXTRUDER_RECIPES;
    public static final RecipeType FERMENTING_RECIPES = (RecipeType) GTRecipeTypes.FERMENTING_RECIPES;
    public static final RecipeType FLUID_HEATER_RECIPES = (RecipeType) GTRecipeTypes.FLUID_HEATER_RECIPES;
    public static final RecipeType FLUID_SOLIDFICATION_RECIPES = (RecipeType) GTRecipeTypes.FLUID_SOLIDFICATION_RECIPES;
    public static final RecipeType FORGE_HAMMER_RECIPES = (RecipeType) GTRecipeTypes.FORGE_HAMMER_RECIPES;
    public static final RecipeType FORMING_PRESS_RECIPES = (RecipeType) GTRecipeTypes.FORMING_PRESS_RECIPES;
    public static final RecipeType LATHE_RECIPES = (RecipeType) GTRecipeTypes.LATHE_RECIPES;
    public static final RecipeType MIXER_RECIPES = (RecipeType) GTRecipeTypes.MIXER_RECIPES;
    public static final RecipeType ORE_WASHER_RECIPES = (RecipeType) GTRecipeTypes.ORE_WASHER_RECIPES;
    public static final RecipeType PACKER_RECIPES = (RecipeType) GTRecipeTypes.PACKER_RECIPES.prepareBuilder(recipeBuilder -> recipeBuilder.EUt(7).duration(20));
    public static final RecipeType POLARIZER_RECIPES = (RecipeType) GTRecipeTypes.POLARIZER_RECIPES;
    public static final RecipeType LASER_ENGRAVER_RECIPES = (RecipeType) GTRecipeTypes.LASER_ENGRAVER_RECIPES;
    public static final RecipeType SIFTER_RECIPES = (RecipeType) GTRecipeTypes.SIFTER_RECIPES;
    public static final RecipeType THERMAL_CENTRIFUGE_RECIPES = (RecipeType) GTRecipeTypes.THERMAL_CENTRIFUGE_RECIPES;
    public static final RecipeType WIREMILL_RECIPES = (RecipeType) GTRecipeTypes.WIREMILL_RECIPES;
    public static final RecipeType CIRCUIT_ASSEMBLER_RECIPES = (RecipeType) GTRecipeTypes.CIRCUIT_ASSEMBLER_RECIPES;
    public static final RecipeType GAS_COLLECTOR_RECIPES = (RecipeType) GTRecipeTypes.GAS_COLLECTOR_RECIPES.onRecipeBuild(InfiniteIntakeHatchPartMachine::init);
    public static final RecipeType AIR_SCRUBBER_RECIPES = (RecipeType) GTRecipeTypes.AIR_SCRUBBER_RECIPES;
    public static final RecipeType RESEARCH_STATION_RECIPES = (RecipeType) GTRecipeTypes.RESEARCH_STATION_RECIPES;
    public static final RecipeType ROCK_BREAKER_RECIPES = (RecipeType) GTRecipeTypes.ROCK_BREAKER_RECIPES;
    public static final RecipeType SCANNER_RECIPES = (RecipeType) GTRecipeTypes.SCANNER_RECIPES;
    public static final RecipeType COMBUSTION_GENERATOR_FUELS = (RecipeType) GTRecipeTypes.COMBUSTION_GENERATOR_FUELS.onRecipeBuild((b) -> PowerlessJetpack.FUELS.putIfAbsent(FluidRecipeCapability.CAP.of(b.input.get(FluidRecipeCapability.CAP).get(0).content).copy(), b.duration));
    public static final RecipeType GAS_TURBINE_FUELS = (RecipeType) GTRecipeTypes.GAS_TURBINE_FUELS;
    public static final RecipeType STEAM_TURBINE_FUELS = (RecipeType) GTRecipeTypes.STEAM_TURBINE_FUELS;
    public static final RecipeType PLASMA_GENERATOR_FUELS = (RecipeType) GTRecipeTypes.PLASMA_GENERATOR_FUELS;
    public static final RecipeType LARGE_BOILER_RECIPES = (RecipeType) GTRecipeTypes.LARGE_BOILER_RECIPES;
    public static final RecipeType COKE_OVEN_RECIPES = (RecipeType) GTRecipeTypes.COKE_OVEN_RECIPES;
    public static final RecipeType PRIMITIVE_BLAST_FURNACE_RECIPES = (RecipeType) GTRecipeTypes.PRIMITIVE_BLAST_FURNACE_RECIPES;
    public static final RecipeType BLAST_RECIPES = (RecipeType) GTRecipeTypes.BLAST_RECIPES;
    public static final RecipeType DISTILLATION_RECIPES = (RecipeType) GTRecipeTypes.DISTILLATION_RECIPES;
    public static final RecipeType PYROLYSE_RECIPES = (RecipeType) GTRecipeTypes.PYROLYSE_RECIPES;
    public static final RecipeType CRACKING_RECIPES = (RecipeType) GTRecipeTypes.CRACKING_RECIPES;
    public static final RecipeType IMPLOSION_RECIPES = (RecipeType) GTRecipeTypes.IMPLOSION_RECIPES;
    public static final RecipeType VACUUM_RECIPES = (RecipeType) GTRecipeTypes.VACUUM_RECIPES;
    public static final RecipeType ASSEMBLY_LINE_RECIPES = (RecipeType) GTRecipeTypes.ASSEMBLY_LINE_RECIPES;
    public static final RecipeType LARGE_CHEMICAL_RECIPES = (RecipeType) GTRecipeTypes.LARGE_CHEMICAL_RECIPES;
    public static final RecipeType FUSION_RECIPES = (RecipeType) GTRecipeTypes.FUSION_RECIPES;
    public static final RecipeType DUMMY_RECIPES = (RecipeType) GTRecipeTypes.DUMMY_RECIPES;

    public static final RecipeType RADIATION_HATCH_RECIPES = register("radiation_hatch", "放射仓材料", MULTIBLOCK)
            .setMaxIOSize(1, 0, 0, 0)
            .setEUIO(IO.NONE)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .addDataInfo(data -> LocalizationUtils.format("gtocore.recipe.radioactivity", data.getInt("radioactivity")));

    public static final RecipeType ARC_GENERATOR_RECIPES = register("arc_generator", "电弧发生器", ELECTRIC)
            .setMaxIOSize(6, 1, 6, 1)
            .setSlotOverlay(true, false, false, GuiTextures.LIGHTNING_OVERLAY_1)
            .setSlotOverlay(false, false, false, GuiTextures.LIGHTNING_OVERLAY_2)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final RecipeType DEHYDRATOR_RECIPES = register("dehydrator", "脱水机", ELECTRIC)
            .setMaxIOSize(2, 6, 2, 2)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_EXTRACT, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final RecipeType UNPACKER_RECIPES = register("unpacker", "解包机", ELECTRIC)
            .setMaxIOSize(2, 2, 0, 0)
            .setEUIO(IO.IN)
            .prepareBuilder(recipeBuilder -> recipeBuilder.EUt(12).duration(10))
            .setSlotOverlay(false, false, true, GuiTextures.BOX_OVERLAY)
            .setSlotOverlay(true, false, GuiTextures.BOXED_OVERLAY)
            .setProgressBar(GuiTextures.PROGRESS_BAR_UNPACKER, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ASSEMBLER);

    public static final RecipeType CLUSTER_RECIPES = register("cluster", "多辊式轧机", ELECTRIC)
            .setMaxIOSize(1, 1, 0, 0)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MOTOR);

    public static final RecipeType ROLLING_RECIPES = register("rolling", "辊轧机", ELECTRIC)
            .setMaxIOSize(2, 1, 0, 0)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_BENDING, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MOTOR);

    public static final RecipeType LAMINATOR_RECIPES = register("laminator", "过胶机", ELECTRIC)
            .setMaxIOSize(3, 1, 1, 0)
            .setEUIO(IO.IN)
            .setSlotOverlay(false, false, GuiTextures.CIRCUIT_OVERLAY)
            .setProgressBar(GuiTextures.PROGRESS_BAR_CIRCUIT, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.BATH);

    public static final RecipeType LOOM_RECIPES = register("loom", "织布机", ELECTRIC)
            .setMaxIOSize(2, 1, 0, 0)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_WIREMILL, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MOTOR);

    public static final RecipeType LASER_WELDER_RECIPES = register("laser_welder", "激光焊接器", ELECTRIC)
            .setMaxIOSize(3, 1, 0, 0)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_WIREMILL, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC)
            .onRecipeBuild(GenerateDisassembly::generateDisassembly);

    public static final RecipeType WORLD_DATA_SCANNER_RECIPES = register("world_data_scanner", "世界信息扫描仪", ELECTRIC)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 1, 2, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final RecipeType VACUUM_PUMP_RECIPES = register("vacuum_pump", "真空泵", STEAM)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 0, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.TURBINE);

    public static final RecipeType THERMAL_GENERATOR_FUELS = register("thermal_generator", "热力发电", GENERATOR)
            .setMaxIOSize(1, 0, 1, 0).setEUIO(IO.OUT)
            .setSlotOverlay(false, true, true, GuiTextures.FURNACE_OVERLAY_2)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COMBUSTION);

    public static final RecipeType SEMI_FLUID_GENERATOR_FUELS = register("semi_fluid_generator", "半流质燃烧", GENERATOR)
            .setMaxIOSize(0, 0, 2, 0).setEUIO(IO.OUT)
            .setSlotOverlay(false, true, true, GuiTextures.FURNACE_OVERLAY_2)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COMBUSTION);

    public static final RecipeType SUPERCRITICAL_STEAM_TURBINE_FUELS = register("supercritical_steam_turbine", "超临界蒸汽发电", GENERATOR)
            .setMaxIOSize(0, 0, 1, 1)
            .setEUIO(IO.OUT)
            .setSlotOverlay(false, true, true, GuiTextures.CENTRIFUGE_OVERLAY)
            .setProgressBar(GuiTextures.PROGRESS_BAR_GAS_COLLECTOR, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.TURBINE);

    public static final RecipeType ROCKET_ENGINE_FUELS = register("rocket_engine", "火箭燃料", GENERATOR)
            .setEUIO(IO.OUT)
            .setMaxIOSize(0, 0, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.JET_ENGINE);

    public static final RecipeType NAQUADAH_REACTOR = register("naquadah_reactor", "硅岩反应", GENERATOR)
            .setEUIO(IO.OUT)
            .setMaxIOSize(0, 0, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COMBUSTION);

    public static final RecipeType EVAPORATION_RECIPES = register("evaporation", "蒸发", ELECTRIC)
            .setMaxIOSize(0, 0, 1, 1)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MOTOR);

    public static final RecipeType ELECTRIC_IMPLOSION_COMPRESSOR_RECIPES = register("electric_implosion_compressor", "电力聚爆压缩", MULTIBLOCK)
            .setMaxIOSize(2, 1, 0, 0).setEUIO(IO.IN)
            .prepareBuilder(recipeBuilder -> recipeBuilder.duration(20).EUt(GTValues.VA[GTValues.UV]))
            .setSlotOverlay(false, false, true, GuiTextures.IMPLOSION_OVERLAY_1)
            .setSlotOverlay(false, false, false, GuiTextures.IMPLOSION_OVERLAY_2)
            .setSlotOverlay(true, false, true, GuiTextures.DUST_OVERLAY)
            .setSound(new ExistingSoundEntry(SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS));

    public static final RecipeType DISASSEMBLY_RECIPES = register("disassembly", "拆解", MULTIBLOCK)
            .setMaxIOSize(1, 16, 0, 4)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ASSEMBLER);

    public static final RecipeType NEUTRON_ACTIVATOR_RECIPES = register("neutron_activator", "中子活化", MULTIBLOCK)
            .setMaxIOSize(6, 3, 1, 1)
            .setSound(GTSoundEntries.COOLING)
            .addDataInfo(data -> LocalizationUtils.format("gtocore.recipe.ev_min", data.getInt("ev_min")))
            .addDataInfo(data -> LocalizationUtils.format("gtocore.recipe.ev_max", data.getInt("ev_max")))
            .addDataInfo(data -> LocalizationUtils.format("gtocore.recipe.evt", data.getInt("evt")));

    public static final RecipeType HEAT_EXCHANGER_RECIPES = register("heat_exchanger", "流体热交换", MULTIBLOCK)
            .setMaxIOSize(0, 0, 2, 3)
            .setProgressBar(GuiTextures.PROGRESS_BAR_MIXER, LEFT_TO_RIGHT)
            .setMaxTooltips(1)
            .setSound(GTSoundEntries.COOLING);

    public static final RecipeType ELEMENT_COPYING_RECIPES = register("element_copying", "元素复制", MULTIBLOCK)
            .setMaxIOSize(1, 1, 1, 1)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final RecipeType INTEGRATED_ORE_PROCESSOR = register("integrated_ore_processor", "集成矿石处理", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 9, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MACERATOR)
            .onRecipeBuild(b -> b.duration((int) (Math.sqrt(b.duration + 100) * 2)));

    public static final RecipeType FISSION_REACTOR_RECIPES = register("fission_reactor", "裂变反应堆", MULTIBLOCK)
            .setMaxIOSize(1, 1, 0, 0)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC)
            .addDataInfo(data -> LocalizationUtils.format("gtocore.recipe.frheat", FormattingUtil.formatNumbers(data.getInt("FRheat"))));

    public static final RecipeType STELLAR_FORGE_RECIPES = register("stellar_forge", "恒星热能熔炼", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(3, 2, 9, 2)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARC_FURNACE, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC)
            .addDataInfo(data -> {
                String tierString = switch (data.getInt(STELLAR_CONTAINMENT_TIER)) {
                    case 3 -> I18n.get("gtocore.tier.ultimate");
                    case 2 -> I18n.get("gtocore.tier.advanced");
                    default -> I18n.get("gtocore.tier.base");
                };
                return LocalizationUtils.format(TierCasingTrait.getTierTranslationKey(STELLAR_CONTAINMENT_TIER), tierString);
            })
            .onRecipeBuild(b -> b.duration(b.duration * GTOCore.difficulty / 3));

    public static final RecipeType COMPONENT_ASSEMBLY_RECIPES = register("component_assembly", "部件装配", MULTIBLOCK)
            .setMaxIOSize(9, 1, 9, 0)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .prepareBuilder(recipeBuilder -> recipeBuilder.addMaterialInfo(true, true))
            .addDataInfo(data -> LocalizationUtils.format(TierCasingTrait.getTierTranslationKey(COMPONENT_ASSEMBLY_CASING_TIER), GTValues.VN[data.getInt(COMPONENT_ASSEMBLY_CASING_TIER)]))
            .setSound(GTSoundEntries.ASSEMBLER);

    public static final RecipeType GREENHOUSE_RECIPES = register("greenhouse", "温室培育", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMANAIO(IO.IN)
            .setMaxIOSize(3, 1, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_BATH, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COOLING);

    public static final RecipeType DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES = register("dimensionally_transcendent_plasma_forge", "超维度熔炼", MULTIBLOCK)
            .setMaxIOSize(2, 2, 2, 2)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARC_FURNACE, LEFT_TO_RIGHT)
            .setSound(GTOSoundEntries.DTPF)
            .addDataInfo(data -> LocalizationUtils.format("gtceu.recipe.temperature", FormattingUtil.formatNumbers(data.getInt("ebf_temp"))))
            .addDataInfo(data -> {
                int temp = data.getInt("ebf_temp");
                ICoilType requiredCoil = ICoilType.getMinRequiredType(temp);
                if (requiredCoil != null && requiredCoil.getMaterial() != null) {
                    return LocalizationUtils.format("gtceu.recipe.coil.tier", (temp > 21600 && temp <= 32000) ? "超级热容" : I18n.get(requiredCoil.getMaterial().getUnlocalizedName()));
                }
                return "";
            })
            .setUiBuilder((recipe, widgetGroup) -> {
                List<List<ItemStack>> items = new ArrayList<>();
                int temp = recipe.data.getInt("ebf_temp");
                items.add(GTCEuAPI.HEATING_COILS.entrySet().stream().filter(coil -> {
                    int ctemp = coil.getKey().getCoilTemperature();
                    if (ctemp == 273) {
                        return temp <= 32000;
                    } else {
                        return ctemp >= temp;
                    }
                }).map(coil -> new ItemStack(coil.getValue().get())).toList());
                widgetGroup.addWidget(new SlotWidget(new CycleItemStackHandler(items), 0, widgetGroup.getSize().width - 25, widgetGroup.getSize().height - 32, false, false));
            });

    public static final RecipeType PLASMA_CONDENSER_RECIPES = register("plasma_condenser", "等离子冷凝", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 2, 2, 2)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COOLING);

    public static final RecipeType RARE_EARTH_CENTRIFUGAL_RECIPES = register("rare_earth_centrifugal", "稀土离心", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 17, 1, 1)
            .setProgressBar(GuiTextures.CENTRIFUGE_OVERLAY, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.CENTRIFUGE);

    public static final RecipeType TRANSCENDING_CRAFTING_RECIPES = register("transcending_crafting", "超临界合成", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(3, 1, 3, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final RecipeType MATTER_FABRICATOR_RECIPES = register("matter_fabricator", "物质制造", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 1, 0, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COOLING);

    public static final RecipeType LARGE_VOID_MINER_RECIPES = register("large_void_miner", "Precise Void Mining", "精准虚空采矿", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 4, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MINER);

    public static final RecipeType RANDOM_ORE_RECIPES = register("random_ore", "Random Void Mining", "随机虚空采矿", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(0, 200, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setXEIVisible(false)
            .setSound(GTSoundEntries.MINER);

    public static final RecipeType ANNIHILATE_GENERATOR_RECIPES = register("annihilate_generator", "湮灭发电", MULTIBLOCK)
            .setEUIO(IO.OUT)
            .setMaxIOSize(1, 1, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final RecipeType HYPER_REACTOR_RECIPES = register("hyper_reactor", "超能反应", MULTIBLOCK)
            .setEUIO(IO.OUT)
            .setMaxIOSize(0, 0, 2, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final RecipeType ADVANCED_HYPER_REACTOR_RECIPES = register("advanced_hyper_reactor", "进阶超能反应", MULTIBLOCK)
            .setEUIO(IO.OUT)
            .setMaxIOSize(0, 0, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final RecipeType LARGE_NAQUADAH_REACTOR_RECIPES = register("large_naquadah_reactor", "进阶硅岩反应", MULTIBLOCK)
            .setEUIO(IO.OUT)
            .setMaxIOSize(0, 0, 2, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COMBUSTION);

    public static final RecipeType COSMOS_SIMULATION_RECIPES = register("cosmos_simulation", "宇宙模拟", MULTIBLOCK)
            .setMaxIOSize(1, 120, 1, 24)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC)
            .addDataInfo(data -> I18n.get("tooltip.avaritia.tier", data.getInt("tier")));

    public static final RecipeType SPACE_PROBE_SURFACE_RECEPTION_RECIPES = register("space_probe_surface_reception", "宇宙射线搜集", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxTooltips(4)
            .setMaxIOSize(2, 0, 0, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final RecipeType DECAY_HASTENER_RECIPES = register("decay_hastener", "衰变扭曲", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(0, 1, 1, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final RecipeType RECYCLER_RECIPES = register("recycler", "材料回收", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 1, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_RECYCLER, LEFT_TO_RIGHT)
            .addCustomRecipeLogic(new RecyclerLogic())
            .setSound(GTSoundEntries.MACERATOR);

    public static final RecipeType MASS_FABRICATOR_RECIPES = register("mass_fabricator", "质量发生器", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 0, 1, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_REPLICATOR, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final RecipeType CIRCUIT_ASSEMBLY_LINE_RECIPES = register("circuit_assembly_line", "电路装配线", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(16, 1, 4, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ASSEMBLER)
            .onRecipeBuild(GenerateDisassembly::generateDisassembly);

    public static final RecipeType SUPRACHRONAL_ASSEMBLY_LINE_RECIPES = register("suprachronal_assembly_line", "超时空装配线", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxTooltips(4)
            .setMaxIOSize(16, 1, 4, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ASSEMBLER)
            .setHasResearchSlot(true)
            .onRecipeBuild(GenerateDisassembly::generateDisassembly);

    public static final RecipeType PRECISION_ASSEMBLER_RECIPES = register("precision_assembler", "精密组装", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxTooltips(4)
            .setMaxIOSize(4, 1, 4, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ASSEMBLER);

    public static final RecipeType ASSEMBLER_MODULE_RECIPES = register("assembler_module", "Space Assembly", "太空组装", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(16, 1, 4, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ASSEMBLER)
            .onRecipeBuild(GenerateDisassembly::generateDisassembly)
            .addDataInfo(data -> LocalizationUtils.format(TierCasingTrait.getTierTranslationKey(POWER_MODULE_TIER), FormattingUtil.formatNumbers(data.getInt(POWER_MODULE_TIER))));

    public static final RecipeType MINER_MODULE_RECIPES = register("miner_module", "Space Miner", "太空采矿", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 6, 1, 0)
            .setProgressBar(GTOGuiTextures.PROGRESS_BAR_MINING_MODULE, UP_TO_DOWN)
            .setSound(GTSoundEntries.MINER);

    public static final RecipeType DRILLING_MODULE_RECIPES = register("drilling_module", "Space Drilling", "太空钻井", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 0, 1, 1)
            .setProgressBar(GTOGuiTextures.PROGRESS_BAR_DRILLING_MODULE, UP_TO_DOWN)
            .setSound(GTSoundEntries.MINER);

    public static final RecipeType FISHING_GROUND_RECIPES = register("fishing_ground", "渔场", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 24, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MINER);

    public static final RecipeType BLOCK_CONVERSIONRECIPES = register("block_conversion", "方块转换", MULTIBLOCK)
            .setEUIO(IO.IN)
            .noSearch(true)
            .setMaxIOSize(1, 1, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final RecipeType INCUBATOR_RECIPES = register("incubator", "培养缸", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(6, 1, 2, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_BATH, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COOLING)
            .addDataInfo(data -> {
                String filterCasing = switch (data.getInt("filter_casing")) {
                    case 3 -> "T3：" + I18n.get("block.gtocore.law_filter_casing");
                    case 2 -> "T2：" + I18n.get("block.gtceu.sterilizing_filter_casing");
                    default -> "T1：" + I18n.get("block.gtceu.filter_casing");
                };
                return LocalizationUtils.format("gtceu.recipe.cleanroom", filterCasing);
            })
            .addDataInfo(data -> data.contains("radioactivity") ? LocalizationUtils.format("gtocore.recipe.radioactivity", data.getInt("radioactivity")) : "");

    public static final RecipeType PCB_FACTORY_RECIPES = register("pcb_factory", "PCB工厂", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 1, 2, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.CHEMICAL)
            .onRecipeBuild((b) -> {
                int tier = 1;
                if (b.EUt() > 491519) {
                    tier = 3;
                } else if (b.EUt() > 30719) {
                    tier = 2;
                }
                b.addData("tier", tier);
            })
            .addDataInfo(data -> LocalizationUtils.format(PCBFactoryMachine.TIER) + data.getInt("tier"));

    public static final RecipeType LAVA_FURNACE_RECIPES = register("lava_furnace", "熔岩炉", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 0, 0, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARC_FURNACE, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.FURNACE);

    public static final RecipeType LARGE_GAS_COLLECTOR_RECIPES = register("large_gas_collector", "Void Gas Collector", "虚空集气", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 0, 0, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COOLING);

    public static final RecipeType AGGREGATION_DEVICE_RECIPES = register("aggregation_device", "聚合装置", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(9, 1, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final RecipeType SUPER_PARTICLE_COLLIDER_RECIPES = register("super_particle_collider", "粒子对撞", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(0, 0, 2, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTOSoundEntries.FUSIONLOOP);

    public static final RecipeType DIMENSIONAL_FOCUS_ENGRAVING_ARRAY_RECIPES = register("dimensional_focus_engraving_array", "维度聚焦激光蚀刻阵列", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxTooltips(4)
            .setMaxIOSize(2, 1, 2, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC)
            .setHasResearchSlot(true);

    public static final RecipeType PRECISION_LASER_ENGRAVER_RECIPES = register("precision_laser_engraver", "精密激光蚀刻", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxTooltips(4)
            .setMaxIOSize(9, 1, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final RecipeType DIMENSIONALLY_TRANSCENDENT_SHOCK_RECIPES = register("dimensionally_transcendent_shock", "超维度震荡", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(9, 1, 6, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_BATH, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MIXER);

    public static final RecipeType NEUTRON_COMPRESSOR_RECIPES = register("neutron_compressor", "奇点压缩", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 1, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_COMPRESS, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COMPRESSOR);

    public static final RecipeType QUANTUM_FORCE_TRANSFORMER_RECIPES = register("quantum_force_transformer", "量子操纵者", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(18, 1, 3, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final RecipeType DRAGON_EGG_COPIER_RECIPES = register("dragon_egg_copier", "龙蛋复制", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 2, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COOLING);

    public static final RecipeType DOOR_OF_CREATE_RECIPES = register("door_of_create", "创造之门", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxTooltips(4)
            .setMaxIOSize(1, 0, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setXEIVisible(false);

    public static final RecipeType BEDROCK_DRILLING_RIG_RECIPES = register("bedrock_drilling_rig", "基岩素提取", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 1, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MACERATOR);

    public static final RecipeType CREATE_AGGREGATION_RECIPES = register("create_aggregation", "创造聚合", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 0, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC)
            .setXEIVisible(false);

    public static final RecipeType GRAVITATION_SHOCKBURST_RECIPES = register("gravitation_shockburst", "时空引力震爆", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 1, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MACERATOR);

    public static final RecipeType ULTIMATE_MATERIAL_FORGE_RECIPES = register("ultimate_material_forge", "终极物质锻造", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 2, 2, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MACERATOR);

    public static final RecipeType DYSON_SPHERE_RECIPES = register("dyson_sphere", "戴森球", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 0, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final RecipeType PETROCHEMICAL_PLANT_RECIPES = register("petrochemical_plant", "集成石油化工处理", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(0, 0, 2, 12)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COOLING);

    public static final RecipeType NANITES_INTEGRATED_PROCESSING_CENTER_RECIPES = register("nanites_integrated_processing_center", "纳米集成加工中心", MULTIBLOCK)
            .setMaxIOSize(9, 9, 9, 9)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .addDataInfo(TEMPERATURE)
            .addDataInfo(COIL)
            .setUiBuilder(COIL_UI)
            .addDataInfo(data -> switch (data.getInt("module")) {
                case 0 -> I18n.get("gtocore.machine.need", I18n.get("block.gtocore.nanites_integrated_processing_center"));
                case 1 -> I18n.get("gtocore.machine.need", I18n.get("block.gtocore.ore_extraction_module"));
                case 2 -> I18n.get("gtocore.machine.need", I18n.get("block.gtocore.bioengineering_module"));
                case 3 -> I18n.get("gtocore.machine.need", I18n.get("block.gtocore.polymer_twisting_module"));
                default -> "";
            })
            .setMaxTooltips(5)
            .setSound(GTSoundEntries.ARC);

    public static final RecipeType NANO_FORGE_RECIPES = register("nano_forge", "纳米蜂群工厂", MULTIBLOCK)
            .setMaxIOSize(6, 1, 3, 0)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .addDataInfo(data -> LocalizationUtils.format("gtocore.recipe.nano_forge_tier", FormattingUtil.formatNumbers(data.getInt("nano_forge_tier"))))
            .setSound(GTSoundEntries.ARC);

    public static final RecipeType FUEL_REFINING_RECIPES = register("fuel_refining", "燃料精炼", MULTIBLOCK)
            .setMaxIOSize(3, 0, 6, 1)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .addDataInfo(TEMPERATURE)
            .addDataInfo(COIL)
            .setUiBuilder(COIL_UI)
            .setSound(GTSoundEntries.ARC);

    public static final RecipeType ATOMIC_ENERGY_EXCITATION_RECIPES = register("atomic_energy_excitation", "原子能激发", MULTIBLOCK)
            .setMaxIOSize(3, 0, 6, 2)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .addDataInfo(TEMPERATURE)
            .addDataInfo(COIL)
            .setUiBuilder(COIL_UI)
            .setSound(GTSoundEntries.ARC);

    public static final RecipeType ISA_MILL_RECIPES = register("isa_mill", "湿法碾磨", MULTIBLOCK)
            .setMaxIOSize(2, 1, 1, 0)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MACERATOR)
            .addDataInfo(data -> LocalizationUtils.format("gtocore.recipe.grindball", I18n.get(data.getInt("grindball") == 2 ? "material.gtceu.aluminium" : "material.gtceu.soapstone")));

    public static final RecipeType FLOTATING_BENEFICIATION_RECIPES = register("flotating_beneficiation", "浮游选矿", MULTIBLOCK)
            .setMaxIOSize(2, 0, 1, 1)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_BATH, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.CHEMICAL);

    public static final RecipeType VACUUM_DRYING_RECIPES = register("vacuum_drying", "真空干燥", MULTIBLOCK)
            .setMaxIOSize(0, 6, 1, 2)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COOLING)
            .addDataInfo(TEMPERATURE)
            .addDataInfo(COIL)
            .setUiBuilder(COIL_UI);

    public static final RecipeType DISSOLUTION_TREATMENT_RECIPES = register("dissolution_treatment", "溶解", MULTIBLOCK)
            .setMaxIOSize(2, 2, 2, 1)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_EXTRACT, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final RecipeType DIGESTION_TREATMENT_RECIPES = register("digestion_treatment", "煮解", MULTIBLOCK)
            .setMaxIOSize(1, 1, 1, 1)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_EXTRACT, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COOLING)
            .addDataInfo(TEMPERATURE)
            .addDataInfo(COIL)
            .setUiBuilder(COIL_UI);

    public static final RecipeType WOOD_DISTILLATION_RECIPES = register("wood_distillation", "集成木质生物质热解", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 1, 1, 15)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.BATH);

    public static final RecipeType DESULFURIZER_RECIPES = register("desulfurizer", "脱硫", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(0, 1, 1, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MIXER);

    public static final RecipeType LIQUEFACTION_FURNACE_RECIPES = register("liquefaction_furnace", "高温液化", MULTIBLOCK)
            .setMaxIOSize(1, 0, 0, 1)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_EXTRACT, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC)
            .addDataInfo(TEMPERATURE)
            .addDataInfo(COIL);

    public static final RecipeType REACTION_FURNACE_RECIPES = register("reaction_furnace", "高温反应", MULTIBLOCK)
            .setMaxIOSize(3, 3, 3, 3)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC)
            .addDataInfo(TEMPERATURE)
            .addDataInfo(COIL)
            .setUiBuilder(COIL_UI);

    public static final RecipeType STEAM_CRACKING_RECIPES = register("steam_cracker", "蒸汽裂化", MULTIBLOCK)
            .setMaxIOSize(1, 0, 1, 1)
            .setEUIO(IO.IN)
            .setSlotOverlay(false, true, GuiTextures.CRACKING_OVERLAY_1)
            .setSlotOverlay(true, true, GuiTextures.CRACKING_OVERLAY_2)
            .setProgressBar(GuiTextures.PROGRESS_BAR_CRACKING, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.FIRE);

    public static final RecipeType CRUSHER_RECIPES = register("crusher", "Ore Crusher", "矿石破碎", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setSlotOverlay(false, false, GuiTextures.CRUSHED_ORE_OVERLAY)
            .setMaxIOSize(1, 3, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MACERATOR);

    public static final RecipeType MOLECULAR_TRANSFORMER_RECIPES = register("molecular_transformer", "物质重组", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 1, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final RecipeType THREE_DIMENSIONAL_PRINTER_RECIPES = register("three_dimensional_printer", "3D Printer", "3D打印", MULTIBLOCK)
            .setMaxIOSize(1, 1, 1, 0)
            .setEUIO(IO.IN)
            .setMaxTooltips(4)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final RecipeType SINTERING_FURNACE_RECIPES = register("sintering_furnace", "烧结炉", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 1, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.FURNACE)
            .addDataInfo(TEMPERATURE)
            .addDataInfo(COIL)
            .setUiBuilder(COIL_UI);

    public static final RecipeType ISOSTATIC_PRESSING_RECIPES = register("isostatic_pressing", "等静压成型", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 1, 1, 0)
            .setProgressBar(GuiTextures.COMPRESSOR_OVERLAY, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COMPRESSOR);

    public static final RecipeType CHEMICAL_VAPOR_DEPOSITION_RECIPES = register("chemical_vapor_deposition", "化学气相沉积", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(3, 1, 3, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.CHEMICAL);

    public static final RecipeType TREE_GROWTH_SIMULATOR_RECIPES = register("tree_growth_simulator", "原木拟生场", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(3, 2, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COOLING);

    public static final RecipeType ELECTRIC_COOKING_RECIPES = register("electric_cooking", "电力烹饪", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(7, 2, 2, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_BATH, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.FURNACE);

    public static final RecipeType ELECTROPLATING_RECIPES = register("electrochemical_deposition", "电化学电镀", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(4, 2, 3, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_BATH, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.CHEMICAL);

    public static final RecipeType DRAWING_RECIPES = register("drawing", "拉丝", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxTooltips(5)
            .setMaxIOSize(2, 1, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_WIREMILL, LEFT_TO_RIGHT)
            .addDataInfo(TEMPERATURE)
            .addDataInfo(COIL)
            .setSound(GTSoundEntries.COMPRESSOR)
            .setUiBuilder((recipe, widgetGroup) -> {
                ItemStack itemStack = new ItemStack(SPOOL.entrySet().stream()
                        .filter(entry -> entry.getValue() == recipe.data.getInt("spool"))
                        .findFirst()
                        .orElseThrow(IllegalArgumentException::new)
                        .getKey());
                widgetGroup.addWidget(new SlotWidget(new CycleItemStackHandler(List.of(List.of(itemStack))), 0,
                        widgetGroup.getSize().width - 50, widgetGroup.getSize().height - 40, false, false));
            });

    public static final RecipeType ROCKET_ASSEMBLER_RECIPES = register("rocket_assembler", "火箭装配", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(9, 1, 3, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ASSEMBLER);

    public static final RecipeType POLYMERIZATION_REACTOR_RECIPES = register("polymerization_reactor", "聚合反应", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 1, 3, 2)
            .setProgressBar(GuiTextures.PROGRESS_BAR_BATH, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.CHEMICAL);

    public static final RecipeType WATER_PURIFICATION_PLANT_RECIPES = register("water_purification_plant", "净化水厂", MULTIBLOCK)
            .noSearch(true)
            .setMaxIOSize(0, 0, 1, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC)
            .addDataInfo(data -> LocalizationUtils.format("tooltip.avaritia.tier", data.getInt("tier")));

    public static final RecipeType PHYSICAL_VAPOR_DEPOSITION_RECIPES = register("physical_vapor_deposition", "物理气相沉积", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(3, 1, 3, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final RecipeType CRYSTALLIZATION_RECIPES = register("crystallization", "结晶", MULTIBLOCK)
            .setMaxIOSize(3, 1, 2, 0)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_CRYSTALLIZATION, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.FURNACE)
            .addDataInfo(TEMPERATURE)
            .addDataInfo(COIL)
            .setUiBuilder(COIL_UI);

    public static final RecipeType BIOCHEMICAL_REACTION_RECIPES = register("biochemical_reaction", "生化反应", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(3, 2, 5, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .addDataInfo(data -> data.contains("radioactivity") ? LocalizationUtils.format("gtocore.recipe.radioactivity", data.getInt("radioactivity")) : "")
            .setSound(GTSoundEntries.COOLING);

    public static final RecipeType BIOCHEMICAL_EXTRACTION_RECIPES = register("biochemical_extraction", "生物提取", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 6, 1, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_EXTRACT, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final RecipeType GAS_COMPRESSOR_RECIPES = register("gas_compressor", "气体压缩", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(0, 0, 1, 1)
            .setProgressBar(GuiTextures.COMPRESSOR_OVERLAY, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COMPRESSOR);

    public static final RecipeType RARITY_FORGE_RECIPES = register("rarity_forge", "珍宝锻炉", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(3, 1, 0, 0)
            .setProgressBar(GuiTextures.COMPRESSOR_OVERLAY, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    // TODO 添加用途
    public static final RecipeType PLASMA_CENTRIFUGE_RECIPES = register("plasma_centrifuge", "等离子体离心", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 0, 1, 9)
            .setProgressBar(GuiTextures.PROGRESS_BAR_EXTRACT, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final RecipeType PLASMA_EXTRACTION_RECIPES = register("plasma_extraction", "等离子体萃取", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(0, 0, 2, 2)
            .setProgressBar(GuiTextures.PROGRESS_BAR_EXTRACT, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final RecipeType CRYSTAL_SCAN_RECIPES = register("crystal_scan", "晶片扫描", ELECTRIC)
            .setMaxIOSize(3, 1, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setMaxTooltips(4)
            .setSound(GTSoundEntries.COMPUTATION);

    public static final RecipeType DATA_ANALYSIS_RECIPES = register("data_analysis", "数据分析", ELECTRIC)
            .setMaxIOSize(3, 6, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setMaxTooltips(4)
            .setSound(GTSoundEntries.COMPUTATION);

    public static final RecipeType DATA_INTEGRATION_RECIPES = register("data_integration", "数据统合", ELECTRIC)
            .setMaxIOSize(13, 2, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setMaxTooltips(4)
            .setSound(GTSoundEntries.COMPUTATION);

    public static final RecipeType RECIPES_DATA_GENERATE_RECIPES = register("recipes_data_generate", "配方数据生成", ELECTRIC)
            .setMaxIOSize(11, 1, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setMaxTooltips(4)
            .setSound(GTSoundEntries.COMPUTATION);

    //////////////////////////////////////
    // ********** Magic **********//
    //////////////////////////////////////
    public static final RecipeType ALCHEMY_CAULDRON_RECIPES = register("alchemy_cauldron", "炼金锅", MAGIC)
            .setMANAIO(IO.IN)
            .setMaxIOSize(6, 6, 3, 3)
            .setProgressBar(GuiTextures.PROGRESS_BAR_BATH, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COOLING)
            .addDataInfo(data -> {
                int temperature = data.getInt("temperature");
                if (temperature > 0) {
                    return I18n.get("gtceu.multiblock.hpca.temperature", temperature);
                }
                return "";
            });

    public static final RecipeType MANA_HEATER_RECIPES = register("mana_heater", "魔力加热器", MAGIC)
            .setMaxIOSize(0, 0, 1, 0)
            .setSound(GTSoundEntries.FURNACE);

    public static final RecipeType MANA_INFUSER_RECIPES = register("mana_infuser", "魔力灌注", MAGIC)
            .setMANAIO(IO.IN)
            .setMaxIOSize(3, 1, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_BATH, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.BATH);

    public static final RecipeType MANA_CONDENSER_RECIPES = register("mana_condenser", "魔力凝聚", MAGIC)
            .setMANAIO(IO.IN)
            .setMaxIOSize(15, 1, 3, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.FIRE);

    public static final RecipeType ELF_EXCHANGE_RECIPES = register("elf_exchange", "ELF Exchange", "精灵交易", MAGIC)
            .setMANAIO(IO.IN)
            .setMaxIOSize(1, 1, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.REPLICATOR);

    public static final RecipeType INDUSTRIAL_ALTAR_RECIPES = register("industrial_altar", "工业祭坛", MAGIC)
            .setMANAIO(IO.IN)
            .setMaxIOSize(18, 1, 3, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final RecipeType RUNE_ENGRAVING_RECIPES = register("rune_engraving", "符文铭刻", MAGIC)
            .setMANAIO(IO.IN)
            .setMaxIOSize(6, 1, 3, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.REPLICATOR);

    public static final RecipeType MANA_GARDEN_RECIPES = register("mana_garden", "魔力花园", MAGIC)
            .setEUIO(IO.IN)
            .setMANAIO(IO.OUT)
            .setMaxIOSize(2, 2, 2, 2)
            .setMaxTooltips(5)
            .setProgressBar(GuiTextures.PROGRESS_BAR_BATH, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final RecipeType MANA_GARDEN_FUEL = register("mana_garden_fuel", "魔力花园：燃料", MAGIC)
            .setEUIO(IO.IN)
            .setMANAIO(IO.OUT)
            .setMaxIOSize(2, 0, 1, 0)
            .setSlotOverlay(false, true, true, GuiTextures.FURNACE_OVERLAY_2)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, LEFT_TO_RIGHT)
            .setMaxTooltips(5)
            .setSound(GTSoundEntries.FIRE);

    public static final RecipeType INFUSER_CORE_RECIPES = register("infuser_core", "灌注核心", MAGIC)
            .setMANAIO(IO.IN)
            .setMaxIOSize(18, 1, 3, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_BATH, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.BATH);
    //////////////////////////////////////
    // ********** Combined **********//
    //////////////////////////////////////
    public static final RecipeType CHEMICAL = CombinedRecipeType.register("chemical", LARGE_CHEMICAL_RECIPES, CHEMICAL_RECIPES).setMaxIOSize(3, 3, 5, 4).setEUIO(IO.IN).setSound(GTSoundEntries.CHEMICAL);

    public static final RecipeType CHEMICAL_ENERGY_DEVOURER_FUELS = CombinedRecipeType.register("chemical_energy_devourer", COMBUSTION_GENERATOR_FUELS, GAS_TURBINE_FUELS, ROCKET_ENGINE_FUELS).setSound(GTSoundEntries.COMBUSTION);

    public static final RecipeType VAPOR_DEPOSITION = CombinedRecipeType.register("vapor_deposition", CHEMICAL_VAPOR_DEPOSITION_RECIPES, PHYSICAL_VAPOR_DEPOSITION_RECIPES).setMaxIOSize(3, 1, 3, 1).setEUIO(IO.IN).setSound(GTSoundEntries.ARC);

    public static final RecipeType SUPRACHRONAL_ASSEMBLY_LINE = CombinedRecipeType.register("suprachronal_assembly", SUPRACHRONAL_ASSEMBLY_LINE_RECIPES, ASSEMBLY_LINE_RECIPES, CIRCUIT_ASSEMBLY_LINE_RECIPES).setMaxIOSize(16, 1, 4, 0).setEUIO(IO.IN).setSound(GTSoundEntries.ASSEMBLER);

    public static final RecipeType INTEGRATED_ASSEMBLER = CombinedRecipeType.register("integrated_assembler", ASSEMBLER_RECIPES, LAMINATOR_RECIPES).setMaxIOSize(9, 1, 1, 0).setEUIO(IO.IN).setSound(GTSoundEntries.ASSEMBLER);

    public static final RecipeType HEAVY_ROLLING = CombinedRecipeType.register("heavy_rolling", ROLLING_RECIPES, CLUSTER_RECIPES).setMaxIOSize(2, 1, 0, 0).setEUIO(IO.IN).setSound(GTSoundEntries.MOTOR);

    public static final RecipeType LARGE_CHEMICAL_PLANT = CombinedRecipeType.register("large_chemical_plant", LARGE_CHEMICAL_RECIPES, CHEMICAL_RECIPES, POLYMERIZATION_REACTOR_RECIPES).setMaxIOSize(3, 3, 5, 4).setEUIO(IO.IN).setSound(GTSoundEntries.CHEMICAL);

    public static final RecipeType EXTREME_COMPRESSOR = CombinedRecipeType.register("extreme_compressor", COMPRESSOR_RECIPES, GAS_COMPRESSOR_RECIPES).setMaxIOSize(1, 1, 1, 1).setEUIO(IO.IN).setSound(GTSoundEntries.COMPRESSOR);
}
