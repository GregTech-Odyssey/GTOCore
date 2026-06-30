package com.gtocore.common.machine.multiblock.water;

import com.gtolib.api.capability.IIWirelessInteractor;
import com.gtolib.api.machine.feature.multiblock.IParallelMachine;
import com.gtolib.api.machine.multiblock.NoEnergyCustomParallelMultiblockMachine;
import com.gtolib.utils.GTOUtils;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.api.sound.SoundEntry;
import com.gregtechceu.gtceu.common.data.GTSoundEntries;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.Level;

import com.gto.datasynclib.annotations.SaveToDisk;
import org.jetbrains.annotations.NotNull;

@MethodsReturnNonnullByDefault
abstract class WaterPurificationUnitMachine extends NoEnergyCustomParallelMultiblockMachine implements IIWirelessInteractor<WaterPurificationPlantMachine> {

    abstract long prepareRecipe(RecipeHandlerUnit unit);

    private WaterPurificationPlantMachine netMachineCache;
    GTRecipe recipe;
    RecipeHandlerUnit unit;
    @SaveToDisk
    long eut;
    public final long multiple;
    private final ConditionalSubscriptionHandler tickSubs;

    WaterPurificationUnitMachine(MetaMachineBlockEntity holder, long multiple) {
        super(holder, m -> IParallelMachine.MAX_PARALLEL, m -> 1000L);
        this.multiple = multiple;
        tickSubs = new ConditionalSubscriptionHandler(this, this::tickUpdate, 80, this::isFormed);
        customParallelTrait.setDefaultMax(false);
    }

    private void tickUpdate() {
        WaterPurificationPlantMachine machine = getNetMachine();
        if (machine == null) getRecipeLogic().resetRecipeLogic();
        tickSubs.updateSubscription();
    }

    void calculateVoltage(long input) {
        eut = input * multiple / 2;
    }

    long parallel() {
        WaterPurificationPlantMachine machine = getNetMachine();
        if (machine == null) {
            return 0;
        }
        return Math.min(super.getParallel(), (machine.availableEu << 1) / multiple);
    }

    void setWorking(boolean isWorkingAllowed) {
        super.setWorkingEnabled(isWorkingAllowed);
    }

    @Override
    public void onContentChanges(RecipeHandlerUnit handlerList) {
        if (getRecipeLogic().isIdle()) {
            WaterPurificationPlantMachine machine = getNetMachine();
            if (machine != null && machine.getRecipeLogic().isIdle()) {
                machine.getRecipeLogic().updateTickSubscription();
            }
        }
    }

    @Override
    public Class<WaterPurificationPlantMachine> getProviderClass() {
        return WaterPurificationPlantMachine.class;
    }

    @Override
    public boolean firstTestMachine(WaterPurificationPlantMachine machine) {
        Level level = machine.getLevel();
        if (level != null && isFormed() && machine.isFormed() && GTOUtils.calculateDistance(machine.getPos(), getPos()) < 32) {
            machine.waterPurificationUnitMachineMap.put(this, getRecipeLogic().isWorking());
            return true;
        }
        return false;
    }

    @Override
    public boolean testMachine(WaterPurificationPlantMachine machine) {
        return isFormed() && machine.isFormed();
    }

    @Override
    public void removeNetMachineCache() {
        if (netMachineCache != null) {
            netMachineCache.waterPurificationUnitMachineMap.removeBoolean(this);
            netMachineCache = null;
        }
    }

    @Override
    public void onStructureFormed() {
        unit = null;
        super.onStructureFormed();
        if (!isRemote()) {
            getNetMachine();
            tickSubs.initialize(getLevel());
        }
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        removeNetMachineCache();
        unit = null;
    }

    @Override
    public void onUnload() {
        super.onUnload();
        tickSubs.unsubscribe();
        removeNetMachineCache();
    }

    @Override
    public SoundEntry getSound() {
        return GTSoundEntries.COOLING;
    }

    @Override
    public void setWorkingEnabled(boolean isWorkingAllowed) {}

    @Override
    public RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CustomLogic(this);
    }

    private static final class CustomLogic extends RecipeLogic {

        private CustomLogic(WaterPurificationUnitMachine machine) {
            super(machine);
        }

        @Override
        public boolean findAndHandleRecipe() {
            return false;
        }

        @Override
        public void updateTickSubscription() {}

        @Override
        public void serverTick() {}

        @Override
        public boolean onRecipeFinish() {
            machine.afterWorking();
            if (lastRecipe != null) {
                machine.handleRecipeOutput(lastRecipe);
                lastRecipe = null;
            }
            if (suspendAfterFinish) {
                setStatus(SUSPEND);
                suspendAfterFinish = false;
            } else {
                setStatus(IDLE);
            }
            progress = 0;
            duration = 0;
            isActive = false;
            return false;
        }
    }

    @Override
    public void setNetMachineCache(final WaterPurificationPlantMachine netMachineCache) {
        this.netMachineCache = netMachineCache;
    }

    @Override
    public WaterPurificationPlantMachine getNetMachineCache() {
        return this.netMachineCache;
    }
}
