package com.gto.gtocore.common.machine.multiblock.generator;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.TooltipsPanel;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.ITieredMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.chance.logic.ChanceLogic;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.logic.OCParams;
import com.gregtechceu.gtceu.api.recipe.logic.OCResult;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;

import com.lowdragmc.lowdraglib.side.fluid.FluidStack;

import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import lombok.Getter;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ChemicalEnergyDevourerMachine extends WorkableElectricMultiblockMachine implements ITieredMachine {

    private static final FluidStack DINITROGEN_TETROXIDE_STACK = GTMaterials.DinitrogenTetroxide.getFluid(480);
    private static final FluidStack LIQUID_OXYGEN_STACK = GTMaterials.Oxygen.getFluid(FluidStorageKeys.LIQUID, 320);
    private static final FluidStack LUBRICANT_STACK = GTMaterials.Lubricant.getFluid(10);

    @Getter
    private final int tier = 5;
    private boolean isOxygenBoosted = false;
    private boolean isDinitrogenTetroxideBoosted = false;

    public ChemicalEnergyDevourerMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    private boolean isIntakesObstructed() {
        var facing = this.getFrontFacing();
        boolean permuteXZ = facing.getAxis() == Direction.Axis.Z;
        var centerPos = this.getPos().relative(facing);
        for (int x = -3; x < 4; x++) {
            for (int y = -3; y < 4; y++) {
                if (x == 0 && y == 0)
                    continue;
                var blockPos = centerPos.offset(permuteXZ ? x : 0, y, permuteXZ ? 0 : x);
                var blockState = this.getLevel().getBlockState(blockPos);
                if (!blockState.isAir())
                    return true;
            }
        }
        return false;
    }

    public boolean isBoostAllowed() {
        return getMaxVoltage() >= GTValues.V[getTier() + 3];
    }

    //////////////////////////////////////
    // ****** Recipe Logic *******//
    //////////////////////////////////////

    @Override
    public long getOverclockVoltage() {
        if (isOxygenBoosted && isDinitrogenTetroxideBoosted)
            return GTValues.V[tier] * 64;
        else if (isOxygenBoosted)
            return GTValues.V[tier] * 32;
        else
            return GTValues.V[tier] * 16;
    }

    protected GTRecipe getLubricantRecipe() {
        return GTRecipeBuilder.ofRaw().inputFluids(LUBRICANT_STACK).buildRawRecipe();
    }

    protected GTRecipe getBoostRecipe() {
        return GTRecipeBuilder.ofRaw().inputFluids(LIQUID_OXYGEN_STACK).buildRawRecipe();
    }

    protected GTRecipe getBoostRecipea() {
        return GTRecipeBuilder.ofRaw().inputFluids(DINITROGEN_TETROXIDE_STACK).buildRawRecipe();
    }

    @Nullable
    public static GTRecipe recipeModifier(MetaMachine machine, @NotNull GTRecipe recipe, @NotNull OCParams params,
                                          @NotNull OCResult result) {
        if (machine instanceof ChemicalEnergyDevourerMachine engineMachine) {
            var EUt = RecipeHelper.getOutputEUt(recipe);
            if (EUt > 0 && engineMachine.getLubricantRecipe().matchRecipe(engineMachine).isSuccess() &&
                    !engineMachine.isIntakesObstructed()) {
                var maxParallel = (int) (engineMachine.getOverclockVoltage() / EUt);
                var parallelResult = GTRecipeModifiers.fastParallel(engineMachine, recipe, maxParallel, false);
                if (engineMachine.isOxygenBoosted && engineMachine.isDinitrogenTetroxideBoosted) {
                    recipe = parallelResult.getFirst() == recipe ? recipe.copy() : parallelResult.getFirst();
                    long eut = EUt * parallelResult.getSecond() * 4;
                    recipe.tickOutputs.put(EURecipeCapability.CAP, List.of(new Content(eut,
                            ChanceLogic.getMaxChancedValue(), ChanceLogic.getMaxChancedValue(), 0, null, null)));
                } else if (engineMachine.isOxygenBoosted) {
                    recipe = parallelResult.getFirst() == recipe ? recipe.copy() : parallelResult.getFirst();
                    long eut = EUt * parallelResult.getSecond() * 2;
                    recipe.tickOutputs.put(EURecipeCapability.CAP, List.of(new Content(eut,
                            ChanceLogic.getMaxChancedValue(), ChanceLogic.getMaxChancedValue(), 0, null, null)));
                } else {
                    recipe = parallelResult.getFirst();
                }
                return recipe;
            }
        }
        return null;
    }

    @Override
    public boolean onWorking() {
        boolean value = super.onWorking();
        val totalContinuousRunningTime = recipeLogic.getTotalContinuousRunningTime();
        if ((totalContinuousRunningTime == 1 || totalContinuousRunningTime % 72 == 0)) {
            if (!getLubricantRecipe().handleRecipeIO(IO.IN, this, this.recipeLogic.getChanceCaches())) {
                recipeLogic.interruptRecipe();
                return false;
            }
        }
        if ((totalContinuousRunningTime == 1 || totalContinuousRunningTime % 20 == 0) && isBoostAllowed()) {
            var boosterRecipe = getBoostRecipe();
            var boosterRecipea = getBoostRecipea();
            this.isOxygenBoosted = boosterRecipe.matchRecipe(this).isSuccess() &&
                    boosterRecipe.handleRecipeIO(IO.IN, this, this.recipeLogic.getChanceCaches());
            this.isDinitrogenTetroxideBoosted = boosterRecipea.matchRecipe(this).isSuccess() &&
                    boosterRecipea.handleRecipeIO(IO.IN, this, this.recipeLogic.getChanceCaches());
        }
        return value;
    }

    @Override
    public boolean dampingWhenWaiting() {
        return false;
    }

    //////////////////////////////////////
    // ******* GUI ********//
    //////////////////////////////////////

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if (isFormed()) {
            if (isBoostAllowed()) {
                if (isOxygenBoosted && isDinitrogenTetroxideBoosted) {
                    textList.add(Component.translatable(
                            "gtocore.machine.large_combustion_engine.Joint_boosted"));
                } else if (isOxygenBoosted) {
                    textList.add(Component.translatable(
                            "gtocore.machine.large_combustion_engine.supply_dinitrogen_tetroxide_to_boost"));
                    textList.add(Component.translatable(
                            "gtceu.multiblock.large_combustion_engine.liquid_oxygen_boosted"));
                } else {
                    textList.add(Component.translatable(
                            "gtceu.multiblock.large_combustion_engine.supply_liquid_oxygen_to_boost"));
                }
            } else {
                textList.add(Component.translatable(
                        "gtceu.multiblock.large_combustion_engine.boost_disallowed"));
            }
        }
    }

    @Override
    public void attachTooltips(TooltipsPanel tooltipsPanel) {
        super.attachTooltips(tooltipsPanel);
        tooltipsPanel.attachTooltips(new Basic(
                () -> GuiTextures.INDICATOR_NO_STEAM.get(false),
                () -> List.of(Component.translatable("gtceu.multiblock.large_combustion_engine.obstructed")
                        .setStyle(Style.EMPTY.withColor(ChatFormatting.RED))),
                this::isIntakesObstructed,
                () -> null));
    }
}
