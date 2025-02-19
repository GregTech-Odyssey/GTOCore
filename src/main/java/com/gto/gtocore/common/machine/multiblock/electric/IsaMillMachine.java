package com.gto.gtocore.common.machine.multiblock.electric;

import com.gto.gtocore.api.machine.multiblock.ElectricMultiblockMachine;
import com.gto.gtocore.common.machine.multiblock.part.BallHatchPartMachine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class IsaMillMachine extends ElectricMultiblockMachine {

    private BallHatchPartMachine ballHatchPartMachine;

    public IsaMillMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
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
    public boolean beforeWorking(@Nullable GTRecipe recipe) {
        CustomItemStackHandler storage = ballHatchPartMachine.getInventory().storage;
        ItemStack item = storage.getStackInSlot(0);
        int tier = BallHatchPartMachine.GRINDBALL.getOrDefault(item.getItem(), 0);
        if (recipe != null && tier == recipe.data.getInt("grindball")) {
            int damage = item.getDamageValue();
            if (damage < item.getMaxDamage()) {
                item.setDamageValue(damage + 1);
                storage.setStackInSlot(0, item);
            } else {
                storage.setStackInSlot(0, ItemStack.EMPTY);
            }
            return super.beforeWorking(recipe);
        }
        return false;
    }
}
