package com.gtocore.api.entity;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;

import java.util.Set;

public interface ILivingEntity {

    void gtocore$getAllDeathLoot(DamageSource source, Set<ItemStack> itemStacks, int multiplier, boolean filterNbt);
}
