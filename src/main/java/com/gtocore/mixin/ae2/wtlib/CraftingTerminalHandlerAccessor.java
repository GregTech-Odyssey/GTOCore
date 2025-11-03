package com.gtocore.mixin.ae2.wtlib;

import de.mari_023.ae2wtlib.terminal.WTMenuHost;
import org.spongepowered.asm.mixin.gen.Accessor;

@org.spongepowered.asm.mixin.Mixin(de.mari_023.ae2wtlib.wct.CraftingTerminalHandler.class)
public interface CraftingTerminalHandlerAccessor {

    @Accessor(remap = false, value = "menuHost")
    WTMenuHost gto$getMenuHost();
}
