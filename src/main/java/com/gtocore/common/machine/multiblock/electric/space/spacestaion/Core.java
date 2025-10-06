package com.gtocore.common.machine.multiblock.electric.space.spacestaion;

import com.gtocore.api.machine.part.ILargeSpaceStationMachine;
import com.gtocore.common.machine.multiblock.IWirelessDimensionProvider;

import com.gtolib.api.GTOValues;
import com.gtolib.api.machine.trait.CustomRecipeLogic;
import com.gtolib.api.machine.trait.TierCasingTrait;
import com.gtolib.api.recipe.Recipe;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.TickTask;
import net.minecraftforge.fluids.FluidStack;

import com.hepdd.gtmthings.api.misc.WirelessEnergyContainer;
import earth.terrarium.adastra.api.planets.PlanetApi;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.DistilledWater;
import static com.gtocore.common.data.GTOMaterials.FlocculationWasteSolution;
import static com.gtolib.utils.ServerUtils.getServer;

public class Core extends AbstractSpaceStation implements ILargeSpaceStationMachine, IWirelessDimensionProvider {

    private final Set<ILargeSpaceStationMachine> subMachinesFlat;
    private WirelessEnergyContainer WirelessEnergyContainerCache;
    private final TierCasingTrait tierCasingTrait;

    public Core(MetaMachineBlockEntity metaMachineBlockEntity) {
        super(metaMachineBlockEntity);
        this.subMachinesFlat = new ObjectOpenHashSet<>();
        tierCasingTrait = new TierCasingTrait(this, GTOValues.INTEGRAL_FRAMEWORK_TIER);
    }

    @Override
    public Core getRoot() {
        return this;
    }

    @Override
    public void setRoot(Core root) {}

    @Override
    public boolean onWorking() {
        onWork();
        return super.onWorking();
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        onFormed();
        loadContainer();
    }

    @Override
    public void onUnload() {
        unloadContainer();
        super.onUnload();
    }

    @Override
    public boolean isWorkingEnabled() {
        return true;
    }

    @Override
    public void setWorkingEnabled(boolean ignored) {}

    private void delayedUnload() {
        if (!isRemote()) {
            getServer().tell(new TickTask(200, () -> {
                if (getHolder().hasLevel() && !isFormed()) unloadContainer();
            }));
        }
    }

    @Override
    public void onStructureInvalid() {
        delayedUnload();
        super.onStructureInvalid();
        onInvalid();
    }

    @Override
    public void onMachineRemoved() {
        super.onMachineRemoved();
        removeAllSubMachines();
    }

    @Override
    public void customText(@NotNull List<Component> list) {
        super.customText(list);
        list.add(Component.translatable("gui.ae2.PowerUsageRate", "%s EU/t".formatted(getEUt())).withStyle(ChatFormatting.YELLOW));
        list.add(Component.translatable("gtocore.machine.spacestation.energy_consumption.total", Optional.ofNullable(getRecipeLogic().getLastRecipe()).map(GTRecipe::getInputEUt).orElse(0L)).withStyle(ChatFormatting.GOLD));
        list.add(Component.translatable("gtocore.machine.modules_amount", subMachinesFlat.size()));
    }

    public void removeAllSubMachines() {
        for (ILargeSpaceStationMachine m : subMachinesFlat) {
            if (m != this) {
                m.setRoot(null);
            }
        }
        subMachinesFlat.clear();
    }

    public void refreshModules() {
        removeAllSubMachines();
        Set<ILargeSpaceStationMachine> its = new ObjectOpenHashSet<>();
        its.addAll(getConnectedModules());
        while (its.iterator().hasNext()) {
            ILargeSpaceStationMachine m = its.iterator().next();
            its.remove(m);
            m.setRoot(this);
            if (subMachinesFlat.add(m)) {
                its.addAll(m.getConnectedModules());
            }
        }
    }

    @Override
    public Set<BlockPos> getModulePositions() {
        var pos = getPos();
        var fFacing = getFrontFacing();
        var uFacing = RelativeDirection.UP.getRelative(fFacing, getUpwardsFacing(), false);
        var thirdAxis = RelativeDirection.RIGHT.getRelative(fFacing, getUpwardsFacing(), false);
        return Set.of(pos.relative(fFacing, 38).relative(uFacing, 6).relative(thirdAxis, 2),
                pos.relative(fFacing, 38).relative(uFacing, 6).relative(thirdAxis.getOpposite(), 2),
                pos.relative(fFacing, 38).relative(uFacing, 4),
                pos.relative(fFacing, 38).relative(uFacing, 8));
    }

    @Override
    public ConnectType getConnectType() {
        return ConnectType.CORE;
    }

    @Override
    @NotNull
    public RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CustomRecipeLogic(this, this::getRecipe, false);
    }

    @Override
    public Recipe getRecipe() {
        if (!PlanetApi.API.isSpace(getLevel()))
            return null;

        refreshModules();
        long EUt = getEUt();
        for (ILargeSpaceStationMachine machine : subMachinesFlat) {
            EUt += machine.getEUt();
            if (machine instanceof IRecipeLogicMachine r) r.getRecipeLogic().updateTickSubscription();
        }
        return getRecipeBuilder().duration(40).EUt(EUt)
                .inputFluids(inputFluids(subMachinesFlat.size() + 1))
                .outputFluids(FlocculationWasteSolution.getFluid(30 * (subMachinesFlat.size() + 1)))
                .buildRawRecipe();
    }

    private FluidStack[] inputFluids(int mul) {
        return new FluidStack[] {
                DistilledWater.getFluid(30 * mul),
                GTMaterials.RocketFuel.getFluid(20 * mul),
                GTMaterials.Air.getFluid(200 * mul)
        };
    }

    @Override
    public long getEUt() {
        return VA[IV];
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
