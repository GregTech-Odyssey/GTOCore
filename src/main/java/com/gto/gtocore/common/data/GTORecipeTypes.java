package com.gto.gtocore.common.data;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.machine.trait.TierCasingTrait;
import com.gto.gtocore.api.recipe.JointRecipeType;
import com.gto.gtocore.common.machine.trait.RecyclerLogic;
import com.gto.gtocore.common.recipe.RecipeTypeModify;
import com.gto.gtocore.config.GTOConfig;
import com.gto.gtocore.data.lang.LangHandler;
import com.gto.gtocore.data.recipe.generated.GenerateDisassembly;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.block.ICoilType;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeSerializer;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.api.sound.ExistingSoundEntry;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.common.data.GTSoundEntries;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;
import com.gregtechceu.gtceu.utils.ResearchManager;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;

import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.CycleItemStackHandler;
import com.lowdragmc.lowdraglib.utils.LocalizationUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;
import static com.gto.gtocore.api.GTOValues.*;
import static com.lowdragmc.lowdraglib.gui.texture.ProgressTexture.FillDirection.LEFT_TO_RIGHT;

public final class GTORecipeTypes {

    public static final Map<String, LangHandler.ENCN> LANG = GTCEu.isDataGen() ? new HashMap<>() : null;

    private static final Function<CompoundTag, String> TEMPERATURE = data -> LocalizationUtils.format("gtceu.recipe.temperature", FormattingUtil.formatNumbers(data.getInt("ebf_temp")));

    private static final Function<CompoundTag, String> COIL = data -> {
        ICoilType requiredCoil = ICoilType.getMinRequiredType(data.getInt("ebf_temp"));
        if (requiredCoil != null && requiredCoil.getMaterial() != null) {
            return LocalizationUtils.format("gtceu.recipe.coil.tier",
                    I18n.get(requiredCoil.getMaterial().getUnlocalizedName()));
        }
        return "";
    };

    private static final BiConsumer<GTRecipe, WidgetGroup> COIL_UI = (recipe, widgetGroup) -> {
        int temp = recipe.data.getInt("ebf_temp");
        List<List<ItemStack>> items = new ArrayList<>();
        items.add(GTCEuAPI.HEATING_COILS.entrySet().stream()
                .filter(coil -> coil.getKey().getCoilTemperature() >= temp)
                .map(coil -> new ItemStack(coil.getValue().get())).toList());
        widgetGroup.addWidget(new SlotWidget(new CycleItemStackHandler(items), 0,
                widgetGroup.getSize().width - 50, widgetGroup.getSize().height - 40, false, false));
    };

    public static final GTRecipeType RADIATION_HATCH_RECIPES = recipeType("radiation_hatch", "放射仓材料", MULTIBLOCK)
            .setMaxIOSize(1, 0, 0, 0)
            .setEUIO(IO.NONE)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .addDataInfo(data -> LocalizationUtils.format("gtocore.recipe.radioactivity", data.getInt("radioactivity")));

