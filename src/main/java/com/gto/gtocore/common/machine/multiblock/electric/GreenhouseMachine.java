package com.gto.gtocore.common.machine.multiblock.electric;

import com.gto.gtocore.api.machine.multiblock.ElectricMultiblockMachine;
import com.gto.gtocore.utils.MachineUtils;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class GreenhouseMachine extends ElectricMultiblockMachine {

    public GreenhouseMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    private int SkyLight = 15;

    private void getGreenhouseLight() {
        Level level = getLevel();
        if (level == null) return;
        SkyLight = 15;
        BlockPos pos = MachineUtils.getOffsetPos(1, 3, getFrontFacing(), getPos());
        BlockPos[] coordinates = { pos,
                pos.offset(1, 0, 0),
                pos.offset(1, 0, 1),
                pos.offset(1, 0, -1),
                pos.offset(0, 0, 1),
                pos.offset(0, 0, -1),
                pos.offset(-1, 0, 0),
                pos.offset(-1, 0, 1),
                pos.offset(-1, 0, -1) };
        for (BlockPos i : coordinates) {
            int l = level.getBrightness(LightLayer.SKY, i) - (level.dimension() == Level.OVERWORLD ? level.getSkyDarken() : 0);
            if (l < SkyLight) {
                SkyLight = l;
            }
        }
    }

    @Override
    public boolean beforeWorking(@Nullable GTRecipe recipe) {
        getGreenhouseLight();
        if (SkyLight == 0) {
            return false;
        }
        return super.beforeWorking(recipe);
    }

    @Override
    public boolean onWorking() {
        if (!super.onWorking()) return false;
        if (getOffsetTimer() % 20 == 0) {
            getGreenhouseLight();
            if (SkyLight == 0) {
                getRecipeLogic().setProgress(0);
            }
            if (SkyLight < 13) {
                getRecipeLogic().setProgress(getRecipeLogic().getProgress() - 10);
            }
        }
        return true;
    }

    @Override
    protected void customText(List<Component> textList) {
        super.customText(textList);
        if (getOffsetTimer() % 10 == 0) {
            getGreenhouseLight();
        }
        textList.add(Component.translatable("gtocore.machine.greenhouse.SkyLight", SkyLight));
    }
}
