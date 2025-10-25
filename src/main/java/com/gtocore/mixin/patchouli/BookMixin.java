package com.gtocore.mixin.patchouli;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import vazkii.patchouli.common.book.Book;

@Mixin(Book.class)
public class BookMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void reloadLocks(boolean suppressToasts) {}
}
