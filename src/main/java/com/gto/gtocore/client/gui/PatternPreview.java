package com.gto.gtocore.client.gui;

import com.gto.gtocore.api.gui.PatternSlotWidget;
import com.gto.gtocore.api.item.ItemHandlerModifiable;
import com.gto.gtocore.api.machine.IMultiblockMachineDefinition;
import com.gto.gtocore.api.machine.feature.multiblock.IMultiStructureMachine;
import com.gto.gtocore.config.GTOConfig;
import com.gto.gtocore.init.GTOBlocks;
import com.gto.gtocore.init.machines.GeneratorMultiblock;
import com.gto.gtocore.init.machines.MultiBlockD;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;
import com.gregtechceu.gtceu.api.pattern.predicates.SimplePredicate;
import com.gregtechceu.gtceu.common.data.GCYMBlocks;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.machines.GTMultiMachines;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.gregtechceu.gtceu.integration.xei.handlers.item.CycleItemStackHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.google.common.collect.ImmutableMap;
import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.texture.ColorRectTexture;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.jei.IngredientIO;
import com.lowdragmc.lowdraglib.utils.BlockInfo;
import com.lowdragmc.lowdraglib.utils.TrackedDummyWorld;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import dev.emi.emi.screen.RecipeScreen;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.longs.LongSets;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@OnlyIn(Dist.CLIENT)
public final class PatternPreview extends WidgetGroup {

    public static final ImmutableMap<MachineDefinition, Pair<String, List<ItemStack>>> SAME_STRUCTURE_MACHINES;

