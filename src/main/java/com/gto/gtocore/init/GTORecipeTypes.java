package com.gto.gtocore.init;

import com.gto.gtocore.api.machine.trait.TierCasingTrait;
import com.gto.gtocore.api.recipe.JointRecipeType;
import com.gto.gtocore.common.machine.trait.RecyclerLogic;
import com.gto.gtocore.config.GTOConfig;
import com.gto.gtocore.data.recipe.generated.GenerateDisassembly;

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
import com.gregtechceu.gtceu.utils.GTUtil;
import com.gregtechceu.gtceu.utils.ResearchManager;

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
import static com.gto.gtocore.api.GTOValues.*;
import static com.gto.gtocore.utils.register.RecipeTypeRegisterUtils.*;
import static com.gto.gtocore.utils.register.RecipeTypeRegisterUtils.register;
import static com.lowdragmc.lowdraglib.gui.texture.ProgressTexture.FillDirection.LEFT_TO_RIGHT;

public interface GTORecipeTypes {

    static void init() {
        RecipeTypeModify.init();
    }

    GTRecipeType PRIMITIVE_VOID_ORE_RECIPES = GTOConfig.INSTANCE.enablePrimitiveVoidOre ?
            register("primitive_void_ore", "原始虚空采矿", MULTIBLOCK)
                    .setMaxIOSize(0, 0, 1, 0)
                    .setMaxTooltips(1)
                    .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, LEFT_TO_RIGHT)
                    .setSound(GTSoundEntries.MINER) :
            null;

    GTRecipeType RADIATION_HATCH_RECIPES = register("radiation_hatch", "放射仓材料", MULTIBLOCK)
            .setMaxIOSize(1, 0, 0, 0)
            .setEUIO(IO.NONE)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .addDataInfo(data -> LocalizationUtils.format("gtocore.recipe.radioactivity", data.getInt("radioactivity")));

    GTRecipeType ARC_GENERATOR_RECIPES = register("arc_generator", "电弧发生器", ELECTRIC)
            .setMaxIOSize(6, 1, 6, 1)
            .setSlotOverlay(true, false, false, GuiTextures.LIGHTNING_OVERLAY_1)
            .setSlotOverlay(false, false, false, GuiTextures.LIGHTNING_OVERLAY_2)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    GTRecipeType DEHYDRATOR_RECIPES = register("dehydrator", "脱水机", ELECTRIC)
            .setMaxIOSize(2, 6, 2, 2)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_EXTRACT, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    GTRecipeType UNPACKER_RECIPES = register("unpacker", "解包机", ELECTRIC)
            .setMaxIOSize(2, 2, 0, 0)
            .setEUIO(IO.IN)
            .prepareBuilder(recipeBuilder -> recipeBuilder.EUt(12).duration(10))
            .setSlotOverlay(false, false, true, GuiTextures.BOX_OVERLAY)
            .setSlotOverlay(true, false, GuiTextures.BOXED_OVERLAY)
            .setProgressBar(GuiTextures.PROGRESS_BAR_UNPACKER, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ASSEMBLER);

    GTRecipeType CLUSTER_RECIPES = register("cluster", "多辊式轧机", ELECTRIC)
            .setMaxIOSize(1, 1, 0, 0)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MOTOR);

    GTRecipeType ROLLING_RECIPES = register("rolling", "辊压机", ELECTRIC)
            .setMaxIOSize(2, 1, 0, 0)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_BENDING, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MOTOR);

    GTRecipeType LAMINATOR_RECIPES = register("laminator", "过胶机", ELECTRIC)
            .setMaxIOSize(3, 1, 1, 0)
            .setEUIO(IO.IN)
            .setSlotOverlay(false, false, GuiTextures.CIRCUIT_OVERLAY)
            .setProgressBar(GuiTextures.PROGRESS_BAR_CIRCUIT, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.BATH);

    GTRecipeType LOOM_RECIPES = register("loom", "织布机", ELECTRIC)
            .setMaxIOSize(2, 1, 0, 0)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_WIREMILL, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MOTOR);

    GTRecipeType LASER_WELDER_RECIPES = register("laser_welder", "激光焊接器", ELECTRIC)
            .setMaxIOSize(3, 1, 0, 0)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_WIREMILL, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    GTRecipeType WORLD_DATA_SCANNER_RECIPES = register("world_data_scanner", "世界信息扫描仪", ELECTRIC)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 1, 2, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    GTRecipeType VACUUM_PUMP_RECIPES = register("vacuum_pump", "真空泵", STEAM)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 0, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.TURBINE);

    GTRecipeType THERMAL_GENERATOR_FUELS = register("thermal_generator", "热力发电", GENERATOR)
            .setMaxIOSize(1, 0, 1, 0).setEUIO(IO.OUT)
            .setSlotOverlay(false, true, true, GuiTextures.FURNACE_OVERLAY_2)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COMBUSTION);

    GTRecipeType SEMI_FLUID_GENERATOR_FUELS = register("semi_fluid_generator", "半流质燃烧", GENERATOR)
            .setMaxIOSize(0, 0, 2, 0).setEUIO(IO.OUT)
            .setSlotOverlay(false, true, true, GuiTextures.FURNACE_OVERLAY_2)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COMBUSTION);

    GTRecipeType SUPERCRITICAL_STEAM_TURBINE_FUELS = register("supercritical_steam_turbine", "超临界蒸汽发电", GENERATOR)
            .setMaxIOSize(0, 0, 1, 1)
            .setEUIO(IO.OUT)
            .setSlotOverlay(false, true, true, GuiTextures.CENTRIFUGE_OVERLAY)
            .setProgressBar(GuiTextures.PROGRESS_BAR_GAS_COLLECTOR, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.TURBINE);

    GTRecipeType ROCKET_ENGINE_FUELS = register("rocket_engine", "火箭燃料", GENERATOR)
            .setEUIO(IO.OUT)
            .setMaxIOSize(0, 0, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.JET_ENGINE);

    GTRecipeType NAQUADAH_REACTOR = register("naquadah_reactor", "硅岩反应", GENERATOR)
            .setEUIO(IO.OUT)
            .setMaxIOSize(0, 0, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COMBUSTION);

    GTRecipeType ELECTRIC_IMPLOSION_COMPRESSOR_RECIPES = register("electric_implosion_compressor", "电力聚爆压缩", MULTIBLOCK)
            .setMaxIOSize(2, 1, 0, 0).setEUIO(IO.IN)
            .prepareBuilder(recipeBuilder -> recipeBuilder.duration(1).EUt(GTValues.VA[GTValues.UV]))
            .setSlotOverlay(false, false, true, GuiTextures.IMPLOSION_OVERLAY_1)
            .setSlotOverlay(false, false, false, GuiTextures.IMPLOSION_OVERLAY_2)
            .setSlotOverlay(true, false, true, GuiTextures.DUST_OVERLAY)
            .setSound(new ExistingSoundEntry(SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS));

    GTRecipeType DISASSEMBLY_RECIPES = register("disassembly", "拆解", MULTIBLOCK)
            .setMaxIOSize(1, 16, 0, 4)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, LEFT_TO_RIGHT)
            .setXEIVisible(false)
            .setSound(GTSoundEntries.ASSEMBLER);

    GTRecipeType NEUTRON_ACTIVATOR_RECIPES = register("neutron_activator", "中子活化", MULTIBLOCK)
            .setMaxIOSize(6, 3, 1, 1)
            .setSound(GTSoundEntries.COOLING)
            .addDataInfo(data -> LocalizationUtils.format("gtocore.recipe.ev_min", data.getInt("ev_min")))
            .addDataInfo(data -> LocalizationUtils.format("gtocore.recipe.ev_max", data.getInt("ev_max")))
            .addDataInfo(data -> LocalizationUtils.format("gtocore.recipe.evt", data.getInt("evt")));

    GTRecipeType HEAT_EXCHANGER_RECIPES = register("heat_exchanger", "流体热交换", MULTIBLOCK)
            .setMaxIOSize(0, 0, 2, 3)
            .setProgressBar(GuiTextures.PROGRESS_BAR_MIXER, LEFT_TO_RIGHT)
            .setMaxTooltips(1)
            .setSound(GTSoundEntries.COOLING);

    GTRecipeType ELEMENT_COPYING_RECIPES = register("element_copying", "元素复制", MULTIBLOCK)
            .setMaxIOSize(1, 1, 1, 1)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    GTRecipeType INTEGRATED_ORE_PROCESSOR = register("integrated_ore_processor", "集成矿石处理", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 9, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setXEIVisible(false)
            .setSound(GTSoundEntries.MACERATOR);

    GTRecipeType FISSION_REACTOR_RECIPES = register("fission_reactor", "裂变反应堆", MULTIBLOCK)
            .setMaxIOSize(1, 1, 0, 0)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC)
            .addDataInfo(data -> LocalizationUtils.format("gtocore.recipe.frheat", FormattingUtil.formatNumbers(data.getInt("FRheat"))));

    GTRecipeType STELLAR_FORGE_RECIPES = register("stellar_forge", "恒星热能熔炼", MULTIBLOCK)
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

    GTRecipeType COMPONENT_ASSEMBLY_RECIPES = register("component_assembly", "部件装配", MULTIBLOCK)
            .setMaxIOSize(9, 1, 9, 0)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .addDataInfo(data -> LocalizationUtils.format(TierCasingTrait.getTierTranslationKey(COMPONENT_ASSEMBLY_CASING_TIER), GTValues.VN[data.getInt(COMPONENT_ASSEMBLY_CASING_TIER)]))
            .setSound(GTSoundEntries.ASSEMBLER);

    GTRecipeType GREENHOUSE_RECIPES = register("greenhouse", "温室培育", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(3, 3, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_BATH, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COOLING);

    GTRecipeType DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES = register("dimensionally_transcendent_plasma_forge", "超维度熔炼", MULTIBLOCK)
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

    GTRecipeType PLASMA_CONDENSER_RECIPES = register("plasma_condenser", "等离子冷凝", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 2, 2, 2)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COOLING);

    GTRecipeType RARE_EARTH_CENTRIFUGAL_RECIPES = register("rare_earth_centrifugal", "稀土离心", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 17, 1, 1)
            .setProgressBar(GuiTextures.CENTRIFUGE_OVERLAY, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.CENTRIFUGE);

    GTRecipeType MAGIC_MANUFACTURER_RECIPES = register("magic_manufacturer", "魔力生成", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 0, 0, 0)
            .setMaxTooltips(4)
            .setProgressBar(GuiTextures.PROGRESS_BAR_MASS_FAB, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COOLING);

    GTRecipeType TRANSCENDING_CRAFTING_RECIPES = register("transcending_crafting", "超临界合成", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(3, 1, 3, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    GTRecipeType MATTER_FABRICATOR_RECIPES = register("matter_fabricator", "物质制造", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 1, 0, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COOLING);

    GTRecipeType VOID_MINER_RECIPES = register("void_miner", "虚空采矿", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(0, 0, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setXEIVisible(false)
            .setSound(GTSoundEntries.MINER);

    GTRecipeType LARGE_VOID_MINER_RECIPES = register("large_void_miner", "Precise Void Mining", "精准虚空采矿", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 4, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MINER);

    GTRecipeType RANDOM_ORE_RECIPES = register("random_ore", "Random Void Mining", "随机虚空采矿", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(0, 200, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setXEIVisible(false)
            .setSound(GTSoundEntries.MINER);

    GTRecipeType VOID_FLUID_DRILLING_RIG_RECIPES = register("void_fluid_drilling_rig", "虚空流体钻机", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 0, 0, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setXEIVisible(false)
            .setSound(GTSoundEntries.CHEMICAL);

    GTRecipeType ANNIHILATE_GENERATOR_RECIPES = register("annihilate_generator", "湮灭发电", MULTIBLOCK)
            .setEUIO(IO.OUT)
            .setMaxIOSize(1, 1, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    GTRecipeType HYPER_REACTOR_RECIPES = register("hyper_reactor", "超能反应", MULTIBLOCK)
            .setEUIO(IO.OUT)
            .setMaxIOSize(0, 0, 2, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    GTRecipeType ADVANCED_HYPER_REACTOR_RECIPES = register("advanced_hyper_reactor", "进阶超能反应", MULTIBLOCK)
            .setEUIO(IO.OUT)
            .setMaxIOSize(0, 0, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    GTRecipeType LARGE_NAQUADAH_REACTOR_RECIPES = register("large_naquadah_reactor", "进阶硅岩反应", MULTIBLOCK)
            .setEUIO(IO.OUT)
            .setMaxIOSize(0, 0, 2, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COMBUSTION);

    GTRecipeType COSMOS_SIMULATION_RECIPES = register("cosmos_simulation", "宇宙模拟", MULTIBLOCK)
            .setMaxIOSize(1, 120, 1, 24)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    GTRecipeType SPACE_PROBE_SURFACE_RECEPTION_RECIPES = register("space_probe_surface_reception", "宇宙射线搜集", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxTooltips(4)
            .setMaxIOSize(2, 0, 0, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    GTRecipeType DECAY_HASTENER_RECIPES = register("decay_hastener", "衰变扭曲", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(0, 1, 1, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    GTRecipeType RECYCLER_RECIPES = register("recycler", "材料回收", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 1, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_RECYCLER, LEFT_TO_RIGHT)
            .addCustomRecipeLogic(new RecyclerLogic())
            .setSound(GTSoundEntries.MACERATOR);

    GTRecipeType MASS_FABRICATOR_RECIPES = register("mass_fabricator", "质量发生器", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 0, 1, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_REPLICATOR, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    GTRecipeType CIRCUIT_ASSEMBLY_LINE_RECIPES = register("circuit_assembly_line", "电路装配线", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(16, 1, 4, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ASSEMBLER)
            .onRecipeBuild(GenerateDisassembly::generateDisassembly);

    GTRecipeType SUPRACHRONAL_ASSEMBLY_LINE_RECIPES = register("suprachronal_assembly_line", "超时空装配线", MULTIBLOCK)
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

    GTRecipeType PRECISION_ASSEMBLER_RECIPES = register("precision_assembler", "精密组装", MULTIBLOCK)
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

    GTRecipeType ASSEMBLER_MODULE_RECIPES = register("assembler_module", "Space Assembly", "太空组装", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(16, 1, 4, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ASSEMBLER)
            .onRecipeBuild(GenerateDisassembly::generateDisassembly)
            .addDataInfo(data -> LocalizationUtils.format(TierCasingTrait.getTierTranslationKey(POWER_MODULE_TIER), FormattingUtil.formatNumbers(data.getInt(POWER_MODULE_TIER))));

    GTRecipeType MINER_MODULE_RECIPES = register("miner_module", "Space Miner", "太空采矿", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 6, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MINER);

    GTRecipeType DRILLING_MODULE_RECIPES = register("drilling_module", "Space Drilling", "太空钻井", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 0, 1, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MINER);

    GTRecipeType FISHING_GROUND_RECIPES = register("fishing_ground", "渔场", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 24, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MINER);

    GTRecipeType BLOCK_CONVERSIONRECIPES = register("block_conversion", "方块转换", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 1, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    GTRecipeType INCUBATOR_RECIPES = register("incubator", "培养缸", MULTIBLOCK)
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

    GTRecipeType PCB_FACTORY_RECIPES = register("pcb_factory", "PCB工厂", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 1, 2, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.CHEMICAL);

    GTRecipeType LAVA_FURNACE_RECIPES = register("lava_furnace", "熔岩炉", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 0, 0, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARC_FURNACE, LEFT_TO_RIGHT)
            .setXEIVisible(false)
            .setSound(GTSoundEntries.FURNACE);

    GTRecipeType LARGE_GAS_COLLECTOR_RECIPES = register("large_gas_collector", "Void Gas Collector", "虚空集气", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 0, 0, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COOLING);

    GTRecipeType AGGREGATION_DEVICE_RECIPES = register("aggregation_device", "聚合装置", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(9, 1, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    GTRecipeType SUPER_PARTICLE_COLLIDER_RECIPES = register("super_particle_collider", "粒子对撞", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(0, 0, 2, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTOSoundEntries.FUSIONLOOP);

    GTRecipeType DIMENSIONAL_FOCUS_ENGRAVING_ARRAY_RECIPES = register("dimensional_focus_engraving_array", "维度聚焦激光蚀刻阵列", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxTooltips(4)
            .setMaxIOSize(2, 1, 2, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC)
            .setHasResearchSlot(true)
            .onRecipeBuild(ResearchManager::createDefaultResearchRecipe);

    GTRecipeType PRECISION_LASER_ENGRAVER_RECIPES = register("precision_laser_engraver", "精密激光蚀刻", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxTooltips(4)
            .setMaxIOSize(9, 1, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    GTRecipeType DIMENSIONALLY_TRANSCENDENT_MIXER_RECIPES = register("dimensionally_transcendent_mixer", "超维度搅拌", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(9, 1, 6, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_BATH, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MIXER);

    GTRecipeType NEUTRON_COMPRESSOR_RECIPES = register("neutron_compressor", "奇点压缩", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 1, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_COMPRESS, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COMPRESSOR);

    GTRecipeType QUANTUM_FORCE_TRANSFORMER_RECIPES = register("quantum_force_transformer", "量子操纵者", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(18, 1, 3, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    GTRecipeType DRAGON_EGG_COPIER_RECIPES = register("dragon_egg_copier", "龙蛋复制", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 2, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COOLING);

    GTRecipeType DOOR_OF_CREATE_RECIPES = register("door_of_create", "创造之门", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxTooltips(4)
            .setMaxIOSize(1, 0, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setXEIVisible(false);

    GTRecipeType BEDROCK_DRILLING_RIG_RECIPES = register("bedrock_drilling_rig", "基岩素提取", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 1, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MACERATOR);

    GTRecipeType CREATE_AGGREGATION_RECIPES = register("create_aggregation", "创造聚合", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 0, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC)
            .setXEIVisible(false);

    GTRecipeType GRAVITATION_SHOCKBURST_RECIPES = register("gravitation_shockburst", "时空引力震爆", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 1, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MACERATOR)
            .onRecipeBuild(ResearchManager::createDefaultResearchRecipe);

    GTRecipeType ULTIMATE_MATERIAL_FORGE_RECIPES = register("ultimate_material_forge", "终极物质锻造", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(2, 2, 2, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MACERATOR);

    GTRecipeType DYSON_SPHERE_RECIPES = register("dyson_sphere", "戴森球", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 0, 0, 0)
            .setXEIVisible(false)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    GTRecipeType PETROCHEMICAL_PLANT_RECIPES = register("petrochemical_plant", "集成石油化工处理", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(0, 0, 2, 12)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COOLING);

    GTRecipeType WEATHER_CONTROL_RECIPES = register("weather_control", "天气控制", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 0, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC)
            .setXEIVisible(false);

    GTRecipeType NANITES_INTEGRATED_PROCESSING_CENTER_RECIPES = register("nanites_integrated_processing_center", "纳米集成加工中心", MULTIBLOCK)
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

    GTRecipeType NANO_FORGE_RECIPES = register("nano_forge", "纳米蜂群工厂", MULTIBLOCK)
            .setMaxIOSize(6, 1, 3, 0)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .addDataInfo(data -> LocalizationUtils.format("gtocore.recipe.nano_forge_tier", FormattingUtil.formatNumbers(data.getInt("nano_forge_tier"))))
            .setSound(GTSoundEntries.ARC);

    GTRecipeType FUEL_REFINING_RECIPES = register("fuel_refining", "燃料精炼", MULTIBLOCK)
            .setMaxIOSize(3, 0, 6, 1)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .addDataInfo(TEMPERATURE)
            .addDataInfo(COIL)
            .setUiBuilder(COIL_UI)
            .setSound(GTSoundEntries.ARC);

    GTRecipeType ATOMIC_ENERGY_EXCITATION_RECIPES = register("atomic_energy_excitation", "原子能激发", MULTIBLOCK)
            .setMaxIOSize(3, 0, 6, 2)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .addDataInfo(TEMPERATURE)
            .addDataInfo(COIL)
            .setUiBuilder(COIL_UI)
            .setSound(GTSoundEntries.ARC);

    GTRecipeType ISA_MILL_RECIPES = register("isa_mill", "湿法碾磨", MULTIBLOCK)
            .setMaxIOSize(2, 1, 1, 0)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MACERATOR)
            .addDataInfo(data -> LocalizationUtils.format("gtocore.recipe.grindball", I18n.get(data.getInt("grindball") == 2 ? "material.gtceu.aluminium" : "material.gtceu.soapstone")));

    GTRecipeType FLOTATING_BENEFICIATION_RECIPES = register("flotating_beneficiation", "浮游选矿", MULTIBLOCK)
            .setMaxIOSize(2, 0, 1, 1)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_BATH, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.CHEMICAL);

    GTRecipeType VACUUM_DRYING_RECIPES = register("vacuum_drying", "真空干燥", MULTIBLOCK)
            .setMaxIOSize(0, 6, 1, 2)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COOLING)
            .addDataInfo(TEMPERATURE)
            .addDataInfo(COIL)
            .setUiBuilder(COIL_UI);

    GTRecipeType DISSOLUTION_TREATMENT_RECIPES = register("dissolution_treatment", "溶解", MULTIBLOCK)
            .setMaxIOSize(2, 2, 2, 1)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_EXTRACT, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    GTRecipeType DIGESTION_TREATMENT_RECIPES = register("digestion_treatment", "煮解", MULTIBLOCK)
            .setMaxIOSize(1, 1, 1, 1)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_EXTRACT, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COOLING)
            .addDataInfo(TEMPERATURE)
            .addDataInfo(COIL)
            .setUiBuilder(COIL_UI);

    GTRecipeType WOOD_DISTILLATION_RECIPES = register("wood_distillation", "集成木质生物质热解", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 1, 1, 15)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.BATH);

    GTRecipeType DESULFURIZER_RECIPES = register("desulfurizer", "脱硫", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(0, 1, 1, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MIXER);

    GTRecipeType LIQUEFACTION_FURNACE_RECIPES = register("liquefaction_furnace", "高温液化", MULTIBLOCK)
            .setMaxIOSize(1, 0, 0, 1)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_EXTRACT, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC)
            .addDataInfo(TEMPERATURE)
            .addDataInfo(COIL)
            .setUiBuilder(COIL_UI);

    GTRecipeType REACTION_FURNACE_RECIPES = register("reaction_furnace", "高温反应", MULTIBLOCK)
            .setMaxIOSize(3, 3, 3, 3)
            .setEUIO(IO.IN)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC)
            .addDataInfo(TEMPERATURE)
            .addDataInfo(COIL)
            .setUiBuilder(COIL_UI);

    GTRecipeType STEAM_CRACKING_RECIPES = register("steam_cracker", "蒸汽裂化", MULTIBLOCK)
            .setMaxIOSize(1, 0, 1, 1)
            .setEUIO(IO.IN)
            .setSlotOverlay(false, true, GuiTextures.CRACKING_OVERLAY_1)
            .setSlotOverlay(true, true, GuiTextures.CRACKING_OVERLAY_2)
            .setProgressBar(GuiTextures.PROGRESS_BAR_CRACKING, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.FIRE);

    GTRecipeType CRUSHER_RECIPES = register("crusher", "Ore Crusher", "矿石破碎", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setSlotOverlay(false, false, GuiTextures.CRUSHED_ORE_OVERLAY)
            .setMaxIOSize(1, 3, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.MACERATOR);

    GTRecipeType MOLECULAR_TRANSFORMER_RECIPES = register("molecular_transformer", "物质重组", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 1, 0, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    GTRecipeType THREE_DIMENSIONAL_PRINTER_RECIPES = register("three_dimensional_printer", "3D部件打印", MULTIBLOCK)
            .setMaxIOSize(1, 1, 1, 0)
            .setEUIO(IO.IN)
            .setMaxTooltips(4)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    // TODO 添加用途
    GTRecipeType CHEMICAL_VAPOR_DEPOSITION_RECIPES = register("chemical_vapor_deposition", "化学气相沉积", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 1, 1, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.CHEMICAL);

    GTRecipeType PHYSICAL_VAPOR_DEPOSITION_RECIPES = register("physical_vapor_deposition", "物理气相沉积", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 1, 1, 1)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    GTRecipeType BIOCHEMICAL_REACTION_RECIPES = register("biochemical_reaction", "生化反应", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(3, 3, 3, 3)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    GTRecipeType BIOCHEMICAL_EXTRACTION_RECIPES = register("biochemical_extraction", "生物提取", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 0, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_EXTRACT, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    GTRecipeType PLASMA_CENTRIFUGE_RECIPES = register("plasma_centrifuge", "等离子体离心", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(1, 0, 1, 9)
            .setProgressBar(GuiTextures.PROGRESS_BAR_EXTRACT, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    GTRecipeType PLASMA_EXTRACTION_RECIPES = register("plasma_extraction", "等离子体萃取", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(0, 0, 2, 2)
            .setProgressBar(GuiTextures.PROGRESS_BAR_EXTRACT, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.ARC);

    GTRecipeType SINTERING_FURNACE_RECIPES = register("sintering_furnace", "烧结炉", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(6, 1, 1, 0)
            .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.FURNACE)
            .addDataInfo(TEMPERATURE)
            .addDataInfo(COIL)
            .setUiBuilder(COIL_UI);

    GTRecipeType ISOSTATIC_PRESSING_RECIPES = register("isostatic_pressing", "等静压成型", MULTIBLOCK)
            .setEUIO(IO.IN)
            .setMaxIOSize(6, 1, 1, 0)
            .setProgressBar(GuiTextures.COMPRESSOR_OVERLAY, LEFT_TO_RIGHT)
            .setSound(GTSoundEntries.COMPRESSOR);

    GTRecipeType CHEMICAL = JointRecipeType.register("chemical", GTRecipeTypes.CHEMICAL_RECIPES, GTRecipeTypes.LARGE_CHEMICAL_RECIPES).setMaxIOSize(3, 3, 5, 4).setEUIO(IO.IN).setSound(GTSoundEntries.CHEMICAL);
    GTRecipeType CHEMICAL_ENERGY_DEVOURER_FUELS = JointRecipeType.register("chemical_energy_devourer", GTRecipeTypes.COMBUSTION_GENERATOR_FUELS, GTRecipeTypes.GAS_TURBINE_FUELS, GTORecipeTypes.ROCKET_ENGINE_FUELS).setSound(GTSoundEntries.COMBUSTION);
}
