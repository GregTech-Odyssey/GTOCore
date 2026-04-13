package com.gtocore.mixin.ae2.eae;

import com.gtolib.utils.ServerUtils;

import net.minecraft.Util;

import appeng.api.stacks.AEKey;

import com.glodblock.github.extendedae.common.me.taglist.TagPriorityList;
import it.unimi.dsi.fastutil.objects.Reference2BooleanMap;
import it.unimi.dsi.fastutil.objects.Reference2BooleanOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Mixin(TagPriorityList.class)
public abstract class TagPriorityListMixin {

    @Shadow(remap = false)
    public abstract boolean isEmpty();

    @Shadow(remap = false)
    @Final
    private Reference2BooleanMap<Object> memory;

    @Shadow(remap = false)
    protected abstract boolean eval(@NotNull Object input);

    @Shadow(remap = false)
    @Final
    private static Map<TagPriorityList, Runnable> INVALIDATOR;
    @Unique
    private volatile Reference2BooleanMap<Object> gto$underlyingMemory;

    @Unique
    private volatile Set<AEKey> gtolib$pending;

    /**
     * @author 1
     * @reason 3
     */
    @Overwrite(remap = false)
    public boolean isListed(AEKey input) {
        // empty filter pass all inputs
        if (this.isEmpty()) {
            return true;
        }
        Boolean b = this.memory.getOrDefault(input.getPrimaryKey(), null);
        if (b == null) {
            var pending = gtolib$pending;
            if (pending == null) {
                pending = new HashSet<>();
                pending.add(input);
                gtolib$pending = pending;
                INVALIDATOR.put((TagPriorityList) (Object) this, () -> {
                    gtolib$pending = null;
                    gto$underlyingMemory = null;
                });
                CompletableFuture.runAsync(() -> {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        return;
                    }
                    var loading = gtolib$pending;
                    gtolib$pending = null;
                    if (loading == null) {
                        return;
                    }
                    var underlying = new Reference2BooleanOpenHashMap<>(loading.size());
                    for (var item : loading) {
                        var pk = item.getPrimaryKey();
                        underlying.put(pk, eval(pk));
                    }
                    if (gto$underlyingMemory == null) {
                        gto$underlyingMemory = underlying;
                    }
                    ServerUtils.getServer().execute(() -> {
                        var mem = this.memory;
                        var gtoMem = gto$underlyingMemory;
                        gto$underlyingMemory = null;
                        if (gtoMem == null) {
                            return;
                        }
                        mem.putAll(gtoMem);
                    });

                }, Util.backgroundExecutor());
            } else {
                pending.add(input);
            }
            return false;
        } else {
            return b;
        }
    }
}
