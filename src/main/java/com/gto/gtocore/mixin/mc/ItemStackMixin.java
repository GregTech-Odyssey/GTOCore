package com.gto.gtocore.mixin.mc;

import com.gto.gtocore.client.Tooltips;
import com.gto.gtocore.common.block.WirelessEnergyUnitBlock;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.item.MetaMachineItem;
import com.gregtechceu.gtceu.common.block.CoilBlock;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = ItemStack.class, priority = 0)
public abstract class ItemStackMixin {

    @Shadow
    public abstract Item getItem();

    @Unique
    private List<Component> TOOLTIP_CACHE;

    @Unique
    private boolean gtocore$isTickTooltip;

    @Unique
    private boolean gtocore$cacheTick;

    @Inject(method = "getTooltipLines", at = @At("TAIL"), cancellable = true)
    private void tooltipCache(Player player, TooltipFlag isAdvanced, CallbackInfoReturnable<List<Component>> cir, @Local List<Component> list) {
        if (gtocore$isTickTooltip() || GTValues.CLIENT_TIME == 0) return;
        TOOLTIP_CACHE = list;
        cir.setReturnValue(TOOLTIP_CACHE);
    }

    @Inject(method = "getTooltipLines", at = @At("HEAD"), cancellable = true)
    private void getTooltipLines(Player player, TooltipFlag isAdvanced, CallbackInfoReturnable<List<Component>> cir) {
        if (GTValues.CLIENT_TIME % 20 == 0 || TOOLTIP_CACHE == null || gtocore$isTickTooltip()) return;
        cir.setReturnValue(List.copyOf(TOOLTIP_CACHE));
    }

    @Unique
    private boolean gtocore$isTickTooltip() {
        if (!gtocore$cacheTick) {
            gtocore$cacheTick = true;
            if (getItem() instanceof TieredItem) {
                gtocore$isTickTooltip = true;
            } else if (getItem() instanceof BlockItem blockItem) {
                if (blockItem instanceof MetaMachineItem) {
                    gtocore$isTickTooltip = true;
                } else {
                    Block block = blockItem.getBlock();
                    if (block instanceof CoilBlock || block instanceof WirelessEnergyUnitBlock) {
                        gtocore$isTickTooltip = true;
                    }
                }
            } else {
                gtocore$isTickTooltip = Tooltips.FLICKER_TOOL_TIPS_MAP.containsKey(getItem());
            }
        }
        return gtocore$isTickTooltip;
    }
}
