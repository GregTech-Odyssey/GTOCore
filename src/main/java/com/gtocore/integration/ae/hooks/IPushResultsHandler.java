package com.gtocore.integration.ae.hooks;

import com.gtolib.api.ae2.IPatternProviderLogic;

import net.minecraft.network.FriendlyByteBuf;

import appeng.api.stacks.AEKey;
import com.google.common.collect.Multimap;

public interface IPushResultsHandler {

    void gtocore$syncCraftingResults(FriendlyByteBuf buf);

    Multimap<AEKey, IPatternProviderLogic.PushResult> gto$getLastCraftingResults();
}
