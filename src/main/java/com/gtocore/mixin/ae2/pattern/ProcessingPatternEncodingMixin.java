package com.gtocore.mixin.ae2.pattern;

import net.minecraft.nbt.ListTag;

import appeng.api.stacks.GenericStack;
import com.google.common.base.Preconditions;
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
        ListTag tag = new ListTag();
        boolean foundStack = false;
        for (var stack : stacks) {
            if (stack == null || stack.amount() == 0) continue;
            tag.add(GenericStack.writeTag(stack));
            foundStack = true;
        }
        Preconditions.checkArgument(foundStack, "List passed to pattern must contain at least one stack.");
        return tag;
    }
}
