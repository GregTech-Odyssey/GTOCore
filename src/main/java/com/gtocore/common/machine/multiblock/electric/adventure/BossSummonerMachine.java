package com.gtocore.common.machine.multiblock.electric.adventure;

import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.utils.MachineUtils;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.handler.ICustomRecipeLogicHolder;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

import dev.shadowsoffire.apotheosis.adventure.boss.ApothBoss;
import dev.shadowsoffire.apotheosis.adventure.boss.BossRegistry;
import dev.shadowsoffire.placebo.reload.WeightedDynamicRegistry;

public final class BossSummonerMachine extends ElectricMultiblockMachine implements ICustomRecipeLogicHolder {

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
    public void afterWorking() {
        super.afterWorking();
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

    @Override
    public GTRecipeDefinition createCustomRecipe(RecipeHandlerUnit unit) {
        if (getOverclockVoltage() < 1) return null;
        return getRecipeBuilder().duration(Math.max(5, 400 / (getTier() + 1))).EUt(getOverclockVoltage()).build();
    }
}
