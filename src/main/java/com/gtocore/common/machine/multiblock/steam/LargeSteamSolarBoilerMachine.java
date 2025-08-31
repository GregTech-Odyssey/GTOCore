package com.gtocore.common.machine.multiblock.steam;

import com.gtocore.common.data.GTOBlocks;

import com.gtolib.api.annotation.Scanned;
import com.gtolib.api.annotation.dynamic.DynamicInitialValue;
import com.gtolib.api.annotation.dynamic.DynamicInitialValueTypes;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeBuilder;
import com.gtolib.api.recipe.RecipeRunner;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.feature.IExplosionMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IDisplayUIMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.config.ConfigHolder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.gregtechceu.gtceu.api.machine.multiblock.PartAbility.*;
import static com.gregtechceu.gtceu.api.pattern.Predicates.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.Steam;
import static com.gregtechceu.gtceu.common.data.GTMaterials.Water;
import static com.gtolib.GTOCore.*;

@Scanned
public class LargeSteamSolarBoilerMachine extends WorkableMultiblockMachine implements IExplosionMachine, IDisplayUIMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(LargeSteamSolarBoilerMachine.class, WorkableMultiblockMachine.MANAGED_FIELD_HOLDER);

    private static final TraceabilityPredicate INNER_PREDICATE = new TraceabilityPredicate(
            blockWorldState -> true, null, null) {

        @Override
        public boolean testOnly() {
            return true;
        }

        @Override
        public boolean isAny() {
            return false;
        }

        @Override
        public boolean isAir() {
            return false;
        }
    };

    @DynamicInitialValue(key = "gtocore.machine.large_steam_solar_boiler", typeKey = DynamicInitialValueTypes.KEY_MULTIPLY, simpleValue = "30", normalValue = "18", expertValue = "10", cn = "基础蒸汽产率 : %s / t", cnComment = """
             根据集热管数量决定蒸汽产量，只有在阳光下才会运行。
            大小 ：3×3 到 13×13.""", en = "Basic steam production : %s / t", enComment = """
            The steam production is determined by the number of heat collector tubes and it only operates when there is sunlight.
            Size: 3×3 to 13×13.""")
    private static int basicSteamProduction = 10;

    private static final int MAX_LR_DIST = 5, MAX_B_DIST = 11;
    private static final int MIN_LR_DIST = 1, MIN_B_DIST = 3;
    private static final int SUNLIT_CHECK_INTERVAL = 40;
    private static final int STEAM_GENERATION_INTERVAL = 20;

    @Persisted
    private int lDist, rDist, bDist, sunlit;
    private int steamGenerated;
    @Nullable
    private TickableSubscription tickSubs;
    private Recipe currentRecipe;
    private boolean shouldStartNextRecipe = true, needNewRecipe = true;

    public LargeSteamSolarBoilerMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public @NotNull ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        setupMachineState(true);
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        setupMachineState(false);
    }

    private void setupMachineState(boolean isFormed) {
        shouldStartNextRecipe = isFormed;
        needNewRecipe = isFormed;
        if (getLevel() instanceof ServerLevel serverLevel) serverLevel.getServer().tell(new TickTask(0, this::updateTickSubscription));
    }

    @Override
    public void onUnload() {
        if (tickSubs != null) {
            tickSubs.unsubscribe();
            tickSubs = null;
        }
        super.onUnload();
    }

    protected void updateTickSubscription() {
        if (isFormed()) {
            if (tickSubs == null || !tickSubs.isStillSubscribed())
                tickSubs = subscribeServerTick(this::updateSolarCollection);
        } else if (tickSubs != null) {
            tickSubs.unsubscribe();
            tickSubs = null;
        }
    }

    public void updateStructureDimensions() {
        Level world = getLevel();
        if (world == null) {
            resetStructure();
            return;
        }

        Direction front = getFrontFacing();
        Direction back = front.getOpposite();
        Direction left = front.getCounterClockWise();
        Direction right = left.getOpposite();

        int newBDist = calculateDistance(world, getPos(), back, MAX_B_DIST);
        int newLDist = calculateDistance(world, getPos().relative(back), left, MAX_LR_DIST);
        int newRDist = calculateDistance(world, getPos().relative(back), right, MAX_LR_DIST);

        if (validateStructure(world, front, newLDist, newRDist, newBDist)) {
            this.lDist = newLDist;
            this.rDist = newRDist;
            this.bDist = newBDist;
            this.isFormed = true;
            shouldStartNextRecipe = true;
            needNewRecipe = true;
        } else {
            resetStructure();
            shouldStartNextRecipe = false;
        }

        updateTickSubscription();
    }

    private int calculateDistance(Level world, BlockPos startPos, Direction direction, int maxDistance) {
        int distance = 0;
        BlockPos.MutableBlockPos pos = startPos.mutable();
        for (int i = 1; i <= maxDistance; i++) {
            pos.move(direction);
            if (isBlockSolar(world, pos)) distance = i;
            else break;
        }
        return distance;
    }

    private boolean validateStructure(Level world, Direction front, int lDist, int rDist, int bDist) {
        if (lDist < MIN_LR_DIST || rDist < MIN_LR_DIST || bDist < MIN_B_DIST || lDist > MAX_LR_DIST || rDist > MAX_LR_DIST || bDist > MAX_B_DIST) return false;

        Direction back = front.getOpposite();
        Direction left = front.getCounterClockWise();
        Direction right = left.getOpposite();
        BlockPos startPos = getPos();

        for (int b = 1; b <= bDist; b++) {
            BlockPos backPos = startPos.relative(back, b);
            for (int l = 1; l <= lDist; l++)
                if (!isBlockSolar(world, backPos.relative(left, l))) return false;
            for (int r = 1; r <= rDist; r++)
                if (!isBlockSolar(world, backPos.relative(right, r))) return false;
        }
        return true;
    }

    private void resetStructure() {
        lDist = rDist = bDist = 0;
        isFormed = false;
    }

    public boolean isBlockSolar(@NotNull Level world, @NotNull BlockPos pos) {
        return world.getBlockState(pos).is(GTOBlocks.SOLAR_HEAT_COLLECTOR_PIPE_CASING.get());
    }

    @NotNull
    @Override
    public BlockPattern getPattern() {
        if (getLevel() != null) updateStructureDimensions();

        int safeLDist = isFormed() ? lDist : MIN_LR_DIST;
        int safeRDist = isFormed() ? rDist : MIN_LR_DIST;
        int safeBDist = isFormed() ? bDist : MIN_B_DIST;

        int totalWidth = safeLDist + safeRDist + 3;

        String boundaryRow = "a".repeat(totalWidth);
        String middleRow = "a" + "b".repeat(totalWidth - 2) + "a";
        String controllerRow = "a".repeat(safeLDist + 1) + "~" + "a".repeat(safeRDist + 1);

        return FactoryBlockPattern.start(RelativeDirection.LEFT, RelativeDirection.UP, RelativeDirection.FRONT)
                .aisle(boundaryRow)
                .aisle(middleRow).setRepeatable(safeBDist)
                .aisle(controllerRow)
                .where('a', blocks(GTBlocks.STEEL_HULL.get()).or(abilities(EXPORT_FLUIDS_1X)).or(abilities(IMPORT_FLUIDS_1X)))
                .where('b', blocks(GTOBlocks.SOLAR_HEAT_COLLECTOR_PIPE_CASING.get()))
                .where('~', controller(INNER_PREDICATE))
                .build();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        updateTickSubscription();
    }

    @Override
    public boolean onWorking() {
        return currentRecipe != null;
    }

    @Override
    public void afterWorking() {
        super.afterWorking();
        needNewRecipe = true;
        currentRecipe = null;
    }

    protected void updateSolarCollection() {
        Level level = getLevel();
        if (level == null || !isFormed()) {
            updateTickSubscription();
            return;
        }

        if (getOffsetTimer() % SUNLIT_CHECK_INTERVAL == 0) {
            sunlit = calculateSunlit(level);
            shouldStartNextRecipe = sunlit > 0;
        }

        if (needNewRecipe && shouldStartNextRecipe && !getRecipeLogic().isWorking()) {
            currentRecipe = createNextRecipe();
            if (currentRecipe != null && RecipeRunner.matchRecipe(this, currentRecipe)) {
                getRecipeLogic().setupRecipe(currentRecipe);
                needNewRecipe = false;
            }
        }
    }

    private int calculateSunlit(Level level) {
        if (!isAppropriateDimensionAndTime(level, getPos())) return 0;
        int count = 0;
        Direction front = getFrontFacing();
        Direction back = front.getOpposite();
        Direction left = front.getCounterClockWise();
        Direction right = left.getOpposite();

        BlockPos pos = getPos();
        for (int i = 1; i <= bDist; i++) {
            if (hasClearSky(level, pos.relative(back, i))) count++;
            for (int j = 1; j <= lDist; j++) if (hasClearSky(level, pos.relative(back, i).relative(left, j))) count++;
            for (int j = 1; j <= rDist; j++) if (hasClearSky(level, pos.relative(back, i).relative(right, j))) count++;
        }
        return count;
    }

    private static boolean isAppropriateDimensionAndTime(Level world, BlockPos pos) {
        return !world.getBiome(pos.above()).is(BiomeTags.IS_END) && world.isDay();
    }

    private static boolean hasClearSky(Level world, BlockPos pos) {
        BlockPos checkPos = pos.above();
        if (!world.canSeeSky(checkPos)) return false;
        Biome biome = world.getBiome(checkPos).value();
        boolean hasPrecipitation = world.isRaining() && (biome.warmEnoughToRain(checkPos) || biome.coldEnoughToSnow(checkPos));
        return !hasPrecipitation;
    }

    private Recipe createNextRecipe() {
        if (!shouldStartNextRecipe) return null;

        int steamAmount = basicSteamProduction * sunlit * STEAM_GENERATION_INTERVAL;
        int waterAmount = (int) Math.ceil((double) steamAmount / ConfigHolder.INSTANCE.machines.largeBoilers.steamPerWater);

        if (waterAmount <= 0 || steamAmount <= 0) return null;

        steamGenerated = steamAmount;
        return RecipeBuilder.ofRaw()
                .inputFluids(Water.getFluid(waterAmount))
                .outputFluids(Steam.getFluid(steamAmount))
                .duration(STEAM_GENERATION_INTERVAL)
                .EUt(0)
                .buildRawRecipe();
    }

    public void addDisplayText(List<Component> textList) {
        IDisplayUIMachine.super.addDisplayText(textList);
        if (isFormed()) {
            textList.add(Component.translatable("gtocore.machine.large_steam_solar_boiler.size", lDist + rDist + 3, bDist + 2));
            textList.add(Component.translatable("gtocore.machine.large_steam_solar_boiler.heat_collector_pipe", sunlit));
            textList.add(Component.translatable("gtocore.machine.large_steam_solar_boiler.steam_production", steamGenerated));
        } else {
            textList.add(Component.translatable("gtceu.top.invalid_structure"));
        }
    }
}
