package com.gtocore.mixin.ae2.eae;

import net.minecraft.Util;
import net.minecraftforge.registries.ForgeRegistries;

import appeng.api.stacks.AEKey;

import com.glodblock.github.extendedae.common.me.taglist.TagPriorityList;
import it.unimi.dsi.fastutil.objects.Reference2BooleanMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Mixin(TagPriorityList.class)
public abstract class TagPriorityListMixin {

    @Mutable
    @Shadow(remap = false)
    @Final
    private Reference2BooleanMap<Object> memory;

    @Shadow(remap = false)
    protected abstract boolean eval(@NotNull Object input);

    @Shadow(remap = false)
    @Final
    private static Map<TagPriorityList, Runnable> INVALIDATOR;
    @Shadow(remap = false)
    @Final
    private String rawWhiteListExpression;
    @Shadow(remap = false)
    @Final
    private String rawBlackListExpression;

    @Unique
    private volatile ReferenceOpenHashSet<Object> gto$underlyingMemory;

    @Unique
    private final ReadWriteLock gtocore$lock = new ReentrantReadWriteLock();

    @Unique
    private final Lock gtocore$readLock = gtocore$lock.readLock();

    @Unique
    private final Runnable gtocore$build = () -> {
        gtocore$lock.writeLock().lock();
        try {
            var memory = new ReferenceOpenHashSet<>();
            ForgeRegistries.ITEMS.forEach(i -> {
                if (eval(i)) {
                    memory.add(i);
                }
            });
            ForgeRegistries.FLUIDS.forEach(f -> {
                if (eval(f)) {
                    memory.add(f);
                }
            });
            gto$underlyingMemory = memory;
        } finally {
            gtocore$lock.writeLock().unlock();
        }
    };

    @Unique
    private boolean gtocore$isEmpty;

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void init(String whiteListExpression, String blackListExpression, CallbackInfo ci) {
        gtocore$isEmpty = this.rawWhiteListExpression.isBlank() && this.rawBlackListExpression.isBlank();
        memory = null;
        if (!gtocore$isEmpty) {
            INVALIDATOR.put((TagPriorityList) (Object) this, () -> gto$underlyingMemory = null);
            CompletableFuture.runAsync(gtocore$build, Util.backgroundExecutor());
        }
    }

    /**
     * @author 1
     * @reason 3
     */
    @Overwrite(remap = false)
    public boolean isEmpty() {
        return gtocore$isEmpty;
    }

    /**
     * @author 1
     * @reason 3
     */
    @Overwrite(remap = false)
    public boolean isListed(AEKey input) {
        if (gtocore$isEmpty) return true;
        gtocore$readLock.lock();
        try {
            if (gto$underlyingMemory == null) gtocore$build.run();
            return gto$underlyingMemory.contains(input.getPrimaryKey());
        } finally {
            gtocore$readLock.unlock();
        }
    }
}
