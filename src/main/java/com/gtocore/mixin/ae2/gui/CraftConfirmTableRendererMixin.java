package com.gtocore.mixin.ae2.gui;

import com.gtocore.integration.ae.IConfirmLongMenu;

import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import appeng.api.stacks.KeyCounter;
import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.me.crafting.AbstractTableRenderer;
import appeng.client.gui.me.crafting.CraftConfirmTableRenderer;
import appeng.menu.me.common.IClientRepo;
import appeng.menu.me.crafting.CraftingPlanSummaryEntry;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(CraftConfirmTableRenderer.class)
public abstract class CraftConfirmTableRendererMixin extends AbstractTableRenderer<CraftingPlanSummaryEntry> {

    public CraftConfirmTableRendererMixin(AEBaseScreen<?> screen, int x, int y, int rows) {
        super(screen, x, y, rows);
    }

    @Nullable
    @Unique
    KeyCounter gto$cachedKeys = null;

    @Inject(method = "getEntryDescription(Lappeng/menu/me/crafting/CraftingPlanSummaryEntry;)Ljava/util/List;",
            remap = false,
            at = @At(
                     value = "FIELD",
                     target = "Lappeng/core/localization/GuiText;FromStorage:Lappeng/core/localization/GuiText;",
                     shift = At.Shift.AFTER,
                     by = 3))
    private void gto$modifyGetEntryDescription(CraftingPlanSummaryEntry entry, CallbackInfoReturnable<List<Component>> cir, @Local List<Component> lines) {
        if (gto$cachedKeys == null) return;
        long storedTotal = gto$cachedKeys.get(entry.getWhat());
        if (storedTotal <= 0) return;
        float storedPercent = Math.min((float) entry.getStoredAmount() / storedTotal, 1.0f);
        ChatFormatting color = storedPercent < 0.25f ? ChatFormatting.DARK_GREEN :
                storedPercent < 0.5f ? ChatFormatting.GOLD :
                        storedPercent < 0.75f ? ChatFormatting.RED : ChatFormatting.DARK_RED;
        lines.add(Component.translatable("gtocore.ae.appeng.craft.used_percent", FormattingUtil.formatNumber2Places(storedPercent * 100)).withStyle(color));
    }

    @WrapMethod(method = "getEntryDescription(Lappeng/menu/me/crafting/CraftingPlanSummaryEntry;)Ljava/util/List;", remap = false)
    private List<Component> gto$modifyGetEntryDescription2(CraftingPlanSummaryEntry entry, Operation<List<Component>> original) {
        IClientRepo repo = (screen.getMenu()) instanceof IConfirmLongMenu.IConfirmLongStartMenu me ? me.gtocore$getClientRepo() : null;
        if (repo == null) return original.call(entry);
        gto$cachedKeys = new KeyCounter();
        repo.getAllEntries().stream().filter(e -> e.getWhat() != null && e.getStoredAmount() > 0)
                .forEach(e -> gto$cachedKeys.add(e.getWhat(), e.getStoredAmount()));
        List<Component> lines = original.call(entry);
        gto$cachedKeys = null;
        return lines;
    }
}