    static {
        ImmutableMap.Builder<MachineDefinition, Pair<String, List<ItemStack>>> SameStructureMachineBuilder = ImmutableMap.builder();

        SameStructureMachineBuilder.put(GTMultiMachines.FUSION_REACTOR[GTValues.ZPM], Pair.of(GTMultiMachines.FUSION_REACTOR[GTValues.LuV].getDescriptionId(), List.of(
                GTMultiMachines.FUSION_REACTOR[GTValues.ZPM].asStack(),
                GTMachines.ENERGY_INPUT_HATCH[GTValues.ZPM].asStack(16),
                GTMachines.FLUID_IMPORT_HATCH[GTValues.ZPM].asStack(16),
                GTMachines.FLUID_EXPORT_HATCH[GTValues.ZPM].asStack(16),
                GTBlocks.FUSION_CASING_MK2.asStack(79),
                GTBlocks.FUSION_COIL.asStack(4))));
        SameStructureMachineBuilder.put(GTMultiMachines.FUSION_REACTOR[GTValues.UV], Pair.of(GTMultiMachines.FUSION_REACTOR[GTValues.LuV].getDescriptionId(), List.of(
                GTMultiMachines.FUSION_REACTOR[GTValues.UV].asStack(),
                GTMachines.ENERGY_INPUT_HATCH[GTValues.UV].asStack(16),
                GTMachines.FLUID_IMPORT_HATCH[GTValues.UV].asStack(16),
                GTMachines.FLUID_EXPORT_HATCH[GTValues.UV].asStack(16),
                GTBlocks.FUSION_CASING_MK3.asStack(79),
                GTBlocks.FUSION_COIL.asStack(4))));
        SameStructureMachineBuilder.put(MultiBlockD.FUSION_REACTOR[GTValues.UHV], Pair.of(GTMultiMachines.FUSION_REACTOR[GTValues.LuV].getDescriptionId(), List.of(
                MultiBlockD.FUSION_REACTOR[GTValues.UHV].asStack(),
                GTMachines.ENERGY_INPUT_HATCH[GTValues.UHV].asStack(16),
                GTMachines.FLUID_IMPORT_HATCH[GTValues.UHV].asStack(16),
                GTMachines.FLUID_EXPORT_HATCH[GTValues.UHV].asStack(16),
                GTOBlocks.FUSION_CASING_MK4.asStack(79),
                GTOBlocks.ADVANCED_FUSION_COIL.asStack(4))));
        SameStructureMachineBuilder.put(MultiBlockD.FUSION_REACTOR[GTValues.UEV], Pair.of(GTMultiMachines.FUSION_REACTOR[GTValues.LuV].getDescriptionId(), List.of(
                MultiBlockD.FUSION_REACTOR[GTValues.UEV].asStack(),
                GTMachines.ENERGY_INPUT_HATCH[GTValues.UEV].asStack(16),
                GTMachines.FLUID_IMPORT_HATCH[GTValues.UEV].asStack(16),
                GTMachines.FLUID_EXPORT_HATCH[GTValues.UEV].asStack(16),
                GTOBlocks.FUSION_CASING_MK5.asStack(79),
                GTOBlocks.FUSION_COIL_MK2.asStack(4))));
        SameStructureMachineBuilder.put(MultiBlockD.KUANGBIAO_ONE_GIANT_NUCLEAR_FUSION_REACTOR[GTValues.ZPM], Pair.of(MultiBlockD.KUANGBIAO_ONE_GIANT_NUCLEAR_FUSION_REACTOR[GTValues.LuV].getDescriptionId(), List.of(
                MultiBlockD.KUANGBIAO_ONE_GIANT_NUCLEAR_FUSION_REACTOR[GTValues.ZPM].asStack(),
                GTMachines.FLUID_EXPORT_HATCH[GTValues.ZPM].asStack(16),
                GTMachines.FLUID_IMPORT_HATCH[GTValues.ZPM].asStack(16),
                GTMachines.LASER_INPUT_HATCH_256[GTValues.ZPM].asStack(16),
                GTOBlocks.PBI_RADIATION_RESISTANT_MECHANICAL_ENCLOSURE.asStack(1012),
                GTOBlocks.STRENGTHEN_THE_BASE_BLOCK.asStack(940),
                ChemicalHelper.get(TagPrefix.frameGt, GTMaterials.Duranium).copyWithCount(237),
                ChemicalHelper.get(TagPrefix.frameGt, GTMaterials.Tungsten).copyWithCount(163),
                GTBlocks.FUSION_GLASS.asStack(80),
                GTBlocks.FUSION_CASING_MK2.asStack(1534),
                GCYMBlocks.ELECTROLYTIC_CELL.asStack(60),
                GCYMBlocks.CASING_NONCONDUCTING.asStack(819),
                GTOBlocks.FISSION_REACTOR_CASING.asStack(584),
                GTOBlocks.HIGH_STRENGTH_CONCRETE.asStack(396),
                GTOBlocks.COMPRESSED_FUSION_COIL.asStack(584))));
        SameStructureMachineBuilder.put(MultiBlockD.KUANGBIAO_ONE_GIANT_NUCLEAR_FUSION_REACTOR[GTValues.UV], Pair.of(MultiBlockD.KUANGBIAO_ONE_GIANT_NUCLEAR_FUSION_REACTOR[GTValues.LuV].getDescriptionId(), List.of(
                MultiBlockD.KUANGBIAO_ONE_GIANT_NUCLEAR_FUSION_REACTOR[GTValues.UV].asStack(),
                GTMachines.FLUID_EXPORT_HATCH[GTValues.UV].asStack(16),
                GTMachines.FLUID_IMPORT_HATCH[GTValues.UV].asStack(16),
                GTMachines.LASER_INPUT_HATCH_256[GTValues.UV].asStack(16),
                GTOBlocks.PBI_RADIATION_RESISTANT_MECHANICAL_ENCLOSURE.asStack(1012),
                GTOBlocks.STRENGTHEN_THE_BASE_BLOCK.asStack(940),
                ChemicalHelper.get(TagPrefix.frameGt, GTMaterials.Naquadria).copyWithCount(237),
                ChemicalHelper.get(TagPrefix.frameGt, GTMaterials.Tungsten).copyWithCount(163),
                GTBlocks.FUSION_GLASS.asStack(80),
                GTBlocks.FUSION_CASING_MK3.asStack(1534),
                GCYMBlocks.ELECTROLYTIC_CELL.asStack(60),
                GCYMBlocks.CASING_NONCONDUCTING.asStack(819),
                GTOBlocks.FISSION_REACTOR_CASING.asStack(584),
                GTOBlocks.HIGH_STRENGTH_CONCRETE.asStack(396),
                GTOBlocks.ADVANCED_COMPRESSED_FUSION_COIL.asStack(584))));
        SameStructureMachineBuilder.put(MultiBlockD.KUANGBIAO_ONE_GIANT_NUCLEAR_FUSION_REACTOR[GTValues.UHV], Pair.of(MultiBlockD.KUANGBIAO_ONE_GIANT_NUCLEAR_FUSION_REACTOR[GTValues.LuV].getDescriptionId(), List.of(
                MultiBlockD.KUANGBIAO_ONE_GIANT_NUCLEAR_FUSION_REACTOR[GTValues.UHV].asStack(),
                GTMachines.FLUID_EXPORT_HATCH[GTValues.UHV].asStack(16),
                GTMachines.FLUID_IMPORT_HATCH[GTValues.UHV].asStack(16),
                GTMachines.LASER_INPUT_HATCH_256[GTValues.UHV].asStack(16),
                GTOBlocks.PBI_RADIATION_RESISTANT_MECHANICAL_ENCLOSURE.asStack(1012),
                GTOBlocks.STRENGTHEN_THE_BASE_BLOCK.asStack(940),
                ChemicalHelper.get(TagPrefix.frameGt, GTMaterials.Trinium).copyWithCount(237),
                ChemicalHelper.get(TagPrefix.frameGt, GTMaterials.Tungsten).copyWithCount(163),
                GTBlocks.FUSION_GLASS.asStack(80),
                GTOBlocks.FUSION_CASING_MK4.asStack(1534),
                GCYMBlocks.ELECTROLYTIC_CELL.asStack(60),
                GCYMBlocks.CASING_NONCONDUCTING.asStack(819),
                GTOBlocks.FISSION_REACTOR_CASING.asStack(584),
                GTOBlocks.HIGH_STRENGTH_CONCRETE.asStack(396),
                GTOBlocks.COMPRESSED_FUSION_COIL_MK2_PROTOTYPE.asStack(584))));
        SameStructureMachineBuilder.put(MultiBlockD.KUANGBIAO_ONE_GIANT_NUCLEAR_FUSION_REACTOR[GTValues.UEV], Pair.of(MultiBlockD.KUANGBIAO_ONE_GIANT_NUCLEAR_FUSION_REACTOR[GTValues.LuV].getDescriptionId(), List.of(
                MultiBlockD.KUANGBIAO_ONE_GIANT_NUCLEAR_FUSION_REACTOR[GTValues.UEV].asStack(),
                GTMachines.FLUID_EXPORT_HATCH[GTValues.UEV].asStack(16),
                GTMachines.FLUID_IMPORT_HATCH[GTValues.UEV].asStack(16),
                GTMachines.LASER_INPUT_HATCH_256[GTValues.UEV].asStack(16),
                GTOBlocks.PBI_RADIATION_RESISTANT_MECHANICAL_ENCLOSURE.asStack(1012),
                GTOBlocks.STRENGTHEN_THE_BASE_BLOCK.asStack(940),
                ChemicalHelper.get(TagPrefix.frameGt, GTMaterials.Neutronium).copyWithCount(237),
                ChemicalHelper.get(TagPrefix.frameGt, GTMaterials.Tungsten).copyWithCount(163),
                GTBlocks.FUSION_GLASS.asStack(80),
                GTOBlocks.FUSION_CASING_MK5.asStack(1534),
                GCYMBlocks.ELECTROLYTIC_CELL.asStack(60),
                GCYMBlocks.CASING_NONCONDUCTING.asStack(819),
                GTOBlocks.FISSION_REACTOR_CASING.asStack(584),
                GTOBlocks.HIGH_STRENGTH_CONCRETE.asStack(396),
                GTOBlocks.COMPRESSED_FUSION_COIL_MK2.asStack(584))));
        SameStructureMachineBuilder.put(GeneratorMultiblock.PHOTOVOLTAIC_POWER_STATION_PULSATING, Pair.of(GeneratorMultiblock.PHOTOVOLTAIC_POWER_STATION_ENERGETIC.getDescriptionId(), List.of(
                GeneratorMultiblock.PHOTOVOLTAIC_POWER_STATION_PULSATING.asStack(),
                GTMachines.ITEM_IMPORT_BUS[0].asStack(),
                GTBlocks.CASING_TITANIUM_STABLE.asStack(22),
                GTOBlocks.PULSATING_PHOTOVOLTAIC_BLOCK.asStack(25))));
        SameStructureMachineBuilder.put(GeneratorMultiblock.PHOTOVOLTAIC_POWER_STATION_VIBRANT, Pair.of(GeneratorMultiblock.PHOTOVOLTAIC_POWER_STATION_ENERGETIC.getDescriptionId(), List.of(
                GeneratorMultiblock.PHOTOVOLTAIC_POWER_STATION_VIBRANT.asStack(),
                GTMachines.ITEM_IMPORT_BUS[0].asStack(),
                GTBlocks.CASING_TUNGSTENSTEEL_ROBUST.asStack(22),
                GTOBlocks.VIBRANT_PHOTOVOLTAIC_BLOCK.asStack(25))));
        SameStructureMachineBuilder.put(GTMultiMachines.LARGE_GAS_TURBINE, Pair.of(GTMultiMachines.LARGE_STEAM_TURBINE.getDescriptionId(), List.of(
                GTMultiMachines.LARGE_GAS_TURBINE.asStack(),
                GTMachines.ROTOR_HOLDER[GTValues.EV].asStack(),
                GTMachines.MUFFLER_HATCH[GTValues.LV].asStack(),
                GTMachines.ENERGY_OUTPUT_HATCH[GTValues.ULV].asStack(),
                GTMachines.FLUID_IMPORT_HATCH[GTValues.ULV].asStack(),
                GTMachines.MAINTENANCE_HATCH.asStack(),
                GTBlocks.CASING_STAINLESS_TURBINE.asStack(28),
                GTBlocks.CASING_STAINLESS_STEEL_GEARBOX.asStack(2))));
        SameStructureMachineBuilder.put(GeneratorMultiblock.ROCKET_LARGE_TURBINE, Pair.of(GTMultiMachines.LARGE_STEAM_TURBINE.getDescriptionId(), List.of(
                GeneratorMultiblock.ROCKET_LARGE_TURBINE.asStack(),
                GTMachines.ROTOR_HOLDER[GTValues.EV].asStack(),
                GTMachines.MUFFLER_HATCH[GTValues.LV].asStack(),
                GTMachines.ENERGY_OUTPUT_HATCH[GTValues.ULV].asStack(),
                GTMachines.FLUID_IMPORT_HATCH[GTValues.ULV].asStack(),
                GTMachines.MAINTENANCE_HATCH.asStack(),
                GTBlocks.CASING_TITANIUM_TURBINE.asStack(28),
                GTBlocks.CASING_TITANIUM_GEARBOX.asStack(2))));
        SameStructureMachineBuilder.put(GeneratorMultiblock.SUPERCRITICAL_STEAM_TURBINE, Pair.of(GTMultiMachines.LARGE_STEAM_TURBINE.getDescriptionId(), List.of(
                GeneratorMultiblock.SUPERCRITICAL_STEAM_TURBINE.asStack(),
                GTMachines.ROTOR_HOLDER[GTValues.IV].asStack(),
                GTMachines.MUFFLER_HATCH[GTValues.LV].asStack(),
                GTMachines.ENERGY_OUTPUT_HATCH[GTValues.ULV].asStack(),
                GTMachines.FLUID_EXPORT_HATCH[GTValues.ULV].asStack(),
                GTMachines.FLUID_IMPORT_HATCH[GTValues.ULV].asStack(),
                GTMachines.MAINTENANCE_HATCH.asStack(),
                GTOBlocks.SUPERCRITICAL_TURBINE_CASING.asStack(27),
                GTBlocks.CASING_TUNGSTENSTEEL_GEARBOX.asStack(2))));
        SameStructureMachineBuilder.put(GeneratorMultiblock.GAS_MEGA_TURBINE, Pair.of(GeneratorMultiblock.STEAM_MEGA_TURBINE.getDescriptionId(), List.of(
                GeneratorMultiblock.GAS_MEGA_TURBINE.asStack(),
                GTMachines.ROTOR_HOLDER[GTValues.IV].asStack(12),
                GTMachines.MUFFLER_HATCH[GTValues.LV].asStack(),
                GTMachines.ENERGY_OUTPUT_HATCH[GTValues.ULV].asStack(4),
                GTMachines.MAINTENANCE_HATCH.asStack(),
                GTBlocks.CASING_STAINLESS_TURBINE.asStack(392),
                GTBlocks.CASING_STAINLESS_STEEL_GEARBOX.asStack(30))));
        SameStructureMachineBuilder.put(GeneratorMultiblock.ROCKET_MEGA_TURBINE, Pair.of(GeneratorMultiblock.STEAM_MEGA_TURBINE.getDescriptionId(), List.of(
                GeneratorMultiblock.ROCKET_MEGA_TURBINE.asStack(),
                GTMachines.ROTOR_HOLDER[GTValues.IV].asStack(12),
                GTMachines.MUFFLER_HATCH[GTValues.LV].asStack(),
                GTMachines.ENERGY_OUTPUT_HATCH[GTValues.ULV].asStack(4),
                GTMachines.MAINTENANCE_HATCH.asStack(),
                GTBlocks.CASING_TITANIUM_TURBINE.asStack(392),
                GTBlocks.CASING_TITANIUM_GEARBOX.asStack(30))));
        SameStructureMachineBuilder.put(GeneratorMultiblock.SUPERCRITICAL_MEGA_STEAM_TURBINE, Pair.of(GeneratorMultiblock.STEAM_MEGA_TURBINE.getDescriptionId(), List.of(
                GeneratorMultiblock.SUPERCRITICAL_MEGA_STEAM_TURBINE.asStack(),
                GTMachines.ROTOR_HOLDER[GTValues.LuV].asStack(12),
                GTMachines.MUFFLER_HATCH[GTValues.LV].asStack(),
                GTMachines.ENERGY_OUTPUT_HATCH[GTValues.ULV].asStack(4),
                GTMachines.MAINTENANCE_HATCH.asStack(),
                GTOBlocks.SUPERCRITICAL_TURBINE_CASING.asStack(392),
                GTBlocks.CASING_TUNGSTENSTEEL_GEARBOX.asStack(30))));

        SAME_STRUCTURE_MACHINES = SameStructureMachineBuilder.build();
    }

