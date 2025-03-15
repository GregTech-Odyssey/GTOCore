package com.gto.gtocore.mixin.gtm.pattern;

import com.gregtechceu.gtceu.api.pattern.MultiblockState;
import com.gregtechceu.gtceu.api.pattern.predicates.SimplePredicate;
import com.gregtechceu.gtceu.api.pattern.util.PatternMatchContext;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(MultiblockState.class)
public class MultiblockStateMixin {

    @Shadow(remap = false)
    @Final
    private PatternMatchContext matchContext;

    @Shadow(remap = false)
    private Map<SimplePredicate, Integer> globalCount;

    @Shadow(remap = false)
    private Map<SimplePredicate, Integer> layerCount;

    @Shadow(remap = false)
    public LongOpenHashSet cache;

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    protected void clean() {
        this.matchContext.reset();
        this.globalCount = new Object2IntOpenHashMap<>();
        this.layerCount = new Object2IntOpenHashMap<>();
        cache = new LongOpenHashSet();
    }
}
