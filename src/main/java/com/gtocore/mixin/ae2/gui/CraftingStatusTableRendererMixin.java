package com.gtocore.mixin.ae2.gui;

import com.gtocore.integration.ae.hooks.IPushResultsHandler;

import com.gtolib.api.ae2.IPatternProviderLogic;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.me.crafting.AbstractTableRenderer;
import appeng.client.gui.me.crafting.CraftingStatusTableRenderer;
import appeng.core.localization.GuiText;
import appeng.menu.me.crafting.CraftingStatusEntry;
import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Mixin(CraftingStatusTableRenderer.class)
public abstract class CraftingStatusTableRendererMixin extends AbstractTableRenderer<CraftingStatusEntry> {

    public CraftingStatusTableRendererMixin(AEBaseScreen<?> screen, int x, int y, int rows) {
        super(screen, x, y, rows);
    }

    @Redirect(remap = false,
              method = "getEntryDescription(Lappeng/menu/me/crafting/CraftingStatusEntry;)Ljava/util/List;",
              at = @At(value = "INVOKE", target = "Lappeng/core/localization/GuiText;text([Ljava/lang/Object;)Lnet/minecraft/network/chat/MutableComponent;"))
    private MutableComponent gto$modifyGetEntryDescription(GuiText instance, Object[] objects, @Local(argsOnly = true) CraftingStatusEntry entry) {
        if (screen.getMenu() instanceof IPushResultsHandler handler) {
            var lastResults = handler.gto$getLastCraftingResults();
            if (lastResults != null &&
                    lastResults.containsKey(entry.getWhat()) && lastResults.get(entry.getWhat()).stream().filter(Objects::nonNull).anyMatch(r -> !r.success() && r != IPatternProviderLogic.PushResult.NOWHERE_TO_PUSH)) {
                return instance.text(objects).append(Component.literal("⚠").withStyle(style -> style.withColor(0xFF5555)));
            }
        }
        return instance.text(objects);
    }

    @Inject(remap = false, method = "getEntryTooltip(Lappeng/menu/me/crafting/CraftingStatusEntry;)Ljava/util/List;", at = @At("RETURN"), cancellable = true)
    private void gto$modifyGetEntryTooltip(CraftingStatusEntry entry, CallbackInfoReturnable<List<Component>> cir) {
        if (screen.getMenu() instanceof IPushResultsHandler handler) {
            var lastResults = handler.gto$getLastCraftingResults();
            if (lastResults != null &&
                    lastResults.containsKey(entry.getWhat())) {
                Set<IPatternProviderLogic.PushResult> results = new ObjectOpenHashSet<>(lastResults.get(entry.getWhat()));
                var v = cir.getReturnValue();
                v.addAll(results.stream().filter(Objects::nonNull).map(r -> Component.translatable(r.getTranslationKey()).withStyle(r.success() ? ChatFormatting.GREEN : ChatFormatting.GOLD)).toList());
                cir.setReturnValue(v);
            }
        }
    }
}