    private static final boolean isEMILoaded = GTCEu.Mods.isEMILoaded();
    private boolean isLoaded;
    private static TrackedDummyWorld LEVEL;
    private static final Map<MultiblockMachineDefinition, MBPattern[]> CACHE = new Object2ObjectOpenHashMap<>();
    private final SceneWidget sceneWidget;
    @Getter
    private final DraggableScrollableWidgetGroup scrollableWidgetGroup;
    private final MultiblockMachineDefinition controllerDefinition;
    private final MBPattern[] patterns;
    private final List<SimplePredicate> predicates = new ArrayList<>();
    private boolean same;
    private int index;
    private int layer;
    private SlotWidget[] slotWidgets;
    private SlotWidget[] candidates;

    private PatternPreview(MultiblockMachineDefinition controllerDefinition) {
        super(0, 0, 160, 160);
        setClientSideWidget();
        if (GTOConfig.INSTANCE.fastMultiBlockPage) {
            same = SAME_STRUCTURE_MACHINES.containsKey(controllerDefinition);
        }
        boolean hide = same;
        this.controllerDefinition = controllerDefinition;
        layer = -1;

        if (hide) {
            addWidget(new ImageWidget(3, 3, 150, 150,
                    new TextTexture((I18n.get("gui.ae2.And") + " " + I18n.get(SAME_STRUCTURE_MACHINES.get(controllerDefinition).getFirst()) + " " + I18n.get("gtocore.same")), -1)
                            .setType(TextTexture.TextType.ROLL)
                            .setWidth(170)
                            .setDropShadow(true)));
            sceneWidget = null;
        } else {
            addWidget(sceneWidget = new SceneWidget(3, 3, 150, 150, LEVEL)
                    .setOnSelected(this::onPosSelected)
                    .setRenderFacing(false)
                    .setRenderFacing(false));
        }

        scrollableWidgetGroup = new DraggableScrollableWidgetGroup(3, 132, 154, 22)
                .setXScrollBarHeight(4)
                .setXBarStyle(GuiTextures.SLIDER_BACKGROUND, GuiTextures.BUTTON)
                .setScrollable(true)
                .setDraggable(true)
                .setScrollWheelDirection(DraggableScrollableWidgetGroup.ScrollWheelDirection.HORIZONTAL);
        scrollableWidgetGroup.setScrollYOffset(0);
        addWidget(scrollableWidgetGroup);

        if (!hide && ConfigHolder.INSTANCE.client.useVBO) {
            if (!RenderSystem.isOnRenderThread()) {
                RenderSystem.recordRenderCall(sceneWidget::useCacheBuffer);
            } else {
                sceneWidget.useCacheBuffer();
            }
        }

        addWidget(new ImageWidget(3, 3, 160, 10,
                new TextTexture(controllerDefinition.getDescriptionId(), -1)
                        .setType(TextTexture.TextType.ROLL)
                        .setWidth(170)
                        .setDropShadow(true)));

        if (hide) {
            patterns = null;
        } else {
            if (CACHE.containsKey(controllerDefinition)) {
                patterns = CACHE.get(controllerDefinition);
            } else {
                IMultiblockMachineDefinition.Pattern[] pattern = ((IMultiblockMachineDefinition) controllerDefinition).gtocore$getPatterns();
                patterns = new MBPattern[pattern.length];
                for (int i = 0; i < pattern.length; i++) {
                    patterns[i] = initializePattern(pattern[i], i);
                }
                CACHE.put(controllerDefinition, patterns);
                ((IMultiblockMachineDefinition) controllerDefinition).gtocore$clear();
            }
        }

        if (!hide) {
            addWidget(new ButtonWidget(138, 30, 18, 18, new GuiTextureGroup(
                    ColorPattern.T_GRAY.rectTexture(),
                    new TextTexture("1").setSupplier(() -> "P:" + index)),
                    (x) -> {
                        index = (index + 1 >= patterns.length) ? 0 : index + 1;
                        setPage();
                    })
                    .setHoverBorderTexture(1, -1));

            addWidget(new ButtonWidget(138, 50, 18, 18, new GuiTextureGroup(
                    ColorPattern.T_GRAY.rectTexture(),
                    new TextTexture("1").setSupplier(() -> layer >= 0 ? "L:" + layer : "ALL")),
                    cd -> updateLayer())
                    .setHoverBorderTexture(1, -1));
        }
        setPage();
    }

