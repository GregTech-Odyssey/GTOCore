package com.gtocore.mixin.ae2.pattern;

import net.minecraft.world.level.Level;

import appeng.api.stacks.AEItemKey;
import appeng.crafting.pattern.AECraftingPattern;
import appeng.crafting.pattern.CraftingPatternItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import java.util.WeakHashMap;

@Mixin(CraftingPatternItem.class)
public class CraftingPatternItemMixin {

    @Unique
    private static final WeakHashMap<AEItemKey, AECraftingPattern> gtolib$CACHE = new WeakHashMap<>();

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public AECraftingPattern decode(AEItemKey what, Level level) {
        if (what == null || !what.hasTag()) {
            return null;
        }

        try {
            synchronized (gtolib$CACHE) {
                return gtolib$CACHE.computeIfAbsent(what, k -> new AECraftingPattern(what, level));
            }
        } catch (Exception e) {
            return null;
        }
    }
}
