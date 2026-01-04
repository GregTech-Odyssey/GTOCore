package com.gtocore.common.machine.mana;

import com.gtolib.api.machine.SimpleNoEnergyMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.IWailaDisplayProvider;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import static com.gtocore.common.machine.mana.CelestialHandler.*;

public class CelestialCondenser extends SimpleNoEnergyMachine implements IWailaDisplayProvider {

    private final CelestialHandler celestialHandler;

    @Getter
    @Persisted
    private long solaris = 0;
    @Getter
    @Persisted
    private long lunara = 0;
    @Getter
    @Persisted
    private long voidflux = 0;
    @Getter
    @Persisted
    private long stellarm = 0;

    @Persisted
    private final long MAX_CAPACITY = 1000000;

    @Persisted
    private CelestialHandler.Mode mode = CelestialHandler.Mode.OVERWORLD;

    private int timing;
    private boolean clearSky;
    private TickableSubscription tickSubs;

    public CelestialCondenser(MetaMachineBlockEntity holder) {
        super(holder, 1, t -> 16000);
        this.celestialHandler = new CelestialHandler(MAX_CAPACITY);
    }

    @Override
    public boolean beforeWorking(@NotNull GTRecipe recipe) {
        int solarisCost = recipe.data.getInt(SOLARIS);
        int lunaraCost = recipe.data.getInt(LUNARA);
        int voidfluxCost = recipe.data.getInt(VOIDFLUX);
        int stellarmCost = recipe.data.getInt(STELLARM);
        int anyCost = recipe.data.getInt("any");

        Object[] deductResult = null;
        if (solarisCost > 0) {
            deductResult = celestialHandler.deductResource(SOLARIS, solarisCost, 1, solaris, lunara, voidflux, stellarm);
        } else if (lunaraCost > 0) {
            deductResult = celestialHandler.deductResource(LUNARA, lunaraCost, 1, solaris, lunara, voidflux, stellarm);
        } else if (voidfluxCost > 0) {
            deductResult = celestialHandler.deductResource(VOIDFLUX, voidfluxCost, 1, solaris, lunara, voidflux, stellarm);
        } else if (stellarmCost > 0) {
            deductResult = celestialHandler.deductResource(STELLARM, stellarmCost, 1, solaris, lunara, voidflux, stellarm);
        } else if (anyCost > 0) {
            deductResult = celestialHandler.deductResource("ANY", anyCost, 1, solaris, lunara, voidflux, stellarm);
        }

        if (deductResult == null || !(boolean) deductResult[0]) {
            return false;
        }
        this.solaris = (long) deductResult[1];
        this.lunara = (long) deductResult[2];
        this.voidflux = (long) deductResult[3];
        this.stellarm = (long) deductResult[4];

        return true;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.mode = celestialHandler.initMode(getLevel());
        if (!isRemote()) {
            tickSubs = subscribeServerTick(tickSubs, this::tickUpdate, 10);
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (tickSubs != null) {
            tickSubs.unsubscribe();
            tickSubs = null;
        }
    }

    private void tickUpdate() {
        Level world = getLevel();
        if (world == null) return;
        BlockPos pos = getPos();
        if (timing == 0) {
            getRecipeLogic().updateTickSubscription();
            clearSky = hasClearSky(world, pos);
            timing = 40;
        } else if (timing % 10 == 0) {
            clearSky = hasClearSky(world, pos);
            timing--;
        } else {
            timing--;
        }
        if (clearSky) {
            long[] updatedResources = celestialHandler.increase(world, 1, solaris, lunara, voidflux, stellarm, mode);
            this.solaris = updatedResources[0];
            this.lunara = updatedResources[1];
            this.voidflux = updatedResources[2];
            this.stellarm = updatedResources[3];
        }
    }

    private static boolean hasClearSky(Level world, BlockPos pos) {
        BlockPos checkPos = pos.above();
        if (!canSeeSky(world, pos)) return false;
        if (world.dimension().equals(Level.END)) return true;
        Biome biome = world.getBiome(checkPos).value();
        boolean hasPrecipitation = world.isRaining() && (biome.warmEnoughToRain(checkPos) || biome.coldEnoughToSnow(checkPos));
        return !hasPrecipitation;
    }

    private static boolean canSeeSky(Level world, BlockPos blockPos) {
        int maxY = world.getMaxBuildHeight();
        BlockPos.MutableBlockPos checkPos = blockPos.mutable().move(Direction.UP);
        while (checkPos.getY() < maxY) {
            if (!world.getBlockState(checkPos).getBlock().equals(Blocks.AIR)) return false;
            checkPos.move(Direction.UP);
        }
        return true;
    }

    @Override
    public void appendWailaTooltip(CompoundTag data, ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        long solaris = data.getLong(SOLARIS);
        long lunara = data.getLong(LUNARA);
        long voidflux = data.getLong(VOIDFLUX);
        long stellarm = data.getLong(STELLARM);
        long maxCapacity = data.getLong("max_capacity");
        if (solaris > 0) iTooltip.add(Component.translatable("gtocore.celestial_condenser.solaris", solaris + "/" + maxCapacity));
        if (lunara > 0) iTooltip.add(Component.translatable("gtocore.celestial_condenser.lunara", lunara + "/" + maxCapacity));
        if (voidflux > 0) iTooltip.add(Component.translatable("gtocore.celestial_condenser.voidflux", voidflux + "/" + maxCapacity));
        if (stellarm > 0) iTooltip.add(Component.translatable("gtocore.celestial_condenser.stellarm", stellarm + "/" + maxCapacity));
    }

    @Override
    public void appendWailaData(CompoundTag data, BlockAccessor blockAccessor) {
        data.putLong(SOLARIS, this.solaris);
        data.putLong(LUNARA, this.lunara);
        data.putLong(VOIDFLUX, this.voidflux);
        data.putLong(STELLARM, this.stellarm);
        data.putLong("max_capacity", this.MAX_CAPACITY);
    }
}
