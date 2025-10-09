package com.gtocore.common.machine.multiblock.generator;

import com.gtolib.api.data.GTODimensions;
import com.gtolib.api.machine.mana.feature.IManaMultiblock;
import com.gtolib.api.machine.mana.trait.ManaTrait;
import com.gtolib.api.machine.multiblock.StorageMultiblockMachine;
import com.gtolib.api.machine.trait.CustomRecipeLogic;
import com.gtolib.api.misc.ManaContainerList;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeRunner;
import com.gtolib.utils.GTOUtils;
import com.gtolib.utils.MachineUtils;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import earth.terrarium.adastra.api.planets.PlanetApi;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.common.block.BotaniaBlocks;

public final class PhotovoltaicPowerStationMachine extends StorageMultiblockMachine implements IManaMultiblock {

    private final int basic_rate;

    private final ManaTrait manaTrait;

    public PhotovoltaicPowerStationMachine(MetaMachineBlockEntity holder, int basicRate) {
        super(holder, 64, i -> i.getItem() == BotaniaBlocks.motifDaybloom.asItem());
        basic_rate = basicRate;
        this.manaTrait = new ManaTrait(this);
    }

    @Override
    public @NotNull ManaContainerList getManaContainer() {
        return manaTrait.getManaContainers();
    }

    @Override
    public boolean isGeneratorMana() {
        return true;
    }

    private boolean canSeeSky(Level level) {
        BlockPos pos = MachineUtils.getOffsetPos(1, 5, getFrontFacing(), getPos());
        for (int i = -2; i < 3; i++) {
            for (int j = -2; j < 3; j++) {
                if (!level.canSeeSky(pos.offset(i, 0, j))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean keepSubscribing() {
        return true;
    }

    @Nullable
    private Recipe getRecipe() {
        Level level = getLevel();
        if (level != null && canSeeSky(level)) {
            int eut;
            int basic = (int) (basic_rate * PlanetApi.API.getSolarPower(level));
            if (PlanetApi.API.isSpace(level)) {
                eut = inputFluid(GTMaterials.DistilledWater.getFluid(), basic / 4) ? basic << 4 : 0;
            } else {
                eut = (int) (basic * (GTODimensions.isVoid(level.dimension().location()) ? 14 : GTOUtils.getSunIntensity(level.getDayTime()) * 15 / 100 * (level.isRaining() ? (level.isThundering() ? 0.3f : 0.7f) : 1)));
            }
            if (eut == 0) return null;
            var builder = getRecipeBuilder().duration(20);
            if (getStorageStack().getCount() == 64) {
                builder.MANAt(-eut);
            } else {
                builder.EUt(-eut);
            }
            Recipe recipe = builder.buildRawRecipe();
            if (RecipeRunner.matchTickRecipe(this, recipe)) return recipe;
        }
        return null;
    }

    @Override
    public RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CustomRecipeLogic(this, this::getRecipe);
    }
}
