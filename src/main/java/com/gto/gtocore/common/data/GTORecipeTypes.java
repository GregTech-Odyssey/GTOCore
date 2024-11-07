package com.gto.gtocore.common.data;

import com.gto.gtocore.api.capability.recipe.ManaRecipeCapability;
import com.gto.gtocore.common.recipe.RecipeModify;
import com.gto.gtocore.config.GTOConfigHolder;
import com.gto.gtocore.data.recipe.GenerateDisassembly;
import com.gto.gtocore.utils.Registries;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.block.ICoilType;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.sound.ExistingSoundEntry;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.common.data.GTSoundEntries;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.ResearchManager;

import com.lowdragmc.lowdraglib.gui.texture.ProgressTexture;
import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import com.lowdragmc.lowdraglib.utils.CycleItemStackHandler;
import com.lowdragmc.lowdraglib.utils.LocalizationUtils;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;

import java.util.*;

import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;
import static com.lowdragmc.lowdraglib.gui.texture.ProgressTexture.FillDirection.LEFT_TO_RIGHT;

public class GTORecipeTypes {

    public static final String MAGIC = "magic";

    public static final Set<String> ALL_RECIPE_TYPE = new HashSet<>();

    public static final GTRecipeType RADIATION_HATCH_RECIPES = registerRecipeType("radiation_hatch", MULTIBLOCK)
            .setMaxIOSize(1, 0, 0, 0)
            .setEUIO(IO.NONE)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .addDataInfo(data -> LocalizationUtils.format("gtocore.recipe.radioactivity", data.getInt("radioactivity")));