    private void updateLayer() {
        MBPattern pattern = patterns[index];
        if (layer + 1 >= -1 && layer + 1 <= pattern.maxY - pattern.minY) {
            layer += 1;
            if (pattern.controllerBase.isFormed()) {
                onFormedSwitch(false);
            }
        } else {
            layer = -1;
            if (!pattern.controllerBase.isFormed()) {
                onFormedSwitch(true);
            }
        }
        setupScene(pattern);
    }

    private void setupScene(MBPattern pattern) {
        Stream<BlockPos> stream = pattern.blockMap.keySet().stream()
                .filter(pos -> layer == -1 || layer + pattern.minY == pos.getY());
        if (pattern.controllerBase.isFormed()) {
            LongSet set = pattern.controllerBase.getMultiblockState().getMatchContext().getOrDefault("renderMask",
                    LongSets.EMPTY_SET);
            Set<BlockPos> modelDisabled = set.stream().map(BlockPos::of).collect(Collectors.toSet());
            if (!modelDisabled.isEmpty()) {
                sceneWidget.setRenderedCore(
                        stream.filter(pos -> !modelDisabled.contains(pos)).collect(Collectors.toList()), null);
            } else {
                sceneWidget.setRenderedCore(stream.toList(), null);
            }
        } else {
            sceneWidget.setRenderedCore(stream.toList(), null);
        }
    }

