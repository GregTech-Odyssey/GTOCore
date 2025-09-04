package com.gtocore.mixin.ae2.gui;

import com.gtolib.utils.NumberUtils;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;

import appeng.core.localization.Tooltips;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Tooltips.class)
public final class TooltipsMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static MutableComponent ofBytes(long number) {
        return NumberUtils.numberText(number).withStyle(ChatFormatting.BLUE);
    }
}
