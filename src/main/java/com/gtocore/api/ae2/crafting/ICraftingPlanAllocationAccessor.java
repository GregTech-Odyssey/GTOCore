package com.gtocore.api.ae2.crafting;

import appeng.api.crafting.IPatternDetails;
import appeng.api.stacks.AEKey;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public interface ICraftingPlanAllocationAccessor {

    Object2ObjectOpenHashMap<AEKey, Object2LongOpenHashMap<IPatternDetails>> getGtocore$allocations();

    void setGtocore$allocations(Object2ObjectOpenHashMap<AEKey, Object2LongOpenHashMap<IPatternDetails>> allocations);
}
