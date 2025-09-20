package com.gtocore.common.machine.multiblock.generator;

import com.gtocore.common.machine.multiblock.part.InfiniteIntakeHatchPartMachine;

import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.modifier.ParallelLogic;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.TooltipsPanel;
import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.api.transfer.fluid.IFluidHandlerModifiable;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class CombustionEngineMachine extends ElectricMultiblockMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(CombustionEngineMachine.class, ElectricMultiblockMachine.MANAGED_FIELD_HOLDER);
    private static final FluidStack OXYGEN_STACK = GTMaterials.Oxygen.getFluid(20);
    private static final FluidStack LIQUID_OXYGEN_STACK = GTMaterials.Oxygen.getFluid(FluidStorageKeys.LIQUID, 80);
    private static final FluidStack LUBRICANT_STACK = GTMaterials.Lubricant.getFluid(1);
    private final int tier;
    // runtime
    private boolean isOxygenBoosted;
    @Persisted
    private final NotifiableFluidTank tank;
    private final ConditionalSubscriptionHandler tankSubs;

    public CombustionEngineMachine(MetaMachineBlockEntity holder, int tier) {
        super(holder);
        this.tier = tier;
        this.tank = new NotifiableFluidTank(this, 1, 128000, IO.IN, IO.NONE);
        tankSubs = new ConditionalSubscriptionHandler(this, this::intake, () -> isFormed && !isIntakesObstructed());
    }

    @Override
    @Nullable
    public IItemHandlerModifiable getItemHandlerCap(@Nullable Direction side, boolean useCoverCapability) {
        return null;
    }

    @Override
    @Nullable
    public IFluidHandlerModifiable getFluidHandlerCap(@Nullable Direction side, boolean useCoverCapability) {
        return null;
    }

    private void intake() {
        if (getOffsetTimer() % 20 == 0) {
            var fluid = InfiniteIntakeHatchPartMachine.AIR_MAP.get(getLevel().dimension().location());
            if (fluid == null) {
                tankSubs.unsubscribe();
                return;
            }
            tank.fillInternal(new FluidStack(fluid, (formedCount * 8000) + 8000), IFluidHandler.FluidAction.EXECUTE);
            tankSubs.updateSubscription();
        }
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public void setWorkingEnabled(boolean workingEnabled) {
        super.setWorkingEnabled(workingEnabled);
        tankSubs.updateSubscription();
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        tankSubs.initialize(getLevel());
    }

    private boolean isIntakesObstructed() {
        if (getLevel() == null) return false;
        Direction facing = getFrontFacing();
        boolean permuteXZ = facing.getAxis() == Direction.Axis.Z;
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                if (x == 0 && y == 0) continue;
                if (!getLevel().getBlockState(getPos().relative(facing).offset(permuteXZ ? x : 0, y, permuteXZ ? 0 : x)).isAir()) return true;
            }
        }
        return false;
    }

    private boolean isExtreme() {
        return tier > GTValues.EV;
    }

    private boolean isBoostAllowed() {
        return getMaxVoltage() >= GTValues.V[tier + 1];
    }

    //////////////////////////////////////
    // ****** Recipe Logic *******//
    //////////////////////////////////////
    @Override
    public long getOverclockVoltage() {
        if (isOxygenBoosted) return GTValues.V[tier] << (2 + formedCount);
        else return GTValues.V[tier] << (1 + formedCount);
    }

    @Nullable
    @Override
    protected Recipe getRealRecipe(Recipe recipe) {
        long EUt = recipe.getOutputEUt();
        if (EUt > 0 && notConsumableFluid(LUBRICANT_STACK) && !isIntakesObstructed()) {
            recipe = ParallelLogic.accurateParallel(this, recipe, getOverclockVoltage() / EUt);
            if (recipe == null) return null;
            if (isOxygenBoosted) {
                recipe.setOutputEUt((long) (recipe.getOutputEUt() * (isExtreme() ? 2 : 1.5)));
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
            if (!inputFluid(LUBRICANT_STACK)) {
                return false;
            }
        }
        if ((totalContinuousRunningTime == 1 || totalContinuousRunningTime % 20 == 0) && isBoostAllowed()) {
            isOxygenBoosted = inputFluid(isExtreme() ? LIQUID_OXYGEN_STACK : OXYGEN_STACK);
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
            if (!isExtreme()) {
                if (isOxygenBoosted) {
                    textList.add(Component.translatable("gtceu.multiblock.large_combustion_engine.oxygen_boosted"));
                } else {
                    textList.add(Component.translatable("gtceu.multiblock.large_combustion_engine.supply_oxygen_to_boost"));
                }
            } else {
                if (isOxygenBoosted) {
                    textList.add(Component.translatable("gtceu.multiblock.large_combustion_engine.liquid_oxygen_boosted"));
                } else {
                    textList.add(Component.translatable("gtceu.multiblock.large_combustion_engine.supply_liquid_oxygen_to_boost"));
                }
            }
        } else {
            textList.add(Component.translatable("gtceu.multiblock.large_combustion_engine.boost_disallowed"));
        }
    }

    @Override
    public void attachTooltips(TooltipsPanel tooltipsPanel) {
        super.attachTooltips(tooltipsPanel);
        tooltipsPanel.attachTooltips(new Basic(() -> GuiTextures.INDICATOR_NO_STEAM.get(false), () -> List.of(Component.translatable("gtceu.multiblock.large_combustion_engine.obstructed").setStyle(Style.EMPTY.withColor(ChatFormatting.RED))), this::isIntakesObstructed, () -> null));
    }

    @Override
    public int getTier() {
        return this.tier;
    }
}
