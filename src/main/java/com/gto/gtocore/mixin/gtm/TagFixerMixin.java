package com.gto.gtocore.mixin.gtm;

import com.gregtechceu.gtceu.common.datafixers.TagFixer;

import net.minecraft.nbt.CompoundTag;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(TagFixer.class)
public class TagFixerMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static void fixFluidTags(CompoundTag tag) {}
}