    public static PatternPreview getPatternWidget(MultiblockMachineDefinition controllerDefinition) {
        if (LEVEL == null) {
            if (Minecraft.getInstance().level == null) {
                GTCEu.LOGGER.error("Try to init pattern previews before level load");
                throw new IllegalStateException();
            }
            LEVEL = new TrackedDummyWorld();
        }
        return new PatternPreview(controllerDefinition);
    }

    private void setPage() {
        List<ItemStack> itemList;
        if (same) {
            itemList = SAME_STRUCTURE_MACHINES.get(controllerDefinition).getSecond();
        } else if (index < patterns.length && index >= 0) {
            layer = -1;
            MBPattern pattern = patterns[index];
            setupScene(pattern);
            itemList = pattern.parts;
        } else {
            return;
        }
        if (slotWidgets != null) {
            for (SlotWidget slotWidget : slotWidgets) {
                scrollableWidgetGroup.removeWidget(slotWidget);
            }
        }
        slotWidgets = new SlotWidget[itemList.size()];
        for (int i = 0; i < slotWidgets.length; i++) {
            if (isEMILoaded) {
                slotWidgets[i] = new PatternSlotWidget(new ItemHandlerModifiable(itemList.get(i)), i, 4 + i * 18, 0);
            } else {
                slotWidgets[i] = new SlotWidget(new ItemHandlerModifiable(itemList.get(i)), i, 4 + i * 18, 0, false, false)
                        .setBackgroundTexture(ColorPattern.T_GRAY.rectTexture())
                        .setIngredientIO(IngredientIO.INPUT);
            }
            scrollableWidgetGroup.addWidget(slotWidgets[i]);
        }
    }

