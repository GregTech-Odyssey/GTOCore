package com.gtocore.mixin.ae2.pattern;

import net.minecraft.world.level.Level;

import appeng.api.stacks.AEItemKey;
import appeng.crafting.pattern.AESmithingTablePattern;
import appeng.crafting.pattern.SmithingTablePatternItem;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SmithingTablePatternItem.class)
public class SmithingTablePatternItemMixin {

    @Unique
    private static final Object2ObjectOpenHashMap<AEItemKey, AESmithingTablePattern> gtolib$CACHE = new Object2ObjectOpenHashMap<>();

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
