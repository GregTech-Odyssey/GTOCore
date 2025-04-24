package com.gto.gtocore.common.machine.multiblock.generator;

import com.gto.gtocore.api.machine.multiblock.ElectricMultiblockMachine;
import com.gto.gtocore.api.recipe.ContentBuilder;
import com.gto.gtocore.api.recipe.GTORecipeBuilder;
import com.gto.gtocore.api.recipe.RecipeRunnerHelper;
import com.gto.gtocore.common.data.GTORecipeModifiers;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.TooltipsPanel;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraftforge.fluids.FluidStack;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class ChemicalEnergyDevourerMachine extends ElectricMultiblockMachine {

    private static final FluidStack DINITROGEN_TETROXIDE_STACK = GTMaterials.DinitrogenTetroxide.getFluid(480);
    private static final FluidStack LIQUID_OXYGEN_STACK = GTMaterials.Oxygen.getFluid(FluidStorageKeys.LIQUID, 320);
    private static final FluidStack LUBRICANT_STACK = GTMaterials.Lubricant.getFluid(10);

    @Getter
    private final int tier = 5;
    private boolean isOxygenBoosted;
    private boolean isDinitrogenTetroxideBoosted;

    public ChemicalEnergyDevourerMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    private boolean isIntakesObstructed() {
        if (getLevel() == null) return false;
        Direction facing = getFrontFacing();
        boolean permuteXZ = facing.getAxis() == Direction.Axis.Z;
        for (int x = -3; x < 4; x++) {
            for (int y = -3; y < 4; y++) {
                if (x == 0 && y == 0) continue;
                if (!getLevel().getBlockState(getPos().relative(facing).offset(permuteXZ ? x : 0, y, permuteXZ ? 0 : x)).isAir())
                    return true;
            }
        }
        return false;
    }

    private boolean isBoostAllowed() {
        return getMaxVoltage() >= GTValues.V[tier + 3];
    }

    //////////////////////////////////////
    // ****** Recipe Logic *******//
    //////////////////////////////////////

    @Override
    public long getOverclockVoltage() {
        if (isOxygenBoosted && isDinitrogenTetroxideBoosted)
            return GTValues.V[tier] << 6;
        else if (isOxygenBoosted)
            return GTValues.V[tier] << 5;
        else
            return GTValues.V[tier] << 4;
    }

    private static GTRecipe getLubricantRecipe() {
        return GTORecipeBuilder.ofRaw().inputFluids(LUBRICANT_STACK).buildRawRecipe();
    }

    private static GTRecipe getBoostRecipe() {
        return GTORecipeBuilder.ofRaw().inputFluids(LIQUID_OXYGEN_STACK).buildRawRecipe();
    }

    private static GTRecipe getBoostRecipea() {
        return GTORecipeBuilder.ofRaw().inputFluids(DINITROGEN_TETROXIDE_STACK).buildRawRecipe();
    }

    @Nullable
    @Override
    protected GTRecipe getRealRecipe(GTRecipe recipe) {
        var EUt = RecipeHelper.getOutputEUt(recipe);
        if (EUt > 0 && RecipeRunnerHelper.matchRecipe(this, getLubricantRecipe()) && !isIntakesObstructed()) {
            recipe = GTORecipeModifiers.accurateParallel(this, recipe, (int) (getOverclockVoltage() / EUt));
            if (isOxygenBoosted && isDinitrogenTetroxideBoosted) {
                recipe.tickOutputs.put(EURecipeCapability.CAP, List.of(ContentBuilder.builderLong(EUt * recipe.parallels * 4)));
            } else if (isOxygenBoosted) {
                recipe.tickOutputs.put(EURecipeCapability.CAP, List.of(ContentBuilder.builderLong(EUt * recipe.parallels * 2)));
            }
            return recipe;
        }
        return null;
    }

    @Override
    public boolean onWorking() {
        if (!super.onWorking()) return false;
        long totalContinuousRunningTime = recipeLogic.getTotalContinuousRunningTime();
        if ((totalContinuousRunningTime == 1 || totalContinuousRunningTime % 72 == 0)) {
            if (!RecipeRunnerHelper.handleRecipeInput(this, getLubricantRecipe())) {
                return false;
            }
        }
        if ((totalContinuousRunningTime == 1 || totalContinuousRunningTime % 20 == 0) && isBoostAllowed()) {
            var boosterRecipe = getBoostRecipe();
            var boosterRecipea = getBoostRecipea();
            isOxygenBoosted = RecipeRunnerHelper.matchRecipe(this, boosterRecipe) && RecipeRunnerHelper.handleRecipeInput(this, boosterRecipe);
            isDinitrogenTetroxideBoosted = RecipeRunnerHelper.matchRecipe(this, boosterRecipea) && RecipeRunnerHelper.handleRecipeInput(this, boosterRecipea);
        }
        return true;
    }

    //////////////////////////////////////
    // ******* GUI ********//
    //////////////////////////////////////

    @Override
    public void customText(List<Component> textList) {
        super.customText(textList);
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