    private void onFormedSwitch(boolean isFormed) {
        MBPattern pattern = patterns[index];
        IMultiController controllerBase = pattern.controllerBase;
        if (isFormed) {
            layer = -1;
            loadControllerFormed(pattern.blockMap.keySet(), controllerBase, index);
        } else {
            sceneWidget.setRenderedCore(pattern.blockMap.keySet(), null);
            controllerBase.onStructureInvalid();
        }
    }

    private void onPosSelected(BlockPos pos, Direction facing) {
        if (index >= patterns.length || index < 0) return;
        TraceabilityPredicate predicate = patterns[index].predicateMap.get(pos);
        if (predicate != null) {
            predicates.clear();
            predicates.addAll(predicate.common);
            predicates.addAll(predicate.limited);
            predicates.removeIf(p -> p == null || p.candidates == null); // why it happens?
            if (candidates != null) {
                for (SlotWidget candidate : candidates) {
                    removeWidget(candidate);
                }
            }
            List<List<ItemStack>> candidateStacks = new ArrayList<>();
            List<List<Component>> predicateTips = new ArrayList<>();
            for (SimplePredicate simplePredicate : predicates) {
                List<ItemStack> itemStacks = simplePredicate.getCandidates();
                if (!itemStacks.isEmpty()) {
                    candidateStacks.add(itemStacks);
                    predicateTips.add(simplePredicate.getToolTips(predicate));
                }
            }
            candidates = new SlotWidget[candidateStacks.size()];
            CycleItemStackHandler itemHandler = new CycleItemStackHandler(candidateStacks);
            int maxCol = (160 - (((slotWidgets.length - 1) / 9 + 1) * 18) - 35) % 18;
            for (int i = 0; i < candidateStacks.size(); i++) {
                int finalI = i;
                if (isEMILoaded) {
                    candidates[i] = new PatternSlotWidget(itemHandler, i, 3 + (i / maxCol) * 18, 3 + (i % maxCol) * 18)
                            .setBackgroundTexture(new ColorRectTexture(0x4fffffff))
                            .setOnAddedTooltips((slot, list) -> list.addAll(predicateTips.get(finalI)));
                } else {
                    candidates[i] = new SlotWidget(itemHandler, i, 3 + (i / maxCol) * 18, 3 + (i % maxCol) * 18, false,
                            false)
                            .setIngredientIO(IngredientIO.INPUT)
                            .setBackgroundTexture(new ColorRectTexture(0x4fffffff))
                            .setOnAddedTooltips((slot, list) -> list.addAll(predicateTips.get(finalI)));
                }
                addWidget(candidates[i]);
            }
        }
    }

