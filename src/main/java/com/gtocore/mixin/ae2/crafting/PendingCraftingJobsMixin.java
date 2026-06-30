package com.gtocore.mixin.ae2.crafting;

import com.gtocore.config.GTOConfig;
import com.gtocore.utils.NotificationUtils;

import com.gtolib.utils.GTOUtils;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;

import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.client.gui.me.common.PendingCraftingJobs;
import appeng.core.localization.GuiText;
import appeng.core.sync.packets.CraftingJobStatusPacket;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(PendingCraftingJobs.class)
public class PendingCraftingJobsMixin {

    @Inject(method = "jobStatus", at = @At(value = "INVOKE", target = "Lappeng/core/AEConfig;isNotifyForFinishedCraftingJobs()Z"), remap = false)
    private static void notify(UUID id, AEKey what, long requestedAmount, long remainingAmount, CraftingJobStatusPacket.Status status, CallbackInfo ci) {
        if (!GTOConfig.INSTANCE.client.craftingJobFinishedNotification || Minecraft.getInstance().isWindowActive()) return;
        GTOUtils.asyncExecute(() -> NotificationUtils.notify(
                GuiText.ToastCraftingJobFinishedTitle.text().getString(),
                null,
                ChatFormatting.stripFormatting(what.getDisplayName().getString()),
                NotificationUtils.Type.INFO,
                new GenericStack(what, requestedAmount)));
    }
}