    public static final GTRecipeType PRIMITIVE_VOID_ORE_RECIPES = GTOConfigHolder.INSTANCE.enablePrimitiveVoidOre ?
            registerRecipeType("primitive_void_ore", MULTIBLOCK)
                    .setMaxIOSize(0, 0, 1, 0)
                    .setMaxTooltips(1)
                    .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, LEFT_TO_RIGHT)
                    .setSound(GTSoundEntries.MINER) :
            null;

    public static final GTRecipeType LIGHTNING_PROCESSOR_RECIPES = registerRecipeType("lightning_processor", ELECTRIC)
            .setMaxIOSize(6, 1, 6, 1)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final GTRecipeType DEHYDRATOR_RECIPES = registerRecipeType("dehydrator", ELECTRIC)
            .setMaxIOSize(2, 6, 2, 2)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_EXTRACT, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public final static GTRecipeType UNPACKER_RECIPES = registerRecipeType("unpacker", ELECTRIC)
            .setMaxIOSize(2, 2, 0, 0)
            .setEUIO(IO.IN)
            .prepareBuilder(recipeBuilder -> recipeBuilder.EUt(12).duration(10))
            .setSlotOverlay(false, false, true, GuiTextures.BOX_OVERLAY)
            .setSlotOverlay(true, false, GuiTextures.BOXED_OVERLAY)
            .setProgressBar(GuiTextures.PROGRESS_BAR_UNPACKER, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ASSEMBLER);

    public final static GTRecipeType CLUSTER_RECIPES = registerRecipeType("cluster", ELECTRIC)
            .setMaxIOSize(1, 1, 0, 0)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MOTOR);

    public final static GTRecipeType ROLLING_RECIPES = registerRecipeType("rolling", ELECTRIC)
            .setMaxIOSize(2, 1, 0, 0)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_BENDING, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MOTOR);

    public final static GTRecipeType LAMINATOR_RECIPES = registerRecipeType("laminator", ELECTRIC)
            .setMaxIOSize(3, 1, 1, 0)
            .setEUIO(IO.IN)
            .setSlotOverlay(false, false, GuiTextures.CIRCUIT_OVERLAY)
            .setProgressBar(GuiTextures.PROGRESS_BAR_CIRCUIT, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.BATH);

    public final static GTRecipeType LOOM_RECIPES = registerRecipeType("loom", ELECTRIC)
            .setMaxIOSize(2, 1, 0, 0)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_WIREMILL, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MOTOR);

    public final static GTRecipeType LASER_WELDER_RECIPES = registerRecipeType("laser_welder", ELECTRIC)
            .setMaxIOSize(3, 1, 0, 0)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_WIREMILL, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final GTRecipeType WORLD_DATA_SCANNER_RECIPES = registerRecipeType("world_data_scanner", ELECTRIC)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 1, 2, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.SCIENCE);

    public static final GTRecipeType VACUUM_PUMP_RECIPES = registerRecipeType("vacuum_pump", ELECTRIC)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 0, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.TURBINE);

    public final static GTRecipeType THERMAL_GENERATOR_FUELS = registerRecipeType("thermal_generator", GENERATOR)
            .setMaxIOSize(1, 0, 1, 0).setEUIO(IO.OUT)
            .setSlotOverlay(false, true, true, GuiTextures.FURNACE_OVERLAY_2)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COMBUSTION);

    public final static GTRecipeType SEMI_FLUID_GENERATOR_FUELS = registerRecipeType("semi_fluid_generator", GENERATOR)
            .setMaxIOSize(0, 0, 1, 0).setEUIO(IO.OUT)
            .setSlotOverlay(false, true, true, GuiTextures.FURNACE_OVERLAY_2)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COMBUSTION);

    public final static GTRecipeType SUPERCRITICAL_STEAM_TURBINE_FUELS = registerRecipeType("supercritical_steam_turbine", GENERATOR)
            .setMaxIOSize(0, 0, 1, 1)
            .setEUIO(IO.OUT)
            .setSlotOverlay(false, true, true, GuiTextures.CENTRIFUGE_OVERLAY)
            .setProgressBar(GuiTextures.PROGRESS_BAR_GAS_COLLECTOR, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.TURBINE);

    public final static GTRecipeType ROCKET_ENGINE_FUELS = registerRecipeType("rocket_engine", GENERATOR)
            .setEUIO(IO.OUT)
            .setMaxIOSize(0, 0, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.JET_ENGINE);

    public final static GTRecipeType NAQUADAH_REACTOR = registerRecipeType("naquadah_reactor", GENERATOR)
            .setEUIO(IO.OUT)
            .setMaxIOSize(0, 0, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COMBUSTION);

    public final static GTRecipeType ELECTRIC_IMPLOSION_COMPRESSOR_RECIPES = registerRecipeType("electric_implosion_compressor", MULTIBLOCK)
            .setMaxIOSize(2, 1, 0, 0).setEUIO(IO.IN)
            .prepareBuilder(recipeBuilder -> recipeBuilder.duration(1).EUt(GTValues.VA[GTValues.UV]))
            .setSlotOverlay(false, false, true, GuiTextures.IMPLOSION_OVERLAY_1)
            .setSlotOverlay(false, false, false, GuiTextures.IMPLOSION_OVERLAY_2)
            .setSlotOverlay(true, false, true, GuiTextures.DUST_OVERLAY)
            .setSound(new ExistingSoundEntry(SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS));

    public static final GTRecipeType DISASSEMBLY_RECIPES = registerRecipeType("disassembly", MULTIBLOCK)
            .setMaxIOSize(1, 16, 0, 4)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ASSEMBLER);

    public static final GTRecipeType NEUTRON_ACTIVATOR_RECIPES = registerRecipeType("neutron_activator", MULTIBLOCK)
            .setMaxIOSize(6, 3, 1, 1)
            .setSound(GTSoundEntries.COOLING)
            .addDataInfo(data -> LocalizationUtils.format("gtocore.recipe.ev_min", data.getInt("ev_min")))
            .addDataInfo(data -> LocalizationUtils.format("gtocore.recipe.ev_max", data.getInt("ev_max")))
            .addDataInfo(data -> LocalizationUtils.format("gtocore.recipe.evt", data.getInt("evt")));

    public static final GTRecipeType HEAT_EXCHANGER_RECIPES = registerRecipeType("heat_exchanger", MULTIBLOCK)
            .setMaxIOSize(0, 0, 2, 2)
            .setProgressBar(GuiTextures.PROGRESS_BAR_MIXER, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
            .setMaxTooltips(1)
            .setSound(GTSoundEntries.COOLING);

    public static final GTRecipeType ELEMENT_COPYING_RECIPES = registerRecipeType("element_copying", MULTIBLOCK)
            .setMaxIOSize(1, 1, 2, 1)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final GTRecipeType INTEGRATED_ORE_PROCESSOR = registerRecipeType("integrated_ore_processor", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 9, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MACERATOR);

    public static final GTRecipeType FISSION_REACTOR_RECIPES = registerRecipeType("fission_reactor", MULTIBLOCK)
            .setMaxIOSize(1, 1, 0, 0)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC)
            .addDataInfo(data -> LocalizationUtils.format("gtocore.recipe.frheat", FormattingUtil.formatNumbers(data.getInt("FRheat"))));

    public static final GTRecipeType SPACE_ELEVATOR_RECIPES = registerRecipeType("space_elevator", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 0, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setXEIVisible(false);

    public static final GTRecipeType STELLAR_FORGE_RECIPES = registerRecipeType("stellar_forge", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(3, 2, 9, 2)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARC_FURNACE, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC)
            .addDataInfo(data -> {
                String tierString = switch (data.getInt("stellar_containment_tier")) {
                    case 3 -> I18n.get("gtocore.tier.ultimate");
                    case 2 -> I18n.get("gtocore.tier.advanced");
                    default -> I18n.get("gtocore.tier.base");
                };
                return LocalizationUtils.format("gtocore.tier.stellar_containment_tier", tierString);
            });

    public static final GTRecipeType COMPONENT_ASSEMBLY_LINE_RECIPES = registerRecipeType("component_assembly_line", MULTIBLOCK)
            .setMaxIOSize(9, 1, 9, 0)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .addDataInfo(data -> LocalizationUtils.format("gtocore.tier.component_assembly_line_casing_tier", GTValues.VN[data.getInt("component_assembly_line_casing_tier")]))
            .setSound(GTSoundEntries.ASSEMBLER);

    public static final GTRecipeType SLAUGHTERHOUSE_RECIPES = registerRecipeType("slaughterhouse", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 0, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MACERATOR)
            .setXEIVisible(false);

    public static final GTRecipeType GREENHOUSE_RECIPES = registerRecipeType("greenhouse", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(3, 3, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_BATH, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COOLING);

    public static final GTRecipeType DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES = registerRecipeType("dimensionally_transcendent_plasma_forge", MULTIBLOCK)
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

    public static final GTRecipeType PLASMA_CONDENSER_RECIPES = registerRecipeType("plasma_condenser", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 2, 2, 2)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COOLING);

    public static final GTRecipeType RARE_EARTH_CENTRIFUGAL_RECIPES = registerRecipeType("rare_earth_centrifugal", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 17, 1, 1)
            .setProgressBar(GuiTextures.CENTRIFUGE_OVERLAY, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.CENTRIFUGE);

    public static final GTRecipeType MAGIC_MANUFACTURER_RECIPES = registerRecipeType("magic_manufacturer", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 0, 0, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_MASS_FAB, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COOLING);

    public static final GTRecipeType SPS_CRAFTING_RECIPES = registerRecipeType("sps_crafting", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(3, 1, 3, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.SCIENCE);

    public static final GTRecipeType MATTER_FABRICATOR_RECIPES = registerRecipeType("matter_fabricator", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 1, 0, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COOLING);

    public static final GTRecipeType VOID_MINER_RECIPES = registerRecipeType("void_miner", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 4, 2, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MINER);

    public static final GTRecipeType LARGE_VOID_MINER_RECIPES = registerRecipeType("large_void_miner", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 4, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MINER);

    public static final GTRecipeType RANDOM_ORE_RECIPES = registerRecipeType("random_ore", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(0, 216, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MINER);

    public static final GTRecipeType VOID_FLUID_DRILLING_RIG_RECIPES = registerRecipeType("void_fluid_drilling_rig", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 0, 0, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.CHEMICAL);

    public static final GTRecipeType ANNIHILATE_GENERATOR_RECIPES = registerRecipeType("annihilate_generator", MULTIBLOCK)
            .setEUIO(IO.OUT)
            .setMaxIOSize(1, 1, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final GTRecipeType HYPER_REACTOR_RECIPES = registerRecipeType("hyper_reactor", MULTIBLOCK)
            .setEUIO(IO.OUT)
            .setMaxIOSize(0, 0, 2, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final GTRecipeType ADVANCED_HYPER_REACTOR_RECIPES = registerRecipeType("advanced_hyper_reactor", MULTIBLOCK)
            .setEUIO(IO.OUT)
            .setMaxIOSize(0, 0, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final GTRecipeType LARGE_NAQUADAH_REACTOR_RECIPES = registerRecipeType("large_naquadah_reactor", MULTIBLOCK)
            .setEUIO(IO.OUT)
            .setMaxIOSize(0, 0, 2, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COMBUSTION);

    public static final GTRecipeType COSMOS_SIMULATION_RECIPES = registerRecipeType("cosmos_simulation", MULTIBLOCK)
            .setMaxIOSize(1, 120, 1, 18)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.SCIENCE);

    public static final GTRecipeType SPACE_PROBE_SURFACE_RECEPTION_RECIPES = registerRecipeType("space_probe_surface_reception", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxTooltips(4)
            .setMaxIOSize(2, 0, 0, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.SCIENCE);

    public static final GTRecipeType DECAY_HASTENER_RECIPES = registerRecipeType("decay_hastener", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(0, 1, 1, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final GTRecipeType LARGE_RECYCLER_RECIPES = registerRecipeType("large_recycler", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 1, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_RECYCLER, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MACERATOR);

    public static final GTRecipeType MASS_FABRICATOR_RECIPES = registerRecipeType("mass_fabricator", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 0, 1, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_REPLICATOR, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final GTRecipeType CIRCUIT_ASSEMBLY_LINE_RECIPES = registerRecipeType("circuit_assembly_line", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(16, 1, 4, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ASSEMBLER)
            .onRecipeBuild(GenerateDisassembly::generateDisassembly);

    public static final GTRecipeType SUPRACHRONAL_ASSEMBLY_LINE_RECIPES = registerRecipeType("suprachronal_assembly_line", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxTooltips(4)
            .setMaxIOSize(16, 1, 4, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ASSEMBLER)
            .setHasResearchSlot(true)
            .onRecipeBuild((recipeBuilder, provider) -> {
                ResearchManager.createDefaultResearchRecipe(recipeBuilder, provider);
                GenerateDisassembly.generateDisassembly(recipeBuilder, provider);
            });

    public static final GTRecipeType PRECISION_ASSEMBLER_RECIPES = registerRecipeType("precision_assembler", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxTooltips(4)
            .setMaxIOSize(4, 1, 4, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ASSEMBLER)
            .onRecipeBuild(GenerateDisassembly::generateDisassembly);

    public static final GTRecipeType ASSEMBLER_MODULE_RECIPES = registerRecipeType("assembler_module", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(16, 1, 4, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ASSEMBLER)
            .onRecipeBuild(GenerateDisassembly::generateDisassembly)
            .addDataInfo(data -> LocalizationUtils.format("gtocore.tier.power_module_tier", FormattingUtil.formatNumbers(data.getInt("power_module_tier"))));

    public static final GTRecipeType MINER_MODULE_RECIPES = registerRecipeType("miner_module", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 6, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MINER);

    public static final GTRecipeType DRILLING_MODULE_RECIPES = registerRecipeType("drilling_module", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 0, 1, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MINER);

    public static final GTRecipeType FISHING_GROUND_RECIPES = registerRecipeType("fishing_ground", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 24, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MINER);

    public static final GTRecipeType BLOCK_CONVERSIONRECIPES = registerRecipeType("block_conversion", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 0, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC)
            .setXEIVisible(false);

    public static final GTRecipeType INCUBATOR_RECIPES = registerRecipeType("incubator", MULTIBLOCK)
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

    public static final GTRecipeType PCB_FACTORY_RECIPES = registerRecipeType("pcb_factory", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 1, 2, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.CHEMICAL);

    public static final GTRecipeType LAVA_FURNACE_RECIPES = registerRecipeType("lava_furnace", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 0, 0, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARC_FURNACE, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.FURNACE);

    public static final GTRecipeType LARGE_GAS_COLLECTOR_RECIPES = registerRecipeType("large_gas_collector", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 0, 0, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COOLING);

    public static final GTRecipeType AGGREGATION_DEVICE_RECIPES = registerRecipeType("aggregation_device", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(9, 1, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final GTRecipeType SUPER_PARTICLE_COLLIDER_RECIPES = registerRecipeType("super_particle_collider", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(0, 0, 2, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTOSoundEntries.FUSIONLOOP);

    public static final GTRecipeType DIMENSIONAL_FOCUS_ENGRAVING_ARRAY_RECIPES = registerRecipeType("dimensional_focus_engraving_array", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxTooltips(4)
            .setMaxIOSize(2, 1, 2, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC)
            .onRecipeBuild(ResearchManager::createDefaultResearchRecipe);

    public static final GTRecipeType PRECISION_LASER_ENGRAVER_RECIPES = registerRecipeType("precision_laser_engraver", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxTooltips(4)
            .setMaxIOSize(9, 1, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final GTRecipeType DIMENSIONALLY_TRANSCENDENT_MIXER_RECIPES = registerRecipeType("dimensionally_transcendent_mixer", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(9, 1, 6, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_BATH, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MIXER);

    public static final GTRecipeType NEUTRON_COMPRESSOR_RECIPES = registerRecipeType("neutron_compressor", ELECTRIC)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 1, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_COMPRESS, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COMPRESSOR);

    public static final GTRecipeType QUANTUM_FORCE_TRANSFORMER_RECIPES = registerRecipeType("quantum_force_transformer", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(18, 1, 3, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.SCIENCE);

    public static final GTRecipeType DRAGON_EGG_COPIER_RECIPES = registerRecipeType("dragon_egg_copier", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 2, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COOLING);

    public static final GTRecipeType DOOR_OF_CREATE_RECIPES = registerRecipeType("door_of_create", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxTooltips(4)
            .setMaxIOSize(1, 0, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setXEIVisible(false);

    public static final GTRecipeType BEDROCK_DRILLING_RIG_RECIPES = registerRecipeType("bedrock_drilling_rig", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 1, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MACERATOR);

    public static final GTRecipeType CREATE_AGGREGATION_RECIPES = registerRecipeType("create_aggregation", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 0, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.SCIENCE)
            .setXEIVisible(false);

    public static final GTRecipeType GRAVITATION_SHOCKBURST_RECIPES = registerRecipeType("gravitation_shockburst", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 1, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MACERATOR)
            .onRecipeBuild(ResearchManager::createDefaultResearchRecipe);

    public static final GTRecipeType ULTIMATE_MATERIAL_FORGE_RECIPES = registerRecipeType("ultimate_material_forge", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 2, 2, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MACERATOR);

    public static final GTRecipeType DYSON_SPHERE_RECIPES = registerRecipeType("dyson_sphere", MULTIBLOCK)
            .setEUIO(IO.BOTH)
            .setMaxTooltips(4)
            .setMaxIOSize(1, 0, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.SCIENCE);

    public static final GTRecipeType PETROCHEMICAL_PLANT_RECIPES = registerRecipeType("petrochemical_plant", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(0, 0, 2, 12)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COOLING);

    public static final GTRecipeType WEATHER_CONTROL_RECIPES = registerRecipeType("weather_control", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 0, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.SCIENCE)
            .setXEIVisible(false);

    public static final GTRecipeType DISTORT_RECIPES = registerRecipeType("distort", MULTIBLOCK)
            .setMaxIOSize(9, 9, 9, 9)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .addDataInfo(data -> {
                int temp = data.getInt("ebf_temp");
                return LocalizationUtils.format("gtceu.recipe.temperature", FormattingUtil.formatNumbers(temp));
            })
            .addDataInfo(data -> {
                int temp = data.getInt("ebf_temp");
                ICoilType requiredCoil = ICoilType.getMinRequiredType(temp);

                if (requiredCoil != null && requiredCoil.getMaterial() != null) {
                    return LocalizationUtils.format("gtceu.recipe.coil.tier",
                            I18n.get(requiredCoil.getMaterial().getUnlocalizedName()));
                }
                return "";
            })
            .setUiBuilder((recipe, widgetGroup) -> {
                int temp = recipe.data.getInt("ebf_temp");
                List<List<ItemStack>> items = new ArrayList<>();
                items.add(GTCEuAPI.HEATING_COILS.entrySet().stream()
                        .filter(coil -> coil.getKey().getCoilTemperature() >= temp)
                        .map(coil -> new ItemStack(coil.getValue().get())).toList());
                widgetGroup.addWidget(new SlotWidget(new CycleItemStackHandler(items), 0,
                        widgetGroup.getSize().width - 25, widgetGroup.getSize().height - 40, false, false));
            })
            .setMaxTooltips(4)
            .setSound(GTSoundEntries.ARC);

    public static final GTRecipeType NANO_FORGE_RECIPES = registerRecipeType("nano_forge", MULTIBLOCK)
            .setMaxIOSize(6, 1, 3, 0)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .addDataInfo(data -> LocalizationUtils.format("gtocore.recipe.nano_forge_tier", FormattingUtil.formatNumbers(data.getInt("nano_forge_tier"))))
            .setSound(GTSoundEntries.SCIENCE);

    public static final GTRecipeType FUEL_REFINING_RECIPES = registerRecipeType("fuel_refining", MULTIBLOCK)
            .setMaxIOSize(3, 0, 6, 1)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .addDataInfo(data -> {
                int temp = data.getInt("ebf_temp");
                return LocalizationUtils.format("gtceu.recipe.temperature", FormattingUtil.formatNumbers(temp));
            })
            .addDataInfo(data -> {
                int temp = data.getInt("ebf_temp");
                ICoilType requiredCoil = ICoilType.getMinRequiredType(temp);

                if (requiredCoil != null && requiredCoil.getMaterial() != null) {
                    return LocalizationUtils.format("gtceu.recipe.coil.tier",
                            I18n.get(requiredCoil.getMaterial().getUnlocalizedName()));
                }
                return "";
            })
            .setUiBuilder((recipe, widgetGroup) -> {
                int temp = recipe.data.getInt("ebf_temp");
                List<List<ItemStack>> items = new ArrayList<>();
                items.add(GTCEuAPI.HEATING_COILS.entrySet().stream()
                        .filter(coil -> coil.getKey().getCoilTemperature() >= temp)
                        .map(coil -> new ItemStack(coil.getValue().get())).toList());
                widgetGroup.addWidget(new SlotWidget(new CycleItemStackHandler(items), 0,
                        widgetGroup.getSize().width - 25, widgetGroup.getSize().height - 40, false, false));
            })
            .setSound(GTSoundEntries.ARC);

    public static final GTRecipeType ATOMIC_ENERGY_EXCITATION_RECIPES = registerRecipeType("atomic_energy_excitation", MULTIBLOCK)
            .setMaxIOSize(3, 0, 6, 2)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .addDataInfo(data -> {
                int temp = data.getInt("ebf_temp");
                return LocalizationUtils.format("gtceu.recipe.temperature", FormattingUtil.formatNumbers(temp));
            })
            .addDataInfo(data -> {
                int temp = data.getInt("ebf_temp");
                ICoilType requiredCoil = ICoilType.getMinRequiredType(temp);

                if (requiredCoil != null && requiredCoil.getMaterial() != null) {
                    return LocalizationUtils.format("gtceu.recipe.coil.tier",
                            I18n.get(requiredCoil.getMaterial().getUnlocalizedName()));
                }
                return "";
            })
            .setUiBuilder((recipe, widgetGroup) -> {
                int temp = recipe.data.getInt("ebf_temp");
                List<List<ItemStack>> items = new ArrayList<>();
                items.add(GTCEuAPI.HEATING_COILS.entrySet().stream()
                        .filter(coil -> coil.getKey().getCoilTemperature() >= temp)
                        .map(coil -> new ItemStack(coil.getValue().get())).toList());
                widgetGroup.addWidget(new SlotWidget(new CycleItemStackHandler(items), 0,
                        widgetGroup.getSize().width - 25, widgetGroup.getSize().height - 40, false, false));
            })
            .setSound(GTSoundEntries.ARC);

    public static final GTRecipeType ISA_MILL_RECIPES = registerRecipeType("isa_mill", MULTIBLOCK)
            .setMaxIOSize(2, 1, 1, 0)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MACERATOR)
            .addDataInfo(data -> LocalizationUtils.format("gtocore.recipe.grindball", I18n.get(data.getInt("grindball") == 2 ? "material.gtceu.aluminium" : "material.gtceu.soapstone")));

    public static final GTRecipeType FLOTATING_BENEFICIATION_RECIPES = registerRecipeType("flotating_beneficiation", MULTIBLOCK)
            .setMaxIOSize(2, 0, 1, 1)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_BATH, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.CHEMICAL);

    public static final GTRecipeType VACUUM_DRYING_RECIPES = registerRecipeType("vacuum_drying", MULTIBLOCK)
            .setMaxIOSize(0, 6, 1, 2)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COOLING)
            .addDataInfo(data -> {
                int temp = data.getInt("ebf_temp");
                return LocalizationUtils.format("gtceu.recipe.temperature", FormattingUtil.formatNumbers(temp));
            })
            .addDataInfo(data -> {
                int temp = data.getInt("ebf_temp");
                ICoilType requiredCoil = ICoilType.getMinRequiredType(temp);

                if (requiredCoil != null && requiredCoil.getMaterial() != null) {
                    return LocalizationUtils.format("gtceu.recipe.coil.tier",
                            I18n.get(requiredCoil.getMaterial().getUnlocalizedName()));
                }
                return "";
            })
            .setUiBuilder((recipe, widgetGroup) -> {
                int temp = recipe.data.getInt("ebf_temp");
                List<List<ItemStack>> items = new ArrayList<>();
                items.add(GTCEuAPI.HEATING_COILS.entrySet().stream()
                        .filter(coil -> coil.getKey().getCoilTemperature() >= temp)
                        .map(coil -> new ItemStack(coil.getValue().get())).toList());
                widgetGroup.addWidget(new SlotWidget(new CycleItemStackHandler(items), 0,
                        widgetGroup.getSize().width - 25, widgetGroup.getSize().height - 40, false, false));
            });

    public static final GTRecipeType DISSOLUTION_TREATMENT = registerRecipeType("dissolution_treatment", MULTIBLOCK)
            .setMaxIOSize(2, 2, 2, 1)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_EXTRACT, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final GTRecipeType DIGESTION_TREATMENT = registerRecipeType("digestion_treatment", MULTIBLOCK)
            .setMaxIOSize(1, 1, 1, 1)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_EXTRACT, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COOLING)
            .addDataInfo(data -> {
                int temp = data.getInt("ebf_temp");
                return LocalizationUtils.format("gtceu.recipe.temperature", FormattingUtil.formatNumbers(temp));
            })
            .addDataInfo(data -> {
                int temp = data.getInt("ebf_temp");
                ICoilType requiredCoil = ICoilType.getMinRequiredType(temp);

                if (requiredCoil != null && requiredCoil.getMaterial() != null) {
                    return LocalizationUtils.format("gtceu.recipe.coil.tier",
                            I18n.get(requiredCoil.getMaterial().getUnlocalizedName()));
                }
                return "";
            })
            .setUiBuilder((recipe, widgetGroup) -> {
                int temp = recipe.data.getInt("ebf_temp");
                List<List<ItemStack>> items = new ArrayList<>();
                items.add(GTCEuAPI.HEATING_COILS.entrySet().stream()
                        .filter(coil -> coil.getKey().getCoilTemperature() >= temp)
                        .map(coil -> new ItemStack(coil.getValue().get())).toList());
                widgetGroup.addWidget(new SlotWidget(new CycleItemStackHandler(items), 0,
                        widgetGroup.getSize().width - 50, widgetGroup.getSize().height - 40, false, false));
            });

    public static final GTRecipeType WOOD_DISTILLATION_RECIPES = registerRecipeType("wood_distillation", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 1, 1, 15)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.BATH);

    public static final GTRecipeType DESULFURIZER_RECIPES = registerRecipeType("desulfurizer", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(0, 1, 1, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MIXER);

    public static final GTRecipeType LIQUEFACTION_FURNACE_RECIPES = registerRecipeType("liquefaction_furnace", MULTIBLOCK)
            .setMaxIOSize(1, 0, 0, 1)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_EXTRACT, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC)
            .addDataInfo(data -> {
                int temp = data.getInt("ebf_temp");
                return LocalizationUtils.format("gtceu.recipe.temperature", FormattingUtil.formatNumbers(temp));
            })
            .addDataInfo(data -> {
                int temp = data.getInt("ebf_temp");
                ICoilType requiredCoil = ICoilType.getMinRequiredType(temp);

                if (requiredCoil != null && requiredCoil.getMaterial() != null) {
                    return LocalizationUtils.format("gtceu.recipe.coil.tier",
                            I18n.get(requiredCoil.getMaterial().getUnlocalizedName()));
                }
                return "";
            })
            .setUiBuilder((recipe, widgetGroup) -> {
                int temp = recipe.data.getInt("ebf_temp");
                List<List<ItemStack>> items = new ArrayList<>();
                items.add(GTCEuAPI.HEATING_COILS.entrySet().stream()
                        .filter(coil -> coil.getKey().getCoilTemperature() >= temp)
                        .map(coil -> new ItemStack(coil.getValue().get())).toList());
                widgetGroup.addWidget(new SlotWidget(new CycleItemStackHandler(items), 0,
                        widgetGroup.getSize().width - 50, widgetGroup.getSize().height - 40, false, false));
            });

    public static final GTRecipeType REACTION_FURNACE_RECIPES = registerRecipeType("reaction_furnace", MULTIBLOCK)
            .setMaxIOSize(3, 3, 3, 3)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC)
            .addDataInfo(data -> {
                int temp = data.getInt("ebf_temp");
                return LocalizationUtils.format("gtceu.recipe.temperature", FormattingUtil.formatNumbers(temp));
            })
            .addDataInfo(data -> {
                int temp = data.getInt("ebf_temp");
                ICoilType requiredCoil = ICoilType.getMinRequiredType(temp);

                if (requiredCoil != null && requiredCoil.getMaterial() != null) {
                    return LocalizationUtils.format("gtceu.recipe.coil.tier",
                            I18n.get(requiredCoil.getMaterial().getUnlocalizedName()));
                }
                return "";
            })
            .setUiBuilder((recipe, widgetGroup) -> {
                int temp = recipe.data.getInt("ebf_temp");
                List<List<ItemStack>> items = new ArrayList<>();
                items.add(GTCEuAPI.HEATING_COILS.entrySet().stream()
                        .filter(coil -> coil.getKey().getCoilTemperature() >= temp)
                        .map(coil -> new ItemStack(coil.getValue().get())).toList());
                widgetGroup.addWidget(new SlotWidget(new CycleItemStackHandler(items), 0,
                        widgetGroup.getSize().width - 50, widgetGroup.getSize().height - 40, false, false));
            });

    public static final GTRecipeType PHOTOVOLTAIC_POWER_RECIPES = GTRecipeTypes.register("photovoltaic_power", MULTIBLOCK)
            .setMaxIOSize(1, 0, 0, 0)
            .setEUIO(IO.OUT)
            .setXEIVisible(false)
            .setSlotOverlay(false, false, GuiTextures.SOLIDIFIER_OVERLAY)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, ProgressTexture.FillDirection.LEFT_TO_RIGHT);

    public static final GTRecipeType MAGIC_SYNTHESIS_RECIPES = registerRecipeType("magic_synthesis", MAGIC)
            .setEUIO(IO.NONE)
            .setMaxSize(IO.IN, ManaRecipeCapability.CAP, 1)
            .setMaxIOSize(9, 1, 9, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.FORGE_HAMMER);

    public static final GTRecipeType CRUCIBLE_RECIPES = registerRecipeType("crucible", MAGIC)
            .setEUIO(IO.NONE)
            .setMaxIOSize(0, 0, 9, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.FURNACE)
            .setUiBuilder((recipe, widgetGroup) -> {
                List<List<ItemStack>> input_stacks = List.of(List.of(Registries.getItemStack(recipe.data.getString("input"), recipe.data.getInt("input_count"))));
                widgetGroup.addWidget(new SlotWidget(new CycleItemStackHandler(input_stacks), 0, widgetGroup.getSize().width - 40, widgetGroup.getSize().height - 100, false, false));
                List<List<ItemStack>> output_stacks = List.of(List.of(Registries.getItemStack(recipe.data.getString("output"), recipe.data.getInt("output_count"))));
                widgetGroup.addWidget(new SlotWidget(new CycleItemStackHandler(output_stacks), 0, widgetGroup.getSize().width - 40, widgetGroup.getSize().height - 60, false, false));
            });

    // 炼金锅炉，一个燃料输入，可使用普通熔炉燃料，使用源动之焰（8000t）并加速*2，使用炼狱之火（4000000t）并加速*16并行*16，使用炽焱之脉（2000000000t）并加速*64并行*64，一个物品输入，一个流体输入，一个流体输出
    public static final GTRecipeType ALCHEMICAL_BOILER_RECIPES = registerRecipeType("alchemical_boiler", MAGIC)
            .setEUIO(IO.NONE)
            .setMaxIOSize(2, 0, 1, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.FURNACE);

    // 炼金蒸馏，一个燃料输入，可使用普通熔炉燃料，使用源动之焰（8000t）并加速*2，使用炼狱之火（4000000t）并加速*16并行*16，使用炽焱之脉（2000000000t）并加速*64并行*64，一个流体输入，一个物品输出，一个流体输出
    public static final GTRecipeType ALCHEMICAL_DISTILLATION_RECIPES = registerRecipeType("alchemical_distillation", MAGIC)
            .setEUIO(IO.NONE)
            .setMaxIOSize(1, 1, 1, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.FORGE_HAMMER);

    // 暗珠启辉
    public static final GTRecipeType DARK_PEARL_QIHUI_RECIPES = registerRecipeType("dark_pearl_qihui", MAGIC)
            .setEUIO(IO.NONE)
            .setMaxIOSize(1, 1, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.FORGE_HAMMER);

    // 注魔仪式
    public static final GTRecipeType INFUSION_RITUAL_RECIPES = registerRecipeType("infusion_ritual", MAGIC)
            .setEUIO(IO.NONE)
            .setMaxIOSize(16, 1, 8, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.FORGE_HAMMER);

    // 陨星仪式
    // 搓了个方法GTOEBallGeneration不知道怎么调用
    // 配方结束时触发这个方法，想在配方里面传入会生成的方块和权重，不知道怎么写
    public static final GTRecipeType FALLING_STAR_CEREMONY_RECIPES = registerRecipeType("falling_star_ceremony", MAGIC)
            .setEUIO(IO.NONE)
            .setMaxIOSize(1, 1, 1, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.FORGE_HAMMER);

    public static void init() {
        RecipeModify.init();
    }

    private static GTRecipeType registerRecipeType(String name, String type) {
        ALL_RECIPE_TYPE.add(name);
        return register(name, type);
    }
}
