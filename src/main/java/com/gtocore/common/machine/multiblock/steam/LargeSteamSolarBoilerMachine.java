package com.gtocore.common.machine.multiblock.steam;

import com.gtocore.common.data.GTOBlocks;

import com.gtolib.api.annotation.Scanned;
import com.gtolib.api.annotation.dynamic.DynamicInitialValue;
import com.gtolib.api.annotation.dynamic.DynamicInitialValueTypes;
import com.gtolib.api.machine.feature.IEnhancedRecipeLogicMachine;
import com.gtolib.api.machine.trait.CustomRecipeLogic;
import com.gtolib.api.recipe.Recipe;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IExplosionMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IDisplayUIMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.config.ConfigHolder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.Fluids;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.gregtechceu.gtceu.api.machine.multiblock.PartAbility.EXPORT_FLUIDS;
import static com.gregtechceu.gtceu.api.machine.multiblock.PartAbility.IMPORT_FLUIDS;
import static com.gregtechceu.gtceu.api.pattern.Predicates.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.Steam;

@Scanned
public class LargeSteamSolarBoilerMachine extends WorkableMultiblockMachine implements IExplosionMachine, IDisplayUIMachine, IEnhancedRecipeLogicMachine {

    @DynamicInitialValue(key = "gtocore.machine.large_steam_solar_boiler", typeKey = DynamicInitialValueTypes.KEY_MULTIPLY, easyValue = "30", normalValue = "18", expertValue = "12", cn = "基础蒸汽产率 : %s / t", en = "Basic steam production : %s / t")
    private static int basicSteamProduction = 10;

    private static final int MAX_LR_DIST = 62, MAX_B_DIST = 125;
    private static final int MIN_LR_DIST = 1, MIN_B_DIST = 3;
    private static final int STEAM_GENERATION_INTERVAL = 20;

    private int lDist, rDist, bDist, sunlit;
    private int steamGenerated;
    private int timing;
    private boolean formed;

    public LargeSteamSolarBoilerMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    private void updateStructureDimensions() {
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
            this.formed = true;
        } else {
            resetStructure();
        }
    }

    private static int calculateDistance(Level world, BlockPos startPos, Direction direction, int maxDistance) {
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
        formed = false;
    }

    private static boolean isBlockSolar(@NotNull Level world, @NotNull BlockPos pos) {
        return world.getBlockState(pos).is(GTOBlocks.SOLAR_HEAT_COLLECTOR_PIPE_CASING.get());
    }

    @NotNull
    @Override
    public BlockPattern getPattern() {
        if (getLevel() != null) updateStructureDimensions();

        int safeLDist = formed ? lDist : MIN_LR_DIST;
        int safeRDist = formed ? rDist : MIN_LR_DIST;
        int safeBDist = formed ? bDist : MIN_B_DIST;

        int totalWidth = safeLDist + safeRDist + 3;

        String boundaryRow = "a".repeat(totalWidth);
        String middleRow = "a" + "b".repeat(totalWidth - 2) + "a";
        String controllerRow = "a".repeat(safeLDist + 1) + "~" + "a".repeat(safeRDist + 1);

        return FactoryBlockPattern.start(RelativeDirection.LEFT, RelativeDirection.UP, RelativeDirection.FRONT)
                .aisle(boundaryRow)
                .aisle(middleRow).setRepeatable(safeBDist)
                .aisle(controllerRow)
                .where('a', blocks(GTBlocks.STEEL_HULL.get()).or(abilities(EXPORT_FLUIDS)).or(abilities(IMPORT_FLUIDS)))
                .where('b', blocks(GTOBlocks.SOLAR_HEAT_COLLECTOR_PIPE_CASING.get()))
                .where('~', controller(this.getDefinition()))
                .build();
    }

    @Override
    public boolean keepSubscribing() {
        return true;
    }

    @Override
    public RecipeLogic createRecipeLogic(Object... args) {
        return new CustomRecipeLogic(this, this::getRecipe);
    }

    private Recipe getRecipe() {
        Level level = getLevel();
        if (level != null && isFormed()) {
            if (timing == 0) {
                sunlit = calculateSunlit(level);
                timing = 5;
            } else {
                timing--;
            }
            if (sunlit > 0) {
                return createNextRecipe();
            }
        }
        return null;
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

    private boolean isAppropriateDimensionAndTime(Level world, BlockPos pos) {
        if (!world.isDay()) {
            getEnhancedRecipeLogic().gtolib$setIdleReason(Component.translatable("gtceu.recipe_logic.condition_fails")
                    .append(": ").append(Component.translatable("recipe.condition.daytime.day.tooltip")));
            return false;
        }
        return !world.getBiome(pos.above()).is(BiomeTags.IS_END);
    }

    private static boolean hasClearSky(Level world, BlockPos pos) {
        BlockPos checkPos = pos.above();
        if (!world.canSeeSky(checkPos)) return false;
        Biome biome = world.getBiome(checkPos).value();
        boolean hasPrecipitation = world.isRaining() && (biome.warmEnoughToRain(checkPos) || biome.coldEnoughToSnow(checkPos));
        return !hasPrecipitation;
    }

    private Recipe createNextRecipe() {
        int steamAmount = basicSteamProduction * sunlit * STEAM_GENERATION_INTERVAL;
        int waterAmount = (int) Math.ceil((double) steamAmount / ConfigHolder.INSTANCE.machines.largeBoilers.steamPerWater);

        if (waterAmount <= 0 || steamAmount <= 0) return null;

        steamGenerated = steamAmount;
        return getRecipeBuilder()
                .inputFluids(Fluids.WATER, waterAmount)
                .outputFluids(Steam.getFluid(), steamAmount)
                .duration(STEAM_GENERATION_INTERVAL)
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
