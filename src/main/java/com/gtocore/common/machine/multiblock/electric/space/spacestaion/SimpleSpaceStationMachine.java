package com.gtocore.common.machine.multiblock.electric.space.spacestaion;

import com.gtocore.api.pattern.GTOPredicates;
import com.gtocore.client.forge.ForgeClientEvent;

import com.gtolib.api.recipe.RecipeBuilder;

import com.gregtechceu.gtceu.api.block.IFilterType;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.handler.ICustomRecipeLogicHolder;
import com.gregtechceu.gtceu.api.recipe.handler.IO;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import com.gto.datasynclib.annotations.SaveToDisk;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import earth.terrarium.adastra.api.planets.PlanetApi;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.gregtechceu.gtceu.api.GTValues.EV;
import static com.gregtechceu.gtceu.api.GTValues.VA;
import static com.gregtechceu.gtceu.common.data.GTMaterials.DistilledWater;
import static com.gtocore.common.data.GTOMaterials.FlocculationWasteSolution;

public class SimpleSpaceStationMachine extends AbstractSpaceStation implements ICustomRecipeLogicHolder {

    @Nullable
    private Set<BlockPos> outputDistilledWaterHatches;
    @Nullable
    private List<RecipeHandlerUnit> outputDistilledWaterHatchesList;
    /// 空间站附赠超净间
    @Nullable
    private CleanroomType cleanroomType = null;

    @SaveToDisk
    private int waterAmountPerHatch = 8;

    public SimpleSpaceStationMachine(MetaMachineBlockEntity metaMachineBlockEntity) {
        super(metaMachineBlockEntity);
    }

    @Override
    public void addHandlerList(RecipeHandlerUnit handler) {
        if (outputDistilledWaterHatches != null && outputDistilledWaterHatches.contains(handler.part.self().getPos()) && handler.handlerIO == IO.OUT) {
            if (outputDistilledWaterHatchesList == null) {
                outputDistilledWaterHatchesList = new ArrayList<>();
            }
            outputDistilledWaterHatchesList.add(handler);
            return;
        }
        super.addHandlerList(handler);
    }

    /// 超净间太空版
    /// @see com.gregtechceu.gtceu.common.machine.multiblock.electric.CleanroomMachine

    /// @see com.gregtechceu.gtceu.common.machine.multiblock.electric.CleanroomMachine#onStructureFormed()
    @Override
    public void onStructureFormed() {
        this.outputDistilledWaterHatches = getMultiblockState().getMatchContext().getOrDefault(GTOPredicates.DataKeys.SPACE_MACHINE_PHOTOVOLTAIC_SUPP, Collections.emptySet());
        super.onStructureFormed();
        IFilterType filterType = getMultiblockState().getMatchContext().get(Predicates.DataKey.FILTER_TYPE);
        if (filterType != null) {
            this.cleanroomType = filterType.getCleanroomType();
        } else {
            this.cleanroomType = CleanroomType.CLEANROOM;
        }
        onFormed();
    }

    /// @see com.gregtechceu.gtceu.common.machine.multiblock.electric.CleanroomMachine#onStructureInvalid()
    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        this.outputDistilledWaterHatches = null;
        this.outputDistilledWaterHatchesList = null;
        onInvalid();
    }

    @Override
    public List<ForgeClientEvent.HighlightNeed> getCustomHighlights() {
        BlockPos corner0 = getPos()
                .relative(getFrontFacing(), 0)
                .above(3)
                .relative(getFrontFacing().getClockWise(), 3);
        BlockPos corner1 = getPos()
                .relative(getFrontFacing(), 29)
                .below(3)
                .relative(getFrontFacing().getCounterClockWise(), 3);
        return List.of(new ForgeClientEvent.HighlightNeed(corner0, corner1, ChatFormatting.GRAY.getColor()));
    }

    public List<Component> getHighlightText() {
        return List.of(Component.translatable("tooltip.ad_astra.oxygen_distribution_area"));
    }

    private static RecipeBuilder inputFluids(RecipeBuilder builder) {
        builder.inputFluids(DistilledWater, 15);
        builder.inputFluids(GTMaterials.RocketFuel, 10);
        builder.inputFluids(GTMaterials.Air, 100);
        return builder;
    }

    @Override
    public void customText(@NotNull List<Component> list) {
        super.customText(list);
        list.add(Component.translatable("gtocore.machine.simple_spacestation.distilled_water", waterAmountPerHatch).append(ComponentPanelWidget.withButton(Component.literal(" [-]"), "Sub")).append(ComponentPanelWidget.withButton(Component.literal(" [+]"), "Add")));
    }

    @Override
    public void handleDisplayClick(String componentData, ClickData clickData) {
        if (!clickData.isRemote) {
            int delta = (clickData.isCtrlClick ? 64 : 1) * (clickData.isShiftClick ? 8 : 1);
            switch (componentData) {
                case "Add" -> {
                    waterAmountPerHatch += delta;
                    waterAmountPerHatch = Math.min(waterAmountPerHatch, 1000);
                }
                case "Sub" -> {
                    waterAmountPerHatch -= delta;
                    waterAmountPerHatch = Math.max(waterAmountPerHatch, 0);
                }
                default -> super.handleDisplayClick(componentData, clickData);
            }
        }
    }

    @Override
    public void onWorking() {
        var time = getOffsetTimer();
        if (time % 20 == 0) {

            if (firstLoad() || time % 400 == 0) provideOxygen();

            /// Distilled Water distribution
            if (waterAmountPerHatch > 0 && outputDistilledWaterHatchesList != null && !outputDistilledWaterHatchesList.isEmpty()) {
                for (var handler : outputDistilledWaterHatchesList) {
                    if (handler.simulateOutputFluid(DistilledWater.getFluid(), waterAmountPerHatch) && inputFluid(DistilledWater.getFluid(), waterAmountPerHatch)) {
                        handler.outputFluid(DistilledWater.getFluid(), waterAmountPerHatch);
                    }
                }
            }
        }
        super.onWorking();
    }

    @Override
    public Set<CleanroomType> getTypes() {
        return this.cleanroomType == null ? Set.of() : Set.of(this.cleanroomType);
    }

    @Override
    public GTRecipeDefinition createCustomRecipe(RecipeHandlerUnit unit) {
        if (!PlanetApi.API.isSpace(getLevel())) return null;
        return inputFluids(getRecipeBuilder().duration(200).EUt(VA[EV]))
                .outputFluids(FlocculationWasteSolution.getFluid(30))
                .build();
    }
}