    public static final GTRecipeType PRIMITIVE_VOID_ORE_RECIPES = GTOConfig.INSTANCE.enablePrimitiveVoidOre ?
            recipeType("primitive_void_ore", "原始虚空采矿", MULTIBLOCK)
                    .setMaxIOSize(0, 0, 1, 0)
                    .setMaxTooltips(1)
                    .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, LEFT_TO_RIGHT)
                    .setSound(GTSoundEntries.MINER) :
            null;

    public static final GTRecipeType ARC_GENERATOR_RECIPES = recipeType("arc_generator", "电弧发生器", ELECTRIC)
            .setMaxIOSize(6, 1, 6, 1)
            .setSlotOverlay(true, false, false, GuiTextures.LIGHTNING_OVERLAY_1)
            .setSlotOverlay(false, false, false, GuiTextures.LIGHTNING_OVERLAY_2)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final GTRecipeType DEHYDRATOR_RECIPES = recipeType("dehydrator", "脱水机", ELECTRIC)
            .setMaxIOSize(2, 6, 2, 2)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_EXTRACT, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public final static GTRecipeType UNPACKER_RECIPES = recipeType("unpacker", "解包机", ELECTRIC)
            .setMaxIOSize(2, 2, 0, 0)
            .setEUIO(IO.IN)
            .prepareBuilder(recipeBuilder -> recipeBuilder.EUt(12).duration(10))
            .setSlotOverlay(false, false, true, GuiTextures.BOX_OVERLAY)
            .setSlotOverlay(true, false, GuiTextures.BOXED_OVERLAY)
            .setProgressBar(GuiTextures.PROGRESS_BAR_UNPACKER, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ASSEMBLER);

    public final static GTRecipeType CLUSTER_RECIPES = recipeType("cluster", "多辊式轧机", ELECTRIC)
            .setMaxIOSize(1, 1, 0, 0)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MOTOR);

    public final static GTRecipeType ROLLING_RECIPES = recipeType("rolling", "辊压机", ELECTRIC)
            .setMaxIOSize(2, 1, 0, 0)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_BENDING, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MOTOR);

    public final static GTRecipeType LAMINATOR_RECIPES = recipeType("laminator", "过胶机", ELECTRIC)
            .setMaxIOSize(3, 1, 1, 0)
            .setEUIO(IO.IN)
            .setSlotOverlay(false, false, GuiTextures.CIRCUIT_OVERLAY)
            .setProgressBar(GuiTextures.PROGRESS_BAR_CIRCUIT, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.BATH);

    public final static GTRecipeType LOOM_RECIPES = recipeType("loom", "织布机", ELECTRIC)
            .setMaxIOSize(2, 1, 0, 0)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_WIREMILL, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MOTOR);

    public final static GTRecipeType LASER_WELDER_RECIPES = recipeType("laser_welder", "激光焊接器", ELECTRIC)
            .setMaxIOSize(3, 1, 0, 0)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_WIREMILL, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final GTRecipeType WORLD_DATA_SCANNER_RECIPES = recipeType("world_data_scanner", "世界信息扫描仪", ELECTRIC)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 1, 2, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final GTRecipeType VACUUM_PUMP_RECIPES = recipeType("vacuum_pump", "真空泵", STEAM)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 0, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.TURBINE);

    public final static GTRecipeType THERMAL_GENERATOR_FUELS = recipeType("thermal_generator", "热力发电", GENERATOR)
            .setMaxIOSize(1, 0, 1, 0).setEUIO(IO.OUT)
            .setSlotOverlay(false, true, true, GuiTextures.FURNACE_OVERLAY_2)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COMBUSTION);

    public final static GTRecipeType SEMI_FLUID_GENERATOR_FUELS = recipeType("semi_fluid_generator", "半流质燃烧", GENERATOR)
            .setMaxIOSize(0, 0, 2, 0).setEUIO(IO.OUT)
            .setSlotOverlay(false, true, true, GuiTextures.FURNACE_OVERLAY_2)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COMBUSTION);

    public final static GTRecipeType SUPERCRITICAL_STEAM_TURBINE_FUELS = recipeType("supercritical_steam_turbine", "超临界蒸汽发电", GENERATOR)
            .setMaxIOSize(0, 0, 1, 1)
            .setEUIO(IO.OUT)
            .setSlotOverlay(false, true, true, GuiTextures.CENTRIFUGE_OVERLAY)
            .setProgressBar(GuiTextures.PROGRESS_BAR_GAS_COLLECTOR, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.TURBINE);

    public final static GTRecipeType ROCKET_ENGINE_FUELS = recipeType("rocket_engine", "火箭燃料", GENERATOR)
            .setEUIO(IO.OUT)
            .setMaxIOSize(0, 0, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.JET_ENGINE);

    public final static GTRecipeType NAQUADAH_REACTOR = recipeType("naquadah_reactor", "硅岩反应", GENERATOR)
            .setEUIO(IO.OUT)
            .setMaxIOSize(0, 0, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COMBUSTION);

    public final static GTRecipeType ELECTRIC_IMPLOSION_COMPRESSOR_RECIPES = recipeType("electric_implosion_compressor", "电力聚爆压缩", MULTIBLOCK)
            .setMaxIOSize(2, 1, 0, 0).setEUIO(IO.IN)
            .prepareBuilder(recipeBuilder -> recipeBuilder.duration(1).EUt(GTValues.VA[GTValues.UV]))
            .setSlotOverlay(false, false, true, GuiTextures.IMPLOSION_OVERLAY_1)
            .setSlotOverlay(false, false, false, GuiTextures.IMPLOSION_OVERLAY_2)
            .setSlotOverlay(true, false, true, GuiTextures.DUST_OVERLAY)
            .setSound(new ExistingSoundEntry(SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS));

    public static final GTRecipeType DISASSEMBLY_RECIPES = recipeType("disassembly", "拆解", MULTIBLOCK)
            .setMaxIOSize(1, 16, 0, 4)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, LEFT_TO_RIGHT)
            .setXEIVisible(false)
            .setSound(GTSoundEntries.ASSEMBLER);

    public static final GTRecipeType NEUTRON_ACTIVATOR_RECIPES = recipeType("neutron_activator", "中子活化", MULTIBLOCK)
            .setMaxIOSize(6, 3, 1, 1)
            .setSound(GTSoundEntries.COOLING)
            .addDataInfo(data -> LocalizationUtils.format("gtocore.recipe.ev_min", data.getInt("ev_min")))
            .addDataInfo(data -> LocalizationUtils.format("gtocore.recipe.ev_max", data.getInt("ev_max")))
            .addDataInfo(data -> LocalizationUtils.format("gtocore.recipe.evt", data.getInt("evt")));

    public static final GTRecipeType HEAT_EXCHANGER_RECIPES = recipeType("heat_exchanger", "流体热交换", MULTIBLOCK)
            .setMaxIOSize(0, 0, 2, 2)
            .setProgressBar(GuiTextures.PROGRESS_BAR_MIXER, LEFT_TO_RIGHT)
            .setMaxTooltips(1)
            .setSound(GTSoundEntries.COOLING);

    public static final GTRecipeType ELEMENT_COPYING_RECIPES = recipeType("element_copying", "元素复制", MULTIBLOCK)
            .setMaxIOSize(1, 1, 1, 1)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final GTRecipeType INTEGRATED_ORE_PROCESSOR = recipeType("integrated_ore_processor", "集成矿石处理", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 9, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setXEIVisible(false)
            .setSound(GTSoundEntries.MACERATOR);

    public static final GTRecipeType FISSION_REACTOR_RECIPES = recipeType("fission_reactor", "裂变反应堆", MULTIBLOCK)
            .setMaxIOSize(1, 1, 0, 0)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC)
            .addDataInfo(data -> LocalizationUtils.format("gtocore.recipe.frheat", FormattingUtil.formatNumbers(data.getInt("FRheat"))));

    public static final GTRecipeType STELLAR_FORGE_RECIPES = recipeType("stellar_forge", "恒星热能熔炼", MULTIBLOCK)
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
            });

    public static final GTRecipeType COMPONENT_ASSEMBLY_RECIPES = recipeType("component_assembly", "部件装配", MULTIBLOCK)
            .setMaxIOSize(9, 1, 9, 0)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .addDataInfo(data -> LocalizationUtils.format(TierCasingTrait.getTierTranslationKey(COMPONENT_ASSEMBLY_CASING_TIER), GTValues.VN[data.getInt(COMPONENT_ASSEMBLY_CASING_TIER)]))
            .setSound(GTSoundEntries.ASSEMBLER);

    public static final GTRecipeType GREENHOUSE_RECIPES = recipeType("greenhouse", "温室培育", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(3, 3, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_BATH, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COOLING);

    public static final GTRecipeType DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES = recipeType("dimensionally_transcendent_plasma_forge", "超维度熔炼", MULTIBLOCK)
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

    public static final GTRecipeType PLASMA_CONDENSER_RECIPES = recipeType("plasma_condenser", "等离子冷凝", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 2, 2, 2)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COOLING);

    public static final GTRecipeType RARE_EARTH_CENTRIFUGAL_RECIPES = recipeType("rare_earth_centrifugal", "稀土离心", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 17, 1, 1)
            .setProgressBar(GuiTextures.CENTRIFUGE_OVERLAY, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.CENTRIFUGE);

    public static final GTRecipeType MAGIC_MANUFACTURER_RECIPES = recipeType("magic_manufacturer", "魔力生成", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 0, 0, 0)
            .setMaxTooltips(4)
            .setProgressBar(GuiTextures.PROGRESS_BAR_MASS_FAB, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COOLING);

    public static final GTRecipeType TRANSCENDING_CRAFTING_RECIPES = recipeType("transcending_crafting", "超临界合成", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(3, 1, 3, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final GTRecipeType MATTER_FABRICATOR_RECIPES = recipeType("matter_fabricator", "物质制造", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 1, 0, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COOLING);

    public static final GTRecipeType VOID_MINER_RECIPES = recipeType("void_miner", "虚空采矿", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(0, 0, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setXEIVisible(false)
            .setSound(GTSoundEntries.MINER);

    public static final GTRecipeType LARGE_VOID_MINER_RECIPES = recipeType("large_void_miner", "Precise Void Mining", "精准虚空采矿", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 4, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MINER);

    public static final GTRecipeType RANDOM_ORE_RECIPES = recipeType("random_ore", "Random Void Mining", "随机虚空采矿", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(0, 200, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setXEIVisible(false)
            .setSound(GTSoundEntries.MINER);

    public static final GTRecipeType VOID_FLUID_DRILLING_RIG_RECIPES = recipeType("void_fluid_drilling_rig", "虚空流体钻机", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 0, 0, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setXEIVisible(false)
            .setSound(GTSoundEntries.CHEMICAL);

    public static final GTRecipeType ANNIHILATE_GENERATOR_RECIPES = recipeType("annihilate_generator", "湮灭发电", MULTIBLOCK)
            .setEUIO(IO.OUT)
            .setMaxIOSize(1, 1, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final GTRecipeType HYPER_REACTOR_RECIPES = recipeType("hyper_reactor", "超能反应", MULTIBLOCK)
            .setEUIO(IO.OUT)
            .setMaxIOSize(0, 0, 2, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final GTRecipeType ADVANCED_HYPER_REACTOR_RECIPES = recipeType("advanced_hyper_reactor", "进阶超能反应", MULTIBLOCK)
            .setEUIO(IO.OUT)
            .setMaxIOSize(0, 0, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final GTRecipeType LARGE_NAQUADAH_REACTOR_RECIPES = recipeType("large_naquadah_reactor", "进阶硅岩反应", MULTIBLOCK)
            .setEUIO(IO.OUT)
            .setMaxIOSize(0, 0, 2, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COMBUSTION);

    public static final GTRecipeType COSMOS_SIMULATION_RECIPES = recipeType("cosmos_simulation", "宇宙模拟", MULTIBLOCK)
            .setMaxIOSize(1, 120, 1, 24)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final GTRecipeType SPACE_PROBE_SURFACE_RECEPTION_RECIPES = recipeType("space_probe_surface_reception", "宇宙射线搜集", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxTooltips(4)
            .setMaxIOSize(2, 0, 0, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final GTRecipeType DECAY_HASTENER_RECIPES = recipeType("decay_hastener", "衰变扭曲", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(0, 1, 1, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final GTRecipeType RECYCLER_RECIPES = recipeType("recycler", "材料回收", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 1, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_RECYCLER, LEFT_TO_RIGHT)
            .addCustomRecipeLogic(new RecyclerLogic())
            .setSound(GTSoundEntries.MACERATOR);

    public static final GTRecipeType MASS_FABRICATOR_RECIPES = recipeType("mass_fabricator", "质量发生器", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 0, 1, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_REPLICATOR, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final GTRecipeType CIRCUIT_ASSEMBLY_LINE_RECIPES = recipeType("circuit_assembly_line", "电路装配线", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(16, 1, 4, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ASSEMBLER)
            .onRecipeBuild(GenerateDisassembly::generateDisassembly);

    public static final GTRecipeType SUPRACHRONAL_ASSEMBLY_LINE_RECIPES = recipeType("suprachronal_assembly_line", "超时空装配线", MULTIBLOCK)
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

    public static final GTRecipeType PRECISION_ASSEMBLER_RECIPES = recipeType("precision_assembler", "精密组装", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxTooltips(4)
            .setMaxIOSize(4, 1, 4, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ASSEMBLER)
            .onRecipeBuild((b, p) -> {
                GenerateDisassembly.generateDisassembly(b, p);
                int tier = GTUtil.getFloorTierByVoltage(b.EUt());
                b.addData(MACHINE_CASING_TIER, tier);
            });

    public static final GTRecipeType ASSEMBLER_MODULE_RECIPES = recipeType("assembler_module", "Space Assembly", "太空组装", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(16, 1, 4, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ASSEMBLER)
            .onRecipeBuild(GenerateDisassembly::generateDisassembly)
            .addDataInfo(data -> LocalizationUtils.format(TierCasingTrait.getTierTranslationKey(POWER_MODULE_TIER), FormattingUtil.formatNumbers(data.getInt(POWER_MODULE_TIER))));

    public static final GTRecipeType MINER_MODULE_RECIPES = recipeType("miner_module", "Space Miner", "太空采矿", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 6, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MINER);

    public static final GTRecipeType DRILLING_MODULE_RECIPES = recipeType("drilling_module", "Space Drilling", "太空钻井", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 0, 1, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MINER);

    public static final GTRecipeType FISHING_GROUND_RECIPES = recipeType("fishing_ground", "渔场", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 24, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MINER);

    public static final GTRecipeType BLOCK_CONVERSIONRECIPES = recipeType("block_conversion", "方块转换", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 1, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final GTRecipeType INCUBATOR_RECIPES = recipeType("incubator", "培养缸", MULTIBLOCK)
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
            .addDataInfo(data -> data.contains("radioactivity") ? LocalizationUtils.format("gtocore.recipe.radioactivity", data.getInt("radioactivity")) : "")
            .onRecipeBuild((b, p) -> b.addData(GLASS_TIER, GTUtil.getFloorTierByVoltage(b.EUt())));

    public static final GTRecipeType PCB_FACTORY_RECIPES = recipeType("pcb_factory", "PCB工厂", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 1, 2, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.CHEMICAL);

    public static final GTRecipeType LAVA_FURNACE_RECIPES = recipeType("lava_furnace", "熔岩炉", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 0, 0, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARC_FURNACE, LEFT_TO_RIGHT)
            .setXEIVisible(false)
            .setSound(GTSoundEntries.FURNACE);

    public static final GTRecipeType LARGE_GAS_COLLECTOR_RECIPES = recipeType("large_gas_collector", "Void Gas Collector", "虚空集气", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 0, 0, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COOLING);

    public static final GTRecipeType AGGREGATION_DEVICE_RECIPES = recipeType("aggregation_device", "聚合装置", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(9, 1, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final GTRecipeType SUPER_PARTICLE_COLLIDER_RECIPES = recipeType("super_particle_collider", "粒子对撞", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(0, 0, 2, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTOSoundEntries.FUSIONLOOP);

    public static final GTRecipeType DIMENSIONAL_FOCUS_ENGRAVING_ARRAY_RECIPES = recipeType("dimensional_focus_engraving_array", "维度聚焦激光蚀刻阵列", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxTooltips(4)
            .setMaxIOSize(2, 1, 2, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC)
            .setHasResearchSlot(true)
            .onRecipeBuild(ResearchManager::createDefaultResearchRecipe);

    public static final GTRecipeType PRECISION_LASER_ENGRAVER_RECIPES = recipeType("precision_laser_engraver", "精密激光蚀刻", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxTooltips(4)
            .setMaxIOSize(9, 1, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final GTRecipeType DIMENSIONALLY_TRANSCENDENT_MIXER_RECIPES = recipeType("dimensionally_transcendent_mixer", "超维度搅拌", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(9, 1, 6, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_BATH, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MIXER);

    public static final GTRecipeType NEUTRON_COMPRESSOR_RECIPES = recipeType("neutron_compressor", "奇点压缩", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 1, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_COMPRESS, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COMPRESSOR);

    public static final GTRecipeType QUANTUM_FORCE_TRANSFORMER_RECIPES = recipeType("quantum_force_transformer", "量子操纵者", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(18, 1, 3, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final GTRecipeType DRAGON_EGG_COPIER_RECIPES = recipeType("dragon_egg_copier", "龙蛋复制", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 2, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COOLING);

    public static final GTRecipeType DOOR_OF_CREATE_RECIPES = recipeType("door_of_create", "创造之门", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxTooltips(4)
            .setMaxIOSize(1, 0, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setXEIVisible(false);

    public static final GTRecipeType BEDROCK_DRILLING_RIG_RECIPES = recipeType("bedrock_drilling_rig", "基岩素提取", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 1, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MACERATOR);

    public static final GTRecipeType CREATE_AGGREGATION_RECIPES = recipeType("create_aggregation", "创造聚合", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 0, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC)
            .setXEIVisible(false);

    public static final GTRecipeType GRAVITATION_SHOCKBURST_RECIPES = recipeType("gravitation_shockburst", "时空引力震爆", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 1, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MACERATOR)
            .onRecipeBuild(ResearchManager::createDefaultResearchRecipe);

    public static final GTRecipeType ULTIMATE_MATERIAL_FORGE_RECIPES = recipeType("ultimate_material_forge", "终极物质锻造", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 2, 2, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MACERATOR);

    public static final GTRecipeType DYSON_SPHERE_RECIPES = recipeType("dyson_sphere", "戴森球", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 0, 0, 0)
            .setXEIVisible(false)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final GTRecipeType PETROCHEMICAL_PLANT_RECIPES = recipeType("petrochemical_plant", "集成石油化工处理", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(0, 0, 2, 12)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COOLING);

    public static final GTRecipeType WEATHER_CONTROL_RECIPES = recipeType("weather_control", "天气控制", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 0, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC)
            .setXEIVisible(false);

    public static final GTRecipeType NANITES_INTEGRATED_PROCESSING_CENTER_RECIPES = recipeType("nanites_integrated_processing_center", "纳米集成加工中心", MULTIBLOCK)
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

    public static final GTRecipeType NANO_FORGE_RECIPES = recipeType("nano_forge", "纳米蜂群工厂", MULTIBLOCK)
            .setMaxIOSize(6, 1, 3, 0)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .addDataInfo(data -> LocalizationUtils.format("gtocore.recipe.nano_forge_tier", FormattingUtil.formatNumbers(data.getInt("nano_forge_tier"))))
            .setSound(GTSoundEntries.ARC);

    public static final GTRecipeType FUEL_REFINING_RECIPES = recipeType("fuel_refining", "燃料精炼", MULTIBLOCK)
            .setMaxIOSize(3, 0, 6, 1)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .addDataInfo(TEMPERATURE)
            .addDataInfo(COIL)
            .setUiBuilder(COIL_UI)
            .setSound(GTSoundEntries.ARC);

    public static final GTRecipeType ATOMIC_ENERGY_EXCITATION_RECIPES = recipeType("atomic_energy_excitation", "原子能激发", MULTIBLOCK)
            .setMaxIOSize(3, 0, 6, 2)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .addDataInfo(TEMPERATURE)
            .addDataInfo(COIL)
            .setUiBuilder(COIL_UI)
            .setSound(GTSoundEntries.ARC);

    public static final GTRecipeType ISA_MILL_RECIPES = recipeType("isa_mill", "湿法碾磨", MULTIBLOCK)
            .setMaxIOSize(2, 1, 1, 0)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MACERATOR)
            .addDataInfo(data -> LocalizationUtils.format("gtocore.recipe.grindball", I18n.get(data.getInt("grindball") == 2 ? "material.gtceu.aluminium" : "material.gtceu.soapstone")));

    public static final GTRecipeType FLOTATING_BENEFICIATION_RECIPES = recipeType("flotating_beneficiation", "浮游选矿", MULTIBLOCK)
            .setMaxIOSize(2, 0, 1, 1)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_BATH, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.CHEMICAL);

    public static final GTRecipeType VACUUM_DRYING_RECIPES = recipeType("vacuum_drying", "真空干燥", MULTIBLOCK)
            .setMaxIOSize(0, 6, 1, 2)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COOLING)
            .addDataInfo(TEMPERATURE)
            .addDataInfo(COIL)
            .setUiBuilder(COIL_UI);

    public static final GTRecipeType DISSOLUTION_TREATMENT_RECIPES = recipeType("dissolution_treatment", "溶解", MULTIBLOCK)
            .setMaxIOSize(2, 2, 2, 1)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_EXTRACT, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final GTRecipeType DIGESTION_TREATMENT_RECIPES = recipeType("digestion_treatment", "煮解", MULTIBLOCK)
            .setMaxIOSize(1, 1, 1, 1)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_EXTRACT, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COOLING)
            .addDataInfo(TEMPERATURE)
            .addDataInfo(COIL)
            .setUiBuilder(COIL_UI);

    public static final GTRecipeType WOOD_DISTILLATION_RECIPES = recipeType("wood_distillation", "集成木质生物质热解", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 1, 1, 15)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.BATH);

    public static final GTRecipeType DESULFURIZER_RECIPES = recipeType("desulfurizer", "脱硫", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(0, 1, 1, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MIXER);

    public static final GTRecipeType LIQUEFACTION_FURNACE_RECIPES = recipeType("liquefaction_furnace", "高温液化", MULTIBLOCK)
            .setMaxIOSize(1, 0, 0, 1)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_EXTRACT, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC)
            .addDataInfo(TEMPERATURE)
            .addDataInfo(COIL)
            .setUiBuilder(COIL_UI);

    public static final GTRecipeType REACTION_FURNACE_RECIPES = recipeType("reaction_furnace", "高温反应", MULTIBLOCK)
            .setMaxIOSize(3, 3, 3, 3)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC)
            .addDataInfo(TEMPERATURE)
            .addDataInfo(COIL)
            .setUiBuilder(COIL_UI);

    public final static GTRecipeType STEAM_CRACKING_RECIPES = recipeType("steam_cracker", "蒸汽裂化", MULTIBLOCK)
            .setMaxIOSize(1, 0, 1, 1)
            .setEUIO(IO.IN)
            .setSlotOverlay(false, true, GuiTextures.CRACKING_OVERLAY_1)
            .setSlotOverlay(true, true, GuiTextures.CRACKING_OVERLAY_2)
            .setProgressBar(GuiTextures.PROGRESS_BAR_CRACKING, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.FIRE);

    public static final GTRecipeType CRUSHER_RECIPES = recipeType("crusher", "Ore Crusher", "矿石破碎", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setSlotOverlay(false, false, GuiTextures.CRUSHED_ORE_OVERLAY)
            .setMaxIOSize(1, 3, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MACERATOR);

    public static final GTRecipeType MOLECULAR_TRANSFORMER_RECIPES = recipeType("molecular_transformer", "物质重组", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 1, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public final static GTRecipeType THREE_DIMENSIONAL_PRINTER_RECIPES = recipeType("three_dimensional_printer", "3D部件打印", MULTIBLOCK)
            .setMaxIOSize(1, 1, 1, 0)
            .setEUIO(IO.IN)
            .setMaxTooltips(4)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    // TODO 添加用途
    public static final GTRecipeType CHEMICAL_VAPOR_DEPOSITION_RECIPES = recipeType("chemical_vapor_deposition", "化学气相沉积", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 1, 1, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.CHEMICAL);

    public static final GTRecipeType PHYSICAL_VAPOR_DEPOSITION_RECIPES = recipeType("physical_vapor_deposition", "物理气相沉积", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 1, 1, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final GTRecipeType BIOCHEMICAL_REACTION_RECIPES = recipeType("biochemical_reaction", "生化反应", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(3, 3, 3, 3)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final GTRecipeType BIOCHEMICAL_EXTRACTION_RECIPES = recipeType("biochemical_extraction", "生物提取", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 0, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_EXTRACT, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final GTRecipeType PLASMA_CENTRIFUGE_RECIPES = recipeType("plasma_centrifuge", "等离子体离心", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 0, 1, 9)
            .setProgressBar(GuiTextures.PROGRESS_BAR_EXTRACT, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final GTRecipeType PLASMA_EXTRACTION_RECIPES = recipeType("plasma_extraction", "等离子体萃取", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(0, 0, 2, 2)
            .setProgressBar(GuiTextures.PROGRESS_BAR_EXTRACT, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    public static final GTRecipeType SINTERING_FURNACE_RECIPES = recipeType("sintering_furnace", "烧结炉", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(6, 1, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.FURNACE)
            .addDataInfo(TEMPERATURE)
            .addDataInfo(COIL)
            .setUiBuilder(COIL_UI);

    public static final GTRecipeType ISOSTATIC_PRESSING_RECIPES = recipeType("isostatic_pressing", "等静压成型", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(6, 1, 1, 0)
            .setProgressBar(GuiTextures.COMPRESSOR_OVERLAY, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COMPRESSOR);

    public static void init() {
        RecipeTypeModify.init();
    }

    private static GTRecipeType recipeType(String name, String enLang, String cnLang, String type) {
        if (LANG != null) LANG.put(name, new LangHandler.ENCN(enLang, cnLang));
        return register(name, type);
    }

    private static GTRecipeType recipeType(String name, String cnLang, String type) {
        if (LANG != null) LANG.put(name, new LangHandler.ENCN(FormattingUtil.toEnglishName(name), cnLang));
        return register(name, type);
    }

    public static GTRecipeType register(String name, String group) {
        GTRecipeType recipeType = new GTRecipeType(GTOCore.id(name), group);
        GTRegistries.register(BuiltInRegistries.RECIPE_TYPE, recipeType.registryName, recipeType);
        GTRegistries.register(BuiltInRegistries.RECIPE_SERIALIZER, recipeType.registryName, new GTRecipeSerializer());
        GTRegistries.RECIPE_TYPES.register(recipeType.registryName, recipeType);
        return recipeType;
    }

    public final static GTRecipeType CHEMICAL = JointRecipeType.register("chemical", GTRecipeTypes.CHEMICAL_RECIPES, GTRecipeTypes.LARGE_CHEMICAL_RECIPES).setMaxIOSize(3, 3, 5, 4).setEUIO(IO.IN).setSound(GTSoundEntries.CHEMICAL);
    public final static GTRecipeType CHEMICAL_ENERGY_DEVOURER_FUELS = JointRecipeType.register("chemical_energy_devourer", GTRecipeTypes.COMBUSTION_GENERATOR_FUELS, GTRecipeTypes.GAS_TURBINE_FUELS, GTORecipeTypes.ROCKET_ENGINE_FUELS).setSound(GTSoundEntries.COMBUSTION);
}
