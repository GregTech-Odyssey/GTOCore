package com.gtocore.mixin.ae2.eae;

import appeng.api.implementations.blockentities.PatternContainerGroup;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "com.glodblock.github.extendedae.client.gui.GuiExPatternTerminal$GroupHeaderRow", remap = false)
public interface GuiExPatternTerminalGroupHeaderAccessor {

    @Accessor("group")
    PatternContainerGroup gto$getGroup();
}
