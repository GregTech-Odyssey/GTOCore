package com.gtocore.mixin.gtm.item;

import com.gregtechceu.gtceu.common.item.TurbineRotorBehaviour;
import com.gregtechceu.gtceu.common.item.tool.CoatedTurbineRotorBehaviour;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CoatedTurbineRotorBehaviour.class)
public abstract class CoatedTurbineRotorBehaviourMixin extends TurbineRotorBehaviour {

    @Shadow(remap = false)
    @Final
    private RandomSource rd;

    @Shadow(remap = false)
    public abstract int getCoatDamage(ItemStack itemStack);

    @Shadow(remap = false)
    public abstract int getCoatMaxDamage(ItemStack itemStack);

    @Shadow(remap = false)
    public abstract void setCoatDamage(ItemStack itemStack, int resultDamage);

    /**
     * @author GTOCore
     * @reason Use rotor durability delta, not entity attack damage, when charging coating wear.
     */
    @Overwrite(remap = false)
    public void setPartDamage(ItemStack itemStack, int resultDamage) {
        int damageApplied = resultDamage - getPartDamage(itemStack);
        if (damageApplied <= 0) {
            super.setPartDamage(itemStack, resultDamage);
            return;
        }
        if (getCoatDamage(itemStack) < getCoatMaxDamage(itemStack) &&
                rd.nextInt(100) < 95 && !CoatedTurbineRotorBehaviour.isCoatingMagical(itemStack)) {
            setCoatDamage(itemStack, Math.min(getCoatDamage(itemStack) + damageApplied, getCoatMaxDamage(itemStack)));
            return;
        } else if (CoatedTurbineRotorBehaviour.isCoatingMagical(itemStack)) {
            setCoatDamage(itemStack, getCoatDamage(itemStack) + damageApplied);
        }
        super.setPartDamage(itemStack, resultDamage);
    }
}
