package com.gtocore.common.machine.multiblock.electric.space.spacestaion;

import com.gtocore.client.forge.ForgeClientEvent;

import com.gtolib.api.machine.trait.CustomRecipeLogic;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeRunner;
import com.gtolib.api.recipe.ingredient.FastFluidIngredient;
import com.gtolib.utils.MachineUtils;

import com.gregtechceu.gtceu.api.block.IFilterType;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.*;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fluids.FluidStack;

import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import earth.terrarium.adastra.api.planets.PlanetApi;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.gregtechceu.gtceu.api.GTValues.EV;
import static com.gregtechceu.gtceu.api.GTValues.VA;
import static com.gregtechceu.gtceu.common.data.GTMaterials.DistilledWater;
import static com.gtocore.common.data.GTOMaterials.FlocculationWasteSolution;

public class SimpleSpaceStationMachine extends AbstractSpaceStation {

    @Nullable
    private Collection<BlockPos> outputDistilledWaterHatches;
    @Nullable
    private ObjectArrayList<RecipeHandlerList> outputDistilledWaterHatchesList;
    /// 空间站附赠超净间
    @Nullable
    private CleanroomType cleanroomType = null;

    @Persisted
    private int waterAmountPerHatch = 8;

    public SimpleSpaceStationMachine(MetaMachineBlockEntity metaMachineBlockEntity) {
        super(metaMachineBlockEntity);
    }

    @Override
    public void addHandlerList(RecipeHandlerList handler) {
        if (outputDistilledWaterHatches != null && outputDistilledWaterHatches.stream().anyMatch(p -> handler.part.getPos() == p)) {
            if (outputDistilledWaterHatchesList == null) {
                outputDistilledWaterHatchesList = new ObjectArrayList<>();
            }
            outputDistilledWaterHatchesList.add(handler);
            if (handler.getHandlerIO() == IO.OUT) return;
        }
        super.addHandlerList(handler);
    }

    /// 超净间太空版
    /// @see com.gregtechceu.gtceu.common.machine.multiblock.electric.CleanroomMachine

    /// @see com.gregtechceu.gtceu.common.machine.multiblock.electric.CleanroomMachine#onStructureFormed()
    @Override
    public void onStructureFormed() {
        this.outputDistilledWaterHatches = getMultiblockState().getMatchContext().getOrDefault("spaceMachinePhotovoltaicSupp", Collections.emptyList());
        super.onStructureFormed();
        IFilterType filterType = getMultiblockState().getMatchContext().get("FilterType");
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

    private Recipe getRecipe() {
        if (!PlanetApi.API.isSpace(getLevel()))
            return null;
        return getRecipeBuilder().duration(200).EUt(VA[EV])
                .inputFluids(inputFluids)
                .outputFluids(FlocculationWasteSolution.getFluid(30))
                .buildRawRecipe();
    }

    private static final FluidStack[] inputFluids = new FluidStack[] {
            DistilledWater.getFluid(15),
            GTMaterials.RocketFuel.getFluid(10),
            GTMaterials.Air.getFluid(100)
    };

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
    public boolean onWorking() {
        if (getOffsetTimer() % 20 == 0) {

            provideOxygen();

            /// Distilled Water distribution
            if (waterAmountPerHatch > 0 && outputDistilledWaterHatchesList != null && !outputDistilledWaterHatchesList.isEmpty()) {
                for (RecipeHandlerList handler : outputDistilledWaterHatchesList) {
                    IRecipeCapabilityHolder waterHolder = new IRecipeCapabilityHolder() {

                        @Override
                        public @NotNull Map<IO, List<RecipeHandlerList>> getCapabilitiesProxy() {
                            return Map.of(IO.OUT, Collections.singletonList(handler));
                        }

                        @Override
                        public @NotNull Map<IO, Map<RecipeCapability<?>, List<IRecipeHandler<?>>>> getCapabilitiesFlat() {
                            return Map.of(IO.OUT, handler.handlerMap);
                        }
                    };
                    if (RecipeRunner.handleContent(
                            waterHolder,
                            IO.OUT,
                            ObjectArrayList.of(FastFluidIngredient.of(DistilledWater.getFluid(waterAmountPerHatch))), FluidRecipeCapability.CAP, true, false) &&
                            inputFluid(DistilledWater.getFluid(waterAmountPerHatch))) {

                        MachineUtils.outputFluid(waterHolder, DistilledWater.getFluid(waterAmountPerHatch));
                    }
                }
            }
        }
        return super.onWorking();
    }

    @Override
    @NotNull
    public RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CustomRecipeLogic(this, this::getRecipe, true);
    }

    @Override
    public Set<CleanroomType> getTypes() {
        return this.cleanroomType == null ? Set.of() : Set.of(this.cleanroomType);
    }
}
