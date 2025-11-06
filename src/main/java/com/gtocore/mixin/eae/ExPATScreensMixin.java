package com.gtocore.mixin.eae;

import com.glodblock.github.extendedae.client.gui.GuiWirelessExPAT;
import com.glodblock.github.extendedae.xmod.wt.GuiUWirelessExPAT;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ GuiUWirelessExPAT.class, GuiWirelessExPAT.class })
public class ExPATScreensMixin {

    @Redirect(
              method = "<init>",
              at = @At(
                       value = "INVOKE",
                       target = "Lappeng/menu/ToolboxMenu;isPresent()Z",
                       remap = false),
              remap = false)
    private boolean redirectIsToolboxMenu(appeng.menu.ToolboxMenu instance) {
        return false;
    }
}
