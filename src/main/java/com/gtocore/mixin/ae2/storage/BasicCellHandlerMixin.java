package com.gtocore.mixin.ae2.storage;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import appeng.me.cells.BasicCellHandler;
import appeng.me.cells.BasicCellInventory;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(BasicCellHandler.class)
public class BasicCellHandlerMixin {

    @Redirect(method = "addCellInformationToTooltip", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 1), remap = false)
    private boolean addCellInformationToTooltip(List<Component> instance, Object e, @Local BasicCellInventory inventory) {
        return instance.add(Component.literal(String.valueOf(inventory.getStoredItemTypes())).withStyle(ChatFormatting.AQUA).append(Component.literal(" ").append(Component.translatable("gui.ae2.Types").withStyle(ChatFormatting.GRAY))));
    }
}
