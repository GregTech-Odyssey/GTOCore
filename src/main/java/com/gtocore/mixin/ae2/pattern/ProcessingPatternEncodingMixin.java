package com.gtocore.mixin.ae2.pattern;

import net.minecraft.nbt.ListTag;

import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.Object2LongLinkedOpenHashMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(targets = "appeng.crafting.pattern.ProcessingPatternEncoding")
public class ProcessingPatternEncodingMixin {

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    private static ListTag encodeStackList(GenericStack[] stacks) {
        var map = new Object2LongLinkedOpenHashMap<AEKey>(stacks.length);
        for (var stack : stacks) {
            if (stack == null || stack.amount() == 0) continue;
            map.addTo(stack.what(), stack.amount());
        }
        Preconditions.checkArgument(!map.isEmpty(), "List passed to pattern must contain at least one stack.");
        ListTag tag = new ListTag();
        for (var stack : map.object2LongEntrySet()) {
            tag.add(GenericStack.writeTag(new GenericStack(stack.getKey(), stack.getLongValue())));
        }
        return tag;
    }
}
