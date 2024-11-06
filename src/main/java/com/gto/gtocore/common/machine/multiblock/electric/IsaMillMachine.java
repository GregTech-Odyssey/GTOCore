package com.gto.gtocore.common.machine.multiblock.electric;

import com.gto.gtocore.common.machine.multiblock.part.BallHatchPartMachine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import com.lowdragmc.lowdraglib.misc.ItemStackTransfer;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class IsaMillMachine extends WorkableElectricMultiblockMachine {

    private BallHatchPartMachine ballHatchPartMachine;

    public IsaMillMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        for (IMultiPart part : getParts()) {
            if (part instanceof BallHatchPartMachine ballHatchPart) {
                this.ballHatchPartMachine = ballHatchPart;
            }
        }
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        ballHatchPartMachine = null;
    }

    @Override
    public boolean beforeWorking(@Nullable GTRecipe recipe) {
        ItemStackTransfer storage = ballHatchPartMachine.getInventory().storage;
        ItemStack item = storage.getStackInSlot(0);
        int tier = switch (item.kjs$getId()) {
            case "kubejs:grindball_aluminium" -> 2;
            case "kubejs:grindball_soapstone" -> 1;
            default -> 0;
        };
        if (recipe != null && tier == recipe.data.getInt("grindball")) {
            int damage = item.getDamageValue();
            if (damage < item.getMaxDamage()) {
                item.setDamageValue(damage + 1);
                storage.setStackInSlot(0, item);
            } else {
                storage.setStackInSlot(0, new ItemStack(Items.AIR));
            }
            return super.beforeWorking(recipe);
        }
        return false;
    }
}
