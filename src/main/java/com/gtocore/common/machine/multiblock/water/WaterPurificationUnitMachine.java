package com.gtocore.common.machine.multiblock.water;

import com.gtolib.api.capability.IIWirelessInteractor;
import com.gtolib.api.machine.feature.multiblock.IParallelMachine;
import com.gtolib.api.machine.multiblock.NoEnergyCustomParallelMultiblockMachine;
import com.gtolib.api.machine.trait.CustomRecipeLogic;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.utils.GTOUtils;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.sound.SoundEntry;
import com.gregtechceu.gtceu.common.data.GTSoundEntries;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

@MethodsReturnNonnullByDefault
abstract class WaterPurificationUnitMachine extends NoEnergyCustomParallelMultiblockMachine implements IIWirelessInteractor<WaterPurificationPlantMachine> {

    abstract long before();

    private WaterPurificationPlantMachine netMachineCache;
    Recipe recipe;
    @Persisted
    long eut;
    public final long multiple;
    private final ConditionalSubscriptionHandler tickSubs;

    WaterPurificationUnitMachine(MetaMachineBlockEntity holder, long multiple) {
        super(holder, false, m -> IParallelMachine.MAX_PARALLEL);
        this.multiple = multiple;
        tickSubs = new ConditionalSubscriptionHandler(this, this::tickUpdate, this::isFormed);
        customParallelTrait.setDefaultMax(false);
    }

    private void tickUpdate() {
        if (getOffsetTimer() % 80 == 0) {
            WaterPurificationPlantMachine machine = getNetMachine();
            if (machine == null) getRecipeLogic().resetRecipeLogic();
            tickSubs.updateSubscription();
        }
    }

    void calculateVoltage(long input) {
        eut = input * multiple / 2;
    }

    long parallel() {
        WaterPurificationPlantMachine machine = getNetMachine();
        if (machine != null) {
            var p = super.getParallel();
            if (p < 1000) {
                p = 1000;
                super.setParallel(p);
            }
            p = Math.min(p, (machine.availableEu << 1) / multiple);
            return p >= 1000 ? p : 0;
        }
        return 0;
    }

    void setWorking(boolean isWorkingAllowed) {
        super.setWorkingEnabled(isWorkingAllowed);
    }

    @Override
    public void onContentChanges(RecipeHandlerList handlerList) {
        if (getRecipeLogic().isIdle()) {
            WaterPurificationPlantMachine machine = getNetMachine();
            if (machine != null && machine.getRecipeLogic().isIdle()) {
                machine.getRecipeLogic().updateTickSubscription();
            }
        }
    }

    @Override
    public Map<ResourceLocation, Set<WaterPurificationPlantMachine>> getMachineNet() {
        return WaterPurificationPlantMachine.NETWORK;
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
    @NotNull
    public RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CustomLogic(this);
    }

    private static final class CustomLogic extends CustomRecipeLogic {

        private CustomLogic(WaterPurificationUnitMachine machine) {
            super(machine, () -> null);
        }

        @Override
        public void updateTickSubscription() {}

        @Override
        public void serverTick() {}

        @Override
        public void onRecipeFinish() {
            super.onRecipeFinish();
            lastRecipe = null;
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
