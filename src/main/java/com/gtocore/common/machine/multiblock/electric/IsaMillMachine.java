package com.gtocore.common.machine.multiblock.electric;

import com.gtocore.common.machine.multiblock.part.BallHatchPartMachine;
import com.gtocore.data.IdleReason;

import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.utils.MathUtil;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class IsaMillMachine extends ElectricMultiblockMachine {

    private BallHatchPartMachine ballHatchPartMachine;

    public IsaMillMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public void onPartScan(IMultiPart part) {
        super.onPartScan(part);
        if (ballHatchPartMachine == null && part instanceof BallHatchPartMachine ballHatchPart) {
            ballHatchPartMachine = ballHatchPart;
        }
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        ballHatchPartMachine = null;
    }

    @Override
    protected boolean beforeWorking(@Nullable Recipe recipe) {
        CustomItemStackHandler storage = ballHatchPartMachine.getInventory().storage;
        ItemStack item = storage.getStackInSlot(0);
        int tier = BallHatchPartMachine.GRINDBALL.getOrDefault(item.getItem(), 0);
        if (recipe != null && tier == recipe.data.getInt("grindball")) {
            int damage = item.getDamageValue() + MathUtil.saturatedCast(recipe.parallels);
            if (damage < item.getMaxDamage()) {
                item.setDamageValue(damage);
            } else {
                storage.setStackInSlot(0, ItemStack.EMPTY);
            }
            return super.beforeWorking(recipe);
        }
        setIdleReason(IdleReason.GRIND_BALL);
        return false;
    }
}
