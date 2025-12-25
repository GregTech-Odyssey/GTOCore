package com.gtocore.common.machine.multiblock.electric;

import com.gtocore.api.machine.IMultiFluidRendererMachine;

import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.api.recipe.Recipe;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.material.Fluid;

import com.fast.fastcollection.OpenCacheHashSet;
import com.google.common.collect.*;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class PigmentMixer extends ElectricMultiblockMachine implements IMultiFluidRendererMachine {

    @DescSynced
    final Set<BlockPos> cachedYellowOffsets = new OpenCacheHashSet<>();
    @DescSynced
    final Set<BlockPos> cachedCyanOffsets = new OpenCacheHashSet<>();
    @DescSynced
    final Set<BlockPos> cachedMagentaOffsets = new OpenCacheHashSet<>();
    @DescSynced
    final Set<BlockPos> cachedBlackOffsets = new OpenCacheHashSet<>();
    @DescSynced
    final Set<BlockPos> cachedWhiteOffsets = new OpenCacheHashSet<>();

    public PigmentMixer(MetaMachineBlockEntity metaMachineBlockEntity) {
        super(metaMachineBlockEntity);
    }

    @Override
    protected boolean beforeWorking(@NotNull Recipe recipe) {
        cachedYellowOffsets.addAll(getMultiblockState().getMatchContext().getOrDefault("yellow", new OpenCacheHashSet<>()));
        cachedCyanOffsets.addAll(getMultiblockState().getMatchContext().getOrDefault("cyan", new OpenCacheHashSet<>()));
        cachedMagentaOffsets.addAll(getMultiblockState().getMatchContext().getOrDefault("magenta", new OpenCacheHashSet<>()));
        cachedBlackOffsets.addAll(getMultiblockState().getMatchContext().getOrDefault("black", new OpenCacheHashSet<>()));
        cachedWhiteOffsets.addAll(getMultiblockState().getMatchContext().getOrDefault("white", new OpenCacheHashSet<>()));
        return super.beforeWorking(recipe);
    }

    @Override
    public void afterWorking() {
        super.afterWorking();
        invalid();
    }

    private void invalid() {
        cachedYellowOffsets.clear();
        cachedCyanOffsets.clear();
        cachedMagentaOffsets.clear();
        cachedBlackOffsets.clear();
        cachedWhiteOffsets.clear();
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        invalid();
    }

    @Override
    public Multimap<Fluid, BlockPos> getFluidBlockOffsets() {
        Multimap<Fluid, BlockPos> map = Multimaps.newMultimap(new Reference2ObjectOpenHashMap<>(), OpenCacheHashSet::new);
        map.putAll(Wrapper.Yellow, cachedYellowOffsets);
        map.putAll(Wrapper.Cyan, cachedCyanOffsets);
        map.putAll(Wrapper.Magenta, cachedMagentaOffsets);
        map.putAll(Wrapper.Black, cachedBlackOffsets);
        map.putAll(Wrapper.White, cachedWhiteOffsets);
        return map;
    }

    private static class Wrapper {

        public static final Fluid Yellow = GTMaterials.DyeYellow.getFluid();
        public static final Fluid Cyan = GTMaterials.DyeCyan.getFluid();
        public static final Fluid Magenta = GTMaterials.DyeMagenta.getFluid();
        public static final Fluid Black = GTMaterials.DyeBlack.getFluid();
        public static final Fluid White = GTMaterials.DyeWhite.getFluid();
    }
}
