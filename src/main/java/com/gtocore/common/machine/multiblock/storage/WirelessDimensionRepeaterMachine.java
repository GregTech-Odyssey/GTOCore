package com.gtocore.common.machine.multiblock.storage;

import com.gtolib.api.GTOValues;
import com.gtolib.api.capability.IExtendWirelessEnergyContainerHolder;
import com.gtolib.api.machine.feature.multiblock.ITierCasingMachine;
import com.gtolib.api.machine.multiblock.NoRecipeLogicMultiblockMachine;
import com.gtolib.api.machine.trait.TierCasingTrait;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import com.hepdd.gtmthings.api.misc.WirelessEnergyContainer;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public final class WirelessDimensionRepeaterMachine extends NoRecipeLogicMultiblockMachine implements IMachineLife, IExtendWirelessEnergyContainerHolder, ITierCasingMachine {

    private WirelessEnergyContainer WirelessEnergyContainerCache;
    private final TierCasingTrait tierCasingTrait;

    public WirelessDimensionRepeaterMachine(MetaMachineBlockEntity holder) {
        super(holder);
        tierCasingTrait = new TierCasingTrait(this, GTOValues.INTEGRAL_FRAMEWORK_TIER);
        setWorkingEnabled(false);
    }

    @Override
    public void setWorkingEnabled(boolean isWorkingAllowed) {
        if (isWorkingAllowed && isFormed()) {
            loadContainer();
        } else {
            unloadContainer();
        }
        super.setWorkingEnabled(isWorkingAllowed);
    }

    private void loadContainer() {
        if (isRemote()) return;
        Level level = getLevel();
        if (level == null) return;
        var container = getWirelessEnergyContainer();
        if (container == null) return;
        container.getDimension().put(level.dimension().location(), getCasingTier(GTOValues.INTEGRAL_FRAMEWORK_TIER));
    }

    private void unloadContainer() {
        if (isRemote()) return;
        Level level = getLevel();
        if (level == null) return;
        var container = getWirelessEnergyContainer();
        if (container == null) return;
        container.getDimension().put(level.dimension().location(), 0);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        loadContainer();
    }

    @Override
    public void onStructureInvalid() {
        unloadContainer();
        super.onStructureInvalid();
    }

    @Override
    public void onUnload() {
        unloadContainer();
        super.onUnload();
    }

    @Override
    public void onMachinePlaced(@Nullable LivingEntity player, ItemStack stack) {
        if (player != null) {
            setOwnerUUID(player.getUUID());
        }
    }

    @Override
    public Object2IntMap<String> getCasingTiers() {
        return tierCasingTrait.getCasingTiers();
    }

    @Override
    @Nullable
    public UUID getUUID() {
        return getOwnerUUID();
    }

    @Override
    public void setWirelessEnergyContainerCache(final WirelessEnergyContainer WirelessEnergyContainerCache) {
        this.WirelessEnergyContainerCache = WirelessEnergyContainerCache;
    }

    @Override
    public WirelessEnergyContainer getWirelessEnergyContainerCache() {
        return this.WirelessEnergyContainerCache;
    }
}
