package com.gtocore.common.machine.multiblock.part.research;

import com.gtocore.common.data.GTOBlocks;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.common.machine.multiblock.part.hpca.HPCAComponentPartMachine;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class ExResearchBasePartMachine extends HPCAComponentPartMachine {

    final int tier;

    ExResearchBasePartMachine(MetaMachineBlockEntity holder, int tier) {
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
                    if (tier == 3) drops.set(i, GTOBlocks.BIOCOMPUTER_CASING.asStack());
                    else if (tier == 4) drops.set(i, GTOBlocks.GRAVITON_COMPUTER_CASING.asStack());
                    else drops.set(i, GTOBlocks.GRAVITON_COMPUTER_CASING.asStack());
                }
                break;
            }
        }
    }

    public int getTier() {
        return this.tier;
    }
}
