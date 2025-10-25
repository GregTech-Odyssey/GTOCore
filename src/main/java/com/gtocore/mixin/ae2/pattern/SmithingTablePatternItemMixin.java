package com.gtocore.mixin.ae2.pattern;

import com.gregtechceu.gtceu.utils.collection.O2OOpenCacheHashMap;

import net.minecraft.world.level.Level;

import appeng.api.stacks.AEItemKey;
import appeng.crafting.pattern.AESmithingTablePattern;
import appeng.crafting.pattern.SmithingTablePatternItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SmithingTablePatternItem.class)
public class SmithingTablePatternItemMixin {

    @Unique
    private static final O2OOpenCacheHashMap<AEItemKey, AESmithingTablePattern> gtolib$CACHE = new O2OOpenCacheHashMap<>();

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public AESmithingTablePattern decode(AEItemKey what, Level level) {
        if (what == null || !what.hasTag()) {
            return null;
        }

        try {
            synchronized (gtolib$CACHE) {
                return gtolib$CACHE.computeIfAbsent(what, k -> new AESmithingTablePattern(what, level));
            }
        } catch (Exception e) {
            return null;
        }
    }
}
