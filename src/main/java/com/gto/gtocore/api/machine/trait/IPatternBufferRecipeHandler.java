package com.gto.gtocore.api.machine.trait;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import it.unimi.dsi.fastutil.objects.Object2LongOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;

public interface IPatternBufferRecipeHandler {

    Object2LongOpenCustomHashMap<ItemStack> getItemMap();

    Object2LongOpenHashMap<FluidStack> getFluidMap();
}
