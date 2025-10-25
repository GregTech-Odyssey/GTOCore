package com.gtocore.mixin.ae2.pattern;

import com.gregtechceu.gtceu.utils.collection.O2OOpenCacheHashMap;

import net.minecraft.world.level.Level;

import appeng.api.stacks.AEItemKey;
import appeng.crafting.pattern.AEStonecuttingPattern;
import appeng.crafting.pattern.StonecuttingPatternItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@Mixin(StonecuttingPatternItem.class)
public class StonecuttingPatternItemMixin {

    @Unique
    private static final O2OOpenCacheHashMap<AEItemKey, AEStonecuttingPattern> gtolib$CACHE = new O2OOpenCacheHashMap<>();

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public AEStonecuttingPattern decode(AEItemKey what, Level level) {
        if (what == null || !what.hasTag()) {
            return null;
        }

        try {
            synchronized (gtolib$CACHE) {
                return gtolib$CACHE.computeIfAbsent(what, k -> new AEStonecuttingPattern(what, level));
            }
        } catch (Exception e) {
            return null;
        }
    }
}
