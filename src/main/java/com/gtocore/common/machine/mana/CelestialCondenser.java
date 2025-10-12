package com.gtocore.common.machine.mana;

import com.gtolib.api.machine.SimpleNoEnergyMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;

public class CelestialCondenser extends SimpleNoEnergyMachine {

    @Persisted
    private int solaris = 0;
    @Persisted
    private int lunara = 0;
    @Persisted
    private int voidflux = 0;
    private final int max_capacity = 1000000;
    private int timing;
    private boolean clearSky;
    private TickableSubscription tickSubs;

    public CelestialCondenser(MetaMachineBlockEntity holder) {
        super(holder, 1, t -> 16000);
    }

    @Override
    public boolean beforeWorking(GTRecipe recipe) {
        if (recipe == null) return false;
        int solarisCost = recipe.data.contains("solaris") ? recipe.data.getInt("solaris") : 0;
        int lunaraCost = recipe.data.contains("lunara") ? recipe.data.getInt("lunara") : 0;
        int voidfluxCost = recipe.data.contains("voidflux") ? recipe.data.getInt("voidflux") : 0;
        int anyCost = recipe.data.contains("any") ? recipe.data.getInt("any") : 0;
        if (solarisCost > 0) if (solarisCost > this.solaris) return false;
        else if (lunaraCost > 0) if (lunaraCost > this.lunara) return false;
        else if (voidfluxCost > 0) if (voidfluxCost > this.voidflux) return false;
        else if (anyCost > 0) if (this.solaris < anyCost && this.lunara < anyCost && this.voidflux < anyCost) return false;

        if (!super.beforeWorking(recipe)) return false;

        if (solarisCost > 0) this.solaris -= solarisCost;
        else if (lunaraCost > 0) this.lunara -= lunaraCost;
        else if (voidfluxCost > 0) this.voidflux -= voidfluxCost;
        else if (anyCost > 0) {
            if (this.solaris >= anyCost) this.solaris -= anyCost;
            else if (this.lunara >= anyCost) this.lunara -= anyCost;
            else this.voidflux -= anyCost;
        }

        return true;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!isRemote()) {
            tickSubs = subscribeServerTick(tickSubs, this::tickUpdate);
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
        if (getOffsetTimer() % 10 == 0) {
            if (timing == 0) {
                getRecipeLogic().updateTickSubscription();
                clearSky = hasClearSky(world, pos);
                timing = 20;
            } else if (timing % 5 == 0) {
                clearSky = hasClearSky(world, pos);
                timing--;
            } else {
                timing--;
            }
            if (clearSky) increase(world);
        }
    }

    private void increase(Level world) {
        int sky = 0;
        if (world.dimension().equals(Level.END)) sky = 3;
        else if (world.isDay()) sky = 1;
        else if (world.isNight()) sky = 2;
        switch (sky) {
            case 1 -> solaris = Math.min(max_capacity, solaris + 10);
            case 2 -> lunara = Math.min(max_capacity, lunara + 10);
            case 3 -> voidflux = Math.min(max_capacity, voidflux + 10);
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

    public int getSolaris() {
        return solaris;
    }

    public int getLunara() {
        return lunara;
    }

    public int getVoidflux() {
        return voidflux;
    }

    public int getMaxMapacity() {
        return max_capacity;
    }
}
