package com.gtocore.api.ae2.crafting;

import appeng.api.crafting.IPatternDetails;
import appeng.api.stacks.AEKey;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;

public interface ICraftingPlanAllocationAccessor {

    Reference2ObjectOpenHashMap<AEKey, Object2LongOpenHashMap<IPatternDetails>> getGtocore$allocations();

    void setGtocore$allocations(Reference2ObjectOpenHashMap<AEKey, Object2LongOpenHashMap<IPatternDetails>> allocations);
}
