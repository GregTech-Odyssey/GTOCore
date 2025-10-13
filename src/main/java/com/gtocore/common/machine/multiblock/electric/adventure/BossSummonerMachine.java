package com.gtocore.common.machine.multiblock.electric.adventure;

import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.api.machine.trait.CustomRecipeLogic;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeRunner;
import com.gtolib.utils.MachineUtils;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

import dev.shadowsoffire.apotheosis.adventure.boss.ApothBoss;
import dev.shadowsoffire.apotheosis.adventure.boss.BossRegistry;
import dev.shadowsoffire.placebo.reload.WeightedDynamicRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BossSummonerMachine extends ElectricMultiblockMachine {

    public BossSummonerMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public boolean hasOverclockConfig() {
        return false;
    }

    @Override
    public boolean hasBatchConfig() {
        return false;
    }

    @Override
    public void onRecipeFinish() {
        super.onRecipeFinish();
        Level world = getLevel();
        if (world == null || world.isClientSide) return;
        ApothBoss item = BossRegistry.INSTANCE.getRandomItem(world.getRandom(), getTier() << 2, WeightedDynamicRegistry.IDimensional.matches(world));
        if (item == null) return;
        BlockPos pos = MachineUtils.getOffsetPos(2, 2, getFrontFacing(), getPos());
        if (!world.noCollision(item.getSize().move(pos))) {
            pos = pos.above();
            if (!world.noCollision(item.getSize().move(pos))) return;
        }
        Mob boss = item.createBoss((ServerLevel) world, pos, world.getRandom(), getTier() << 2);
        ((ServerLevel) world).addFreshEntityWithPassengers(boss);
    }

    @Nullable
    private Recipe getRecipe() {
        Recipe recipe = getRecipeBuilder().duration(Math.max(5, 400 / (getTier() + 1))).EUt(getOverclockVoltage()).buildRawRecipe();
        if (RecipeRunner.matchTickRecipe(this, recipe)) return recipe;
        return null;
    }

    @Override
    public RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CustomRecipeLogic(this, this::getRecipe, true);
    }
}
