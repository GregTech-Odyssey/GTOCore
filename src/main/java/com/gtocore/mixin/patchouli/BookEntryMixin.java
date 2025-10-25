package com.gtocore.mixin.patchouli;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import vazkii.patchouli.client.book.BookEntry;

@Mixin(BookEntry.class)
public class BookEntryMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public boolean isLocked() {
        return false;
    }
}
