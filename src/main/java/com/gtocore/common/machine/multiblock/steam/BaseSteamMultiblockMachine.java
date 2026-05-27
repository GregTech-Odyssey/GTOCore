package com.gtocore.common.machine.multiblock.steam;

import com.gtocore.common.machine.multiblock.part.LargeSteamHatchPartMachine;

import com.gtolib.utils.MathUtil;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.machine.feature.ICleanroomProvider;
import com.gregtechceu.gtceu.api.machine.feature.IDummyEnergyMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.CleanroomMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.SteamHatchPartMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.steam.SteamParallelMultiblockMachine;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BaseSteamMultiblockMachine extends SteamParallelMultiblockMachine {

    protected int maxOCamount;
    private int euMultiplier;

    @Persisted
    private int amountOC;

    private final long eut;
    private final double durationMultiplier;

    public BaseSteamMultiblockMachine(MetaMachineBlockEntity holder, int maxParallels, int eut, double durationMultiplier) {
        super(holder, maxParallels);
        this.eut = eut;
        this.durationMultiplier = durationMultiplier;
    }

    BaseSteamMultiblockMachine(MetaMachineBlockEntity holder, int maxParallels, double durationMultiplier) {
        this(holder, maxParallels, 32, durationMultiplier);
    }

    boolean oc() {
        return false;
    }

    @Override
    public void onStructureFormed() {
        energyContainer = IEnergyContainer.DEFAULT;
        super.onStructureFormed();
        maxOCamount = 0;
        euMultiplier = 0;
        for (var part : getParts()) {
            if (part instanceof SteamHatchPartMachine machine) {
                var conversionRate = 2D;
                var fluid = GTMaterials.Steam.getFluid(1);
                if (machine instanceof LargeSteamHatchPartMachine partMachine) {
                    conversionRate = partMachine.c;
                    fluid = partMachine.f;
                    euMultiplier = partMachine.o;
                    if (oc()) maxOCamount = partMachine.o;
                }
                energyContainer = new EnergyContainer(fluid, conversionRate, machine.tank);
                return;
            }
        }
    }

    @Override
    public int getTier() {
        return GTUtil.getTierByVoltage(this.eut << euMultiplier);
    }

    @Override
    protected void addSteamEnergy() {}

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        energyContainer = IEnergyContainer.DEFAULT;
    }

    @Nullable
    @Override
    protected GTRecipe getRealRecipe(RecipeHandlerUnit unit, GTRecipe recipe) {
        long eut = recipe.getInputEUt();
        if (eut <= (this.eut << euMultiplier)) {
            recipe = ParallelLogic.accurateParallel(this, unit, recipe, getMaxParallels());
            if (recipe == null) return null;
            recipe.duration = (int) (recipe.duration * durationMultiplier);
            if (maxOCamount > 0) {
                eut *= recipe.parallels;
                recipe.eut = eut << (amountOC << 1);
                recipe.duration = Math.max(1, recipe.duration / (1 << amountOC));
            }
            return recipe;
        }
        return null;
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if (isFormed() && maxOCamount > 0) {
            textList.add(Component.translatable("gtocore.machine.oc_amount", amountOC)
                    .withStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            Component.translatable("gtocore.machine.steam_parallel_machine.oc")))));

            textList.add(Component.translatable("gtocore.machine.steam_parallel_machine.modification_oc")
                    .append(ComponentPanelWidget.withButton(Component.literal("[-] "), "ocSub"))
                    .append(ComponentPanelWidget.withButton(Component.literal("[+]"), "ocAdd")));
        }
    }

    @Override
    public void handleDisplayClick(String componentData, ClickData clickData) {
        if (!clickData.isRemote && maxOCamount > 0) {
            amountOC = Mth.clamp(amountOC + ("ocAdd".equals(componentData) ? 1 : -1), 0, maxOCamount);
        }
    }

    @Override
    public void setCleanroom(@Nullable ICleanroomProvider provider) {
        if (provider instanceof CleanroomMachine) return;
        super.setCleanroom(provider);
    }

    private static class EnergyContainer extends IDummyEnergyMachine.DummyContainer {

        private final FluidStack steam;

        private final double conversionRate;
        private final NotifiableFluidTank steamTank;

        private EnergyContainer(FluidStack steam, double conversionRate, NotifiableFluidTank steamTank) {
            super(Integer.MAX_VALUE);
            this.steam = steam;
            this.conversionRate = conversionRate;
            this.steamTank = steamTank;
        }

        @Override
        public long changeEnergy(long differenceAmount) {
            differenceAmount = -differenceAmount;
            int totalSteam = Math.max(1, MathUtil.saturatedCast((long) (differenceAmount * conversionRate)));
            var steam = this.steam.copy();
            steam.setAmount(totalSteam);
            var leftSteam = steamTank.drainInternal(steam, IFluidHandler.FluidAction.EXECUTE).getAmount();
            if (leftSteam == totalSteam) return -differenceAmount;
            differenceAmount = (long) (leftSteam / conversionRate);
            return -differenceAmount;
        }

        @Override
        public long getEnergyStored() {
            return (long) (steamTank.getFluidInTank(0).getAmount() / conversionRate);
        }
    }
}
