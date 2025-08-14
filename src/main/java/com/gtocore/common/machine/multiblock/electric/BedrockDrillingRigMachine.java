package com.gtocore.common.machine.multiblock.electric;

import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.api.recipe.Recipe;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import org.jetbrains.annotations.Nullable;

public final class BedrockDrillingRigMachine extends ElectricMultiblockMachine {

    public BedrockDrillingRigMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public boolean keepSubscribing() {
        return true;
    }

    @Override
    protected boolean beforeWorking(@Nullable Recipe recipe) {
        Level level = getLevel();
        boolean value = false;
        if (level != null) {
            value = level.getBlockState(getPos().offset(0, -9, 0)).getBlock() == Blocks.BEDROCK;
            if (value && GTValues.RNG.nextInt(10) == 1) {
                level.setBlockAndUpdate(getPos().offset(0, -9, 0), Blocks.AIR.defaultBlockState());
            }
        }
        return value;
    }
}
