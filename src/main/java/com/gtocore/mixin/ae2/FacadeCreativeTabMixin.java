package com.gtocore.mixin.ae2;

import net.minecraft.core.Registry;
import net.minecraft.world.item.CreativeModeTab;

import appeng.core.FacadeCreativeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(FacadeCreativeTab.class)
public final class FacadeCreativeTabMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static void init(Registry<CreativeModeTab> registry) {}
}
