package com.gtocore.common.machine.mana;

import com.gtolib.api.data.GTODimensions;
import com.gtolib.api.machine.SimpleNoEnergyMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.IWailaDisplayProvider;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import earth.terrarium.adastra.api.planets.PlanetApi;
import org.jetbrains.annotations.NotNull;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class CelestialCondenser extends SimpleNoEnergyMachine implements IWailaDisplayProvider {

    public static final String SOLARIS = "solaris";
    public static final String LUNARA = "lunara";
    public static final String VOIDFLUX = "voidflux";
    public static final String STELLARM = "stellarm";

    @Persisted
    private int solaris = 0;
    @Persisted
    private int lunara = 0;
    @Persisted
    private int voidflux = 0;
    @Persisted
    private int stellarm = 0;
    private static final int max_capacity = 1000000;
    private int timing;
    private boolean clearSky;
    private TickableSubscription tickSubs;

    public CelestialCondenser(MetaMachineBlockEntity holder) {
        super(holder, 1, t -> 16000);
    }

    @Override
    public boolean beforeWorking(@NotNull GTRecipe recipe) {
        int solarisCost = recipe.data.contains(SOLARIS) ? recipe.data.getInt(SOLARIS) : 0;
        int lunaraCost = recipe.data.contains(LUNARA) ? recipe.data.getInt(LUNARA) : 0;
        int voidfluxCost = recipe.data.contains(VOIDFLUX) ? recipe.data.getInt(VOIDFLUX) : 0;
        int stellarmCost = recipe.data.contains(STELLARM) ? recipe.data.getInt(STELLARM) : 0;
        int anyCost = recipe.data.contains("any") ? recipe.data.getInt("any") : 0;
        if (solarisCost > 0 && solarisCost > this.solaris) return false;
        else if (lunaraCost > 0 && lunaraCost > this.lunara) return false;
        else if (voidfluxCost > 0 && voidfluxCost > this.voidflux) return false;
        else if (stellarmCost > 0 && stellarmCost > this.stellarm) return false;
        else if (anyCost > 0 && anyCost > this.solaris && anyCost > this.lunara && anyCost > this.voidflux && anyCost > this.stellarm) return false;

        if (!super.beforeWorking(recipe)) return false;

        if (solarisCost > 0) this.solaris -= solarisCost;
        else if (lunaraCost > 0) this.lunara -= lunaraCost;
        else if (voidfluxCost > 0) this.voidflux -= voidfluxCost;
        else if (stellarmCost > 0) this.stellarm -= stellarmCost;
        else if (anyCost > 0) {
            if (this.solaris >= anyCost) this.solaris -= anyCost;
            else if (this.lunara >= anyCost) this.lunara -= anyCost;
            else if (this.voidflux >= anyCost) this.voidflux -= anyCost;
            else this.stellarm -= anyCost;
        }

        return true;
    }

    @Override
    public void onLoad() {
        super.onLoad();
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
            timing = 20;
        } else if (timing % 5 == 0) {
            clearSky = hasClearSky(world, pos);
            timing--;
        } else {
            timing--;
        }
        if (clearSky) increase(world);
    }

    private void increase(Level world) {
        ResourceLocation dimLocation = world.dimension().location();

        // 太空维度
        if (PlanetApi.API.isSpace(getLevel())) {
            stellarm = Math.min(max_capacity, stellarm + 20);
        }
        // Void维度：solaris 和 lunara 各加5
        else if (GTODimensions.isVoid(dimLocation)) {
            solaris = Math.min(max_capacity, solaris + 5);
            lunara = Math.min(max_capacity, lunara + 5);
        }
        // OTHERSIDE维度：voidflux 加50
        else if (GTODimensions.OTHERSIDE.equals(dimLocation)) {
            voidflux = Math.min(max_capacity, voidflux + 50);
        }
        // ALFHEIM维度：白天 solaris 20，黑夜 lunara + 20
        else if (GTODimensions.ALFHEIM.equals(dimLocation)) {
            if (world.isDay()) {
                solaris = Math.min(max_capacity, solaris + 20);
            } else if (world.isNight()) {
                lunara = Math.min(max_capacity, lunara + 20);
            }
        }
        // 主世界/末地的资源增加逻辑
        else if (world.dimension().equals(Level.END)) {
            voidflux = Math.min(max_capacity, voidflux + 10);
        } else if (world.isDay()) {
            solaris = Math.min(max_capacity, solaris + 10);
        } else if (world.isNight()) {
            lunara = Math.min(max_capacity, lunara + 10);
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
        int solaris = data.getInt(SOLARIS);
        int lunara = data.getInt(LUNARA);
        int voidflux = data.getInt(VOIDFLUX);
        int stellarm = data.getInt(STELLARM);
        int maxCapacity = data.getInt("max_capacity");

        if (solaris > 0) {
            iTooltip.add(Component.translatable("gtocore.celestial_condenser.solaris", (solaris + "/" + maxCapacity)));
        }
        if (lunara > 0) {
            iTooltip.add(Component.translatable("gtocore.celestial_condenser.lunara", (lunara + "/" + maxCapacity)));
        }
        if (voidflux > 0) {
            iTooltip.add(Component.translatable("gtocore.celestial_condenser.voidflux", (voidflux + "/" + maxCapacity)));
        }
        if (stellarm > 0) {
            iTooltip.add(Component.translatable("gtocore.celestial_condenser.stellarm", (stellarm + "/" + maxCapacity)));
        }
    }

    @Override
    public void appendWailaData(CompoundTag data, BlockAccessor blockAccessor) {
        data.putInt(SOLARIS, solaris);
        data.putInt(LUNARA, lunara);
        data.putInt(VOIDFLUX, voidflux);
        data.putInt(STELLARM, stellarm);
        data.putInt("max_capacity", max_capacity);
    }
}
