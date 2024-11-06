package com.gto.gtocore.mixin.ae2;

import com.gto.gtocore.utils.NumberUtils;

import appeng.client.gui.ICompositeWidget;
import appeng.client.gui.widgets.CPUSelectionList;
import appeng.menu.me.crafting.CraftingStatusMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CPUSelectionList.class)
public abstract class CPUSelectionListMixin implements ICompositeWidget {

    @Inject(method = "formatStorage", at = @At("HEAD"), remap = false, cancellable = true)
    private void formatStorage(CraftingStatusMenu.CraftingCpuListEntry cpu, CallbackInfoReturnable<String> cir) {
        cir.setReturnValue(NumberUtils.numberText(cpu.storage()).getString());
    }
}
