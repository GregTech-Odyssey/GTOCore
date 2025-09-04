package com.gtocore.api.ae2.crafting;

import appeng.api.stacks.AEKeyType;

public interface IElapsedTimeTracker {

    void gtolib$decrementItems(long itemDiff, AEKeyType keyType);

    void gtolib$addMaxItems(long itemDiff, AEKeyType keyType);
}
