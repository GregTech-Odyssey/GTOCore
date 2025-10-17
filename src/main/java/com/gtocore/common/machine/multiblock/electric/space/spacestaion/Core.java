package com.gtocore.common.machine.multiblock.electric.space.spacestaion;

import com.gtocore.api.machine.part.ILargeSpaceStationMachine;
import com.gtocore.common.machine.multiblock.IWirelessDimensionProvider;

import com.gtolib.api.GTOValues;
import com.gtolib.api.capability.IIWirelessInteractor;
import com.gtolib.api.machine.trait.CustomRecipeLogic;
import com.gtolib.api.machine.trait.TierCasingTrait;
import com.gtolib.api.recipe.Recipe;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.collection.O2OOpenCacheHashMap;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraftforge.fluids.FluidStack;

import com.hepdd.gtmthings.api.misc.WirelessEnergyContainer;
import earth.terrarium.adastra.api.planets.PlanetApi;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.gregtechceu.gtceu.api.GTValues.IV;
import static com.gregtechceu.gtceu.api.GTValues.VA;
import static com.gregtechceu.gtceu.common.data.GTMaterials.DistilledWater;
import static com.gtocore.common.data.GTOMaterials.FlocculationWasteSolution;
import static com.gtolib.utils.ServerUtils.getServer;

public class Core extends AbstractSpaceStation implements ILargeSpaceStationMachine, IWirelessDimensionProvider {

    public static final Map<ResourceLocation, Set<Core>> NETWORK = new O2OOpenCacheHashMap<>();

    private @Nullable CleanroomProvider provider = null;

    private final Set<ILargeSpaceStationMachine> subMachinesFlat;
    private WirelessEnergyContainer WirelessEnergyContainerCache;
    private final TierCasingTrait tierCasingTrait;

    private boolean dirty = false;

    public boolean isDirty() {
        return dirty;
    }

    @Override
    public void markDirty(boolean dirty) {
        this.dirty = dirty;
    }

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
    protected void tickReady() {
        super.tickReady();
        if (getOffsetTimer() % 40 == 0) {
            updateSpaceMachines();
        }
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        onFormed();
        IIWirelessInteractor.addToNet(NETWORK, this);
        markDirty(true);
        loadContainer();
    }

    @Override
    public void onUnload() {
        unloadContainer();
        IIWirelessInteractor.removeFromNet(NETWORK, this);
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
        IIWirelessInteractor.removeFromNet(NETWORK, this);
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
        list.add(Component.translatable("gui.ae2.PowerUsageRate", "%s EU/t".formatted(FormattingUtil.formatNumbers(getEUt()))).withStyle(ChatFormatting.YELLOW));
        list.add(Component.translatable("gtocore.machine.spacestation.energy_consumption.total", FormattingUtil.formatNumbers(Optional.ofNullable(getRecipeLogic().getLastRecipe()).map(GTRecipe::getInputEUt).orElse(0L))).withStyle(ChatFormatting.GOLD));
        list.add(Component.translatable("gtocore.machine.modules_amount", subMachinesFlat.size()));
    }

    private void removeAllSubMachines() {
        for (ILargeSpaceStationMachine m : subMachinesFlat) {
            if (m != this && m.getRoot() == this) {
                m.setRoot(null);
            }
        }
        subMachinesFlat.clear();
    }

    /// 很吃性能的操作，使用dirty标记需要更新
    private void refreshModules() {
        removeAllSubMachines();
        provider = null;
        Set<ILargeSpaceStationMachine> its = new ReferenceOpenHashSet<>(getConnectedModules());
        while (!its.isEmpty()) {
            var it = its.iterator();
            ILargeSpaceStationMachine m = it.next();
            it.remove();
            if (m.getRoot() != null) continue;
            m.setRoot(this);
            if (m instanceof CleanroomProvider p && provider == null) {
                provider = p;
            }
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
        if (dirty) {
            refreshModules();
            dirty = false;
        }
        long EUt = getEUt();
        for (ILargeSpaceStationMachine machine : subMachinesFlat) {
            EUt += machine.getEUt();
            if (machine instanceof IRecipeLogicMachine r) r.getRecipeLogic().updateTickSubscription();
        }
        return getRecipeBuilder().duration(20).EUt(EUt)
                .inputFluids(inputFluids(subMachinesFlat.size() + 1))
                .outputFluids(FlocculationWasteSolution.getFluid(30 * (subMachinesFlat.size() + 1)))
                .buildRawRecipe();
    }

    private static FluidStack[] inputFluids(int mul) {
        return new FluidStack[] {
                DistilledWater.getFluid(15 * mul),
                GTMaterials.RocketFuel.getFluid(10 * mul),
                GTMaterials.Air.getFluid(100 * mul)
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

    @Override
    public Set<CleanroomType> getTypes() {
        if (provider == null) {
            return Collections.emptySet();
        }
        return provider.getTypes();
    }
}