    private void loadControllerFormed(Collection<BlockPos> poses, IMultiController controllerBase, int index) {
        BlockPattern pattern;
        if (controllerBase instanceof IMultiStructureMachine machine) {
            pattern = machine.getMultiPattern().get(index);
        } else {
            pattern = controllerBase.getPattern();
        }
        if (pattern != null && pattern.checkPatternAt(controllerBase.getMultiblockState(), true)) {
            controllerBase.onStructureFormed();
        }
        if (controllerBase.isFormed()) {
            LongSet set = controllerBase.getMultiblockState().getMatchContext().getOrDefault("renderMask",
                    LongSets.EMPTY_SET);
            Set<BlockPos> modelDisabled = set.stream().map(BlockPos::of).collect(Collectors.toSet());
            if (!modelDisabled.isEmpty()) {
                sceneWidget.setRenderedCore(
                        poses.stream().filter(pos -> !modelDisabled.contains(pos)).collect(Collectors.toList()), null);
            } else {
                sceneWidget.setRenderedCore(poses, null);
            }
        } else {
            GTCEu.LOGGER.warn("Pattern formed checking failed: {}", controllerBase.self().getDefinition());
        }
    }

    private MBPattern initializePattern(IMultiblockMachineDefinition.Pattern pattern, int index) {
        Map<BlockPos, BlockInfo> blockMap = pattern.blockMap();
        IMultiController controllerBase = pattern.multiController();
        LEVEL.addBlocks(blockMap);
        if (controllerBase != null) {
            controllerBase.self().holder.getSelf().setLevel(LEVEL);
            LEVEL.setInnerBlockEntity(controllerBase.self().holder.getSelf());
        }
        Map<BlockPos, TraceabilityPredicate> predicateMap = controllerBase == null ? null : new HashMap<>();
        if (controllerBase != null) {
            loadControllerFormed(predicateMap.keySet(), controllerBase, index);
            predicateMap = controllerBase.getMultiblockState().getMatchContext().get("predicates");
        }
        return controllerBase == null ? null : new MBPattern(blockMap, pattern.parts(), predicateMap, controllerBase);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (!isLoaded && isEMILoaded && Minecraft.getInstance().screen instanceof RecipeScreen) {
            setPage();
            isLoaded = true;
        }
    }

    @Override
    public void drawInBackground(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.enableBlend();
        super.drawInBackground(graphics, mouseX, mouseY, partialTicks);
    }

    private static class MBPattern {

        @NotNull
        final List<ItemStack> parts;
        @NotNull
        final Map<BlockPos, TraceabilityPredicate> predicateMap;
        @NotNull
        final Map<BlockPos, BlockInfo> blockMap;
        @NotNull
        final IMultiController controllerBase;
        final int maxY, minY;

        MBPattern(@NotNull Map<BlockPos, BlockInfo> blockMap, @NotNull List<ItemStack> parts,
                  @NotNull Map<BlockPos, TraceabilityPredicate> predicateMap,
                  @NotNull IMultiController controllerBase) {
            this.parts = parts;
            this.blockMap = blockMap;
            this.predicateMap = predicateMap;
            this.controllerBase = controllerBase;
            int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
            for (BlockPos pos : blockMap.keySet()) {
                min = Math.min(min, pos.getY());
                max = Math.max(max, pos.getY());
            }
            minY = min;
            maxY = max;
        }
    }
}
