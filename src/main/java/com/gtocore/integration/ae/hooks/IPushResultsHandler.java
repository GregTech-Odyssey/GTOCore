package com.gtocore.integration.ae.hooks;

import com.gtolib.api.ae2.IPatternProviderLogic;
import com.gtolib.api.network.NetworkPack;

import net.minecraft.network.FriendlyByteBuf;

import appeng.api.stacks.AEKey;
import com.google.common.collect.Multimap;

public interface IPushResultsHandler {

    NetworkPack CRAFT_MENU_PUSH_RESULTS = NetworkPack.registerS2C("pushResultUpdate", (p, b) -> {
        if (p.containerMenu.containerId == b.readInt() && p.containerMenu instanceof IPushResultsHandler handler) {
            handler.gtocore$syncCraftingResults(b);
        }
    });

    void gtocore$syncCraftingResults(FriendlyByteBuf buf);

    Multimap<AEKey, IPatternProviderLogic.PushResult> gto$getLastCraftingResults();
}
