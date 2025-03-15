package com.gto.gtocore.common.machine.multiblock.part.research;

import com.gto.gtocore.common.data.GTOBlocks;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.common.machine.multiblock.part.hpca.HPCAComponentPartMachine;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.ItemStack;

import lombok.Getter;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@Getter
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class ExResearchBasePartMachine extends HPCAComponentPartMachine {

    protected final int tier;

    ExResearchBasePartMachine(IMachineBlockEntity holder, int tier) {
        super(holder);
        this.tier = tier;
    }

    @Override
    public boolean isAdvanced() {
        return true;
    }

    @Override
    public void onDrops(List<ItemStack> drops) {
        for (int i = 0; i < drops.size(); ++i) {
            ItemStack drop = drops.get(i);
            if (drop.getItem() == this.getDefinition().getItem()) {
                if (canBeDamaged() && isDamaged()) {
                    if (tier == 3) drops.set(i, GTOBlocks.BIOCOMPUTER_SHELL.asStack());
                    else if (tier == 4) drops.set(i, GTOBlocks.GRAVITON_COMPUTER_SHELL.asStack());
                    else drops.set(i, GTOBlocks.GRAVITON_COMPUTER_SHELL.asStack());
                }
                break;
            }
        }
    }
}
