package com.gtocore.mixin.ae2.pattern;

import net.minecraft.world.level.Level;

import appeng.api.stacks.AEItemKey;
import appeng.crafting.pattern.AECraftingPattern;
import appeng.crafting.pattern.CraftingPatternItem;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@Mixin(CraftingPatternItem.class)
public class CraftingPatternItemMixin {

    @Unique
    private static final Object2ObjectOpenHashMap<AEItemKey, AECraftingPattern> gtolib$CACHE = new Object2ObjectOpenHashMap<>();

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
