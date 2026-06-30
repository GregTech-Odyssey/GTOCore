package com.gtocore.config;

import com.hollingsworth.arsnouveau.common.block.tile.StorageLecternTile;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StorageBusBlacklist {

    public final static ReferenceOpenHashSet<Class<?>> LIST = new ReferenceOpenHashSet<>();

    static {
        LIST.add(StorageLecternTile.class);
    }
}
