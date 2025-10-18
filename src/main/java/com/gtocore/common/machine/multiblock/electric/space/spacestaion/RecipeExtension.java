package com.gtocore.common.machine.multiblock.electric.space.spacestaion;

import com.gtocore.api.machine.part.GTOPartAbility;
import com.gtocore.common.data.machines.SpaceMultiblock;
import com.gtocore.common.machine.multiblock.part.OverclockHatchPartMachine;
import com.gtocore.common.machine.multiblock.part.ThreadHatchPartMachine;
import com.gtocore.common.machine.multiblock.part.WirelessEnergyInterfacePartMachine;

import com.gtolib.api.machine.multiblock.ICrossRecipeLogicMachine;
import com.gtolib.api.machine.trait.CrossRecipeTrait;
import com.gtolib.api.machine.trait.IEnhancedRecipeLogic;
import com.gtolib.api.recipe.ContentBuilder;
import com.gtolib.api.recipe.IdleReason;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.utils.MachineUtils;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.machine.feature.ICleanroomProvider;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.utils.collection.O2OOpenCustomCacheHashMap;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import earth.terrarium.adastra.api.planets.PlanetApi;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.Function;
import java.util.function.ToLongFunction;

public class RecipeExtension extends Extension implements ICrossRecipeLogicMachine {

    private boolean hasLaserInput = false;
    private final Set<Recipe> lastRecipes = new ReferenceOpenHashSet<>();
    private int lastItems = 16;
    private int lastFluids = 16;
    private int outputColor = -1;
    private final Object2ObjectOpenCustomHashMap<Content, Content> itemMap = new O2OOpenCustomCacheHashMap<>(ContentBuilder.HASH_STRATEGY);
    private final Object2ObjectOpenCustomHashMap<Content, Content> fluidMap = new O2OOpenCustomCacheHashMap<>(ContentBuilder.HASH_STRATEGY);
    private long availableParallel;
    private long lastParallel;
    private ThreadHatchPartMachine threadHatchPartMachine;
    private OverclockHatchPartMachine overclockHatchPartMachine;
    @Persisted
    private double totalEu;
    @Persisted
    protected final CrossRecipeTrait crossRecipeTrait;

    @NotNull
    private ToLongFunction<RecipeExtension> parallel = MachineUtils::getHatchParallel;

    private boolean hasLaserInput = false;

    public RecipeExtension(MetaMachineBlockEntity metaMachineBlockEntity) {
        super(metaMachineBlockEntity);
        crossRecipeTrait = new CrossRecipeTrait(this, false, machine -> parallel.applyAsLong((RecipeExtension) machine));
    }

    public RecipeExtension(MetaMachineBlockEntity metaMachineBlockEntity, @Nullable Function<AbstractSpaceStation, Set<BlockPos>> positionFunction) {
        super(metaMachineBlockEntity, positionFunction);
        crossRecipeTrait = new CrossRecipeTrait(this, false, machine -> parallel.applyAsLong((RecipeExtension) machine));
    }

    @Override
    protected boolean beforeWorking(@Nullable Recipe recipe) {
        if (!isWorkspaceReady()) {
            setIdleReason(IdleReason.CANNOT_WORK_IN_SPACE);
            return false;
        }
        if (hasLaserInput && !core.canUseLaser()) {
            ((IEnhancedRecipeLogic) getRecipeLogic())
                    .gtolib$setIdleReason(Component.translatable("gtocore.machine.spacestation.require_module", Component.translatable(SpaceMultiblock.SPACE_STATION_ENERGY_CONVERSION_MODULE.getDescriptionId())));
            return false;
        }

        return super.beforeWorking(recipe);
    }

    @Override
    public void onPartScan(@NotNull IMultiPart iMultiPart) {
        super.onPartScan(iMultiPart);
        if (hasLaserInput) return;
        for (var partAbility : new PartAbility[] {
                PartAbility.INPUT_LASER, GTOPartAbility.OVERCLOCK_HATCH, GTOPartAbility.THREAD_HATCH }) {
            if (partAbility.isApplicable(iMultiPart.self().getBlockState().getBlock()))
                hasLaserInput = true;
        }
    }

    @Override
    public void onStructureFormed() {
        hasLaserInput = false;
        super.onStructureFormed();
    }

    @Override
    public @Nullable ICleanroomProvider getCleanroom() {
        return this;
    }

    public void setParallel(@NotNull ToLongFunction<RecipeExtension> parallel) {
        this.parallel = parallel;
    }

    @Override
    public void attachConfigurators(@NotNull ConfiguratorPanel configuratorPanel) {
        super.attachConfigurators(configuratorPanel);
        ICrossRecipeLogicMachine.super.attachConfigurators(configuratorPanel);
    }

    @Override
    public Recipe getRecipe() {
        if (!PlanetApi.API.isSpace(getLevel()))
            return null;
        if (getRoot() == null || !getRoot().isWorkspaceReady())
            return null;

        return ICrossRecipeLogicMachine.super.getRecipe();
    }

    @Override
    public Recipe getRealRecipe(@NotNull Recipe recipe) {
        return ICrossRecipeLogicMachine.super.getRealRecipe(recipe);
    }

    @Override
    public Set<Recipe> getLastRecipes() {
        return lastRecipes;
    }

    @Override
    public WirelessEnergyInterfacePartMachine getEnergyInterfacePartMachine() {
        return null;
    }

    @Override
    public double getTotalEu() {
        return totalEu;
    }

    @Override
    public int getLastItems() {
        return lastItems;
    }

    @Override
    public void setLastItems(int i) {
        lastItems = i;
    }

    @Override
    public int getLastFluids() {
        return lastFluids;
    }

    @Override
    public void setLastFluids(int i) {
        lastFluids = i;
    }

    @Override
    public int getOutputColor() {
        return outputColor;
    }

    @Override
    public void setOutputColor(int i) {
        outputColor = i;
    }

    @Override
    public Object2ObjectOpenCustomHashMap<Content, Content> getItemMap() {
        return itemMap;
    }

    @Override
    public Object2ObjectOpenCustomHashMap<Content, Content> getFluidMap() {
        return fluidMap;
    }

    @Override
    public long getAvailableParallel() {
        return availableParallel;
    }

    @Override
    public void setAvailableParallel(long l) {
        this.availableParallel = l;
    }

    @Override
    public void setMaxParallel(long l) {}

    @Override
    public long getLastParallel() {
        return lastParallel;
    }

    @Override
    public void setLastParallel(long l) {
        lastParallel = l;
    }

    @Override
    public ThreadHatchPartMachine getThreadHatchPartMachine() {
        return threadHatchPartMachine;
    }

    @Override
    public void setThreadHatchPartMachine(ThreadHatchPartMachine threadHatchPartMachine) {
        this.threadHatchPartMachine = threadHatchPartMachine;
    }

    @Override
    public OverclockHatchPartMachine getOverclockHatchPartMachine() {
        return overclockHatchPartMachine;
    }

    @Override
    public void setOverclockHatchPartMachine(OverclockHatchPartMachine overclockHatchPartMachine) {
        this.overclockHatchPartMachine = overclockHatchPartMachine;
    }

    @Override
    public void setEnergyInterfacePartMachine(WirelessEnergyInterfacePartMachine wirelessEnergyInterfacePartMachine) {}

    @Override
    public void setTotalEu(double v) {
        this.totalEu = v;
    }

    @Override
    public CrossRecipeTrait getCustomParallelTrait() {
        return crossRecipeTrait;
    }

    @Override
    public boolean isInfinite() {
        return false;
    }

    @Override
    public boolean isHatchParallel() {
        return true;
    }
}
