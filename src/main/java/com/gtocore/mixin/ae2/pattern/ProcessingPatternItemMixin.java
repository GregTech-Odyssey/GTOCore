package com.gtocore.mixin.ae2.pattern;

import com.gtolib.api.ae2.MyPatternDetailsHelper;

import net.minecraft.world.level.Level;

import appeng.api.stacks.AEItemKey;
import appeng.crafting.pattern.AEProcessingPattern;
import appeng.crafting.pattern.ProcessingPatternItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ProcessingPatternItem.class)
public class ProcessingPatternItemMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public AEProcessingPattern decode(AEItemKey what, Level level) {
        if (what == null || !what.hasTag()) {
            return null;
        }

        try {
            return MyPatternDetailsHelper.CACHE.get(what);
        } catch (Exception e) {
            return null;
        }
    }
}
